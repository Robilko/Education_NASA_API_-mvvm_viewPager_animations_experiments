package com.example.photoeveryday.ui.main.view.picture

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import coil.api.load
import com.example.photoeveryday.R
import com.example.photoeveryday.databinding.MainFragmentStartBinding
import com.example.photoeveryday.ui.main.repository.PODServerResponseData
import com.example.photoeveryday.ui.main.viewmodel.PictureOfTheDayData
import com.example.photoeveryday.ui.main.utils.*
import com.example.photoeveryday.ui.main.view.MainActivity
import com.example.photoeveryday.ui.main.view.api.ApiActivity
import com.example.photoeveryday.ui.main.view.settings.SettingsFragment
import com.example.photoeveryday.ui.main.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import java.util.*

class PictureOfTheDayFragment : Fragment() {
    private var _binding: MainFragmentStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var isExpanded = false

    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this)[PictureOfTheDayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(0).observe(viewLifecycleOwner, { renderData(it) })
        setBottomAppBar(view)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        with(binding) {
            todayChip.isChecked = true
            inputLayout.setEndIconOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data =
                        Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
                })
            }

            chipGroupPreviousDays.setOnCheckedChangeListener { chipGroup, checkedId ->
                isExpanded = false
                changeImageExpand()
                chipGroup.findViewById<Chip>(checkedId)?.let {
                    when (checkedId) {
                        yesterdayChip.id -> viewModel.getData(-1)
                            .observe(viewLifecycleOwner, { renderData(it) })
                        twoDaysAgoChip.id -> viewModel.getData(-2)
                            .observe(viewLifecycleOwner, { renderData(it) })
                        else -> viewModel.getData(0).observe(viewLifecycleOwner, { renderData(it) })
                    }
                }
            }

            imageView.setOnClickListener{
                isExpanded = !isExpanded
                changeImageExpand()
            }
        }
    }

    private fun changeImageExpand() {
        with(binding) {
            TransitionManager.beginDelayedTransition(
                main, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = imageView.layoutParams
            params.height = if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            imageView.layoutParams = params
            imageView.scaleType = if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> {
                activity?.let {
                    startActivity(Intent(it, ApiActivity::class.java))
                }
            }
            R.id.app_bar_settings -> {
                activity?.let {
                    it.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment()).addToBackStack(null).commit()
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
        if (data.mediaType.equals("video")) {
            showVideo(data)
        } else {
            showImage(data)
        }

        view?.findViewById<TextView>(R.id.bottom_sheet_description_header)?.text = data.title
        view?.findViewById<TextView>(R.id.bottom_sheet_description)?.text = data.explanation
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showVideo(data: PODServerResponseData) {
        with(binding) {
            imageView.visibility = View.GONE
            videoView.apply {
                visibility = android.view.View.VISIBLE
                videoView.settings.javaScriptEnabled = true
                videoView.loadUrl(Uri.parse(data.url).toString())
            }
        }
    }

    private fun showImage(data: PODServerResponseData) {
        with(binding) {
            videoView.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            imageView.load(data.url) {
                lifecycle(this@PictureOfTheDayFragment)
                error(R.drawable.ic_load_error_vector)
                placeholder(R.drawable.ic_no_photo_vector)
            }
        }
    }


    private fun showError(error: String) {
        binding.main.showSnackBar(
            error,
            getString(R.string.close),
            { View.OnClickListener { } }
        )
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}