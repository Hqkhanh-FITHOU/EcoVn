package com.fithou.ecovn.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.fithou.ecovn.model.product.Comment;
import com.fithou.ecovn.model.product.ProductsModel;

import java.io.Serializable;
import java.util.List;

public class ExtendProductModel extends ProductsModel implements Serializable, Parcelable {
    private String quantity_order;

    private boolean isChecked;

    public ExtendProductModel() {
    }

    public ExtendProductModel(String quantity, boolean isChecked) {
        this.quantity_order = quantity;
        this.isChecked = isChecked;
    }

    public ExtendProductModel(String product_id, String name, String des, Double cost, String img, String FK_category_id, String FK_shop_id, int quantity, String unit, String container_type, List<Comment> commentList, String quantity1, boolean isChecked) {
        super(product_id, name, des, cost, img, FK_category_id, FK_shop_id, quantity, unit, container_type, commentList);
        this.quantity_order = quantity1;
        this.isChecked = isChecked;
    }

    public ExtendProductModel(Parcel in) {
        super(in);
        quantity_order = in.readString();
        isChecked = in.readBoolean();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(quantity_order);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExtendProductModel> CREATOR = new Creator<ExtendProductModel>() {
        @Override
        public ExtendProductModel createFromParcel(Parcel in) {
            return new ExtendProductModel(in);
        }

        @Override
        public ExtendProductModel[] newArray(int size) {
            return new ExtendProductModel[size];
        }
    };

    public String getQuantity_order() {
        return quantity_order;
    }

    public void setQuantity_order(String quantity_order) {
        this.quantity_order = quantity_order;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public interface OnMinusQuantityClickListener {
        void onMinusQuantityClick(ExtendProductModel extendProductModel);
    }

    public interface OnPlusQuantityClickListener {
        void onPlusQuantityClick(ExtendProductModel extendProductModel);
    }
}
