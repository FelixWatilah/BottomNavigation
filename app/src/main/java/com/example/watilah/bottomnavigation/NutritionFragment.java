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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class NutritionFragment extends Fragment {

    View view;
    private List<Nutrition> nutritionArrayList;
    private RecyclerView recyclerView;

    public NutritionFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_nutrition);

        RecyclerViewAdapterNutrition viewAdapter = new RecyclerViewAdapterNutrition(getContext(), nutritionArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(viewAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nutritionArrayList = new ArrayList<>();

        nutritionArrayList.add(new Nutrition("Vitamin A"));
        nutritionArrayList.add(new Nutrition("Full of calories"));
        nutritionArrayList.add(new Nutrition("Energy"));
        nutritionArrayList.add(new Nutrition("Enrich vision"));
        nutritionArrayList.add(new Nutrition("Soft Skin"));
        nutritionArrayList.add(new Nutrition("Heal pimples"));
        nutritionArrayList.add(new Nutrition("Vitamin A"));

    }
}
