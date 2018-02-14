package com.ukm.customerUKM;
 

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ukm.facebookLogin.Login_backup;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
 
 
  

public class Register_customer extends Activity implements OnClickListener {
	
	 
//	public String uploadFilePath = "/mnt/sdcard/";
//	public String upLoadServerUri = Global.url_upload;
//	public String uploadFileName;
    int serverResponseCode = 0;
    
	SQLiteDatabase mydb;
	public static String DBNAME = "local_db.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    public static String TABLE = "customer";       // THIS IS THE TABLE NAME
   
	private static final int TAKE_PICTURE = 0;
	

	private Uri mUri;
	public int i = 0;
	public static String status="";
	
	
	private Button send;
	private Button cancel;
	
	private EditText t_username;
	private EditText t_password;
	
	private EditText t_toko;
	private EditText t_nama_lengkap;
	private EditText t_alamat;
	private EditText t_tanggal_lahir;
	private EditText t_no_hp;
	
	private EditText t_lat;
	private EditText t_long;
	
	private String s_username;
	private String s_password;
	private String s_toko;
	private String s_nama_lengkap;
	private String s_alamat;
	private String s_no_hp;
	private String s_lat;
	private String s_tanggal_lahir;
	private String s_long;
	private String s_sex;
	
	private TextView t_datepicker;
	private DatePicker dpResult;
	private Button btnChangeDate;
 
	private int year;
	private int month;
	private int day;
	
	
	private ImageButton ib;
 	private Calendar cal;
 	
 	
 	private RadioGroup radioSexGroup;
 	private RadioButton radioSexButton;
	
 	
 	JSONArray jArray = null;
 	
	SimpleDateFormat s1 = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
	String format = s1.format(new java.util.Date());
	SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
	String date = s2.format(new java.util.Date());
	SimpleDateFormat s3 = new SimpleDateFormat("HH:mm:ss");
	String time = s3.format(new java.util.Date());
	
	@Override
	public void onBackPressed() 
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		//Home.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.register_ukm);
		 
		//menu=(Button)findViewById(R.id.sample_button);
		
        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel);
        
        t_username 			= (EditText) findViewById(R.id.t_username);
        t_password 			= (EditText) findViewById(R.id.t_password);
        
        t_toko 				= (EditText) findViewById(R.id.t_toko);
        t_nama_lengkap 		= (EditText) findViewById(R.id.val_nama_barang);
        t_alamat 		= (EditText) findViewById(R.id.t_alamat);
        radioSexGroup 		= (RadioGroup) findViewById(R.id.radioSexGroup);
        t_tanggal_lahir 	= (EditText) findViewById(R.id.t_tanggal_lahir);
        t_no_hp 			= (EditText) findViewById(R.id.t_no_hp);
        t_lat 				= (EditText) findViewById(R.id.val_harga_toko);
        t_long 				= (EditText) findViewById(R.id.val_asal_daerah);
        
        
        
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
        //setCurrentDateOnView();
        
        ib = (ImageButton) findViewById(R.id.imageButton1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        
        

        
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
		
        ib.setOnClickListener(new View.OnClickListener() {
       	 public void onClick(View v) {
       	  // Do something in response to button click
       		 showDialog(0);
       	 }
      });
		 
	
	}
	

	 @Override
	 @Deprecated
	 protected Dialog onCreateDialog(int id) {
		 return new DatePickerDialog(this, datePickerListener, year, month, day);
	 }

	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		 public void onDateSet(DatePicker view, int selectedYear,
				 int selectedMonth, int selectedDay) {
			 		t_tanggal_lahir.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
		 }
	 };
	
	// display current date
	/*public void setCurrentDateOnView() {
 
		t_datepicker = (TextView) findViewById(R.id.tvDate);
		dpResult = (DatePicker) findViewById(R.id.dpResult);
 
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
 
		// set current date into textview
		t_datepicker.setText(new StringBuilder()
			// Month is 0 based, just add 1
			.append(month + 1).append("-").append(day).append("-")
			.append(year).append(" "));
 
		// set current date into datepicker
		dpResult.init(year, month, day, null);
 
	}*/

	 
	public void onClick(View v) { 
		
		
		if(v.getId()==R.id.send){
			
			s_username = t_username.getText().toString().trim();
			s_password = t_password.getText().toString().trim();
			
			s_toko = t_toko.getText().toString().trim();
			s_nama_lengkap = t_nama_lengkap.getText().toString().trim();
			s_alamat = t_alamat.getText().toString().trim();
			s_tanggal_lahir = t_tanggal_lahir.getText().toString().trim();
			s_no_hp = t_no_hp.getText().toString().trim();
			s_lat = t_lat.getText().toString().trim();
			s_long = t_long.getText().toString().trim();

			
			 // get selected radio button from radioGroup
			 int selectedId = radioSexGroup.getCheckedRadioButtonId();
			 // find the radiobutton by returned id
			 radioSexButton = (RadioButton) findViewById(selectedId);
			 s_sex = radioSexButton.getText().toString();
			 //Toast.makeText(Register_ukm.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();
			
			
			AlertDialog.Builder builder=new AlertDialog.Builder(Register_customer.this);
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
				        
				        Intent intent = new Intent(getApplicationContext(), Login_backup.class);
						startActivity(intent);
						Register_customer.this.finish();
					
					}
			  })
			  .setNegativeButton("Simpan Saja",new DialogInterface.OnClickListener()
			  {
					public void onClick(DialogInterface dialog,int id) 
					{
						status = "Tersimpan";
				        insertIntoTable();
				        Toast.makeText(Register_customer.this,
								"Tersimpan di device.", Toast.LENGTH_LONG)
								.show();
				        
				        Intent intent = new Intent(getApplicationContext(), Login_backup.class);
						startActivity(intent);
						Register_customer.this.finish();
					}
			  })
			  .show();
			
		}
		else if (v.getId()==R.id.cancel){
			Intent intent = new Intent(getApplicationContext(), Login_backup.class);
			startActivity(intent);
			Register_customer.this.finish();
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
			url = Global.url_insert +
					"t_username="+s_username+"&"+
					"t_nama="+s_nama_lengkap+"&"+					
					"t_lat="+s_lat+"&"+
					"t_long="+s_long+"";
			 
			url = url.replace("+", "%20");
			url = url.replace(" ", "%20");
			url = url.replace("true", "1");
			url = url.replace("false", "0");
			System.out.println(url);

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
	                   HttpPost( Global.url_global + "mukm/register_ukm/format/json");
	            String json = "";
	            // 3. build jsonObject
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("username",s_username);
	            jsonObject.put("password",s_password);
	            jsonObject.put("toko",s_toko);
	            jsonObject.put("nama_lengkap",s_nama_lengkap);
	            jsonObject.put("alamat",s_alamat);
	            jsonObject.put("jk",s_sex);
	            jsonObject.put("no_hp",s_no_hp);
	            jsonObject.put("tanggal_lahir",s_tanggal_lahir);
	            jsonObject.put("latitude",s_lat);
	            jsonObject.put("longtitude",s_long);
	            
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
	                                  jArray = jObject.getJSONArray("datas");
	                                  for (int i=0; i < jArray.length(); i++)
	                                  {
	                                      try {
	                                          JSONObject oneObject = jArray.getJSONObject(i);
	                                          // Pulling items from the array
	                                          res_usernmae = oneObject.getString("username");
	                                          String res_nama_lengkap = oneObject.getString("nama_lengkap");
	                                          
	                                      } catch (JSONException e) {
	                                          // Oops
	                                      }
	                                  }
	                                  
	                              }else{
	                                  result = "Did not work!";
	                              }
	            Log.d("test_respon", httpResponse.toString());
	        } catch (Exception e) {
	            Log.d("InputStream", e.getLocalizedMessage());
	        }
	        
	        Log.d("isi_result_stream", result);
	        Log.d("isi_code", code);
	        Log.d("isi_username", res_usernmae);
	        return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (code.equalsIgnoreCase("1")) {
				Toast.makeText(Register_customer.this, "Data Terkirim Lewat Internet.",
						Toast.LENGTH_LONG).show();
				  Intent intent = new Intent(getApplicationContext(), Login_backup.class);
					startActivity(intent);
					Register_customer.this.finish();
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
            	"(username,"+
            	"nama,"+
        		"nama_lokasi,"+
        		"lat,"+
        		"long,"+ 
				"status_kirim)VALUES('"+s_username + "',"+ 
					 				"'"+s_nama_lengkap+ "',"+ 
					 				"'"+s_lat + "',"+  
					 				"'"+s_long + "',"+  
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
					t_lat.setText(la);
					t_long.setText(ln);
					i++;
				}
			}

		}

		public void onProviderDisabled(String provider) {

			Toast.makeText(Register_customer.this,
					"Error onProviderDisabled", Toast.LENGTH_LONG).show();

		}

		public void onProviderEnabled(String provider) {

			Toast.makeText(Register_customer.this, "onProviderEnabled",
					Toast.LENGTH_LONG).show();

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			Toast.makeText(Register_customer.this, "onStatusChanged",
					Toast.LENGTH_LONG).show();

		}

	}
	  
	
	
	
}
