package com.fithou.ecovn.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fithou.ecovn.MainActivity;
import com.fithou.ecovn.R;
import com.fithou.ecovn.Sensor.ShakeDetector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationMenu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ShakeDetector shakeDetector;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationMenu newInstance(String param1, String param2) {
        NotificationMenu fragment = new NotificationMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification_menu, container, false);

        shakeDetector = new ShakeDetector(getContext());
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                // Xử lý khi xảy ra sự kiện lắc màn hình
                // Chuyển về màn hình Home
                // Ví dụ:

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });

        return rootView;

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