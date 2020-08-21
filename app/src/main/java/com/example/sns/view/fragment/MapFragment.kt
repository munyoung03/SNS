package com.example.sns.view.fragment

import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMapBinding
import com.example.sns.viewModel.MapViewModel
import com.example.sns.widget.GpsTracker
import com.example.sns.widget.extension.toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.IOException
import java.util.*


class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback {

    private lateinit var gpsTracker: GpsTracker
    private lateinit var mMap: GoogleMap

    override val viewModel: MapViewModel
        get() = getViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_map

    override fun init() {
        if(!checkLocationServicesStatus())
        showDialogForLocationServiceSetting()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View? = super.onCreateView(inflater, container, savedInstanceState);
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

        gpsTracker = GpsTracker(this);
        gpsTracker.getLocation(requireContext())
        return view;
    }
    override fun observerViewModel() {

    }

    override fun onMapReady(googleMap: GoogleMap) {


        val latitude = gpsTracker!!.getLatitude()
        val longitude = gpsTracker!!.getLongitude()

        Log.d("TAG", "위도 : $latitude, 경도 : $longitude")

        val address = getCurrentAddress(latitude, longitude)
        address_text.text = address

        mMap = googleMap
        val latLng = LatLng(latitude, longitude)
        val marker = MarkerOptions().position(latLng)
        mMap.addMarker(marker)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
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
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(callGPSSettingIntent)
        })
        builder.setNegativeButton("취소",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        builder.create().show()
    }


    fun getCurrentAddress(latitude: Double, longitude: Double): String? {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses: List<Address>
        addresses = try {
            geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            toast("지오코더 서비스 사용불가")
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            toast("잘못된 GPS 좌표")
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            toast("주소 미발견")
            return "주소 미발견"
        }
        val address: Address = addresses[0]

        return address.getAddressLine(0).toString().toString() + "\n"
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager?
        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

}