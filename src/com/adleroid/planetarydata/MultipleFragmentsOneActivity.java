package com.adleroid.planetarydata;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MultipleFragmentsOneActivity extends Activity implements TitlesFragment.OnTitleSelectedListener {
	
	private final static String TITLES_FRAGMENT_TAG = "titles";
	private final static String DETAILS_FRAGMENT_TAG = "details";
	private final static int TITLES_ID = 101;
	private final static int DETAILS_ID = 102;

	private static final String ARG_DETAILS_DISPLAYED = "detailsDisplayed";

	private boolean mDualPane;
	private boolean mDetailsDisplayed; // only important when activity is
										// displaying one fragment only
	private int mCurrentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_activity);

		// make sure to restore fragments if necessary
		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(
					DetailsFragment.ARG_INDEX, 0);
			mDetailsDisplayed = savedInstanceState.getBoolean(
					ARG_DETAILS_DISPLAYED);

			// the inner layouts containing the fragments are not automatically
			// recreated by android so we need to recreate them here
			// see below for why inner layouts were necessary
			if (isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
				showBothFragments();
			}
		} else {
			if (isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
				showBothFragments();
			} else {
				showOneFragment();
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(DetailsFragment.ARG_INDEX, mCurrentIndex);
		outState.putBoolean(ARG_DETAILS_DISPLAYED, mDetailsDisplayed);
	}

	/**
	 * Mirrors SDK method: Configuration.isLayoutSizeAtLeast, API 11+
	 * 
	 * @param size
	 * @return
	 */
	private boolean isLayoutSizeAtLeast(int size) {
		Configuration config = getResources().getConfiguration();
		final int screenSize = config.screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return (screenSize == Configuration.SCREENLAYOUT_SIZE_UNDEFINED) ? false
				: screenSize >= size;
	}

	/**
	 * Need to handle recreating fragment state when activity is recreated,
	 * for example, after a configuration change.
	 */
	private void showOneFragment() {
		mDualPane = false;

		FragmentManager fm = getFragmentManager();
		TitlesFragment titlesFragment = new TitlesFragment();
		FragmentTransaction trans = fm.beginTransaction();
		trans.add(android.R.id.content, titlesFragment, TITLES_FRAGMENT_TAG);
		trans.commit();

		if (mDetailsDisplayed) {
			displayDetailsOnly();
		}
	}

	private void showBothFragments() {
		mDualPane = true;

		// we need to add inner layouts to the main layout to account for
		// the weights of the fragments in our linear layout
		FragmentManager fm = getFragmentManager();
		TitlesFragment titlesFragment = new TitlesFragment();
		LinearLayout layout = (LinearLayout) findViewById(R.id.one_activity_parent_layout);
		FrameLayout titlesLayout = new FrameLayout(this);
		titlesLayout.setId(TITLES_ID);
		titlesLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 2.0f));
		FragmentTransaction trans = fm.beginTransaction();
		trans.add(TITLES_ID, titlesFragment, TITLES_FRAGMENT_TAG);
		trans.commit();
		layout.addView(titlesLayout);

		FrameLayout detailsLayout = new FrameLayout(this);
		detailsLayout.setId(DETAILS_ID);
		detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 3.0f));
		DetailsFragment detailsFragment = new DetailsFragment();
		Bundle args = new Bundle();
		titlesFragment = (TitlesFragment) fm.findFragmentById(TITLES_ID);
		args.putInt(DetailsFragment.ARG_INDEX, mCurrentIndex);
		detailsFragment.setArguments(args);
		trans = fm.beginTransaction();
		trans.add(DETAILS_ID, detailsFragment, DETAILS_FRAGMENT_TAG);
		trans.commit();
		layout.addView(detailsLayout);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		mDetailsDisplayed = false;
	}

	@Override
	public void onTitleSelected(int position) {
		mCurrentIndex = position;

		if (mDualPane) {
			// change content of details fragment
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment)
					fm.findFragmentById(DETAILS_ID);
			if (details == null || details.getCurrentIndex() != position) {
				// recreate the details fragment with the new position
				// and replace the old one
				details = DetailsFragment.create(position);
				FragmentTransaction trans = fm.beginTransaction();
				trans.replace(DETAILS_ID, details);
				trans.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
				trans.commit();
			}
		} else {
			displayDetailsOnly();
		}
	}

	private void displayDetailsOnly() {
		// replace titles fragment with details fragment
		mDetailsDisplayed = true;
		FragmentManager fm = getFragmentManager();
		DetailsFragment details = (DetailsFragment)
				fm.findFragmentById(TITLES_ID);
		if (details == null || details.getCurrentIndex() != mCurrentIndex) {
			details = DetailsFragment.create(mCurrentIndex);

			FragmentTransaction trans = fm.beginTransaction();
			trans.replace(
					fm.findFragmentByTag(TITLES_FRAGMENT_TAG).getId(),
					details);
			trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			// need to add to the back stack so that the back key will go
			// back to the titles fragment
			trans.addToBackStack(null);
			trans.commit();
		}
	}
}
