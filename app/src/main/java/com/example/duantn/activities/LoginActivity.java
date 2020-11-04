package com.example.duantn.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantn.adapter.AdapterLanguage;
import com.example.duantn.morder.ClassSelectLanguage;
import com.example.duantn.morder.KeyLangguage;
import com.example.duantn.sql.LanguageDAO;
import com.example.duantn.sql.MySqliteOpenHelper;
import com.facebook.FacebookSdk;
import com.example.duantn.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private Button btn_facebook;
    private Button btn_google;
    private MySqliteOpenHelper mySqliteOpenHelper;
    private LanguageDAO languageDAO;
    private List<KeyLangguage> keyLangguageList;
    private KeyLangguage keyLangguage;
    private ArrayList<ClassSelectLanguage> selectLanguageArrayList;
    private AdapterLanguage adapterLanguage;
    private int position_selected_language;
    private ImageView img_change_language;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        callbackFacebook();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        btn_google = findViewById(R.id.btn_google);
        findViewById(R.id.btn_google).setOnClickListener(this);
        btn_facebook = findViewById(R.id.btn_facebook);
        findViewById(R.id.btn_facebook).setOnClickListener(this);
        findViewById(R.id.img_change_language).setOnClickListener(this);
        img_change_language = findViewById(R.id.img_change_language);
        img_change_language.setOnClickListener(this);
        btn_google.getLayoutParams().width = getSizeWithScale(298);
        btn_google.getLayoutParams().height = getSizeWithScale(60);
        btn_facebook.getLayoutParams().width = getSizeWithScale(298);
        btn_facebook.getLayoutParams().height = getSizeWithScale(60);
        setNgonngu();
        btn_google.setText(getResources().getString(R.string.label_btn_sign_in_with_google));
        btn_facebook.setText(getResources().getString(R.string.label_btn_login_in_with_Facebook));

    }

    private void setNgonngu(){
        mySqliteOpenHelper = new MySqliteOpenHelper(this);
        mySqliteOpenHelper.createDataBase();
        keyLangguage = new KeyLangguage();
        languageDAO = new LanguageDAO(this);
        keyLangguageList = languageDAO.getAll();
        ganNgonngu(keyLangguageList.get(0).getValue());
        checkFlag();
    }

    private void changeLanguage(String key) {
        keyLangguage.setPk("pk");
        keyLangguage.setValue(key);
        languageDAO.update(keyLangguage);
        keyLangguageList = languageDAO.getAll();
        ganNgonngu(keyLangguageList.get(0).getValue());
        recreate();
    }

    private void ganNgonngu(String language) {
        Locale locale = new Locale(language);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(
                configuration, getBaseContext().getResources().getDisplayMetrics()
        );
    }

    private void createAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_change_langue, null);
        RecyclerView rvLanguage;
        rvLanguage = alertLayout.findViewById(R.id.rvLanguage);
        rvLanguage.getLayoutParams().width = getSizeWithScale(289);
        selectLanguageArrayList = new ArrayList<>();
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.vietnam, R.string.LblVietNam));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.japan, R.string.LblJapan));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.american, R.string.LblEnglish));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.china, R.string.LblChina));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.korea, R.string.LblKorea));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.france, R.string.LblFrance));
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        adapterLanguage = new AdapterLanguage(this, selectLanguageArrayList, position_selected_language, new AdapterLanguage.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                clickLanguageItem(position, dialog);
            }

            @Override
            public void onSwitched(boolean isChecked) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvLanguage.setLayoutManager(linearLayoutManager);
        rvLanguage.setAdapter(adapterLanguage);
    }

    private void clickLanguageItem(int position, AlertDialog dialog) {
        switch (position) {
            case 0:
                changeLanguage("vi");
                dialog.dismiss();
                break;
            case 1:
                changeLanguage("ja");
                dialog.dismiss();
                break;
            case 2:
                changeLanguage("");
                dialog.dismiss();
                break;
            case 3:
                changeLanguage("zh");
                dialog.dismiss();
                break;
            case 4:
                changeLanguage("ko");
                dialog.dismiss();
                break;
            case 5:
                changeLanguage("fr");
                dialog.dismiss();
                break;
        }

    }

    private void checkFlag() {
        if (keyLangguageList.get(0).getValue().equals("vi")) {
            img_change_language.setImageResource(R.drawable.vietnam);
        } else if (keyLangguageList.get(0).getValue().equals("ja")) {
            img_change_language.setImageResource(R.drawable.japan);
        } else if (keyLangguageList.get(0).getValue().equals("zh")) {
            img_change_language.setImageResource(R.drawable.china);
        } else if (keyLangguageList.get(0).getValue().equals("ko")) {
            img_change_language.setImageResource(R.drawable.korea);
        } else if (keyLangguageList.get(0).getValue().equals("fr")) {
            img_change_language.setImageResource(R.drawable.france);
        } else {
            img_change_language.setImageResource(R.drawable.american);
        }
    }

    private void checkNN() {
        if (keyLangguageList.get(0).getValue().equals("vi")) {
            position_selected_language = 0;
        } else if (keyLangguageList.get(0).getValue().equals("ja")) {
            position_selected_language = 1;
        } else if (keyLangguageList.get(0).getValue().equals("zh")) {
            position_selected_language = 3;
        } else if (keyLangguageList.get(0).getValue().equals("ko")) {
            position_selected_language = 4;
        } else if (keyLangguageList.get(0).getValue().equals("fr")) {
            position_selected_language = 5;
        } else {
            position_selected_language = 2;
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // add this line
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Log.i("name",personName);
                Log.i("email",personEmail);
                Log.i("ID",personId);
                Log.i("photo",String.valueOf(personPhoto));
                showToast(personName);
            }

            nextActivity(TourListActivity.class);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.v("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void loginWithFacebook() {
        loginManager.logInWithReadPermissions(
                LoginActivity.this,
                Arrays.asList(
                        "email",
                        "public_profile"));
    }

    public void callbackFacebook() {

        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginManager.registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getFbInfo();
                        nextActivity(TourListActivity.class);

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
                signInGoogle();
                break;
            case R.id.btn_facebook:
                printHashKey();
                loginWithFacebook();
                break;
            case R.id.img_change_language:
                checkNN();
                createAlertDialog();
                break;
        }
    }

    private void getFbInfo() {
        if (AccessToken.getCurrentAccessToken() == null) {
            showDialogLoading();
        } else {

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
                                String gender = me.optString(getString(R.string.gender));
                                URL imageURL = extractFacebookIcon(id);
                                Log.i("Login: ", name);
                                Log.i("ID: ", id);
                                Log.i("email: ", email);
                                Log.i("imageURL: ", imageURL.toString());
                                Log.i("link: ", link);
                                Log.i("gender: ", gender);
                                showToast(name);
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