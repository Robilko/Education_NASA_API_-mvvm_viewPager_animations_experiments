package com.example.photoeveryday.ui.main.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoeveryday.BuildConfig
import com.example.photoeveryday.ui.main.repository.PODRetrofitImpl
import com.example.photoeveryday.ui.main.repository.PODServerResponseData
import com.example.photoeveryday.ui.main.repository.PictureOfTheDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayData() : LiveData<PictureOfTheDayData> {
        val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        sendServerRequest(date)
        return liveDataForViewToObserve
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getYesterdayData(): LiveData<PictureOfTheDayData> {
        val date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        sendServerRequest(date)
        return liveDataForViewToObserve
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getTwoDaysAgoData(): LiveData<PictureOfTheDayData> {
        val date = LocalDate.now().minusDays(2).format(DateTimeFormatter.ISO_DATE)
        sendServerRequest(date)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(date, apiKey).enqueue(object : Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }

            })
        }
    }
}