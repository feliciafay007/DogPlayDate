package project.coen268.scu.dogplaydate;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Lydia on 6/2/15.
 */
public class LogoutFragment extends Fragment {

    TextView logoutLabel;
    public LogoutFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
//        logoutLabel = (TextView) rootView.findViewById(R.id.logoutLabel);
//        logoutLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in=new Intent(getActivity(),Login.class);
//                System.out.println("back to login");
//                startActivity(in);
//            }
//        });

        Intent in = new Intent(getActivity(),Login.class);
        System.out.println("back to login");
        startActivity(in);
        return rootView;
    }

}

