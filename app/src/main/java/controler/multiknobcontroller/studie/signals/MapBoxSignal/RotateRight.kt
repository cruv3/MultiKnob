package controler.multiknobcontroller.studie.signals.MapBoxSignal

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.studie.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState
import controler.multiknobcontroller.utils.entities.StudieSignal


class RotateRight(private val tag: String) {
    companion object {
        private const val TAG_PRESET = " RotateRight"
    }

    private val TAG = tag + TAG_PRESET

    fun rotateRightInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback) {
        if(multiKnob.snapPoint != 1) return

        GlobalState.incrementSnapPoint()

//        // Move right to the next city with 5 fingers
//        if (multiKnob.fingerCount == 5) {
//            Log.d(TAG, "Next City Right")
//            callback.onSignalReceived(StudieSignal.RIGHT_NEXT_CITY)
//        }
//
//        // Move up to the next city with 3 fingers beyond 50 degrees
//        if (multiKnob.fingerCount == 5) {
//            Log.d(TAG, "Next City Up")
//            callback.onSignalReceived(StudieSignal.RIGHT_UP_CITY)
//        }

        // Zoom in with 4 fingers
        if (multiKnob.fingerCount == 4) {
            Log.d(TAG, "Zoom In")
            callback.onSignalReceived(StudieSignal.ZOOM_IN)
        }

        // Move right with 3 fingers
        if (multiKnob.fingerCount == 3) {
            Log.d(TAG, "Move Right")
            callback.onSignalReceived(StudieSignal.RIGHT)
        }

        // Move up with 4 fingers beyond 50 degrees
        if (multiKnob.fingerCount == 2) {
            Log.d(TAG, "Move Up")
            callback.onSignalReceived(StudieSignal.UP)
        }
    }
}
