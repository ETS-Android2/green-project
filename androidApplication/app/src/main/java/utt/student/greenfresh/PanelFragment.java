package utt.student.greenfresh;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PanelFragment extends Fragment {

    // Constructor
    public PanelFragment() {
        // Required empty public constructor
    }

    // Methods
    public static PanelFragment newInstance(String param1, String param2) {
        PanelFragment fragment = new PanelFragment();
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
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }
}