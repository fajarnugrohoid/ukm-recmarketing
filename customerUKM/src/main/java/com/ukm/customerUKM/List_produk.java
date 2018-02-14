package com.ukm.customerUKM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.ukm.app.android.SlideoutActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class List_produk extends Activity {
	private LinearLayout menu;
	private LinearLayout Linear;
	
	
	TextView jumlahsaldo,key_nohp;
	private String url = Global.url_global + "mbarang/barang/format/json";
	private ListView lv,lv_recommender;
	private Button load_more,btnSearch;
	private String jsonResult;
	
	// XML node keys
	static final String KEY_ITEM = "item"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_NAME = "name";
	String keyno;
	int current_page = 0;
	String[] menuItems;
	ProgressDialog pDialog, pd;
	ArrayList<String> passing;
	HashMap<String, String> map = new HashMap<String, String>();
	List<HashMap<String,String>> aList;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	EditText inputSearch; 
	Button btnLoadMore;
	ArrayList<String> list = new ArrayList<String>();
	SimpleAdapter adapter;
	LazyAdapter adapterLazy;
    FriendsListAdapter adapterFriends;
	private ImageView varImageView;

	static final String KEY_TITLE = "title";
	static final String KEY_SUBTITLE = "subtitle";
	static final String KEY_PRICE = "price";
	static final String KEY_THUMB_URL = "thumb_url";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_product);
		
		
		

		//menu=(Button)findViewById(R.id.sample_button);
		menu = (LinearLayout) findViewById(R.id.sample_button_view); 
		
		

		menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
				SlideoutActivity.prepare(List_produk.this, R.id.inner_content, width);
						
				Intent intent = new Intent(List_produk.this, MenuActivity.class);
					 
					startActivity(intent);
				overridePendingTransition(0, 0);
				 
			}
		});
		
		lv = (ListView) findViewById(R.id.listView1);
		lv_recommender = (ListView) findViewById(R.id.listView_recommender);
		varImageView = (ImageView) findViewById(R.id.image_view);
		inputSearch = (EditText)findViewById(R.id.inputSearchTrans);
        inputSearch.addTextChangedListener(filterTextWatcher);

		lv.setTextFilterEnabled(true);
		lv.requestFocus();

		
		/*btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				//accessWebService_Search(inputSearch.getText().toString());
				// TODO Auto-generated method stub
				System.out.println("Masuk Search");
				passing.clear();
				adapter.clear(); 
				
				current_page = 0;
				passing.add(String.valueOf(current_page));
				passing.add(inputSearch.getText().toString());
				new getListBarang().execute(passing);
			}
		});
		*/

		
		System.out.println("Awalan");
		//passing.clear();
//		passing.add(String.valueOf(current_page));
//		passing.add(inputSearch.getText().toString());
		new getListBarang().execute(); 
	}
	


	@Override
	protected void onResume() {
	    super.onResume();
	    lv.requestFocus();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    inputSearch.removeTextChangedListener(lv);
	}

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            //adapterFriends.getFilter().filter(s);
            if(s.toString().equalsIgnoreCase(""))
            {
            	//adapterFriends.setListData(FriendsListAdapter.this);
            	adapterFriends.notifyDataSetChanged();
            }
            else
            	adapterFriends.getFilter().filter(s);
        }

    };

	class getListBarang extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>{
		private String json;
		private JSONObject jsonobject,arrdata;
		private String code,msg,arrjsondata;
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			//pd = new ProgressDialog(List_produk.this);
//			pd.setTitle("Please Wait...");
		    //pd.setMessage("Loading..");
			//pd.setCancelable(false);
			//pd.show();
		}
		@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... passing) {
			// TODO Auto-generated method stub
			
			ArrayList<String> result = new ArrayList<String>();
//			ArrayList<String> param = passing[0];
//			System.out.println("Limit:"+param + "-result" + result);
//			String getLimit = param.get(0);
//			System.out.println("Limit:"+getLimit);
//			String getSearch = param.get(1);
//			System.out.println("Search:"+getSearch);
			
			try 
			{
				//System.out.println(url + "?offset=" + getLimit +  "&nohp=" + keyno + "&search=" + getSearch);
				System.out.println(url);
				json=JSONfunctions.getStringContent(url);
				arrdata = new JSONObject(json);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result)
		{
			super.onPostExecute(result);
			//pd.dismiss();
			aList = new ArrayList<HashMap<String,String>>();
			 //String[] from = { "noresi","title","artist","stat", "image_view"};
			 
		        // Ids of views in listview_layout
		    // int[] to = { R.id.txt_noresi,R.id.title,R.id.txt_notujuan,R.id.duration, R.id.image_view};
			
			//adapter = new ArrayAdapter<String>(ListTransaksi.this, android.R.layout.simple_list_item_1, list);
			//adapter = new SimpleAdapter(List_produk.this, aList, R.layout.list_transaction_item, from, to);
			ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
			 try {
				   String status = arrdata.getString("countitem");
				   JSONArray jsonMainNode = arrdata.getJSONArray("datas");
				   System.out.println("status:" + status);
				   if(!status.equalsIgnoreCase("0"))
				   {
					   System.out.println("jsonmainnode:" + jsonMainNode);
					   /*
					   HashMap<String, String> map = new HashMap<String, String>();
					   for (int i = 0; i < jsonMainNode.length(); i++) {
						    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						    
						    String noresi = jsonChildNode.optString("NoResi");
						    String notujuan = jsonChildNode.optString("notujuan");
						    String kodebrg = jsonChildNode.optString("kodebrg");
						    String stat = jsonChildNode.optString("status");
						    String outPut = noresi + ". " + notujuan + " " + kodebrg + " - " + stat;
						    
						    list.add(outPut);
					   }*/
					   /*aList = new ArrayList<HashMap<String,String>>();
					   
					   for (int i = 0; i < jsonMainNode.length(); i++) {
						   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				            HashMap<String, String> hm = new HashMap<String,String>();
				            
				            hm.put("noresi", jsonChildNode.optString("id"));
				            hm.put("title",jsonChildNode.optString("nama_barang") + " - " + jsonChildNode.optString("kategori"));
				            hm.put("artist",jsonChildNode.optString("asal_daerah"));
				            hm.put("stat",jsonChildNode.optString("harga_toko"));
				            hm.put("image_view",jsonChildNode.optString("photo1"));
				            
//				            String tempPath = Global.base_url + jsonChildNode.optString("photo1");
//							Bitmap bm;
//							BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//							bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
//							varImageView.setImageBitmap(bm);
							
							//varImageView.setImageResource(jsonChildNode.optString("photo1"));
				            //varImageView.setImageResource((int)hm.get("photo1"));
							
				            //hm.put("image_view",jsonChildNode.optString("photo1"));
				            aList.add(hm);
				            
				            System.out.println("nama_barang" + jsonChildNode.optString("nama_barang"));
				        }*/
					   //adapter.notifyDataSetChanged();
//					   adapter = new SimpleAdapter(List_produk.this, aList, R.layout.list_transaction_item, from, to);
//					   lv.setAdapter(adapter);
					   
					   for (int i = 0; i < jsonMainNode.length(); i++) {
							// creating new HashMap
							
							JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							
							// adding each child node to HashMap key => value
							
							
							map.put(KEY_ID,jsonChildNode.optString("id"));
							map.put(KEY_TITLE, jsonChildNode.optString("nama_barang"));
							map.put(KEY_SUBTITLE, jsonChildNode.optString("asal_daerah"));
							map.put(KEY_PRICE, jsonChildNode.optString("harga_toko"));
							map.put(KEY_THUMB_URL, jsonChildNode.optString("photo1"));

							// adding HashList to ArrayList
							songsList.add(map);
						}

                       adapterFriends = new FriendsListAdapter(List_produk.this, songsList);
                       lv.setAdapter(adapterFriends);
                       
                       adapterFriends = new FriendsListAdapter(List_produk.this, songsList);
                       lv_recommender.setAdapter(adapterFriends);
                       
                       
					   //adapterLazy=new LazyAdapter(List_produk.this, songsList);        
					   //lv.setAdapter(adapterLazy);

					   
				   }else{
					   Toast.makeText(getApplicationContext(), "Data Kosong" ,Toast.LENGTH_SHORT).show();
				   }
			  } catch (JSONException e) {
				  Toast.makeText(getApplicationContext(), "Data Kosong" ,Toast.LENGTH_SHORT).show();
			  }
			
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	    this.finish();
	}


}
