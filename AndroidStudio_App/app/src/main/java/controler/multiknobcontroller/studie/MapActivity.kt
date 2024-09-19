package controler.multiknobcontroller.studie

import PermissionUtils
import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.RenderFrameFinished
import com.mapbox.maps.Style
import com.mapbox.maps.coroutine.mapLoadedEvents
import com.mapbox.maps.extension.observable.eventdata.RenderFrameFinishedEventData
import com.mapbox.maps.plugin.gestures.gestures
import controler.multiknobcontroller.R
import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.GlobalState
import controler.multiknobcontroller.studie.controls.StudieController
import controler.multiknobcontroller.utils.entities.StudieSignal

class MapActivity : AppCompatActivity(), BluetoothService.BluetoothDataCallback{
    companion object{
        private const val TAG = "MapActivity"
    }

    private lateinit var bluetoothService: BluetoothService


    private lateinit var mapView: MapView
    private lateinit var rootLayout: FrameLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var controller: StudieController
    private lateinit var mapHandler: MapHandler

    private lateinit var commandReceiver: CommandReceiver
    private lateinit var analysisHandler: AnalysisHandler

    private lateinit var testLabel: TextView

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        val filter = IntentFilter("controler.multiknobcontroller.studie.proband")

        super.onCreate(savedInstanceState)
        GlobalState.currentApp = AppPackage.MULITKNOB
        bluetoothService = BluetoothService(this, this)
        bluetoothService.startBluetoothScan("64:B7:08:29:37:8E")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_map)
        rootLayout = findViewById(R.id.root_layout)
        mapView = findViewById(R.id.mapView)
        testLabel = findViewById(R.id.testLabel)
        commandReceiver = CommandReceiver(testLabel)
        registerReceiver(commandReceiver, filter)
        analysisHandler = AnalysisHandler(this, "studie.csv")

        mapView.mapboxMap.loadStyleUri(Style.MAPBOX_STREETS){ style ->
            if(!PermissionUtils.hasLocationPermissions(this)) return@loadStyleUri
            style.styleLayers.forEach { layer ->
                Log.d(TAG, "Layer id: ${layer.id}")
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if(location == null) return@addOnSuccessListener
                val center = Point.fromLngLat(location.longitude, location.latitude)
                val zoom = 10.0
                val position = CameraOptions.Builder()
                    .center(center)
                    .zoom(zoom) // Zoom level
                    .build()
                mapView.mapboxMap.setCamera(position)
                mapView.mapboxMap.mapLoadedEvents

                mapHandler.setZoom(zoom)
                mapHandler.setCenter(center)
            }
            mapView.gestures.scrollEnabled = true
            mapView.gestures.pinchToZoomEnabled = true
            mapHandler = MapHandler(mapView, rootLayout, analysisHandler)
            controller = StudieController(mapHandler, analysisHandler)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onSignalReceived(signal: StudieSignal) {
        when (signal) {
            StudieSignal.ZOOM_IN -> controller.zoomIn()
            StudieSignal.ZOOM_OUT -> controller.zoomOut()
            StudieSignal.LEFT -> controller.goLeft()
            StudieSignal.RIGHT -> controller.goRight()
            StudieSignal.UP -> controller.goUp()
            StudieSignal.DOWN -> controller.goDown()
            StudieSignal.GO_BACK -> Log.d(TAG, "GO BACK (TODO)")
            StudieSignal.LEFT_NEXT_CITY -> controller.nextLeftCity()
            StudieSignal.RIGHT_NEXT_CITY -> controller.nextRightCity()
            StudieSignal.RIGHT_UP_CITY -> controller.nextUpCity()
            StudieSignal.LEFT_DOWN_CITY -> controller.nextDownCity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onSignalReceived(signal: StudieSignal.ButtonSignal) {
        when (signal) {
            StudieSignal.ButtonSignal.SHORT_TAP -> controller.clickOnCenter()
            StudieSignal.ButtonSignal.LONG_PRESS -> Log.d(TAG, "not implemented")
            StudieSignal.ButtonSignal.DOUBLE_TAP -> Log.d(TAG, "not implemented")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(commandReceiver)
    }
}