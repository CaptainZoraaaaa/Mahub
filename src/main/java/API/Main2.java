package API;

import Entity.ImageIcontester;
import Entity.Persons;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args) throws IOException {


        File file2 = new File("Webbpage/images/webpic.jpg");
        ImageIcontester image2 = new ImageIcontester();
        image2.s = file2;
        image2.title = "hej2";


        Persons persons = new Persons("2","2");
        Persons persons2 = new Persons("3","3");
        ArrayList<Persons> list = new ArrayList<>();
        Persons[] ok = new Persons[2];
        list.add(persons);
        list.add(persons2);
        Gson gson = new Gson();
        String ssss = gson.toJson(list);
        BufferedWriter br = new BufferedWriter(new FileWriter("src/main/java/API/Text.txt"));
        br.write(ssss);
        br.close();


        list.clear();
        BufferedReader bw = new BufferedReader(new FileReader("src/main/java/API/Text.txt"));
        String text = bw.readLine();
        list = gson.fromJson(text, (Type) Persons[].class);
        System.out.println(ok[1]);
    }
}
