package controler.multiknobcontroller.studie.signals

import android.content.Context
import controler.multiknobcontroller.studie.signals.MapBoxSignal.MapBoxSignal
import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.GlobalState
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.StudieSignal

class SignalProcessor(private val context: Context, private val callback: SignalCallback) {
    companion object{
        private const val TAG = "SignalProcessor"
        private const val LONG_PRESS_THRESHOLD = 1000L // 1 second
    }

    private var recentApp : AppPackage? = null

    interface SignalCallback{
        fun onSignalReceived(signal: StudieSignal)
        fun onSignalReceived(signal: StudieSignal.ButtonSignal)
    }

    private val handlers = listOf(
        MapBoxSignal()
    )

    fun processMultiKnobData(multiKnob: MultiKnob){
        handlers.forEach { handler ->
            handler.handleInteraction(multiKnob ,callback)
        }
    }
}