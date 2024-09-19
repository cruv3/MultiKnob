package controler.multiknobcontroller

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import controler.multiknobcontroller.studie.MapActivity
import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.GlobalState


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MAINACTIVITY_LOGS"
    }

    private var locationAllowed = false
    private var bluetoothAllowed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity started successfully")
        GlobalState.resetGlobalState()

        // Start by checking location permission first
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (PermissionUtils.hasLocationPermissions(this)) {
            locationAllowed = true
            checkBluetoothPermission() // Once location permission is granted, check Bluetooth
        } else {
            PermissionUtils.requestLocationPermissions(this, PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkBluetoothPermission() {
        if (PermissionUtils.hasBluetoothPermissions(this)) {
            bluetoothAllowed = true
            // Now that both permissions are granted, ask user choice
            askUserChoice()
        } else {
            PermissionUtils.requestBluetoothPermissions(this, PermissionUtils.BLUETOOTH_PERMISSION_REQUEST_CODE)
        }
    }

    // Handle permissions result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationAllowed = true
                    // After location permission is granted, check Bluetooth permission
                    checkBluetoothPermission()
                }
            }
            PermissionUtils.BLUETOOTH_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bluetoothAllowed = true
                    // Now that both permissions are granted, ask user choice
                    askUserChoice()
                }
            }
        }
    }

    // Function to ask user to choose between Studie and Proof of Concept
    private fun askUserChoice() {
        Log.d(TAG, "HERE")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Mode")
        builder.setMessage("Please choose a mode to proceed:")

        // Add options for Studie and Proof of Concept
        builder.setPositiveButton("Studie") { dialog, _ ->
            startMapActivity()
            dialog.dismiss()
        }

        builder.setNegativeButton("Proof of Concept") { dialog, _ ->
            startAccessibilityService()
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Function to start Accessibility Service
    private fun startAccessibilityService() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    // Function to start the MapActivity
    private fun startMapActivity() {
        Log.d(TAG, "Launching MapActivity")
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }
}

