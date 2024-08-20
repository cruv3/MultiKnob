package controler.multiknobcontroller.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import controler.multiknobcontroller.prototype.controls.NodeManager
import controler.multiknobcontroller.utils.entities.GlobalState

fun <T> delayFunction(delayMs: Long, nodeManager: NodeManager, log: String, action: (T) -> Unit): (T) -> Unit {
    val handler = Handler(Looper.getMainLooper())
    return { param : T ->
        if (!nodeManager.isNavigating()){
            nodeManager.setNavigating(true)
            handler.postDelayed ({
                Log.d(controler.multiknobcontroller.prototype.controls.Controller.TAG, log)
                action(param)
                nodeManager.setNavigating(false)
            },delayMs)
        }
    }
}

fun <T> delayFunction(delayMs: Long, tag: String, log: String, action: (T) -> Unit): (T) -> Unit {
    val handler = Handler(Looper.getMainLooper())
    return { param : T ->
        if (!GlobalState.isNavigating){
            GlobalState.isNavigating = true
            handler.postDelayed ({
                Log.d(tag, log)
                action(param)
                GlobalState.isNavigating = false
            },delayMs)
        }
    }
}

fun <T> delayFunctionWithCallback(
    delayMs: Long,
    delayAfterFinish: Long,
    nodeManager: NodeManager,
    log: String,
    action: (T) -> Unit,
    callback: () -> Unit,
): (T) -> Unit {
    val handler = Handler(Looper.getMainLooper())
    return { param: T ->
        if (!nodeManager.isNavigating()) {
            nodeManager.setNavigating(true)
            handler.postDelayed({
                Log.d(controler.multiknobcontroller.prototype.controls.Controller.TAG, log)
                action(param)
                handler.postDelayed({
                    callback()
                    nodeManager.setNavigating(false)
                }, delayAfterFinish)
            }, delayMs)
        }
    }
}