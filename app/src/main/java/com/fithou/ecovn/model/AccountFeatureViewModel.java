package com.fithou.ecovn.model;

public class AccountFeatureViewModel {
    private int feature_id;
    private int feature_icon;
    private String feature_name;

    public AccountFeatureViewModel(int feature_id, int feature_icon, String feature_name) {
        this.feature_id = feature_id;
        this.feature_icon = feature_icon;
        this.feature_name = feature_name;
    }

    public AccountFeatureViewModel() {
    }

    public int getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(int feature_id) {
        this.feature_id = feature_id;
    }

    public int getFeature_icon() {
        return feature_icon;
    }

    public void setFeature_icon(int feature_icon) {
        this.feature_icon = feature_icon;
    }

    public String getFeature_name() {
        return feature_name;
    }

    public void setFeature_name(String feature_name) {
        this.feature_name = feature_name;
    }
}
