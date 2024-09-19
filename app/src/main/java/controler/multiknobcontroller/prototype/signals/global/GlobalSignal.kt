package controler.multiknobcontroller.prototype.signals.global

import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.prototype.signals.InteractionHandler
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState


class GlobalSignal: InteractionHandler() {
    companion object{
        private const val TAG = "GlobalSignal"
    }

    private var resetSnapPosition = false
    private val buttonPressGlobal = ButtonPressGlobal(TAG)
    private val rotateLeftGlobal = RotateLeftGlobal(TAG)
    private val rotateRightGlobal = RotateRightGlobal(TAG)
    private val allAppGlobal = AllAppsGlobal(TAG)

    override fun handleInteraction(
        packageName: AppPackage?,
        multiKnob: MultiKnob,
        callback: SignalProcessor.SignalCallback
    ) {
        if(multiKnob.fingerCount == 5 && !resetSnapPosition){
            GlobalState.snapPointPosition = 0
            resetSnapPosition = true
        }
        if(multiKnob.fingerCount == 0 && resetSnapPosition){
            resetSnapPosition = false
        }

        allAppGlobal.globalAppSignal(multiKnob,callback)
        buttonPressGlobal.globalButtonInteraction(multiKnob, callback)
        rotateLeftGlobal.globalLeftInteraction(multiKnob, callback)
        rotateRightGlobal.globalInteraction(multiKnob, callback)

    }
}