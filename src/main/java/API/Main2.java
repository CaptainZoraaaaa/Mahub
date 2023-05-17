package API;

import Entity.ImageIcontester;
import Entity.Perons;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import netscape.javascript.JSObject;
import org.eclipse.jetty.io.WriterOutputStream;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args) throws IOException {


        File file2 = new File("Webbpage/images/webpic.jpg");
        ImageIcontester image2 = new ImageIcontester();
        image2.s = file2;
        image2.title = "hej2";


        Perons perons = new Perons("2","2");
        Perons perons2 = new Perons("3","3");
        ArrayList<Perons> list = new ArrayList<>();
        Perons[] ok = new Perons[2];
        list.add(perons);
        list.add(perons2);
        Gson gson = new Gson();
        String ssss = gson.toJson(list);
        BufferedWriter br = new BufferedWriter(new FileWriter("src/main/java/API/Text.txt"));
        br.write(ssss);
        br.close();


        list.clear();
        BufferedReader bw = new BufferedReader(new FileReader("src/main/java/API/Text.txt"));
        String text = bw.readLine();
        list = gson.fromJson(text, (Type) Perons[].class);
        System.out.println(ok[1]);
    }
}
