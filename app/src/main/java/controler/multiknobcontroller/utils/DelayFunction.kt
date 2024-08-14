package controler.multiknobcontroller.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import controler.multiknobcontroller.controls.Controller
import controler.multiknobcontroller.controls.NodeManager

fun <T> delayFunction(delayMs: Long, nodeManager: NodeManager, log: String, action: (T) -> Unit): (T) -> Unit {
    val handler = Handler(Looper.getMainLooper())
    return { param : T ->
        if (!nodeManager.isNavigating()){
            nodeManager.setNavigating(true)
            handler.postDelayed ({
                Log.d(Controller.TAG, log)
                action(param)
                nodeManager.setNavigating(false)
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
                Log.d(Controller.TAG, log)
                action(param)
                handler.postDelayed({
                    callback()
                    nodeManager.setNavigating(false)
                }, delayAfterFinish)
            }, delayMs)
        }
    }
}