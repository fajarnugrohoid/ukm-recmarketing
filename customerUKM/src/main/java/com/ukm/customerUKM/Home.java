package com.ukm.customerUKM;
 

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.ukm.app.android.*;
 
  

public class Home extends Activity {
	
	 

	public static int tutup = 0;
	private LinearLayout menu;
	private TextView TextView1;
	private String fb_name, fb_email;
	
	@Override
	public void onBackPressed() 
	{
		 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		//Home.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.home);
		TextView1 = (TextView)findViewById(R.id.textView1);
		 
		
		//menu=(Button)findViewById(R.id.sample_button);
		menu = (LinearLayout) findViewById(R.id.sample_button); 
	 

		menu.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
						SlideoutActivity.prepare(Home.this, R.id.inner_content, width);
						
						Intent intent = new Intent(Home.this, MenuActivity.class);
						 
						startActivity(intent); 
						overridePendingTransition(0, 0);
					}
				});
		
	  	SharedPreferences fb_pref = getApplicationContext().getSharedPreferences("facebook_pref", MODE_PRIVATE); // 0 - for private mode
	  	fb_name =fb_pref.getString("fb_name", null);
	  	fb_email =fb_pref.getString("fb_email", null);
		
		
		TextView1.setText("Selamat datang " + fb_name + " Email " + fb_email + " \ndi Aplikasi UKM");
		
	}
	
	

	 
	
}
