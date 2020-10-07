package com.example.demoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class MyPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_layout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference sensorPreference1 = findPreference("preference_key_1");
        sensorPreference1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent fragIntent = new Intent(getContext(), FragmentActivity.class);
                startActivity(fragIntent);
                return true;
            }
        });

        Preference sensorPreference2 = findPreference("preference_key_2");
        sensorPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent tabIntent = new Intent(getContext(), TabActivity.class);
                startActivity(tabIntent);
                return true;
            }
        });

        Preference sensorPreference3 = findPreference("preference_key_3");
        sensorPreference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent galleryIntent = new Intent(getContext(), GalleryActivity.class);
                startActivity(galleryIntent);
                return true;
            }
        });

        Preference sensorPreference4 = findPreference("preference_key_4");
        sensorPreference4.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent dbIntent = new Intent(getContext(), EmployeeActivity.class);
                startActivity(dbIntent);
                return true;
            }
        });
    }
}
