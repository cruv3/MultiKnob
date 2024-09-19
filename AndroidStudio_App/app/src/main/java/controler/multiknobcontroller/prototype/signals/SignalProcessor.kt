package controler.multiknobcontroller.prototype.signals

import android.content.Context
import android.util.Log
import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.GlobalState
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.global.GlobalSignal
import controler.multiknobcontroller.prototype.signals.googlemaps.GoogleMapsSignal


class SignalProcessor(private val context: Context, private val callback: SignalCallback) {
    companion object{
        private const val TAG = "SignalProcessor"
        private const val LONG_PRESS_THRESHOLD = 1000L // 1 second
    }

    private var recentApp : AppPackage? = null

    interface SignalCallback{
        fun onSignalReceived(signal: Signal.GlobalSignal)
        fun onSignalReceived(signal: Signal.GlobalSignal.ButtonSignal)
        fun onSignalReceived(signal: Signal.GoogleMaps)
        fun onSignalReceived(signal: Signal.GoogleMaps.ButtonSignal)
    }

    private val handlers = listOf(
        GoogleMapsSignal(),
        GlobalSignal(),
    )

    fun processMultiKnobData(multiKnob: MultiKnob){
        resetBlocker(GlobalState.currentApp)
        // Process global signals
        handlers.forEach { handler ->
            handler.handleInteraction(GlobalState.currentApp, multiKnob ,callback)
        }
    }

    private fun resetBlocker(appPackage: AppPackage?){
        //Log.d(TAG, appPackage.toString())
        if(recentApp != appPackage){
            Log.d(TAG, "recentApp : ${recentApp}, appPackage: $appPackage")
            SignalBlocker.unblockAllSignals()
            Log.d(TAG, "Unblocking all signals as created by ${recentApp?.packageName}")

            recentApp = GlobalState.currentApp
        }
    }
}