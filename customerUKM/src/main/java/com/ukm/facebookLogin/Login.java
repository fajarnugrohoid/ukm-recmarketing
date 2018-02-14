package com.ukm.facebookLogin;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;



public class Login extends FragmentActivity {

	CallbackManager callbackManager;
	
	/*public View onCreateView(
	        LayoutInflater inflater,
	        ViewGroup container,
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.login, container, false);

	    loginButton = (Button) view.findViewById(R.id.login_button);
	    ((Object) loginButton).setReadPermissions("user_friends");
	    // If using in a fragment
	    loginButton.setFragment(this);    
	    // Other app specific specialization
	    
	 
	    
	    // Callback registration
	    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
	        @Override
	        public void onSuccess(LoginResult loginResult) {
	            // App code
	        }

	        @Override
	        public void onCancel() {
	            // App code
	        }

	        @Override
	        public void onError(FacebookException exception) {
	            // App code
	        }
	    });    
	}*/
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    FacebookSdk.sdkInitialize(this.getApplicationContext());

	    callbackManager = CallbackManager.Factory.create();

	    LoginManager.getInstance().registerCallback(callbackManager,
	            new FacebookCallback<LoginResult>() {
	                @Override
	                public void onSuccess(LoginResult loginResult) {
	                    // App code
	                }

	                @Override
	                public void onCancel() {
	                     // App code
	                }

	                @Override
	                public void onError(FacebookException exception) {
	                     // App code   
	                }
	    });
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    accessTokenTracker.stopTracking();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    accessTokenTracker.stopTracking();
	}
}
