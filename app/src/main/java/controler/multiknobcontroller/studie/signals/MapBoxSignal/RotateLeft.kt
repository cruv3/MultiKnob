package controler.multiknobcontroller.studie.signals.MapBoxSignal

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.studie.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState
import controler.multiknobcontroller.utils.entities.StudieSignal


// Global
// Left
// OpenSpotify
// GoBack

class RotateLeft(private val tag: String) {
    companion object {
        private const val TAG_PRESET = " RotateLeft"
    }

    private val TAG = tag + TAG_PRESET

    fun rotateLeftInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback) {
        if(multiKnob.snapPoint != -1) return

        GlobalState.decrementSnapPoint()

        if (multiKnob.fingerCount == 4){
            Log.d(TAG, "Zoom Out")
            callback.onSignalReceived(StudieSignal.ZOOM_OUT)
        }

        if (multiKnob.fingerCount == 3){
            Log.d(TAG, "Move left")
            callback.onSignalReceived(StudieSignal.LEFT)
        }

        if (multiKnob.fingerCount == 2){
            Log.d(TAG, "Move Down")
            callback.onSignalReceived(StudieSignal.DOWN)
        }
    }
}
