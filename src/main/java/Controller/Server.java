package Controller;

import Entity.Product;
import Entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.javalin.http.sse.SseClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;


public class Server {
    static Server server = new Server();
    //ProductId and Product
    private LinkedHashMap<Integer, Product> productHashMap = new LinkedHashMap<>();
    //Username as Id
    private HashMap<String, User> users = new HashMap<>();
    //Username (owner of items) and their items that have been requested to be bought
    private HashMap<String, ArrayList<Product>> buyRequestsToSeller = new HashMap<>();
    //Username and their purchase history
    private HashMap<String, ArrayList<Product>> purchaseHistory = new HashMap<>();

    public static Server getInstance(){
        if (server==null){
            server = new Server();
        }
        return server;
    }

    // TODO: 2023-05-18 template method är en pattern som vi kan använda oss av för add product
    //  och remove. Ex) att vi ska logga den. Kolla lab1.
    // TODO: 2023-05-18 Proxy pattern seminar 3 part 2. lazy loading
    // TODO: 2023-05-19 Observer pattern

    public String addProduct(Product product){
        product.productId = productHashMap.size();
        productHashMap.put((product.productId), product);
        return "The product have been added to MaHub";
    }

    public Product removeProduct(int productId){
        return productHashMap.remove(productId);
    }

    //From owner's client
    public void sellProduct(int productId, String buyerName){
        Product product = productHashMap.get(productId);
        product.status = "sold";
        product.buyerName = buyerName;
        product.datePurchased = new Date(System.currentTimeMillis());

        if(purchaseHistory.get(buyerName) != null){
            purchaseHistory.get(buyerName).add(product);
        } else {
            purchaseHistory.put(buyerName, new ArrayList<>());
            purchaseHistory.get(buyerName).add(product);
        }
    }

    public void buyRequest(int[] productIds, String buyerName){
        for (int productId: productIds) {
            Product product = productHashMap.get(productId);
            String userId = productHashMap.get(productId).sellerName;
            product.buyerName = buyerName;
            if(buyRequestsToSeller.get(userId) != null){
                buyRequestsToSeller.get(userId).add(product);
            } else {
                buyRequestsToSeller.put(userId, new ArrayList<>());
                buyRequestsToSeller.get(userId).add(product);
            }
        }
    }

    public Product[] getItemsWithOffer(String username){
        ArrayList<Product> temp = buyRequestsToSeller.get(username);
        Product[] products = new Product[temp.size()];
        int i=0;
        for (Product product:temp) {
            products[i++]=product;
        }

        return products;
    }

    public Product[] getProducts (int offset){
        Product[] temp = new Product[9];
        int counter = 0;
        int offsetCounter = offset;

        for (Integer key: productHashMap.keySet()) {
            if(offsetCounter>0){
               offsetCounter--;
               continue;
            }
            Product product = productHashMap.get(key);
            if(product.status.equals("available")){
                temp[counter++] = product;
            }
            if (counter == temp.length){
                break;
            }
        }

        return temp;
    }

    public Product[] getPurchaseHistory(String username, Date start, Date end){
        ArrayList<Product> tenp = purchaseHistory.get(username);
        Product[] products = new Product[tenp.size()];
        int i=0;
        for (Product product:tenp) {
            if(product.date.before(end) && product.date.after(start)){
                products[i++]=product;
            }
        }

        return products;
    }


    // TODO: 2023-05-19 Skriva klart metoden, fixa if satsen med ifall price och condition är null.
    public Product[] searchProduct(String name, double priceRangeMin, double priceRangeMax, String condition){
        ArrayList<Product> temp = new ArrayList<>();

        for (Integer key: productHashMap.keySet()) {
            Product product = productHashMap.get(key);
            if(product.productName.equalsIgnoreCase(name)
                    && product.price <= priceRangeMax
                    && product.price>= priceRangeMin
                    && product.condition.equalsIgnoreCase(condition)
                    && product.status.equals("available")){

            }
        }

        return null;
    }

    public String registerNewUser(User newUser){
        if(users.containsKey(newUser.username)) return "Could not register user, change username";

        for (String key: users.keySet()) {
            if(users.get(key).email.equalsIgnoreCase(newUser.email)) return "Could not register user, change email";
        }

        users.put(newUser.username, newUser);
        return "User have successfully been added";
    }

    public boolean login(String username, String password){
        return users.get(username).password.equals(password);
    }

    public User getUser(String username){
        return users.get(username);
    }

    public String saveToFile(){
        Gson gson = new Gson();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("Products"))){
            String json = gson.toJson(productHashMap);
            System.out.println(json);
            bw.write(json);
            bw.flush();
            return "The products have been successfully saved";
        } catch (Exception e){
            e.printStackTrace();
            return "Error: Could not be saved";
        }

    }

    public String readFile(){
        Gson gson = new Gson();
        try(BufferedReader br = new BufferedReader(new FileReader("Products"))){
            String json = br.readLine();
            productHashMap = gson.fromJson(json, new TypeToken<LinkedHashMap<String, Product>>(){}.getType());

            return "The products have been successfully raed from the file";
        } catch (Exception e){
            e.printStackTrace();
            return "Error: Could not read from file";
        }
    }

    public static void main(String[] args) {
        Server server = Server.getInstance();
        Product product = new Product();
        product.productId = 0;
        product.productName = "Nintendo Switch";
        product.sellerName = "Max Tiderman";
        product.date = new Date(2023,05,19);
        product.image = "https://www.netonnet.se/GetFile/ProductImagePrimary/gaming/spel-och-konsol/nintendo/nintendo-konsol/nintendo-switch-oled-model-white(1019708)_450564_14_Normal_Extra.jpg";

        Product product1 = new Product();
        product1.productId = 1;
        product1.productName = "Playstation 5";
        product1.sellerName = "Daniel Olsson";
        product1.date = new Date(2023,05,19);
        product1.image = "https://www.netonnet.se/GetFile/ProductImagePrimary/gaming/spel-och-konsol/playstation/playstation-konsol/sony-playstation-5-c-chassi(1027489)_535790_6_Normal_Extra.jpg";

        server.addProduct(product);
        server.addProduct(product1);
        //System.out.println(server.removeProduct("TestProduct1"));

        System.out.println(server.saveToFile());
        System.out.println(server.readFile());

        System.out.println(server.removeProduct(0));
        System.out.println(server.removeProduct(1));
    }
}
