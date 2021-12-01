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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSettings()
        with(binding) {
            chipGroupThemeSelection.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    defaultTheme.id -> saveThemeMode(KEY_DEFAULT_THEME)
                    cosmicTheme.id -> saveThemeMode(KEY_RED_ROSE_THEME)
                }
            }
            chipGroupFontSelection.setOnCheckedChangeListener { chipGroup, checkedId ->
                chipGroup.findViewById<Chip>(checkedId)?.let {
                    Toast.makeText(context, "Выбран ${it.text}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadSettings() {
        with(binding) {
            val theme =
                requireActivity().getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
                    .getString(THEME_PREFERENCES, KEY_DEFAULT_THEME)

            if (theme.equals(KEY_RED_ROSE_THEME)) {
                cosmicTheme.isChecked = true
            } else {
                defaultTheme.isChecked = true
            }
        }
    }

    private fun saveThemeMode(theme: String) {
        requireContext().getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(THEME_PREFERENCES, theme)
            .apply()
        requireActivity().recreate()
    }
}