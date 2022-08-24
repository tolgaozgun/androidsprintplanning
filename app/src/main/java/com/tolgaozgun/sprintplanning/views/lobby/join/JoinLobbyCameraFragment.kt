package com.tolgaozgun.sprintplanning.views.lobby.join

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.databinding.FragmentJoinLobbyCameraBinding
import com.tolgaozgun.sprintplanning.databinding.FragmentJoinRoomBinding
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyCameraViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyCameraViewModelFactory
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JoinLobbyCameraFragment : Fragment() {

    private lateinit var binding: FragmentJoinLobbyCameraBinding
    private lateinit var viewModel: JoinLobbyCameraViewModel
    private lateinit var viewModelFactory: JoinLobbyCameraViewModelFactory
    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var shouldContinue: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {}

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if (detections != null && detections.detectedItems.isNotEmpty()) {
                val barcode = detections?.detectedItems
                if ((barcode?.size() ?: 0) > 0) {
                    // show barcode content value
                    val value: String = barcode?.valueAt(0)?.displayValue ?: ""
                    if(value.length == 6 && shouldContinue.value!!){
                        shouldContinue.postValue(false)
                        readCode(value)
                    }
                }
            }
        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback {

        override fun surfaceChanged(p0: SurfaceHolder, format: Int, width: Int, height: Int) {
        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            cameraSource.stop()
        }

        override fun surfaceCreated(p0: SurfaceHolder) {
            // check camera permission for api version 23
            if(ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED )
                cameraSource.start(p0)
            else requestPermissions( arrayOf<String>(Manifest.permission.CAMERA),1001)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            }
        } else {
            Toast.makeText(context, "Camera permissions are not granted", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startCamera(){

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(requireContext(), "Camera permissions are not granted", Toast.LENGTH_SHORT).show()
            return
        }
        cameraSource.start(binding.surfaceViewCamera.holder)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.stop()
        cameraSource.release()
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = JoinLobbyCameraViewModelFactory(context = requireContext(),
            fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(JoinLobbyCameraViewModel::class.java)

        setupBarcode()
    }

    private fun setupBarcode(){
        detector = BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build()
        detector.setProcessor(processor)
        cameraSource = CameraSource.Builder(context,detector)
            .setRequestedFps(25f)
            .setAutoFocusEnabled(true).build()
        binding.surfaceViewCamera.holder.addCallback(surfaceCallBack)
    }


    private fun readCode(value: String){
        Log.d("qr", "Read code $value")
        viewModel.joinRoom(shouldContinue, value)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinLobbyCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imgBack.setOnClickListener{
            viewModel.goBackFragment()
        }
    }

}