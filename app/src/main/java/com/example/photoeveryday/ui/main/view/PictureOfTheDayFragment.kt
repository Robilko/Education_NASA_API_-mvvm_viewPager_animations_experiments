package com.example.photoeveryday.ui.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.photoeveryday.R
import com.example.photoeveryday.databinding.MainFragmentBinding
import com.example.photoeveryday.ui.main.repository.PictureOfTheDayData
import com.example.photoeveryday.ui.main.utils.showSnackBar
import com.example.photoeveryday.ui.main.viewmodel.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //Отобразите ошибку
                    // showError("Сообщение, что ссылка пустая")
                    binding.main.showSnackBar(
                        getString(R.string.error_message_empty_url),
                        getString(R.string.reload),
                        { View.OnClickListener { } }
                    )
                } else {
                    //Отобразите фото
                    // showSuccess()
                    // Coil в работе: достаточно вызвать у нашего ImageView
                    // нужную extension-функцию и передать ссылку и заглушки для placeholder
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                //Отобразите загрузку
                // showLoading()
            }
            is PictureOfTheDayData.Error -> {
                //Отобразите ошибку
            }
        }
    }
}