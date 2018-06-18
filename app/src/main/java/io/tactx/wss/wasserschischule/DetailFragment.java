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
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "title";

    // Store instance variables
    private String mTitle;
    private int mPage;


    private TextView[] mTemp = new TextView[4];

    public void setValues(String t0, String t1, String t2, String t3){
        if(this.mTemp[0] != null) {
            this.mTemp[0].setText(t0);
            this.mTemp[1].setText(t1);
            this.mTemp[2].setText(t2);
            this.mTemp[3].setText(t3);
        }
    }


    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(int page, String title) {
        DetailFragment fragment = new DetailFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_detail, container, false);

            this.mTemp[0] = (TextView) v.findViewById(R.id.watertemp0);
            this.mTemp[1] = (TextView) v.findViewById(R.id.watertemp1);
            this.mTemp[2] = (TextView) v.findViewById(R.id.watertemp2);
            this.mTemp[3] = (TextView) v.findViewById(R.id.watertemp3);

        return v;
    }



}
