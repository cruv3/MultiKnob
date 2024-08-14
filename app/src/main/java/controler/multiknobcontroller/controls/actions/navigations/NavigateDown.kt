package controler.multiknobcontroller.controls.actions.navigations

import controler.multiknobcontroller.controls.NodeManager

fun navigateDown(nodeManager: NodeManager) {
    val nodes = nodeManager.getNodes() ?: return
    val currentNode = nodeManager.getCurrentNode() ?: return

    // Find the closest node below the current node that is focusable, clickable, and visible
    val downNode = nodeManager.findClosestNode(currentNode, nodes) { other ->
        other.bounds.top >= currentNode.bounds.bottom &&
                other.node.isFocusable &&
                other.node.isClickable &&
                other.node.isVisibleToUser
    }

    if (downNode != null) {
        nodeManager.focusNode(downNode)
    }
}
