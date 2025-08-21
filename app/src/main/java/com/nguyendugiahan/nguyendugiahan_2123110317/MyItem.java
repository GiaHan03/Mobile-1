package com.nguyendugiahan.nguyendugiahan_2123110317;

public class MyItem {
    public int image;
    private int imageRes;
    private String name;
    private String price;   // có thể để String cho đơn giản

    public MyItem(int imageRes, String name, String price) {
        this.imageRes = imageRes;
        this.name = name;
        this.price = price;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}

