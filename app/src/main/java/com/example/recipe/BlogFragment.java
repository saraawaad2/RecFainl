package com.example.recipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlogFragment extends Fragment {

    private RecyclerView blogRecycler;
    private FirebaseFirestore db;
    private List<Map<String, Object>> blogList = new ArrayList<>();

    public BlogFragment() {
        super(R.layout.fragment_blog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        blogRecycler = view.findViewById(R.id.blog_recycler);
        blogRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        view.findViewById(R.id.back_button).setOnClickListener(v -> requireActivity().onBackPressed());

        db = FirebaseFirestore.getInstance();
        db.collection("diary_entries")
                .get()
                .addOnSuccessListener(result -> {
                    List<BlogModel> blogs = new ArrayList<>();
                    for (DocumentSnapshot doc : result) {
                        blogs.add(new BlogModel(
                                doc.getString("recipeName"),
                                doc.getString("description"),
                                doc.getLong("duration").intValue(),
                                doc.getString("imageUrl")));
                    }
                    blogRecycler.setAdapter(new BlogAdapter(blogs));
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        View decorView = requireActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
}
