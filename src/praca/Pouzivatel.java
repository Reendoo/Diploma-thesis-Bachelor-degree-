/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package praca;

/**
 *Trieda reprezentujuca pouzivatel
 * @author Peter Rendek
 */
public class Pouzivatel {
     private String prac_pozicia; 
     private int id_zamestnavatela;
     private String login;   
     private int id_zamestnanca;
     private String stav_zamestnanca;
/**
 * Kostruktor na objekt pouzivatel
 * @param id_zamestnavatela - id_zamestnavatela
 * @param id_zamestnanca -zamestnanca
 */
    public Pouzivatel(int id_zamestnavatela, int id_zamestnanca) {
        this.id_zamestnavatela = id_zamestnavatela;
        this.id_zamestnanca = id_zamestnanca;
    }
    
    /**
     * Kostruktor Pouzivatel
     */ 
    public Pouzivatel() {       
    }
    /**
     * getter na pracvonu poziciu
     * @return 
     */
    public String getPrac_pozicia() {
        return prac_pozicia;
    }
    /**
     * setter na prac poziciu
     * @param prac_pozicia 
     */
    public void setPrac_pozicia(String prac_pozicia) {
        this.prac_pozicia = prac_pozicia;
    }
    /**
     * getter na id_zamestnavatela
     * @return 
     */
    public int getId_zamestnavatela() {
        return id_zamestnavatela;
    }
    /**
     * setter na zamestnavatela
     * @param id_zamestnavatela 
     */
    public void setId_zamestnavatela(int id_zamestnavatela) {
        this.id_zamestnavatela = id_zamestnavatela;
    }
    /**
     * getter na login
     * @return 
     */
    public String getLogin() {
        return login;
    }
    /**
     * setter na login
     * @param login 
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /***
     * geter na id_zamestnanca
     * @return 
     */
    public int getId_zamesnanca() {
        return id_zamestnanca;
    }
    /**
     * setter na id_zamestnanca
     * @param id_zamesnanca 
     */
    public void setId_zamesnanca(int id_zamesnanca) {
        this.id_zamestnanca = id_zamesnanca;
    }
    /**
     * getter na stav_zamestnanca
     * @return 
     */
    public String getStav_zamestnanca() {
        return stav_zamestnanca;
    }
    /**
     * setter na stav_zamestnanca
     * @param stav_zamestnanca 
     */
    public void setStav_zamestnanca(String stav_zamestnanca) {
        this.stav_zamestnanca = stav_zamestnanca;
    } 
   /**
    * kostruktor
    * @param id_zamestnavatela
    * @param login
    * @param id_zamestnanca 
    */ 
    public Pouzivatel(int id_zamestnavatela, String login, int id_zamestnanca) {
        this.id_zamestnavatela = id_zamestnavatela;
        this.login = login;
        this.id_zamestnanca = id_zamestnanca;        
    }
    /**
     * setter id_zamestnanca
     * @param id_zamestnanca 
     */
    public Pouzivatel(int id_zamestnanca) {
        this.id_zamestnanca = id_zamestnanca;
    }
    
   
   
    
            

   
    
    
}
