package controler.multiknobcontroller.signals

import controler.multiknobcontroller.entities.AppPackage
import controler.multiknobcontroller.entities.MultiKnob

abstract class InteractionHandler {
    abstract fun handleInteraction(packageName: AppPackage?, multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback)
}