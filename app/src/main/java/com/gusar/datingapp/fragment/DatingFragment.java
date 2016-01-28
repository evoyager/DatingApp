package com.gusar.datingapp.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.gusar.datingapp.DatingActivity;
import com.gusar.datingapp.adapter.DatingAdapter;

/**
 * Created by igusar on 1/28/16.
 */
public class DatingFragment extends Fragment {
    public DatingActivity activity;
    protected DatingAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected RecyclerView recyclerView;
}
