package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duantn.R;
import com.example.duantn.adapter.AdapterSlideDialoginformation;
import com.example.duantn.adapter.AdapterSlideShowInformation;
import com.example.duantn.morder.ClassShowInformation;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {
    private int LOCATION_REQUEST_CODE = 10001;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private FloatingActionButton btnMyLocation;
    private ArrayList<ClassShowInformation> showInformationArrayList;
    private ArrayList<LatLng> latLngs;
    private Polygon line;
    private PolygonOptions polylineOptions;
    private ViewPager2 viewPager;
    private AdapterSlideShowInformation slideShowInformation;
    private List<ImageView> imageViewList;
    private int rating;
    private ArrayList<ClassShowInformation> imgListInformation;
    private int currentPage = 0;
    private Timer timer;
    private String contentFeedback;
    private Marker userLocationMarker;
    public LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMyLocation = findViewById(R.id.add_fab);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        initDialogLoading();
        showDialogLoading();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync((OnMapReadyCallback) MainActivity.this);
        }
        addDataSlideInformation();
        viewPager = findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = getSizeWithScale(139);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(200);
        locationRequest.setFastestInterval(200);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        setAdapter();
        setViewPager();
        addDataListImg();
    }

    private void addDataListImg() {
        imgListInformation = new ArrayList<>();
        imgListInformation.add(new ClassShowInformation(new String[]{"https://www.bqllang.gov.vn/images/NAM_2019/THANG_1/31-1/22.jpg", "https://www.bqllang.gov.vn/images/NAM_2019/THANG_1/31-1/22.jpg", "https://dulichnamha.com/wp-content/uploads/2016/10/lang-bac-co-mo-cua-thu-7-chu-nhat-khong.jpg", "https://nemtv.vn/wp-content/uploads/2019/02/hinh-anh-lang-bac-nemtv-07.jpg"}));
        imgListInformation.add(new ClassShowInformation(new String[]{"https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Flag_tower%2C_Hanoi.jpg/250px-Flag_tower%2C_Hanoi.jpg", "https://laodongthudo.vn/stores/news_dataimages/quocdai/082019/30/17/4151_cYt_cY_HN.jpg", "https://upload.wikimedia.org/wikipedia/vi/1/17/C%E1%BB%99t_c%E1%BB%9D_H%C3%A0_N%E1%BB%99i_x%C6%B0a.jpg", "https://lh3.googleusercontent.com/proxy/GlpfWnSUxBhIrH1XVKYflWoReAlupUARUUxkaB_aYpsEWKaDJ59kBqZJ5zAw9c3F12m8fwWgpF8hiN86ugj_qJZ_c3Av-QI"}));
        imgListInformation.add(new ClassShowInformation(new String[]{"https://laodongthudo.vn/stores/news_dataimages/ngocthang/012020/30/13/2337_b1a29f49-f486-45b3-ae99-f8d661ff8cb6.jpg", "https://laodongthudo.vn/stores/news_dataimages/ngocthang/012020/30/13/2337_b1a29f49-f486-45b3-ae99-f8d661ff8cb6.jpg", "https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/dai-trung-mon.jpg", "https://i0.wp.com/maskonline.vn/wp-content/uploads/2018/05/vm_2_1.jpg?resize=640%2C412"}));
        imgListInformation.add(new ClassShowInformation(new String[]{"https://sodulich.hanoi.gov.vn/storage/nhatuhoalo120190915230108.png", "https://sodulich.hanoi.gov.vn/storage/nhatuhoalo120190915230108.png", "https://cdnmedia.baotintuc.vn/2014/07/25/19/23/hoalo7%20(2).JPG", "https://cdnimg.vietnamplus.vn/t870/uploaded/fsmsy/2020_01_29/ttxvn_nha_tu_hoa_lo_1.jpg"}));
        imgListInformation.add(new ClassShowInformation(new String[]{"https://icdn.dantri.com.vn/zoom/1200_630/2017/nha-hat-lon-ha-noi-1499853914500-crop-1499854079654.jpg", "https://hanoi1000.vn/wp-content/uploads/2019/09/nha-hat-lon-thumnail.jpg", "https://media-cdn.tripadvisor.com/media/photo-s/0e/8e/9f/f0/hanoi-opera-house.jpg", "https://photo-1-baomoi.zadn.vn/w1000_r1/2019_01_10_180_29302323/4196b74e8c0d65533c1c.jpg"}));
        imgListInformation.add(new ClassShowInformation(new String[]{"https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hanoi_sjc.jpg/1200px-Hanoi_sjc.jpg", "https://dulichkhampha24.com/wp-content/uploads/2020/01/nha-tho-lon-ha-noi-9.jpg", "https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/07/nha-tho-lon-ha-noi-1-1.jpg", "https://laodongthudo.vn/stores/news_dataimages/maiquy/032020/28/14/4305_DSC_6917.jpg"}));
        imgListInformation.add(new ClassShowInformation(new String[]{"https://e.dowload.vn/data/image/2020/01/08/thuyet-minh-ve-ho-guom-1.jpg", "https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/07/ho-hoan-kiem-1.png", "https://dulichkhampha24.com/wp-content/uploads/2020/01/gioi-thieu-ve-ho-guom-15.jpg", "https://phapluat.tuoitrethudo.com.vn/stores/news_dataimages/nguyenthithanhhoa/072019/06/18/in_article/ha-noi-day-manh-tuyen-truyen-su-kien-20-nam-thanh-pho-vi-hoa-binh.jpg"}));
        imgListInformation.add(new ClassShowInformation(new String[]{"https://useful.vn/wp-content/uploads/2020/04/1568099002089_4146890.png", "https://baoxaydung.com.vn/stores/news_dataimages/vananh/112018/30/23/231143baoxaydung_image001.jpg", "https://thoibaonganhang.vn/stores/news_dataimages/thanhlm/022019/01/09/f40a1d5e2f78bad9fa1ab42ec1790b8f_Untitled.jpg", "https://sohanews.sohacdn.com/thumb_w/660/2018/2/13/photo1518495306225-15184953062251022493055.jpg"}));

    }

    private void createDialogRating() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View alertLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_rating, (LinearLayout) findViewById(R.id.layout_content));

        ImageView img_star1, img_star2, img_star3, img_star4, img_star5;
        final EditText edt_note;
        Button btn_submit;
        img_star1 = alertLayout.findViewById(R.id.img_star1);
        img_star2 = alertLayout.findViewById(R.id.img_star2);
        img_star3 = alertLayout.findViewById(R.id.img_star3);
        img_star4 = alertLayout.findViewById(R.id.img_star4);
        img_star5 = alertLayout.findViewById(R.id.img_star5);
        edt_note = alertLayout.findViewById(R.id.edt_note);
        btn_submit = alertLayout.findViewById(R.id.btn_submit);
        edt_note.setImeOptions(EditorInfo.IME_ACTION_DONE);
        imageViewList = Arrays.asList(new ImageView[]{img_star1, img_star2, img_star3, img_star4, img_star5});

        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setImageResource(R.drawable.no_selected_star);
        }
        for (int i = 0; i < imageViewList.size(); i++) {
            final int finalI = i;
            imageViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < imageViewList.size(); i++) {
                        imageViewList.get(i).setImageResource(R.drawable.no_selected_star);
                    }

                    for (int j = 0; j < finalI + 1; j++) {
                        imageViewList.get(j).setImageResource(R.drawable.selected_star);
                        //ket qua rating
                        rating = j + 1;
                    }
                }
            });
        }
        alert.setView(alertLayout);
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendFeedback(edt_note);

            }
        });
        dialog.show();

    }

    private void sendFeedback(EditText edt) {
        contentFeedback = edt.getText().toString().trim();
        showToast(contentFeedback + "\n" + rating + " sao");
    }

    private void addDataSlideInformation() {
        showInformationArrayList = new ArrayList<>();

        showInformationArrayList.add(new ClassShowInformation(21.037000, 105.834727, "Lăng Bác", "Lăng Bác là nơi lưu giữ thi hài của vị lãnh tụ kính yêu. Bên ngoài lăng là những hàng tre xanh bát ngát. Lăng chủ tích mở cửa vào sáng thứ 3,4,5,7 và chủ nhật. Khi vào viếng lăng Bác, bạn chú ý ăn mặc chỉnh tề, không đem theo các thiết bị điện tử ghi hành và giữ trật tự trong lăng.", 0, "https://www.bqllang.gov.vn/images/NAM_2019/THANG_1/31-1/22.jpg"));
        showInformationArrayList.add(new ClassShowInformation(21.032555, 105.839804, "Cột cờ Hà Nội", "Kỳ đài Hà Nội hay còn được nhiều biết tới hơn với tên gọi Cột cờ Hà Nội nằm trong khuôn viên của bảo tàng lịch sử quân sự Việt Nam. Được đánh giá là công trình nguyên vẹn và hoành tráng nhất trong quần thể di tích Hoàng thành Thăng Long, Cột Cờ chính là điểm tham quan du lịch ở Hà Nội mà du khách không thể bỏ qua trong hành trình khám phá lịch sử của đất Hà Thành.", 1, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Flag_tower%2C_Hanoi.jpg/250px-Flag_tower%2C_Hanoi.jpg"));
        showInformationArrayList.add(new ClassShowInformation(21.029565, 105.836206, "Văn Miếu - Quốc Tử Giám", "Nếu kể tên các địa điểm du lịch Hà Nội bậc nhất xưa và nay có lẽ ai cũng sẽ nghĩ ngay đến Văn Miếu Quốc Tử Giám. Đây là một quần thể kiến trúc văn hoá hàng đầu và là niềm tự hào của người dân Thủ đô khi nhắc đến truyền thống ngàn năm văn hiến của Thăng Long – Đông Đô – Hà Nội.", 2, "https://laodongthudo.vn/stores/news_dataimages/ngocthang/012020/30/13/2337_b1a29f49-f486-45b3-ae99-f8d661ff8cb6.jpg"));
        showInformationArrayList.add(new ClassShowInformation(21.025445, 105.846422, "Di Tích Lịch Sử Nhà Tù Hỏa Lò", "Nhà tù Hỏa Lò được thực dân Pháp xây dựng từ năm 1896 với tên gọi “Maison Central”, là nơi giam giữ những chiến sĩ cách mạng chống lại chế độ thực dân. Đây là một trong những công trình kiên cố vào loại bậc nhất Đông Dương khi đó. Sau ngày giải phóng thủ đô, nhà tù được đặt dưới quyền của chính quyền cách mạng. Từ năm 1963 đến 1975, nơi đây còn được sử dụng để làm nơi giam giữ những phi công Mỹ bị quân đội Việt Nam bắn rơi trong cuộc chiến tranh phá hoại miền Bắc. Nhà tù Hỏa Lò được thực dân Pháp xây dựng từ năm 1896 với tên gọi “Maison Central”, là nơi giam giữ những chiến sĩ cách mạng chống lại chế độ thực dân. Đây là một trong những công trình kiên cố vào loại bậc nhất Đông Dương khi đó. Sau ngày giải phóng thủ đô, nhà tù được đặt dưới quyền của chính quyền cách mạng. Từ năm 1963 đến 1975, nơi đây còn được sử dụng để làm nơi giam giữ những phi công Mỹ bị quân đội Việt Nam bắn rơi trong cuộc chiến tranh phá hoại miền Bắc.", 3, "https://sodulich.hanoi.gov.vn/storage/nhatuhoalo120190915230108.png"));
        showInformationArrayList.add(new ClassShowInformation(21.024118, 105.857947, "Nhà hát Lớn Hà Nội", "Nằm ở số 1 Tràng Tiền, Nhà hát lớn là một trong các địa điểm du lịch đẹp ở Hà Nội mang nhiều dấu ấn lịch sử. Đây là địa điểm tổ chức những chương trình nghệ thuật lớn của nhiều ca sĩ, nghệ sĩ tên tuổi hàng đầu Việt Nam. Du khách có thể chiêm ngưỡng kiến trúc tuyệt vời của Nhà hát Lớn hay mua vé vào xem một trong những chương trình biểu diễn thường xuyên được tổ chức để có thể tận mắt thấy được hết nội thất tráng lệ của nhà hát.", 4, "https://icdn.dantri.com.vn/zoom/1200_630/2017/nha-hat-lon-ha-noi-1499853914500-crop-1499854079654.jpg"));
        showInformationArrayList.add(new ClassShowInformation(21.028683, 105.848812, "Nhà thờ Lớn Hà Nội", "Nằm ở 40 phố Nhà Chung, phường Hàng Trống, Nhà thờ lớn là một trong những điểm đến thú vị ở Hà Nội, nơi lui tới không chỉ của các tín đồ theo đạo mà còn là địa điểm quen thuộc của giới trẻ, khách du lịch tứ phương. Nhà thờ được thiết kế theo phong cách kiến trúc Gothic trung cổ châu Âu với bức tường xây cao, có mái vòm và nhiều cửa sổ.", 5, "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hanoi_sjc.jpg/1200px-Hanoi_sjc.jpg"));
        showInformationArrayList.add(new ClassShowInformation(21.028805, 105.852150, "Hồ Hoàn Kiếm", "Hồ Gươm hay hồ Hoàn Kiếm là một trong những nơi nên đến ở Hà Nội khi du lịch thủ đô. Nằm ở giữa trung tâm, Hồ Gươm được ví như trái tim của thành phố ngàn năm tuổi này.. Mặt hồ như tấm gương lớn soi bóng những cây cổ thụ, những rặng liễu thướt tha tóc rủ, những mái đền, chùa cổ kính, tháp cũ rêu phong, các toà nhà mới cao tầng vươn lên trời xanh.", 6, "https://e.dowload.vn/data/image/2020/01/08/thuyet-minh-ve-ho-guom-1.jpg"));
        showInformationArrayList.add(new ClassShowInformation(21.034399, 105.840115, "Hoàng Thành Thăng Long", "Hoàng thành Thăng Long là quần thể di tích gắn liền với sự phát triển của Thăng Long – Hà Nội, được các triều vua xây dựng trong nhiều giai đoạn lịch sử. Đây cũng là di tích quan trọng bậc nhất trong hệ thống các di tích lịch sử của Việt Nam. Đến Hoàng thành Thăng Long du khách có thể tham quan những địa điểm nổi bật như khu khảo cổ học số 18 Hoàng Diệu, Đoan Môn, Điện Kính Thiên, Bắc Môn (thành Cửa Bắc)…", 7, "https://useful.vn/wp-content/uploads/2020/04/1568099002089_4146890.png"));

    }

    private void setAdapter() {
        slideShowInformation = new AdapterSlideShowInformation(showInformationArrayList, this, new AdapterSlideShowInformation.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                if (isConnected(false)) {
                    showCustomDialog(position);
                }else showDialogNoInternet();
            }

            @Override
            public void onSwitched(boolean isChecked) {

            }
        });
    }

    private void setViewPager() {
        viewPager.setAdapter(slideShowInformation);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);

        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(50));
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

    private void setUserLocationMarker(Location location){
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        if(userLocationMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
//            markerOptions.rotation(location.getBearing());
//            markerOptions.anchor((float) 0.5,(float)0.5);
            userLocationMarker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng,22)));
        }else {
            userLocationMarker.setPosition(latLng);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,22));
        }


    }
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("TAG", "onLocationResult: " + locationResult.getLastLocation());
            if (mGoogleMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        startLocationUpdates();
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                stopLocationUpdate();
                if (btnMyLocation.getVisibility() == View.VISIBLE && viewPager.getVisibility() == View.VISIBLE) {
                    btnMyLocation.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                } else {
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

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                   startLocationUpdates();
                } else {

                }

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

                stopLocationUpdate();

                final LatLng position1 = new LatLng(showInformationArrayList.get(position).getLatitude(), showInformationArrayList.get(position).getLongitude());
                MarkerOptions option = new MarkerOptions();
                option.position(position1);
                option.title(showInformationArrayList.get(position).getTitle());
                option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position1, 15));
                Log.e("TAG", "onPageSelected: " + position);
                final Marker maker = mGoogleMap.addMarker(option);
                maker.showInfoWindow();
            }
        };
        viewPager.registerOnPageChangeCallback(pageChangeCallback);


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                stopLocationUpdate();
                viewPager.setVisibility(View.VISIBLE);
                String indexMarker = String.valueOf(marker.getId().charAt(1));
                int positionMarker = Integer.parseInt(indexMarker);
                Log.e("TAG", "onMarkerClick: " + indexMarker);
                viewPager.setCurrentItem(positionMarker - 1);
                return false;
            }
        });
    }


    private void showCustomDialog(int position) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_show_information, viewGroup, false);
        TextView tvContent = dialogView.findViewById(R.id.tvDialogContent);
        CardView cvDialog = dialogView.findViewById(R.id.cvDialog);

        tvContent.setText(showInformationArrayList.get(position).getContent());
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        final ViewPager2 viewPager = dialogView.findViewById(R.id.viewPager);
        AdapterSlideDialoginformation adapterSlideDialoginformation = new AdapterSlideDialoginformation(imgListInformation.get(position).imgInformationList, this);
        viewPager.setAdapter(adapterSlideDialoginformation);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                currentPage = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(currentPage, true);
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
        tvContent.getLayoutParams().width = getSizeWithScale(331);
        tvContent.getLayoutParams().height = getSizeWithScale(261);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onClick(View v) {

    }



    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdate();
    }

}