package controler.multiknobcontroller.signals.global

import android.os.Handler
import android.os.Looper
import android.util.Log
import controler.multiknobcontroller.entities.MultiKnob
import controler.multiknobcontroller.entities.Signal
import controler.multiknobcontroller.signals.SignalBlocker
import controler.multiknobcontroller.signals.SignalProcessor

class ButtonPressGlobal(private val tag: String) {
    companion object {
        private const val TAG_PRESET = " ButtonPress"
        private const val LONG_PRESS_THRESHOLD = 1000L
        private const val DOUBLE_TAP_TIMEOUT = 300L
    }

    private var TAG_NEW = tag + TAG_PRESET

    private var lastButtonPressTime: Long = 0
    private var lastButtonReleaseTime: Long = 0
    private var tapCount: Int = 0
    private var isButtonPressed: Boolean = false
    private var longPressTriggered: Boolean = false
    private val handler = Handler(Looper.getMainLooper())


    fun globalButtonInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        val currentTime = System.currentTimeMillis()

        if (multiKnob.buttonPress == 1 && !isButtonPressed) {
            isButtonPressed = true
            longPressTriggered = false
            lastButtonPressTime = currentTime

            if (currentTime - lastButtonReleaseTime < DOUBLE_TAP_TIMEOUT) {
                tapCount++
            } else {
                tapCount = 1
            }

            if (tapCount == 2) {
                handler.removeCallbacksAndMessages(null)
                if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.ButtonSignal.DOUBLE_TAP)){
                    Log.d(TAG_NEW, "Double Tap")
                    callback.onSignalReceived(Signal.GlobalSignal.ButtonSignal.DOUBLE_TAP)
                    tapCount = 0
                }
            } else {
                handler.postDelayed({
                    if (tapCount == 1 && !longPressTriggered) {
                        val pressDuration = System.currentTimeMillis() - lastButtonPressTime
                        if (pressDuration >= LONG_PRESS_THRESHOLD) {
                            if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.ButtonSignal.LONG_PRESS)){
                                Log.d(TAG_NEW, "Long Press")
                                callback.onSignalReceived(Signal.GlobalSignal.ButtonSignal.LONG_PRESS)
                                longPressTriggered = true
                            }
                        }
                    }
                }, LONG_PRESS_THRESHOLD)
            }
        } else if (multiKnob.buttonPress == 0 && isButtonPressed) {
            isButtonPressed = false
            lastButtonReleaseTime = currentTime

            if (!longPressTriggered) {
                handler.postDelayed({
                    if (tapCount == 1) {
                        if(!SignalBlocker.isSignalBlocked(Signal.GlobalSignal.ButtonSignal.SHORT_TAP)){
                            Log.d(TAG_NEW, "Short Tap")
                            if(multiKnob.fingerCount == 5){
                                callback.onSignalReceived(Signal.GlobalSignal.ButtonSignal.ALL_APPS)
                            }else{
                                callback.onSignalReceived(Signal.GlobalSignal.ButtonSignal.SHORT_TAP)
                            }

                            tapCount = 0
                        }
                    }
                }, DOUBLE_TAP_TIMEOUT)
            }
        }
    }
}