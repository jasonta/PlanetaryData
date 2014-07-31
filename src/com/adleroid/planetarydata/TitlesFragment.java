package com.adleroid.planetarydata;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Displays a list of titles, each of which can be clicked on to display
 * the contents using DetailsFragment.
 */
public class TitlesFragment extends ListFragment {

	private OnTitleSelectedListener mCallback = null;

	/**
	 * Container Activity must implement this callback.
	 */
	public interface OnTitleSelectedListener {
		public void onTitleSelected(int position);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		TitleAdapter adapter = new TitleAdapter(getActivity());
		setListAdapter(adapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnTitleSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTitleSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mCallback != null) {
			mCallback.onTitleSelected(position);
		}
	}

	private static class TitleAdapter extends BaseAdapter {

		private LayoutInflater mLayoutInflater;

		public TitleAdapter(final Context context) {
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return Data.DETAILS.length;
		}

		@Override
		public Object getItem(int position) {
			return Data.DETAILS[position].title;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {
				View layout = mLayoutInflater.inflate(
						R.layout.title_item_layout, parent, false);
				tv = (TextView) layout.findViewById(R.id.titleText);
				tv.setText(Data.DETAILS[position].title);
			} else {
				tv = (TextView) convertView;
			}
			return tv;
		}
	}
}