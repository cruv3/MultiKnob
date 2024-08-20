package controler.multiknobcontroller.prototype.signals.global

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalBlocker
import controler.multiknobcontroller.prototype.signals.SignalProcessor


class RotateRightGlobal(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateRight"
    }

    private var TAG_NEW = tag + TAG_PRESET

    fun globalInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if(multiKnob.rotation >= 60 && multiKnob.fingerCount == 5){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.OPEN_GOOGLE_MAPS)){
                Log.d(TAG_NEW, "Opening Google Maps")
                callback.onSignalReceived(Signal.GlobalSignal.OPEN_GOOGLE_MAPS)
            }
        }

        if(multiKnob. rotation >= 25 && multiKnob.fingerCount == 2){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.RIGHT)){
                Log.d(TAG_NEW, "Right")
                callback.onSignalReceived(Signal.GlobalSignal.RIGHT)
            }
        }
    }
}