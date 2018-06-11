package com.example.watilah.bottomnavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DirectionFragment extends Fragment {

    View view;
    private List<Direction> directionArrayList;
    private RecyclerView recyclerView;

    public DirectionFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_direction, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_direction);

        RecyclerViewAdapterDirection viewAdapter = new RecyclerViewAdapterDirection(getContext(), directionArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(viewAdapter);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        directionArrayList = new ArrayList<>();

        directionArrayList.add(new Direction("Boil water for 30 mins"));
        directionArrayList.add(new Direction("Chop the onions"));
        directionArrayList.add(new Direction("Put one spoonful of cooking fat on the cooking pot."));
        directionArrayList.add(new Direction("Put the chopped onions into the melted cooking fat."));
        directionArrayList.add(new Direction("Add chopped tomatoes."));
        directionArrayList.add(new Direction("Add Nyama"));

    }

}
