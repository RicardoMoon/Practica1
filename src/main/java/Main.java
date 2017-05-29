import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Adolfo on 26/5/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Escriba el URL a parsear: ");
        String url = scan.next();

        //System.out.println("URL: "+url);

        Document document;
        try {
            document = Jsoup.connect(url).get();
            System.out.println("URL valido!\n");
            IncisoA(document);

            Elements parrafos = document.getElementsByTag("p");
            IncisoB(parrafos);

            Elements forms = document.getElementsByTag("form");
            IncisoD(forms);
            IncisoE(forms);
            IncisoF(forms, url);

        } catch (UnknownHostException e) {
            System.err.println("URL no existe, verificar URL entrado \n");
        } catch (IllegalArgumentException e){
            System.err.println("URL no valido, arregar protocolo http o https\n");
        }
    }

    private static void IncisoF(Elements forms, String url) throws IOException {
        int i = 1;
        String formUrl;
        Document newURL;

        System.out.println("\nInciso F: \n");

        for (Element form: forms) {
            System.out.println("Formulario " + i + ":");
            Elements posts = form.getElementsByAttributeValueContaining("method","post");
            for (Element post: posts) {
                char urlChar = form.attr("action").charAt(0);
                if(urlChar == '.'){
                    int formUrlSize = form.attr("action").length();
                    formUrl = form.attr("action").toString().substring(1,formUrlSize);
                }
                else{
                    formUrl = form.attr("action");
                }
                newURL = Jsoup.connect(url + formUrl).data("asignatura","practica1").post();
                System.out.println(newURL);
            }
            System.out.println("\n");
            i++;
        }
    }

    private static void IncisoE(Elements forms) {
        int i = 1;
        int j = 1;
        System.out.println("- Inciso E: \n");
        for (Element form: forms) {
            System.out.println("formulario " + j + ":");
            j++;
            Elements inputs = form.getElementsByTag("input");
            for (Element input: inputs) {
                Attributes inputAttributes = input.attributes();
                System.out.print(" input " + i);
                i++;
                for (Attribute attribute: inputAttributes) {
                    if(attribute.getKey().equalsIgnoreCase("type")){
                        System.out.println(" tipo: " + attribute.getValue());
                    }
                }
            }
            System.out.println();
            i = 1;
        }
    }

    private static void IncisoD(Elements forms) {
        int postCount = 0;
        int getCount = 0;

        System.out.println("- Inciso D: \n");
        for (Element form: forms) {
            Elements posts = form.getElementsByAttributeValueContaining("method","post");
            postCount += posts.size();

            Elements gets = form.getElementsByAttributeValueContaining("method","get");
            getCount += gets.size();
        }
        System.out.println("Cantidad de formularios utilizando el método post: " + postCount);
        System.out.println("Cantidad de formularios utilizando el método get: " + getCount + "\n");
    }

    private static void IncisoB(Elements parrafos) {
        int pCount = 0;
        int imgCount = 0;
        System.out.println("- Inciso B: \n");
        for (Element p: parrafos) {
            Elements children = p.children();
            imgCount += IncisoC(children);
            pCount++;
        }
        System.out.println("Cantidad de parrafos (p) en el documento: " + pCount + "\n");
        System.out.println("- Inciso C: \n");
        System.out.println("Cantidad de imagenes (img) dentro de parrafos (p) en el documento: " + imgCount + "\n");
    }

    private static int IncisoC(Elements children) {
        int imgCount = 0;
        for (Element child: children) {
            Elements imgs = child.getElementsByTag("img");
            imgCount += imgs.size();
        }
        return imgCount;
    }

    private static void IncisoA(Document document) {
        System.out.println("- Inciso A: \n");
        String[] lines = document.html().split("\n");
        System.out.println("Cantidad de lineas en el documento: "+lines.length + "\n");
    }
}
