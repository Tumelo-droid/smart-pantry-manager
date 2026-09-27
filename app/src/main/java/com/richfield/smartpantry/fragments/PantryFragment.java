package com.richfield.smartpantry.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.richfield.smartpantry.AddEditIngredientActivity;
import com.richfield.smartpantry.R;
import com.richfield.smartpantry.adapter.PantryAdapter;
import com.richfield.smartpantry.database.DatabaseHelper;
import com.richfield.smartpantry.database.PantryItem;

import java.util.List;

public class PantryFragment extends Fragment implements PantryAdapter.OnPantryItemClickListener {

    private static final int REQUEST_ADD_EDIT = 1;

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private PantryAdapter adapter;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        recyclerView = view.findViewById(R.id.recyclerPantry);
        tvEmpty = view.findViewById(R.id.tvEmptyPantry);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddIngredient);

        dbHelper = new DatabaseHelper(requireContext());
        adapter = new PantryAdapter();
        adapter.setOnPantryItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
            startActivityForResult(intent, REQUEST_ADD_EDIT);
        });

        loadPantryItems();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        adapter.setItems(items);
        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEditClick(PantryItem item) {
        Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
        intent.putExtra("pantry_id", item.getId());
        startActivityForResult(intent, REQUEST_ADD_EDIT);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        dbHelper.deletePantryItem(item.getId());
        loadPantryItems();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EDIT) {
            loadPantryItems();
        }
    }
}
