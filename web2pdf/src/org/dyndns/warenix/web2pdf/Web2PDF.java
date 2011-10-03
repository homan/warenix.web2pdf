package org.dyndns.warenix.web2pdf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.widget.Toast;

public class Web2PDF extends PreferenceActivity {

	PDFmyURL pdf = new PDFmyURL();
	String pageSize;
	String orientation;

	public void onStart() {
		super.onStart();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pdf_preference);

		loadSavedPrefs();

		initPrefs();

	}

	private void loadSavedPrefs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		orientation = prefs.getString(PDFmyURL.Option.PARAMETER_ORIENTATION,
				PDFmyURL.Option.ORIENTATION_PORTRAIT);

		pageSize = prefs.getString(PDFmyURL.Option.PARAMETER_PAGE_SIZE,
				PDFmyURL.Option.PAGE_SIZE_A4);

	}

	void initPrefs() {
		savePreference(PDFmyURL.Option.PARAMETER_ORIENTATION, orientation);
		savePreference(PDFmyURL.Option.PARAMETER_PAGE_SIZE, pageSize);

		String url = getURLFromIntent();
		if (url == null) {
			url = "";
		}
		savePreference(PDFmyURL.Option.PARAMETER_URL, url);

		pdf.setURL(url);
		pdf.setOrientation(orientation);
		pdf.setPageSize(pageSize);

		final ListPreference pageSizePref = (ListPreference) findPreference("page_size");
		pageSizePref.setSummary(pageSize);
		pageSizePref.setValue(pageSize);
		pageSizePref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						pageSize = (String) newValue;
						pdf.setPageSize(pageSize);
						pageSizePref.setSummary(pageSize);
						savePreference(PDFmyURL.Option.PARAMETER_PAGE_SIZE,
								pageSize);

						return true;
					}

				});

		final ListPreference orientationPref = (ListPreference) findPreference("orientation");
		orientationPref.setSummary(orientation);
		orientationPref.setValue(orientation);
		orientationPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						orientation = (String) newValue;
						pdf.setOrientation(orientation);
						orientationPref.setSummary(orientation);
						savePreference(PDFmyURL.Option.PARAMETER_ORIENTATION,
								orientation);

						return true;
					}

				});

		EditTextPreference urlPref = (EditTextPreference) findPreference("url");
		if (url.compareTo("") != 0) {
			urlPref.setTitle(url);
		}
		urlPref.setText(url);
		urlPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {

				String newURL = (String) newValue;

				if (newURL.compareTo("") == 0) {
					Toast.makeText(
							getBaseContext(),
							"The URL seems not correct, please double check it"
									+ preference.getKey(), Toast.LENGTH_LONG)
							.show();
					return true;
				}

				savePreference(PDFmyURL.Option.PARAMETER_URL, newURL);
				pdf.setURL(newURL);

				String web2pdfurl = pdf.toString();

				Log.i(LOG_TAG, web2pdfurl);
				Intent viewIntent = new Intent("android.intent.action.VIEW",
						Uri.parse(web2pdfurl));

				startActivity(viewIntent);
				return true;
			}

		});
	}

	protected void savePreference(String key, String value) {
		SharedPreferences customSharedPreference = getSharedPreferences(
				"web2pdfSharedPrefs", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = customSharedPreference.edit();
		editor.putString(key, value);
		editor.commit();
	}

	protected String getURLFromIntent() {
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		if (bundle != null) {
			return bundle.getString(Intent.EXTRA_TEXT);
		}
		return null;
	}

	final static String LOG_TAG = "warenix";

}
