package ir.peivandyar.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.example.android.roomwordssample.R;

import java.util.ArrayList;
import java.util.List;

import ir.peivandyar.database.entity.SchoolInfo;
import ir.peivandyar.database.viewmodel.SchoolInfoViewModel;
import ir.peivandyar.net.RestService;
import ir.peivandyar.ui.adapter.SchoolInfoListAdapter;
import ir.peivandyar.ui.util.OnListInteractionsListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SchoolsActivity extends AppCompatActivity {

    public static final RestService rs = new RestService();


    AppCompatImageView searchOptions, provinceSelect, townSelect, regionSelect;
    ConstraintLayout searchOptionsLayout, optionsSelectedLayout;
    CardView search;
    boolean visible;

    OnListInteractionsListener listener = new OnListInteractionsListener() {
        @Override
        public void onStateChanged(int state) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schools);

        final SchoolInfoViewModel mSchoolInfoViewModel = ViewModelProviders.of(this).get(SchoolInfoViewModel.class);


        /// ui initializations/////////////////

        searchOptions = findViewById(R.id.school_search_drop);
        searchOptionsLayout = findViewById(R.id.searchSchoolsExpanded);

        final ArrayList<SchoolInfo> alakis = new ArrayList<SchoolInfo>();
        final SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setId(10);
        schoolInfo.setLastRefreshed(System.currentTimeMillis());
        mSchoolInfoViewModel.insert(schoolInfo);

        SchoolInfo alaki = new SchoolInfo();
        alaki.setId(24);
        alaki.setProvince("mashhad");
        alaki.setLastRefreshed(System.currentTimeMillis());
        mSchoolInfoViewModel.insert(alaki);


        RecyclerView recyclerView = findViewById(R.id.schoolCards);
        final SchoolInfoListAdapter adapter = new SchoolInfoListAdapter(this, this.listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setSchool(alakis);


        rs.getService().Get(new Callback<List<SchoolInfo>>() {
            @Override
            public void success(List<SchoolInfo> schoolInfos, Response response) {
                mSchoolInfoViewModel.insertAll(schoolInfos);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();

            }
        });


        ////////////////// actions////////////////////

        searchOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SchoolInfo alaki = new SchoolInfo();
                alaki.setId(38);
                alaki.setProvince("قزوین");
                alaki.setLastRefreshed(System.currentTimeMillis());
                mSchoolInfoViewModel.insert(alaki);
            }
        });


        mSchoolInfoViewModel.getProvinces().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> provinces) {
//                    Toast.makeText(getApplicationContext(),"is null? " + (provinces == null)+provinces.size(), Toast.LENGTH_LONG).show();
            }
        });

        mSchoolInfoViewModel.getTowns().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> towns) {
                Toast.makeText(getApplicationContext(),"is null? " + (towns == null)+towns.size(), Toast.LENGTH_SHORT).show();
            }
        });

        mSchoolInfoViewModel.getRegions().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> regions) {
//                Toast.makeText(getApplicationContext(),"is null? " + (regions == null)+regions.size(), Toast.LENGTH_LONG).show();
            }
        });

        mSchoolInfoViewModel.getAllSchoolInfos().observe(this, new Observer<List<SchoolInfo>>() {
            @Override
            public void onChanged(List<SchoolInfo> schoolInfos) {
                adapter.setSchool(schoolInfos);
            }
        });

    }
}
