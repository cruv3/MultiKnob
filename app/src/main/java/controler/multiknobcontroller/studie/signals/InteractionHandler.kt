package controler.multiknobcontroller.studie.signals

import controler.multiknobcontroller.utils.entities.MultiKnob

abstract class InteractionHandler {
    abstract fun handleInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback)
}