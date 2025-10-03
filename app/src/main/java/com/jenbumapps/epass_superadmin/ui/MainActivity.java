package com.jenbumapps.epass_superadmin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.jenbumapps.core.api.ApiManager;
import com.jenbumapps.core.model.viewmodel.CityViewModel;
import com.jenbumapps.core.model.viewmodel.HomeModel;
import com.jenbumapps.epass_superadmin.R;
import com.jenbumapps.epass_superadmin.app.App;
import com.jenbumapps.epass_superadmin.ui.division.DivisionActivity;
import com.jenbumapps.epass_superadmin.ui.permit.CityPermitActivity;
import com.jenbumapps.epass_superadmin.ui.permit.CityViewModelAdapter;
import com.jenbumapps.epass_superadmin.ui.user.UserListActivity;
import com.jenbumapps.epass_superadmin.utility.SpacesItemDecoration;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvTotalExpiredReq;
    private TextView tvTotalNewReq;
    private TextView tvTotalActive;
    private LinearLayout llExpiredReq;
    private LinearLayout llNewReq;

    private ExtendedFloatingActionButton eFabConfig;
    private SwipeRefreshLayout srlMainActivity;

    private RecyclerView rvCity;
    private CityViewModelAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        initView();
        initListener();

        fetchData();
    }

    private void initView() {

        srlMainActivity = findViewById(R.id.srl_main_activity);

        tvTotalActive = findViewById(R.id.tv_total_active_permits);
        tvTotalNewReq = findViewById(R.id.tv_total_new_requests);
        tvTotalExpiredReq = findViewById(R.id.tv_total_expired_req);

        llNewReq = findViewById(R.id.ll_new_req);
        llExpiredReq = findViewById(R.id.ll_expired_req);
        eFabConfig = findViewById(R.id.efab_config);
        rvCity = findViewById(R.id.rv_cities);

    }

    private void initAdapter() {
        mAdapter = new CityViewModelAdapter(App.homeModel.getCityViewModels(), new CityViewModelAdapter.Listener() {
            @Override
            public void onCityModelSelected(CityViewModel cvm, int pos) {
                startCityPermitActivity(cvm);
            }
        });

        rvCity.setAdapter(mAdapter);
        rvCity.setLayoutManager(new GridLayoutManager(this, 2));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing_8dp);
        rvCity.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    private void initListener() {

        srlMainActivity.setOnRefreshListener(this);

        llNewReq.setOnClickListener(this);
        llExpiredReq.setOnClickListener(this);
        eFabConfig.setOnClickListener(this);

    }

    private void fetchData() {
        if (!srlMainActivity.isRefreshing()) srlMainActivity.setRefreshing(true);
        ApiManager.VIEW_MODEL.fetchHomeData()
                .enqueue(new Callback<HomeModel>() {
                    @Override
                    public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                        if (srlMainActivity.isRefreshing()) srlMainActivity.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                App.homeModel = response.body();
                                initAdapter();
                                setData();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Error Code - " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HomeModel> call, Throwable t) {
                        if (srlMainActivity.isRefreshing()) srlMainActivity.setRefreshing(false);
                        t.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setData() {
        mAdapter.notifyDataSetChanged();
        tvTotalActive.setText(String.valueOf(App.homeModel.getTotalActiveReq()));
        tvTotalNewReq.setText(String.valueOf(App.homeModel.getTotalNewReq()));
        tvTotalExpiredReq.setText(String.valueOf(App.homeModel.getTotalExpiredReq()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_new_req:
                break;

            case R.id.ll_expired_req:
                break;

            case R.id.efab_config:
                showOptionMenu();
                break;

        }
    }

    private void showOptionMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, eFabConfig);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.main_activity_popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.item_division) {
                    startActivity(new Intent(MainActivity.this, DivisionActivity.class));
                } else if (item.getItemId() == R.id.item_user) {
                    startActivity(new Intent(MainActivity.this, UserListActivity.class));
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    private void startCityPermitActivity(CityViewModel cvm) {
        Intent bsaIntent = new Intent(this, CityPermitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("CITY_MODEL", Parcels.wrap(cvm));
        bsaIntent.putExtras(bundle);
        startActivity(bsaIntent);
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
