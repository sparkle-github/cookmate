package edu.sjsu.android.cookmate;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.sjsu.android.cookmate.databinding.FragmentInformationScreenBinding;

public class InformationScreen extends Fragment {
    private FragmentInformationScreenBinding binding;
    public InformationScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInformationScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}