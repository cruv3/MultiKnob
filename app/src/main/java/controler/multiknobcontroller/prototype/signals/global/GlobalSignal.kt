package controler.multiknobcontroller.prototype.signals.global

import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.prototype.signals.InteractionHandler
import controler.multiknobcontroller.prototype.signals.SignalProcessor



class GlobalSignal: InteractionHandler() {
    companion object{
        private const val TAG = "GlobalSignal"
    }

    private val buttonPressGlobal = ButtonPressGlobal(TAG)
    private val rotateLeftGlobal = RotateLeftGlobal(TAG)
    private val rotateRightGlobal = RotateRightGlobal(TAG)

    override fun handleInteraction(
        packageName: AppPackage?,
        multiKnob: MultiKnob,
        callback: SignalProcessor.SignalCallback
    ) {
        buttonPressGlobal.globalButtonInteraction(multiKnob, callback)
        rotateLeftGlobal.globalLeftInteraction(multiKnob, callback)
        rotateRightGlobal.globalInteraction(multiKnob, callback)
    }
}