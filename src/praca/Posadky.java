/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package praca;

/**
 *Trieda reprezentujúca posádku Záchranky
 * @author Peter Rendek
 */
public class Posadky {
    /**
     * Koštruktor posadky
     */
    public Posadky() {
    }
    /**
     * Koštruktor posadky
     * @param id_posadky_spz - spz
     * @param typ_posadky   -typ
     * @param stav_posadky  -stav_posadky
     * @param mesto_posadky -adresa posadky
     * @param vzdialenost -vzdialenost od stanice posadky k miestu zasahu
     */
    public Posadky(String id_posadky_spz, String typ_posadky, String stav_posadky, String mesto_posadky, float vzdialenost) {
        this.id_posadky_spz = id_posadky_spz;
        this.typ_posadky = typ_posadky;
        this.stav_posadky = stav_posadky;
        this.mesto_posadky = mesto_posadky;
        this.vzdialenost = vzdialenost;

    }
    /**
     * Koštruktor posadky
     * @param id_posadky_spz - spz
     * @param typ_posadky   -typ
     * @param stav_posadky  -stav_posadky     * 
     */
    public Posadky(String id_posadky_spz, String typ_posadky, String stav_posadky) {
        this.id_posadky_spz = id_posadky_spz;
        this.typ_posadky = typ_posadky;
        this.stav_posadky = stav_posadky;
    }

    private String id_posadky_spz;
    private int id_zamestnanca_vodic;
    private int id_zamestnanca_lekar;
    private int id_zamestnanca_zachranar;
    private String typ_posadky;
    private String stav_posadky;
    private float vzdialenost;
    private String mesto_posadky;
    private String vodic;
    private String gg; // reťazec reprezentujúcu ][vzdialenost/cas] od miesta posadky k miestu zasahu z GOOGLEAPI

   /**
    * setter na GG -
    * @param gg reťazec reprezentujúcu ][vzdialenost/cas] od miesta posadky k miestu zasahu z GOOGLEAPI
    */
    public void setGg(String gg) {
        this.gg = gg;
    }
    /**
     * Getter na GG
     * @return gg reťazec reprezentujúcu ][vzdialenost/cas] od miesta posadky k miestu zasahu z GOOGLEAPI
     */
    public String getGg() {
        return gg;
    }
    
    
    /**
     * Koštruktor posadky
     * @param id_posadky_spz-
     * @param typ_posadky-
     * @param vodic
     * @param zachranar
     * @param lekar
     * @param stav_posadky 
     */
    public Posadky(String id_posadky_spz, String typ_posadky,String vodic,String zachranar,String lekar, String stav_posadky) {
        this.id_posadky_spz = id_posadky_spz;
        this.typ_posadky = typ_posadky;
        this.stav_posadky = stav_posadky;
        this.vodic = vodic;
        this.lekar = lekar;
        this.zachranar = zachranar;
    }
    /**
     * Getter na vodica
     * @return vodic
     */
    public String getVodic() {
        return vodic;
    }
    /**
     * seter na vodica
     * @param vodic 
     */
    public void setVodic(String vodic) {
        this.vodic = vodic;
    }
    /**
     * Getter na lekara
     * @return ôlekara
     */
    public String getLekar() {
        return lekar;
    }
    /**
     * Setter na lekara
     * @return lekar
     */
    public void setLekar(String lekar) {
        this.lekar = lekar;
    }
    /**
     * Getter zachranar
     * @return zachranar
     */
    public String getZachranar() {
        return zachranar;
    }
    /**
     * setter zachranar
     * @param zachranar -zachranar
     */
    public void setZachranar(String zachranar) {
        this.zachranar = zachranar;
    }
    
    private String lekar;
    private String zachranar;
    /**
     * Kostruktor posadky
     * @param id_posadky_spz
     * @param id_zamestnanca_vodic
     * @param id_zamestnanca_lekar
     * @param id_zamestnanca_zachranar
     * @param typ_posadky
     * @param stav_posadky
     * @param vzdialenost
     * @param mesto_posadky 
     */
    public Posadky(String id_posadky_spz, int id_zamestnanca_vodic, int id_zamestnanca_lekar, int id_zamestnanca_zachranar, String typ_posadky, String stav_posadky, float vzdialenost, String mesto_posadky) {
        this.id_posadky_spz = id_posadky_spz;
        this.id_zamestnanca_vodic = id_zamestnanca_vodic;
        this.id_zamestnanca_lekar = id_zamestnanca_lekar;
        this.id_zamestnanca_zachranar = id_zamestnanca_zachranar;
        this.typ_posadky = typ_posadky;
        this.stav_posadky = stav_posadky;
        this.vzdialenost = vzdialenost;
        this.mesto_posadky = mesto_posadky;
    }
    /**
     * Kostruktor posadky
     * @param id_posadky_spz
     * @param id_zamestnanca_vodic
     * @param id_zamestnanca_lekar
     * @param id_zamestnanca_zachranar
     * @param typ_posadky
     * @param stav_posadky
     * @param vzdialenost
     * @param mesto_posadky 
     */
    public Posadky(String id_posadky_spz, String typ_posadky, String stav_posadky, String mesto_posadky) {
        this.id_posadky_spz = id_posadky_spz;
        this.typ_posadky = typ_posadky;
        this.stav_posadky = stav_posadky;
        this.mesto_posadky = mesto_posadky;
    }
    /**
     * getter na id_posadky_spz
     * @return 
     */
    public String getId_posadky_spz() {
        return id_posadky_spz;
    }
    /**
     * getter na id_vodica
     * @return 
     */
    public int getId_zamestnanca_vodic() {
        return id_zamestnanca_vodic;
    }
     /**
     * getter na id_lekara
     * @return 
     */
    public int getId_zamestnanca_lekar() {
        return id_zamestnanca_lekar;
    }
     /**
     * getter na id_zachranar
     * @return 
     */
    public int getId_zamestnanca_zachranar() {
        return id_zamestnanca_zachranar;
    }
    /**
     * getter na typ posadky
     * @return  typ posadky
     */
    public String getTyp_posadky() {
        return typ_posadky;
    }
    /**
     * 
     *getter stav Posadky
     * @return stav posadky
     */
    public String getStav_posadky() {
        return stav_posadky;
    }
    /**
     * getter na vzdiatelost
     * @return vzdialenost od miesta posadky k miestu zasahu
     */
    public float getVzdialenost() {
        return vzdialenost;
    }
    /**
     * getter na mesto zasahu
     * @return mesto zasahu
     */
    public String getMesto_posadky() {
        return mesto_posadky;
    }

}
