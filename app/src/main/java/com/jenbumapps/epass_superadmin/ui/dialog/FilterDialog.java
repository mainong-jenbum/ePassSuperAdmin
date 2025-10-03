package com.jenbumapps.epass_superadmin.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jenbumapps.core.model.time.DateHelper;
import com.jenbumapps.epass_superadmin.R;

import java.util.Calendar;

import ui.mainong.pico.Pico;
import ui.mainong.pico.codec.Type;
import ui.mainong.pico.helper.PicoListener;

public class FilterDialog extends Dialog implements View.OnClickListener {

    private Button btnCancel;
    private Button btnProceed;
    private TextView tvToDate;
    private TextView tvFromDate;
    private LinearLayout llToDate;
    private LinearLayout llFromDate;


    public interface Listener {
        void filter(Calendar from, Calendar to);
    }

    private Listener listener;
    private Context context;

    private Calendar fromDate;
    private Calendar toDate;

    public FilterDialog(@NonNull Context context, Listener listener) {
        super(context);

        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_filter);

        initView();
        initListener();
    }

    private void initView() {

        Window window = getWindow();
        if (window != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setLayout(width, height);
        }

        llFromDate = findViewById(R.id.ll_from_date);
        llToDate = findViewById(R.id.ll_to_date);
        tvFromDate = findViewById(R.id.tv_from_date);
        tvToDate = findViewById(R.id.tv_to_date);

        btnProceed = findViewById(R.id.btn_proceed);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void initListener() {
        btnCancel.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        llToDate.setOnClickListener(this);
        llFromDate.setOnClickListener(this);
    }

    private void showFromDatePicker() {
        Pico pico = new Pico(context, null, Type.CALENDAR);
        pico.setPicoListener(new PicoListener() {
            @Override
            public void result(Calendar calendar) {
                fromDate = calendar;
                tvFromDate.setText(DateHelper.formatDate(calendar.getTime()));
            }
        });
        pico.show();
    }

    private void showToDatePicker() {
        Pico pico = new Pico(context, null, Type.CALENDAR);
        pico.setPicoListener(new PicoListener() {
            @Override
            public void result(Calendar calendar) {
                toDate = calendar;
                tvToDate.setText(DateHelper.formatDate(calendar.getTime()));
            }
        });
        pico.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_proceed) {

            if (fromDate == null) {
                Toast.makeText(context, "Select from date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (toDate == null) {
                Toast.makeText(context, "Select to date", Toast.LENGTH_SHORT).show();
                return;
            }

            listener.filter(fromDate, toDate);
            dismiss();

        } else if (v.getId() == R.id.btn_cancel) {
            dismiss();
        } else if (v.getId() == R.id.ll_from_date) {
            showFromDatePicker();
        } else if (v.getId() == R.id.ll_to_date) {
            showToDatePicker();
        }
    }
}
