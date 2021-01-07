package com.bonniepeng.bloomi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    // Preference.OnPreferenceClickListener

    private static FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new HeaderFragment())
                    .commit();
        } else {
            setTitle(savedInstanceState.getCharSequence("Settings"));
        }

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            setTitle(R.string.title_activity_settings);
                        }
                    }
                });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence("Settings", getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
//        // Instantiate the new Fragment
//        final Bundle args = pref.getExtras();
//        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
//                getClassLoader(),
//                pref.getFragment());
//        fragment.setArguments(args);
//        fragment.setTargetFragment(caller, 0);
//
//        // Replace the existing Fragment with the new Fragment
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.settings, fragment)
//                .addToBackStack(null)
//                .commit();
//        setTitle(pref.getTitle());
        return true;
    }


    // INSIDE SETTINGS MENU
    public static class HeaderFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // Load the preferences from an XML resource
            setPreferencesFromResource(R.xml.header_preferences, rootKey);

            setHasOptionsMenu(true);

            // display current user's email as the summary code part 1
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
            onSharedPreferenceChanged(sharedPrefs, getString(R.string.CEmail));


            // for some reason, implementing Preference on click listener and using a switch statement doesnt work.
            // so here is my hardcoding for each preference:

            // EMAIL
            Preference email = findPreference("CEmail");
            email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ChangeEmailDialog ced = new ChangeEmailDialog(getActivity());
                    InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                    ced.getWindow().setBackgroundDrawable(inset);
                    ced.show();

                    // update summary once user changes email
                    ced.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Preference pref = findPreference(getString(R.string.CEmail));
                            mAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            pref.setSummary(currentUser.getEmail());
                        }
                    });

                    return false;
                }
            });

            // PASSWORD
            Preference password = findPreference("RPass");
            password.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return false;
                }
            });

            // LOGOUT
            Preference logout = findPreference("LogOut");
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ChangeEmailDialog ced = new ChangeEmailDialog(getActivity());
                    InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                    ced.getWindow().setBackgroundDrawable(inset);
                    ced.show();
                    return false;
                }
            });

            // NOTIFS
            Preference notifs = findPreference("notifs");
            notifs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return false;
                }
            });

            // FEEDBACK
            Preference feedback = findPreference("feedback");
            feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return false;
                }
            });

            // ABOUT
            Preference about = findPreference("about");
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return false;
                }
            });
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // display current user's email as the summary code part 2
            Preference pref = findPreference(key);
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            pref.setSummary(currentUser.getEmail());
        }
    }


//        boolean refresh = ((Globals) this.getApplication()).isRefreshSettings();
//        ((Globals) this.getApplication()).setRefreshSettings(false);



    // CURRENTLY USELESS

    // not actually used, but system needs it for some reason
    public static class MessagesFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        }
    }

    // ON EACH OPTION CLICK - doesn't work, re-developed in OnCreatePreferences
//    @Override
//    public boolean onPreferenceClick(Preference preference) {
//        String key = preference.getKey();
//        Log.i("SETTINGS", "On Tree Clicked triggered");
//
//        switch (key) {
//            case "CEmail":
//                Log.i("SETTINGS", "change email button clicked");
//                ChangeEmailDialog ced = new ChangeEmailDialog(SettingsActivity.this);
//                InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
//                ced.getWindow().setBackgroundDrawable(inset);
//                ced.show();
//                break;
//            case "RPass":
//                Log.i("SETTINGS", "change password button clicked");
//                // TODO: open dialog to change pass
//                break;
//            case "LogOut":
//                Log.i("SETTINGS", "logout button clicked");
//
//                // TODO: logout with firebase and go back to login
//                break;
//            case "notifs":
//                Log.i("SETTINGS", "notifs button clicked");
//
//                // TODO: make all notifs in database false
//                break;
//            case "feedback":
//                Log.i("SETTINGS", "feedback button clicked");
//
//                // TODO: get feedback thing probably another activity
//                break;
//            case "about":
//                Log.i("SETTINGS", "about button clicked");
//
//                // TODO: display about the app info
//                break;
//            default:
//                Log.i("SETTINGS", "in default switch");
//                break;
//        }
//        return false;
//    }
}