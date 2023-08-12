package com.project.studiopatyzinha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class NotiWeekFragment extends Fragment {

    public static NotiWeekFragment newInstance() {
        return new NotiWeekFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notiweek, container, false);







        return view;
    }
}