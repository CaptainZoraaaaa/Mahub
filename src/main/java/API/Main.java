package API;

import Controller.Server;
import Entity.ImageIcontester;
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
            Server server = Server.getInstance();
            ConcurrentHashMap<String, WsContext> users = new ConcurrentHashMap<>();
            Javalin app = Javalin.create(config -> {
                config.plugins.enableCors(cors -> {
                    cors.add(it -> {
                        it.anyHost();
                    });
                });
                config.staticFiles.add("/public", Location.CLASSPATH);
            }).start(5500);

            app.get("/", ctx->{
                System.out.println("noice");
            }).get("/testPage", ctx->{
                System.out.println("testar");
                File file = new File("Webbpage/images/webpic.jpg");
                ImageIcontester image = new ImageIcontester();
                image.s = file;
                ctx.json(image);

            }).post("/addProduct", ctx->{

            }).ws("/inbox/{id}", ws -> {
                //offline meddelanden + ta emot userId
                ws.onConnect(ctx -> {
                    String userid = ctx.pathParam("id");
                    users.put(userid, ctx);
                });

                ws.onClose(ctx -> {

                });

                //Skicka ut rå data så kan javascript hantera detta
                ws.onMessage(ctx -> {

                });
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

