package controler.multiknobcontroller.controls.actions.navigations

import controler.multiknobcontroller.controls.GestureControls
import controler.multiknobcontroller.controls.NodeManager
import controler.multiknobcontroller.utils.logNodeDetails

const val TAG_Navigation_Right = "NavigationRight"

//fun navigateRight(nodeManager: NodeManager) {
//    val grid = nodeManager.getNodes() ?: return
//    var currentRowIndex = nodeManager.getCurrentRowIndex()
//    var currentColumnIndex = nodeManager.getCurrentColumnIndex()
//    if (currentColumnIndex < grid[currentRowIndex].size - 1) {
//        currentColumnIndex++
//    } else if (currentRowIndex < grid.size - 1) {
//        currentRowIndex++
//        currentColumnIndex = 0
//    }
//
//    val rightNode = grid[currentRowIndex][currentColumnIndex].node
//    logNodeDetails(TAG_Navigation_Right, rightNode)
//
//    nodeManager.setCurrentRowIndex(currentRowIndex)
//    nodeManager.setCurrentColumnIndex(currentColumnIndex)
//    nodeManager.focusNode(grid[currentRowIndex][currentColumnIndex].node)
//}

fun navigateRight(nodeManager: NodeManager, gestureControls: GestureControls) {
    val nodes = nodeManager.getNodes() ?: return
    val currentNode = nodeManager.getCurrentNode()
    val currentIndex = nodes.indexOf(currentNode)

    for (i in currentIndex + 1 until nodes.size) {
        val nextNode = nodes[i].node
        if (nextNode.isFocusable && nextNode.isClickable && nextNode.isVisibleToUser) {
            nodeManager.focusNode(nodes[i])
            return
        }
    }
}