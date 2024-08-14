package controler.multiknobcontroller.utils

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo


fun logNodeDetails(tag: String, node: AccessibilityNodeInfo) {
    val bounds = android.graphics.Rect()
    node.getBoundsInScreen(bounds)
    Log.d(tag, "Text: ${node.text}, Class: ${node.className}, Bounds: $bounds, ContentDescription: ${node.contentDescription}")
}

fun isAppRunning(context: Context, packageName: String): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val procInfos = activityManager.runningAppProcesses
    if (procInfos != null) {
        for (processInfo in procInfos) {
            if (processInfo.processName == packageName) {
                return true
            }
        }
    }
    return false
}