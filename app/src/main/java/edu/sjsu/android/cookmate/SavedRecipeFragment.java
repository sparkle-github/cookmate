package edu.sjsu.android.cookmate;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.facebook.shimmer.ShimmerFrameLayout;

public class SavedRecipeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_recipe, container, false);
        createShimmers(view);

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