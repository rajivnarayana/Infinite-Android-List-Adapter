package com.webileapps.infiniteadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class InfiniteAdapter extends BaseAdapter {
	
	public void doneRefreshing() {
		pageIndex++;
		refreshing = false;
		notifyDataSetChanged();
	}
	
	public void setEndOfList() {
		endOfList = true;
		refreshing = false;
		notifyDataSetChanged();
	}
	
	private int pageIndex = 0;
	
	private boolean refreshing = true;
	private boolean endOfList = false;
	
	private ListAdapter mAdapter;
	
	public interface InfiniteAdapterDelegate {
		public void loadPage(int pageNo); 
	}

	private InfiniteAdapterDelegate mDelegate;
	private View loadingView;
	private View endOfListView;
	
	public InfiniteAdapter(ListAdapter adapter, View loadingView, View endOfListView, InfiniteAdapterDelegate delegate) {
		this.mAdapter = adapter;
		this.loadingView = loadingView;
		this.endOfListView = endOfListView; 
		this.mDelegate = delegate;
		refreshing = false;
		endOfList = false;
	}
	
	@Override
	public int getCount() {
		if(endOfList) {
			return mAdapter.getCount() + 1; //for EOL
		}
//		} else if (refreshing){
//			return mAdapter.getCount() + 1; //for showing spinner.
//		}
		return mAdapter.getCount() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position == getCount() - 1)
		return null;
		return mAdapter.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		if (position == getCount() - 1)
		return 0;
		return mAdapter.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == 1)
			return endOfListView;
		
		if(getItemViewType(position) == 0) {
			if (!refreshing) {
				//load next page.
				android.util.Log.v("Infinite","load next page");
				mDelegate.loadPage(pageIndex);
			}
			return loadingView;
		}
		
		return mAdapter.getView(position, convertView, parent);
	}
	
	@Override
	public int getViewTypeCount() {
		return mAdapter.getViewTypeCount() + 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position == getCount() - 1) {
			if (endOfList)
				return 1;
			return 0;
		}
		return 2 + mAdapter.getItemViewType(position);
	}
}