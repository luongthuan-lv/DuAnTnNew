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
import android.widget.Toast;

import com.example.duantn.network.RetrofitService;
import com.example.duantn.R;
import com.example.duantn.adapter.AdapterSlideDialoginformation;
import com.example.duantn.adapter.AdapterSlideShowInformation;
import com.example.duantn.gson.Example;
import com.example.duantn.morder.ClassShowInformation;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements OnMapReadyCallback{
    private int LOCATION_REQUEST_CODE = 10001;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private ArrayList<LatLng> latLngs;
    private ViewPager2 viewPager;
    private AdapterSlideShowInformation slideShowInformation;
    private List<ImageView> imageViewList;
    private int rating;
    private int currentPage = 0;
    private Timer timer;
    private String contentFeedback;
    private LatLng mCurrentLocation;
    private final String api_key = "AIzaSyCmxFS2arHibTbROQAfTkZAJRkEpz8LErU";
    private int locationIndex = 0;
    private List<Colors> colorsList;
    private int colorIndex = 0;
    private PolylineOptions polylineOptions;
    private ArrayList<ClassShowInformation> locationList = new ArrayList<>();
    private int mLocationIndex=0;
    private boolean moveCamera=true;
    private int itemIndex=0;

    private String json = "[\n" +
            "  {\n" +
            "    \"latitude\": 21.037000,\n" +
            "    \"longitude\": 105.834727,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Lăng Bác\",\n" +
            "    \"content\": \"Lăng Bác là nơi lưu giữ thi hài của vị lãnh tụ kính yêu. Bên ngoài lăng là những hàng tre xanh bát ngát. Lăng chủ tích mở cửa vào sáng thứ 3,4,5,7 và chủ nhật. Khi vào viếng lăng Bác, bạn chú ý ăn mặc chỉnh tề, không đem theo các thiết bị điện tử ghi hành và giữ trật tự trong lăng.\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://www.bqllang.gov.vn/images/NAM_2019/THANG_1/31-1/22.jpg\",\n" +
            "      \"https://www.bqllang.gov.vn/images/NAM_2019/THANG_1/31-1/22.jpg\",\n" +
            "      \"https://dulichnamha.com/wp-content/uploads/2016/10/lang-bac-co-mo-cua-thu-7-chu-nhat-khong.jpg\",\n" +
            "      \"https://nemtv.vn/wp-content/uploads/2019/02/hinh-anh-lang-bac-nemtv-07.jpg\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"latitude\": 21.032555,\n" +
            "    \"longitude\": 105.839804,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Cột cờ Hà Nội\",\n" +
            "    \"content\": \"Kỳ đài Hà Nội hay còn được nhiều biết tới hơn với tên gọi Cột cờ Hà Nội nằm trong khuôn viên của bảo tàng lịch sử quân sự Việt Nam. Được đánh giá là công trình nguyên vẹn và hoành tráng nhất trong quần thể di tích Hoàng thành Thăng Long, Cột Cờ chính là điểm tham quan du lịch ở Hà Nội mà du khách không thể bỏ qua trong hành trình khám phá lịch sử của đất Hà Thành.\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Flag_tower%2C_Hanoi.jpg/250px-Flag_tower%2C_Hanoi.jpg\",\n" +
            "      \"https://laodongthudo.vn/stores/news_dataimages/quocdai/082019/30/17/4151_cYt_cY_HN.jpg\",\n" +
            "      \"https://upload.wikimedia.org/wikipedia/vi/1/17/C%E1%BB%99t_c%E1%BB%9D_H%C3%A0_N%E1%BB%99i_x%C6%B0a.jpg\",\n" +
            "      \"https://lh3.googleusercontent.com/proxy/GlpfWnSUxBhIrH1XVKYflWoReAlupUARUUxkaB_aYpsEWKaDJ59kBqZJ5zAw9c3F12m8fwWgpF8hiN86ugj_qJZ_c3Av-QI\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"latitude\": 21.029565,\n" +
            "    \"longitude\": 105.836206,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Văn Miếu - Quốc Tử Giám\",\n" +
            "    \"content\": \"Nếu kể tên các địa điểm du lịch Hà Nội bậc nhất xưa và nay có lẽ ai cũng sẽ nghĩ ngay đến Văn Miếu Quốc Tử Giám. Đây là một quần thể kiến trúc văn hoá hàng đầu và là niềm tự hào của người dân Thủ đô khi nhắc đến truyền thống ngàn năm văn hiến của Thăng Long – Đông Đô – Hà Nội.\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://laodongthudo.vn/stores/news_dataimages/ngocthang/012020/30/13/2337_b1a29f49-f486-45b3-ae99-f8d661ff8cb6.jpg\",\n" +
            "      \"https://laodongthudo.vn/stores/news_dataimages/ngocthang/012020/30/13/2337_b1a29f49-f486-45b3-ae99-f8d661ff8cb6.jpg\",\n" +
            "      \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/dai-trung-mon.jpg\",\n" +
            "      \"https://i0.wp.com/maskonline.vn/wp-content/uploads/2018/05/vm_2_1.jpg?resize=640%2C412\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"latitude\": 21.025445,\n" +
            "    \"longitude\": 105.846422,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Di Tích Lịch Sử Nhà Tù Hỏa Lò\",\n" +
            "    \"content\": \"Nhà tù Hỏa Lò được thực dân Pháp xây dựng từ năm 1896 với tên gọi “Maison Central”, là nơi giam giữ những chiến sĩ cách mạng chống lại chế độ thực dân. Đây là một trong những công trình kiên cố vào loại bậc nhất Đông Dương khi đó. Sau ngày giải phóng thủ đô, nhà tù được đặt dưới quyền của chính quyền cách mạng. Từ năm 1963 đến 1975, nơi đây còn được sử dụng để làm nơi giam giữ những phi công Mỹ bị quân đội Việt Nam bắn rơi trong cuộc chiến tranh phá hoại miền Bắc. Nhà tù Hỏa Lò được thực dân Pháp xây dựng từ năm 1896 với tên gọi “Maison Central”, là nơi giam giữ những chiến sĩ cách mạng chống lại chế độ thực dân. Đây là một trong những công trình kiên cố vào loại bậc nhất Đông Dương khi đó. Sau ngày giải phóng thủ đô, nhà tù được đặt dưới quyền của chính quyền cách mạng. Từ năm 1963 đến 1975, nơi đây còn được sử dụng để làm nơi giam giữ những phi công Mỹ bị quân đội Việt Nam bắn rơi trong cuộc chiến tranh phá hoại miền Bắc.\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://laodongthudo.vn/stores/news_dataimages/ngocthang/012020/30/13/2337_b1a29f49-f486-45b3-ae99-f8d661ff8cb6.jpg\",\n" +
            "      \"https://laodongthudo.vn/stores/news_dataimages/ngocthang/012020/30/13/2337_b1a29f49-f486-45b3-ae99-f8d661ff8cb6.jpg\",\n" +
            "      \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/dai-trung-mon.jpg\",\n" +
            "      \"https://i0.wp.com/maskonline.vn/wp-content/uploads/2018/05/vm_2_1.jpg?resize=640%2C412\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"latitude\": 21.024118,\n" +
            "    \"longitude\": 105.857947,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Nhà hát Lớn Hà Nội\",\n" +
            "    \"content\": \"Nằm ở số 1 Tràng Tiền, Nhà hát lớn là một trong các địa điểm du lịch đẹp ở Hà Nội mang nhiều dấu ấn lịch sử. Đây là địa điểm tổ chức những chương trình nghệ thuật lớn của nhiều ca sĩ, nghệ sĩ tên tuổi hàng đầu Việt Nam. Du khách có thể chiêm ngưỡng kiến trúc tuyệt vời của Nhà hát Lớn hay mua vé vào xem một trong những chương trình biểu diễn thường xuyên được tổ chức để có thể tận mắt thấy được hết nội thất tráng lệ của nhà hát.\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://icdn.dantri.com.vn/zoom/1200_630/2017/nha-hat-lon-ha-noi-1499853914500-crop-1499854079654.jpg\",\n" +
            "      \"https://hanoi1000.vn/wp-content/uploads/2019/09/nha-hat-lon-thumnail.jpg\",\n" +
            "      \"https://media-cdn.tripadvisor.com/media/photo-s/0e/8e/9f/f0/hanoi-opera-house.jpg\",\n" +
            "      \"https://photo-1-baomoi.zadn.vn/w1000_r1/2019_01_10_180_29302323/4196b74e8c0d65533c1c.jpg\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"latitude\": 21.028683,\n" +
            "    \"longitude\": 105.848812,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Nhà thờ Lớn Hà Nội\",\n" +
            "    \"content\": \"Nằm ở 40 phố Nhà Chung, phường Hàng Trống, Nhà thờ lớn là một trong những điểm đến thú vị ở Hà Nội, nơi lui tới không chỉ của các tín đồ theo đạo mà còn là địa điểm quen thuộc của giới trẻ, khách du lịch tứ phương. Nhà thờ được thiết kế theo phong cách kiến trúc Gothic trung cổ châu Âu với bức tường xây cao, có mái vòm và nhiều cửa sổ.\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hanoi_sjc.jpg/1200px-Hanoi_sjc.jpg\",\n" +
            "      \"https://dulichkhampha24.com/wp-content/uploads/2020/01/nha-tho-lon-ha-noi-9.jpg\",\n" +
            "      \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/07/nha-tho-lon-ha-noi-1-1.jpg\",\n" +
            "      \"https://laodongthudo.vn/stores/news_dataimages/maiquy/032020/28/14/4305_DSC_6917.jpg\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"latitude\": 21.028805,\n" +
            "    \"longitude\": 105.852150,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Hồ Hoàn Kiếm\",\n" +
            "    \"content\": \"Hồ Gươm hay hồ Hoàn Kiếm là một trong những nơi nên đến ở Hà Nội khi du lịch thủ đô. Nằm ở giữa trung tâm, Hồ Gươm được ví như trái tim của thành phố ngàn năm tuổi này.. Mặt hồ như tấm gương lớn soi bóng những cây cổ thụ, những rặng liễu thướt tha tóc rủ, những mái đền, chùa cổ kính, tháp cũ rêu phong, các toà nhà mới cao tầng vươn lên trời xanh.\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://e.dowload.vn/data/image/2020/01/08/thuyet-minh-ve-ho-guom-1.jpg\",\n" +
            "      \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/07/ho-hoan-kiem-1.png\",\n" +
            "      \"https://dulichkhampha24.com/wp-content/uploads/2020/01/gioi-thieu-ve-ho-guom-15.jpg\",\n" +
            "      \"https://phapluat.tuoitrethudo.com.vn/stores/news_dataimages/nguyenthithanhhoa/072019/06/18/in_article/ha-noi-day-manh-tuyen-truyen-su-kien-20-nam-thanh-pho-vi-hoa-binh.jpg\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"latitude\": 21.034399,\n" +
            "    \"longitude\": 105.840115,\n" +
            "    \"waypoints\": [\n" +
            "      \n" +
            "    ],\n" +
            "    \"title\": \"Hoàng Thành Thăng Long\",\n" +
            "    \"content\": \"Hoàng thành Thăng Long là quần thể di tích gắn liền với sự phát triển của Thăng Long – Hà Nội, được các triều vua xây dựng trong nhiều giai đoạn lịch sử. Đây cũng là di tích quan trọng bậc nhất trong hệ thống các di tích lịch sử của Việt Nam. Đến Hoàng thành Thăng Long du khách có thể tham quan những địa điểm nổi bật như khu khảo cổ học số 18 Hoàng Diệu, Đoan Môn, Điện Kính Thiên, Bắc Môn (thành Cửa Bắc)\",\n" +
            "    \"imageList\": [\n" +
            "      \"https://useful.vn/wp-content/uploads/2020/04/1568099002089_4146890.png\",\n" +
            "      \"https://baoxaydung.com.vn/stores/news_dataimages/vananh/112018/30/23/231143baoxaydung_image001.jpg\",\n" +
            "      \"https://thoibaonganhang.vn/stores/news_dataimages/thanhlm/022019/01/09/f40a1d5e2f78bad9fa1ab42ec1790b8f_Untitled.jpg\",\n" +
            "      \"https://sohanews.sohacdn.com/thumb_w/660/2018/2/13/photo1518495306225-15184953062251022493055.jpg\"\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        viewPager = findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = getSizeWithScale(139);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(50);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Gson gson = new Gson();
        locationList = gson.fromJson(json,new TypeToken<List<ClassShowInformation>>(){}.getType());


        setAdapter();
        setViewPager();
        addColor();
        getRetrofit(locationIndex, locationIndex + 1);

    }


    private void getRetrofit(int location1, int location2) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        String waypoints = "";
        for (int i = 0; i < locationList.get(locationIndex).getWaypoints().size(); i++) {
            waypoints += locationList.get(locationIndex).getWaypoints().get(i) + "|";
        }
        retrofitService.getHttp(getLatLng(location1), getLatLng(location2),waypoints, api_key).enqueue(new Callback<Example>() {
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
                    getRetrofit(locationIndex, locationIndex + 1);
                } else if (locationIndex == locationList.size() - 2) {
                    locationIndex++;
                    getRetrofit(locationIndex, 0);
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

        String latLng = locationList.get(i).getLatitude() + "," + locationList.get(i).getLongitude();

        return latLng;
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
        slideShowInformation = new AdapterSlideShowInformation(locationList, this, new AdapterSlideShowInformation.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                if (isConnected(false)) {
                    showCustomDialog(position);
                } else showDialogNoInternet();
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
                if (viewPager.getVisibility() == View.VISIBLE) {
                    viewPager.setVisibility(View.GONE);
                } else {
                    viewPager.setVisibility(View.VISIBLE);
                }
            }
        });
        // onLoadMap
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                dismissDialog();
            }
        });
        // onClickButtonMyLocation
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                moveCamera=true;
                return false;
            }
        });
    }

    private void showCustomDialog(int position) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_show_information, viewGroup, false);
        TextView tvContent = dialogView.findViewById(R.id.tvDialogContent);
        CardView cvDialog = dialogView.findViewById(R.id.cvDialog);

        tvContent.setText(locationList.get(position).getContent());
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        final ViewPager2 viewPager = dialogView.findViewById(R.id.viewPager);
        AdapterSlideDialoginformation adapterSlideDialoginformation = new AdapterSlideDialoginformation(locationList, this);
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

    private void addMarkerAllAndClick() {
        latLngs = new ArrayList<>();
        for (int i = 0; i < locationList.size(); i++) {
            final LatLng position = new LatLng(locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
            MarkerOptions option = new MarkerOptions();
            option.position(position);
            option.title(locationList.get(i).getTitle());
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            final Marker maker = mGoogleMap.addMarker(option);
            maker.showInfoWindow();
            latLngs.add(position);
        }


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                moveCamera=false;
                viewPager.setVisibility(View.VISIBLE);
                String indexMarker = String.valueOf(marker.getId().charAt(1));
                int positionMarker = Integer.parseInt(indexMarker);
                viewPager.setCurrentItem(positionMarker);
                return false;
            }
        });
    }

    private void setViewPageAndMarker() {
        ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(itemIndex!=0){
                    moveCamera=false;
                    final LatLng position1 = new LatLng(locationList.get(position).getLatitude(), locationList.get(position).getLongitude());
                    MarkerOptions option = new MarkerOptions();
                    option.position(position1);
                    option.title(locationList.get(position).getTitle());
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position1, 15));
                    final Marker maker = mGoogleMap.addMarker(option);
                    maker.showInfoWindow();
                }
                itemIndex=1;
            }
        };
        viewPager.registerOnPageChangeCallback(pageChangeCallback);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (mGoogleMap != null) {
                mCurrentLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                getDistance(locationResult,mLocationIndex);
                if(moveCamera){
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 20));
                }
            }
        }
    };

    private void getDistance(LocationResult locationResult,int a){
        float results[] = new float[10];
        Location.distanceBetween(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(),locationList.get(a).getLatitude(), locationList.get(a).getLongitude(),results);
        if(results[0]<200){
            viewPager.setCurrentItem(mLocationIndex);
            mLocationIndex++;
            showToast(String.valueOf(results[0]));
            if(mLocationIndex==locationList.size()){
                mLocationIndex=0;
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
    }

}