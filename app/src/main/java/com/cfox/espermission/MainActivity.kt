package com.cfox.espermission

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity-esp"

        private val permissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun requestPermission(view: View) {

        val pResult = EsPermissions(this).isGranted(permissions)

        Log.d(TAG, "requestPermission: pResult: $pResult")
        if (!pResult) {
            EsPermissions(this).request(permissions , {
                Log.d(TAG, "requestPermission: $it")
                Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show()

            }, { permissions, deniedPermissions, permanentlyDeniedPermissions ->
                Log.d(TAG, "requestPermission: $permissions  $deniedPermissions   $permanentlyDeniedPermissions")

                if (permanentlyDeniedPermissions.isNotEmpty()) {
                    AlertDialog.Builder(this)
                        .setMessage("打开设置设置权限")
                        .setCancelable(false)
                        .setNegativeButton(
                            "取消"
                        ) { _, _ -> }
                        .setPositiveButton(
                            "打开"
                        ) { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, 2)

                        }
                        .create().show()
                }
            })
        } else {
            Toast.makeText(this, "权限已经获取成功", Toast.LENGTH_SHORT).show()
        }

    }
}