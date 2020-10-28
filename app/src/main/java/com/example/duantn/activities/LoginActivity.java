package com.example.duantn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.example.duantn.R;
import com.example.duantn.api.ApiConstants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.BuildConfig;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private Button btn_facebook;
    private Button btn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();
        btn_google = findViewById(R.id.btn_google);
        findViewById(R.id.btn_google).setOnClickListener(this);
        btn_facebook = findViewById(R.id.btn_facebook);
        findViewById(R.id.btn_facebook).setOnClickListener(this);

        btn_google.getLayoutParams().width = getSizeWithScale(298);
        btn_google.getLayoutParams().height = getSizeWithScale(60);
        btn_facebook.getLayoutParams().width = getSizeWithScale(298);
        btn_facebook.getLayoutParams().height = getSizeWithScale(60);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // add this line
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginWithFacebook() {
        loginManager.logInWithReadPermissions(
                LoginActivity.this,
                Arrays.asList(
                        "email",
                        "public_profile"));
    }

    public void facebookLogin() {

        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginManager.registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getFbInfo();
                        nextActivity(SelectLanguageActivity.class);

                    }

                    @Override
                    public void onCancel() {
                        Log.v("LoginScreen", "---onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // here write code when get error
                        Log.v("LoginScreen", "----onError: "
                                + error.getMessage());
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google:
                nextActivity(SelectLanguageActivity.class);
                break;
            case R.id.btn_facebook:
                printHashKey();
                initDialogLoading();
                loginWithFacebook();

                break;
        }
    }

    private void getFbInfo() {
        if (AccessToken.getCurrentAccessToken() == null){
            showDialogLoading();
        }
        else {

            dismissDialog();
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(final JSONObject me, GraphResponse response) {
                            if (me != null) {
                                String name = me.optString(getString(R.string.name));
                                String id = me.optString(getString(R.string.id));
                                String email = me.optString(getString(R.string.email));
                                String link = me.optString(getString(R.string.link));
                                URL imageURL = extractFacebookIcon(id);
                                Log.i("Login: ", name);
                                Log.i("ID: ", id);
                                Log.i("email: ", link);
                                Log.i("imageURL: ",imageURL.toString());

                                Toast.makeText(LoginActivity.this, "Name: " + me.optString("name"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "ID: " + me.optString("id"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
    public URL extractFacebookIcon(String id) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageURL = new URL("http://graph.facebook.com/" + id
                    + "/picture?type=large");
            return imageURL;
        } catch (Throwable e) {
            return null;
        }
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("TAG", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("TAG", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("TAG", "printHashKey()", e);
        }
    }
}

