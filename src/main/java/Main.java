
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.stream.Stream;

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
            IncisoA(document,url);

            Elements parrafos = document.getElementsByTag("p");
            IncisoB(parrafos);

            Elements forms = document.getElementsByTag("form");
            IncisoD(forms);
            IncisoE(forms);
            IncisoF(forms);

        } catch (UnknownHostException e) {
            System.err.println("URL no existe, verificar URL entrado \n");
        } catch (IllegalArgumentException e){
            System.err.println("URL no valido, arregar protocolo http o https\n");
        }
    }

    private static void IncisoF(Elements forms) throws IOException {
        int i = 1;
        String formUrl;
        Document newURL;

        System.out.println("- Inciso F: \n");

        for (Element form: forms) {
            System.out.println("Formulario " + i + ":");
            Elements posts = form.getElementsByAttributeValueContaining("method","post");
            for (Element post: posts) {
                String absURL = post.absUrl("action");
                newURL = Jsoup.connect(absURL).data("asignatura","practica1").post();
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

    private static void IncisoA(Document document, String url) throws MalformedURLException {
        URL getURL =  new URL(url);
        InputStreamReader streamURL = null;
        try {
            streamURL = new InputStreamReader(getURL.openStream());
        } catch (IOException e) {
            //e.printStackTrace();
        }
        BufferedReader readURL = new BufferedReader(streamURL);
        Stream<String> readLines = readURL.lines();
        Object[] lines = readLines.toArray();
        System.out.println("- Inciso A: \n");
        System.out.println("Cantidad de lineas en el documento: "+lines.length + "\n");
    }
}