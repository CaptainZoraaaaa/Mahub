package Entity;

import Controller.Server;

import java.util.Date;

public class Product implements IProduct{
    public int productId;
    public String productName;
    //seller name is used as id
    public String sellerName;
    public String buyerName;
    public double price;
    public String image;
    //date for when it's put on the market
    public Date date;
    //New, very good, good, not working properly
    public String condition;
    public String colour;
    //available or sold
    public String status;
    public Date datePurchased;

    // TODO: 2023-05-23 Remove constructors
    public Product(String productName, String sellerName, double price, String image, Date date, String condition, String colour) {
        this.productName = productName;
        this.sellerName = sellerName;
        this.price = price;
        this.image = image;
        this.date = date;
        this.condition = condition;
        this.colour = colour;
    }

    public Product(String productName, String sellerName) {
        this.productName = productName;
        this.sellerName = sellerName;
    }

    public String clone(int productId, String productName, String sellerName, String buyerName, double price, String image, Date date, String condition, String colour, String status, Date datePurchased) {
        this.productId = productId;
        this.productName = productName;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.price = price;
        this.image = image;
        this.date = date;
        this.condition = condition;
        this.colour = colour;
        this.status = status;
        this.datePurchased = datePurchased;
        return "return clonned product";
    }

    public Product() {
    }

    @Override
    public void load(int productId, String productName, String sellerName, String buyerName, double price, String image, Date date, String condition, String colour, String status, Date datePurchased) {
        this.productId = productId;
        this.productName = productName;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.price = price;
        this.image = image;
        this.date = date;
        this.condition = condition;
        this.colour = colour;
        this.status = status;
        this.datePurchased = datePurchased;
    }

    @Override
    public void load(Server server) {
        Product product = server.getProductById(this.productId);
        this.productId = product.productId;
        this.productName = product.productName;
        this.sellerName = product.sellerName;
        this.buyerName = product.buyerName;
        this.price = product.price;
        this.image = product.image;
        this.date = product.date;
        this.condition = product.condition;
        this.colour = product.colour;
        this.status = product.status;
        this.datePurchased = product.datePurchased;
    }
}
