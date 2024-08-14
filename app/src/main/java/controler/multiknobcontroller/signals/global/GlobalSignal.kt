package controler.multiknobcontroller.signals.global

import controler.multiknobcontroller.entities.AppPackage
import controler.multiknobcontroller.entities.MultiKnob
import controler.multiknobcontroller.signals.InteractionHandler
import controler.multiknobcontroller.signals.SignalProcessor



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