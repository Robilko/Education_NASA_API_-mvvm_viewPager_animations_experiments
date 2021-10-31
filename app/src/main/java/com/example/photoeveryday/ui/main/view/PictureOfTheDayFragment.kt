package com.example.photoeveryday.ui.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.photoeveryday.R
import com.example.photoeveryday.databinding.MainFragmentBinding
import com.example.photoeveryday.ui.main.repository.PODServerResponseData
import com.example.photoeveryday.ui.main.repository.PictureOfTheDayData
import com.example.photoeveryday.ui.main.utils.showSnackBar
import com.example.photoeveryday.ui.main.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_container.*
import kotlinx.android.synthetic.main.main_fragment.*

class PictureOfTheDayFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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
        setBottomAppBar(view)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(context, resources.getString(R.string.favourite), Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> {
                activity?.let {
                    it.supportFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment()).addToBackStack(null).commit()
                }
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(
                        it.supportFragmentManager,
                        "tag"
                    )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
    }

    private fun setBottomSheetBehavior(bottomSheet: LinearLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                serverResponseData.url?.let {
                    showSuccess(serverResponseData)
                    showLoadingLayout(false)
                } ?: showError(getString(R.string.error_message_empty_url))
            }
            is PictureOfTheDayData.Loading -> {
                showLoadingLayout(true)
            }
            is PictureOfTheDayData.Error -> {
                showLoadingLayout(false)
                showError(data.error.message.toString())

            }
        }
    }

    private fun showLoadingLayout(state: Boolean) {
        with(binding.includedLoadingLayout.loadingLayout) {
            if (state) this.visibility = View.VISIBLE else this.visibility = View.GONE
        }
    }

    private fun showSuccess(data: PODServerResponseData) {
        binding.imageView.load(data.url) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
        }
        view?.findViewById<TextView>(R.id.bottom_sheet_description_header)?.text = data.title
        view?.findViewById<TextView>(R.id.bottom_sheet_description)?.text = data.explanation
    }

    private fun showError(error: String) {
        binding.main.showSnackBar(
            error,
            getString(R.string.reload),
            { View.OnClickListener { } }
        )
    }
}