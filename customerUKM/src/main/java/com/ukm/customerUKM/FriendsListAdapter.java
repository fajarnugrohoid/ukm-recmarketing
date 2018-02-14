package com.ukm.customerUKM;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class FriendsListAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    public ArrayList<HashMap<String, String>> data;
    public ArrayList<HashMap<String, String>> allData;
    public HashMap<String, String> friends;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private ItemsFilter mFilter;
    static SharedPreferences TODLoginPrefs;

    @SuppressWarnings({"unchecked", "static-access"})
    public FriendsListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = (ArrayList<HashMap<String, String>>) d;
        allData = (ArrayList<HashMap<String, String>>) data.clone();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        TODLoginPrefs = activity.getSharedPreferences("LogInSession",
                activity.MODE_PRIVATE);

    }

    public int getCount() {
    	System.out.println("data_count:" + data.size());
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_transaction_item, null);

        TextView title = (TextView) vi.findViewById(R.id.txt_product_name); // title
        TextView subtitle = (TextView) vi.findViewById(R.id.duration); // subtitle
        TextView price = (TextView) vi.findViewById(R.id.txt_price); // price
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.image_view); // thumb image

        System.out.println("data_get_view:" + data);
        friends = new HashMap<String, String>();
        friends = data.get(position);
        

        title.setText(friends.get(List_produk.KEY_TITLE));
        subtitle.setText(friends.get(List_produk.KEY_SUBTITLE));
        price.setText(friends.get(List_produk.KEY_PRICE));
        imageLoader.DisplayImage(friends.get(List_produk.KEY_THUMB_URL), thumb_image);

        return vi;
    }



    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemsFilter();
        }
        
        return mFilter;
    }

    private class ItemsFilter extends Filter {
        @SuppressWarnings("unchecked")
        @Override
        public String convertResultToString(Object resultValue) {
            return ((HashMap<String, String>) (resultValue))
                    .get(List_produk.KEY_TITLE);
        }

        @Override
        protected FilterResults performFiltering(CharSequence s) {
        	System.out.println("pencarian:" + s);
            if (s != null) {
                ArrayList<HashMap<String, String>> tmpAllData = allData;
                ArrayList<HashMap<String, String>> tmpDataShown = data;
                tmpDataShown.clear();
                
                System.out.println("tmpAllData:" + tmpAllData);
                for (int i = 0; i < tmpAllData.size(); i++) {
                    if (tmpAllData.get(i).get(List_produk.KEY_TITLE)
                            .toLowerCase()
                            .startsWith(s.toString().toLowerCase())) {
                        tmpDataShown.add(tmpAllData.get(i));
                    }
                }
                System.out.println("newfilterResults");
                
                FilterResults filterResults = new FilterResults();
                System.out.println("filterResults_values " + filterResults.values);
                System.out.println("filterResults_count " + filterResults.count);
                filterResults.values = tmpDataShown;
                filterResults.count = tmpDataShown.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

    
        protected void publishResults(CharSequence prefix, FilterResults results) {
           // data = (ArrayList<HashMap<String, String>>) results.values;
            data = (ArrayList<HashMap<String, String>>)results.values;
            System.out.println("results_data:" + data);
            System.out.println("results_count:" + results.count);
            
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}