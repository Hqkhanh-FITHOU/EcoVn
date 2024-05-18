package com.fithou.ecovn.view.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fithou.ecovn.view.MainActivity;
import com.fithou.ecovn.R;
import com.fithou.ecovn.Sensor.ShakeDetector;


public class NotificationMenuFragment extends Fragment {
    private ShakeDetector shakeDetector;

    public static NotificationMenuFragment newInstance(String param1, String param2) {
        NotificationMenuFragment fragment = new NotificationMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification_menu, container, false);



        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shakeDetector = new ShakeDetector(view.getContext());
        shakeDetector.setOnShakeListener((x, y, z) -> {
            if (x > 2f || x < -2f || y > 12f || y < -12f || z > 2f || z < -2f) {
                //Toast.makeText(this.getActivity(), x+"", Toast.LENGTH_SHORT).show();
                MainActivity activity = (MainActivity) getActivity();
                if(activity != null){
                    activity.setBottomNavigationSelectedItem(0);
                }
            }
        });
    }

    //sensor--------------
    @Override
    public void onPause() {
        super.onPause();
        shakeDetector.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        shakeDetector.start();
    }
    // ---------------------------------
}