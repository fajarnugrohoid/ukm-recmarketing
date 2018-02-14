package com.ukm.facebookLogin;




import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.ukm.customerUKM.Global;
import com.ukm.customerUKM.Home;
import com.ukm.customerUKM.JSONfunctions;
import com.ukm.customerUKM.R;
import com.ukm.customerUKM.Register_customer;
import com.ukm.customerUKM.SQLiteDB;
import com.ukm.customerUKM.SQLiteDB_Account;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Login_backup extends Activity
{

	private SQLiteDB_Account account;
	private Button login_button;
	private Button login_keluar;
	private Button register;
	private EditText user;
	private EditText pwd;
	protected String user1;
	protected String pwd1;
	public String url;
	private CheckBox checkbox;
	private String u="";
	private String p="";
	private String username,userid, toko;
	private int c=0;
	protected String u1;
	protected String p1;
	public ProgressDialog pd;
 	JSONArray jArray = null;
	
 	
 	
 // Your Facebook APP ID
 	private static String APP_ID = "1567002620247793"; // Replace with your App ID

 	// Instance of Facebook Class
 	private Facebook facebook = new Facebook(APP_ID);
 	private AsyncFacebookRunner mAsyncRunner;
 	String FILENAME = "AndroidSSO_data";
 	private SharedPreferences mPrefs;
 	private String name, email;
 	// Buttons
 	Button btnFbLogin;
 	Button btnFbGetProfile;
 	Button btnPostToWall;
 	Button btnShowAccessTokens;

 	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Login.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		user=(EditText)findViewById(R.id.login_username);
		pwd=(EditText)findViewById(R.id.login_password);
		
		login_button=(Button)findViewById(R.id.login_sigin);
		login_keluar=(Button)findViewById(R.id.login_keluar);
		register = (Button)findViewById(R.id.Btn_daftar_ukm);
		checkbox=(CheckBox)findViewById(R.id.login_remember);
		account=new SQLiteDB_Account(getApplicationContext());
		Intent intent = new Intent(this, Login_backup.class);
		
		btnFbLogin = (Button) findViewById(R.id.btn_fblogin);
		btnFbGetProfile = (Button) findViewById(R.id.btn_get_profile);
		btnPostToWall = (Button) findViewById(R.id.btn_fb_post_to_wall);
		btnShowAccessTokens = (Button) findViewById(R.id.btn_show_access_tokens);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		
		
		/*try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ukm.recmarketing", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", "key is: "+Base64.encodeToString(md.digest(), Base64.DEFAULT));

                }
        } catch (NameNotFoundException e) {

            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

        	e.printStackTrace();

        }*/
		
		
		resume();
		
		SQLiteDB db = new SQLiteDB(this);
		 
		db.createTable();
		db.insertIntoTable();
		
		login_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				    
				 
				account.openToRead(); 
				user1=user.getText().toString().trim();
				pwd1=pwd.getText().toString().trim();
				account.close();
			
				new Check().execute();
			

				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		login_keluar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				    
					logoutFromFacebook();
					Login_backup.this.finish();
				
			}
		});
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(getApplicationContext(),Register_customer.class);
				startActivity(intent);
				Login_backup.this.finish();
			}
		});
		
		/**
		 * Login button Click event
		 * */
		btnFbLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Image Button", "button Clicked");
				System.out.println("Klik Butoon facebook");
				loginToFacebook();
			}
		});

		/**
		 * Getting facebook Profile info
		 * */
		btnFbGetProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getProfileInformation();
			}
		});

		/**
		 * Posting to Facebook Wall
		 * */
		btnPostToWall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				postToWall();
			}
		});

		/**
		 * Showing Access Tokens
		 * */
		btnShowAccessTokens.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showAccessTokens();
			}
		});
		
	}
	
	/**
	 * Function to login into facebook
	 * */
	public void loginToFacebook() {
		System.out.println("loginfacebook");
		mPrefs = getPreferences(MODE_PRIVATE);
		System.out.println("preferences:" + mPrefs);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		System.out.println("expires:" + expires);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
			
			//btnFbLogin.setVisibility(View.INVISIBLE);
//			
//			// Making get profile button visible
//			btnFbGetProfile.setVisibility(View.VISIBLE);
//
//			// Making post to wall visible
//			btnPostToWall.setVisibility(View.VISIBLE);
//
//			// Making show access tokens button visible
//			btnShowAccessTokens.setVisibility(View.VISIBLE);

			Log.d("FB Sessions", "fbsession:" + facebook.isSessionValid());
			
			
			mAsyncRunner.request("me", new RequestListener() {
				@Override
				public void onComplete(String response, Object state) {
					Log.d("Profile", response);
					String json = response;
					try {
						// Facebook Profile JSON data
						JSONObject profile = new JSONObject(json);
						
						// getting name of the user
						name = profile.getString("name");
						
						// getting email of the user
						email = profile.getString("email");
						SharedPreferences fbpreferences=getSharedPreferences("facebook_pref",MODE_PRIVATE);
						SharedPreferences.Editor fbeditor=fbpreferences.edit();
						fbeditor.putString("fb_name", name);
						fbeditor.putString("fb_email", email);
						
						System.out.println("fb_nama" + email);
						
//						runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
//							}
//
//						});

						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onIOException(IOException e, Object state) {
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
				}
			});
			
			Intent intent=new Intent(getApplicationContext(),Home.class);
			startActivity(intent);
			Login_backup.this.finish();
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							/*// Making Login button invisible
							btnFbLogin.setVisibility(View.INVISIBLE);

							// Making logout Button visible
							btnFbGetProfile.setVisibility(View.VISIBLE);

							// Making post to wall visible
							btnPostToWall.setVisibility(View.VISIBLE);

							// Making show access tokens button visible
							btnShowAccessTokens.setVisibility(View.VISIBLE);*/
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}


	/**
	 * Get Profile information by making request to Facebook Graph API
	 * */
	public void getProfileInformation() {
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);
					
					// getting name of the user
					final String name = profile.getString("name");
					
					// getting email of the user
					final String email = profile.getString("email");
					
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
						}

					});

					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}

	/**
	 * Function to post to facebook wall
	 * */
	public void postToWall() {
		// post on user's wall.
		facebook.dialog(this, "feed", new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
			}

			@Override
			public void onError(DialogError e) {
			}

			@Override
			public void onComplete(Bundle values) {
			}

			@Override
			public void onCancel() {
			}
		});

	}

	/**
	 * Function to show Access Tokens
	 * */
	public void showAccessTokens() {
		String access_token = facebook.getAccessToken();

		Toast.makeText(getApplicationContext(),
				"Access Token: " + access_token, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Function to Logout user from Facebook
	 * */
	public void logoutFromFacebook() {
		mAsyncRunner.logout(this, new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Logout from Facebook", response);
				if (Boolean.parseBoolean(response) == true) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// make Login button visible
							btnFbLogin.setVisibility(View.VISIBLE);

							// making all remaining buttons invisible
							btnFbGetProfile.setVisibility(View.INVISIBLE);
							btnPostToWall.setVisibility(View.INVISIBLE);
							btnShowAccessTokens.setVisibility(View.INVISIBLE);
						}

					});

				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}
	
class Check extends AsyncTask{

	
	private String json;
	private JSONObject jsonobject;
	private String code;
	private String msg;
	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		pd = new ProgressDialog(Login_backup.this);
//		pd.setTitle("Please Wait...");
		pd.setMessage("Harap tunggu...");
		pd.setCancelable(false);
		pd.show();
		/*url=Global.url_login+user1+"&password="+pwd1;
		System.out.println(url);
		Global.username=user1;*/
	}

	@Override
	protected Object doInBackground(Object... params) {
		
		InputStream inputStream = null;
        String result = ""; 
        String res_usernmae="kosong";
        JSONArray jArray=null;
        String datas = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPOST = new    
                   HttpPost( Global.url_global + "mcustomer/auth/format/json");
            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",user1);
            jsonObject.put("password",pwd1);
           
            
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
                              
                              System.out.println("test_input_stream:" + inputStream);
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

        
        return result;
	}
	@Override
	protected void onPostExecute(Object result)
	{
		super.onPostExecute(result);
		pd.dismiss();
		
		System.out.println("resultPost:" + result);
		JSONObject jObject;
		try {
			jObject = new JSONObject(result.toString());
			code = jObject.getString("status");
			System.out.println("testtcode-" + code);
            JSONArray jsonMainNode = jObject.getJSONArray("datas");
            //System.out.println("testt-" + jsonMainNode);
			if(code.equalsIgnoreCase("1"))
		   {
			     //System.out.println("jsonmainnode:" + jsonMainNode);
			 for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				userid=jsonChildNode.optString("id");
				username=jsonChildNode.optString("username");
				
			}
		   }
//            
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if(code.equalsIgnoreCase("1"))
		{
			
			account.openToWrite();
			account.deleteAll();
			account.insert(user1, pwd1);
			account.close();
			SharedPreferences preferences=getSharedPreferences("log",MODE_PRIVATE);
			SharedPreferences.Editor editor=preferences.edit();
			if(checkbox.isChecked())
			{
				System.out.println("clicked");
		
				editor.putString("user",user1);
				editor.putString("pwd",pwd1);
				editor.putInt("checked",1);
				
			}
			else
			{
			
				editor.putString("user",user1);
				editor.putString("pwd",pwd1);
				editor.putInt("checked",0);
				
			}

			
			editor.putString("userid",userid);
			editor.commit();
			Intent intent=new Intent(getApplicationContext(),Home.class);
			startActivity(intent);
			Login_backup.this.finish();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Username / Password Anda Salah", Toast.LENGTH_LONG).show();
		}
		
	}
 
}

public void resume()
{
	SharedPreferences preferences=getSharedPreferences("log",MODE_PRIVATE);
	u=preferences.getString("user","");
	p=preferences.getString("pwd","");
	c=preferences.getInt("checked",0);
	if(c==1){
	user.setText(u);
	pwd.setText(p);
	checkbox.setChecked(true);
	}
}

public boolean online()
{
	ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo ni=cm.getActiveNetworkInfo();
	if(ni != null && ni.isConnected())
	{
		return true;
	}
	return false;
	
}

public static void showHashKey(Context context) {
    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                "com.facebook.ukmrecmarketing", PackageManager.GET_SIGNATURES); //Your            package name here
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {
    } catch (NoSuchAlgorithmException e) {
    }
}

 
 
}
