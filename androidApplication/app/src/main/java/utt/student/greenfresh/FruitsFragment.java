package utt.student.greenfresh;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FruitsFragment extends Fragment {

    // Constructor
    public FruitsFragment() {
        // Required empty public constructor
    }

    // Methods
    public static FruitsFragment newInstance(String param1, String param2) {
        FruitsFragment fragment = new FruitsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fruits, container, false);
    }

}