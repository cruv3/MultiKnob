package controler.multiknobcontroller.studie.signals.MapBoxSignal

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.studie.signals.SignalProcessor
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

    fun rotateLeftInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if (multiKnob.rotation <= -25 && multiKnob.fingerCount == 5){
            Log.d(TAG, "Zoom Out")
            callback.onSignalReceived(StudieSignal.ZOOM_OUT)
        }

        if (multiKnob.rotation <= -25 && multiKnob.fingerCount == 4){
            Log.d(TAG, "Move left")
            callback.onSignalReceived(StudieSignal.LEFT)
        }

        if (multiKnob.rotation <= -25 && multiKnob.fingerCount == 3){
            Log.d(TAG, "Move Down")
            callback.onSignalReceived(StudieSignal.LEFT_NEXT_CITY)
        }

//        if (multiKnob.rotation <= -25 && multiKnob.fingerCount == 3){
//            Log.d(TAG, "Move Down")
//            callback.onSignalReceived(Signal.GoogleMaps.DOWN)
//        }
    }

}