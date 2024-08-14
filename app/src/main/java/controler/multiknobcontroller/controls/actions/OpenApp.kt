package controler.multiknobcontroller.controls.actions

import android.content.Context
import android.content.Intent
import android.net.Uri
import controler.multiknobcontroller.utils.isAppRunning

fun openAppWithPackageName(context: Context, packageName: String) {

    if(isAppRunning(context, packageName)){
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(launchIntent)
    }else{
        val intent = setIntent(packageName)
        intent.setPackage(packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }
}

private fun setIntent(packageName: String): Intent{

    return when(packageName){
        "com.spotify.music" -> Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"))
        "com.google.android.apps.maps" -> Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps"))
        else -> Intent(Intent.ACTION_VIEW)
    }
}