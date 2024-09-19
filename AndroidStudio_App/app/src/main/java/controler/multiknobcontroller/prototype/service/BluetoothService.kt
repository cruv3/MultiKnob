package controler.multiknobcontroller.prototype.service

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import controler.multiknobcontroller.utils.entities.FingerData
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService(private val context: Context, private val callback: BluetoothDataCallback) : SignalProcessor.SignalCallback {
    companion object{
        private val TAG = "BluetoothService"
    }

    interface BluetoothDataCallback {
        fun onSignalReceived(signal: Signal.GlobalSignal)
        fun onSignalReceived(signal: Signal.GlobalSignal.ButtonSignal)
        fun onSignalReceived(signal: Signal.GoogleMaps)
        fun onSignalReceived(signal: Signal.GoogleMaps.ButtonSignal)
    }

    private val signalProcessor = SignalProcessor(context, this)

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var deviceAddress: String? = null

    private val handler = Handler(Looper.getMainLooper())
    @Volatile private var listeningThread: Thread? = null

    @SuppressLint("MissingPermission")
    fun startBluetoothScan(deviceAddress: String) {
        Log.d(TAG, "Start bluetooth Scan")
        this.deviceAddress = deviceAddress
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        pairedDevices?.forEach { device ->
            if (deviceAddress == device.address){
                connectToDevice(device)
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard UUID for SPP

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            inputStream = bluetoothSocket?.inputStream
            outputStream = bluetoothSocket?.outputStream
            Log.d(TAG, "Connected to device")
            startListeningForData()
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to device", e)
            retryConnection(device)
        }
    }

    private fun retryConnection(device: BluetoothDevice) {
        handler.postDelayed({
            Log.d(TAG, "Retrying connection...")
            connectToDevice(device)
        }, 1000) // Retry every 1 second
    }

    private fun startListeningForData() {
        stopListeningThread() // Stop any existing thread before starting a new one
        listeningThread = Thread {
            listenForData()
        }
        listeningThread?.start()
    }

    fun stopListeningThread() {
        listeningThread?.interrupt()
        listeningThread = null
    }

    private fun listenForData() {
        Log.d(TAG, "Listening for incoming data")
        val buffer = ByteArray(1024)
        var bytes: Int

        while (!Thread.currentThread().isInterrupted) {
            try {
                bytes = inputStream?.read(buffer) ?: break
                val data = String(buffer, 0, bytes)
                handler.post {
                    val multiKnobData = parseAndHandleData(data)
                    Log.d(TAG, multiKnobData.toString())
                    signalProcessor.processMultiKnobData(multiKnobData)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error reading data", e)
                startBluetoothScan(deviceAddress ?: "")
                break
            }
        }
    }

    private fun parseAndHandleData(data: String) : MultiKnob {
        try {
            val parts = data.split(";")
            if (parts.size < 5) {
                Log.e(TAG, "Invalid data format")
                return MultiKnob(0f,0,0,0, emptyList(),0)
            }

            val rotation = parts[0].substring(1).toFloat() // Remove '#' and convert to float
            val snapPoint = parts[1].toInt()
            val buttonPress = parts[2].toInt()
            val fingerCount = parts[3].toInt()

            val fingers = mutableListOf<FingerData>()
            var index = 4
            for (i in 0 until fingerCount) {
                val fingerParts = parts[index].split(",")
                if (fingerParts.size < 3) {
                    Log.e(TAG, "Invalid finger data format")
                    return MultiKnob(0f,0,0,0, emptyList(),0)
                }

                val position = fingerParts[0].toFloat()
                val pressure = fingerParts[1].toInt()
                val channelCount = fingerParts[2].toInt()

                fingers.add(FingerData(position, pressure, channelCount))
                index++
            }

            val characterCount = parts.last().toInt()
            return MultiKnob(rotation, snapPoint, buttonPress, fingerCount, fingers, characterCount)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing data: ${e.message}")
        }
        return MultiKnob(0f,0,0,0, emptyList<FingerData>(),0)
    }

    fun sendBluetoothMessage(message: String) {
        val formattedMessage = message + "\n" // Adding a newline to signify the end of the message, if needed
        try {
            outputStream?.write(formattedMessage.toByteArray())
            outputStream?.flush() // Ensure message is sent immediately
            Log.d(TAG, "Message sent: $message")
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
        }
    }

    override fun onSignalReceived(signal: Signal.GlobalSignal) {
        callback.onSignalReceived(signal)
    }

    override fun onSignalReceived(signal: Signal.GlobalSignal.ButtonSignal) {
        callback.onSignalReceived(signal)
    }

    override fun onSignalReceived(signal: Signal.GoogleMaps) {
        callback.onSignalReceived(signal)
    }

    override fun onSignalReceived(signal: Signal.GoogleMaps.ButtonSignal) {
        callback.onSignalReceived(signal)
    }
}