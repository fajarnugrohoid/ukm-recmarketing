package com.ukm.customerUKM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ukm.facebookLogin.Login_backup;

public class MenuFragment extends ListFragment {
	
	String nohp,pin;
	String newString;

	Intent intent = null;
	
	// Array of strings storing country names
    String[] countries = new String[] {
        "Beranda",
        "List Produk",
        "Search Detail",
        "Get Voucher",
        "Keluar"
    };
 
    // Array of integers points to images stored in /res/drawable/
    int[] flags = new int[]{
        R.drawable.home,
        R.drawable.list,
        R.drawable.insert,
        R.drawable.insert,
        R.drawable.logout
    };
 
    
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	 
		// Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
 
        for(int i=0;i<=4;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", " " + countries[i]); 
            hm.put("flag", Integer.toString(flags[i]) );
            aList.add(hm);
        }
 
        // Keys used in Hashmap
        String[] from = { "flag","txt" };
 
        // Ids of views in listview_layout
        int[] to = { R.id.flag,R.id.txt};
 
        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.list_item, from, to);
        
        setListAdapter(adapter); 
        /*
        setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, countries)); */
        getListView().setCacheColorHint(0);
        /*
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.row, ids)); 
		ItemAdapter adapter = new ItemAdapter(getActivity(),R.layout.row, ids);
		*/
        
        
        
	}
 

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		

		Intent extras = getActivity().getIntent(); 
		
		Intent intent = null;
		((MenuActivity)getActivity()).getSlideoutHelper().close();
		switch (position) {
		case 0:
			intent = new Intent(getActivity().getApplicationContext(),Home.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    break;
		case 1:
			intent = new Intent(getActivity().getApplicationContext(),List_produk.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    break;
		case 2:
			intent = new Intent(getActivity().getApplicationContext(),SearchDetail.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    break;
		case 3:
			intent = new Intent(getActivity().getApplicationContext(),GetVoucher.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    break;
		case 4:
			intent = new Intent(getActivity().getApplicationContext(),Login_backup.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    break;
		    
		}
		
		startActivity(intent);
	}

	
	 

 
	
}
