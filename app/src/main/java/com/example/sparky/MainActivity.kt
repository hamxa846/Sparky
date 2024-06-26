package com.example.sparky

import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.sparky.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var torchState = false
    private var cameraId: String = 0.toString()
    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]

        Dexter.withContext(this).withPermission(android.Manifest.permission.CAMERA).withListener(object :
            PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                turnOnFlashLight()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()

    }

    private fun turnOnFlashLight() {
        binding.imageTorch.setOnClickListener {
            torchState = when(torchState){
                false -> {
                    cameraManager.setTorchMode(cameraId,true)
                    binding.imageTorch.setImageResource(R.drawable.flashlight_on)
                    true
                }
                true -> {
                    cameraManager.setTorchMode(cameraId,false)
                    binding.imageTorch.setImageResource(R.drawable.flashlight_off)
                    false
                }
            }
        }
    }
}