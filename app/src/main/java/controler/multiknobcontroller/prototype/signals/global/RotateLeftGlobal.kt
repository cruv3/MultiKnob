package controler.multiknobcontroller.prototype.signals.global

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalBlocker
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState

class RotateLeftGlobal(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateLeft"
    }
    private var TAG_NEW = tag + TAG_PRESET

    fun globalLeftInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if(multiKnob.snapPoint != -1) return

        if(multiKnob.fingerCount == 4){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.LEFT)){
                Log.d(TAG_NEW, "Left")
                callback.onSignalReceived(Signal.GlobalSignal.LEFT)
            }
        }

        if(multiKnob.fingerCount == 2){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.GO_BACK)){
                Log.d(TAG_NEW, "Go Back")
                callback.onSignalReceived(Signal.GlobalSignal.GO_BACK)
            }
        }
    }
}