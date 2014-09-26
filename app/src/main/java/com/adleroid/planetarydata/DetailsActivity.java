package com.adleroid.planetarydata;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// if the screen is now in landscape mode, we can return to the launcher activity,
			// which will show both the titles and details, so we don't need this activity
			finish();
		} else {
			// only need to set up fragment upon first initialization since framework 
			// takes care of recreating it upon re-initialization
			if (savedInstanceState == null) {
				DetailsFragment detailsFragment = new DetailsFragment();
				detailsFragment.setArguments(getIntent().getExtras());
				FragmentTransaction trans = getFragmentManager().beginTransaction();
				trans.add(android.R.id.content, detailsFragment);
				trans.commit();
			}
		}
	}
}
