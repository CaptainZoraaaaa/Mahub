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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;


public class Server {
    static Server server = new Server();
    private LinkedHashMap<String, Product> productHashMap = new LinkedHashMap<>();
    private ArrayList<User> users = new ArrayList<>();

    private HashMap<Integer, ArrayList<String>> message = new HashMap<>();
    private ConcurrentLinkedDeque<SseClient> clients = new ConcurrentLinkedDeque<>();
    private Object lock = new Object();

    public static Server getInstance(){
        if (server==null){
            server = new Server();
        }
        return server;
    }

    // TODO: 2023-05-18 template method är en pattern som vi kan använda oss av för add product
    //  och remove. Ex) att vi ska logga den. Kolla lab1.
    // TODO: 2023-05-18 Proxy pattern seminar 3 part 2.

    public String addProduct(Product product){
        productHashMap.put(product.productId, product);

        return "The product have been added to MaHub";
    }

    public Product removeProduct(String productId){
        return productHashMap.remove(productId);
    }

    public void sellProduct(String productId){
        productHashMap.get(productId).status = "sold";
    }

    public String[] getMessages(int userId){
        synchronized (lock){
            while(message.get(userId) == null){
                try {
                    lock.wait();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public void buyRequest(){

    }

    public Product[] getProducts (int offset){
        Product[] temp = new Product[10];
        int counter = 0;
        int offsetCounter = offset;

        for (String key: productHashMap.keySet()) {
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


    // TODO: 2023-05-19 Skriva klart metoden, fixa if satsen med ifall price och condition är null.
    public Product[] searchProduct(String name, double priceRangeMin, double priceRangeMax, String condition){
        ArrayList<Product> temp = new ArrayList<>();

        for (String key: productHashMap.keySet()) {
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
        boolean ok = true;
        for (User user: users) {
            if(user.email.equalsIgnoreCase(newUser.email) || user.username.equalsIgnoreCase(newUser.username)){
                ok = false;
                return "Could not register user, change email and/or username";
            }
        }
        if(ok){
            newUser.userId = users.size();
            users.add(newUser);
            return "User have successfully been added";
        }

        return "Could not register user, change email and/or username";
    }

    public User login(String username, String password){
        for (User user: users) {
            if(user.username.equals(username) && user.password.equals(password)){
                return user;
            }
        }

        return null;
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
        product.productId = "TestProduct1";
        product.productName = "Nintendo Switch";
        product.sellerName = "Max Tiderman";
        product.date = "2023-05-17";
        product.image = "https://www.netonnet.se/GetFile/ProductImagePrimary/gaming/spel-och-konsol/nintendo/nintendo-konsol/nintendo-switch-oled-model-white(1019708)_450564_14_Normal_Extra.jpg";

        Product product1 = new Product();
        product1.productId = "TestProduct2";
        product1.productName = "Playstation 5";
        product1.sellerName = "Daniel Olsson";
        product1.date = "2023-05-01";
        product1.image = "https://www.netonnet.se/GetFile/ProductImagePrimary/gaming/spel-och-konsol/playstation/playstation-konsol/sony-playstation-5-c-chassi(1027489)_535790_6_Normal_Extra.jpg";

        server.addProduct(product);
        server.addProduct(product1);
        //System.out.println(server.removeProduct("TestProduct1"));

        System.out.println(server.saveToFile());
        System.out.println(server.readFile());

        System.out.println(server.removeProduct("TestProduct1"));
        System.out.println(server.removeProduct("TestProduct2"));
    }
}
