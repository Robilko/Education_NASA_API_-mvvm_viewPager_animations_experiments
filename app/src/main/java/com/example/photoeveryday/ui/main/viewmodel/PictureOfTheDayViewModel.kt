package com.example.photoeveryday.ui.main.viewmodel

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
import java.util.*

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(amountOfMinusDays: Int) : LiveData<PictureOfTheDayData> {
        sendServerRequest(getStringDateForRequest(amountOfMinusDays))
        return liveDataForViewToObserve
    }

    private fun getStringDateForRequest(amountOfMinusDays: Int): String {
        val date = Calendar.getInstance()
        date.add(Calendar.DATE, amountOfMinusDays)
        return "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}"
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