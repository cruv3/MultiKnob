package controler.multiknobcontroller.entities

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

data class NodeWrapper(val node: AccessibilityNodeInfo, val bounds: Rect)