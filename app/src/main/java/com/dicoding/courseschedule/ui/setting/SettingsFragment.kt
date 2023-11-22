package com.dicoding.courseschedule.ui.setting

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference done
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference done
        val switchDark = findPreference<ListPreference?>(getString(R.string.pref_key_dark))
        switchDark?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue.toString() == getString(R.string.pref_dark_auto)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    updateTheme(NightMode.AUTO.value)
                } else updateTheme(NightMode.ON.value)
            } else if (newValue.toString() == getString(R.string.pref_dark_off)) {
                updateTheme(NightMode.OFF.value)
            } else {
                updateTheme(NightMode.ON.value)
            }
            true
        }

        val switchNotification = findPreference<SwitchPreference?>(getString(R.string.pref_key_notify))
        switchNotification?.setOnPreferenceChangeListener { preference, newValue ->
            val workManager = DailyReminder()
            if (newValue == true) {
                workManager.setDailyReminder(requireContext())
            } else {
                workManager.cancelAlarm(requireContext())
            }
            true
        }



    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}