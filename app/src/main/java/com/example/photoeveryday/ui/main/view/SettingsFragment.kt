package com.example.photoeveryday.ui.main.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.photoeveryday.databinding.FragmentSettingsBinding
import com.example.photoeveryday.ui.main.utils.*
import com.google.android.material.chip.Chip

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadSettings() {
        with(binding) {
            val checkedId =
                requireActivity().getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE).getInt(
                    CHIP_THEME_SELECTED, defaultTheme.id
                )
            chipGroupThemeSelection.findViewById<Chip>(checkedId).isChecked = true
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSettings()
        with(binding) {
            chipGroupThemeSelection.setOnCheckedChangeListener { chipGroup, checkedId ->
                when (chipGroup.findViewById<Chip>(checkedId)) {
                    defaultTheme -> saveThemeMode(KEY_DEFAULT_THEME, checkedId)
                    cosmicTheme -> saveThemeMode(KEY_COSMIC_THEME, checkedId)

                }
            }
            chipGroupFontSelection.setOnCheckedChangeListener { chipGroup, checkedId ->
                chipGroup.findViewById<Chip>(checkedId)?.let {
                    Toast.makeText(context, "Выбран ${it.text}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveThemeMode(theme: String, checkedId: Int) {
        requireContext().getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(THEME_PREFERENCES, theme)
            .putInt(CHIP_THEME_SELECTED, checkedId)
            .apply()
        requireActivity().recreate()
    }
}