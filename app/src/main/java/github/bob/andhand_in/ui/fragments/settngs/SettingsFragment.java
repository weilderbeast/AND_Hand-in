package github.bob.andhand_in.ui.fragments.settngs;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import github.bob.andhand_in.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}