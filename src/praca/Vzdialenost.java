/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package praca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.net.URI;

/**
 *
 * @author Peter Rendek
 */
public class Vzdialenost {

    public Vzdialenost() {
    }

    String vzdialenost;
    String cas;

    public String getVzdialenost() {
        return vzdialenost;
    }

    public void setVzdialenost(String vzdialenost) {
        this.vzdialenost = vzdialenost;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public Vzdialenost(String vzdialenost, String cas) {
        this.vzdialenost = vzdialenost;
        this.cas = cas;
    }


    /**
     * Metóda slúžia na vytvorenie poľa vzdialeností medzi Stanicami ZZS a miestom zásahu
     * @param poleStanic - jednotlivé stanice ZZS
     * @param ciel - adresa zásahu
     * @return pole vzdialenosti medzi miestom zásahu a jednotlivými stanicami, vrati NULL ak adresu cieľa G.Api nenašlo
     */
    public String[][] getzdialenostiCasy(String[] poleStanic, String ciel) {// throws MalformedURLException, IOException, ParseException {
        String stanice = "";
        for (int i = 0; i < poleStanic.length; i++) {
            stanice += poleStanic[i] + "%7C";        }
        stanice = (String) stanice.substring(0, stanice.length() - 1);
        stanice = stanice.replace(" ", "+");
        String urls = "https://maps.googleapis.com/maps/api/distancematrix/"
                + "json?origins="
                + stanice
                + "&destinations="
                + ciel
                + "&mode=driving"
                + "&language=en-EN"
                + "&key=AIzaSyChgYn2IO8qL1iZXuyC4Ly7wn5DKmmCIVY";
        urls=urls.replaceAll("(?i)%(?![\\da-f]{2})", "%25");
        try {
            URI uri = new URI(urls); 
            String request = uri.toASCIIString();            
            String[][] pole = null;
            URL myURL = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result, line = reader.readLine();
            result = line;
            while ((line = reader.readLine()) != null) {    //načítanie JSON výstupu po riadkoch
                result += line;
                System.out.println(line);
            }
            connection.disconnect();
            reader.close();            
            JSONParser parser = new JSONParser();                   //rozparsovanie JSON výstupu z Google API
            JSONObject jb = (JSONObject) parser.parse(result);
            JSONArray rows = (JSONArray) jb.get("rows");
            JSONArray origin_addresses = (JSONArray) jb.get("origin_addresses"); //pole stanic
            JSONArray destination_addresses = (JSONArray) jb.get("destination_addresses"); //pole cieľov
            String pom = destination_addresses.get(0).toString();//cieľ-miesto zásahu
            if(pom.isEmpty()) // Ak cieľová adresa nebola nájdená metóda vrati null
            {return null;}  
            int k = rows.size();    //Inak sa zvyso
            pole = new String[k][4];
            int i = 0;
            while (i < k) { //rozparsovanie vzdialeností a časov medzi jednotlivými stanicami a miestom zásahu
                JSONObject row_i = (JSONObject) rows.get(i);
                JSONObject json_row_i = (JSONObject) parser.parse(row_i.toString());
                JSONArray elements = (JSONArray) json_row_i.get("elements");
                JSONObject element0 = (JSONObject) elements.get(0);
                JSONObject json_element0 = (JSONObject) parser.parse(element0.toString());
                JSONObject duration = (JSONObject) json_element0.get("duration");
                JSONObject distance = (JSONObject) json_element0.get("distance");
                pole[i][0] = destination_addresses.get(0).toString();   // naplnenie miesta zásahu
                pole[i][1] = origin_addresses.get(i).toString();        //naplnenie adries jednotlivých staníc
                String p =duration.get("text").toString();              
                p= p.replaceAll("hour","hodina a");     // premonovanie výstupu z Google API
                p= p.replaceAll("mins","minút");
                pole[i][2] = p;                                         //naplnenie času
                pole[i][3] = distance.get("text").toString();           //naplnenie vzdialenosti
                i++;
            }
//            for (int q = 0; q < pole.length; q++) {           //pomocný výpis
//                for (int j = 0; j < pole[q].length; j++) {
//                    System.out.println("[" + pole[q][j].toString() + "]");
//                }
//            }
            return pole;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage() + e.getClass().getCanonicalName());
            return null;
        }
    }
    /**
     * Metóda pomocou ktorej som testoval, pre beh programu nie je potrebná
     * @param args
     * @throws ParseException
     * @throws IOException 
     */
//    public static void main(String[] args) throws ParseException, IOException {
//        String[] start = {"Trenčianska+724/46+NováDubnica",
//            "Kino+Panorex+Nova+Dubnica",
//            "Janka+Krala+1+Nova+Dubnica"};
//        Vzdialenost vz = new Vzdialenost();
//        //vz.setVzdialenostCas(start, "48.9377105,18.1416429");
//        String[][] p = vz.getzdialenostiCasy(start, "48.9377105,18.1416429");
//        System.out.println("**********************");
//        System.out.println("" + p[0][0]);
//        System.out.println("" + p[0][1]);
//        System.out.println("" + p[0][2]);
//        System.out.println("" + p[0][3]);
//        System.out.println("**********************");
//        System.out.println("" + p[1][0]);
//        System.out.println("" + p[1][1]);
//        System.out.println("" + p[1][2]);
//        System.out.println("" + p[1][3]);
//        System.out.println("**********************");
//        System.out.println("" + p[2][0]);
//        System.out.println("" + p[2][1]);
//        System.out.println("" + p[2][2]);
//        System.out.println("" + p[2][3]);
//        System.out.println("**********************");
//        //System.out.println(vz.getCas() + "\n" + vz.getVzdialenost());
//    }
    /**
     * Metóda pomocou ktorej som testoval, pre beh programu nie je potrebná
     * @param poleStanic
     * @param ciel
     * @throws MalformedURLException
     * @throws IOException
     * @throws ParseException 
     */
//        public void setVzdialenostCas(String[] poleStanic, String ciel) throws MalformedURLException, IOException, ParseException {
//        String stanice = "";
//        for (int i = 0; i < poleStanic.length; i++) {
//            stanice += poleStanic[i] + "|";
//        }
//        stanice = (String) stanice.substring(0, stanice.length() - 1);
//        //System.out.print(stanice);
//        String urls = "https://maps.googleapis.com/maps/api/distancematrix/"
//                + "json?origins="
//                + stanice
//                + "&destinations="
//                + ciel
//                + "&mode=driving"
//                + "&language=sk-SK"
//                + "&key=AIzaSyChgYn2IO8qL1iZXuyC4Ly7wn5DKmmCIVY";
//        URL url = new URL(urls);
//        JOptionPane.showMessageDialog(null, urls);
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        String formattedAddress = "";
//        try {
//            InputStream in = url.openStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            String result, line = reader.readLine();
//            result = line;
//            while ((line = reader.readLine()) != null) {
//                result += line;
//            }
//            JSONParser parser = new JSONParser();
//            JSONObject jb = (JSONObject) parser.parse(result);
//            JSONArray rows = (JSONArray) jb.get("rows");
//            JSONArray origin_addresses = (JSONArray) jb.get("origin_addresses");
//            JSONArray destination_addresses = (JSONArray) jb.get("destination_addresses");
//
//            int k = rows.size();
//            int i = 0;
//            String[][] pole = new String[k][4];
//            while (i < k) {
//                JSONObject row_i = (JSONObject) rows.get(i);
//                JSONObject json_row_i = (JSONObject) parser.parse(row_i.toString());
//                JSONArray elements = (JSONArray) json_row_i.get("elements");
//                JSONObject element0 = (JSONObject) elements.get(0);
//                JSONObject json_element0 = (JSONObject) parser.parse(element0.toString());
//                JSONObject duration = (JSONObject) json_element0.get("duration");
//                JSONObject distance = (JSONObject) json_element0.get("distance");
//                pole[i][0] = destination_addresses.get(0).toString();
//                pole[i][1] = origin_addresses.get(i).toString();
//                pole[i][2] = duration.get("text").toString();
//                pole[i][3] = distance.get("text").toString();
//                i++;
//            }
//            /**
//             * System.out.println("**********************");
//             * System.out.println("" + pole[0][0]); System.out.println("" +
//             * pole[0][1]); System.out.println("" + pole[0][2]);
//             * System.out.println("" + pole[0][3]);
//             * System.out.println("**********************");
//             * System.out.println("" + pole[1][0]); System.out.println("" +
//             * pole[1][1]); System.out.println("" + pole[1][2]);
//             * System.out.println("" + pole[1][3]);
//             * System.out.println("**********************");
//             * System.out.println("" + pole[2][0]); System.out.println("" +
//             * pole[2][1]);
//             *
//             */
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null,e.getMessage() + e.getClass().getCanonicalName());
//        } finally {
//            urlConnection.disconnect();
//        }
//    }
}
