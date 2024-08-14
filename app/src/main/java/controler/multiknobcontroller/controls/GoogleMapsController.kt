package controler.multiknobcontroller.controls

import android.util.DisplayMetrics
import controler.multiknobcontroller.controls.actions.maps.moveMapDown
import controler.multiknobcontroller.controls.actions.maps.moveMapLeft
import controler.multiknobcontroller.controls.actions.maps.moveMapRight
import controler.multiknobcontroller.controls.actions.maps.moveMapUp
import controler.multiknobcontroller.controls.actions.maps.startMapNavigation
import controler.multiknobcontroller.controls.actions.maps.startNavigationAtCenterPosition
import controler.multiknobcontroller.controls.actions.maps.zoomInMaps
import controler.multiknobcontroller.controls.actions.maps.zoomOutMaps
import controler.multiknobcontroller.service.BluetoothService
import controler.multiknobcontroller.utils.delayFunction
import controler.multiknobcontroller.utils.delayFunctionWithCallback

class GoogleMapsControls(private val nodeManager: NodeManager, private val gestureControls: GestureControls, displayMetrics: DisplayMetrics, private val bluetoothService: BluetoothService) {
    companion object{
        private const val DELAY = 200L
        private const val MIN_DURATION = 1
        private const val MAX_DURATION = 100
        private const val DURATION_INCREASE = 10
    }
    private val metrics = displayMetrics
    private var strength = 5
    private var duration = 1
    private val feedBackMessage = "FEEDBACK:"

    fun zoomIn() {
        delayFunctionWithCallback(DELAY, DELAY, nodeManager, "Google Maps - zoom in", {
                _: Unit -> zoomInMaps(gestureControls, metrics)
        }){
            if(duration < MAX_DURATION){
                duration += DURATION_INCREASE
                strength += DURATION_INCREASE
            }
            val message = "$feedBackMessage$strength,$duration"
            bluetoothService.sendBluetoothMessage(message)
        }.invoke(Unit)
    }

    fun zoomOut() {
        delayFunctionWithCallback(DELAY, DELAY, nodeManager, "Google Maps - zoom out", {
            _: Unit -> zoomOutMaps(gestureControls, metrics)
        }){
            if(duration > MIN_DURATION){
                duration -= DURATION_INCREASE
                strength -= DURATION_INCREASE
            }
            val message = "$feedBackMessage$strength,$duration"
            bluetoothService.sendBluetoothMessage(message)
        }.invoke(Unit)
    }

    fun startNavigation(){
        delayFunctionWithCallback(DELAY, DELAY, nodeManager, "Google Maps - start navigation",{
                _: Unit -> startNavigationAtCenterPosition(gestureControls, metrics)
        }){
            startMapNavigation(gestureControls, nodeManager)
        }.invoke(Unit)
    }

    fun moveLeft(){
        delayFunction<Unit>(DELAY, nodeManager, "Google Maps - move left"){
            moveMapLeft(gestureControls, metrics)
        }.invoke(Unit)
    }

    fun moveRight(){
        delayFunction<Unit>(DELAY, nodeManager, "Google Maps - move right"){
            moveMapRight(gestureControls, metrics)
        }.invoke(Unit)
    }

    fun moveUp(){
        delayFunction<Unit>(DELAY, nodeManager, "Google Maps - move up"){
            moveMapUp(gestureControls, metrics)
        }.invoke(Unit)
    }

    fun moveDown(){
        delayFunction<Unit>(DELAY, nodeManager, "Google Maps - move down"){
            moveMapDown(gestureControls, metrics)
        }.invoke(Unit)
    }
}
