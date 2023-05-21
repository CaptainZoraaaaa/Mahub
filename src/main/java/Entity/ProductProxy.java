package Entity;

import java.util.Date;

public class ProductProxy implements IProduct{
    public Product realProduct;
    public int productId;
    public String productName;
    public String image;

    public ProductProxy(int productId, String productName, String image) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
    }

    @Override
    public void load(int productId, String productName, String sellerName, String buyerName, double price, String image, Date date, String condition, String colour, String status, Date datePurchased) {
        if(realProduct == null){
            realProduct = new Product();
        }
        realProduct.load(productId, productName, sellerName, buyerName, price, image, date, condition, colour, status, datePurchased);
    }
}
