package com.adleroid.planetarydata;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TitlesActivity extends Activity implements TitlesFragment.OnTitleSelectedListener {

	private static final String DETAILS_FRAGMENT_TAG = "details";
	private static final String ARG_CURRENT_INDEX = "currentIndex";
	private static final String ARG_SHOWN_INDEX = "shownIndex";

	private int mCurrentIndex = 0;
	private int mShownIndex = -1;
	private boolean mDualPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.titles_activity);

		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(ARG_CURRENT_INDEX);
			mShownIndex = savedInstanceState.getInt(ARG_SHOWN_INDEX);
		}

		// we can determine if the layout supports both titles and details by checking for the
		// existence of the detailsLayout since that view is only present in 'dual pane' mode
		View detailsFrame = findViewById(R.id.detailsLayout);
		mDualPane = (detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE);

		if (mDualPane) {
			showDetails(mCurrentIndex);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_CURRENT_INDEX, mCurrentIndex);
		outState.putInt(ARG_SHOWN_INDEX, mShownIndex);
	}

	@Override
	public void onTitleSelected(final int position) {
		showDetails(position);
	}

	private void showDetails(final int index) {
		mCurrentIndex = index;
		if (mDualPane) {
			// change content of details fragment
			if (mShownIndex != mCurrentIndex) {
				mShownIndex = mCurrentIndex;
				FragmentManager fm = getFragmentManager();
				DetailsFragment df = DetailsFragment.create(index);
				FragmentTransaction trans = fm.beginTransaction();
				trans.replace(R.id.detailsLayout, df, DETAILS_FRAGMENT_TAG);
				trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				trans.commit();
			}
		} else {
			Intent intent = new Intent(this, DetailsActivity.class);
			intent.putExtra(DetailsFragment.ARG_INDEX, index);
			startActivity(intent);
		}
	}
}
