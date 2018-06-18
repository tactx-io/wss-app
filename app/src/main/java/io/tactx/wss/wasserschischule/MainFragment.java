package io.tactx.wss.wasserschischule;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "title";

    // Store instance variables
    private String mTitle;
    private int mPage;

    private TextView mAirTemp;
    private TextView mWaterTemp;

    private String mAirTempValue;
    private String mWaterTempValue;

    public void setValues(String air, String water){
        mAirTempValue = air;
        mWaterTempValue = water;
        System.out.println("air " + air + " --- water " + water);
        if(mAirTemp != null)
            mAirTemp.setText(air + "°C");
        if(mWaterTemp != null)
            mWaterTemp.setText(water + "°C");

    }



    public MainFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(int page, String title) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            mTitle = getArguments().getString(ARG_TITLE);
        }

        if(mAirTemp != null)
            mAirTemp.setText(mAirTempValue + "°C");
        if(mWaterTemp != null)
            mWaterTemp.setText(mWaterTempValue + "°C");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mAirTemp = (TextView) v.findViewById(R.id.airtemp);
        mWaterTemp = (TextView)v.findViewById(R.id.watertemp);

        return v;
    }
}
