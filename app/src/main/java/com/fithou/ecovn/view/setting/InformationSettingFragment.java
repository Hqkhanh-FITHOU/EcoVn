package com.fithou.ecovn.view.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.view.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class InformationSettingFragment extends Fragment {
    CircleImageView setting_info_avatar;
    TextView setting_info_email, setting_info_dateofbirth, setting_info_gender, setting_info_phone, setting_info_address;

    Button btn_edit_info;
    public InformationSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewComponents(View view) {
        setting_info_avatar = view.findViewById(R.id.setting_info_avatar);
        setting_info_email = view.findViewById(R.id.setting_info_email);
        setting_info_dateofbirth = view.findViewById(R.id.setting_info_dateofbirth);
        setting_info_gender = view.findViewById(R.id.setting_info_gender);
        setting_info_phone = view.findViewById(R.id.setting_info_phone);
        setting_info_address = view.findViewById(R.id.setting_info_address);
        btn_edit_info = view.findViewById(R.id.btn_edit_info);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_setting, container, false);
        initViewComponents(view);
        loadViewData();

        onClickToEdit();
        return view;
    }

    private void onClickToEdit() {
        btn_edit_info.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditAccountActivity.class));
        });
    }

    private void loadViewData() {
        if(MainActivity.CURRENT_USER != null){
            Glide.with(this).load(MainActivity.CURRENT_USER.getImage()).placeholder(R.drawable.logo).into(setting_info_avatar);
            setting_info_email.setText(MainActivity.CURRENT_USER.getEmail());
            String gender = MainActivity.CURRENT_USER.isGender() ? "Nam":"Nữ";
            setting_info_gender.setText(gender);

            if(!MainActivity.CURRENT_USER.getDate_of_birth().isEmpty()){
                SimpleDateFormat formatStringToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat formatDateToString = new SimpleDateFormat("dd/MM/yyyy");
                Date dateTime;
                try {
                    dateTime = formatStringToDate.parse(MainActivity.CURRENT_USER.getDate_of_birth());
                    setting_info_dateofbirth.setText(formatDateToString.format(dateTime));
                } catch (ParseException e) {
                    Log.e("datetime parse error", e.toString());
                }
            }else {
                setting_info_dateofbirth.setText("Chưa có thông tin");
            }

            if(MainActivity.CURRENT_USER.getPhone().isEmpty()){
                setting_info_phone.setText("Chưa có thông tin");
            }else{
                setting_info_phone.setText(MainActivity.CURRENT_USER.getPhone());
            }

            if(MainActivity.CURRENT_USER.getAddress().isEmpty()){
                setting_info_address.setText("Chưa có thông tin");
            }else{
                setting_info_address.setText(MainActivity.CURRENT_USER.getAddress());
            }

        }
    }
}