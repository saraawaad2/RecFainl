package com.example.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final List<RecipeModel> recipeList;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(RecipeModel model, String documentId);
    }

    public RecipeAdapter(Context context, List<RecipeModel> recipeList, OnItemClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        RecipeModel model = recipeList.get(position);
        String recipeId = model.getDocumentId(); // You'll need to set this manually when fetching
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onLongClick(model, model.getDocumentId());
            }
            return true;
        });

        holder.title.setText(model.getName());
        holder.ingredients.setText("Ingredients: " + model.getIngredients());

        Glide.with(context)
                .load(model.getImage())
                .error(R.drawable.img_9) // fallback if load fails
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(model, recipeId));

        holder.like.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("Liked")
                    .document(uid)
                    .collection("Recipes")
                    .document(recipeId)
                    .set(model);
            Toast.makeText(context, "this Recipe is saved", Toast.LENGTH_SHORT).show();
        });
    }
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onLongClick(RecipeModel model, String recipeId);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, like;
        TextView title, ingredients;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recipe_image);
            like = itemView.findViewById(R.id.like_button);
            title = itemView.findViewById(R.id.recipe_title);
            ingredients = itemView.findViewById(R.id.recipe_ingredients);
        }
    }
}
