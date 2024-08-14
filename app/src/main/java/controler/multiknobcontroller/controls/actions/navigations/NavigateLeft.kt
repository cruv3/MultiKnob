package controler.multiknobcontroller.controls.actions.navigations

import controler.multiknobcontroller.controls.GestureControls
import controler.multiknobcontroller.controls.NodeManager
import controler.multiknobcontroller.utils.logNodeDetails

const val TAG_Navigation_Left = "NavigationLeft"

//fun navigateLeft(nodeManager: NodeManager) {
//    val grid = nodeManager.getNodes() ?: return
//    var currentRowIndex = nodeManager.getCurrentRowIndex()
//    var currentColumnIndex = nodeManager.getCurrentColumnIndex()
//    if (currentColumnIndex > 0) {
//        currentColumnIndex--
//    } else if (currentRowIndex > 0) {
//        currentRowIndex--
//        currentColumnIndex = grid[currentRowIndex].size - 1
//    }
//
//    val leftNode = grid[currentRowIndex][currentColumnIndex].node
//    logNodeDetails(TAG_Navigation_Left, leftNode)
//    nodeManager.setCurrentRowIndex(currentRowIndex)
//    nodeManager.setCurrentColumnIndex(currentColumnIndex)
//    nodeManager.focusNode(grid[currentRowIndex][currentColumnIndex].node)
//}

fun navigateLeft(nodeManager: NodeManager, gestureControls: GestureControls) {
    val nodes = nodeManager.getNodes() ?: return
    val currentNode = nodeManager.getCurrentNode()
    val currentIndex = nodes.indexOf(currentNode)

    // Check nodes to the left of the current one
    for (i in currentIndex - 1 downTo 0) {
        val previousNode = nodes[i].node
        if (previousNode.isFocusable && previousNode.isClickable && previousNode.isVisibleToUser) {
            nodeManager.focusNode(nodes[i])
            return
        }
    }

//    // If at the first node or no valid node is found, perform a swipe gesture to the left
//    if (currentIndex == 0) {
//        gestureControls.performSwipeGesture(0f, 400f, 800f, 400f) {
//            nodeManager.setupNodes()
//        }
//    }
}

