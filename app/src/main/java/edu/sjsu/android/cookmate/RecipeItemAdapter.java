package edu.sjsu.android.cookmate;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import java.util.List;

import edu.sjsu.android.cookmate.databinding.FragmentItemBinding;
import edu.sjsu.android.cookmate.model.RecipeItem;

// My adapter class that binds to each row in the list of rows.
public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.ViewHolder> {

    private final List<RecipeItem> mValues;

    public RecipeItemAdapter(List<RecipeItem> items) {
        mValues = items;
    }

    // This method returns a view holder for a row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    // Retrieves each Recipe from the List of Recipes & update for each row view
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        RecipeItem recipeItem = mValues.get(position);
        // Set the image as the launcher icon of Android
        Picasso.get().load(recipeItem.getImage()).into(holder.binding.icon);
        // Get the current data from the arraylist based on the position
        holder.binding.content.setText(recipeItem.getTitle());
    }

    // Returns the total number of recipes
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // Creates references the entire list of recipes, so they one can edit it using onBindViewHolder method
    // Improves performance of RecyclerView as the entire list is cached.
    // In a nutshell, it just binds the holder to the entire list, so we do holder.ui_element
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final FragmentItemBinding binding;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                // Launch Detail Screen for corresponding item
                Bundle bundle = new Bundle();
                int position = getBindingAdapterPosition();
                long recipeId = mValues.get(position).getId();
                String title = mValues.get(position).getTitle();
                String image = mValues.get(position).getImage();
                bundle.putSerializable("recipeId", recipeId);
                bundle.putSerializable("title", title);
                bundle.putSerializable("image", image);
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_detailScreen, bundle);
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}