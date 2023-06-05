/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package praca;

/**
 *
 * @author Peter Rendek
 */
public class ZasahovyFormular {
    private String telefon;
    private String meno;
    private String priezvisko;
    private String rod_cislo;
    private String ulica;
    private String popisCislo;
    private String obec;
    private String pPDiagnoza;
    private String infoPacient;
    private String infoZasah;
    private String konecnaDiagnoza;
    private String nemocnica;
    private String id_zasahu;

    public ZasahovyFormular() {
    }
    
    
    
    public ZasahovyFormular(String telefon, String meno, String priezvisko, String rod_cislo, String ulica, String popisCislo, String obec, String pPDiagnoza, String infoPacient, String infoZasah, String konecnaDiagnoza, String nemocnica, String id_zasahu) {
        this.telefon = telefon;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.rod_cislo = rod_cislo;
        this.ulica = ulica;
        this.popisCislo = popisCislo;
        this.obec = obec;
        this.pPDiagnoza = pPDiagnoza;
        this.infoPacient = infoPacient;
        this.infoZasah = infoZasah;
        this.konecnaDiagnoza = konecnaDiagnoza;
        this.nemocnica = nemocnica;
        this.id_zasahu = id_zasahu;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public void setPriezvisko(String priezvisko) {
        this.priezvisko = priezvisko;
    }

    public String getRod_cislo() {
        return rod_cislo;
    }

    public void setRod_cislo(String rod_cislo) {
        this.rod_cislo = rod_cislo;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getPopisCislo() {
        return popisCislo;
    }

    public void setPopisCislo(String popisCislo) {
        this.popisCislo = popisCislo;
    }

    public String getObec() {
        return obec;
    }

    public void setObec(String obec) {
        this.obec = obec;
    }

    public String getpPDiagnoza() {
        return pPDiagnoza;
    }

    public void setpPDiagnoza(String pPDiagnoza) {
        this.pPDiagnoza = pPDiagnoza;
    }

    public String getInfoPacient() {
        return infoPacient;
    }

    public void setInfoPacient(String infoPacient) {
        this.infoPacient = infoPacient;
    }

    public String getInfoZasah() {
        return infoZasah;
    }

    public void setInfoZasah(String infoZasah) {
        this.infoZasah = infoZasah;
    }

    public String getKonecnaDiagnoza() {
        return konecnaDiagnoza;
    }

    public void setKonecnaDiagnoza(String konecnaDiagnoza) {
        this.konecnaDiagnoza = konecnaDiagnoza;
    }

    public String getNemocnica() {
        return nemocnica;
    }

    public void setNemocnica(String nemocnica) {
        this.nemocnica = nemocnica;
    }

    public String getId_zasahu() {
        return id_zasahu;
    }

    public void setId_zasahu(String id_zasahu) {
        this.id_zasahu = id_zasahu;
    }
   
    
    
    
    
    
    

}
