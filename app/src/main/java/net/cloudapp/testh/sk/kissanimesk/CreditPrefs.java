package net.cloudapp.testh.sk.kissanimesk;

/**
 * Created by SK on 21/7/2016.
 */

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CreditPrefs extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
