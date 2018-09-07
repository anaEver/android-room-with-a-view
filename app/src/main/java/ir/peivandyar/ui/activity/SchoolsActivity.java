package ir.peivandyar.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.roomwordssample.R;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import ir.peivandyar.database.entity.SchoolInfo;
import ir.peivandyar.database.viewmodel.SchoolInfoViewModel;
import ir.peivandyar.net.RestService;
import ir.peivandyar.ui.adapter.SchoolInfoListAdapter;
import ir.peivandyar.ui.util.Constants;
import ir.peivandyar.ui.util.OnListInteractionsListener;
import ir.peivandyar.ui.util.PersianDigitConverter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SchoolsActivity extends AppCompatActivity implements
        OnListInteractionsListener {

    public static final RestService rs = new RestService();
    public android.arch.lifecycle.LifecycleOwner owner;

    public List<String> provincesTotal, townsTotal, gendersTotal, provincesSearch, townsSearch, gendersSearch;
    public List<String> provincesStrings, regionsStrings, townsStrings;
    public List<Integer> regionsTotal, regionsSearch;
    public List<Boolean> likesTotal, likesSearch;
    public String name;
    public int scrolled = Constants.scrolled;
    public List<SchoolInfo> schoolInfoList;
    ArrayAdapter<String> provincesArrayAdapter, townsArrayAdapter, regionsArrayAdapter;
    public SchoolInfoListAdapter adapter;

    AppCompatImageView searchDropIcon, searchDeleteIcon, searchButton;
    public AppCompatSpinner provincesSpinner, regionsSpinner, townsSpinner;
    ConstraintLayout searchOptionsLayout, searchOptions, optionsSelectedLayout;
    RecyclerView recyclerView;
    SchoolInfoViewModel mSchoolInfoViewModel;
    CardView search;
    boolean visible;
    TextView loadMore, provinceSelected, townSelected, regionSelected;
    SwitchCompat boyToggle, girlToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schools);

        initilizeSearchArrays();

        schoolInfoList = new ArrayList<>();

        mSchoolInfoViewModel = ViewModelProviders.of(this).get(SchoolInfoViewModel.class);
        owner = this;
        visible = true;

        SchoolInfo farda = new SchoolInfo();
        farda.id = 100;
        farda.imageUrl = "http://peivandyar.ir/images/schools/farzanegan-poya/IMG_2749.JPG";
        farda.lastRefreshed = System.currentTimeMillis();
        farda.liked = false;
        farda.phoneNumber = PersianDigitConverter.PerisanNumber("028-33667589");
        farda.province = "قزوین";
        farda.town = "قزوین";
        farda.region = 1;
        farda.about = "---";
        farda.schoolName = "فردا - فرزانگان پویا";
        farda.gender = "دخترانه";
        farda.address = PersianDigitConverter.PerisanNumber("قزوین ، خیابان دانشگاه ، کوچه 25 ");
        mSchoolInfoViewModel.insert(farda);


        SchoolInfo pardisan = new SchoolInfo();
        pardisan.id = 101;
        pardisan.imageUrl = "http://peivandyar.ir/images/schools/pardisan/IMG_2700.JPG";
        pardisan.lastRefreshed = System.currentTimeMillis();
        pardisan.liked = false;
        pardisan.phoneNumber = PersianDigitConverter.PerisanNumber("028-33335050");
        pardisan.province = "قزوین";
        pardisan.town = "قزوین";
        pardisan.region = 1;
        pardisan.about = "با مدیریت خانم حدادی با 27 سال سابقه کار آموزشی و 18 سال سابقه مدیریت در حوزه ی مدارس غیردولتی";
        pardisan.schoolName = "دبیرستان غیردولتی پردیسان بهشتیان";
        pardisan.gender = "دخترانه";
        pardisan.address = PersianDigitConverter.PerisanNumber("قزوین ، ملاصدرا ، کوچه رجا ، پلاک 39");
        mSchoolInfoViewModel.insert(pardisan);

        SchoolInfo anahid = new SchoolInfo();
        anahid.id = 102;
        anahid.imageUrl = "http://peivandyar.ir/images/schools/anahid/IMG_2789.JPG";
        anahid.lastRefreshed = System.currentTimeMillis();
        anahid.liked = false;
        anahid.phoneNumber = PersianDigitConverter.PerisanNumber("028-33670147");
        anahid.province = "قزوین";
        anahid.town = "قزوین";
        anahid.region = 1;
        anahid.about = "با مدیریت خانم جباری ، رییس گروه اسبق اداره کل آموزش و پرورش استان قزوین";
        anahid.schoolName = "آناهید";
        anahid.gender = "دخترانه";
        anahid.address = PersianDigitConverter.PerisanNumber("قزوین ، میدان جانبازان ، بلوار مطهری ، پلاک 93");
        mSchoolInfoViewModel.insert(anahid);


        ///////////////            ui initializations                /////////////////

        searchOptions = findViewById(R.id.school_search_drop);
        searchOptionsLayout = findViewById(R.id.selectOptionsLayout);
        search = findViewById(R.id.search_card);
        searchDropIcon = findViewById(R.id.searchDropIcon);
        searchDeleteIcon = findViewById(R.id.searchDeleteIcon);
        searchButton = findViewById(R.id.doSchoolSearch);
        recyclerView = findViewById(R.id.schoolCards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        provincesSpinner = findViewById(R.id.provinceSpinner);
        townsSpinner = findViewById(R.id.townSpinner);
        regionsSpinner = findViewById(R.id.regionSpinner);
        loadMore = findViewById(R.id.loadMore);
        provinceSelected = findViewById(R.id.provinceSelected);
        townSelected = findViewById(R.id.townSelected);
        regionSelected = findViewById(R.id.regionSelected);
        optionsSelectedLayout = findViewById(R.id.selectedOptionsLayout);


        adapter = new SchoolInfoListAdapter(this, this);

        ////////////////// actions////////////////////

        recyclerView.setAdapter(adapter);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mSchoolInfoViewModel.searchSchools(townsTotal,
                        provincesTotal, name, likesTotal,
                        gendersTotal, regionsTotal).observe(owner, new Observer<List<SchoolInfo>>() {
                    @Override
                    public void onChanged(@Nullable List<SchoolInfo> schoolInfos) {
                        schoolInfoList = schoolInfos;
                        adapter.setSchool(schoolInfoList);
                    }
                });

            }
        });


        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMore.setText("...");
                rs.getService().Get(new Callback<List<SchoolInfo>>() {
                    @Override
                    public void success(List<SchoolInfo> schoolInfos, Response response) {
                        loadMore.setVisibility(View.GONE);
                        mSchoolInfoViewModel.insertAll(schoolInfos);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "خطا در اتصال به اینترنت", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

        searchOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animateLayout();

                visible = !visible;

                if (visible) {
                    adapter.setSchool(mSchoolInfoViewModel.getAllSchoolInfos().getValue());
                }

            }
        });


        mSchoolInfoViewModel.getProvinces().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> provinces) {
                provincesTotal.clear();
                provincesTotal.addAll(provinces);
                provincesStrings.clear();
                provincesStrings.add("همه استانها");
                provincesStrings.addAll(provincesTotal);
                provincesArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, provincesStrings);
                provincesSpinner.setAdapter(provincesArrayAdapter);
                provincesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (!adapterView.getItemAtPosition(i).toString().contains("همه")) {
                            Transition transition = new ChangeBounds();
                            TransitionManager.beginDelayedTransition(search, transition);
                            TransitionManager.beginDelayedTransition(recyclerView);
                            optionsSelectedLayout.setVisibility(View.VISIBLE);
                            provinceSelected.setText(adapterView.getItemAtPosition(i).toString());
                        } else {
                            provinceSelected.setText("همه");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });

        mSchoolInfoViewModel.getTowns().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> towns) {
                townsTotal.clear();
                townsTotal.addAll(towns);
                townsStrings.clear();
                townsStrings.add("همه شهرها");
                townsStrings.addAll(townsTotal);
                townsArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, townsStrings);
                townsSpinner.setAdapter(townsArrayAdapter);
                townsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (!adapterView.getItemAtPosition(i).toString().contains("همه")) {

                            Transition transition = new ChangeBounds();
                            TransitionManager.beginDelayedTransition(search, transition);
                            TransitionManager.beginDelayedTransition(recyclerView);
                            optionsSelectedLayout.setVisibility(View.VISIBLE);
                            townSelected.setText(adapterView.getItemAtPosition(i).toString());
                        } else {
                            townSelected.setText("همه");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });

        mSchoolInfoViewModel.getRegions().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> regions) {
                regionsTotal.clear();
                regionsTotal.addAll(regions);
                regionsStrings.clear();
                regionsStrings.add("همه مناطق");
                for (Integer i : regionsTotal
                        ) {
                    regionsStrings.add(PersianDigitConverter.PerisanNumber(i.toString()));
                }
                regionsArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, regionsStrings);
                regionsSpinner.setAdapter(regionsArrayAdapter);
                regionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!adapterView.getItemAtPosition(i).toString().contains("همه")) {
                            Transition transition = new ChangeBounds();
                            TransitionManager.beginDelayedTransition(search, transition);
                            TransitionManager.beginDelayedTransition(recyclerView);
                            optionsSelectedLayout.setVisibility(View.VISIBLE);
                            regionSelected.setText(adapterView.getItemAtPosition(i).toString());
                        } else {
                            regionSelected.setText("همه");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });


        mSchoolInfoViewModel.getAllSchoolInfos().observe(this, new Observer<List<SchoolInfo>>() {
            @Override
            public void onChanged(@Nullable List<SchoolInfo> schoolInfos) {
                adapter.setSchool(schoolInfos);
            }
        });
    }

    private void animateLayout() {
        Transition transition = new ChangeBounds();
        transition.setInterpolator(new FastOutLinearInInterpolator());
        TransitionManager.beginDelayedTransition(search, transition);
        TransitionManager.beginDelayedTransition(recyclerView);
        TransitionManager.beginDelayedTransition(searchOptions,
                new TransitionSet().addTransition(new Fade()));
        searchDropIcon.setVisibility(visible ? View.GONE : View.VISIBLE);
        searchDeleteIcon.setVisibility(visible ? View.VISIBLE : View.GONE);
        searchOptionsLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        optionsSelectedLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void initilizeSearchArrays() {

        regionsSearch = new ArrayList<>();
        townsSearch = new ArrayList<>();
        provincesSearch = new ArrayList<>();
        likesSearch = new ArrayList<>();
        gendersSearch = new ArrayList<>();

        regionsStrings = new ArrayList<>();
        townsStrings = new ArrayList<>();
        provincesStrings = new ArrayList<>();

        regionsTotal = new ArrayList<>();
        townsTotal = new ArrayList<>();
        provincesTotal = new ArrayList<>();
        likesTotal = new ArrayList<>();
        likesTotal.add(true);
        likesTotal.add(false);
        gendersTotal = new ArrayList<>();
        gendersTotal.add(getResources().getString(R.string.boy));
        gendersTotal.add(getResources().getString(R.string.girl));
        name = "";

        gendersSearch.addAll(gendersTotal);
        likesSearch.addAll(likesTotal);
        townsSearch.add("تهران");


    }

    @Override
    public void onStateChanged(int state) {
        switch (state) {
            case Constants.listsEnd:
                loadMore.setVisibility(View.VISIBLE);
                break;
            case Constants.scrolled:
                    loadMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void itemSelected(Object item) {

    }


}
