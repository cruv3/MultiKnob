package controler.multiknobcontroller.studie

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.RenderedQueryOptions
import com.mapbox.maps.ScreenBox
import com.mapbox.maps.ScreenCoordinate
import controler.multiknobcontroller.utils.entities.CityWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("ClickableViewAccessibility")
class MapHandler(private val mapView: MapView, private val rootLayout: FrameLayout, private val analysisHandler: AnalysisHandler) {
    companion object{
        private const val TAG = "MapHandler"
        private const val RETRY_DELAY_MS = 500L
        private const val SCREEN_MARGIN = 100.0
    }


    private var previousDistance = 0f
    private var startX = 0f
    private var startY = 0f
    private val DRAG_THRESHOLD = 10

    private var analysisJob: Job? = null
    private var analysisActive: Boolean = false

    val mapViewContext = mapView.context
    var currentZoom: Double = 10.0
    var currentCenter: Point = Point.fromLngLat(0.0, 0.0)

    var cities = mutableListOf<MutableList<CityWrapper>>()
    var currentCity: CityWrapper? = null

    init {
        mapView.mapboxMap.subscribeCameraChanged {
            Log.d(TAG, "Camera Changed")
            val cameraPosition = mapView.mapboxMap.cameraState
            currentCenter = cameraPosition.center
            currentZoom = cameraPosition.zoom
            queryVisibleCitiesWithRetry()
        }

        mapView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Initiale Touch-Position erfassen
                    startX = event.x
                    startY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    if (event.classification == MotionEvent.CLASSIFICATION_PINCH){
                        Log.d(TAG, "Pinch erkannt")

                        val currentZoom = mapView.mapboxMap.cameraState.zoom

                        // Berechne den neuen Zoom-Wert basierend auf der Fingerbewegung
                        val distance = calculateFingerSpacing(event)
                        val newZoom = if (distance > previousDistance) {
                            currentZoom + 0.1 // Zoom-In
                        } else {
                            currentZoom - 0.1 // Zoom-Out
                        }

                        // Setze den neuen Zoom-Wert
                        val cameraOptions = CameraOptions.Builder()
                            .zoom(newZoom.coerceIn(2.0, 20.0)) // Zoom-Level begrenzen
                            .build()
                        mapView.mapboxMap.setCamera(cameraOptions)

                        previousDistance = distance
                    }

                    // Bewegungsentfernung berechnen
                    val deltaX = Math.abs(event.x - startX)
                    val deltaY = Math.abs(event.y - startY)

                    // Überprüfen, ob die Bewegung größer als der Schwellenwert ist
                    if (deltaX > DRAG_THRESHOLD || deltaY > DRAG_THRESHOLD) {
                        analysisHandler.startAnalysis()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // Gesamtbewegung berechnen
                    val deltaX = Math.abs(event.x - startX)
                    val deltaY = Math.abs(event.y - startY)

                    // Wenn die Bewegung geringer ist als der Schwellenwert, als Klick behandeln
                    if (deltaX < DRAG_THRESHOLD && deltaY < DRAG_THRESHOLD) {
                        analysisHandler.pauseTime()
                        var city = currentCity?.name.toString()
                        if(currentCity == null) {
                            city = getPositionName().toString()
                        }
                        analysisHandler.stopAnalysis(city)
                        val message = "You clicked on $city."

                        val dialog = AlertDialog.Builder(mapViewContext)
                            .setTitle("Confirm Location")
                            .setMessage(message)
                            .setPositiveButton("Ok") { _, _ ->
                            }
                            .create()

                        dialog.show()
                    }
                }
            }
            mapView.onTouchEvent(event)
            false // Touch-Ereignis wird vollständig verarbeitet
        }
    }

    private fun calculateFingerSpacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    fun stopAnalysis(city: String) {
        if(ProbandManager.getTest()) return
        if(!analysisActive) return

        analysisJob?.cancel()
        analysisHandler.stopAnalysis(city)
        analysisActive = false
    }

    fun startAnalysis() {
        if(ProbandManager.getTest()) return
        if(analysisActive) return

        analysisJob = GlobalScope.launch(Dispatchers.Main) {
            analysisHandler.startAnalysis()
        }
        analysisActive = true
    }

    fun setZoom(value: Double){
        currentZoom = value
    }

    fun setCenter(value: Point){
        currentCenter = value
    }

    fun setCameraPosition() {
        val position = CameraOptions.Builder()
            .center(currentCenter)
            .zoom(currentZoom)
            .build()
        mapView.mapboxMap.setCamera(position)
    }

    private fun queryVisibleCitiesWithRetry(retryCount: Int = 0) {
        queryVisibleCities { success ->
            if (!success && retryCount < 5) { // Retry up to 5 times
                Log.d(TAG, "Retrying... (Attempt: $retryCount)")
                mapView.postDelayed({ queryVisibleCitiesWithRetry(retryCount + 1) }, RETRY_DELAY_MS)
            }
        }
    }

    // Function to query cities and execute a callback with success status
    private fun queryVisibleCities(callback: (Boolean) -> Unit) {
        cities.clear()
        HandleCities.removeCityOverlays(rootLayout)
        val screenBox = ScreenBox(
            ScreenCoordinate(0.0, SCREEN_MARGIN),  // top-left corner of the screen with margin
            ScreenCoordinate(mapView.width.toDouble(), mapView.height.toDouble() - SCREEN_MARGIN)  // bottom-right corner of the screen with margin
        )

        val layersToQuery = listOf(
            "settlement-major-label",
            "settlement-minor-label",
            "country-label"
        )

        mapView.mapboxMap.queryRenderedFeatures(
            RenderedQueryGeometry(screenBox),
            RenderedQueryOptions(layersToQuery, null)
        ) { expected ->
            expected.value?.let { features ->
                if (features.isNotEmpty()) {
                    val cityList = mutableListOf<CityWrapper>()
                    features.forEach { feature ->
                        feature.queriedFeature.feature.geometry()?.let { geometry ->
                            val cityPoint = (geometry as? Point)
                            val cityName = feature.queriedFeature.feature.getStringProperty("name")
                            cityPoint?.let {
                                val screenCoordinate = mapView.mapboxMap.pixelForCoordinate(cityPoint)
                                cityList.add(CityWrapper(cityName, it, screenCoordinate, null, null, null, false, null))
                            }
                        }
                    }
                    val centerCoordinate = mapView.mapboxMap.pixelForCoordinate(currentCenter)
                    cities = HandleCities.setupCities(mapView, cityList, centerCoordinate, rootLayout, mapView.context)
                    currentCity = HandleCities.highlitedCity(cities)
                    if (cities.isNotEmpty()) {
                        callback(true)  // Success
                    } else {
                        callback(false) // Cities still empty
                    }
                } else {
                    Log.e(TAG, "Query failed: ${expected.error}")
                    callback(false) // Query failed or no cities found
                }
            } ?: run {
                Log.e(TAG, "Query failed: ${expected.error}")
                callback(false) // Query failed or no cities found
            }
        }
    }

    fun goToRightCity(){
        val centerCoordinate = mapView.mapboxMap.pixelForCoordinate(currentCenter)
        val city = HandleCities.getRightNextCity(cities, centerCoordinate ) ?: return
        setCenter(city.point)
        setCameraPosition()
    }

    fun goToLeftCity(){
        val centerCoordinate = mapView.mapboxMap.pixelForCoordinate(currentCenter)
        val city = HandleCities.getLeftNextCity(cities, centerCoordinate ) ?: return
        setCenter(city.point)
        setCameraPosition()
    }

    fun getPositionName() : String? {
        val centerCoordinate = mapView.mapboxMap.pixelForCoordinate(currentCenter)
        var name : String? = null
        mapView.mapboxMap.queryRenderedFeatures(
            RenderedQueryGeometry(ScreenCoordinate(centerCoordinate.x, centerCoordinate.y)),
            RenderedQueryOptions(null, null) // You can specify layer IDs or filter options here
        ) { expected ->
            expected.value?.let { features ->
                if (features.isNotEmpty()) {
                    val feature = features.first()
                    name = feature.queriedFeature.feature.getStringProperty("name")
                } else {
                    Log.d(TAG, "No features found at the center position.")
                }
            } ?: run {
                Log.e(TAG, "Query failed: ${expected.error}")
            }
        }
        return name
    }

    fun goToUpCity(){
        val centerCoordinate = mapView.mapboxMap.pixelForCoordinate(currentCenter)
        val city = HandleCities.getUpNextCity(cities, centerCoordinate) ?: return
        setCenter(city.point)
        setCameraPosition()
    }

    fun goToDownCity(){
        val centerCoordinate = mapView.mapboxMap.pixelForCoordinate(currentCenter)
        val city = HandleCities.getDownNextCity(cities, centerCoordinate) ?: return
        setCenter(city.point)
        setCameraPosition()
    }


}