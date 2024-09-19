package controler.multiknobcontroller.prototype.signals.googlemaps

import android.provider.Settings.Global
import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState


// Global
// Left
// OpenSpotify
// GoBack

class RotateLeftGoogleMaps(private val tag: String) {
    companion object {
        private const val TAG_PRESET = " RotateLeftSignal"
    }

    private val TAG = tag + TAG_PRESET

    fun rotateLeftInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if(multiKnob.snapPoint != -1) return

        if (multiKnob.fingerCount == 4){
            Log.d(TAG, "Zoom Out")
            callback.onSignalReceived(Signal.GoogleMaps.ZOOM_OUT)
        }

        if (multiKnob.fingerCount == 3){
            Log.d(TAG, "Move left")
            callback.onSignalReceived(Signal.GoogleMaps.LEFT)
        }

        if (multiKnob.fingerCount == 2){
            Log.d(TAG, "Move Down")
            callback.onSignalReceived(Signal.GoogleMaps.DOWN)
        }
    }

}