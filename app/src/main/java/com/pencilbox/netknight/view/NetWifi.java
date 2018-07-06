package com.pencilbox.netknight.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.pencilbox.netknight.R;
import com.pencilbox.netknight.presentor.DairyImpl;
import com.pencilbox.netknight.presentor.IDairyPresenter;

public class NetWifi extends Fragment implements IDairyView{
    private LineChart wifilineChart;

    private IDairyPresenter mIDairyPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.net_wifi, container, false);
        wifilineChart = (LineChart) view.findViewById(R.id.wifi_chart);
        mIDairyPresenter = new DairyImpl(this);
        mIDairyPresenter.showUseOfWifi(wifilineChart);
        return view;
    }

    @Override
    public void onDatachartListRefresh() {

    }
}
