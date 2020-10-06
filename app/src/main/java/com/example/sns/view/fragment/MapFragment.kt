package com.example.sns.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMapBinding
import com.example.sns.view.activity.LoadingActivity
import com.example.sns.view.activity.MainActivity
import com.example.sns.viewModel.MapViewModel
import com.example.sns.widget.GpsTracker
import com.example.sns.widget.extension.noFinishStartActivity
import com.example.sns.widget.extension.refreshFragment
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback{

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
        Log.d("TAG", "CALL ON CREATE VIEW")
        mapView.getMapAsync(this)

        gpsTracker = GpsTracker(this)
        gpsTracker.getLocation(requireContext())
        return view
    }


    override fun observerViewModel() {
    }

    fun updateLocation(latitude : Double, longitude : Double)  {
        gpsTracker.setCallback { d, d2 -> updateLocation(d, d2);  }

        //주소 설정
        var address = viewModel.getCurrentAddress(latitude, longitude)
        address_text.text = address

        val latLng = LatLng(latitude, longitude)

        //카메라 위치 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

        //----------------------------------------------
        //마커 생성
        marker.position(latLng)
        //----------------------------------------------
    }

    override fun onMapReady(googleMap: GoogleMap) {

        Log.d("TAG", "레디 맵")

        var latitude = gpsTracker.getLatitude()
        var longitude = gpsTracker.getLongitude()

        Log.d("TAG", "위도 : $latitude, 경도 : $longitude")


        mMap = googleMap
        marker = MarkerOptions().position(LatLng(latitude, longitude)).draggable(true)

        updateLocation(latitude, longitude)

        val m =  mMap.addMarker(marker)
            //카메라 이동시 마커 따라옴
        mMap.setOnCameraMoveListener {
            m.position = mMap.cameraPosition.target
            latitude = mMap.cameraPosition.target.latitude
            longitude = mMap.cameraPosition.target.longitude

        }

        //카메라 움직임이 없을시
        mMap.setOnCameraIdleListener {
            address_text.text = viewModel.getCurrentAddress(latitude, longitude)
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
            noFinishStartActivity(MainActivity::class.java)
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id ->
            startActivity(MainActivity::class.java)
            toast("위치설정을 허용해주세요")
        }
        builder.create().show()
    }



}