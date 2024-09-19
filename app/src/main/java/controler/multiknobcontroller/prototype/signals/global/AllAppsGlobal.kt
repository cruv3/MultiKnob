package controler.multiknobcontroller.prototype.signals.global

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalBlocker
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState

class AllAppsGlobal(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateLeft"
    }
    private var TAG_NEW = tag + TAG_PRESET

    fun globalAppSignal(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if(multiKnob.fingerCount == 5 && multiKnob.snapPoint == 1){
            GlobalState.incrementSnapPoint()
        }
        if(multiKnob.fingerCount == 5 && multiKnob.snapPoint == -1){
            GlobalState.decrementSnapPoint()
        }

        if(multiKnob.fingerCount == 5 && GlobalState.snapPointPosition == 1){
            Log.d(TAG_NEW, "Opening Google Maps")
            callback.onSignalReceived(Signal.GlobalSignal.OPEN_GOOGLE_MAPS)
        }
        if(multiKnob.fingerCount == 5 && GlobalState.snapPointPosition == 2){
            Log.d(TAG_NEW, "Opening Settings")
            callback.onSignalReceived(Signal.GlobalSignal.OPEN_SETTINGS)
        }
        if(multiKnob.fingerCount == 5 && GlobalState.snapPointPosition == 3){
            Log.d(TAG_NEW, "Opening PlayStore")
            callback.onSignalReceived(Signal.GlobalSignal.OPEN_PLAYSTORE)
        }

        if(multiKnob.fingerCount == 5 && GlobalState.snapPointPosition == 12){
            Log.d(TAG_NEW, "Opening Spotify")
            callback.onSignalReceived(Signal.GlobalSignal.OPEN_SPOTIFY)
        }
        if(multiKnob.fingerCount == 5 && GlobalState.snapPointPosition == 11){
            Log.d(TAG_NEW, "Opening Youtube")
            callback.onSignalReceived(Signal.GlobalSignal.OPEN_YOUTUBE)
        }
        if(multiKnob.fingerCount == 5 && GlobalState.snapPointPosition == 10){
            Log.d(TAG_NEW, "Opening Telephone")
            callback.onSignalReceived(Signal.GlobalSignal.OPEN_TELEPHONE)
        }
    }
}