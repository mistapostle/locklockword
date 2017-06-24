package locklockwords.mistapostle.appspot.com.locklockworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

/**
 * Created by mistapostle on 17/6/24.
 */

public class PrefFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_pref, rootKey);
        SwitchPreferenceCompat lockScreenPref = (SwitchPreferenceCompat) findPreference("lockScreen_preference");
        lockScreenPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean)newValue) {
                    enableLockScreen();
                } else {
                    disableLookScreen();
                }
                return true;
            }
        });


        
    }

    public static Fragment newInstance( ) {
        return new PrefFragment();
    }


    private void disableLookScreen() {
        Activity activity = this.getActivity();
        activity.stopService(new Intent(activity, LockScreenService.class));
        Snackbar.make(this.getView(), "Disabled Lock scrren", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    private void enableLockScreen() {
        Activity activity = this.getActivity();
        activity.startService(new Intent(activity, LockScreenService.class));
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Snackbar.make(this.getView(), "Enabled Lock scrren", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        // android.R.drawable.ic_input_add

    }
}