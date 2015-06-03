package project.coen268.scu.dogplaydate;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lu Yu
 */
public class MyDogsFragment extends Fragment {

    public MyDogsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mydogs, container, false);

        return rootView;
    }
}

