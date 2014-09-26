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
	private boolean mDetailsDisplayed; // only important when activity is displaying one fragment only
	private int mCurrentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_activity);

		// restore from saved instance if necessary
		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(DetailsFragment.ARG_INDEX, 0);
			mDetailsDisplayed = savedInstanceState.getBoolean(ARG_DETAILS_DISPLAYED);
		}

		// if display is large or in landscape orientation we can show both titles and details fragments
		Configuration config = getResources().getConfiguration();
		boolean isLargeOrLandscape = config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)
				|| config.orientation == Configuration.ORIENTATION_LANDSCAPE;
		if (isLargeOrLandscape) {
			showBothFragments();
		} else {
			showOneFragment();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(DetailsFragment.ARG_INDEX, mCurrentIndex);
		outState.putBoolean(ARG_DETAILS_DISPLAYED, mDetailsDisplayed);
	}

	private void removeDetailsFragment() {
		FragmentManager fm = getFragmentManager();
		DetailsFragment detailsFragment = (DetailsFragment) fm.findFragmentById(DETAILS_ID);
		if (detailsFragment != null && detailsFragment.isAdded()) {
			FragmentTransaction trans = fm.beginTransaction();
			trans.remove(detailsFragment);
			trans.commit();
		}
	}

	private void removeTitlesFragment() {
		FragmentManager fm = getFragmentManager();
		TitlesFragment titlesFragment = (TitlesFragment) fm.findFragmentById(TITLES_ID);
		if (titlesFragment != null && titlesFragment.isAdded()) {
			FragmentTransaction trans = fm.beginTransaction();
			trans.remove(titlesFragment);
			trans.commit();
		}
	}

	/**
	 * Need to handle recreating fragment state when activity is recreated,
	 * for example, after a configuration change.
	 */
	private void showOneFragment() {
		mDualPane = false;

		LinearLayout layout = (LinearLayout) findViewById(R.id.one_activity_parent_layout);
		layout.removeAllViews();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		if (mDetailsDisplayed) {
			removeTitlesFragment();

			DetailsFragment detailsFragment = new DetailsFragment();
			trans.add(R.id.one_activity_parent_layout, detailsFragment, DETAILS_FRAGMENT_TAG);
			trans.commit();
		} else {
			removeDetailsFragment();

			TitlesFragment titlesFragment = new TitlesFragment();
			trans.add(R.id.one_activity_parent_layout, titlesFragment, TITLES_FRAGMENT_TAG);
			trans.commit();
		}
	}

	private void showBothFragments() {
		mDualPane = true;

		// we are using a linear layout but since fragments do not support weights we need to add inner layouts,
		// so the resulting layout will look like:
		// <LinearLayout>
		//     <FrameLayout android:weight="1" ... >
		//         <fragment "titles">
		//     </FrameLayout>
		//     <FrameLayout android:weight="2" ... >
		//         <fragment "details">
		//     </FrameLayout>
		// </LinearLayout>
		
		// clear out any existing layout views and fragments
		LinearLayout layout = (LinearLayout) findViewById(R.id.one_activity_parent_layout);
		layout.removeAllViews();
		removeDetailsFragment();
		removeTitlesFragment();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();

		FrameLayout titlesLayout = new FrameLayout(this);
		titlesLayout.setId(TITLES_ID);
		titlesLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
		TitlesFragment titlesFragment = new TitlesFragment();
		trans.add(TITLES_ID, titlesFragment, TITLES_FRAGMENT_TAG);
		trans.commit();
		layout.addView(titlesLayout);

		FrameLayout detailsLayout = new FrameLayout(this);
		detailsLayout.setId(DETAILS_ID);
		detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 2.0f));
		DetailsFragment detailsFragment = new DetailsFragment();
		// add current index to fragment so that we display the correct details
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

		// if back is pressed while only details fragment is displayed (mDualMode == false), we will 
		// return to titles fragment and if not the activity will finish - either way we can safely 
		// set the details displayed flag to false here
		mDetailsDisplayed = false;
	}

	@Override
	public void onTitleSelected(int position) {
		mCurrentIndex = position;

		if (mDualPane) {
			// change content of details fragment
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm.findFragmentById(DETAILS_ID);
			if (details == null || details.getCurrentIndex() != position) {
				// recreate the details fragment with the new position and replace the old one
				details = DetailsFragment.create(position);
				FragmentTransaction trans = fm.beginTransaction();
				trans.replace(DETAILS_ID, details);
				trans.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
				trans.commit();
			}
		} else {
			// replace titles fragment with details fragment
			mDetailsDisplayed = true;
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm.findFragmentById(DETAILS_ID);
			if (details == null || details.getCurrentIndex() != mCurrentIndex) {
				details = DetailsFragment.create(mCurrentIndex);

				FragmentTransaction trans = fm.beginTransaction();
				trans.replace(fm.findFragmentByTag(TITLES_FRAGMENT_TAG).getId(), details);
				trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// need to add to the back stack so that the back key will go back to the titles fragment
				// no need to name the back stack state so just pass null
				trans.addToBackStack(null);
				trans.commit();
			}
		}
	}
}
