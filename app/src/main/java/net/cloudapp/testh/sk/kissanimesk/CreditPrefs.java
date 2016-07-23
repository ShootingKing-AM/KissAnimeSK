package net.cloudapp.testh.sk.kissanimesk;

/**
 * Created by SK on 21/7/2016.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.util.prefs.Preferences;

public class CreditPrefs extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference myPref = (Preference) findPreference("repo");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://github.com/ShootingKing-AM/KissAnimeSK");
                i.setData(uri);
                startActivity(i);
                return true;
            }
        });

        myPref = (Preference) findPreference("issues");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://github.com/ShootingKing-AM/KissAnimeSK/issues");
                i.setData(uri);
                startActivity(i);
                return true;
            }
        });

        myPref = (Preference) findPreference("email");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                Uri uri = Uri.parse("mailto: raghunarnindi002@gmail.com")
                        .buildUpon()
                        .appendQueryParameter("subject", "KissAnimeSK: ")
                        .appendQueryParameter("body", "Hey there Developer :)")
                        .build();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(Intent.createChooser(emailIntent, "Email Developer"));
                return true;
            }
        });
    }
}
