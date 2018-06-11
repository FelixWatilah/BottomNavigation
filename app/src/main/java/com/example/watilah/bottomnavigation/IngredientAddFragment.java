package com.example.watilah.bottomnavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class IngredientAddFragment extends Fragment {

    View view;
    private List<IngredientAdd> ingredientAddList;
    private RecyclerView recyclerView;

    public IngredientAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ingredient_add, container, false);

        recyclerView = view.findViewById(R.id.rv_ingredient_add);

        RecyclerViewAdapterIngredientAdd viewAdapter = new RecyclerViewAdapterIngredientAdd(getContext(), ingredientAddList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(viewAdapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ingredientAddList = new ArrayList<>();

        ingredientAddList.add(new IngredientAdd("Boil water for 30 mins"));
        ingredientAddList.add(new IngredientAdd("Chop the onions"));
        ingredientAddList.add(new IngredientAdd("Put one spoonful of cooking fat on the cooking pot."));
        ingredientAddList.add(new IngredientAdd("Put the chopped onions into the melted cooking fat."));
        ingredientAddList.add(new IngredientAdd("Add chopped tomatoes."));
        ingredientAddList.add(new IngredientAdd("Add Nyama"));

    }

}