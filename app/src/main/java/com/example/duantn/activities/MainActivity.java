 package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.duantn.R;
import com.example.duantn.adapter.AdapterSlideShowInformation;
import com.example.duantn.morder.ClassShowInformation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

 public class MainActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private FloatingActionButton btnMyLocation;
    private ArrayList<ClassShowInformation> showInformationArrayList;
    private CardView cvInformation;
    private TextView tvInformation;
    private ArrayList<LatLng> latLngs;
    private Polygon line;
    private PolygonOptions polylineOptions;
    private ViewPager2 viewPager;
    private AdapterSlideShowInformation slideShowInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMyLocation = findViewById(R.id.add_fab);
        initDialogLoading();
        showDialogLoading();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            fetchLocation();
        }
        showInformationArrayList = new ArrayList<>();

        showInformationArrayList.add(new ClassShowInformation(21.037000, 105.834727, "Lăng Bác", "Lăng Bác là nơi lưu giữ thi hài của vị lãnh tụ kính yêu. Bên ngoài lăng là những hàng tre xanh bát ngát. Lăng chủ tích mở cửa vào sáng thứ 3,4,5,7 và chủ nhật. Khi vào viếng lăng Bác, bạn chú ý ăn mặc chỉnh tề, không đem theo các thiết bị điện tử ghi hành và giữ trật tự trong lăng.", 0));
        showInformationArrayList.add(new ClassShowInformation(21.032555, 105.839804, "Cột cờ Hà Nội", "Kỳ đài Hà Nội hay còn được nhiều biết tới hơn với tên gọi Cột cờ Hà Nội nằm trong khuôn viên của bảo tàng lịch sử quân sự Việt Nam. Được đánh giá là công trình nguyên vẹn và hoành tráng nhất trong quần thể di tích Hoàng thành Thăng Long, Cột Cờ chính là điểm tham quan du lịch ở Hà Nội mà du khách không thể bỏ qua trong hành trình khám phá lịch sử của đất Hà Thành.", 1));
        showInformationArrayList.add(new ClassShowInformation(21.029565, 105.836206, "Văn Miếu - Quốc Tử Giám", "Nếu kể tên các địa điểm du lịch Hà Nội bậc nhất xưa và nay có lẽ ai cũng sẽ nghĩ ngay đến Văn Miếu Quốc Tử Giám. Đây là một quần thể kiến trúc văn hoá hàng đầu và là niềm tự hào của người dân Thủ đô khi nhắc đến truyền thống ngàn năm văn hiến của Thăng Long – Đông Đô – Hà Nội.", 2));
        showInformationArrayList.add(new ClassShowInformation(21.025445, 105.846422, "Di Tích Lịch Sử Nhà Tù Hỏa Lò", "Nhà tù Hỏa Lò được thực dân Pháp xây dựng từ năm 1896 với tên gọi “Maison Central”, là nơi giam giữ những chiến sĩ cách mạng chống lại chế độ thực dân. Đây là một trong những công trình kiên cố vào loại bậc nhất Đông Dương khi đó. Sau ngày giải phóng thủ đô, nhà tù được đặt dưới quyền của chính quyền cách mạng. Từ năm 1963 đến 1975, nơi đây còn được sử dụng để làm nơi giam giữ những phi công Mỹ bị quân đội Việt Nam bắn rơi trong cuộc chiến tranh phá hoại miền Bắc.", 3));
        showInformationArrayList.add(new ClassShowInformation(21.024118, 105.857947, "Nhà hát Lớn Hà Nội", "Nằm ở số 1 Tràng Tiền, Nhà hát lớn là một trong các địa điểm du lịch đẹp ở Hà Nội mang nhiều dấu ấn lịch sử. Đây là địa điểm tổ chức những chương trình nghệ thuật lớn của nhiều ca sĩ, nghệ sĩ tên tuổi hàng đầu Việt Nam. Du khách có thể chiêm ngưỡng kiến trúc tuyệt vời của Nhà hát Lớn hay mua vé vào xem một trong những chương trình biểu diễn thường xuyên được tổ chức để có thể tận mắt thấy được hết nội thất tráng lệ của nhà hát.", 4));
        showInformationArrayList.add(new ClassShowInformation(21.028683, 105.848812, "Nhà thờ Lớn Hà Nội", "Nằm ở 40 phố Nhà Chung, phường Hàng Trống, Nhà thờ lớn là một trong những điểm đến thú vị ở Hà Nội, nơi lui tới không chỉ của các tín đồ theo đạo mà còn là địa điểm quen thuộc của giới trẻ, khách du lịch tứ phương. Nhà thờ được thiết kế theo phong cách kiến trúc Gothic trung cổ châu Âu với bức tường xây cao, có mái vòm và nhiều cửa sổ.", 5));
        showInformationArrayList.add(new ClassShowInformation(21.028805, 105.852150, "Hồ Hoàn Kiếm", "Hồ Gươm hay hồ Hoàn Kiếm là một trong những nơi nên đến ở Hà Nội khi du lịch thủ đô. Nằm ở giữa trung tâm, Hồ Gươm được ví như trái tim của thành phố ngàn năm tuổi này.. Mặt hồ như tấm gương lớn soi bóng những cây cổ thụ, những rặng liễu thướt tha tóc rủ, những mái đền, chùa cổ kính, tháp cũ rêu phong, các toà nhà mới cao tầng vươn lên trời xanh.", 6));
        showInformationArrayList.add(new ClassShowInformation(21.034399, 105.840115, "Hoàng Thành Thăng Long", "Hoàng thành Thăng Long là quần thể di tích gắn liền với sự phát triển của Thăng Long – Hà Nội, được các triều vua xây dựng trong nhiều giai đoạn lịch sử. Đây cũng là di tích quan trọng bậc nhất trong hệ thống các di tích lịch sử của Việt Nam. Đến Hoàng thành Thăng Long du khách có thể tham quan những địa điểm nổi bật như khu khảo cổ học số 18 Hoàng Diệu, Đoan Môn, Điện Kính Thiên, Bắc Môn (thành Cửa Bắc)…", 7));

        viewPager = findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = getSizeWithScale(139);

        setAdapter();
        setViewPager();
        viewPager.setOnClickListener(this);
//        cvInformation = findViewById(R.id.cvInformation);
//        cvInformation.getLayoutParams().width = getSizeWithScale(289);
//        cvInformation.getLayoutParams().height = getSizeWithScale(139);

//        tvInformation = findViewById(R.id.tvInformation);



    }
    private void setAdapter() {
        slideShowInformation = new AdapterSlideShowInformation(showInformationArrayList, this);
    }

    private void setViewPager() {
        viewPager.setAdapter(slideShowInformation);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setVisibility(View.GONE);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(100));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.80f + r * 0.2f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);


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


    private void fetchLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync((OnMapReadyCallback) MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.mGoogleMap = googleMap;
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (btnMyLocation.getVisibility() == View.VISIBLE && viewPager.getVisibility() == View.VISIBLE){
                    btnMyLocation.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                }else {
                    btnMyLocation.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                }
            }
        });
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                dismissDialog();
            }
        });
        final LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                mGoogleMap.stopAnimation();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
                mGoogleMap.addMarker(markerOptions);
            }
        });

        latLngs = new ArrayList<>();
        polylineOptions = new PolygonOptions();
        for (int i = 0; i < showInformationArrayList.size(); i++) {
                final LatLng position = new LatLng(showInformationArrayList.get(i).getLatitude(), showInformationArrayList.get(i).getLongitude());
            MarkerOptions option = new MarkerOptions();
            option.position(position);
            option.title(showInformationArrayList.get(i).getTitle());
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            final Marker maker = googleMap.addMarker(option);
            maker.showInfoWindow();
            latLngs.add(position);
        }

        polylineOptions.addAll(latLngs);
        line = mGoogleMap.addPolygon(polylineOptions);
        line.setStrokeColor(Color.BLUE);
        line.setStrokeWidth(10);
        line.setGeodesic(true);


        ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                final LatLng position1 = new LatLng(showInformationArrayList.get(position).getLatitude(), showInformationArrayList.get(position).getLongitude());
                MarkerOptions option = new MarkerOptions();
                option.position(position1);
                option.title(showInformationArrayList.get(position).getTitle());
                option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position1, 15));
                final Marker maker = mGoogleMap.addMarker(option);
                maker.showInfoWindow();
                Log.e("TAG", "onPageSelected: " + position );
            }
        };
        viewPager.registerOnPageChangeCallback(pageChangeCallback);


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String indexMarker = String.valueOf(marker.getId().charAt(1));
                int positionMarker = Integer.parseInt(indexMarker);
                Log.e("TAG", "onMarkerClick: "+ indexMarker );
                viewPager.setCurrentItem(positionMarker - 1);
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {

    }


 }