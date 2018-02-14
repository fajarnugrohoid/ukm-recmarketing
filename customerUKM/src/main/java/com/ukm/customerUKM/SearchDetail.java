package com.ukm.customerUKM;
 


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lib.ukm.app.android.SlideoutActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;


public class SearchDetail extends Activity implements OnClickListener {

    
	SQLiteDatabase mydb;
	public static String DBNAME = "local_db.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    public static String TABLE = "barang";       // THIS IS THE TABLE NAME
   
	
	private LinearLayout menu;
	private Uri mUri;
	
	public int i = 0;
	public static String status="";

	private Button send;
	
	
	private EditText val_nama_barang,val_max_harga, val_min_harga,val_asal_daerah;
	
	
	
	private CheckBox val_anak;
	private CheckBox val_remaja;
	private CheckBox val_dewasa;
	private CheckBox val_lansia;
	private EditText val_foto_name;
	
	
	private String str_nama_barang;
	private String str_asal_daerah, str_min_harga, str_max_harga;
	private Boolean str_anak;
	private Boolean str_remaja;
	private Boolean str_dewasa;
	private Boolean str_lansia;
	private String  username, userid;
	
	

	private CharSequence[] rentang_usia_value={"1. Anak-anak","2. Remaja","3. Dewasa", "4. Lansia"};
	
	protected boolean[] rentang_usia_values={false,false,false,false};
	
	
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
		
		setContentView(R.layout.search_detail);
		 
		//menu=(Button)findViewById(R.id.sample_button);
		menu = (LinearLayout) findViewById(R.id.sample_button_insert); 
  
        send = (Button) findViewById(R.id.send);
        
        
        val_nama_barang 			= (EditText) findViewById(R.id.val_nama_barang);
        val_min_harga 				= (EditText) findViewById(R.id.val_min_harga);
        val_max_harga 				= (EditText) findViewById(R.id.val_max_harga);
        val_asal_daerah 			= (EditText) findViewById(R.id.val_asal_daerah);
        
        
        
        val_anak 	= (CheckBox) findViewById(R.id.val_anak);
        val_remaja 	= (CheckBox) findViewById(R.id.val_remaja);
        val_dewasa 	= (CheckBox) findViewById(R.id.val_dewasa);
        val_lansia 	= (CheckBox) findViewById(R.id.val_lansia);
        
        
        
        

        send.setOnClickListener(this);
        

        val_anak.setOnClickListener(this);
        val_remaja.setOnClickListener(this);
        val_dewasa.setOnClickListener(this);
        val_lansia.setOnClickListener(this);
    
        
        
    	SharedPreferences pref = getApplicationContext().getSharedPreferences("log", MODE_PRIVATE); // 0 - for private mode
    	username =pref.getString("user", null);
    	userid =pref.getString("userid", null);
    	
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
		 
		
		
		menu.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
						SlideoutActivity.prepare(SearchDetail.this, R.id.inner_content, width);
						
						Intent intent = new Intent(SearchDetail.this, MenuActivity.class);
					 
						startActivity(intent);
						overridePendingTransition(0, 0);
						 
					}
				});
	}
	
	
	

	public void onClick(View v) { 
		if (v.getId()==R.id.val_anak){
			val_anak.setChecked(false);
			get_range_age();
		}else if (v.getId()==R.id.val_remaja){
			val_remaja.setChecked(false);
			get_range_age();
		}else if (v.getId()==R.id.val_dewasa){
			val_dewasa.setChecked(false);
			get_range_age();
		}else if (v.getId()==R.id.val_lansia){
			val_lansia.setChecked(false);
			get_range_age();
		}
		
		if(v.getId()==R.id.send){
			
			str_nama_barang = val_nama_barang.getText().toString().trim();
			str_asal_daerah = val_asal_daerah.getText().toString().trim();
			str_min_harga = val_min_harga.getText().toString().trim();
			str_max_harga = val_max_harga.getText().toString().trim();
			
			str_anak = val_anak.isChecked();
			str_remaja = val_remaja.isChecked();
			str_dewasa = val_dewasa.isChecked();
			str_lansia = val_lansia.isChecked();
			
		 
		 
			
			
			AlertDialog.Builder builder=new AlertDialog.Builder(SearchDetail.this);
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
						SearchDetail.this.finish();
					
					}
			  })
			  .setNegativeButton("Simpan Saja",new DialogInterface.OnClickListener()
			  {
					public void onClick(DialogInterface dialog,int id) 
					{
						status = "Tersimpan";
				        insertIntoTable();
				        Toast.makeText(SearchDetail.this,
								"Tersimpan di device.", Toast.LENGTH_LONG)
								.show();
				        
				        Intent intent = new Intent(getApplicationContext(), Home.class);
						startActivity(intent);
						SearchDetail.this.finish();
					}
			  })
			  .show();
			
		}
		else if (v.getId()==R.id.cancel){
			Intent intent = new Intent(getApplicationContext(), Home.class);
			startActivity(intent);
			SearchDetail.this.finish();
		}
	}
	
	/*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case TAKE_PICTURE:
            if (resultCode == Activity.RESULT_OK) {
                getContentResolver().notifyChange(mUri, null);
                ContentResolver cr = getContentResolver();
                try {
                    mPhoto = android.provider.MediaStore.Images.Media.getBitmap(cr, mUri);
                    ((ImageView)findViewById(R.id.photo_holder)).setImageBitmap(mPhoto);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
	*/
	 

	
	
	private void get_range_age() {
		 Builder builder = new AlertDialog.Builder(this);
	     builder.setTitle("Answer");
	     builder.setMultiChoiceItems(rentang_usia_value, rentang_usia_values,
	         new DialogInterface.OnMultiChoiceClickListener()
	     {
	           @Override
	           public void onClick(DialogInterface dialog, int which,
	               boolean isChecked) 
	           {
	        	   rentang_usia_values[which] = isChecked;
	           }
	         });
	     builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
	       @Override
	       public void onClick(DialogInterface dialog, int which) {
	         for (int i = 0; i <rentang_usia_value.length; i++) 
	         {
	           if (rentang_usia_values[i]) 
	           {
	        	   if(i==0)
	 				{ 
	            	  	val_anak.setChecked(true);
					}
	 				else if(i==1)
	 				{
	 					val_remaja.setChecked(true);
	 				}
	 				else if(i==2)
	 				{
	 					val_dewasa.setChecked(true);
	 				}
	 				else if(i==3)
	 				{
	 					val_lansia.setChecked(true);
	 				}
	 				
	           }
	           else
	           {
	        	   if(i==0)
	 				{ 
	        		   val_anak.setChecked(false);
					}
	 				else if(i==1)
	 				{
	 					val_remaja.setChecked(false);
	 				}
	 				else if(i==2)
	 				{
	 					val_dewasa.setChecked(false);
	 				}
	 				else if(i==3)
	 				{
	 					val_lansia.setChecked(false);
	 				}
	 				
	           }
	         }
	        }
	     });
	     builder.setNegativeButton("Cancel",
	         new DialogInterface.OnClickListener() {
	           @Override
	           public void onClick(DialogInterface dialog, int which) {
	             dialog.dismiss();
	           }
	         });
	     AlertDialog alert = builder.create();
	     alert.show();
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
//			url = Global.url_insert +
//					"t_username="+str_nama_barang+"&"+
//					"t_nama="+str_asal_daerah+"&"+
//					"t_sumber_masalah_1="+str_anak+"&"+
//					"t_sumber_masalah_2="+str_remaja+"&"+
//					"t_sumber_masalah_3="+str_dewasa+"&"+
//					"t_sumber_masalah_4="+str_lansia+"&"+
//					"t_foto_name="+s_foto_name+"&"+
//					"t_status="+status;
			 
//			url = url.replace("testtt_file+", "%20");
//			url = url.replace(" ", "%20");
//			url = url.replace("true", "1");
//			url = url.replace("false", "0");
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
	                   HttpPost( Global.url_global + "mbarang/barang/format/json");
	            String json = "";
	            // 3. build jsonObject
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("nama_barang",str_nama_barang);
	            jsonObject.put("min_harga",str_min_harga);
	            jsonObject.put("max_harga",str_max_harga);
	            jsonObject.put("asal_daerah",str_asal_daerah);
	            jsonObject.put("rekomendasi_usia",str_anak + ";" + str_remaja + ";" + str_dewasa + ";" + str_lansia); 
	            jsonObject.put("user_customer_id",userid);
	            
	            
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
	        
	      
	        return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (code.equalsIgnoreCase("1")) {
				Toast.makeText(SearchDetail.this, "Data Terkirim Lewat Internet.",
						Toast.LENGTH_LONG).show();
				  Intent intent = new Intent(getApplicationContext(), Home.class);
					startActivity(intent);
					SearchDetail.this.finish();
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


            String query = "INSERT INTO " + TABLE +
            	"(nama_barang,"+
            	"asal_daerah,"+
            	"anak,"+
        		"remaja,"+
				"dewasa,"+
				"lansia,"+
				"status_kirim)VALUES('"+str_nama_barang + "',"+
					 				"'"+str_asal_daerah+ "',"+
					 				"'"+val_anak + "',"+
					 				"'"+val_remaja + "',"+
					 				"'"+val_dewasa + "',"+
					 				"'"+val_lansia + "',"+
					 				"'"+status +"')";


            query = query.replace("+", " ");   
            query = query.replace("true", "1");  
            query = query.replace("false", "0");  

            System.out.println(query);
            mydb.execSQL(query);
            mydb.close(); 
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

			Toast.makeText(SearchDetail.this,
					"Error onProviderDisabled", Toast.LENGTH_LONG).show();

		}

		public void onProviderEnabled(String provider) {

			Toast.makeText(SearchDetail.this, "onProviderEnabled",
					Toast.LENGTH_LONG).show();

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			Toast.makeText(SearchDetail.this, "onStatusChanged",
					Toast.LENGTH_LONG).show();

		}

	}
	  
	
	
	
}
