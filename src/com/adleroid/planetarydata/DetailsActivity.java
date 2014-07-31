package com.adleroid.planetarydata;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class DetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			DetailsFragment df = new DetailsFragment();
			df.setArguments(getIntent().getExtras());
			FragmentTransaction trans = getFragmentManager().beginTransaction();
			trans.add(android.R.id.content, df);
			trans.commit();
		}
	}
}
