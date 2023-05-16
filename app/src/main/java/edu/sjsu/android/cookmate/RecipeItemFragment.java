package edu.sjsu.android.cookmate;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;
import edu.sjsu.android.cookmate.helpers.NetworkTask;
import edu.sjsu.android.cookmate.model.RecipeItem;

public class RecipeItemFragment extends Fragment {
    String query;
    int totalResult;
    int offset;
    boolean isRandom;
    final ArrayList<RecipeItem> recipeItems = new ArrayList<>();
    RecyclerView recyclerView;

    public RecipeItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RecipeItemAdapter(recipeItems));
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isRandom) return;
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();
                    if (lastVisibleItemPosition == totalItemCount - 4) {
                        // End of list is reached
                        searchRecipes(query, offset, context);
                    }
                }
            });
            getRandomRecipes(context);
        }
        return view;
    }

    public void searchRecipes(String query, int offset, Context context) {
        removeNoSearchResults();
        removeNetworkErrorLayout();
        isRandom = false;
        this.query = query;
        if (offset == 0) recipeItems.clear();
        this.offset = offset;
        String apiKey = BuildConfig.SPOONACULAR_API;
        String urlString = "https://api.spoonacular.com/recipes/complexSearch?apiKey=" + apiKey + "&query=" + query + "&number=20&offset=" + offset;
        new NetworkTask(recipes -> {
            try {
                JSONObject responseObject = new JSONObject(recipes);
                int totalResults = responseObject.getInt("totalResults");
                this.totalResult = totalResults;
                if (totalResults > 0) {
                    JSONArray responseArray = responseObject.getJSONArray("results");
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject item = responseArray.getJSONObject(i);
                        recipeItems.add(new RecipeItem(item.getLong("id"),
                                item.getString("title"),
                                item.getString("image"),
                                item.getString("imageType")));
                    }
                    // Update the RecyclerView with the new recipe items
                    recyclerView.getAdapter().notifyDataSetChanged();
                    this.offset += 20;
                } else {
                    Fragment mainFragment = getParentFragment();
                    RelativeLayout parentLayout = mainFragment.getView().findViewById(R.id.no_results);
                    parentLayout.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                System.out.println(e);
                Fragment mainFragment = getParentFragment();
                RelativeLayout parentLayout = mainFragment.getView().findViewById(R.id.network_error);
                parentLayout.setVisibility(View.VISIBLE);
            }
            removeShimmers();
        }, context).execute(urlString);
    }

    private void removeNetworkErrorLayout() {
        Fragment mainFragment = getParentFragment();
        RelativeLayout parentLayout = mainFragment.getView().findViewById(R.id.network_error);
        parentLayout.setVisibility(View.GONE);
    }

    private void removeNoSearchResults() {
        Fragment mainFragment = getParentFragment();
        RelativeLayout parentLayout = mainFragment.getView().findViewById(R.id.no_results);
        parentLayout.setVisibility(View.GONE);
    }


    public void getRandomRecipes(Context context) {
        removeNetworkErrorLayout();
        isRandom = true;
        String apiKey = BuildConfig.SPOONACULAR_API;
        String urlString = "https://api.spoonacular.com/recipes/random?apiKey=" + apiKey + "&number=20";
        new NetworkTask(recipes -> {
            try {
                JSONObject responseObject = new JSONObject(recipes);
                JSONArray responseArray = responseObject.getJSONArray("recipes");
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject item = responseArray.getJSONObject(i);
                    recipeItems.add(new RecipeItem(item.getLong("id"),
                            item.getString("title"),
                            item.getString("image"),
                            item.getString("imageType")));
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(i);
                }
                removeShimmers();
            } catch (JSONException e) {
                System.out.println(e);
                Fragment mainFragment = getParentFragment();
                RelativeLayout parentLayout = mainFragment.getView().findViewById(R.id.network_error);
                parentLayout.setVisibility(View.VISIBLE);
            }
        }, context).execute(urlString);
    }

    private void removeShimmers() {
        Fragment mainFragment = getParentFragment();
        ShimmerFrameLayout parentLayout = mainFragment.getView().findViewById(R.id.shimmer_view_container);
        LinearLayout shimmerFrameLayout = mainFragment.getView().findViewWithTag("Shimmer Holder");
        parentLayout.removeView(shimmerFrameLayout);
    }

}