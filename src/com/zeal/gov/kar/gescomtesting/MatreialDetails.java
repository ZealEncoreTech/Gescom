package com.zeal.gov.kar.gescomtesting;



import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class MatreialDetails extends PreferenceActivity implements
OnSharedPreferenceChangeListener  {
	 private ListPreference tcListPreference;
	 private ListPreference htListPreference;
	 private ListPreference ltListPreference;
	 private ListPreference slListPreference;
	 private ListPreference lyingListPreference;
	 
	 public static final String KEY_TC_LIST_PREFERENCE = "Transformer";
	 public static final String KEY_HT_LIST_PREFERENCE = "Ht-Line";
	 public static final String KEY_LT_LIST_PREFERENCE = "Lt-Line";
	 public static final String KEY_SL_LIST_PREFERENCE = "SpanLenght";
	 public static final String KEY_LAYING_LIST_PREFERENCE = "layingMethod";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 addPreferencesFromResource(R.layout.materialdetails);
//		 
//		 tcListPreference=(ListPreference) getPreferenceScreen().findPreference(KEY_TC_LIST_PREFERENCE);
//		 htListPreference=(ListPreference) getPreferenceScreen().findPreference(KEY_HT_LIST_PREFERENCE);
//		 ltListPreference=(ListPreference) getPreferenceScreen().findPreference(KEY_LT_LIST_PREFERENCE);
		 slListPreference=(ListPreference) getPreferenceScreen().findPreference(KEY_SL_LIST_PREFERENCE);
		 lyingListPreference=(ListPreference) getPreferenceScreen().findPreference(KEY_LAYING_LIST_PREFERENCE);
		 
		 SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//		 tcListPreference.setSummary((String) tcListPreference.getEntry());
//		 htListPreference.setSummary((String) htListPreference.getEntry());
//		 ltListPreference.setSummary((String) ltListPreference.getEntry());
		 slListPreference.setSummary((String) slListPreference.getEntry());
		 lyingListPreference.setSummary((String) lyingListPreference.getEntry());
			
	}
	  @Override
	    protected void onResume() {
	        super.onResume();
	        getPreferenceScreen().getSharedPreferences()
	                .registerOnSharedPreferenceChangeListener(this);
	    }
	  @Override
	    protected void onPause() {
	        super.onPause();
	        // Unregister the listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences()
	                .unregisterOnSharedPreferenceChangeListener(this);
	    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		
		
		/*// Let's do something a preference value changes
        if (key.equals(KEY_TC_LIST_PREFERENCE)) {
        	tcListPreference.setSummary(sharedPreferences.getValue(key, ""));
        	 ListPreference listPref = (ListPreference) pref;
             pref.setSummary(listPref.getEntry());
        }*/
		  Preference pref = findPreference(key);

		    if (pref instanceof ListPreference) {
		        ListPreference listPref = (ListPreference) pref;
		        pref.setSummary(listPref.getEntry());
		    }
	}

}
