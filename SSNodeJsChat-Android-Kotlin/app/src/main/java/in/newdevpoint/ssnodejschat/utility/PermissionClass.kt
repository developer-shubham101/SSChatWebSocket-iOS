package `in`.newdevpoint.ssnodejschat.utility

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionClass(private val permissionRequire: PermissionRequire, private val activity: Activity) {
    private val REQUEST_PERMISSIONS = 4154
    private var flag = 0
    fun askPermission(flag: Int) {
        this.flag = flag
        if (hasPermissions(*permissionRequire.listOfPermission(flag))) {
            permissionRequire.permissionGranted(flag)
        } else {
            ActivityCompat.requestPermissions(activity, permissionRequire.listOfPermission(flag), REQUEST_PERMISSIONS)
        }
    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        if (permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS) {
            var allAllow = true
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allAllow = false
                    break
                }
            }
            if (allAllow) {
                permissionRequire.permissionGranted(flag)
            } else {
                permissionRequire.permissionDeny()
            }
        }
    }

    interface PermissionRequire {
        fun permissionDeny()
        fun permissionGranted(flag: Int)
        fun listOfPermission(flag: Int): Array<String>
    }
}