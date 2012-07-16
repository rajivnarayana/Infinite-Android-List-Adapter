package com.webileapps.infiniteadapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webileapps.infiniteadapter.InfiniteAdapter.InfiniteAdapterDelegate;

public class InfiniteAdapterDemoActivity extends ListActivity implements InfiniteAdapterDelegate {
	private int count = 0;
	private InfiniteAdapter adapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	ArrayAdapter<String> myAdapter = 
    	new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1) {
    		
    		@Override
    		public int getCount() {
    			return count;
    		}
    		
    		@Override
    		public String getItem(int position) {
    			return "Cell "+position;
    		}
    	};
    	
    	TextView loadingView = new TextView(this);
    	loadingView.setText("Loading...");
    	
    	TextView endOfListView = new TextView(this);
    	endOfListView.setText("--End of list--");

    	adapter = new InfiniteAdapter(myAdapter, loadingView, endOfListView, this);
    	setListAdapter(adapter);
    }
    
    private android.os.Handler mHandler = new android.os.Handler();
    
	@Override
	public void loadPage(int pageNo) {
		mHandler.postDelayed(new Runnable(){
			public void run() {
				runOnUiThread(new Runnable(){
					public void run() {
						count += 25;
						if(count > 200)
							adapter.setEndOfList();
						else
							adapter.doneRefreshing();
					}
				});
			}
		}, 3000);
	}
}