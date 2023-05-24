package API;

import Controller.Server;
import Entity.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            ObjectMapper objectMapper = new ObjectMapper();
            Server server = Server.getInstance();
            //TEST DATA
            // TODO: 2023-05-23 Remove this later
            /**
             *             server.registerNewUser(new User("Maximilian", "Tindermannen", "2010-12-12", "Max@Malmo.com", "Sorasora", "StormSora123"));
             *             server.registerNewUser(new User("Daniel", "Olsson", "0001-12-12", "daniel@chad.com", "Chad", "Chaddest"));
             *             server.addProduct(new Product("MacBook", "Sora", 999.0, "data:image/jpeg;base64,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KfN+xMk4VaSGzEL7lP8AwTy4rC9Cr4tHFYXoVfFocpvbfyQlFehstwy0oV1Til1dtuLpc37E8hwp0l/WV9yn/gnlxWF6FXxafnF4boVPFpHPctwe44V6T/rK+5T/AMEyq8LtJsCGxKkEWP5unmNnQmrxeG6FTxb8YFLDnYlTxb8YabItFm+QWmVpYoG3p09nstLZnz5wc4W1MBTenhqYCu2uxemzsTYDbrDKw+JkpU8q+NHq0h202H/VI6WT1Iu+JVvArh/isbiVostMktc6tNgFQKxYs1yF2AAnnNueWlIaoWfhAM8MRhKdRSjojocirKGUjrByM2YkEkOvBjAAWGEwoHVRp/hNjA6GwtEk0KFCmW2lKaIT2lRnJCIBhxY3DwEcWNw8BM4gGHFjcPARxY3DwEziAYcWNw8BHFjcPATOIBhxY3DwEcWNw8BM4gGHFjcPARxY3DwEziAYcWNw8BHFjcPATOIBhxY3DwEcWNw8BM4gGHFjcPARxY3DwEziAYcWNw8BHFjcPATOIBhxY3DwEcWNw8BM4gGHFjcPATUxui8PXULXo0qig3AdFcA7wGE3ogEP/NnA2t5phbbuJp/hNzB6Oo0V1KNKnTS99VFVFvvsBtm5EAxVQNgAmURAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAP/2Q==", new Date(2022, 12, 14), "Good", "Space Grey"));
             *             server.addProduct(new Product("Träd","Sora",222,"data:image/jpeg;base64,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",new Date(),"Good","green"));
             *             server.buyRequest(new int[]{0,1}, "Maximilian");
             *             server.buyRequest(new int[]{0}, "Daniel");
             */
            server.registerNewUser(new User("Max", "Tiderman", "2002-05-12", "Max@Svedala.com", "Sora", "StormSora123"));
            server.registerNewUser(new User("Daniel", "Olsson", "0001-12-12", "daniel@chad.com", "Chad", "Chaddest"));
            server.addProduct(new Product("MacBook", "Chad"));
            server.addProduct(new Product("Bonsai Tree", "Chad"));
            server.addProduct(new Product("Max's Sock", "Chad"));
            server.addProduct(new Product("Used Gum", "Chad"));
            server.buyRequest(new int[]{0,1,2,3}, "Sora");
            server.acceptBuyRequest(0, "Sora");
            server.acceptBuyRequest(1, "Sora");
            server.acceptBuyRequest(2, "Sora");
            server.acceptBuyRequest(3, "Sora");
            // until here
            ConcurrentHashMap<String, WsContext> users = new ConcurrentHashMap<>();
            server.usersConnected = users;
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
                Response response = new Response();
                response.message = "NICE DONE";
                JsonNode jsonNode = objectMapper.valueToTree(response);
                ctx.json(jsonNode);
            });

            app.ws("/inbox", ws -> {
                ws.onConnect(ctx -> {
                    String userid = ctx.queryParam("userid");
                    assert userid != null;
                    users.put(userid, ctx);
                    //check if their product that they are interested is available upon connection
                    User user = server.getUser(userid);
                    if (user.interestedProducts.interests.size() != 0){
                        for (String productName:user.interestedProducts.interests) {
                            server.checkInterestedProducts(productName);
                        }
                    }
                });

                ws.onClose(ctx -> {
                    String userid = ctx.queryParam("userid");
                    assert userid != null;
                    users.remove(userid);
                });

                //Sending an array of interests
                ws.onMessage(ctx -> {
                    String message = ctx.message();
                    Interests interests = gson.fromJson(message, Interests.class);
                    String userid = ctx.queryParam("userid");
                    assert userid != null;
                    User user = server.getUser(userid);
                    user.interestedProducts = interests;
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
                Response response = new Response();
                response.message = server.registerNewUser(user);
                JsonNode jsonNode = objectMapper.valueToTree(response);
                ctx.json(jsonNode);
            }).post("/addProduct", ctx->{
                Product product = gson.fromJson(ctx.body().toString(), Product.class);
                product.status = "available";
                Response response = new Response();
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
                Response response = new Response();
                response.message = server.removeProduct(productId).toString();
                String jsonResponse = objectMapper.writeValueAsString(response);
                ctx.json(jsonResponse);

            }).post("/acceptBuyRequest", ctx -> {
                SellConfirmation sc = gson.fromJson(ctx.body(), SellConfirmation.class);
                ctx.json(gson.toJson(new Response().message = server.acceptBuyRequest(sc.productId, sc.buyerName)));
            }).post("/denyBuyRequest", ctx -> {
                SellConfirmation sc = gson.fromJson(ctx.body(), SellConfirmation.class);
                ctx.json(gson.toJson(new Response().message = server.denyBuyRequest(sc.productId, sc.buyerName)));
            }).post("/buyRequest", ctx -> {
                //int[] productIds = ctx.bodyAsClass(int[].class);
                //String buyerName = ctx.queryParam("buyerName");
                BuyRequest buyRequest = gson.fromJson(ctx.body(), BuyRequest.class);
                int[] productIds = buyRequest.productIds;
                String buyerName = buyRequest.buyerName;
                ctx.json(gson.toJson(new Response().message = server.buyRequest(productIds, buyerName)));
            }).get("/getProducts", ctx -> {
                int offset = Integer.parseInt(ctx.queryParam("offset"));
                //Product[] products = server.getProducts(offset);
                ProductProxy[] products = server.getProxyProducts(offset);
                ctx.json(gson.toJson(products));
            }).get("/getProduct/{id}", ctx -> {
                System.out.println("yes sir");
                int id = Integer.parseInt(ctx.pathParam("id"));
                ctx.json(gson.toJson(server.getProductById(id)));
            }).post("/getPurchaseHistory", ctx -> {
                PurchaseHistoryQuery phq = gson.fromJson(ctx.body(), PurchaseHistoryQuery.class);
                ctx.json(gson.toJson(server.getPurchaseHistory(phq.username, phq.start, phq.end)));
            }).post("/getOffers", ctx -> {
                System.out.println(ctx.body());
                Response response = gson.fromJson(ctx.body(), Response.class);
                String userId = response.message;
                Product[] productsWithOffer = server.getItemsWithOffer(userId);
                ctx.json(gson.toJson(productsWithOffer));
            });
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

