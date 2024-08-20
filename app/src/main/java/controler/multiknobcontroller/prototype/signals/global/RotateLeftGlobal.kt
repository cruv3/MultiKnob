package controler.multiknobcontroller.prototype.signals.global

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalBlocker
import controler.multiknobcontroller.prototype.signals.SignalProcessor

class RotateLeftGlobal(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateLeft"
    }

    private var TAG_NEW = tag + TAG_PRESET

    fun globalLeftInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if(multiKnob.rotation <= -60 && multiKnob.fingerCount == 5){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.OPEN_SPOTIFY)){
                Log.d(TAG_NEW, "Opening Spotify")
                callback.onSignalReceived(Signal.GlobalSignal.OPEN_SPOTIFY)
            }
        }

        if(multiKnob.rotation <= -60 && multiKnob.fingerCount == 2){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.GO_BACK)){
                Log.d(TAG_NEW, "Go Back")
                callback.onSignalReceived(Signal.GlobalSignal.GO_BACK)
            }
        }

        if(multiKnob. rotation <= -25 && multiKnob.fingerCount == 2){
            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.LEFT)){
                Log.d(TAG_NEW, "Left")
                callback.onSignalReceived(Signal.GlobalSignal.LEFT)
            }
        }
    }
}