package com.example.sns.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMapBinding
import com.example.sns.view.activity.MainActivity
import com.example.sns.viewModel.MapViewModel
import com.example.sns.widget.extension.noFinishStartActivity
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.IOException
import java.util.*


class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback ,
    LocationListener {

    private var locationManager: LocationManager? = null
    private lateinit var mMap: GoogleMap
    private lateinit var marker: MarkerOptions
    private lateinit var mContext: Context

    override val viewModel: MapViewModel
        get() = getViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_map


    override fun init() {
        if(!viewModel.checkLocationServicesStatus())
            showDialogForLocationServiceSetting()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
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

        locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager?

        return view
    }


    override fun observerViewModel() {
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("TAG", "레디 맵")

        mMap = googleMap

        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)

    }

    private fun showDialogForLocationServiceSetting() {
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

    override fun onLocationChanged(location: Location?) {
        if(location == null) return
        if(view == null) return;

        var latitude = location.latitude
        var longitude = location.longitude

        Log.d("TAG", "위도 : $latitude, 경도 : $longitude")
        //주소 설정
        var address = getCurrentAddress(latitude, longitude)
        if(address != null) {
            Log.d("TAG", address)
            address_text.text = address
        }

        val latLng = LatLng(latitude, longitude)

        //카메라 위치 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

        //----------------------------------------------
        //마커 생성
        marker = MarkerOptions().position(latLng).draggable(true)
        val m = mMap.addMarker(marker)
        //----------------------------------------------

        //카메라 이동시 마커 따라옴
        mMap.setOnCameraMoveListener {
            m.position = mMap.cameraPosition.target
            latitude = mMap.cameraPosition.target.latitude
            longitude = mMap.cameraPosition.target.longitude

        }

        //카메라 움직임이 없을시
        mMap.setOnCameraIdleListener {
            address_text.text = getCurrentAddress(latitude, longitude)
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

        locationManager!!.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    private fun getCurrentAddress(latitude: Double, longitude: Double): String? {

        Log.d("TAG", "지오코더들어옴")
        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(mContext, Locale.getDefault())
        val addresses: List<Address>
        addresses = try {
            geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            return "주소 미발견"
        }
        val address: Address = addresses[0]

        return address.getAddressLine(0).toString()
    }
}