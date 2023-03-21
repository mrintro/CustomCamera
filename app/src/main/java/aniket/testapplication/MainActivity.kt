package aniket.testapplication

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import aniket.testapplication.utils.Constants.PERMISSION_REQUEST_CODE

class MainActivity : AppCompatActivity() {


    var permissionsStr = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with((application as CameraApplication).applicationComponent) {
            inject(this@MainActivity)
            checkPermissions()
        }
    }

    private fun checkPermissions() {

        val arrayUnsatisfiedPermissions = mutableListOf<String>()

        for(permission in permissionsStr) {
            if(shouldShowRequestPermissionRationale(permission)){
                //SHowRational implementation.
                Toast.makeText(applicationContext, "Show Rational for $permission", Toast.LENGTH_SHORT).show()
            }
            if(ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission
            ) == PackageManager.PERMISSION_DENIED){
                arrayUnsatisfiedPermissions.add(permission)
            }
        }

        if(arrayUnsatisfiedPermissions.size > 0) {
            requestPermissions(arrayUnsatisfiedPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {
                managePermissionsResponse(permissions, grantResults)
            }
        }
    }

    private fun managePermissionsResponse(
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (i in grantResults.indices) {
            if(grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(applicationContext,
                    "We require the ${permissions[i]} permission to move forward in the application",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } }

}