package com.nguyendugiahan.nguyendugiahan_2123110317;

public class CartItem {
    private String name;
    private String price;
    private String description;
    private int image;

    public CartItem(String name, String price, String description, int image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public int getImage() { return image; }
}
