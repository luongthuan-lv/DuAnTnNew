package com.example.duantn.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duantn.R;
import com.example.duantn.morder.Account;
import com.example.duantn.sql.AccountDAO;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;

public class BaseActivity extends AppCompatActivity {
    private Activity context;
    private Dialog mProgressDialog;
    private AccountDAO accountDAO;
    private Account account;

    public String getCurrentDate() {
        return DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(Calendar.getInstance().getTime());
    }

    private static String languageCode;
    private static String voiceName;
    private static String idLanguage;
    private static String vehicleId;
    private static String urlAvt,fullName,userId;

    public static String getLanguageCode() {
        return languageCode;
    }

    public static void setLanguageCode(String languageCode) {
        BaseActivity.languageCode = languageCode;
    }

    public static String getVoiceName() {
        return voiceName;
    }

    public static void setVoiceName(String voiceName) {
        BaseActivity.voiceName = voiceName;
    }

    public static String getIdLanguage() {
        return idLanguage;
    }

    public static void setIdLanguage(String idLanguage) {
        BaseActivity.idLanguage = idLanguage;
    }


    public static String getVehicleId() {
        return vehicleId;
    }

    public static void setVehicleId(String vehicleId) {
        BaseActivity.vehicleId = vehicleId;
    }


    public static String getUrlAvt() {
        return urlAvt;
    }

    public static void setUrlAvt(String urlAvt) {
        BaseActivity.urlAvt = urlAvt;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        BaseActivity.fullName = fullName;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        BaseActivity.userId = userId;
    }

    public void saveAccount(String id, String urlAvatar, String name){
        accountDAO = new AccountDAO(this);
        account = new Account();
        account.setPk("pk");
        account.setName(name);
        account.setUrl_avt(urlAvatar);
        account.setId(id);
        accountDAO.update(account);
    }

    public void showToast(String msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    //TODO size manager

    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics;
    }

    private float screenDensity = 0;

    private int screenWidth = 0;

    public int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    private float getScaleValue() {
        if (scaleValue == 0)
            scaleValue = getScreenWidth() * 1f / 375;
        return scaleValue;
    }

    public int getSizeWithScale(double sizeDesign) {
        return (int) (sizeDesign * getScaleValue());
    }

    public void initDialogLoading() {
        mProgressDialog = new Dialog(this, R.style.dialogNotice);
        mProgressDialog.setContentView(R.layout.dialog_progress);
    }

    public void showDialogLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    public void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showDialogLogout(final Activity context, String title) {
        this.context = context;
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(getResources().getString(R.string.logout));
        b.setPositiveButton(getResources().getString(R.string.label_btn_Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                initDialogLoading();
                showDialogLoading();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
                mGoogleSignInClient.revokeAccess()
                        .addOnCompleteListener(context, new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                saveAccount("","","");
                                nextActivity(LoginActivity.class);
                            }
                        });
                LoginManager.getInstance().logOut();
            }
        });
        b.setNegativeButton(getResources().getString(R.string.label_btn_Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog al = b.create();
        al.show();
        al.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorRed));
        al.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.color_btn_alertDialog));
    }

    public boolean isConnected(boolean connected) {
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            showToast(e.getMessage());
        }
        return connected;
    }

    public void showDialogNoInternet() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View alertLayout = LayoutInflater.from(this).inflate(R.layout.dialog_warning_internet, (LinearLayout) findViewById(R.id.layout_content));
        Button btn_close;
        btn_close = alertLayout.findViewById(R.id.btn_close);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
