package com.adleroid.planetarydata;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

	private int mIndex;

	/** Bundle argument used to display appropriate details data */
	public static final String ARG_INDEX = "index";

	/**
	 * Factory method to create a DetailsFragment and set its index to that
	 * provided.
	 * 
	 * @param index
	 * @return
	 */
	public static DetailsFragment create(int index) {
		DetailsFragment df = new DetailsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_INDEX, index);
		df.setArguments(args);
		return df;
	}

	/**
	 * Now that the activity has been created we can retrieve the index stored
	 * in the arguments.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mIndex = getArguments().getInt(ARG_INDEX, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;

		// only one of our layouts has a container which will hold this fragment
		// so only create view if fragment is part of container, otherwise
		// it will not be displayed and there's no point in constructing the view
		if (container != null) {
			Bundle args = getArguments();
			mIndex = args != null ? args.getInt(ARG_INDEX, 0) : 0;
			final Data.Details dd = Data.DETAILS[mIndex];
			view = inflater.inflate(R.layout.details_layout, container, false);
			TextView text = (TextView) view.findViewById(R.id.detailsText);
			text.setText(dd.description);
			text.setCompoundDrawablePadding(8);
			text.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(dd.resId),
					null,
					null);
		}
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_INDEX, mIndex);
	}

	/**
	 * @return index of currently displayed details
	 */
	public int getCurrentIndex() {
		return mIndex;
	}
}
