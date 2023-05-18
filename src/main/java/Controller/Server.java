package Controller;

import Entity.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;


public class Server {
    static Server server = new Server();
    private LinkedHashMap<String, Product> productHashMap = new LinkedHashMap<>();

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
        productHashMap.put(product.id, product);

        return "The product have been added to MaHub";
    }

    public Product removeProduct(String id){
        return productHashMap.remove(id);
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
            temp[counter++] = productHashMap.get(key);
            if (counter == temp.length){
                break;
            }
        }

        return temp;
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
        product.id = "TestProduct1";
        product.productName = "Nintendo Switch";
        product.sellerName = "Max Tiderman";
        product.date = "2023-05-17";
        product.image = "https://www.netonnet.se/GetFile/ProductImagePrimary/gaming/spel-och-konsol/nintendo/nintendo-konsol/nintendo-switch-oled-model-white(1019708)_450564_14_Normal_Extra.jpg";

        Product product1 = new Product();
        product1.id = "TestProduct2";
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
