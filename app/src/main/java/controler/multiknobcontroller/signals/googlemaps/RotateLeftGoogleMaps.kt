package controler.multiknobcontroller.signals.googlemaps

import android.util.Log
import controler.multiknobcontroller.entities.MultiKnob
import controler.multiknobcontroller.entities.Signal
import controler.multiknobcontroller.signals.SignalProcessor


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
        if (multiKnob.rotation <= -25 && multiKnob.fingerCount == 5
            ){
            Log.d(TAG, "Zoom Out")
            callback.onSignalReceived(Signal.GoogleMaps.ZOOM_OUT)
        }

        if (multiKnob.rotation <= -25 && multiKnob.fingerCount == 4){
            Log.d(TAG, "Move left")
            callback.onSignalReceived(Signal.GoogleMaps.LEFT)
        }

        if (multiKnob.rotation <= -25 && multiKnob.fingerCount == 3){
            Log.d(TAG, "Move Down")
            callback.onSignalReceived(Signal.GoogleMaps.DOWN)
        }
    }

}