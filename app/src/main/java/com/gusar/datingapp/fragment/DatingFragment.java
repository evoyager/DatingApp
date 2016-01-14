package com.gusar.datingapp.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

import com.gusar.datingapp.HomeActivity;
import com.gusar.datingapp.adapter.DatingAdapter;

/**
 * Created by evgeniy on 14.01.16.
 */
public class DatingFragment extends Fragment {
    public HomeActivity activity;
    protected DatingAdapter adapter;
    protected LayoutManager layoutManager;
    protected RecyclerView recyclerView;
}
