package controler.multiknobcontroller.prototype.signals

import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.MultiKnob

abstract class InteractionHandler {
    abstract fun handleInteraction(packageName: AppPackage?, multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback)
}