package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.example.duantn.R;
import com.example.duantn.adapter.FeedbackAdapter;
import com.example.duantn.morder.Feedback;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;
import java.util.Objects;

public class CommentListActivity extends BaseActivity {

    private List<Feedback> feedbackList;
    private RecyclerView rv2;
    private FeedbackAdapter feedbackAdapter;
    private boolean seeMore = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        feedbackList = TourIntroduceActivity.feedbackList;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setAdapter();
        setViewPager();
    }

    private void setAdapter(){
        feedbackAdapter = new FeedbackAdapter(feedbackList, this,seeMore);
    }

    private void setViewPager(){
        rv2 = findViewById(R.id.rv2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        rv2.setLayoutManager(linearLayoutManager2);
        rv2.setAdapter(feedbackAdapter);
        feedbackAdapter.isShimmer=false;
        feedbackAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}