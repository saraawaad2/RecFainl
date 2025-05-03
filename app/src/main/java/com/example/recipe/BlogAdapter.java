package com.example.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private List<BlogModel> blogList;

    public BlogAdapter(List<BlogModel> blogList) {
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_entry, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        BlogModel model = blogList.get(position);
        holder.recipeName.setText("Recipe Name: " + model.recipeName);
        holder.duration.setText("Duration: " + model.duration + " mins");
        holder.description.setText("Description: " + model.description);
        Picasso.get().load(model.imageUrl).into(holder.image); // converting image from url to image view
    } // takes the data of every rec in the blog list so then it can be projected in the recycler view

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, duration, description;
        ImageView image;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.tv_recipe_name);
            duration = itemView.findViewById(R.id.tv_duration);
            description = itemView.findViewById(R.id.tv_description);
            image = itemView.findViewById(R.id.image_recipe);
        }
    }
}
