package edu.sjsu.android.cookmate;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MainFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        createShimmers(view);
        SearchView searchView = view.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    RecipeItemFragment recipeItemFragment = (RecipeItemFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
                    if (recipeItemFragment != null) {
                        createShimmers(view);
                        recipeItemFragment.searchRecipes(query, 0, getContext());
                        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This method will be called whenever the user changes the text in the search view
                return false;
            }
        });
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());

        return view;
    }

    public void createShimmers(View mainView) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setTag("Shimmer Holder");
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        for (int i = 0; i < 10; i++) {
            View itemShimmer = inflater.inflate(R.layout.item_shimmer, null);
            linearLayout.addView(itemShimmer);
        }
        ShimmerFrameLayout shimmerFrameLayout = mainView.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.addView(linearLayout);
        shimmerFrameLayout.startShimmer();
    }

}