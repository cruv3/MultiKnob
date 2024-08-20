package controler.multiknobcontroller

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import controler.multiknobcontroller.studie.MapActivity


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MAINACTIVITY"
    }

    private var locationAllowed = false
    private var bluetoothAllowed = false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (PermissionUtils.hasLocationPermissions(this)) {
            locationAllowed = true
        } else {
            PermissionUtils.requestLocationPermissions(this, PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE)
        }

        if (PermissionUtils.hasBluetoothPermissions(this)) {
            bluetoothAllowed = true
        } else {
            PermissionUtils.requestBluetoothPermissions(this, PermissionUtils.BLUETOOTH_PERMISSION_REQUEST_CODE)
        }


        if(bluetoothAllowed && locationAllowed){
            askUserChoice()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE -> {
                locationAllowed = true
                if(bluetoothAllowed && locationAllowed){
                    askUserChoice()
                }
            }
            PermissionUtils.BLUETOOTH_PERMISSION_REQUEST_CODE -> {
                bluetoothAllowed = true
                if(bluetoothAllowed && locationAllowed){
                    askUserChoice()
                }
            }
        }
    }

    private fun askUserChoice(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Mode")
        builder.setMessage("Please choose a mode to proceed:")

        // Add options for Studie and Proof of Concept
        builder.setPositiveButton("Studie") { dialog, _ ->
            startMapActivity()
            dialog.dismiss()
        }

        builder.setNegativeButton("Proof of Concept") { dialog, _ ->
            startAccessibilityService()
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun startAccessibilityService() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun startMapActivity(){
        Log.d(TAG, "Launching MapActivity")
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }
}
