package controler.multiknobcontroller.controls.actions.navigations

import controler.multiknobcontroller.controls.NodeManager

//fun navigateUp(nodeManager: NodeManager) {
//    val grid = nodeManager.getNodes() ?: return
//    var currentRowIndex = nodeManager.getCurrentRowIndex()
//    var currentColumnIndex = nodeManager.getCurrentColumnIndex()
//    if (currentRowIndex > 0) {
//        currentRowIndex--
//        currentColumnIndex = min(currentColumnIndex, grid[currentRowIndex].size - 1)
//        nodeManager.setCurrentRowIndex(currentRowIndex)
//        nodeManager.setCurrentColumnIndex(currentColumnIndex)
//        nodeManager.focusNode(grid[currentRowIndex][currentColumnIndex].node)
//    }
//}
//
//private fun min(a: Int, b: Int): Int {
//    return if (a < b) a else b
//}

fun navigateUp(nodeManager: NodeManager) {
    val nodes = nodeManager.getNodes() ?: return
    val currentNode = nodeManager.getCurrentNode() ?: return

    // Find the closest node above the current node that is focusable, clickable, and visible
    val upNode = nodeManager.findClosestNode(currentNode, nodes) { other ->
        other.bounds.bottom <= currentNode.bounds.top &&
                other.node.isFocusable &&
                other.node.isClickable &&
                other.node.isVisibleToUser
    }

    if (upNode != null) {
        nodeManager.focusNode(upNode)
    }
}

