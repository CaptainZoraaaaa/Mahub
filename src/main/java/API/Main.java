package API;

import Controller.Server;
import Entity.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            Response response = new Response();
            ObjectMapper objectMapper = new ObjectMapper();
            Server server = Server.getInstance();
            ConcurrentHashMap<String, WsContext> users = new ConcurrentHashMap<>();
            Javalin app = Javalin.create(config -> {
                config.plugins.enableCors(cors -> {
                    cors.add(it -> {
                        it.anyHost();
                    });
                });
                config.staticFiles.add("/Webbpage", Location.CLASSPATH);
            }).start(5500);

            app.get("/", ctx -> {
                System.out.println("noice");
            });

            app.get("/testPage", ctx -> {
                System.out.println("testar");
                File file = new File("Webbpage/images/webpic.jpg");
                ImageIcontester image = new ImageIcontester();
                image.s = file;
                ctx.json(image);
            });

            app.post("/signup", ctx -> {
                System.out.println(ctx.body());
                response.message = "NICE DONE";
                JsonNode jsonNode = objectMapper.valueToTree(response);
                ctx.json(jsonNode);
            });

            app.ws("/inbox", ws -> {
                ws.onConnect(ctx -> {
                    String userid = ctx.queryParam("userid");
                    assert userid != null;
                    users.put(userid, ctx);
                });

                ws.onClose(ctx -> {
                    String userid = ctx.queryParam("userid");
                    assert userid != null;
                    users.remove(userid);
                });

                ws.onMessage(ctx -> {
                    String message = ctx.message();
                    String userid = ctx.queryParam("userid");
                    assert userid != null;
                });
            });

            app.post("/login", ctx -> {
                Login login = gson.fromJson(ctx.body(), Login.class);
                if (server.login(login.username, login.password)) {
                    ctx.json(gson.toJson(server.getUser(login.username)));
                } else {
                    ctx.json(gson.toJson(new LoginError()));
                }
            });

            app.post("/register", ctx -> {
                User user = gson.fromJson(ctx.body(), User.class);
                response.message = server.registerNewUser(user);
                JsonNode jsonNode = objectMapper.valueToTree(response);
                ctx.json(jsonNode);
            }).post("/addProduct", ctx->{
                Product product = gson.fromJson(ctx.body().toString(), Product.class);
                product.status = "available";
                response.message = server.addProduct(product);
                ctx.json(gson.toJson(response));
            }).post("/removeProduct", ctx -> {
                //TODO: Testa om dessa fungerar, addProduct, remove, sell och buy. Inga av dessa har testats, vilka ska ha response/inte ha response, har bara utgått från metoderna i server
               /*
                Gson gson = new Gson();
                int productId = gson.fromJson(ctx.body().toString(), int.class);
                Response response = new Response();
                response.message = server.removeProduct(productId).toString();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(new Gson().toJson(response));
                ctx.json(jsonNode);
                */
                int productId = gson.fromJson(ctx.body(), int.class);
                response.message = server.removeProduct(productId).toString();
                String jsonResponse = objectMapper.writeValueAsString(response);
                ctx.json(jsonResponse);

            }).post("/sellProduct", ctx -> {
                SellConfirmation sc = gson.fromJson(ctx.body(), SellConfirmation.class);
                server.sellProduct(sc.productId, sc.buyerName);
            }).post("/buyRequest", ctx -> {
                int[] productIds = ctx.bodyAsClass(int[].class);
                String buyerName = ctx.queryParam("buyerName");
                server.buyRequest(productIds, buyerName);
            }).get("/getProducts", ctx -> {
                int offset = Integer.parseInt(ctx.queryParam("offset"));
                Product[] products = server.getProducts(offset);
                ctx.json(gson.toJson(products));
            });
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

