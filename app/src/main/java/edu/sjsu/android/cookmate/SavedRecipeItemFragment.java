package edu.sjsu.android.cookmate;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import edu.sjsu.android.cookmate.model.RecipeItem;
import edu.sjsu.android.cookmate.sql.DatabaseHelper;

public class SavedRecipeItemFragment extends Fragment {
    final ArrayList<RecipeItem> recipeItems = new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    int userId;

    public SavedRecipeItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userId = Integer.parseInt(sharedPreferences.getString("user_id", null));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_recipe_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RecipeItemAdapter(recipeItems));
            getSavedRecipes();
            addSlideItemHandler(recyclerView);
        }
        return view;
    }

    private void addSlideItemHandler(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                RecipeItem deleted = recipeItems.remove(position);
                databaseHelper.deleteRecipe(deleted.getId(), userId);
                recyclerView.getAdapter().notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouch = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouch.attachToRecyclerView(recyclerView);
    }

    private void getSavedRecipes() {
        recipeItems.clear();
        Cursor cursor = databaseHelper.getAllSavedRecipes(userId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int recipeId;
                String recipeTitle, recipeImage;
                int recipeIdIndex = cursor.getColumnIndex("recipe_id");
                if (recipeIdIndex >= 0) {
                    recipeId = cursor.getInt(recipeIdIndex);
                } else continue;
                int recipeTitleIndex = cursor.getColumnIndex("recipe_title");
                if (recipeTitleIndex >= 0) {
                    recipeTitle = cursor.getString(recipeTitleIndex);
                } else continue;
                int recipeImageIndex = cursor.getColumnIndex("recipe_image");
                if (recipeImageIndex >= 0) {
                    recipeImage = cursor.getString(recipeImageIndex);
                } else continue;
                recipeItems.add(new RecipeItem(recipeId,
                        recipeTitle, recipeImage, null));
            } while (cursor.moveToNext());
            cursor.close();
        }
        recyclerView.getAdapter().notifyDataSetChanged();
        removeShimmers();
    }

    private void removeShimmers() {
        Fragment mainFragment = getParentFragment();
        ShimmerFrameLayout parentLayout = mainFragment.getView().findViewById(R.id.shimmer_view_container);
        LinearLayout shimmerFrameLayout = mainFragment.getView().findViewWithTag("Shimmer Holder");
        parentLayout.removeView(shimmerFrameLayout);
    }

}