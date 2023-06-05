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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *Trieda slúžia na ma vytváranie objektov Adresa a komunikáciu s GoogleMaps Rozhraniami
 * 
 * @author Peter Rendek
 */
public class Adresa {

    private String adresa_G_nazov;
    private String lat;
    private String lng;
    private String place_id1;

    /**
     * Konštruktor na objekt adresa
     * @param adresa - adresa
     * @param lat   -zemepisna sirka  
     * @param lng  - dlzka
     * @param place_id -  id_miesta
     */
    public Adresa(String adresa, String lat, String lng, String place_id) {
        this.adresa_G_nazov = adresa;
        this.lat = lat;
        this.lng = lng;
        this.place_id1 = place_id;
    }
    /**
     * Konštruktor na objekt adresa
     */ 
    public Adresa() {
    }
    /**
     * ToSting Adresy
     * @return 
     */
    @Override
    public String toString() {
        return "Adresa{" + "adresa_G_nazov=" + adresa_G_nazov + ", lat=" + lat + ", lng=" + lng + ", place_id1=" + place_id1 + '}';
    }
    /**
     * Geter na adresu
     * @return adresa
     */
    public String getAdresa_G_nazov() {
        return adresa_G_nazov;
    }
    /**
     * seter na adresu
     * @param adresa_G_nazov -adresa
     */
    public void setAdresa_G_nazov(String adresa_G_nazov) {
        this.adresa_G_nazov = adresa_G_nazov;
    }
    /**
     * Getez na sirku
     * @return sirka
     */
    public String getLat() {
        return lat;
    }
    /**
     * Setter na sirku
     * @param lat sirka
     */
    public void setLat(String lat) {
        this.lat = lat;
    }
    /**
     * geter na dlzku
     * @return dlzka
     */
    public String getLng() {
        return lng;
    }
    /**
     * setter na dlzku
     * @param lng  dlzka
     */
    public void setLng(String lng) {
        this.lng = lng;
    }
    /**
     * Zistenie Adresa na zaklada gps suradnic - pouzita pri vývoji SW pre beh programu nie je potrebná 
     * @param lat sirka
     * @param lng dlzka
     * @throws MalformedURLException
     * @throws IOException
     * @throws ParseException 
     */
    public void setAddressByGpsCoordinates(String lat, String lng)
            throws MalformedURLException, IOException, ParseException {
        String urls = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + lat + "," + lng
                // +"48.860283,18.307791"
                + "&result_type=street_address|route"
                + "&language=sk-SK"
                + "region=sk"
                + "&key=AIzaSyChgYn2IO8qL1iZXuyC4Ly7wn5DKmmCIVY";
        //System.out.println(urls);        
        URL url = new URL(urls);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String formattedAddress = "";

        try {
            InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result, line = reader.readLine();
            result = line;
            while ((line = reader.readLine()) != null) {
                result += line;
                //System.out.println(line);
            }

            JSONParser parser = new JSONParser();
            JSONObject rsp = (JSONObject) parser.parse(result);

            JSONArray matches = (JSONArray) rsp.get("results");
            JSONObject data = (JSONObject) matches.get(0); //TODO: check if idx=0 exists
            formattedAddress = (String) data.get("formatted_address");
            this.adresa_G_nazov = formattedAddress = (String) data.get("formatted_address");
            this.place_id1 = data.get("place_id").toString();
            this.lat = lat;
            this.lng = lng;

        } finally {
            urlConnection.disconnect();

        }
    }
    /**
     * Metodá na nastavenie súradnic objektu adresa na základe adresy
     * @param adr 
     */
    public void setLocationsByAdress(String adr){//throws MalformedURLException, IOException, ParseException {
         String urls = "https://maps.googleapis.com/maps/api/geocode/json?"
                + "address=!"
                + adr
                //+ "Kino+Panorex+Nova+Dubnica"
                + "&key=AIzaSyChgYn2IO8qL1iZXuyC4Ly7wn5DKmmCIVY";
        urls=urls.replaceAll("(?i)%(?![\\da-f]{2})", "%25");
        //URL url = new URL(urls);
        //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String formattedAddress = "";
        String lat = "";
        String lng = "";
        String place_id = "";

        try {
            URI uri = new URI(urls); 
            String request = uri.toASCIIString();            
            String[][] pole = null;
            URL myURL = new URL(request);
            //HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
            InputStream in = myURL.openStream();            
            //InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result, line = reader.readLine();
            result = line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                result += line;
            }
            //rozparsovanie json-a
            JSONParser parser = new JSONParser();
            JSONObject jb = (JSONObject) parser.parse(result);
            JSONArray jsonObject1 = (JSONArray) jb.get("results");
            String test =jb.get("status").toString();
            if(test.equals("ZERO_RESULTS"))
            {
               this.lat =""+14.352953;  
               this.lng =""+-14.894135;               
               return;
            }
            JSONObject jsonObject2 = (JSONObject) jsonObject1.get(0);
            JSONObject jsonObject3 = (JSONObject) jsonObject2.get("geometry");
            JSONObject location = (JSONObject) jsonObject3.get("location");

            this.place_id1 = jsonObject2.get("place_id").toString();
            this.adresa_G_nazov = jsonObject2.get("formatted_address").toString();
            this.lat = location.get("lat").toString();
            this.lng = location.get("lng").toString();

            System.out.println("Lat = " + location.get("lat"));
            System.out.println("Lng = " + location.get("lng"));
            //System.out.println(place_id);
            //System.out.println(formattedAddress);
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage() + e.getClass().getCanonicalName());
            return; 
        }  
        finally {
            //urlConnection.disconnect();

        }
    }

    /**
     * metóda využitá pri tvorení SW-pre prácu na 
     * @param args
     * @throws IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        //String r;
        //r = getAddressByGpsCoordinates("48.966618", "18.146232");
        //r = getLocationsByAdress("gdsgsg");
        //System.out.println(r);
        Adresa map1 = new Adresa();
        map1.setAddressByGpsCoordinates("48.860283", "18.307791");
        Adresa map2 = new Adresa();
        map2.setLocationsByAdress("Kino+Panorex+Nova+Dubnica");
        System.out.println(map1.toString());
        System.out.println(map2.toString());

    }

}
