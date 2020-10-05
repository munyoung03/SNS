package com.example.sns.viewModel

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns.base.BaseViewModel
import com.example.sns.widget.extension.toast
import org.koin.dsl.koinApplication
import java.io.IOException
import java.util.*

class MapViewModel(private val application : Application) : BaseViewModel() {

    fun getCurrentAddress(latitude: Double, longitude: Double): String? {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(application, Locale.getDefault())
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

        return address.getAddressLine(0).toString().toString() + "\n"
    }


}