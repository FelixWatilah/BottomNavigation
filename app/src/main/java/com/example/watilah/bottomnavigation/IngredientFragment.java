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

import java.util.ArrayList;
import java.util.List;

public class IngredientFragment extends Fragment {

    View view;
    private List<Ingredient> ingredientArrayList;
    private RecyclerView recyclerView;

    public IngredientFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_ingredient);

        RecyclerViewAdapterIngredient viewAdapter = new RecyclerViewAdapterIngredient(getContext(), ingredientArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(viewAdapter);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ingredientArrayList = new ArrayList<>();

        ingredientArrayList.add(new Ingredient(R.drawable.ic_add_black_24dp, "Add", "750g"));
        ingredientArrayList.add(new Ingredient(R.drawable.ic_apps_black_24dp, "Main Course", "74g"));
        ingredientArrayList.add(new Ingredient(R.drawable.ic_wifi_black_24dp, "Main Course", "300g"));
        ingredientArrayList.add(new Ingredient(R.drawable.ic_attach_file_black_24dp, "Main Course", "250g"));
        ingredientArrayList.add(new Ingredient(R.drawable.ic_person_black_24dp, "Main Course", "200g"));
        ingredientArrayList.add(new Ingredient(R.drawable.ic_share_black_24dp, "Kenya", "150g"));
        ingredientArrayList.add(new Ingredient(R.drawable.ic_add_black_24dp, "Add", "750g"));
    }
}
