package controler.multiknobcontroller.prototype.signals.global

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalBlocker
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState


class RotateRightGlobal(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateRight"
    }

    private var TAG_NEW = tag + TAG_PRESET

    fun globalInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if(multiKnob.snapPoint != 1) return

        if(multiKnob.fingerCount == 4){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.RIGHT)){
                Log.d(TAG_NEW, "Right")
                callback.onSignalReceived(Signal.GlobalSignal.RIGHT)
            }
        }
    }
}