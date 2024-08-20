package controler.multiknobcontroller.studie.controls

import com.mapbox.geojson.Point
import controler.multiknobcontroller.studie.MapHandler
import controler.multiknobcontroller.utils.delayFunction


class StudieController(private val mapHandler: MapHandler) {
    companion object{
        const val TAG = "StudieController"
        private const val Navigation_DELAY = 200L
        private const val ZOOM_CHANGE = .1
        private const val MOVE_DELTA = 0.01
    }

    fun nextLeftCity() {
        delayFunction<Unit>(Navigation_DELAY, TAG, "Navigate to Left City") {
            mapHandler.goToLeftCity()
        }.invoke(Unit)
    }

    fun nextRightCity() {
        delayFunction<Unit>(Navigation_DELAY, TAG, "Navigate to Right City") {
            mapHandler.goToRightCity()
        }.invoke(Unit)
    }

    fun goUp() {
        delayFunction<Unit>(Navigation_DELAY, TAG,"Navigate Up"){
            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude(), mapHandler.currentCenter.latitude() + MOVE_DELTA))
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }

    fun goDown() {
        delayFunction<Unit>(Navigation_DELAY, TAG, "Navigate Down") {
            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude(), mapHandler.currentCenter.latitude() - MOVE_DELTA))
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }

    fun goLeft() {
        delayFunction<Unit>(Navigation_DELAY,TAG,"Navigate Left") {
            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude() - MOVE_DELTA, mapHandler.currentCenter.latitude()))
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }

    fun goRight() {
        delayFunction<Unit>(Navigation_DELAY,TAG,"Navigate Right") {
            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude() + MOVE_DELTA, mapHandler.currentCenter.latitude()))
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }

    fun zoomIn(){
        delayFunction<Unit>(Navigation_DELAY,TAG,"Zoom In") {
            mapHandler.setZoom(mapHandler.currentZoom + ZOOM_CHANGE)
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }

    fun zoomOut(){
        delayFunction<Unit>(Navigation_DELAY,TAG,"Zoom Out") {
            mapHandler.setZoom(mapHandler.currentZoom - ZOOM_CHANGE)
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }
}
