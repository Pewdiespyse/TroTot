package com.team10.trotot.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.team10.trotot.R;
import com.team10.trotot.view.supports.RangeSeekBar;

public class FindSettingActivity extends AppCompatActivity {

    RangeSeekBar sbGiaPhong, sbDienTich;
    SeekBar sbBanKinh;
    TextView tvGiaPhongMin, tvDienTichMin, tvBanKinh, tvGiaPhongMax, tvDienTichMax;
    int minGiaPhong, maxGiaPhong, minDienTich, maxDienTich, BanKinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_find_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sbGiaPhong = (RangeSeekBar) findViewById(R.id.sbGiaphong_Find);
        sbBanKinh = (SeekBar) findViewById(R.id.sbBanKinh_Find);
        sbDienTich = (RangeSeekBar) findViewById(R.id.sbDienTich_Find);
        tvGiaPhongMin = (TextView) findViewById(R.id.tvGiaPhongMin_Find);
        tvBanKinh = (TextView) findViewById(R.id.tvBanKinh_Find);
        tvDienTichMin = (TextView) findViewById(R.id.tvDienTichMin_Find);
        tvGiaPhongMax = (TextView) findViewById(R.id.tvGiaPhongMax_Find);
        tvDienTichMax = (TextView) findViewById(R.id.tvDienTichMax_Find);
        tvDienTichMin.setText(Html.fromHtml("0 m<sup>2</sup>"));
        tvDienTichMax.setText(Html.fromHtml("500 m<sup>2</sup>"));

        sbGiaPhong.setRangeValues(0,10);
        sbGiaPhong.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                tvGiaPhongMin.setText(minValue + " triệu");
                tvGiaPhongMax.setText(maxValue + " triệu");
                minGiaPhong = (int)minValue;
                maxGiaPhong = (int)maxValue;
            }
        });

        sbDienTich.setRangeValues(0,500);
        sbDienTich.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                tvDienTichMin.setText(Html.fromHtml(minValue + " m<sup>2</sup>"));
                tvDienTichMax.setText(Html.fromHtml(maxValue + " m<sup>2</sup>"));
                minDienTich = (int)minValue;
                maxDienTich = (int)maxValue;
            }
        });

        sbBanKinh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvBanKinh.setText(i + " m");
                BanKinh = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_find_setting, menu);
        //showCurrentPlace();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(R.id.menu_find_setting_apply == id)
        {
            Intent i = new Intent(FindSettingActivity.this, MainActivity.class);

            Bundle bundle = new Bundle();;
            bundle.putInt("minGiaPhong_Find", minGiaPhong);
            bundle.putInt("maxGiaPhong_Find", maxGiaPhong);
            bundle.putInt("minDienTich_Find", minDienTich);
            bundle.putInt("maxDienTich_Find", maxDienTich);
            bundle.putInt("BanKinh_Find", BanKinh);

            i.putExtra("Bundle_Find", bundle);

            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
