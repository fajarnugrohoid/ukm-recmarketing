package com.ukm.customerUKM;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_transaction_item, null);

        TextView title = (TextView)vi.findViewById(R.id.txt_product_name); // title
        TextView subtitle = (TextView)vi.findViewById(R.id.duration); // subtitle
        TextView price = (TextView)vi.findViewById(R.id.txt_price); // price
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.image_view); // thumb image
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        title.setText(song.get(List_produk.KEY_TITLE));
        subtitle.setText(song.get(List_produk.KEY_SUBTITLE));
        price.setText(song.get(List_produk.KEY_PRICE));
        imageLoader.DisplayImage(song.get(List_produk.KEY_THUMB_URL), thumb_image);
        return vi;
    }
    
    
    
    
    /*public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                menuItems = (   ArrayList<HashMap<String, String>>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<HashMap<String, String>> FilteredArrList = new ArrayList<HashMap<String, String>>();

                if (mDisplayedValues == null) {
                    mDisplayedValues = new ArrayList<HashMap<String, String>>(menuItems); // saves the original data in mOriginalValues
                }

                *//********
                 * 
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********//*
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = mDisplayedValues.size();
                    results.values = mDisplayedValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mDisplayedValues.size(); i++) {
                        String data =      mDisplayedValues.get(i).get(ServerConstant.key_event_name);
                        Log.v("publish result", data);
                        if (data.toLowerCase().startsWith(constraint.toString())) {

                            Log.i("publish result match",constraint.toString() +"hiiiii " + data);
                            FilteredArrList.add(mDisplayedValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

			@Override
			public boolean onLoadClass(Class arg0) {
				// TODO Auto-generated method stub
				return false;
			}
        };
        return filter;
    }*/
    
    
}