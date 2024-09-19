package controler.multiknobcontroller.studie.controls

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.mapbox.geojson.Point
import controler.multiknobcontroller.studie.AnalysisHandler
import controler.multiknobcontroller.studie.MapHandler
import controler.multiknobcontroller.studie.ProbandManager
import controler.multiknobcontroller.utils.delayFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
class StudieController(private val mapHandler: MapHandler, private val analysisHandler: AnalysisHandler) {
    companion object{
        const val TAG = "StudieController"
        private const val Navigation_DELAY = 200L
        private const val ZOOM_CHANGE = .3
        private var MOVE_DELTA = 0.02
    }

    private fun calculateMoveDelta() : Double{
        val zoomFactor = if (mapHandler.currentZoom > 0) mapHandler.currentZoom else 1.0 // avoid division by zero
        val move = MOVE_DELTA * Math.pow(2.0, (10 - zoomFactor)) // exponential scaling to increase speed when zoomed out
        return move
    }

    private fun showConfirmationDialog() {
        var city = mapHandler.currentCity?.name.toString()
        if(mapHandler.currentCity == null) {
            city = mapHandler.getPositionName().toString()
        }

        val message = "You clicked on $city."

        val dialog = AlertDialog.Builder(mapHandler.mapViewContext)
            .setTitle("Confirm Location")
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                // User confirmed the click
                mapHandler.stopAnalysis(city)
            }
            .create()

        dialog.show()
    }
    fun nextLeftCity() {
        mapHandler.startAnalysis()
        delayFunction<Unit>(Navigation_DELAY, TAG, "Navigate to Left City") {
            mapHandler.goToLeftCity()
        }.invoke(Unit)
    }

    fun nextRightCity() {
        mapHandler.startAnalysis()
        delayFunction<Unit>(Navigation_DELAY, TAG, "Navigate to Right City") {
            mapHandler.goToRightCity()
        }.invoke(Unit)
    }

    fun goUp() {
        mapHandler.startAnalysis()
        delayFunction<Unit>(Navigation_DELAY, TAG,"Navigate Up"){
            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude(), mapHandler.currentCenter.latitude() + calculateMoveDelta()))
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }

    fun goDown() {
        mapHandler.startAnalysis()
        delayFunction<Unit>(Navigation_DELAY, TAG, "Navigate Down") {
            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude(), mapHandler.currentCenter.latitude() - calculateMoveDelta()))
            mapHandler.setCameraPosition()
        }.invoke(Unit)
    }

    fun goLeft() {
        mapHandler.startAnalysis()
//        delayFunction<Unit>(Navigation_DELAY,TAG,"Navigate Left") {
//            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude() - MOVE_DELTA, mapHandler.currentCenter.latitude()))
//            mapHandler.setCameraPosition()
//        }.invoke(Unit)
        mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude() - calculateMoveDelta(), mapHandler.currentCenter.latitude()))
        mapHandler.setCameraPosition()
    }

    fun goRight() {
        mapHandler.startAnalysis()
//        delayFunction<Unit>(Navigation_DELAY,TAG,"Navigate Right") {
//            mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude() + MOVE_DELTA, mapHandler.currentCenter.latitude()))
//            mapHandler.setCameraPosition()
//        }.invoke(Unit)
        mapHandler.setCenter(Point.fromLngLat(mapHandler.currentCenter.longitude() + calculateMoveDelta(), mapHandler.currentCenter.latitude()))
        mapHandler.setCameraPosition()
    }

    fun zoomIn(){
        mapHandler.startAnalysis()
//        delayFunction<Unit>(Navigation_DELAY,TAG,"Zoom In") {
//            mapHandler.setZoom(mapHandler.currentZoom + ZOOM_CHANGE)
//            mapHandler.setCameraPosition()
//        }.invoke(Unit)
        mapHandler.setZoom(mapHandler.currentZoom + ZOOM_CHANGE)
        mapHandler.setCameraPosition()
    }

    fun zoomOut(){
        mapHandler.startAnalysis()
//        delayFunction<Unit>(Navigation_DELAY,TAG,"Zoom Out") {
//            mapHandler.setZoom(mapHandler.currentZoom - ZOOM_CHANGE)
//            mapHandler.setCameraPosition()
//        }.invoke(Unit)
        mapHandler.setZoom(mapHandler.currentZoom - ZOOM_CHANGE)
        mapHandler.setCameraPosition()
    }

    fun clickOnCenter(){
        analysisHandler.pauseTime()
        delayFunction<Unit>(0,TAG,"clicked") {
            showConfirmationDialog()
        }.invoke(Unit)
    }

    fun nextDownCity(){
        mapHandler.startAnalysis()
        delayFunction<Unit>(Navigation_DELAY,TAG,"GoDownCity") {
            mapHandler.goToDownCity()
        }.invoke(Unit)
    }

    fun nextUpCity(){
        mapHandler.startAnalysis()
        delayFunction<Unit>(Navigation_DELAY,TAG,"GoUpCity") {
            mapHandler.goToUpCity()
        }.invoke(Unit)
    }
}
