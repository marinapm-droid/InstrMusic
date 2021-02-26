package com.example.instrmusic3.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import androidx.core.app.NavUtils;
import android.view.MenuItem;


public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(com.example.instrmusic3.R.xml.preferences);
		addPreferencesFromResource(org.sensors2.common.R.xml.common_preferences);
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Respond to the action bar's Up/Home button
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
