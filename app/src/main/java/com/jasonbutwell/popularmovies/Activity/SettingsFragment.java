package com.jasonbutwell.popularmovies.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.jasonbutwell.popularmovies.R;

/**
 * Created by J on 03/03/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        // update the summary for our setting
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        // Loop through preferences and check that the instance is not a checkbox,
        // before attempting to set the summary

        for (int i = 0; i < count; i++) {

            Preference p = preferenceScreen.getPreference(i);

            if (!(p instanceof CheckBoxPreference))
                setPreferenceSummary(p, sharedPreferences.getString(p.getKey(),"") );
        }
    }

    // Helper method to set the preference summary

    private void setPreferenceSummary(Preference preference, String value) {
        if ( preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);

            if (prefIndex >= 0)
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if ( preference != null ) {

            if (!(preference instanceof CheckBoxPreference))
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(),""));
        }
    }

    // Register the shared preferences listener here

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // Unregister the shared preferences listener here

    @Override
    public void onDestroy() {
        super.onDestroy();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}