package com.example.instrmusic3.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.instrmusic3.R;


public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_about);
		String versionString = getResources().getString(R.string.app_name);
		try {
			versionString += " " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		TextView nameAndVersion = (TextView) findViewById(R.id.name_and_version);
		nameAndVersion.setText(versionString);
		TextView copyright = (TextView) findViewById(R.id.copyright_links);
		copyright.setMovementMethod(LinkMovementMethod.getInstance());
		copyright.setText(Html.fromHtml(getResources().getString(R.string.about_copyright)));
		TextView javaOsc = (TextView) findViewById(R.id.javaosc_links);
		javaOsc.setMovementMethod(LinkMovementMethod.getInstance());
		javaOsc.setText(Html.fromHtml(getResources().getString(R.string.about_license_javaosc)));
		TextView bugLinks = (TextView) findViewById(R.id.buglinks);
		bugLinks.setMovementMethod(LinkMovementMethod.getInstance());
		bugLinks.setText(Html.fromHtml(getResources().getString(R.string.about_buglinks)));
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
