package com.jenbumapps.epass_superadmin.ui.permit;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.jenbumapps.core.api.ApiManager;
import com.jenbumapps.core.model.City;
import com.jenbumapps.core.model.EPass;
import com.jenbumapps.core.model.time.DateHelper;
import com.jenbumapps.epass_superadmin.R;
import com.jenbumapps.epass_superadmin.ui.dialog.FilterDialog;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermitListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PermitAdapter.Listener, View.OnClickListener {

    private enum ResultType {ACTIVE_REQ, EXPIRED_REQ, REJECTED_REQ, EXPIRED_PERMIT}

    private ResultType resultType = ResultType.ACTIVE_REQ;
    private TextView tvTitle;
    private TextView tvDateFilter;
    private RecyclerView rvPermits;
    private ExtendedFloatingActionButton eFabMenu;

    private PermitAdapter mAdapter;
    private List<EPass> permits = new ArrayList<>();
    private SwipeRefreshLayout srlPermit;
    private Calendar mFromDate;
    private Calendar mToDate;

    private TextView tvNoItem;

    private City mCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_permit_list);

        initView();
        initAdapter();
        initListener();

        mCity = Parcels.unwrap(getIntent().getParcelableExtra("KEY_CITY"));
        if (mCity == null) {
            finish();
        }

        setData();

    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        eFabMenu = findViewById(R.id.efab_menu);
        rvPermits = findViewById(R.id.rv_permits);
        tvDateFilter = findViewById(R.id.tv_date_filter);
        srlPermit = findViewById(R.id.srl_permit);
        tvNoItem = findViewById(R.id.tv_no_item);
    }

    private void initAdapter() {
        mAdapter = new PermitAdapter(permits, this);
        rvPermits.setLayoutManager(new LinearLayoutManager(this));
        rvPermits.setAdapter(mAdapter);
    }

    private void initListener() {
        srlPermit.setOnRefreshListener(this);
        eFabMenu.setOnClickListener(this);

        tvDateFilter.setOnClickListener(this);
    }


    private void setData() {
        tvTitle.setText(mCity.getName());
        if (mFromDate == null || mToDate == null) {
            mToDate = Calendar.getInstance();
            mFromDate = Calendar.getInstance();
            mFromDate.set(Calendar.DAY_OF_MONTH, 1);

            tvDateFilter.setText(String.format("From %s  <-->  %s", DateHelper.formatDate(mFromDate.getTime()), DateHelper.formatDate(mToDate.getTime())));
        }

        switch (resultType) {
            default:
            case ACTIVE_REQ:
                fetchActiveRequest();
                break;
            case EXPIRED_REQ:
                fetchExpiredRequest();
                break;
            case REJECTED_REQ:
                fetchRejectedRequest();
                break;
            case EXPIRED_PERMIT:
                fetchExpiredPermit();
                break;
        }
    }


    @NotNull
    private Callback<List<EPass>> permitObserver() {

        return new Callback<List<EPass>>() {
            @Override
            public void onResponse(Call<List<EPass>> call, Response<List<EPass>> response) {
                if (srlPermit.isRefreshing()) srlPermit.setRefreshing(false);
                if (response.isSuccessful()) {
                    permits.clear();
                    permits.addAll(response.body());

                    if (permits.size() > 0) {
                        tvNoItem.setVisibility(View.GONE);
                        rvPermits.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        tvNoItem.setVisibility(View.VISIBLE);
                        rvPermits.setVisibility(View.GONE);
                    }
                } else {
                    tvNoItem.setVisibility(View.VISIBLE);
                    rvPermits.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<EPass>> call, Throwable t) {
                if (srlPermit.isRefreshing()) srlPermit.setRefreshing(false);
                tvNoItem.setVisibility(View.VISIBLE);
                rvPermits.setVisibility(View.GONE);
                Toast.makeText(PermitListActivity.this, "Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void fetchExpiredRequest() {
        if (mCity == null) {
            return;
        }
        if (!srlPermit.isRefreshing()) srlPermit.setRefreshing(true);
        ApiManager.PASS.fetchExpiredRequestByCity(
                mCity.getId(),
                DateHelper.formatDateOnly(mFromDate.getTime()),
                DateHelper.formatDateOnly(mToDate.getTime())
        ).enqueue(permitObserver());
    }

    private void fetchActiveRequest() {
        if (mCity == null) {
            return;
        }
        if (!srlPermit.isRefreshing()) srlPermit.setRefreshing(true);
        ApiManager.PASS.fetchActiveRequestByCity(
                mCity.getId()
        ).enqueue(permitObserver());
    }

    private void fetchRejectedRequest() {
        if (mCity == null) {
            return;
        }

        if (!srlPermit.isRefreshing()) srlPermit.setRefreshing(true);

        ApiManager.PASS.fetchRejectedRequestByCity(
                mCity.getId(),
                DateHelper.formatDateOnly(mFromDate.getTime()),
                DateHelper.formatDateOnly(mToDate.getTime())
        ).enqueue(permitObserver());
    }

    private void fetchExpiredPermit() {
        if (mCity == null) {
            return;
        }
        if (!srlPermit.isRefreshing()) srlPermit.setRefreshing(true);
        ApiManager.PASS.fetchExpiredPermitByCity(
                mCity.getId(),
                DateHelper.formatDateOnly(mFromDate.getTime()),
                DateHelper.formatDateOnly(mToDate.getTime())
        ).enqueue(permitObserver());
    }


    private void showFilterOptionDialog() {
        new FilterDialog(this, new FilterDialog.Listener() {
            @Override
            public void filter(Calendar from, Calendar to) {
                mFromDate = from;
                mToDate = to;
                tvDateFilter.setText(String.format("From %s  <-->  %s", DateHelper.formatDate(mFromDate.getTime()), DateHelper.formatDate(mToDate.getTime())));
                setData();
            }
        }).show();
    }

    private void showOptionMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(PermitListActivity.this, eFabMenu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.config_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.item_active_req) {
                    resultType = ResultType.ACTIVE_REQ;
                    eFabMenu.setText("ACTIVE PERMITS");
                    setData();
                } else if (item.getItemId() == R.id.item_expired_req) {
                    resultType = ResultType.EXPIRED_REQ;
                    eFabMenu.setText("EXPIRED REQUESTS");
                    setData();
                } else if (item.getItemId() == R.id.item_rejected_req) {
                    resultType = ResultType.REJECTED_REQ;
                    eFabMenu.setText("REJECTED REQUESTS");
                    setData();
                } else if (item.getItemId() == R.id.item_expired_permit) {
                    resultType = ResultType.EXPIRED_PERMIT;
                    eFabMenu.setText("EXPIRED PERMITS");
                    setData();
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    @Override
    public void onRefresh() {
        setData();
    }

    @Override
    public void onPermitSelected(EPass permit, int pos, View view) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.efab_menu) {
            showOptionMenu();
        } else if (v.getId() == R.id.tv_date_filter) {
            showFilterOptionDialog();
        }
    }
}
