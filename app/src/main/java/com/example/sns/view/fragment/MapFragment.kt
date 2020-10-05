package com.example.sns.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMapBinding
import com.example.sns.viewModel.MapViewModel
import com.example.sns.widget.GpsTracker
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback {

    private lateinit var gpsTracker: GpsTracker
    private lateinit var mMap: GoogleMap
    private lateinit var marker: MarkerOptions

    override val viewModel: MapViewModel
        get() = getViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_map


    override fun init() {
        if(!viewModel.checkLocationServicesStatus())
        showDialogForLocationServiceSetting()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View? = super.onCreateView(inflater, container, savedInstanceState)
        val mapView =
            view?.findViewById<View>(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync(this)

        gpsTracker = GpsTracker(this)
        gpsTracker.getLocation(requireContext())

        return view
    }


    override fun observerViewModel() {

    }


    override fun onMapReady(googleMap: GoogleMap) {

        var latitude = gpsTracker.getLatitude()
        var longitude = gpsTracker.getLongitude()

        Log.d("TAG", "위도 : $latitude, 경도 : $longitude")

        var address = viewModel.getCurrentAddress(latitude, longitude)
        address_text.text = address

        mMap = googleMap
        val latLng = LatLng(latitude, longitude)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

        //----------------------------------------------
        marker = MarkerOptions().position(latLng).draggable(true)
        val m = mMap.addMarker(marker)
        //----------------------------------------------

        //카메라 이동시 마커 따라옴
        mMap.setOnCameraMoveListener {
            m.position = mMap.cameraPosition.target
            latitude = mMap.cameraPosition.target.latitude
            longitude = mMap.cameraPosition.target.longitude

        }

        mMap.setOnCameraIdleListener {
            Log.d("TAG", "바이")
            address_text.text = viewModel.getCurrentAddress(latitude, longitude)
            Log.d("TAG", address_text.text as String?)
        }


        //마커 드래그 후 카메라 이동
//        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
//            override fun onMarkerDragStart(arg0: Marker) {
//
//            }
//
//            override fun onMarkerDragEnd(arg0: Marker) {
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arg0.position, 17f))
//
//                address = getCurrentAddress(arg0.position.latitude, arg0.position.longitude)
//                address_text.text = address
//            }
//
//            override fun onMarkerDrag(arg0: Marker?) {
//            }
//        })
    }

     fun showDialogForLocationServiceSetting() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
                앱을 사용하기 위해서는 위치 서비스가 필요합니다.
                위치 설정을 수정하실래요?
                """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(callGPSSettingIntent)
        }
         builder.setNegativeButton(
             "취소"
         ) { dialog, id -> dialog.cancel() }
         builder.create().show()
    }
}