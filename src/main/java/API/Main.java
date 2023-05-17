package API;

import Controller.Server;
import Entity.ImageIcontester;
import io.javalin.Javalin;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try {
            Javalin app = Javalin.create(config -> {
                config.plugins.enableCors(cors -> {
                    cors.add(it -> {
                        it.anyHost();
                    });
                });
            }).start(5500);

            app.get("/", ctx->{
                System.out.println("noice");
            }).get("/testPage", ctx->{
                System.out.println("testar");
                File file = new File("Webbpage/images/webpic.jpg");
                ImageIcontester image = new ImageIcontester();
                image.s = file;
                ctx.json(image);
                int i;
            }).post("/addProduct", ctx->{
                Server server = Server.getInstance();
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

