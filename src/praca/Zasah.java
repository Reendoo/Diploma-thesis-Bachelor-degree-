/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package praca;

/**
 *Trieda reprezentujuca zásah a všetko okolo neho
 * @author Peter Rendek
 */
public class Zasah {

    private int id_zasahu;
    private String id_posadky_spz;
    private int id_pacienta;
    private String rod_cislo;
    private String info_O_zasahu;
    private String info_o_pacientovi;
    private String mesto_posadky;
    private String mesto_zasahu;
    private String stav_posadky;
    private int id_zariadenia;
    private String pacient;
    private String zariadenie;
    private String telefon;
    private String adresa;
    private String stav;

    /**
     *Kostruktor zasah
     */
    public Zasah(int id_zasahu, String id_posadky_spz, String stav_posadky,
            String zariadenie, String pacient, String telefon, String adresa,
            String info_o_pacientovi, String stav, int id_pacienta, String rod_cislo) {
        this.id_zasahu = id_zasahu;
        this.id_posadky_spz = id_posadky_spz;
        this.stav_posadky = stav_posadky;
        this.zariadenie = zariadenie;
        this.pacient = pacient;
        this.telefon = telefon;
        this.adresa = adresa;
        this.info_o_pacientovi = info_o_pacientovi;
        this.stav = stav;
        this.id_pacienta = id_pacienta;
        this.rod_cislo = rod_cislo;
        

    } 
    /**
     * getter na pacienta
     * @return 
     */
    public String getPacient() {
        return pacient;
    }
    /**
     * setter na pacienta
     * @param pacient 
     */
    public void setPacient(String pacient) {
        this.pacient = pacient;
    }
    /**
     * getter na zdravotnice zariadenie
     * @return 
     */
    public String getZariadenie() {
        return zariadenie;
    }
    /**
     * setter na zdravotnice zariadenie
     * @param zariadenie 
     */
    public void setZariadenie(String zariadenie) {
        this.zariadenie = zariadenie;
    }
    
    /**geter na telefon     * 
     * @return 
     */
    public String getTelefon() {
        return telefon;
    }
    /**
     * setter na telefon
     * @param telefon 
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    /**
     * getter adresa
     * @return 
     */
    public String getAdresa() {
        return adresa;
    }
    /**
     * setter adresa
     * @param adresa 
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    /**
     * getter na stav zasahu
     * @return 
     */
    public String getStav() {
        return stav;
    }
    /**
     * setter na stav_zashu
     * @param stav 
     */
    public void setStav(String stav) {
        this.stav = stav;
    }
    /**
     * kostruktor na zasah
     * @param id_zasahu
     * @param mesto_posadky
     * @param id_posadky_spz
     * @param stav_posadky
     * @param info_O_zasahu
     * @param mesto_zasahu
     * @param id_pacienta
     * @param rod_cislo
     * @param info_o_pacientovi
     * @param id_zariadenia
     * @param stav 
     */
    public Zasah(int id_zasahu, String mesto_posadky, String id_posadky_spz, String stav_posadky, String info_O_zasahu,
            String mesto_zasahu, int id_pacienta, String rod_cislo, String info_o_pacientovi, int id_zariadenia, String stav
    ) {
        this.id_zasahu = id_zasahu;
        this.id_posadky_spz = id_posadky_spz;
        this.id_pacienta = id_pacienta;
        this.rod_cislo = rod_cislo;
        this.info_O_zasahu = info_O_zasahu;
        this.info_o_pacientovi = info_o_pacientovi;
        this.mesto_posadky = mesto_posadky;
        this.mesto_zasahu = mesto_zasahu;
        this.stav_posadky = stav_posadky;
        this.id_zariadenia = id_zariadenia;
        this.stav = stav; // nove
    }
    /**
     * getter id_zasahu
     * @return 
     */
    public int getId_zasahu() {
        return id_zasahu;
    }
   /**getter id_posadky_spz
    * 
    * @return 
    */    
    public String getId_posadky_spz() {
        return id_posadky_spz;
    }
    /**
     * getter na id_pacienta
     * @return 
     */
    public int getId_pacienta() {
        return id_pacienta;
    }
    /**
     * getter na rodne_cislo
     * @return 
     */
    public String getRod_cislo() {
        return rod_cislo;
    }
    /**
     * getter na info o zasahu
     * @return 
     */
    public String getInfo_O_zasahu() {
        return info_O_zasahu;
    }
    /**
     * getter na info o pacientovi
     * @return 
     */
    public String getInfo_o_pacientovi() {
        return info_o_pacientovi;
    }
    /**
     * getter na mesto posadky
     * @return 
     */
    public String getMesto_posadky() {
        return mesto_posadky;
    }

    public String getMesto_zasahu() {
        return mesto_zasahu;
    }
    /**
     * getter na stav_posadky
     * @return 
     */
    public String getStav_posadky() {
        return stav_posadky;
    }
    /**
     * getter na id_zariadenia
     * @return 
     */
    public int getId_zariadenia() {
        return id_zariadenia;
    }
    /**
     * konstruktor
     * @param id_zasahu_
     * @param id_posadky_spz
     * @param id_pacienta
     * @param rod_cislo
     * @param info_O_zasahu
     * @param info_o_pacientovi
     * @param stav_posadky 
     */
    public Zasah(int id_zasahu_, String id_posadky_spz, int id_pacienta, String rod_cislo, String info_O_zasahu, String info_o_pacientovi, String stav_posadky) {
        this.id_zasahu = id_zasahu_;
        this.id_posadky_spz = id_posadky_spz;
        this.id_pacienta = id_pacienta;
        this.rod_cislo = rod_cislo;
        this.info_O_zasahu = info_O_zasahu;
        this.info_o_pacientovi = info_o_pacientovi;
        this.stav_posadky = stav_posadky;

    }
    /**
     * metoda to String na vypis zasahu
     * @return 
     */
    @Override
    public String toString() {
        return "Zasah{" + "id_zasahu=" + id_zasahu + ", id_posadky_spz=" + id_posadky_spz + ", id_pacienta=" + id_pacienta + ", rod_cislo=" + rod_cislo + ", info_O_zasahu=" + info_O_zasahu + ", info_o_pacientovi=" + info_o_pacientovi + ", mesto_posadky=" + mesto_posadky + ", mesto_zasahu=" + mesto_zasahu + ", stav_posadky=" + stav_posadky + ", id_zariadenia=" + id_zariadenia + '}';
    }

}
