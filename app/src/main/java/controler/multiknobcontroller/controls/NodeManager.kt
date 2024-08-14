package controler.multiknobcontroller.controls

import android.accessibilityservice.AccessibilityService
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import controler.multiknobcontroller.entities.NodeWrapper
import kotlin.math.pow
import kotlin.math.sqrt

class NodeManager(private val service: AccessibilityService) {
    companion object {
        private const val TAG = "NodeManager"
    }

    private var nodes: List<NodeWrapper>? = null

    private var currentFocusedNode: NodeWrapper? = null

    private var isNavigating = false

    fun setupNodes() {
        val rootNode = service.rootInActiveWindow ?: return
        val allNodes = getAllNodes(rootNode)
        val newNodes = sortNodesByPosition(allNodes)
        if(newNodes == nodes){
            currentFocusedNode!!.node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            return
        }

        nodes = newNodes
        currentFocusedNode = nodes?.get(0)
        currentFocusedNode!!.node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
    }

    fun setupNodes(root: AccessibilityNodeInfo?){
        val allNodes = getAllNodes(root)
        val newNodes = sortNodesByPosition(allNodes)
        if(newNodes == nodes){
            currentFocusedNode!!.node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            return
        }

        nodes = newNodes
        currentFocusedNode = nodes?.get(0)
        currentFocusedNode!!.node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
    }


    fun getNodes(): List<NodeWrapper>? {
        return nodes
    }

    fun getCurrentNode(): NodeWrapper?{
        return currentFocusedNode
    }

    fun focusNode(node: NodeWrapper){
        node.node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
        currentFocusedNode = node
    }

    fun clickCurrentNode() {
        currentFocusedNode?.node?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    fun findClosestNode(currentNode: NodeWrapper, nodes: List<NodeWrapper>, filter: (NodeWrapper) -> Boolean): NodeWrapper? {
        return nodes
            .filter { filter(it) }
            .minByOrNull { distance(currentNode.bounds, it.bounds) }
    }

    private fun distance(rect1: Rect, rect2: Rect): Double {
        val centerX1 = rect1.exactCenterX()
        val centerY1 = rect1.exactCenterY()
        val centerX2 = rect2.exactCenterX()
        val centerY2 = rect2.exactCenterY()
        return sqrt((centerX1 - centerX2).toDouble().pow(2.0) + (centerY1 - centerY2).toDouble()
            .pow(2.0)
        )
    }

    private fun getAllNodes(root: AccessibilityNodeInfo?): List<AccessibilityNodeInfo> {
        val allNodes = mutableListOf<AccessibilityNodeInfo>()
        if (root == null) return allNodes
        val stack = mutableListOf(root)
        while (stack.isNotEmpty()) {
            val node = stack.removeAt(stack.size - 1)
            if (node.isFocusable || node.isClickable || node.isVisibleToUser) {
                allNodes.add(node)
            }
            for (i in 0 until node.childCount) {
                val child = node.getChild(i)
                if (child != null) stack.add(child)
            }
        }
        return allNodes
    }

    private fun sortNodesByPosition(nodes: List<AccessibilityNodeInfo>): List<NodeWrapper> {
        val nodeWrappers = nodes.map { node ->
            val bounds = Rect()
            node.getBoundsInScreen(bounds)
            NodeWrapper(node, bounds)
        }
        return nodeWrappers.sortedWith(compareBy({ it.bounds.top }, { it.bounds.left }))
    }

    fun isNavigating(): Boolean {
        return isNavigating
    }

    fun setNavigating(navigating: Boolean) {
        isNavigating = navigating
    }
}