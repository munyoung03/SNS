package com.example.sns.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMapBinding
import com.example.sns.viewModel.MapViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.getViewModel
import com.example.sns.R

class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override val viewModel: MapViewModel
        get() = getViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_map

    override fun init() {

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
        return view;
    }
    override fun observerViewModel() {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng = LatLng(33.0, 124.0)
        val marker = MarkerOptions().position(latLng)
        mMap.addMarker(marker)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))

    }

}