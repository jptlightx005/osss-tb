package com.hoshizora.ossstb;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JLTeruel on 01/03/2017.
 */

public class ProductItem {
    private int productid;
    private String itemName;
    private int inventory;
    private float price;
    private String imageurl;
    public ProductItem() {
    }

    public ProductItem(int productid, String itemName, int inventory, float price, String imageurl) {
        this.productid = productid;
        this.itemName = itemName;
        this.inventory = inventory;
        this.price = price;
        this.imageurl = imageurl;
    }

    public static ProductItem ProductItemFromJson(JSONObject jsonObject){
        ProductItem productItem = new ProductItem();
        try{
            productItem.productid = Integer.parseInt(jsonObject.getString("ID"));
            productItem.itemName = jsonObject.getString("product_name");
            productItem.price = Float.parseFloat(jsonObject.getString("price"));
            productItem.inventory = Integer.parseInt(jsonObject.getString("inventory"));
            productItem.imageurl = jsonObject.getString("image");
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        return productItem;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public  void setImageurl(String url){
        this.imageurl = url;
    }

    public String getImageurl(){
        return imageurl;
    }
}
