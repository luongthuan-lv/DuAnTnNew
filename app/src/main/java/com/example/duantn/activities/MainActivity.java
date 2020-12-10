package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.duantn.MainContract;
import com.example.duantn.MainPresenter;
import com.example.duantn.morder.TourInfor;
import com.example.duantn.network.RetrofitService;
import com.example.duantn.R;
import com.example.duantn.adapter.AdapterSlideDialoginformation;
import com.example.duantn.adapter.AdapterSlideShowInformation;
import com.example.duantn.api_map_direction.Example;
import com.example.duantn.morder.Colors;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements MainContract.IView, View.OnClickListener, OnMapReadyCallback {
    private int LOCATION_REQUEST_CODE = 10001;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private ArrayList<LatLng> latLngs;
    private RatingBar ratingBar;
    private ViewPager2 viewPager2;
    private AdapterSlideShowInformation slideShowInformation;
    private int currentPage = 0;
    private Timer timer;
    private LatLng mCurrentLocation;
    private final String api_key = "AIzaSyCmxFS2arHibTbROQAfTkZAJRkEpz8LErU";
    private int locationIndex = 0;
    private List<Colors> colorsList;
    private int colorIndex = 0;
    private PolylineOptions polylineOptions;
    private int mLocationIndex;
    private boolean moveCamera = true;
    private int itemIndex = 0;
    private boolean enableAudio;
    private static final int TEXT_TO_SPEECH_CODE = 0x100;
    private MainContract.IPresenter mPresenter;
    private List<TourInfor> locationList;
    private boolean enableDialog = false;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDialogLoading();
        showDialogLoading();
        getIntentExtras();
        initView();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(50);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        setAdapter();
        setViewPager();
        getRetrofit();

    }

    private void initView() {
        findViewById(R.id.btn_feedback).setOnClickListener(this);
        addColor();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        setViewPageAndMarker();
        addMarkerAllAndClick();
        if (polylineOptions != null) {
            mGoogleMap.addPolyline(polylineOptions);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        //onClickMap
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                moveCamera = false;
                if (viewPager2.getVisibility() == View.VISIBLE) {
                    viewPager2.setVisibility(View.GONE);
                } else {
                    viewPager2.setVisibility(View.VISIBLE);
                }
            }
        });
        // onLoadMap
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                dismissDialog();
                isBackPressed = true;
                findViewById(R.id.btnPre).setOnClickListener(MainActivity.this::onClick);
            }
        });
        // onClickButtonMyLocation
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                moveCamera = true;
                return false;
            }
        });
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            enableAudio = intent.getBooleanExtra("enableAudio", false);
        }
        if (enableAudio) {
            mPresenter = new MainPresenter(this, getLanguageCode(), getVoiceName());
            mPresenter.onCreate();
            initAndroidTTS();
        }
    }

    private void getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourintro.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        retrofitService.getTourInfor(getIdLanguage(), getVehicleId()).enqueue(new Callback<List<TourInfor>>() {
            @Override
            public void onResponse(Call<List<TourInfor>> call, Response<List<TourInfor>> response) {
                if (response.body().size() != 0) {
                    int currentSize = locationList.size();
                    locationList.addAll(response.body());
                    slideShowInformation.notifyItemRangeInserted(currentSize, locationList.size());
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        OnGPS();
                    } else {
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync((OnMapReadyCallback) MainActivity.this);
                    }
                    getMapDirection(locationIndex, locationIndex + 1);
                }

            }

            @Override
            public void onFailure(Call<List<TourInfor>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMapDirection(int location1, int location2) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        retrofitService.getMapDirection(getLatLng(location1), getLatLng(location2), "", api_key).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                polylineOptions = new PolylineOptions();
                if (colorIndex < colorsList.size()) {
                    polylineOptions.color(colorsList.get(colorIndex).getColor());
                    colorIndex++;
                } else {
                    colorIndex = 0;
                    polylineOptions.color(colorsList.get(colorIndex).getColor());
                }
                polylineOptions.width(10);
                polylineOptions.addAll(decodePolyLine(response.body().getRoutes().get(0).getOverviewPolyline().getPoints()));
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync((OnMapReadyCallback) MainActivity.this);

                if (locationIndex < locationList.size() - 2) {
                    locationIndex++;
                    getMapDirection(locationIndex, locationIndex + 1);
                } else if (locationIndex == locationList.size() - 2) {
                    locationIndex++;
                    getMapDirection(locationIndex, 0);
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

    private String getLatLng(int i) {
        String latLng = Double.parseDouble(locationList.get(i).getLocation().getLat()) + "," + Double.parseDouble(locationList.get(i).getLocation().getLon());
        return latLng;
    }


    private void sendFeedback(EditText edt, AlertDialog dialog) {
        float rating;
        rating = ratingBar.getRating();
        if (rating <= 0 || edt.getText().toString().trim().equals("")) {
            showToast(getResources().getString(R.string.warning_rating));
        } else {
            showToast(getResources().getString(R.string.thanks));
            postRetrofit(edt, rating);
            dialog.dismiss();
        }

    }

    private void addColor() {
        colorsList = new ArrayList<>();
        colorsList.add(new Colors(Color.GRAY));
        colorsList.add(new Colors(Color.RED));
        colorsList.add(new Colors(Color.GREEN));
        colorsList.add(new Colors(Color.BLUE));
        colorsList.add(new Colors(Color.YELLOW));
        colorsList.add(new Colors(Color.CYAN));
        colorsList.add(new Colors(Color.MAGENTA));
    }

    private void setAdapter() {
        locationList = new ArrayList<>();
        slideShowInformation = new AdapterSlideShowInformation(locationList, enableAudio, this, new AdapterSlideShowInformation.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                if (isConnected(false)) {
                    showCustomDialog(position);

                } else showDialogNoInternet();
            }

            @Override
            public void onSwitched(boolean isChecked) {

            }

            @Override
            public void onClickEnableAudio(int position) {
                locationList.get(position).setAudio(true);
                slideShowInformation.notifyItemChanged(position);
                mPresenter.startSpeak(locationList.get(position).getInformation());
            }

            @Override
            public void onClickDisableAudio(int position) {
                locationList.get(position).setAudio(false);
                slideShowInformation.notifyItemChanged(position);
                mPresenter.stopSpeak();
            }
        });
    }

    private void setViewPager() {
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.getLayoutParams().height = getSizeWithScale(205);
        viewPager2.setAdapter(slideShowInformation);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(50));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.80f + r * 0.2f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createDialogRating() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View alertLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_rating, (LinearLayout) findViewById(R.id.layout_content));


        final EditText edt_note;
        Button btn_submit;
        ratingBar = alertLayout.findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        edt_note = alertLayout.findViewById(R.id.edt_note);
        btn_submit = alertLayout.findViewById(R.id.btn_submit);
        edt_note.setImeOptions(EditorInfo.IME_ACTION_DONE);

        alert.setView(alertLayout);
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected(false)) {
                    sendFeedback(edt_note, dialog);
                } else {
                    showDialogNoInternet();
                }
            }
        });
        dialog.show();

    }


    private void postRetrofit(EditText edtFeedBack, float rating) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourintro.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        retrofitService.postReport(getUserId(), getVehicleId(), rating, edtFeedBack.getText().toString().trim(), getFullName(), getUrlAvt(), getCurrentDate()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCustomDialog(int position) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_show_information, viewGroup, false);
        TextView tvContent = dialogView.findViewById(R.id.tvDialogContent);
        TextView tvTitle = dialogView.findViewById(R.id.tv_Title);
        CardView cvDialog = dialogView.findViewById(R.id.cvDialog);

        tvContent.setText(locationList.get(position).getInformation());
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        tvTitle.setText(locationList.get(position).getPlace());
        final ViewPager2 viewPager = dialogView.findViewById(R.id.viewPager);
        AdapterSlideDialoginformation adapterSlideDialoginformation = new AdapterSlideDialoginformation(locationList.get(position).getAvatar(), this);
        viewPager.setAdapter(adapterSlideDialoginformation);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                currentPage = viewPager.getCurrentItem() + 1;
                if (currentPage == locationList.get(position).getAvatar().size()) {
                    currentPage = 0;
                    viewPager.setCurrentItem(currentPage, true);
                } else {
                    viewPager.setCurrentItem(currentPage, true);
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


        Dialog dialog = new Dialog(this, R.style.dialogNotice);
        cvDialog.getLayoutParams().width = getSizeWithScale(340);
        cvDialog.getLayoutParams().height = getSizeWithScale(519);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void addMarkerAllAndClick() {
        latLngs = new ArrayList<>();
        for (int i = 0; i < locationList.size(); i++) {
            final LatLng latlng = new LatLng(Double.parseDouble(locationList.get(i).getLocation().getLat()), Double.parseDouble(locationList.get(i).getLocation().getLon()));
            MarkerOptions option = new MarkerOptions();
            option.position(latlng);
            option.title(locationList.get(i).getPlace());
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            final Marker maker = mGoogleMap.addMarker(option);
            maker.showInfoWindow();
            latLngs.add(latlng);
        }


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                moveCamera = false;
                viewPager2.setVisibility(View.VISIBLE);
                String indexMarker = String.valueOf(marker.getId().charAt(1));
                int positionMarker = Integer.parseInt(indexMarker);
                viewPager2.setCurrentItem(positionMarker);
                return false;
            }
        });
    }

    private void setViewPageAndMarker() {
        ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (itemIndex != 0) {
                    moveCamera = false;
                    final LatLng latlng = new LatLng(Double.parseDouble(locationList.get(position).getLocation().getLat()), Double.parseDouble(locationList.get(position).getLocation().getLon()));
                    MarkerOptions option = new MarkerOptions();
                    option.position(latlng);
                    option.title(locationList.get(position).getPlace());
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                    final Marker maker = mGoogleMap.addMarker(option);
                    maker.showInfoWindow();
                }
                itemIndex = 1;
            }
        };
        viewPager2.registerOnPageChangeCallback(pageChangeCallback);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (isConnected(false)) {
                if (mGoogleMap != null) {
                    mCurrentLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    getDistance(locationResult);
                    if (moveCamera) {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 17));
                    }
                }
            } else {
                showDialogNoInternet();
            }

        }
    };

    private void getDistance(LocationResult locationResult) {
        float[][] arr = new float[locationList.size()][10];
        for (int i = 0; i < arr.length; i++) {
            Location.distanceBetween(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), Double.parseDouble(locationList.get(i).getLocation().getLat()), Double.parseDouble(locationList.get(i).getLocation().getLon()), arr[i]);
        }
        float min = arr[0][0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][0] < min) {
                min = arr[i][0];
                mLocationIndex = i;
            }
            if (min == arr[0][0]) {
                mLocationIndex = 0;
            }
        }

        float results[] = new float[10];
        Location.distanceBetween(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), Double.parseDouble(locationList.get(mLocationIndex).getLocation().getLat()), Double.parseDouble(locationList.get(mLocationIndex).getLocation().getLon()), results);
        if (results[0] < 500 && locationList.get(mLocationIndex).isVisited() == false) {
            if (viewPager2.getVisibility() == View.GONE) {
                viewPager2.setVisibility(View.VISIBLE);
            }
            viewPager2.setCurrentItem(mLocationIndex);

            if (enableAudio) {
                for (int i = 0; i < locationList.size(); i++) {
                    if (locationList.get(i).isAudio() == true) {
                        locationList.get(i).setAudio(false);
                        slideShowInformation.notifyItemChanged(i);
                    }
                }
                locationList.get(mLocationIndex).setAudio(true);
                slideShowInformation.notifyItemChanged(mLocationIndex);
                mPresenter.startSpeak(locationList.get(mLocationIndex).getInformation());
            }

            locationList.get(mLocationIndex).setVisited(true);
        }
        if (enableDialog == false && mLocationIndex == locationList.size() - 1) {
            if (results[0] > 1000 && locationList.get(mLocationIndex).isVisited() == true) {
                createDialogRating();
                enableDialog = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates();
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdate();
        if (enableAudio) {
            mPresenter.pauseSpeak();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (enableAudio) {
            mPresenter.resumeSpeak();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // init android tts
        if (requestCode == TEXT_TO_SPEECH_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mPresenter.initAndroidTTS();
                return;
            }
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }
    }

    @Override
    public int getProgressPitch() {
        return 1500;
    }

    @Override
    public int getProgressSpeakRate() {
        return 75;
    }

    @Override
    public void invoke(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    @Override
    public void setPresenter(MainContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initAndroidTTS() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TEXT_TO_SPEECH_CODE);
    }

    @Override
    public void onClick(View v) {
        if (isConnected(false)) {
            switch (v.getId()) {
                case R.id.btn_feedback:
                    createDialogRating();
                    break;
                case R.id.btnPre:
                    onBackPressed();
                    break;
            }
        } else {
            showDialogNoInternet();
        }
    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
        }
    }
}
