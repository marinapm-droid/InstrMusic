package com.example.instrmusic3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.instrmusic3.HomePage;


public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		addPreferencesFromResource(com.example.instrmusic3.R.xml.preferences);
		addPreferencesFromResource(org.sensors2.common.R.xml.common_preferences);

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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i=new Intent(SettingsActivity.this, HomePage.class);
		startActivity(i);
	}


}
