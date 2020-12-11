package com.example.duantn.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
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
import com.example.duantn.morder.KeyLanguage;
import com.example.duantn.sql.LanguageDAO;
import com.example.duantn.sql.MySqliteOpenHelper;
import com.example.duantn.view.CustomButton;
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
    private LocationManager locationManager;
    private MySqliteOpenHelper mySqliteOpenHelper;
    private LanguageDAO languageDAO;
    private List<KeyLanguage> keyLanguageList;
    private KeyLanguage keyLanguage;
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
        initDialogLoading();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        callbackFacebook();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_google = findViewById(R.id.btn_google);
        btn_facebook = findViewById(R.id.btn_facebook);
        img_change_language = findViewById(R.id.img_change_language);
        btn_google.getLayoutParams().width = getSizeWithScale(298);
        btn_google.getLayoutParams().height = getSizeWithScale(60);
        btn_facebook.getLayoutParams().width = getSizeWithScale(298);
        btn_facebook.getLayoutParams().height = getSizeWithScale(60);
        setNgonngu();
        btn_google.setText(getResources().getString(R.string.label_btn_sign_in_with_google));
        btn_facebook.setText(getResources().getString(R.string.label_btn_login_in_with_Facebook));


        img_change_language.setOnClickListener(this);
        findViewById(R.id.btn_google).setOnClickListener(this);
        findViewById(R.id.btn_facebook).setOnClickListener(this);

    }

    private void setNgonngu() {
        keyLanguage = new KeyLanguage();
        languageDAO = new LanguageDAO(this);
        keyLanguageList = languageDAO.getAll();
        ganNgonngu(keyLanguageList.get(0).getValue());
        checkFlagAndLanguageCode();
    }

    private void changeLanguage(String key) {
        keyLanguage.setPk("pk");
        keyLanguage.setValue(key);
        languageDAO.update(keyLanguage);
        keyLanguageList = languageDAO.getAll();
        ganNgonngu(keyLanguageList.get(0).getValue());
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
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.germany, R.string.LblGermany));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.indonesia, R.string.LblIndonesia));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.russia, R.string.LblRussia));
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
            case 6:
                changeLanguage("de");
                dialog.dismiss();
                break;
            case 7:
                changeLanguage("in");
                dialog.dismiss();
                break;
            case 8:
                changeLanguage("ru");
                dialog.dismiss();
                break;
        }

    }

    private void checkFlagAndLanguageCode() {
        if (keyLanguageList.get(0).getValue().equals("vi")) {
            img_change_language.setImageResource(R.drawable.vietnam);
            setLanguageCode("vi-VN");
            setVoiceName("vi-VN-Wavenet-C");
            setIdLanguage("5fb6b5077dd56b4ca491373f");
        } else if (keyLanguageList.get(0).getValue().equals("ja")) {
            img_change_language.setImageResource(R.drawable.japan);
            setLanguageCode("ja-JP");
            setVoiceName("ja-JP-Wavenet-B");
            setIdLanguage("5fb29e155fea350ad4f0073a");
        } else if (keyLanguageList.get(0).getValue().equals("zh")) {
            img_change_language.setImageResource(R.drawable.china);
            setLanguageCode("yue-HK");
            setVoiceName("yue-HK-Standard-C");
            setIdLanguage("5fb29df35fea350ad4f00737");
        } else if (keyLanguageList.get(0).getValue().equals("ko")) {
            img_change_language.setImageResource(R.drawable.korea);
            setLanguageCode("ko-KR");
            setVoiceName("ko-KR-Wavenet-A");
            setIdLanguage("5fb29de95fea350ad4f00736");
        } else if (keyLanguageList.get(0).getValue().equals("fr")) {
            img_change_language.setImageResource(R.drawable.france);
            setLanguageCode("fr-FR");
            setVoiceName("fr-FR-Standard-C");
            setIdLanguage("5fb29dfd5fea350ad4f00738");
        } else if (keyLanguageList.get(0).getValue().equals("de")) {
            img_change_language.setImageResource(R.drawable.germany);
            setLanguageCode("de-DE");
            setVoiceName("de-DE-Standard-F");
            setIdLanguage("5fb29e1d5fea350ad4f0073b");
        } else if (keyLanguageList.get(0).getValue().equals("in")) {
            img_change_language.setImageResource(R.drawable.indonesia);
            setLanguageCode("id-ID");
            setVoiceName("id-ID-Standard-A");
            setIdLanguage("5fb29e0a5fea350ad4f00739");
        } else if (keyLanguageList.get(0).getValue().equals("ru")) {
            img_change_language.setImageResource(R.drawable.russia);
            setLanguageCode("ru-RU");
            setVoiceName("ru-RU-Standard-A");
            setIdLanguage("5fb29e285fea350ad4f0073c");
        } else {
            img_change_language.setImageResource(R.drawable.american);
            setLanguageCode("en-GB");
            setVoiceName("en-GB-Standard-A");
            setIdLanguage("5fb6b50f7dd56b4ca4913740");
        }
    }

    private void checkNN() {
        if (keyLanguageList.get(0).getValue().equals("vi")) {
            position_selected_language = 0;
        } else if (keyLanguageList.get(0).getValue().equals("ja")) {
            position_selected_language = 1;
        } else if (keyLanguageList.get(0).getValue().equals("zh")) {
            position_selected_language = 3;
        } else if (keyLanguageList.get(0).getValue().equals("ko")) {
            position_selected_language = 4;
        } else if (keyLanguageList.get(0).getValue().equals("fr")) {
            position_selected_language = 5;
        } else if (keyLanguageList.get(0).getValue().equals("de")) {
            position_selected_language = 6;
        } else if (keyLanguageList.get(0).getValue().equals("in")) {
            position_selected_language = 7;
        } else if (keyLanguageList.get(0).getValue().equals("ru")) {
            position_selected_language = 8;
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
            dismissDialog();
            handleSignInResult(task);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                String urlAvatar;
                if (personPhoto == null) {
                    urlAvatar = "https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png";
                } else {
                    urlAvatar = String.valueOf(personPhoto);
                }
                saveAccount(personId, urlAvatar, personName);
                setFullName(personName);
                setUserId(personId);
                setUrlAvt(urlAvatar);
            }
            Intent intent = new Intent(this, TourListActivity.class);
            startActivity(intent);


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

                    }

                    @Override
                    public void onCancel() {
                        dismissDialog();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        dismissDialog();
                        showToast(error.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google:
                if (isConnected(false)) {
                    initDialogLoading();
                    showDialogLoading();
                    signInGoogle();
                } else {
                    showDialogNoInternet();
                }
                break;
            case R.id.btn_facebook:
                if (isConnected(false)) {
                    initDialogLoading();
                    showDialogLoading();
                    printHashKey();
                    loginWithFacebook();
                } else {
                    showDialogNoInternet();
                }
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
                                String personName = me.optString(getString(R.string.name));
                                String id = me.optString(getString(R.string.id));
                                String link = me.optString(getString(R.string.link));
                                URL imageURL = extractFacebookIcon(id);

                                saveAccount(id, String.valueOf(imageURL), personName);
                                setUrlAvt(String.valueOf(imageURL));
                                setFullName(personName);
                                setUserId(id);

                            }
                            dismissDialog();
                            Intent intent = new Intent(LoginActivity.this, TourListActivity.class);
                            startActivity(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}