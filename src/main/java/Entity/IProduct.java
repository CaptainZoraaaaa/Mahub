package Entity;

import java.util.Date;

public interface IProduct {
    void load(int productId, String productName, String sellerName, String buyerName, double price, String image, Date date, String condition, String colour, String status, Date datePurchased);
}
