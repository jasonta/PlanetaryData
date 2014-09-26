package com.adleroid.planetarydata;

import java.lang.ref.WeakReference;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adleroid.planetarydata.Data.Details;

public class DetailsFragment extends Fragment {

	private int mIndex;
	private ProgressBar mProgressBar;
	private TextView mText;

	/**
	 * We use a WeakReference to create a loose coupling between the fragment and the
	 * async task. This prevents the fragment from holding onto a reference to the
	 * task and allows it to be garbage-collected.
	 */
	private WeakReference<LoadDetailsTask> mTaskWeakRef;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LoadDetailsTask task = new LoadDetailsTask(this);
		mTaskWeakRef = new WeakReference<DetailsFragment.LoadDetailsTask>(task);
		mIndex = getArguments().getInt(ARG_INDEX, 0);
	}

	/**
	 * Now that the activity has been created we can retrieve the index stored
	 * in the arguments.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;

		// only one of our layouts has a container which will hold this fragment
		// so only create view if fragment is part of container, otherwise
		// it will not be displayed and there's no point in constructing the view
		if (container != null) {
			view = inflater.inflate(R.layout.details_layout, container, false);
			mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);
			mText = (TextView) view.findViewById(R.id.detailsText);

			// now that we have a valid text view, execute our async task to retrieve the
			// data if it's not already running
			if (mTaskWeakRef != null
					&& mTaskWeakRef.get() != null
					&& !mTaskWeakRef.get().getStatus().equals(Status.FINISHED)) {
				mTaskWeakRef.get().execute(mIndex);
			}
		}
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTaskWeakRef != null && mTaskWeakRef.get() != null) {
			mTaskWeakRef.get().cancel(true);
		}
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

	/**
	 * Asynchronous task to download details (just simulates server-side call for now.)
	 * 
	 * Template parameters are:
	 * Params = index of data to load
	 * Progress = integer representing percentage (unused)
	 * Result = Data.Details
	 */
	private class LoadDetailsTask extends AsyncTask<Integer, Integer, Data.Details> {

		/**
		 * We use a WeakReference to prevent holding on to a reference to the fragment,
		 * so that it can be garbage-collected.
		 */
		private WeakReference<DetailsFragment> mFragmentWeakRef;

		private LoadDetailsTask(final DetailsFragment fragment) {
			mFragmentWeakRef = new WeakReference<DetailsFragment>(fragment);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// make sure the progress bar is shown before starting work
			final DetailsFragment fragment = mFragmentWeakRef.get();
			if (fragment != null && fragment.isAdded() && mProgressBar != null && !mProgressBar.isShown()) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Data.Details doInBackground(Integer... params) {
			Data.Details details = null;
			if (params != null && params.length == 1) {
				// technically we already have the index from the fragment Intent
				// but this demonstrates how to use parameters in an AsyncTask
				int index = params[0];

				// simulate making a server-side call by sleeping for a bit
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					Log.e(getClass().getCanonicalName(), "error sleeping: " + e);
				}

				// get the actual data
				details = Data.DETAILS[index];
			}
			return details;
		}

		@Override
		protected void onPostExecute(Details result) {
			super.onPostExecute(result);

			// make sure fragment is still valid and attached to activity
			final DetailsFragment fragment = mFragmentWeakRef.get();
			if (fragment != null && fragment.isAdded()) {
				// hide the progress bar now that we've finished the task
				if (mProgressBar != null && mProgressBar.isShown()) {
					mProgressBar.setVisibility(View.GONE);
				}

				// update our TextView with the results from doInBackground
				if (result != null && mText != null) {
					mText.setText(result.description);
					mText.setCompoundDrawablePadding(8);
					mText.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(result.resId),
							null,
							null);
				}
			}
		}
	}
}
