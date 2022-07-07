package com.genexus.android.payments.hms;

public class ProductDetail {
    private final String mId;
    private final String mTitle;
    private final String mDesc;
    private final String mPrice;
    private final boolean mPurchased;

    public ProductDetail(String id, String title, String desc, String price, boolean purchased) {
        mId = id;
        mTitle = title;
        mDesc = desc;
        mPrice = price;
        mPurchased = purchased;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getPrice() {
        return mPrice;
    }

    public boolean isPurchased() {
        return mPurchased;
    }
}
