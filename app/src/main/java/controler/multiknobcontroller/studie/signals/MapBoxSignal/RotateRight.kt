package controler.multiknobcontroller.studie.signals.MapBoxSignal

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.studie.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.StudieSignal


// Global
// OpenGoogleMaps
class RotateRight(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateRight"
    }

    private val TAG = tag + TAG_PRESET

    fun rotateRightInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if (multiKnob.rotation >= 25 && multiKnob.fingerCount == 5){
            Log.d(TAG, "Zoom In")
            callback.onSignalReceived(StudieSignal.ZOOM_IN)
        }

        if (multiKnob.rotation >= 25 && multiKnob.fingerCount == 4){
            Log.d(TAG, "Move Right")
            callback.onSignalReceived(StudieSignal.RIGHT)
        }

        if (multiKnob.rotation >= 25 && multiKnob.fingerCount == 3){
            Log.d(TAG, "Next City Right")
            callback.onSignalReceived(StudieSignal.RIGHT_NEXT_CITY)
        }

//        if (multiKnob.rotation >= 25 && multiKnob.fingerCount == 3){
//            Log.d(TAG, "Move Up")
//            callback.onSignalReceived(Signal.GoogleMaps.UP)
//        }
    }
}