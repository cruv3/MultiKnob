import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    private const val TAG = "PermissionUtils"

    // Permission request codes
    const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    const val BLUETOOTH_PERMISSION_REQUEST_CODE = 1002

    private val LOCATION_PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val BLUETOOTH_PERMISSIONS = arrayOf(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_ADMIN
    )

    private val BLUETOOTH_SCAN_PERMISSION = android.Manifest.permission.BLUETOOTH_SCAN
    private val BLUETOOTH_CONNECT_PERMISSION = android.Manifest.permission.BLUETOOTH_CONNECT


    fun hasLocationPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LOCATION_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            // Permissions are granted at install time for API < 23
            true
        }
    }

    fun hasBluetoothPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, BLUETOOTH_SCAN_PERMISSION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, BLUETOOTH_CONNECT_PERMISSION) == PackageManager.PERMISSION_GRANTED &&
                    BLUETOOTH_PERMISSIONS.all {
                        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                    }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BLUETOOTH_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            // No need for runtime permission checks for Bluetooth below API 31
            true
        }
    }

    fun requestLocationPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, LOCATION_PERMISSIONS, requestCode)
        }
    }

    fun requestBluetoothPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(activity, arrayOf(
                BLUETOOTH_SCAN_PERMISSION,
                BLUETOOTH_CONNECT_PERMISSION
            ), requestCode)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, BLUETOOTH_PERMISSIONS, requestCode)
        }
    }
}
