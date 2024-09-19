package controler.multiknobcontroller.utils.entities

import android.util.Log

object GlobalState {
    var currentApp: AppPackage? = null
    var isNavigating: Boolean = false
    var snapPointPosition = 0

    fun resetGlobalState(){
        currentApp = null
        isNavigating = false
        snapPointPosition = 0
    }

    fun incrementSnapPoint(){
        if(snapPointPosition == 12){
            snapPointPosition = 0
        }else{
            snapPointPosition += 1
        }
        Log.d("GLOBALSTATE", "SnapPoint ${snapPointPosition}")
    }

    fun decrementSnapPoint(){
        if(snapPointPosition == 0){
            snapPointPosition = 12
        }else{
            snapPointPosition -= 1
        }
        Log.d("GLOBALSTATE", "SnapPoint ${snapPointPosition}")
    }
}