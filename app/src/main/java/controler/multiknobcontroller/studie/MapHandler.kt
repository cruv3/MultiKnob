package controler.multiknobcontroller.studie

import android.util.Log
import android.widget.FrameLayout
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.RenderedQueryOptions
import com.mapbox.maps.ScreenBox
import com.mapbox.maps.ScreenCoordinate
import controler.multiknobcontroller.utils.entities.CityWrapper

class MapHandler(private val mapView: MapView, private val rootLayout: FrameLayout) {
    companion object{
        private const val TAG = "MapHandler"
        private const val RETRY_DELAY_MS = 500L
        private const val SCREEN_MARGIN = 100.0
    }

    var currentZoom: Double = 10.0
    var currentCenter: Point = Point.fromLngLat(0.0, 0.0)

    var cities = mutableListOf<MutableList<CityWrapper>>()

    init {
        mapView.mapboxMap.subscribeCameraChanged {
            Log.d(TAG, "Camera Changed")
            val cameraPosition = mapView.mapboxMap.cameraState
            currentCenter = cameraPosition.center
            currentZoom = cameraPosition.zoom
            queryVisibleCitiesWithRetry()
        }
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
}