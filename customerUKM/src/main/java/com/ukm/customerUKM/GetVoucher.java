package com.ukm.customerUKM;
 


import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.lib.ukm.app.android.*;


public class GetVoucher extends Activity implements OnClickListener {
	
	
    
    
	SQLiteDatabase mydb;
	public static String DBNAME = "local_db.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    public static String TABLE = "voucher";       // THIS IS THE TABLE NAME
   
	private static final int TAKE_PICTURE = 0;
	
	private LinearLayout menu;
	private Uri mUri;
	private Bitmap mPhoto;
	private String namafile;
	public int i = 0;
	public static String status="";

	private Button send;
	private Button cancel;
	
	private EditText val_jumlah,val_expire;
	
	
	
	private CheckBox val_anak;
	private CheckBox val_remaja;
	private CheckBox val_dewasa;
	private CheckBox val_lansia;
	private EditText val_foto_name;
	private Spinner val_produk;
	
	private String username, userid, str_expire, str_jumlah, varNameProduk, varIdProduk;

	private String[] selectNameProduk,selectIdProduk;
	private ArrayList<String> stringArrayList1;
	private ArrayList<String> stringArrayList2;

	private int year;
	private int month;
	private int day;
	
	
	private ImageButton ib;
 	private Calendar cal;
	
	SimpleDateFormat s1 = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
	String format = s1.format(new java.util.Date());
	SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
	String date = s2.format(new java.util.Date());
	SimpleDateFormat s3 = new SimpleDateFormat("HH:mm:ss");
	String time = s3.format(new java.util.Date());
	public Object s_nama_barang;
	
	@Override
	public void onBackPressed() 
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		//Home.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.set_voucher);
		 
		//menu=(Button)findViewById(R.id.sample_button);
		menu = (LinearLayout) findViewById(R.id.sample_button_insert); 
  
        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel);
        
        val_jumlah 			= (EditText) findViewById(R.id.val_jumlah);
        val_expire 			= (EditText) findViewById(R.id.val_expire);
        val_produk		=(Spinner)findViewById(R.id.val_produk);
        
        ib = (ImageButton) findViewById(R.id.imageButton1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        
        
     
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);

        System.out.println("setvoucher:x");
        
    	SharedPreferences pref = getApplicationContext().getSharedPreferences("log", MODE_PRIVATE); // 0 - for private mode
    	username =pref.getString("user", null);
    	userid =pref.getString("userid", null);
    	
        System.out.println("username:" + username + "-userid:" + userid);
      
        
        boolean answer;
		answer = isOnline();

		if (answer) {
			LocationManager L = (LocationManager) getSystemService(LOCATION_SERVICE);
			if (L.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				LocationListener ll = new mylocationlistener();
				L.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
						0, ll);
			} else {
				Toast.makeText(getApplicationContext(), "Aktifkan GPS Anda",
						3000).show();
				//Intent in = new Intent(
				//		android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				//startActivity(in);
			}
		} else {
			Toast.makeText(getApplicationContext(), "Aktifkan Internet Anda",
					3000).show();
		}
		 
		new ItemSelected().execute(val_produk);
		
		menu.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
						SlideoutActivity.prepare(GetVoucher.this, R.id.inner_content, width);
						
						Intent intent = new Intent(GetVoucher.this, MenuActivity.class);
					 
						startActivity(intent);
						overridePendingTransition(0, 0);
						 
					}
				});
		
		
		   ib.setOnClickListener(new View.OnClickListener() {
		       	 public void onClick(View v) {
		       	  // Do something in response to button click
		       		 showDialog(0);
		       	 }
		      });
	}
	
	
	
	 protected Dialog onCreateDialog(int id) {
		 return new DatePickerDialog(this, datePickerListener, year, month, day);
	 }

	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		 public void onDateSet(DatePicker view, int selectedYear,
				 int selectedMonth, int selectedDay) {
			 	val_expire.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
		 }
	 };
	

		

	
	public void onClick(View v) { 

		
		if(v.getId()==R.id.send){
			
			str_expire = val_expire.getText().toString().trim();
			str_jumlah = val_jumlah.getText().toString().trim();
	
			AlertDialog.Builder builder=new AlertDialog.Builder(GetVoucher.this);
			builder.setTitle("Kirim Data")
			.setMessage("Pilih lewat apa data akan dikirm!")
			.setPositiveButton("Internet",new DialogInterface.OnClickListener()
			{
					public void onClick(DialogInterface dialog,int id)
					{
						System.out.println("ini jika dijawab ya");
						status = "internet";
						new Check().execute();
				        insertIntoTable(); 
		         
	                    new Thread(new Runnable() {
	                        public void run() {
	                             runOnUiThread(new Runnable() {
	                                    public void run() {
	                                    	//messageText.setText("uploading started.....");
	                                    }
	                                });                      
	                           
	     	                                                          
	                        }
	                      }).start();      
				        
				        Intent intent = new Intent(getApplicationContext(), Home.class);
						startActivity(intent);
						GetVoucher.this.finish();
					
					}
			  })
			  .setNegativeButton("Simpan Saja",new DialogInterface.OnClickListener()
			  {
					public void onClick(DialogInterface dialog,int id) 
					{
						status = "Tersimpan";
				        insertIntoTable();
				        Toast.makeText(GetVoucher.this,
								"Tersimpan di device.", Toast.LENGTH_LONG)
								.show();
				        
				        Intent intent = new Intent(getApplicationContext(), Home.class);
						startActivity(intent);
						GetVoucher.this.finish();
					}
			  })
			  .show();
			
		}
		else if (v.getId()==R.id.cancel){
			Intent intent = new Intent(getApplicationContext(), Home.class);
			startActivity(intent);
			GetVoucher.this.finish();
		}
	}
	

	class Check extends AsyncTask {

		private String json;
		private JSONObject jsonobject;
		private String code;
		private String msg;
		public String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("test_data" + url);
			
		}

		@Override
		protected Object doInBackground(Object... params) {
				
					
					InputStream inputStream = null;
			
	        String result = ""; 
	        String res_usernmae="kosong";
	        try {
	            // 1. create HttpClient
	            HttpClient httpclient = new DefaultHttpClient();
	            // 2. make POST request to the given URL
	            HttpPost httpPOST = new    
	                   HttpPost( Global.url_global + "mvoucher/voucher/format/json");
	            String json = "";
	            // 3. build jsonObject
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("jumlah",str_jumlah);
	            jsonObject.put("expire",str_expire);
	            
	            varIdProduk = getSelectedProduk();
	            jsonObject.put("id_barang", varIdProduk);
	            jsonObject.put("user_ukm_id",userid);
	            
	            
	            // 4. convert JSONObject to JSON to String
	            json = jsonObject.toString();

	            // 5. set json to StringEntity
	            StringEntity se = new StringEntity(json);
	            // 6. set httpPost Entity
	            httpPOST.setEntity(se);
	            // 7. Set some headers to inform server about the type of the content   
	            httpPOST.setHeader("Accept", "application/json");
	            httpPOST.setHeader("Content-type", "application/json");
	            // 8. Execute POST request to the given URL
	            HttpResponse httpResponse = httpclient.execute(httpPOST);
	            // 9. receive response as inputStream
	                              inputStream = httpResponse.getEntity().getContent();
	            //                  // 10. convert inputstream to string
	                              if(inputStream != null){
	                            	  
	                                  result = JSONfunctions.convertStreamToString(inputStream);
	                                  JSONObject jObject = new JSONObject(result);
	                                  code = jObject.getString("status");
	                                  
	                                  
	                              }else{
	                                  result = "Did not work!";
	                              }
	            Log.d("test_respon", httpResponse.toString());
	        } catch (Exception e) {
	            Log.d("InputStream", e.getLocalizedMessage());
	        }
	        
	        Log.d("isi_result_stream", result);
	        Log.d("isi_code", code);
	        Log.d("isi_post", "isi_post:" + json);
	        return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (code.equalsIgnoreCase("1")) {
				Toast.makeText(GetVoucher.this, "Data Terkirim Lewat Internet.",
						Toast.LENGTH_LONG).show();
				  Intent intent = new Intent(getApplicationContext(), Home.class);
					startActivity(intent);
					GetVoucher.this.finish();
			} else {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	
	
	public void  insertIntoTable(){
    	
    	System.out.println("============== Ini Status ============= ");
    	System.out.println( status);
        try{
        	
        	 mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);

		 }catch(Exception e){
		           // Toast.makeText(getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
		        }
	}
	
	
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	
	private class mylocationlistener implements LocationListener {

		private String la;
		private String ln;

		// @Override
		public void onLocationChanged(Location location) {

			if (i == 0) {
				if (location != null) {
					double lat = location.getLatitude();
					double lng = location.getLongitude();
					System.out.println(lat + "(((" + lng);
					la = "" + lat;
					ln = "" + lng;
					i++;
				}
			}

		}

		public void onProviderDisabled(String provider) {

			Toast.makeText(GetVoucher.this,
					"Error onProviderDisabled", Toast.LENGTH_LONG).show();

		}

		public void onProviderEnabled(String provider) {

			Toast.makeText(GetVoucher.this, "onProviderEnabled",
					Toast.LENGTH_LONG).show();

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			Toast.makeText(GetVoucher.this, "onStatusChanged",
					Toast.LENGTH_LONG).show();

		}

	}
	  
	
	
	
	
	
	public String getSelectedProduk(){
		val_produk.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
            	
            	
                //secilenIlce = s.getSelectedItem().toString().trim();
            	
            	varNameProduk = arg0.getItemAtPosition(arg2).toString();
            	varIdProduk= stringArrayList1.get(arg2);
            	
                try {
                	varIdProduk = URLEncoder.encode(varIdProduk,"UTF-8");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        return varIdProduk;
    }
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub	
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
	
	class ItemSelected extends AsyncTask<Spinner, Void, String> {
		Spinner varProduk;
		String result = "fail";
		private String json1;
		private JSONObject jsonobject, arrDatas;
		private String noresi, code;
		private String datas;
		JSONArray  data;
		
		@Override
		protected String doInBackground(Spinner... params) {
			// TODO Auto-generated method stub
			this.varProduk = params[0];
			return GetSomething();
		}
		
		final String GetSomething()
		{
			stringArrayList1 = new ArrayList<String>();
          	stringArrayList2 = new ArrayList<String>();
	        
            //JSONObject json = JSONfunctions.getStringContent(Global.url_global + "mkategori/kategori/format/json");
          	
          	try {
          		json1=JSONfunctions.getStringContent(Global.url_global + "mbarang/barang/key/a.user_ukm_id/val/" + userid + "/page/0/format/json");
				jsonobject=new JSONObject(json1);
				code=jsonobject.getString("countitem");
				datas=jsonobject.getString("datas");
				Log.e("countitem", "countitem " + code);
				Log.e("datas", "datas " + datas);
				
//				JSONObject response1=jsonobject.getJSONObject("response");
//	          	response1.toString();
	        	JSONArray  data = jsonobject.getJSONArray("datas");

	        	Log.e("me_json", "data "+ data);
	        
	            for(int i=0;i<data.length();i++)
	            {						
	    			
	    			JSONObject e = data.getJSONObject(i);
	    			     stringArrayList1.add(e.getString("id"));
	    			     stringArrayList2.add(e.getString("nama_barang"));
	    			     Log.e("nama_barang", "nama_barang: "+ e.getString("nama_barang"));
	            }	
	            selectNameProduk = stringArrayList1.toArray(new String[stringArrayList1.size()]);
	    		selectNameProduk = stringArrayList2.toArray(new String[stringArrayList2.size()]);
	    		result = "success";

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			return result;
		}
		
		protected void onPostExecute(String page)
		{    	
			ArrayAdapter adapter = new ArrayAdapter(GetVoucher.this,android.R.layout.simple_spinner_item, selectNameProduk);
			varProduk.setAdapter(adapter);    	
			getSelectedProduk();
		}
	}
	
}
