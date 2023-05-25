package Controller;

import Entity.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.javalin.websocket.WsContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


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
    public ConcurrentHashMap<String, WsContext> usersConnected;
    private LinkedList<IProduct> proxyProducts = new LinkedList<>();
    private Object proxyLock = new Object();

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
        product.status = "available";
        productHashMap.put((product.productId), product);
        ProductProxy productProxy = new ProductProxy(product.productId, product.productName, product.image);
        synchronized (proxyLock){
            proxyProducts.addLast(productProxy);
        }
        checkInterestedProducts(product.productName);
        return "The product have been added to MaHub";
    }

    public Product removeProduct(int productId){
        return productHashMap.remove(productId);
    }

    //From owner's client
    public String acceptBuyRequest(int productId, String buyerName){
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
        buyRequestsToSeller.get(product.sellerName).removeIf(temp -> temp.productId == product.productId);
        return "Product has been sold";
    }
    public String denyBuyRequest(int productId, String buyerName){
        Product product = productHashMap.get(productId);
        buyRequestsToSeller.get(product.sellerName).removeIf(temp -> temp.productId == productId && temp.buyerName.equals(buyerName));
        return "Buy request has been denied";
    }

    public String buyRequest(int[] productIds, String buyerName){
        for (int productId: productIds) {
            Product product = productHashMap.get(productId);
            String userId = productHashMap.get(productId).sellerName;
            product.buyerName = buyerName;
            Product temp = new Product();
            temp.clone(product.productId,product.productName,product.sellerName,product.buyerName,product.price,product.image,product.date,product.condition,product.colour,product.status,product.datePurchased);
            if(buyRequestsToSeller.get(userId) != null){
                buyRequestsToSeller.get(userId).add(temp);
            } else {
                buyRequestsToSeller.put(userId, new ArrayList<>());
                buyRequestsToSeller.get(userId).add(temp);
            }
        }
        return "Buy request sent";
    }

    // TODO: Kolla så att Lazy Loading fungerar - Linus 21/5
    public Product[] getItemsWithOffer(String username) {
        ArrayList<Product> temp = buyRequestsToSeller.get(username);
        if (temp == null) {
            return new Product[0];  // Return an empty array if no items found
        }
        return temp.toArray(new Product[0]);
    }

    // TODO: Kolla så att Lazy Loading fungerar - Linus 21/5
    // TODO: 2023-05-21 Lazy loading funkar, den kräver dock en proxy
    //  för den. Så skapa en EProduxt med product id, name och bild. Så är det
    //  det som skickas till hemsidan. Och om den vill ha hela produkten så laddar den in
    //  den när man klickar in på den

    //TODO: Använda proxy istället för riktiga Product när man kan, framförallt när man läser in?
    public Product[] getProducts(int offset) {
        ArrayList<Product> temp = new ArrayList<>();
        int counter = 0;
        int offsetCounter = offset;

        for (Integer key : productHashMap.keySet()) {
            if (offsetCounter > 0) {
                offsetCounter--;
                continue;
            }
            Product product = productHashMap.get(key);
            if (product.status.equals("available")) {
                temp.add(product);
                counter++;
            }
            if (counter == 9) {
                break;
            }
        }

        return temp.toArray(new Product[0]);
    }
    public ProductProxy[] getProxyProducts(int offset) {
        ArrayList<ProductProxy> temp = new ArrayList<>();
        int counter = 0;
        int offsetCounter = offset;

        synchronized (proxyLock){
            for (IProduct product : proxyProducts) {
                if (offsetCounter > 0) {
                    offsetCounter--;
                    continue;
                }

                if(productHashMap.get(((ProductProxy) product).productId).status.equals("available")){
                    temp.add((ProductProxy) product);
                    counter++;
                }

                if (counter == proxyProducts.size() || counter == 9) {
                    break;
                }
            }
        }

        return temp.toArray(new ProductProxy[0]);
    }

    public Product getProductById(int id){
        return productHashMap.get(id);
    }
    public ProductProxy[] getLatestProducts(){
        int numberOfProducts = 4;
        LinkedList<ProductProxy> latestProducts = new LinkedList<>();
        for (int i = proxyProducts.size()-1; i >=0 && numberOfProducts > 0; i--) {
            latestProducts.addFirst((ProductProxy) proxyProducts.get(i));
            numberOfProducts--;
        }
        return latestProducts.toArray(latestProducts.toArray(new ProductProxy[0]));
    }

    public Product[] getPurchaseHistory(String username, Date start, Date end){
        System.out.println(start.toString());
        System.out.println(end.toString());
        ArrayList<Product> temp = purchaseHistory.get(username);
        Product[] products = new Product[temp.size()];
        int i=0;
        for (Product product:temp) {
            System.out.println(product.datePurchased.toString());
            if(product.datePurchased.before(end) && product.datePurchased.after(start)){
                products[i++]=product;
            }
        }
        System.out.println(Arrays.toString(products));

        return products;
    }


    // TODO: Fixat - Linus
    public Product[] searchProduct(SearchQuery searchQuery){
        ArrayList<Product> temp = new ArrayList<>();

        for (Integer key : productHashMap.keySet()) {
            Product product = productHashMap.get(key);
            if (product.productName.equalsIgnoreCase(searchQuery.name)
                    && (searchQuery.priceRangeMin == 0 || product.price >= searchQuery.priceRangeMin)
                    && (searchQuery.priceRangeMax == 0 || product.price <= searchQuery.priceRangeMax)
                    && (searchQuery.condition == null || product.condition.equalsIgnoreCase(searchQuery.condition))
                    && product.status.equals("available")) {
                temp.add(product);
            }
        }

        return temp.toArray(new Product[0]);
    }

    // TODO: 2023-05-21 Fixa så att om usern är offline så läggs den någonstans temporärt. Likt chatten.
    // TODO: 2023-05-21 Update: Det det gör just nu är bara att den kollar och skickar för de som är inloggade
    //  De som inte är inloggade kommer ej att få något förens de loggar in. Och då är det onConnect som körs och löser det .
    public void checkInterestedProducts(String newProductName){
        for (String user: usersConnected.keySet()) {
            for (String interestProductName : users.get(user).interestedProducts.interests) {
                if (newProductName.equalsIgnoreCase(interestProductName)){
                    System.out.println("yes");
                    usersConnected.get(user).send(interestProductName + " is now available on MaHub");
                }
            }
        }
    }
    public String[] getInterests(String username) {
        User tempUser = users.get(username);
        ArrayList<String> tempInterests = (ArrayList<String>) tempUser.interestedProducts.interests;
        return tempInterests.toArray(new String[tempInterests.size()]);
    }
    public String addInterest(String username, String interest){
        User tempUser = users.get(username);
        tempUser.interestedProducts.interests.add(interest);
        return "Interest has been added";
    }

    /**
     *
     * Removes the first occurence of the interest String in the List
     * Should be suitable as users have no reason to have duplicates of interests
     *
     */
    public String removeInterest(String username, String interest){
        User tempUser = users.get(username);
        tempUser.interestedProducts.interests.remove(interest);
        return "Interest has been removed";
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
        User tempUser = users.get(username);
        return tempUser;
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
