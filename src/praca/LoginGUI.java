/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package praca;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.parser.ParseException;

/**
 * Trieda Zabezpečujúca prepojenie graficky s funkcionalitou
 *
 * @author Peter Rendek
 */
public class LoginGUI extends javax.swing.JFrame {

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement stored_pro = null;
    int zamestnanec;
    int zamestnavatel;
    int info;

    /**
     * Kontruktor
     */
    public LoginGUI() {
        initComponents();
        naplCBJNemocnice();
        naplCBJTYPDiagnozy();
        naplCBJZachranky();
        jFOpravZasah.setLocationRelativeTo(null);
        JFNemocnicaGUI.setLocationRelativeTo(null);
        //jFNemocnicaZasahOkno.setLocationRelativeTo(null);
        JFZachrankaVelitel.setLocationRelativeTo(null);
        //JFLogin.setVisible(false);
        JFLogin.setLocationRelativeTo(null);
        jCB_RLP.setVisible(false);
        jCB_RZP.setVisible(false);
        jCB_RLP.setSelected(false);
        jCB_RZP.setSelected(false);
        jDateChooserOD.setVisible(false);
        jDateChooserDO.setVisible(false);
        jOd.setVisible(false);
        jDo.setVisible(false);
        jCB_NZ_typDiagnozy1.setVisible(false);
        jCB_FOP_Nemocnice1.setVisible(false);
        jCB_FOP_Zachranky.setVisible(false);
        jCB_NZ_diagnozy1.setVisible(false);
        jTextField1.setVisible(false);
        jTextField2.setVisible(false);
        jTextField3.setVisible(false);
        jTextField4.setVisible(false);
        jTextField5.setVisible(false);
        jTextField6.setVisible(false);
        jTextField7.setVisible(false);
        jTextField8.setVisible(false);
        jTextField9.setVisible(false);
        jTextField1.setText("Názov");
        jTextField2.setText("Obec");
        jTextField3.setText("Ulica");
        jTextField4.setText("Popisné číslo");
        jTextField5.setText("Meno Šéfa");
        jTextField6.setText("Priezvisko");
        jTextField7.setText("Rodné číslo formát:\"XXXXXX/XXXX\"");
        jTextField8.setText("telefón1");
        jTextField9.setText("telefón2");
        jBVytvorZachranku.setVisible(false);
        jTextField10.setVisible(false);
        jTextField11.setVisible(false);
        jTextField12.setVisible(false);
        jTextField13.setVisible(false);
        jTextField14.setVisible(false);
        jTextField15.setVisible(false);
        jTextField16.setVisible(false);
        jTextField10.setText("Meno");
        jTextField11.setText("Priezvisko");
        jTextField12.setText("Rodné číslo formát XXXXXX/XXXX");
        jTextField13.setText("Číslo OP");
        jTextField14.setText("Obec");
        jTextField15.setText("Ulica");
        jTextField16.setText("Popisné číslo");
        jB_VZ_uprav_zamestnanca.setText("Uprav Zamestnanca");

        // vyhladavanie real-time pre obce
        jCB_NZ_obce.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                String pom = jCB_NZ_obce.getEditor().getItem().toString();
                if (evt.getKeyCode() >= 65 && evt.getKeyCode() <= 90
                        || evt.getKeyLocation() > 96 && evt.getKeyCode() <= 105
                        || evt.getKeyCode() == 8) {
                    jCB_NZ_obce.setModel(getNazvyObce(pom));
                    if (jCB_NZ_obce.getItemCount() > 0) {
                        jCB_NZ_obce.showPopup();
                        if (evt.getKeyCode() != 8) {
                            ((JTextComponent) jCB_NZ_obce.getEditor().getEditorComponent())
                                    .select(pom.length(), jCB_NZ_obce.getEditor().getItem().toString().length());
                        } else {
                            jCB_NZ_obce.getEditor().setItem(pom);
                        }
                    } else {
                        jCB_NZ_obce.addItem(pom);
                    }
                }
            }
        });
        // vyhladavanie real-time pre obce
        jCB_OZ_obce.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                String pom = jCB_OZ_obce.getEditor().getItem().toString();
                if (evt.getKeyCode() >= 65 && evt.getKeyCode() <= 90
                        || evt.getKeyLocation() > 96 && evt.getKeyCode() <= 105
                        || evt.getKeyCode() == 8) {
                    jCB_OZ_obce.setModel(getNazvyObce(pom));
                    if (jCB_OZ_obce.getItemCount() > 0) {
                        jCB_OZ_obce.showPopup();
                        if (evt.getKeyCode() != 8) {
                            ((JTextComponent) jCB_OZ_obce.getEditor().getEditorComponent()).select(pom.length(), jCB_OZ_obce.getEditor().getItem().toString().length());
                        } else {
                            jCB_OZ_obce.getEditor().setItem(pom);
                        }
                    } else {
                        jCB_OZ_obce.addItem(pom);
                    }
                }
            }
        });
        // vyhladavanie real-time pre diagnozy
        jCB_NZ_diagnozy.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                String pom = jCB_NZ_diagnozy.getEditor().getItem().toString();
                if (evt.getKeyCode() >= 65 && evt.getKeyCode() <= 90
                        || evt.getKeyLocation() > 96 && evt.getKeyCode() <= 105
                        || evt.getKeyCode() == 8) {
                    jCB_NZ_diagnozy.setModel(getDiagnozy(pom, jCB_NZ_typDiagnozy.getSelectedItem().toString()));
                    if (jCB_NZ_diagnozy.getItemCount() > 0) {
                        jCB_NZ_diagnozy.showPopup();
                        if (evt.getKeyCode() != 8) {
                            ((JTextComponent) jCB_NZ_diagnozy.getEditor().getEditorComponent()).select(pom.length(), jCB_NZ_diagnozy.getEditor().getItem().toString().length());
                        } else {
                            jCB_NZ_diagnozy.getEditor().setItem(pom);
                        }
                    } else {
                        jCB_NZ_diagnozy.addItem(pom);
                    }
                }
            }
        });
        // vyhladavanie real-time pre diagnozy
        jCB_NZ_diagnozy1.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                String pom = jCB_NZ_diagnozy1.getEditor().getItem().toString();
                if (evt.getKeyCode() >= 65 && evt.getKeyCode() <= 90 || evt.getKeyLocation() > 96 && evt.getKeyCode() <= 105 || evt.getKeyCode() == 8) {
                    jCB_NZ_diagnozy1.setModel(getDiagnozy(pom, "všetky"));
                    if (jCB_NZ_diagnozy1.getItemCount() > 0) {
                        jCB_NZ_diagnozy1.showPopup();
                        if (evt.getKeyCode() != 8) {
                            ((JTextComponent) jCB_NZ_diagnozy1.getEditor().getEditorComponent()).select(pom.length(), jCB_NZ_diagnozy1.getEditor().getItem().toString().length());
                        } else {
                            jCB_NZ_diagnozy1.getEditor().setItem(pom);
                        }
                    } else {
                        jCB_NZ_diagnozy1.addItem(pom);
                    }
                }
            }
        });

        JFNemocnicaGUI.setVisible(false);
        jFOknoOperator.setVisible(false);

    }

    /**
     * Metóda slúžiaca na naplnenie JcomboboxovNemocníc
     */
    public void naplCBJNemocnice() {
        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            rs = st.executeQuery("select nazov_zariadenia from zdravotnicke_zariadenie");
            while (rs.next()) {
                jCB_FOP_Nemocnice.addItem(rs.getString(1));
                jCBNemocnice1.addItem(rs.getString(1));
                jCBNemocnice2.addItem(rs.getString(1));
                jCB_FOP_Nemocnice1.addItem(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Metóda slúžiaca na naplnenie JcomboboxovTypDiagnoz
     */
    public void naplCBJTYPDiagnozy() {
        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            rs = st.executeQuery("select typ from diagnozy \n"
                    + "group by typ\n"
                    + " ");
            jCB_NZ_typDiagnozy.addItem("všetky");
            while (rs.next()) {
                //jCB_NZ_typDiagnozy.addItem("všetky"); 
                jCB_NZ_typDiagnozy.addItem(rs.getString(1));
                jCB_NZ_typDiagnozy1.addItem(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Metóda, ktorá vráti zoznam názvov obci z Databázy podľa podreťazca
     * začiatku názvu
     *
     *
     * @param str podreťazec začiatku názvu obce/mesto
     * @return mod -model comboboxuObcí
     */
    public DefaultComboBoxModel getNazvyObce(String str) {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_MESTO (?)}");
            stored_pro.setString(1, str);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                mod.addElement(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return mod;
    }

    /**
     * Metóda, ktorá vráti zoznam diagnóz podľa začiatku názvu
     *
     * @param str podreťazec začiatku názvu obce/mesto
     * @param typ
     * @return mod -model comboboxuObcí
     */
    public DefaultComboBoxModel getDiagnozy(String str, String typ) {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();

        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_DIAGNOZA (?,?)}");
            stored_pro.setString(1, str);
            stored_pro.setString(2, typ);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                mod.addElement(rs.getString(1));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return mod;

    }

    /**
     * Metóda, ktorá vráti zoznam nemocníc
     *
     *
     * @return mod -model comboboxuNemocníc
     */
    public DefaultComboBoxModel getNemocnice() {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            rs = st.executeQuery("select nazov_zariadenia from zdravotnicke_zariadenie");
            while (rs.next()) {
                mod.addElement(rs.getString(1));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return mod;

    }

    /**
     * Metóda, ktorá vráti zoznam Záchranok
     *
     * @return mod -model comboboxuZáchranok
     */
    public DefaultComboBoxModel getZachranky() {
        DefaultComboBoxModel mod = new DefaultComboBoxModel();
        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            rs = st.executeQuery("select CONCAT(nazov_zachranky,'-',ob.nazov)\n"
                    + " from zachranka zach\n"
                    + " join adresa adr on (zach.id_adresy = adr.id_adresy)\n"
                    + "  join obec ob  on (adr.id_obce = ob.id_obce)");
            while (rs.next()) {
                mod.addElement(rs.getString(1));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return mod;
    }

    /**
     * Metóda, ktorá naplní Combobbox jCB_FOP_Zachranky
     */
    public void naplCBJZachranky() {
        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            rs = st.executeQuery("select CONCAT(nazov_zachranky,'-',ob.nazov)\n"
                    + " from zachranka zach\n"
                    + " join adresa adr on (zach.id_adresy = adr.id_adresy)\n"
                    + "  join obec ob  on (adr.id_obce = ob.id_obce) ");
            while (rs.next()) {
                jCB_FOP_Zachranky.addItem(rs.getString(1));
                //jCB_NZ_typDiagnozy.addItem("všetky"); 
                //jCB_NZ_typDiagnozy.addItem(rs.getString(1));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Metóda vrati arraylist všetkých posádky ziskaných z DB * zdieľana
     * procedúra PROC_SELECT_POSADKY vracia
     * id_posadky_spz,typ_posadky,stav_posadky,mesto posádky
     *
     * @return arraylist všetkých posádky
     */
    public ArrayList<Posadky> getPosadkyListZaklad() {
        ArrayList<Posadky> posadkyListZaklad = new ArrayList<Posadky>();
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_POSADKY}");
            rs = stored_pro.executeQuery();
            Posadky posadky;
            while (rs.next()) {
                posadky = new Posadky(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                posadkyListZaklad.add(posadky);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return posadkyListZaklad;
    }

    /**
     * Metóda vrati arraylist posádok Záchranky, do ktorých sa zamestnanec može
     * následnej prihlásiťB * zdieľana procedúra
     * PROC_SELECT_POSADKY_ZACHRANKY(id_zamestnavatela, id_zamestnanca)
     *
     * @param zamestnavatel
     * @param zamestnanec
     * @return arraylist posádok na prihlásenie
     */
    public ArrayList<Posadky> getPosadkyLogin(int zamestnavatel, int zamestnanec) {
        ArrayList<Posadky> posadkyListLogin = new ArrayList<Posadky>();
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_POSADKY_ZACHRANKY (?,?)}");
            stored_pro.setInt(1, zamestnavatel);
            stored_pro.setInt(2, zamestnanec);
            rs = stored_pro.executeQuery();
            Posadky posadky;
            while (rs.next()) {
                posadky = new Posadky(rs.getString(1), rs.getString(2), rs.getString(3));
                posadkyListLogin.add(posadky);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
        return posadkyListLogin;
    }

    /**
     * Metóda slúžia na zobrazenie posádok, do ktorých sa zamestnanec môže
     * príhlasiť
     *
     * @param list - posádok na prihlásenie
     */
    public void Show_posadkyLogin(ArrayList<Posadky> list) {
        DefaultTableModel model = (DefaultTableModel) jTLogin.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        model.setRowCount(0);
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId_posadky_spz();
            row[1] = list.get(i).getTyp_posadky();
            row[2] = list.get(i).getStav_posadky();
            row[3] = model.getColumnName(3);
            model.addRow(row);
        }
    }

    /**
     * Zobrazenie všetkých posadiek v okne operátora
     *
     * @param list
     */
    public void Show_Posadky_In_Table(ArrayList<Posadky> list) {
        DefaultTableModel model = (DefaultTableModel) JTablePosadky.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getMesto_posadky();
            row[1] = list.get(i).getId_posadky_spz();
            row[2] = list.get(i).getTyp_posadky();
            row[3] = list.get(i).getStav_posadky();
            //row[4] = model.getColumnName(4);
            model.addRow(row);
        }
    }

    /**
     * Metóda vráti arrylist najbližších posádiek od mesta miesta zásahu na
     * základe matice vzdialenosti
     *
     * @param pom obec
     * @return arraylist najbližších posádok
     */
    public ArrayList<Posadky> getPosadkyNovyZasah(String pom) {
        ArrayList<Posadky> posadkyList = new ArrayList<Posadky>();
        try {
            conn = DBConnection.DBConnection();
            System.out.println(pom);
            stored_pro = conn.prepareCall("{call PROC_SELECT_NAJBLIZSIE_POSADKY (?)}");
            stored_pro.setString(1, pom);
            rs = stored_pro.executeQuery();
            Posadky posadky;
            while (rs.next()) {
                posadky = new Posadky(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                posadkyList.add(posadky);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return posadkyList;
    }

    /**
     * Metóda vráti arrylist aktuákne prebiehajúcich zásahov
     *
     * @return arraylist najbližších posádok
     */
    public ArrayList<Zasah> getAktualneZasahy() {
        ArrayList<Zasah> aktualneZasahyList = new ArrayList<Zasah>();
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{CALL PROC_SELECT_AKTUALNE_ZASAHY}");
            rs = stored_pro.executeQuery();
            Zasah zasah;
            while (rs.next()) {
                zasah = new Zasah(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11));
                aktualneZasahyList.add(zasah);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return aktualneZasahyList;
    }

    /**
     *Metóda vrati arraylist zlozenia posadok zachranky
     * @param zamestnavatel
     * @return
     */
    public ArrayList<Posadky> getPosadkyZachranky(int zamestnavatel) {
        ArrayList<Posadky> aktualneZlozeniaPosadokZachranky = new ArrayList<Posadky>();
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{CALL PROC_SELECT_POSADKY_ZLOZENIA_ZACHRANKY (?)}");
            stored_pro.setInt(1, zamestnavatel);
            rs = stored_pro.executeQuery();
            Posadky pos;
            while (rs.next()) {
                pos = new Posadky(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                aktualneZlozeniaPosadokZachranky.add(pos);
            }
            //aktualneZlozeniaPosadokZachranky.add(pos = new Posadky());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return aktualneZlozeniaPosadokZachranky;
    }

    /**
     *Metoda zobrazi zlozenia posadky vybranej zachranky
     * @param list
     */
    public void ShowZlozeniaPosadkyZachranky(ArrayList<Posadky> list) {

        DefaultTableModel model = (DefaultTableModel) jTablePosadkyZachranky.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        Object[] row = new Object[7];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId_posadky_spz();
            row[1] = list.get(i).getTyp_posadky();
            row[2] = list.get(i).getVodic();
            row[3] = list.get(i).getZachranar();
            row[4] = list.get(i).getLekar();
            row[5] = list.get(i).getStav_posadky();
            row[6] = model.getColumnName(5);

            model.addRow(row);
        }

    }

    /**
     *Zobrazi aktualne zasahy v tabulke JTableAktualneZasahy
     * @param list
     */
    public void Show_aktualneZasahy(ArrayList<Zasah> list) {
        DefaultTableModel model = (DefaultTableModel) JTableAktualneZasahy.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        model.setRowCount(0);
        Object[] row = new Object[12];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId_zasahu();
            row[1] = list.get(i).getMesto_posadky();
            row[2] = list.get(i).getId_posadky_spz();
            row[3] = list.get(i).getStav_posadky();
            row[4] = list.get(i).getInfo_O_zasahu();
            row[5] = list.get(i).getMesto_zasahu();
            row[6] = list.get(i).getId_pacienta();
            row[7] = list.get(i).getRod_cislo();
            row[8] = list.get(i).getInfo_o_pacientovi();
            row[9] = list.get(i).getId_zariadenia();
            row[10] = list.get(i).getStav();
            row[11] = model.getColumnName(11);
            model.addRow(row);
        }

    }

    /**
     *
     * @param zamestnavatel
     * @return
     */
    public ArrayList<Zasah> getAktualneZasahyZachranky(int zamestnavatel) {
        ArrayList<Zasah> aktualneZasahyZachrankyList = new ArrayList<Zasah>();
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{CALL PROC_SELECT_ZASAHY_ZACHRANKY(?)}");
            stored_pro.setInt(1, zamestnavatel);
            rs = stored_pro.executeQuery();
            Zasah zasah;

            while (rs.next()) {

                zasah = new Zasah(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11));
                aktualneZasahyZachrankyList.add(zasah);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return aktualneZasahyZachrankyList;

    }

    /**
     *
     * @param list
     */
    public void Show_aktualneZasahyZachranky(ArrayList<Zasah> list) {
        DefaultTableModel model = (DefaultTableModel) JTAktualneZasahyZachranky.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        model.setRowCount(0);
        Object[] row = new Object[11];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId_zasahu();
            row[1] = list.get(i).getId_posadky_spz();
            row[2] = list.get(i).getStav_posadky();
            row[3] = list.get(i).getZariadenie();
            row[4] = list.get(i).getPacient();
            row[5] = list.get(i).getTelefon();
            row[6] = list.get(i).getAdresa();
            row[7] = list.get(i).getInfo_o_pacientovi();
            row[8] = list.get(i).getStav();
            row[9] = list.get(i).getId_pacienta();
            row[10] = list.get(i).getRod_cislo();
            //row[11] = model.getColumnName(10);
            //row[6] = model.getColumnName(10);
            model.addRow(row);
        }

    }

    /**
     * Zobrazenie najbližších posádok v tabuľke vo JF Nový zásah     *
     * @param list
     */
    public void ShowPosadkyNovyZasah(ArrayList<Posadky> list) {// throws IOException, ParseException {
        DefaultTableModel model = (DefaultTableModel) JTB_NZ_POS.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        String[] pole = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            pole[i] = list.get(i).getMesto_posadky();
            System.out.println(list.get(i).getMesto_posadky());
        }
        Vzdialenost vzd = new Vzdialenost();
        String ciel = jCB_NZ_obce.getSelectedItem().toString() + "+"
                + jTF_NZ_ulica1.getText() + "+"
                + jTF_NZ_popCis.getText();
        ciel = ciel.replace(" ", "+");
        String[][] pole1 = vzd.getzdialenostiCasy(pole, ciel);//null;
        //try {
        // pole1 = vzd.getzdialenostiCasy(pole, ciel);
//        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(null, "2" + ex.getMessage() + ex.getClass().getCanonicalName());
//           
//        } catch (ParseException ex) {
//            JOptionPane.showMessageDialog(null, "3" + ex.getMessage() + ex.getClass().getCanonicalName());
//           
//        }
//        JOptionPane.showMessageDialog(null, "List" 
//                +list.size()+"pole1" 
//                +pole1.length+"Pole:" 
//                +pole1[1].length);

        //System.out.println(pole1[1][3]);
        if (pole1 != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setGg("[" + pole1[i][3] + "/" + pole1[i][2] + "]");
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setGg("[Neznáma /Neznáma ]");
            }
        }

        Object[] row = new Object[6];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getMesto_posadky();
            row[1] = list.get(i).getId_posadky_spz();
            row[2] = list.get(i).getTyp_posadky();
            row[3] = list.get(i).getStav_posadky();
            row[4] = list.get(i).getVzdialenost();
            row[5] = list.get(i).getGg();
            model.addRow(row);
        }
    }

    /**
     *
     * @param id_zariadenia
     * @return
     */
    public ArrayList<Zasah> getZasahPreNemocnicu(int id_zariadenia) {
        ArrayList<Zasah> zasahNemocnicaList = new ArrayList<Zasah>();
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_ZASAHY_NEMOCNICE (?)}");
            stored_pro.setInt(1, id_zariadenia);
            rs = stored_pro.executeQuery();

            while (rs.next()) {

                Zasah zasah = new Zasah(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));

                //System.out.println(rs.getString(2));
                zasahNemocnicaList.add(zasah);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return zasahNemocnicaList;
    }

    /**
     *
     * @param list
     */
    public void ShowZasahNemocnice(ArrayList<Zasah> list) {
        DefaultTableModel model = (DefaultTableModel) JTabNemocnicaZasah.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        Object[] row = new Object[8];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId_zasahu();
            row[1] = list.get(i).getId_posadky_spz();
            row[2] = list.get(i).getId_pacienta();
            row[3] = list.get(i).getRod_cislo();
            row[4] = list.get(i).getInfo_O_zasahu();
            row[5] = list.get(i).getInfo_o_pacientovi();
            row[6] = list.get(i).getStav_posadky();

            row[7] = model.getColumnName(6);
            model.addRow(row);

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JFNemocnicaGUI = new javax.swing.JFrame();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTabNemocnicaZasah = new javax.swing.JTable() {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
                Component c = super.prepareRenderer(renderer, row, column);
                //nastavenie farby riadku podľa hodnoty bunky v riadku
                //stav_posadky
                if (!isRowSelected(row)) {
                    c.setBackground(getBackground());
                    int modelRow = convertRowIndexToModel(row);
                    String type = (String) getModel().getValueAt(modelRow, 6);
                    if ("NaCesteDoNemocnice".equals(type))
                    {c.setBackground(Color.red);}

                    //System.out.println(type);

                    if ("OdovzdavaniePacienta".equals(type)) {
                        c.setBackground(Color.GREEN);
                    }

                }

                return c;

            }

        };
        ;
        jB_AZ_PrijazdDoNemocnice1 = new javax.swing.JButton();
        jB_AZ_PrijazdDoNemocnice2 = new javax.swing.JButton();
        jB_AZ_PrijazdDoNemocnice3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jB_AZ_PrijazdDoNemocnice9 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        JEP_NEM_TextArea = new javax.swing.JEditorPane("text/html", "");
        jLabelPrihlasenyAko = new javax.swing.JLabel();
        jTextFieldPrihlasenyAKo = new javax.swing.JTextField();
        jLabelZdravotnickeZariadenie1 = new javax.swing.JLabel();
        jTextFieldZdravotnickeZariadenie = new javax.swing.JTextField();
        jButtonOdhlasenie = new javax.swing.JButton();
        jFOknoOperator = new javax.swing.JFrame();
        Administrácia = new javax.swing.JTabbedPane();
        jPTestovaci = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableStatistika = new javax.swing.JTable();
        jDateChooserDO = new com.toedter.calendar.JDateChooser();
        jDateChooserOD = new com.toedter.calendar.JDateChooser();
        jCB_FOP_Zachranky = new javax.swing.JComboBox<>();
        jCB_NZ_typDiagnozy1 = new javax.swing.JComboBox<>();
        jCB_NZ_diagnozy1 = new javax.swing.JComboBox<>();
        jCBZachranka = new javax.swing.JCheckBox();
        jOd = new javax.swing.JLabel();
        jDo = new javax.swing.JLabel();
        jCBDATUM = new javax.swing.JCheckBox();
        jCBTypdiagnozy = new javax.swing.JCheckBox();
        jCBNemocnice = new javax.swing.JCheckBox();
        jCB_FOP_Nemocnice1 = new javax.swing.JComboBox<>();
        jCBDiagnozy = new javax.swing.JCheckBox();
        jCBTPDAno = new javax.swing.JCheckBox();
        jCBTPDNie = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jCheckBox3 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jBVytvorZachranku = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jDateChooserDO1 = new com.toedter.calendar.JDateChooser();
        jDateChooserOD1 = new com.toedter.calendar.JDateChooser();
        jCB_FOP_Zachranky1 = new javax.swing.JComboBox<>();
        jL_NZ_TelCislo6 = new javax.swing.JLabel();
        jCB_NZ_typDiagnozy2 = new javax.swing.JComboBox<>();
        jL_NZ_TelCislo7 = new javax.swing.JLabel();
        jCB_NZ_diagnozy2 = new javax.swing.JComboBox<>();
        jCBZachranka1 = new javax.swing.JCheckBox();
        jOd1 = new javax.swing.JLabel();
        jDo1 = new javax.swing.JLabel();
        jCBDATUM2 = new javax.swing.JCheckBox();
        jPAktualneZasahy = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        JTableAktualneZasahy = new javax.swing.JTable() {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                //  Color row based on a cell value
                if (!isRowSelected(row)) {
                    c.setBackground(getBackground());
                    int modelRow = convertRowIndexToModel(row);
                    String type = (String) getModel().getValueAt(modelRow, 3);
                    String type1 = (String) getModel().getValueAt(modelRow, 10);
                    if("nový".equals(type1)) {
                        c.setBackground(Color.RED);
                    }
                    if(("prebiehajúci".equals(type1))&&("NaCesteKPacientovi".equals(type))){
                        c.setBackground(Color.ORANGE);
                    }
                    if(("prebiehajúci".equals(type1))&&("ZasahujeNaMieste".equals(type))){
                        c.setBackground(Color.GREEN);
                    }
                    if(("prebiehajúci".equals(type1))&&("NaCesteDoNemocnice".equals(type))){
                        c.setBackground(new Color(255,0,255));
                    }
                    if(("prebiehajúci".equals(type1))&&("OdovzdavaniePacienta".equals(type))){
                        c.setBackground(new Color(0,204,102));
                    }
                    if(("prebiehajúci".equals(type1))&&("NaCesteNaStanicu".equals(type))){
                        c.setBackground(new Color(0,255,255));
                    }
                    if("ukončený".equals(type1)) {
                        c.setBackground(new Color(128,128,128));
                    }
                }

                return c;

            }

        };
        ;
        jCB_FOP_Nemocnice = new javax.swing.JComboBox<>();
        jB_AZ_PrijazdDoNemocnice6 = new javax.swing.JButton();
        jB_FOP_UzavriZasah = new javax.swing.JButton();
        jB_FOP_PrijazdPosadkyKPacientovi = new javax.swing.JButton();
        jB_FOP_ZapisPrevozDoNemocnice = new javax.swing.JButton();
        jB_FOP_PrijazdDoZariadenia = new javax.swing.JButton();
        jB_FOP_zapisNavratNaStanicu = new javax.swing.JButton();
        jB_FOP_zapisVyjazdKPacientovi = new javax.swing.JButton();
        jB_FOP_PrijazdNaStanicu = new javax.swing.JButton();
        jB_AZ_PrijazdDoNemocnice16 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTablePosadky = new javax.swing.JTable() {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                //  Color row based on a cell value
                if (!isRowSelected(row)) {
                    c.setBackground(getBackground());
                    int modelRow = convertRowIndexToModel(row);
                    String type = (String) getModel().getValueAt(modelRow, 3);
                    if("MimoPrevadzky".equals(type)) {
                        c.setBackground(Color.RED);
                    }
                    if("Pripravena".equals(type)){
                        c.setBackground(Color.GREEN);
                    }
                    if("NaCesteKPacientovi".equals(type)){
                        c.setBackground(Color.ORANGE);
                    }
                    if("ZasahujeNaMieste".equals(type)){
                        c.setBackground(Color.GREEN);
                    }
                    if("NaCesteDoNemocnice".equals(type)){
                        c.setBackground(new Color(255,0,255));
                    }
                    if("OdovzdavaniePacienta".equals(type)){
                        c.setBackground(new Color(0,204,102));
                    }
                    if("NaCesteNaStanicu".equals(type)){
                        c.setBackground(new Color(0,255,255));
                    }

                }

                return c;

            }

        };
        ;
        jButton8 = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jEPHistoriaOperat = new javax.swing.JEditorPane("text/html", "");
        jB_AZ_ZobrazHistoriu = new javax.swing.JButton();
        jPNovyZasah = new javax.swing.JPanel();
        jPZasahUdaje = new javax.swing.JPanel();
        jL_NZ_meno = new javax.swing.JLabel();
        jL_NZ_TelCislo = new javax.swing.JLabel();
        jL_NZ_Priezvisko = new javax.swing.JLabel();
        jTF_NZ_priezvisko = new javax.swing.JTextField();
        jTF_NZ_meno = new javax.swing.JTextField();
        jTF_NZ_tel = new javax.swing.JTextField();
        jL_NZ_rodCis = new javax.swing.JLabel();
        jTF_NZ_rodCis = new javax.swing.JTextField();
        jL_NZ_Ulica = new javax.swing.JLabel();
        jL_NZ_PopisneCislo = new javax.swing.JLabel();
        jL_NZ_obec = new javax.swing.JLabel();
        jTF_NZ_ulica1 = new javax.swing.JTextField();
        jTF_NZ_popCis = new javax.swing.JTextField();
        jCB_NZ_obce = new javax.swing.JComboBox<>();
        jL_NZ_pacientInfo = new javax.swing.JLabel();
        jTextCAS = new javax.swing.JTextField();
        jB_NZ_HladajPosadky = new javax.swing.JButton();
        jB_NZ_vydajZasah = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTF_NZ_pacientInfo = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        JTB_NZ_POS = new javax.swing.JTable() {

            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                //  Color row based on a cell value
                if (!isRowSelected(row)) {
                    c.setBackground(getBackground());
                    int modelRow = convertRowIndexToModel(row);
                    String type = (String) getModel().getValueAt(modelRow, 3);
                    if ("Pripravena".equals(type)) {
                        c.setBackground(Color.green);
                    }
                    if ("MimoPrevadzky".equals(type)) {
                        c.setBackground(Color.RED);
                    }
                    if ("NaCesteKPacientovi".equals(type)) {
                        c.setBackground(Color.lightGray);
                    }
                    if ("ZasahujeNaMieste".equals(type)) {
                        c.setBackground(Color.CYAN);
                    }
                    if ("NaCesteDoNemocnice".equals(type)) {
                        c.setBackground(Color.pink);
                    }
                    if ("OdovzdavaniePacienta".equals(type)) {
                        c.setBackground(Color.yellow );
                    }
                    if ("NaCesteNaStanicu".equals(type)) {
                        c.setBackground(Color.ORANGE);
                    }
                }

                return c;

            }

        };
        ;
        jPanel11 = new javax.swing.JPanel();
        jL_NZ_TelCislo2 = new javax.swing.JLabel();
        jL_NZ_TelCislo3 = new javax.swing.JLabel();
        jCB_NZ_diagnozy = new javax.swing.JComboBox<>();
        jCB_NZ_typDiagnozy = new javax.swing.JComboBox<>();
        jTFOS = new javax.swing.JTextField();
        jLabelZdravotnickeZariadenie3 = new javax.swing.JLabel();
        jLabelPrihlasenyAko3 = new javax.swing.JLabel();
        JTFOSPrih = new javax.swing.JTextField();
        jButtonOdhlasenie3 = new javax.swing.JButton();
        JFLogin = new javax.swing.JFrame();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTLogin = new javax.swing.JTable();
        JBLoginFinal = new javax.swing.JButton();
        JFZachrankaVelitel = new javax.swing.JFrame();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        JTAktualneZasahyZachranky = new javax.swing.JTable() {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                //  Color row based on a cell value
                if (!isRowSelected(row)) {
                    c.setBackground(getBackground());
                    int modelRow = convertRowIndexToModel(row);
                    String type =(String) getModel().getValueAt(modelRow, 2);
                    String type1 = (String) getModel().getValueAt(modelRow, 8);
                    if("nový".equals(type1)) {
                        c.setBackground(Color.RED);
                    }
                    if(("prebiehajúci".equals(type1))&&("NaCesteKPacientovi".equals(type))){
                        c.setBackground(Color.ORANGE);
                    }
                    if(("prebiehajúci".equals(type1))&&("ZasahujeNaMieste".equals(type))){
                        c.setBackground(Color.GREEN);
                    }
                    if(("prebiehajúci".equals(type1))&&("NaCesteDoNemocnice".equals(type))){
                        c.setBackground(new Color(255,0,255));
                    }
                    if(("prebiehajúci".equals(type1))&&("OdovzdavaniePacienta".equals(type))){
                        c.setBackground(new Color(0,204,102));
                    }
                    if(("prebiehajúci".equals(type1))&&("NaCesteNaStanicu".equals(type))){
                        c.setBackground(new Color(0,255,255));
                    }
                    if("ukončený".equals(type1)) {
                        c.setBackground(new Color(128,128,128));
                    }
                }
                return c;

            }

        };
        ;
        jB_AZ_PrijazdDoNemocnice5 = new javax.swing.JButton();
        jB_FZV_PrijazdPosadkyKPacientovi = new javax.swing.JButton();
        jB_FZV_Zapis_prevozDoNemocnice = new javax.swing.JButton();
        jB_FZV_PrijazdDoZariadenia = new javax.swing.JButton();
        jB_FZV_zapisNavratNaStanicu = new javax.swing.JButton();
        jB_FZV_vyjazdPosadky = new javax.swing.JButton();
        jB_FZV_PrijazdNaStanicu = new javax.swing.JButton();
        jB_AZ_PrijazdDoNemocnice15 = new javax.swing.JButton();
        jCBNemocnice2 = new javax.swing.JComboBox<>();
        jB_FZV_UzavriZasah = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        jEPHistoriaVelitel = new javax.swing.JEditorPane("text/html", "");
        jPanel8 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTablePosadkyZachranky = new javax.swing.JTable();
        jB_AZ_PrijazdDoNemocnice8 = new javax.swing.JButton();
        jB_Velitel_nova_posadka = new javax.swing.JButton();
        jB_AZ_PrijazdDoNemocnice10 = new javax.swing.JButton();
        jB_AZ_PrijazdDoNemocnice11 = new javax.swing.JButton();
        jB_AZ_ZobrazZamestnancov = new javax.swing.JButton();
        jCKB_stav_v_sluzbe = new javax.swing.JCheckBox();
        jCKB_stav_vsetkych = new javax.swing.JCheckBox();
        jCB_zamestnanci = new javax.swing.JComboBox<>();
        jCKB_pozicia_vodic = new javax.swing.JCheckBox();
        jCKB_pozicia_lekar = new javax.swing.JCheckBox();
        jCKB_pozicia_zachranar = new javax.swing.JCheckBox();
        jCKB_pozicia_velitel = new javax.swing.JCheckBox();
        jCKB_stav_volno = new javax.swing.JCheckBox();
        jCKB_pozicia_vsetci = new javax.swing.JCheckBox();
        jCB_RLP = new javax.swing.JCheckBox();
        jCB_RZP = new javax.swing.JCheckBox();
        jB_VZ_uprav_zamestnanca = new javax.swing.JButton();
        jDateChooserOD2 = new com.toedter.calendar.JDateChooser();
        jOd2 = new javax.swing.JLabel();
        jDateChooserDO2 = new com.toedter.calendar.JDateChooser();
        jOd3 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabelPrihlasenyAko2 = new javax.swing.JLabel();
        jTextFieldPrihlasenyAKo2 = new javax.swing.JTextField();
        jLabelZdravotnickeZariadenie2 = new javax.swing.JLabel();
        jTextFieldZachranka = new javax.swing.JTextField();
        jButtonOdhlasenie1 = new javax.swing.JButton();
        jFOpravZasah = new javax.swing.JFrame();
        jPanel9 = new javax.swing.JPanel();
        jL_NZ_TelCislo8 = new javax.swing.JLabel();
        jL_NZ_TelCislo9 = new javax.swing.JLabel();
        jL_NZ_TelCislo10 = new javax.swing.JLabel();
        jL_NZ_TelCislo11 = new javax.swing.JLabel();
        jTF_OZ_tel = new javax.swing.JTextField();
        jTF_OZ_rodCislo = new javax.swing.JTextField();
        jTF_OZ_meno = new javax.swing.JTextField();
        jTF_OZ_priezvisko = new javax.swing.JTextField();
        jL_NZ_TelCislo12 = new javax.swing.JLabel();
        jTF_OZ_ulica = new javax.swing.JTextField();
        jTF_OZ_PopisCislo = new javax.swing.JTextField();
        jL_NZ_TelCislo13 = new javax.swing.JLabel();
        jL_NZ_TelCislo14 = new javax.swing.JLabel();
        jCB_OZ_obce = new javax.swing.JComboBox<>();
        jL_NZ_TelCislo15 = new javax.swing.JLabel();
        jTF_OZ_IdZasahu = new javax.swing.JTextField();
        jL_NZ_TelCislo16 = new javax.swing.JLabel();
        jCB_OZ_Pp_Diag = new javax.swing.JComboBox<>();
        jL_NZ_TelCislo17 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        JTA_OZ_infopacient = new javax.swing.JTextArea();
        jL_NZ_TelCislo18 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        JTA_OZ_infozasah = new javax.swing.JTextArea();
        jL_NZ_TelCislo20 = new javax.swing.JLabel();
        jCB_OZ_Konecna_Diag = new javax.swing.JComboBox<>();
        jL_NZ_TelCislo19 = new javax.swing.JLabel();
        jCBNemocnice1 = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPasswordFieldUserPasswd = new javax.swing.JPasswordField();
        jLabelUserName = new javax.swing.JLabel();
        jLabelUserPasswd = new javax.swing.JLabel();
        jComboBoxUserType = new javax.swing.JComboBox<>();
        jButtonLogin = new javax.swing.JButton();
        jTextFieldUserName = new javax.swing.JTextField();

        JFNemocnicaGUI.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        JFNemocnicaGUI.setMinimumSize(new java.awt.Dimension(1184, 600));
        JFNemocnicaGUI.setResizable(false);
        JFNemocnicaGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                JFNemocnicaGUIWindowOpened(evt);
            }
        });

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1136, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Administrácia", jPanel7);

        jPanel5.setMaximumSize(new java.awt.Dimension(185, 38));
        jPanel5.setMinimumSize(new java.awt.Dimension(185, 38));
        jPanel5.setPreferredSize(new java.awt.Dimension(185, 38));

        JTabNemocnicaZasah.setAutoCreateRowSorter(true);
        JTabNemocnicaZasah.setBackground(new java.awt.Color(0, 0, 0));
        JTabNemocnicaZasah.setBorder(new javax.swing.border.MatteBorder(null));
        JTabNemocnicaZasah.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        JTabNemocnicaZasah.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ZÁSAHU", "ID POSÁDKY ŠPZ", "ID PACIENTA", "RODNÉ ČÍSLO", "INFO O ZÁSAHU", "INFO O PACIENTOVI", "STAV POSÁDKY"
            }
        ));
        JTabNemocnicaZasah.setToolTipText("");
        jScrollPane4.setViewportView(JTabNemocnicaZasah);

        jB_AZ_PrijazdDoNemocnice1.setText("Zapíš príjazd do nemocnice");
        jB_AZ_PrijazdDoNemocnice1.setMaximumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice1.setMinimumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice1.setPreferredSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice1ActionPerformed(evt);
            }
        });

        jB_AZ_PrijazdDoNemocnice2.setText("Zapíš odchod z nemocnice");
        jB_AZ_PrijazdDoNemocnice2.setMaximumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice2.setMinimumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice2.setPreferredSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice2ActionPerformed(evt);
            }
        });

        jB_AZ_PrijazdDoNemocnice3.setText("Zobraz info k zásahu");
        jB_AZ_PrijazdDoNemocnice3.setMaximumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice3.setMinimumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice3.setPreferredSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice3ActionPerformed(evt);
            }
        });

        jButton4.setText("Tlač");
        jButton4.setMaximumSize(new java.awt.Dimension(185, 38));
        jButton4.setMinimumSize(new java.awt.Dimension(185, 38));
        jButton4.setPreferredSize(new java.awt.Dimension(185, 38));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("refresh");
        jButton5.setMaximumSize(new java.awt.Dimension(185, 38));
        jButton5.setMinimumSize(new java.awt.Dimension(185, 38));
        jButton5.setPreferredSize(new java.awt.Dimension(185, 38));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jB_AZ_PrijazdDoNemocnice9.setText("Zobraz históriu pacienta");
        jB_AZ_PrijazdDoNemocnice9.setMaximumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice9.setMinimumSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice9.setPreferredSize(new java.awt.Dimension(185, 38));
        jB_AZ_PrijazdDoNemocnice9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice9ActionPerformed(evt);
            }
        });

        jScrollPane8.setViewportView(JEP_NEM_TextArea);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jB_AZ_PrijazdDoNemocnice2, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jB_AZ_PrijazdDoNemocnice3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jB_AZ_PrijazdDoNemocnice9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Aktuálne zásahy  zariadenia", jPanel5);

        jLabelPrihlasenyAko.setBackground(new java.awt.Color(204, 204, 204));
        jLabelPrihlasenyAko.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPrihlasenyAko.setText("Prihláseny ako :");

        jTextFieldPrihlasenyAKo.setEditable(false);
        jTextFieldPrihlasenyAKo.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldPrihlasenyAKo.setForeground(new java.awt.Color(0, 0, 0));

        jLabelZdravotnickeZariadenie1.setBackground(new java.awt.Color(204, 204, 204));
        jLabelZdravotnickeZariadenie1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelZdravotnickeZariadenie1.setText("Zdravotnícke Zariadenie :");

        jTextFieldZdravotnickeZariadenie.setEditable(false);
        jTextFieldZdravotnickeZariadenie.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldZdravotnickeZariadenie.setForeground(new java.awt.Color(0, 0, 0));

        jButtonOdhlasenie.setText("Odhlásenie");
        jButtonOdhlasenie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOdhlasenieActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JFNemocnicaGUILayout = new javax.swing.GroupLayout(JFNemocnicaGUI.getContentPane());
        JFNemocnicaGUI.getContentPane().setLayout(JFNemocnicaGUILayout);
        JFNemocnicaGUILayout.setHorizontalGroup(
            JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JFNemocnicaGUILayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JFNemocnicaGUILayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JFNemocnicaGUILayout.createSequentialGroup()
                                .addGroup(JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelZdravotnickeZariadenie1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelPrihlasenyAko, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldPrihlasenyAKo, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                    .addComponent(jTextFieldZdravotnickeZariadenie)))
                            .addComponent(jButtonOdhlasenie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18))
        );
        JFNemocnicaGUILayout.setVerticalGroup(
            JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JFNemocnicaGUILayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelZdravotnickeZariadenie1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldZdravotnickeZariadenie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JFNemocnicaGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldPrihlasenyAKo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrihlasenyAko, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOdhlasenie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        jFOknoOperator.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jFOknoOperator.setTitle("Operator GUI");
        jFOknoOperator.setMinimumSize(new java.awt.Dimension(1366, 768));
        jFOknoOperator.setResizable(false);
        jFOknoOperator.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                jFOknoOperatorWindowOpened(evt);
            }
        });

        Administrácia.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));
        Administrácia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AdministráciaStateChanged(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableStatistika.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        for (int column = 0; column < jTableStatistika.getColumnCount(); column++)
        {
            TableColumn tableColumn = jTableStatistika.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < jTableStatistika.getRowCount(); row++)
            {
                TableCellRenderer cellRenderer = jTableStatistika.getCellRenderer(row, column);
                Component c = jTableStatistika.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + jTableStatistika.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                //  We've exceeded the maximum width, no need to check other rows

                if (preferredWidth >= maxWidth)
                {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth( preferredWidth );
        }
        jTableStatistika.setAutoCreateRowSorter(true);
        jTableStatistika.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTableStatistika.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(jTableStatistika);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 1290, 240));

        jDateChooserDO.setDateFormatString("dd.MM.yyyy"); // NOI18N
        jPanel3.add(jDateChooserDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 210, -1));

        jDateChooserOD.setDateFormatString("dd.MM.yyyy"); // NOI18N
        jPanel3.add(jDateChooserOD, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 200, -1));

        jCB_FOP_Zachranky.setBackground(new java.awt.Color(204, 204, 204));
        jCB_FOP_Zachranky.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jCB_FOP_Zachranky, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 410, 40));

        jCB_NZ_typDiagnozy1.setBackground(new java.awt.Color(204, 204, 204));
        jCB_NZ_typDiagnozy1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jCB_NZ_typDiagnozy1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jCB_NZ_typDiagnozy1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 168, 40));

        jCB_NZ_diagnozy1.setBackground(new java.awt.Color(204, 204, 204));
        jCB_NZ_diagnozy1.setEditable(true);
        jCB_NZ_diagnozy1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jCB_NZ_diagnozy1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jCB_NZ_diagnozy1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, 470, 40));

        jCBZachranka.setText("Zachranka");
        jCBZachranka.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBZachrankaStateChanged(evt);
            }
        });
        jPanel3.add(jCBZachranka, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 30));

        jOd.setText("OD");
        jPanel3.add(jOd, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, -1));

        jDo.setText("   DO");
        jPanel3.add(jDo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, -1, -1));

        jCBDATUM.setText("Datum");
        jCBDATUM.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBDATUMStateChanged(evt);
            }
        });
        jPanel3.add(jCBDATUM, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        jCBTypdiagnozy.setText("Typ Diagnozy");
        jCBTypdiagnozy.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBTypdiagnozyStateChanged(evt);
            }
        });
        jCBTypdiagnozy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTypdiagnozyActionPerformed(evt);
            }
        });
        jPanel3.add(jCBTypdiagnozy, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, 30));

        jCBNemocnice.setText("Nemocnica");
        jCBNemocnice.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBNemocniceStateChanged(evt);
            }
        });
        jPanel3.add(jCBNemocnice, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, 30));

        jCB_FOP_Nemocnice1.setBackground(new java.awt.Color(204, 204, 204));
        jCB_FOP_Nemocnice1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jCB_FOP_Nemocnice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, 410, 40));

        jCBDiagnozy.setText("Diagnoza");
        jCBDiagnozy.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBDiagnozyStateChanged(evt);
            }
        });
        jPanel3.add(jCBDiagnozy, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 100, -1, 30));

        jCBTPDAno.setText("Ano");
        jCBTPDAno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTPDAnoActionPerformed(evt);
            }
        });
        jPanel3.add(jCBTPDAno, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 100, -1, 30));

        jCBTPDNie.setText("Nie");
        jCBTPDNie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTPDNieActionPerformed(evt);
            }
        });
        jPanel3.add(jCBTPDNie, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 100, -1, 30));

        jLabel1.setText("Potrvdila sa diagnóza :");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, -1, 30));

        jCheckBox1.setText("S časom");
        jPanel3.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, -1, -1));

        jCheckBox2.setText("S aktérmi");
        jPanel3.add(jCheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, -1, -1));

        jButton9.setText("Vygeneruj štatistiku");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Správa zo zásahu");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton1.setText("Export do Excelu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 230, 470, 30));

        jCheckBox3.setText("Založ novú záchranku");
        jCheckBox3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox3StateChanged(evt);
            }
        });
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel3.add(jCheckBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 20, -1, -1));

        jTextField1.setText("Názov");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jPanel3.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 20, 280, -1));

        jTextField2.setText("Obec");
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 50, 280, -1));

        jTextField3.setText("Ulica");
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });
        jPanel3.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 80, 280, -1));

        jTextField4.setText("Číslo");
        jTextField4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField4MouseClicked(evt);
            }
        });
        jPanel3.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 110, 280, -1));

        jTextField5.setText("Meno Šéfa");
        jTextField5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField5MouseClicked(evt);
            }
        });
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 140, 280, -1));

        jTextField6.setText("Priezvisko");
        jTextField6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField6MouseClicked(evt);
            }
        });
        jPanel3.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 170, 280, -1));

        jTextField7.setText("Rodné číslo");
        jTextField7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField7MouseClicked(evt);
            }
        });
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 200, 280, -1));

        jTextField8.setText("telefón1");
        jTextField8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField8MouseClicked(evt);
            }
        });
        jPanel3.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 230, 140, -1));

        jTextField9.setText("telefón2");
        jTextField9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField9MouseClicked(evt);
            }
        });
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 230, 140, -1));

        jBVytvorZachranku.setText("Vytvor");
        jBVytvorZachranku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVytvorZachrankuActionPerformed(evt);
            }
        });
        jPanel3.add(jBVytvorZachranku, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 50, -1, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane13.setViewportView(jTable2);

        jPanel4.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 1240, 240));

        jButton11.setText("jButton9");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 180, -1, -1));

        jButton12.setText("jButton10");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 210, -1, -1));

        jDateChooserDO1.setDateFormatString("dd.MM.yyyy"); // NOI18N
        jPanel4.add(jDateChooserDO1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 210, -1));

        jDateChooserOD1.setDateFormatString("dd.MM.yyyy"); // NOI18N
        jPanel4.add(jDateChooserOD1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 200, -1));

        jCB_FOP_Zachranky1.setBackground(new java.awt.Color(204, 204, 204));
        jCB_FOP_Zachranky1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel4.add(jCB_FOP_Zachranky1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 480, 40));

        jL_NZ_TelCislo6.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo6.setText("Typ diagnózy :");
        jPanel4.add(jL_NZ_TelCislo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, -1, 42));

        jCB_NZ_typDiagnozy2.setBackground(new java.awt.Color(204, 204, 204));
        jCB_NZ_typDiagnozy2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jCB_NZ_typDiagnozy2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel4.add(jCB_NZ_typDiagnozy2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 168, 40));

        jL_NZ_TelCislo7.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo7.setText("Diagnóza :");
        jPanel4.add(jL_NZ_TelCislo7, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 100, 64, 42));

        jCB_NZ_diagnozy2.setBackground(new java.awt.Color(204, 204, 204));
        jCB_NZ_diagnozy2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jCB_NZ_diagnozy2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel4.add(jCB_NZ_diagnozy2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 220, 270, 40));

        jCBZachranka1.setText("Zachranka");
        jCBZachranka1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBZachranka1StateChanged(evt);
            }
        });
        jPanel4.add(jCBZachranka1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 30));

        jOd1.setText("OD");
        jPanel4.add(jOd1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, -1));

        jDo1.setText("   DO");
        jPanel4.add(jDo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, -1, -1));

        jCBDATUM2.setText("Datum");
        jCBDATUM2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBDATUM2StateChanged(evt);
            }
        });
        jPanel4.add(jCBDATUM2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        javax.swing.GroupLayout jPTestovaciLayout = new javax.swing.GroupLayout(jPTestovaci);
        jPTestovaci.setLayout(jPTestovaciLayout);
        jPTestovaciLayout.setHorizontalGroup(
            jPTestovaciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPTestovaciLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPTestovaciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPTestovaciLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPTestovaciLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(84, 84, 84))))
        );
        jPTestovaciLayout.setVerticalGroup(
            jPTestovaciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPTestovaciLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Administrácia.addTab("Administrácia", jPTestovaci);

        jPAktualneZasahy.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));
        jPAktualneZasahy.setPreferredSize(new java.awt.Dimension(1000, 579));
        jPAktualneZasahy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPAktualneZasahyMouseClicked(evt);
            }
        });

        JTableAktualneZasahy.setAutoCreateRowSorter(true);
        JTableAktualneZasahy.setBackground(new java.awt.Color(0, 0, 0));
        JTableAktualneZasahy.setBorder(new javax.swing.border.MatteBorder(null));
        JTableAktualneZasahy.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        JTableAktualneZasahy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ZÁSAHU", "STANICA POSÁDKY", "ID POSÁDKY", "STAV POSÁDKY", "INFO O ZÁSAHU", "MIESTO ZÁSAHU", "ID PACIENTA", "RODNÉ ČÍSLO", "INFO O PACIENTOVI", "ID ZARIADENIA", "STAV"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        JTableAktualneZasahy.setToolTipText("");
        jScrollPane5.setViewportView(JTableAktualneZasahy);

        jCB_FOP_Nemocnice.setBackground(new java.awt.Color(204, 204, 204));
        jCB_FOP_Nemocnice.setForeground(new java.awt.Color(0, 0, 0));

        jB_AZ_PrijazdDoNemocnice6.setText("Doplň údaje k zásahu");
        jB_AZ_PrijazdDoNemocnice6.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_AZ_PrijazdDoNemocnice6.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_AZ_PrijazdDoNemocnice6.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_AZ_PrijazdDoNemocnice6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice6ActionPerformed(evt);
            }
        });

        jB_FOP_UzavriZasah.setText("Uzavri zásah");
        jB_FOP_UzavriZasah.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_FOP_UzavriZasah.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_FOP_UzavriZasah.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_FOP_UzavriZasah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FOP_UzavriZasahActionPerformed(evt);
            }
        });

        jB_FOP_PrijazdPosadkyKPacientovi.setText("Zapíš príjazd k pacientovi");
        jB_FOP_PrijazdPosadkyKPacientovi.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdPosadkyKPacientovi.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdPosadkyKPacientovi.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdPosadkyKPacientovi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FOP_PrijazdPosadkyKPacientoviActionPerformed(evt);
            }
        });

        jB_FOP_ZapisPrevozDoNemocnice.setText("Zapíš prevoz do nemocnice");
        jB_FOP_ZapisPrevozDoNemocnice.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_FOP_ZapisPrevozDoNemocnice.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_FOP_ZapisPrevozDoNemocnice.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_FOP_ZapisPrevozDoNemocnice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FOP_ZapisPrevozDoNemocniceActionPerformed(evt);
            }
        });

        jB_FOP_PrijazdDoZariadenia.setText("Zapíš príjazd do nemocnice");
        jB_FOP_PrijazdDoZariadenia.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdDoZariadenia.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdDoZariadenia.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdDoZariadenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FOP_PrijazdDoZariadeniaActionPerformed(evt);
            }
        });

        jB_FOP_zapisNavratNaStanicu.setText("Zapíš návrat na stanicu");
        jB_FOP_zapisNavratNaStanicu.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_FOP_zapisNavratNaStanicu.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_FOP_zapisNavratNaStanicu.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_FOP_zapisNavratNaStanicu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FOP_zapisNavratNaStanicuActionPerformed(evt);
            }
        });

        jB_FOP_zapisVyjazdKPacientovi.setText("Zapíš výjazd k pacientovi");
        jB_FOP_zapisVyjazdKPacientovi.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_FOP_zapisVyjazdKPacientovi.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_FOP_zapisVyjazdKPacientovi.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_FOP_zapisVyjazdKPacientovi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FOP_zapisVyjazdKPacientoviActionPerformed(evt);
            }
        });

        jB_FOP_PrijazdNaStanicu.setText("Zapíš príjazd na stanicu");
        jB_FOP_PrijazdNaStanicu.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdNaStanicu.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdNaStanicu.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_FOP_PrijazdNaStanicu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FOP_PrijazdNaStanicuActionPerformed(evt);
            }
        });

        jB_AZ_PrijazdDoNemocnice16.setText("Vytlač správu zo zásahu");
        jB_AZ_PrijazdDoNemocnice16.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_AZ_PrijazdDoNemocnice16.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_AZ_PrijazdDoNemocnice16.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_AZ_PrijazdDoNemocnice16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice16ActionPerformed(evt);
            }
        });

        jButton2.setText("refresh");
        jButton2.setMaximumSize(new java.awt.Dimension(193, 32));
        jButton2.setMinimumSize(new java.awt.Dimension(193, 32));
        jButton2.setPreferredSize(new java.awt.Dimension(193, 32));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        JTablePosadky.setAutoCreateRowSorter(true);
        JTablePosadky.setBackground(new java.awt.Color(0, 0, 0));
        JTablePosadky.setBorder(new javax.swing.border.MatteBorder(null));
        JTablePosadky.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        JTablePosadky.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MESTO_POSADKY", "ID_POSADKY", "TYP_POSADKY", "STAV_POSADKY"
            }
        ));
        JTablePosadky.setToolTipText("");
        JTablePosadky.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTablePosadkyMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JTablePosadky);

        jButton8.setText(" MimoPrevadzky / Pripravená");
        jButton8.setMaximumSize(new java.awt.Dimension(193, 32));
        jButton8.setMinimumSize(new java.awt.Dimension(193, 32));
        jButton8.setPreferredSize(new java.awt.Dimension(193, 32));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jEPHistoriaOperat.setEditable(false);
        jScrollPane14.setViewportView(jEPHistoriaOperat);

        jB_AZ_ZobrazHistoriu.setText("Zobraz historiu pacienta");
        jB_AZ_ZobrazHistoriu.setMaximumSize(new java.awt.Dimension(193, 32));
        jB_AZ_ZobrazHistoriu.setMinimumSize(new java.awt.Dimension(193, 32));
        jB_AZ_ZobrazHistoriu.setPreferredSize(new java.awt.Dimension(193, 32));
        jB_AZ_ZobrazHistoriu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_ZobrazHistoriuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPAktualneZasahyLayout = new javax.swing.GroupLayout(jPAktualneZasahy);
        jPAktualneZasahy.setLayout(jPAktualneZasahyLayout);
        jPAktualneZasahyLayout.setHorizontalGroup(
            jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPAktualneZasahyLayout.createSequentialGroup()
                .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                                .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jB_FOP_zapisVyjazdKPacientovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jB_AZ_PrijazdDoNemocnice6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jB_FOP_PrijazdPosadkyKPacientovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                                        .addComponent(jB_FOP_ZapisPrevozDoNemocnice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCB_FOP_Nemocnice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                                        .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                                                .addComponent(jB_FOP_zapisNavratNaStanicu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jB_FOP_UzavriZasah, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jB_AZ_ZobrazHistoriu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                                                .addComponent(jB_FOP_PrijazdDoZariadenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jB_FOP_PrijazdNaStanicu, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jB_AZ_PrijazdDoNemocnice16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 2, Short.MAX_VALUE))))
                            .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPAktualneZasahyLayout.setVerticalGroup(
            jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                        .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jB_FOP_zapisVyjazdKPacientovi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jB_FOP_ZapisPrevozDoNemocnice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCB_FOP_Nemocnice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(jB_FOP_PrijazdDoZariadenia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jB_FOP_PrijazdPosadkyKPacientovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jB_FOP_PrijazdNaStanicu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jB_FOP_zapisNavratNaStanicu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_FOP_UzavriZasah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jB_AZ_ZobrazHistoriu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPAktualneZasahyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPAktualneZasahyLayout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );

        Administrácia.addTab("Aktuálne zásahy", jPAktualneZasahy);

        jPNovyZasah.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));

        jL_NZ_meno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_meno.setText("meno :");

        jL_NZ_TelCislo.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo.setText(" telefónne číslo :");

        jL_NZ_Priezvisko.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_Priezvisko.setText("priezvisko :");

        jTF_NZ_priezvisko.setBackground(new java.awt.Color(204, 204, 204));
        jTF_NZ_priezvisko.setForeground(new java.awt.Color(0, 0, 0));
        jTF_NZ_priezvisko.setMaximumSize(new java.awt.Dimension(122, 36));
        jTF_NZ_priezvisko.setMinimumSize(new java.awt.Dimension(122, 36));
        jTF_NZ_priezvisko.setPreferredSize(new java.awt.Dimension(122, 36));

        jTF_NZ_meno.setBackground(new java.awt.Color(204, 204, 204));
        jTF_NZ_meno.setForeground(new java.awt.Color(0, 0, 0));
        jTF_NZ_meno.setMaximumSize(new java.awt.Dimension(122, 36));
        jTF_NZ_meno.setMinimumSize(new java.awt.Dimension(122, 36));
        jTF_NZ_meno.setPreferredSize(new java.awt.Dimension(122, 36));

        jTF_NZ_tel.setBackground(new java.awt.Color(204, 204, 204));
        jTF_NZ_tel.setForeground(new java.awt.Color(0, 0, 0));
        jTF_NZ_tel.setMaximumSize(new java.awt.Dimension(122, 36));
        jTF_NZ_tel.setMinimumSize(new java.awt.Dimension(122, 36));
        jTF_NZ_tel.setPreferredSize(new java.awt.Dimension(122, 36));

        jL_NZ_rodCis.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_rodCis.setText("rodné číslo :");

        jTF_NZ_rodCis.setBackground(new java.awt.Color(204, 204, 204));
        jTF_NZ_rodCis.setForeground(new java.awt.Color(0, 0, 0));

        jL_NZ_Ulica.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_Ulica.setText("ulica :");
        jL_NZ_Ulica.setMaximumSize(new java.awt.Dimension(81, 16));
        jL_NZ_Ulica.setMinimumSize(new java.awt.Dimension(81, 16));
        jL_NZ_Ulica.setPreferredSize(new java.awt.Dimension(81, 16));

        jL_NZ_PopisneCislo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_PopisneCislo.setText("popisné číslo :");

        jL_NZ_obec.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_obec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_obec.setText("obec :");
        jL_NZ_obec.setMaximumSize(new java.awt.Dimension(244, 36));
        jL_NZ_obec.setMinimumSize(new java.awt.Dimension(244, 36));
        jL_NZ_obec.setPreferredSize(new java.awt.Dimension(244, 36));
        jL_NZ_obec.setRequestFocusEnabled(false);

        jTF_NZ_ulica1.setBackground(new java.awt.Color(204, 204, 204));
        jTF_NZ_ulica1.setForeground(new java.awt.Color(0, 0, 0));
        jTF_NZ_ulica1.setMaximumSize(new java.awt.Dimension(224, 36));
        jTF_NZ_ulica1.setMinimumSize(new java.awt.Dimension(244, 36));
        jTF_NZ_ulica1.setPreferredSize(new java.awt.Dimension(244, 36));

        jTF_NZ_popCis.setBackground(new java.awt.Color(204, 204, 204));
        jTF_NZ_popCis.setForeground(new java.awt.Color(0, 0, 0));

        jCB_NZ_obce.setBackground(new java.awt.Color(204, 204, 204));
        jCB_NZ_obce.setEditable(true);
        jCB_NZ_obce.setForeground(new java.awt.Color(0, 0, 0));

        jL_NZ_pacientInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_pacientInfo.setText("Volanie prijaté  :");
        jL_NZ_pacientInfo.setToolTipText("");

        jTextCAS.setEditable(false);
        jTextCAS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextCAS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextCASActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPZasahUdajeLayout = new javax.swing.GroupLayout(jPZasahUdaje);
        jPZasahUdaje.setLayout(jPZasahUdajeLayout);
        jPZasahUdajeLayout.setHorizontalGroup(
            jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPZasahUdajeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jL_NZ_rodCis, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jL_NZ_Priezvisko, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jL_NZ_meno, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jL_NZ_TelCislo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPZasahUdajeLayout.createSequentialGroup()
                        .addComponent(jTF_NZ_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jL_NZ_Ulica, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTF_NZ_ulica1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPZasahUdajeLayout.createSequentialGroup()
                        .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTF_NZ_meno, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTF_NZ_priezvisko, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPZasahUdajeLayout.createSequentialGroup()
                                .addComponent(jL_NZ_PopisneCislo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTF_NZ_popCis, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPZasahUdajeLayout.createSequentialGroup()
                                .addComponent(jL_NZ_obec, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCB_NZ_obce, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPZasahUdajeLayout.createSequentialGroup()
                        .addComponent(jTF_NZ_rodCis, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jL_NZ_pacientInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextCAS)))
                .addContainerGap())
        );
        jPZasahUdajeLayout.setVerticalGroup(
            jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPZasahUdajeLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jL_NZ_TelCislo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTF_NZ_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jL_NZ_Ulica, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTF_NZ_ulica1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jL_NZ_meno, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTF_NZ_meno, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jL_NZ_PopisneCislo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTF_NZ_popCis, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jL_NZ_Priezvisko, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTF_NZ_priezvisko, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jL_NZ_obec, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCB_NZ_obce, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jTF_NZ_rodCis, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jL_NZ_rodCis, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPZasahUdajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jL_NZ_pacientInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextCAS, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jL_NZ_Ulica.getAccessibleContext().setAccessibleDescription("");
        jL_NZ_obec.getAccessibleContext().setAccessibleDescription("");

        jB_NZ_HladajPosadky.setText("Vyhľadaj posádky");
        jB_NZ_HladajPosadky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_NZ_HladajPosadkyActionPerformed(evt);
            }
        });

        jB_NZ_vydajZasah.setText("Vydaj žiadosť na zásah");
        jB_NZ_vydajZasah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_NZ_vydajZasahActionPerformed(evt);
            }
        });

        jTF_NZ_pacientInfo.setBackground(new java.awt.Color(204, 204, 204));
        jTF_NZ_pacientInfo.setColumns(20);
        jTF_NZ_pacientInfo.setForeground(new java.awt.Color(0, 0, 0));
        jTF_NZ_pacientInfo.setRows(5);
        jTF_NZ_pacientInfo.setText("\t\t\tZadajte info k  pacientovi :");
        jScrollPane6.setViewportView(jTF_NZ_pacientInfo);

        JTB_NZ_POS.setAutoCreateRowSorter(true);
        JTB_NZ_POS.setBackground(new java.awt.Color(0, 0, 0));
        JTB_NZ_POS.setBorder(new javax.swing.border.MatteBorder(null));
        JTB_NZ_POS.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        JTB_NZ_POS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MESTO POSÁDKY", "ID POSÁDKY", "TYP POSÁDKY", "STAV POSÁDKY", "VZDIALENOSŤ", "[ vzdialenosť / čas  ] z googla"
            }
        ));
        new JScrollPane(JTB_NZ_POS, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JTB_NZ_POS.setToolTipText("");
        JTB_NZ_POS.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        JTB_NZ_POS.setAutoscrolls(false);
        JTB_NZ_POS.setFocusable(false);
        jScrollPane7.setViewportView(JTB_NZ_POS);

        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));

        jL_NZ_TelCislo2.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo2.setText("Diagnóza :");

        jL_NZ_TelCislo3.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo3.setText("Typ diagnózy :");

        jCB_NZ_diagnozy.setBackground(new java.awt.Color(204, 204, 204));
        jCB_NZ_diagnozy.setEditable(true);
        jCB_NZ_diagnozy.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jCB_NZ_diagnozy.setForeground(new java.awt.Color(0, 0, 0));

        jCB_NZ_typDiagnozy.setBackground(new java.awt.Color(204, 204, 204));
        jCB_NZ_typDiagnozy.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jCB_NZ_typDiagnozy.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jL_NZ_TelCislo3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCB_NZ_typDiagnozy, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jL_NZ_TelCislo2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCB_NZ_diagnozy, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jL_NZ_TelCislo3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCB_NZ_typDiagnozy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jL_NZ_TelCislo2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCB_NZ_diagnozy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPNovyZasahLayout = new javax.swing.GroupLayout(jPNovyZasah);
        jPNovyZasah.setLayout(jPNovyZasahLayout);
        jPNovyZasahLayout.setHorizontalGroup(
            jPNovyZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPNovyZasahLayout.createSequentialGroup()
                .addGroup(jPNovyZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPNovyZasahLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPNovyZasahLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPZasahUdaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPNovyZasahLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPNovyZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPNovyZasahLayout.createSequentialGroup()
                                .addComponent(jB_NZ_HladajPosadky, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jB_NZ_vydajZasah, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane7))))
                .addContainerGap())
        );
        jPNovyZasahLayout.setVerticalGroup(
            jPNovyZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPNovyZasahLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPNovyZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPZasahUdaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPNovyZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jB_NZ_HladajPosadky)
                    .addComponent(jB_NZ_vydajZasah))
                .addGap(585, 585, 585))
        );

        Administrácia.addTab("Vytvorenie nového zásahu", jPNovyZasah);

        jTFOS.setEditable(false);
        jTFOS.setBackground(new java.awt.Color(204, 204, 204));
        jTFOS.setForeground(new java.awt.Color(0, 0, 0));
        jTFOS.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabelZdravotnickeZariadenie3.setBackground(new java.awt.Color(204, 204, 204));
        jLabelZdravotnickeZariadenie3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelZdravotnickeZariadenie3.setText("Zdravotnícke Zariadenie :");

        jLabelPrihlasenyAko3.setBackground(new java.awt.Color(204, 204, 204));
        jLabelPrihlasenyAko3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPrihlasenyAko3.setText("Prihláseny ako :");

        JTFOSPrih.setEditable(false);
        JTFOSPrih.setBackground(new java.awt.Color(204, 204, 204));
        JTFOSPrih.setForeground(new java.awt.Color(0, 0, 0));
        JTFOSPrih.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButtonOdhlasenie3.setText("Odhlásenie");
        jButtonOdhlasenie3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOdhlasenie3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFOknoOperatorLayout = new javax.swing.GroupLayout(jFOknoOperator.getContentPane());
        jFOknoOperator.getContentPane().setLayout(jFOknoOperatorLayout);
        jFOknoOperatorLayout.setHorizontalGroup(
            jFOknoOperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFOknoOperatorLayout.createSequentialGroup()
                .addContainerGap(742, Short.MAX_VALUE)
                .addGroup(jFOknoOperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelZdravotnickeZariadenie3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrihlasenyAko3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFOknoOperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(JTFOSPrih)
                    .addComponent(jTFOS, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
            .addGroup(jFOknoOperatorLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jFOknoOperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Administrácia, javax.swing.GroupLayout.PREFERRED_SIZE, 1317, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOdhlasenie3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jFOknoOperatorLayout.setVerticalGroup(
            jFOknoOperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFOknoOperatorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFOknoOperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelZdravotnickeZariadenie3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFOS, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFOknoOperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTFOSPrih, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrihlasenyAko3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Administrácia, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOdhlasenie3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        JFLogin.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        JFLogin.setTitle("Výber posádky");
        JFLogin.setFocusable(false);
        JFLogin.setMinimumSize(new java.awt.Dimension(500, 200));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Výber posádky :");

        jTLogin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID POSÁDKY ŠPZ", "TYP POSÁDKY", "STAV POSÁDKY"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTLogin);

        JBLoginFinal.setText("Prihlásenie");
        JBLoginFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBLoginFinalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JBLoginFinal)
                .addGap(23, 23, 23))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JBLoginFinal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JFLoginLayout = new javax.swing.GroupLayout(JFLogin.getContentPane());
        JFLogin.getContentPane().setLayout(JFLoginLayout);
        JFLoginLayout.setHorizontalGroup(
            JFLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JFLoginLayout.setVerticalGroup(
            JFLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JFLoginLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 54, Short.MAX_VALUE))
        );

        JFZachrankaVelitel.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        JFZachrankaVelitel.setMinimumSize(new java.awt.Dimension(1184, 600));
        JFZachrankaVelitel.setResizable(false);
        JFZachrankaVelitel.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                JFZachrankaVelitelWindowOpened(evt);
            }
        });

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(0, 0, 0)));

        jPanel10.setMaximumSize(new java.awt.Dimension(185, 32));
        jPanel10.setMinimumSize(new java.awt.Dimension(185, 32));
        jPanel10.setPreferredSize(new java.awt.Dimension(185, 32));

        JTAktualneZasahyZachranky.setAutoCreateRowSorter(true);
        JTAktualneZasahyZachranky.setBackground(new java.awt.Color(0, 0, 0));
        JTAktualneZasahyZachranky.setBorder(new javax.swing.border.MatteBorder(null));
        JTAktualneZasahyZachranky.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        JTAktualneZasahyZachranky.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ZÁSAHU", "ŠPZ POSÁDKY", "STAV POSÁDKY", "NEMOCNICA", "PACIENT", "KONTAKT", "ADRESA", "INFO", "STAV", "id_pacienta", "rod_cislo"
            }
        ));
        JTAktualneZasahyZachranky.setToolTipText("");
        JTAktualneZasahyZachranky.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTAktualneZasahyZachrankyMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(JTAktualneZasahyZachranky);
        if (JTAktualneZasahyZachranky.getColumnModel().getColumnCount() > 0) {
            JTAktualneZasahyZachranky.getColumnModel().getColumn(0).setMinWidth(70);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(0).setPreferredWidth(70);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(0).setMaxWidth(70);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(1).setMinWidth(90);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(1).setPreferredWidth(90);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(1).setMaxWidth(90);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(2).setMinWidth(120);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(2).setPreferredWidth(120);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(2).setMaxWidth(120);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(8).setMinWidth(70);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(8).setPreferredWidth(70);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(8).setMaxWidth(70);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(9).setMinWidth(1);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(9).setPreferredWidth(1);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(9).setMaxWidth(1);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(10).setMinWidth(0);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(10).setPreferredWidth(0);
            JTAktualneZasahyZachranky.getColumnModel().getColumn(10).setMaxWidth(0);
        }

        jB_AZ_PrijazdDoNemocnice5.setText("Doplň údaje k zásahu");
        jB_AZ_PrijazdDoNemocnice5.setMaximumSize(new java.awt.Dimension(185, 32));
        jB_AZ_PrijazdDoNemocnice5.setMinimumSize(new java.awt.Dimension(185, 32));
        jB_AZ_PrijazdDoNemocnice5.setPreferredSize(new java.awt.Dimension(185, 32));
        jB_AZ_PrijazdDoNemocnice5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice5ActionPerformed(evt);
            }
        });

        jB_FZV_PrijazdPosadkyKPacientovi.setText("Zapíš príjazd k pacientovi");
        jB_FZV_PrijazdPosadkyKPacientovi.setMaximumSize(new java.awt.Dimension(185, 32));
        jB_FZV_PrijazdPosadkyKPacientovi.setMinimumSize(new java.awt.Dimension(185, 32));
        jB_FZV_PrijazdPosadkyKPacientovi.setPreferredSize(new java.awt.Dimension(185, 32));
        jB_FZV_PrijazdPosadkyKPacientovi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FZV_PrijazdPosadkyKPacientoviActionPerformed(evt);
            }
        });

        jB_FZV_Zapis_prevozDoNemocnice.setText("Zapíš prevoz do nemocnice");
        jB_FZV_Zapis_prevozDoNemocnice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FZV_Zapis_prevozDoNemocniceActionPerformed(evt);
            }
        });

        jB_FZV_PrijazdDoZariadenia.setText("Zapíš príjazd do nemocnice");
        jB_FZV_PrijazdDoZariadenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FZV_PrijazdDoZariadeniaActionPerformed(evt);
            }
        });

        jB_FZV_zapisNavratNaStanicu.setText("Zapíš návrat na stanicu");
        jB_FZV_zapisNavratNaStanicu.setMaximumSize(new java.awt.Dimension(185, 32));
        jB_FZV_zapisNavratNaStanicu.setMinimumSize(new java.awt.Dimension(185, 32));
        jB_FZV_zapisNavratNaStanicu.setPreferredSize(new java.awt.Dimension(185, 32));
        jB_FZV_zapisNavratNaStanicu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FZV_zapisNavratNaStanicuActionPerformed(evt);
            }
        });

        jB_FZV_vyjazdPosadky.setText("Zapíš výjazd k pacientovi");
        jB_FZV_vyjazdPosadky.setMaximumSize(new java.awt.Dimension(185, 32));
        jB_FZV_vyjazdPosadky.setMinimumSize(new java.awt.Dimension(185, 32));
        jB_FZV_vyjazdPosadky.setPreferredSize(new java.awt.Dimension(185, 32));
        jB_FZV_vyjazdPosadky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FZV_vyjazdPosadkyActionPerformed(evt);
            }
        });

        jB_FZV_PrijazdNaStanicu.setText("Zapíš príjazd na stanicu");
        jB_FZV_PrijazdNaStanicu.setMaximumSize(new java.awt.Dimension(185, 32));
        jB_FZV_PrijazdNaStanicu.setMinimumSize(new java.awt.Dimension(185, 32));
        jB_FZV_PrijazdNaStanicu.setPreferredSize(new java.awt.Dimension(185, 32));
        jB_FZV_PrijazdNaStanicu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FZV_PrijazdNaStanicuActionPerformed(evt);
            }
        });

        jB_AZ_PrijazdDoNemocnice15.setText("Vytlač výjazdový formulár");
        jB_AZ_PrijazdDoNemocnice15.setMaximumSize(new java.awt.Dimension(185, 32));
        jB_AZ_PrijazdDoNemocnice15.setMinimumSize(new java.awt.Dimension(185, 32));
        jB_AZ_PrijazdDoNemocnice15.setPreferredSize(new java.awt.Dimension(185, 32));
        jB_AZ_PrijazdDoNemocnice15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice15ActionPerformed(evt);
            }
        });

        jCBNemocnice2.setBackground(new java.awt.Color(204, 204, 204));
        jCBNemocnice2.setForeground(new java.awt.Color(0, 0, 0));
        jCBNemocnice2.setMinimumSize(new java.awt.Dimension(33, 24));
        jCBNemocnice2.setPreferredSize(new java.awt.Dimension(33, 24));

        jB_FZV_UzavriZasah.setText("Uzavri zásah");
        jB_FZV_UzavriZasah.setMaximumSize(new java.awt.Dimension(185, 32));
        jB_FZV_UzavriZasah.setMinimumSize(new java.awt.Dimension(185, 32));
        jB_FZV_UzavriZasah.setPreferredSize(new java.awt.Dimension(185, 32));
        jB_FZV_UzavriZasah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_FZV_UzavriZasahActionPerformed(evt);
            }
        });

        jButton7.setText("refresh");
        jButton7.setMaximumSize(new java.awt.Dimension(185, 32));
        jButton7.setMinimumSize(new java.awt.Dimension(185, 32));
        jButton7.setPreferredSize(new java.awt.Dimension(185, 32));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton13.setText("Zobraz históriu pacienta");
        jButton13.setMaximumSize(new java.awt.Dimension(185, 32));
        jButton13.setMinimumSize(new java.awt.Dimension(185, 32));
        jButton13.setPreferredSize(new java.awt.Dimension(185, 32));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jScrollPane15.setViewportView(jEPHistoriaVelitel);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jB_AZ_PrijazdDoNemocnice5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jB_FZV_vyjazdPosadky, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jB_FZV_PrijazdPosadkyKPacientovi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jB_AZ_PrijazdDoNemocnice15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jB_FZV_PrijazdDoZariadenia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jB_FZV_Zapis_prevozDoNemocnice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jB_FZV_zapisNavratNaStanicu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jB_FZV_PrijazdNaStanicu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBNemocnice2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_FZV_UzavriZasah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(61, 61, 61))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jB_FZV_Zapis_prevozDoNemocnice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jB_AZ_PrijazdDoNemocnice15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBNemocnice2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jB_FZV_PrijazdDoZariadenia, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jB_FZV_vyjazdPosadky, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jB_FZV_UzavriZasah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jB_FZV_PrijazdPosadkyKPacientovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jB_FZV_zapisNavratNaStanicu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_FZV_PrijazdNaStanicu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(59, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jScrollPane15)
                        .addContainerGap())))
        );

        jTabbedPane2.addTab("Aktuálne zásahy  záchranky", jPanel10);

        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel8.setMaximumSize(new java.awt.Dimension(163, 40));
        jPanel8.setMinimumSize(new java.awt.Dimension(163, 40));
        jPanel8.setPreferredSize(new java.awt.Dimension(163, 40));

        jTablePosadkyZachranky.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID POSÁDKA ŠPZ", "TYP_POSÁDKY", "VODIČ", "ZÁCHRANÁR", "LEKÁR", "STAV POSÁDKY"
            }
        ));
        jTablePosadkyZachranky.setCellSelectionEnabled(true);
        jTablePosadkyZachranky.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePosadkyZachrankyMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(jTablePosadkyZachranky);

        jB_AZ_PrijazdDoNemocnice8.setText("Uprav zloženie posádky");
        jB_AZ_PrijazdDoNemocnice8.setMaximumSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice8.setMinimumSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice8.setPreferredSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice8ActionPerformed(evt);
            }
        });

        jB_Velitel_nova_posadka.setText("Vytvor novú sanitku");
        jB_Velitel_nova_posadka.setMaximumSize(new java.awt.Dimension(163, 40));
        jB_Velitel_nova_posadka.setMinimumSize(new java.awt.Dimension(163, 40));
        jB_Velitel_nova_posadka.setPreferredSize(new java.awt.Dimension(163, 40));
        jB_Velitel_nova_posadka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_Velitel_nova_posadkaActionPerformed(evt);
            }
        });

        jB_AZ_PrijazdDoNemocnice10.setText("Vytvor Zamestnanca");
        jB_AZ_PrijazdDoNemocnice10.setMaximumSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice10.setMinimumSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice10.setPreferredSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice10ActionPerformed(evt);
            }
        });

        jB_AZ_PrijazdDoNemocnice11.setText("Vygeneruj dochádzku");
        jB_AZ_PrijazdDoNemocnice11.setMaximumSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice11.setMinimumSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice11.setPreferredSize(new java.awt.Dimension(163, 40));
        jB_AZ_PrijazdDoNemocnice11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_PrijazdDoNemocnice11ActionPerformed(evt);
            }
        });

        jB_AZ_ZobrazZamestnancov.setText("Zobraz Zamestnancov");
        jB_AZ_ZobrazZamestnancov.setMaximumSize(new java.awt.Dimension(163, 40));
        jB_AZ_ZobrazZamestnancov.setMinimumSize(new java.awt.Dimension(163, 40));
        jB_AZ_ZobrazZamestnancov.setPreferredSize(new java.awt.Dimension(163, 40));
        jB_AZ_ZobrazZamestnancov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_AZ_ZobrazZamestnancovActionPerformed(evt);
            }
        });

        jCKB_stav_v_sluzbe.setText("len v službe");
        jCKB_stav_v_sluzbe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_stav_v_sluzbeActionPerformed(evt);
            }
        });

        jCKB_stav_vsetkych.setText(" všetkých");
        jCKB_stav_vsetkych.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_stav_vsetkychActionPerformed(evt);
            }
        });

        jCB_zamestnanci.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCB_zamestnanciMouseClicked(evt);
            }
        });

        jCKB_pozicia_vodic.setText("vodič");
        jCKB_pozicia_vodic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_pozicia_vodicActionPerformed(evt);
            }
        });

        jCKB_pozicia_lekar.setText("lekár");
        jCKB_pozicia_lekar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_pozicia_lekarActionPerformed(evt);
            }
        });

        jCKB_pozicia_zachranar.setText("záchranár");
        jCKB_pozicia_zachranar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_pozicia_zachranarActionPerformed(evt);
            }
        });

        jCKB_pozicia_velitel.setText("veliteľ");
        jCKB_pozicia_velitel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_pozicia_velitelActionPerformed(evt);
            }
        });

        jCKB_stav_volno.setText("voľných");
        jCKB_stav_volno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_stav_volnoActionPerformed(evt);
            }
        });

        jCKB_pozicia_vsetci.setText("všetkých");
        jCKB_pozicia_vsetci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCKB_pozicia_vsetciActionPerformed(evt);
            }
        });

        jCB_RLP.setText(" RLP");
        jCB_RLP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_RLPActionPerformed(evt);
            }
        });

        jCB_RZP.setText(" RZP");
        jCB_RZP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_RZPActionPerformed(evt);
            }
        });

        jB_VZ_uprav_zamestnanca.setText("Uprav Zamestnanca");
        jB_VZ_uprav_zamestnanca.setMaximumSize(new java.awt.Dimension(163, 40));
        jB_VZ_uprav_zamestnanca.setMinimumSize(new java.awt.Dimension(163, 40));
        jB_VZ_uprav_zamestnanca.setPreferredSize(new java.awt.Dimension(163, 40));
        jB_VZ_uprav_zamestnanca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jB_VZ_uprav_zamestnancaActionPerformed(evt);
            }
        });

        jDateChooserOD2.setDateFormatString("dd.MM.yyyy"); // NOI18N

        jOd2.setText("OD :");

        jDateChooserDO2.setDateFormatString("dd.MM.yyyy"); // NOI18N
        jDateChooserDO2.setPreferredSize(new java.awt.Dimension(118, 40));

        jOd3.setText("DO :");

        jButton14.setText("refresh");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jTextField10.setText("jTextField10");
        jTextField10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField10MouseClicked(evt);
            }
        });

        jTextField11.setText("jTextField10");
        jTextField11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField11MouseClicked(evt);
            }
        });

        jTextField12.setText("jTextField10");
        jTextField12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField12MouseClicked(evt);
            }
        });

        jTextField13.setText("jTextField10");
        jTextField13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField13MouseClicked(evt);
            }
        });

        jTextField14.setText("jTextField10");
        jTextField14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField14MouseClicked(evt);
            }
        });

        jTextField15.setText("jTextField10");
        jTextField15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField15MouseClicked(evt);
            }
        });

        jTextField16.setText("jTextField10");
        jTextField16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jB_AZ_PrijazdDoNemocnice11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jB_Velitel_nova_posadka, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(jB_VZ_uprav_zamestnanca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jB_AZ_PrijazdDoNemocnice8, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jB_AZ_ZobrazZamestnancov, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCKB_stav_vsetkych)
                                    .addComponent(jCKB_pozicia_vsetci))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jCKB_stav_v_sluzbe)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCKB_stav_volno))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jCKB_pozicia_vodic)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCKB_pozicia_lekar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCKB_pozicia_zachranar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCKB_pozicia_velitel))))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jCB_RZP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCB_zamestnanci, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCB_RLP)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jOd2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooserOD2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jOd3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooserDO2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCKB_stav_v_sluzbe)
                            .addComponent(jCKB_stav_volno)
                            .addComponent(jCKB_stav_vsetkych))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCKB_pozicia_vodic)
                            .addComponent(jCKB_pozicia_lekar)
                            .addComponent(jCKB_pozicia_zachranar)
                            .addComponent(jCKB_pozicia_velitel)
                            .addComponent(jCKB_pozicia_vsetci)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_AZ_PrijazdDoNemocnice8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jB_AZ_ZobrazZamestnancov, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jCB_RLP, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCB_RZP, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jB_Velitel_nova_posadka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCB_zamestnanci, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField11)
                            .addComponent(jTextField10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jOd2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDateChooserOD2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jOd3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooserDO2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jB_AZ_PrijazdDoNemocnice10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jB_VZ_uprav_zamestnanca, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jB_AZ_PrijazdDoNemocnice11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Administrácia", jPanel8);

        jLabelPrihlasenyAko2.setBackground(new java.awt.Color(204, 204, 204));
        jLabelPrihlasenyAko2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPrihlasenyAko2.setText("Prihláseny ako :");

        jTextFieldPrihlasenyAKo2.setEditable(false);
        jTextFieldPrihlasenyAKo2.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldPrihlasenyAKo2.setForeground(new java.awt.Color(0, 0, 0));

        jLabelZdravotnickeZariadenie2.setBackground(new java.awt.Color(204, 204, 204));
        jLabelZdravotnickeZariadenie2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelZdravotnickeZariadenie2.setText("Zdravotnícke Zariadenie :");

        jTextFieldZachranka.setEditable(false);
        jTextFieldZachranka.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldZachranka.setForeground(new java.awt.Color(0, 0, 0));

        jButtonOdhlasenie1.setText("Odhlásenie");
        jButtonOdhlasenie1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOdhlasenie1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JFZachrankaVelitelLayout = new javax.swing.GroupLayout(JFZachrankaVelitel.getContentPane());
        JFZachrankaVelitel.getContentPane().setLayout(JFZachrankaVelitelLayout);
        JFZachrankaVelitelLayout.setHorizontalGroup(
            JFZachrankaVelitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JFZachrankaVelitelLayout.createSequentialGroup()
                .addContainerGap(504, Short.MAX_VALUE)
                .addGroup(JFZachrankaVelitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JFZachrankaVelitelLayout.createSequentialGroup()
                        .addGroup(JFZachrankaVelitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelZdravotnickeZariadenie2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPrihlasenyAko2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JFZachrankaVelitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldPrihlasenyAKo2, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                            .addComponent(jTextFieldZachranka)))
                    .addComponent(jButtonOdhlasenie1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jTabbedPane2)
        );
        JFZachrankaVelitelLayout.setVerticalGroup(
            JFZachrankaVelitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JFZachrankaVelitelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JFZachrankaVelitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelZdravotnickeZariadenie2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldZachranka, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JFZachrankaVelitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldPrihlasenyAKo2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrihlasenyAko2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOdhlasenie1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        jFOpravZasah.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFOpravZasah.setTitle("Oprav Zasah");
        jFOpravZasah.setMinimumSize(new java.awt.Dimension(890, 610));
        jFOpravZasah.setResizable(false);
        jFOpravZasah.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                jFOpravZasahWindowOpened(evt);
            }
        });

        jPanel9.setMaximumSize(new java.awt.Dimension(820, 660));
        jPanel9.setMinimumSize(new java.awt.Dimension(820, 660));
        jPanel9.setPreferredSize(new java.awt.Dimension(860, 660));
        jPanel9.setRequestFocusEnabled(false);

        jL_NZ_TelCislo8.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo8.setText(" telefónne číslo :");

        jL_NZ_TelCislo9.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo9.setText("Meno:");

        jL_NZ_TelCislo10.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo10.setText("Priezvisko:");

        jL_NZ_TelCislo11.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo11.setText("Rodné číslo:");

        jTF_OZ_rodCislo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTF_OZ_rodCisloActionPerformed(evt);
            }
        });

        jTF_OZ_meno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTF_OZ_menoActionPerformed(evt);
            }
        });

        jTF_OZ_priezvisko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTF_OZ_priezviskoActionPerformed(evt);
            }
        });

        jL_NZ_TelCislo12.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo12.setText("Ulica:");

        jL_NZ_TelCislo13.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo13.setText("Popisné číslo:");

        jL_NZ_TelCislo14.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo14.setText("Obec:");

        jCB_OZ_obce.setBackground(new java.awt.Color(204, 204, 204));
        jCB_OZ_obce.setEditable(true);
        jCB_OZ_obce.setForeground(new java.awt.Color(0, 0, 0));

        jL_NZ_TelCislo15.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo15.setText("ID Zásahu:");

        jTF_OZ_IdZasahu.setEditable(false);
        jTF_OZ_IdZasahu.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jL_NZ_TelCislo16.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo16.setText("Pravdepodobná Diagnóza:");

        jCB_OZ_Pp_Diag.setBackground(new java.awt.Color(204, 204, 204));
        jCB_OZ_Pp_Diag.setEditable(true);
        jCB_OZ_Pp_Diag.setForeground(new java.awt.Color(0, 0, 0));

        jL_NZ_TelCislo17.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo17.setText("Info o pacientovi :");

        JTA_OZ_infopacient.setColumns(20);
        JTA_OZ_infopacient.setLineWrap(true);
        JTA_OZ_infopacient.setRows(5);
        JTA_OZ_infopacient.setWrapStyleWord(true);
        jScrollPane9.setViewportView(JTA_OZ_infopacient);

        jL_NZ_TelCislo18.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo18.setText("Info k zásahu :");

        JTA_OZ_infozasah.setColumns(20);
        JTA_OZ_infozasah.setLineWrap(true);
        JTA_OZ_infozasah.setRows(5);
        JTA_OZ_infozasah.setWrapStyleWord(true);
        jScrollPane12.setViewportView(JTA_OZ_infozasah);

        jL_NZ_TelCislo20.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo20.setText("Konečná Diagnóza :");

        jCB_OZ_Konecna_Diag.setBackground(new java.awt.Color(204, 204, 204));
        jCB_OZ_Konecna_Diag.setEditable(true);
        jCB_OZ_Konecna_Diag.setForeground(new java.awt.Color(0, 0, 0));
        jCB_OZ_Konecna_Diag.setMaximumRowCount(10);

        jL_NZ_TelCislo19.setBackground(new java.awt.Color(204, 204, 204));
        jL_NZ_TelCislo19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jL_NZ_TelCislo19.setText("Prevoz:");

        jCBNemocnice1.setBackground(new java.awt.Color(204, 204, 204));
        jCBNemocnice1.setForeground(new java.awt.Color(0, 0, 0));
        jCBNemocnice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBNemocnice1ActionPerformed(evt);
            }
        });

        jButton6.setText("Refresh");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton3.setText("Uložiť");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jL_NZ_TelCislo17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jL_NZ_TelCislo10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jL_NZ_TelCislo9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jL_NZ_TelCislo11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jL_NZ_TelCislo8, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTF_OZ_tel, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                    .addComponent(jTF_OZ_rodCislo)
                                    .addComponent(jTF_OZ_priezvisko)
                                    .addComponent(jTF_OZ_meno, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jL_NZ_TelCislo12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jL_NZ_TelCislo13, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                    .addComponent(jL_NZ_TelCislo14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTF_OZ_PopisCislo, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                                            .addComponent(jTF_OZ_ulica))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTF_OZ_IdZasahu)
                                            .addComponent(jL_NZ_TelCislo15, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)))
                                    .addComponent(jCB_OZ_obce, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane9)
                            .addComponent(jL_NZ_TelCislo18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane12)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jL_NZ_TelCislo16, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addGap(8, 8, 8)
                                .addComponent(jCB_OZ_Pp_Diag, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jL_NZ_TelCislo20, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                    .addComponent(jL_NZ_TelCislo19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jCBNemocnice1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jCB_OZ_Konecna_Diag, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jL_NZ_TelCislo8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTF_OZ_tel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jL_NZ_TelCislo9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTF_OZ_meno, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jL_NZ_TelCislo13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTF_OZ_PopisCislo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTF_OZ_IdZasahu, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jL_NZ_TelCislo10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTF_OZ_priezvisko, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jL_NZ_TelCislo14, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCB_OZ_obce, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jL_NZ_TelCislo11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTF_OZ_rodCislo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jL_NZ_TelCislo12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTF_OZ_ulica, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jL_NZ_TelCislo15, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jL_NZ_TelCislo16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCB_OZ_Pp_Diag, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jL_NZ_TelCislo17, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jL_NZ_TelCislo18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jL_NZ_TelCislo20, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCB_OZ_Konecna_Diag, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jL_NZ_TelCislo19, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBNemocnice1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTF_OZ_rodCislo.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout jFOpravZasahLayout = new javax.swing.GroupLayout(jFOpravZasah.getContentPane());
        jFOpravZasah.getContentPane().setLayout(jFOpravZasahLayout);
        jFOpravZasahLayout.setHorizontalGroup(
            jFOpravZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFOpravZasahLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 858, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jFOpravZasahLayout.setVerticalGroup(
            jFOpravZasahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFOpravZasahLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Záchrankový systém ---  ⓇRENDO_SW_INDUSTRIESⒸ");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jPasswordFieldUserPasswd.setBackground(new java.awt.Color(102, 102, 102));
        jPasswordFieldUserPasswd.setForeground(new java.awt.Color(0, 0, 0));

        jLabelUserName.setForeground(new java.awt.Color(0, 0, 0));
        jLabelUserName.setText("Uživateľské meno :");

        jLabelUserPasswd.setBackground(new java.awt.Color(0, 0, 0));
        jLabelUserPasswd.setForeground(new java.awt.Color(0, 0, 0));
        jLabelUserPasswd.setText("Uživateľské heslo :");

        jComboBoxUserType.setBackground(new java.awt.Color(102, 102, 102));
        jComboBoxUserType.setForeground(new java.awt.Color(0, 0, 0));
        jComboBoxUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Prihlásenie", "Odhlásenie" }));

        jButtonLogin.setBackground(new java.awt.Color(102, 102, 102));
        jButtonLogin.setForeground(new java.awt.Color(0, 0, 0));
        jButtonLogin.setText("Vykonaj");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        jTextFieldUserName.setBackground(new java.awt.Color(102, 102, 102));
        jTextFieldUserName.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUserName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelUserPasswd, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPasswordFieldUserPasswd)
                            .addComponent(jTextFieldUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxUserType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUserName)
                    .addComponent(jTextFieldUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordFieldUserPasswd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelUserPasswd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBoxUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButtonLogin)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(533, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("empty-statement")
    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        String name;
        String passwd;
        String LoginAction;
        name = jTextFieldUserName.getText();
        passwd = jPasswordFieldUserPasswd.getText();
        LoginAction = (String) jComboBoxUserType.getSelectedItem();
        Pouzivatel usr = new Pouzivatel();

        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_LOGIN_FINAL(?,?,?,?,?,?)}");
            stored_pro.setString(1, LoginAction);
            stored_pro.setString(2, name);
            stored_pro.setString(3, passwd);
            stored_pro.registerOutParameter(4, java.sql.Types.INTEGER);
            stored_pro.registerOutParameter(5, java.sql.Types.INTEGER);
            stored_pro.registerOutParameter(6, java.sql.Types.INTEGER);
            stored_pro.executeUpdate();
            info = stored_pro.getInt(4);
            zamestnavatel = stored_pro.getInt(5);
            zamestnanec = stored_pro.getInt(6);
            usr.setId_zamestnavatela(zamestnavatel);
            usr.setId_zamesnanca(zamestnanec);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            if (stored_pro != null) {
                try {
                    stored_pro.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stored_pro != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        switch (info) {
            case 1:
                JOptionPane.showMessageDialog(null, "Zadali ste nesprávny login");
                //JFZachrankaVelitel.setVisible(true);

                //jFOknoOperator.setVisible(true);
                //this.repaint();
                //this.setVisible(false);
                break;

            case 2:
                JOptionPane.showMessageDialog(null, "Zadali ste nesprávne heslo");
                break;

            case 3:
                JOptionPane.showMessageDialog(null, "Zamestnanec už bol prihlásený");
                break;

            case 4:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako OPERÁTOR/ADMIN");
                try {
                    conn = DBConnection.DBConnection();
                    stored_pro = conn.prepareCall("{call PROC_SELECT_Z_Z(?,?)}");
                    stored_pro.setInt(1, usr.getId_zamestnavatela());
                    stored_pro.setInt(2, usr.getId_zamesnanca());
                    System.out.println("zamestnavatel>" + usr.getId_zamestnavatela() + "zamemestnanec>" + usr.getId_zamesnanca());
                    rs = stored_pro.executeQuery();
                    while (rs.next()) {
                        jTFOS.setText(rs.getString(1));
                        JTFOSPrih.setText(rs.getString(2));
                    }
                    rs.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    if (stored_pro != null) {
                        try {
                            stored_pro.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                // jPanel3.setVisible(false);
                //JTBZalozky.setEnabledAt(0, false);
                jFOknoOperator.setVisible(true);
                //this.repaint();
                this.setVisible(false);
                System.out.println("zamestnavatel:" + usr.getId_zamestnavatela() + "\n" + "Zamestnanec : " + usr.getId_zamesnanca());
                break;

            case 5:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako LEKÁR v zdravotníckom zariadení");

                try {
                    conn = DBConnection.DBConnection();
                    stored_pro = conn.prepareCall("{call PROC_SELECT_Z_Z(?,?)}");
                    stored_pro.setInt(1, usr.getId_zamestnavatela());
                    stored_pro.setInt(2, usr.getId_zamesnanca());
                    rs = stored_pro.executeQuery();
                    while (rs.next()) {
                        jTextFieldZdravotnickeZariadenie.setText(rs.getString(1));
                        jTextFieldPrihlasenyAKo.setText(rs.getString(2));
                    }
                    rs.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    if (stored_pro != null) {
                        try {
                            stored_pro.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (stored_pro != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                JFNemocnicaGUI.setVisible(true);
                //jTextFieldZdravotnickeZariadenie.setText(Integer.toString(zamestnavatel));
                ShowZasahNemocnice(getZasahPreNemocnicu(usr.getId_zamestnavatela()));
                this.setVisible(false);
                break;

            case 6:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako VELITEĽ ZÁCHRANKY");
                try {
                    conn = DBConnection.DBConnection();
                    stored_pro = conn.prepareCall("{call PROC_SELECT_Z_Z(?,?)}");
                    stored_pro.setInt(1, usr.getId_zamestnavatela());
                    stored_pro.setInt(2, usr.getId_zamesnanca());
                    System.out.println("zamestnavatel>" + usr.getId_zamestnavatela() + "zamemestnanec>" + usr.getId_zamesnanca());
                    rs = stored_pro.executeQuery();
                    while (rs.next()) {
                        jTextFieldZachranka.setText(rs.getString(1));
                        jTextFieldPrihlasenyAKo2.setText(rs.getString(2));
                    }
                    rs.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    if (stored_pro != null) {
                        try {
                            stored_pro.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(usr.getId_zamestnavatela()));
                Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(usr.getId_zamestnavatela()));
                JFZachrankaVelitel.repaint();
                JFZachrankaVelitel.setVisible(true);

                this.setVisible(false);
                break;

            case 7:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako VODIČ ZÁCHRANKY");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 8:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako LEKÁR ZÁCHRANKY");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 9:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako ZÁCHRANÁR ZÁCHRANKY");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 10:
                JOptionPane.showMessageDialog(null, "Zamestnanec nebol ešte prihláseny");
                break;

            case 11:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa odhláslili ako OPERÁTOR/ADMIN");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 12:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa odhláslili ako LEKÁR v zdravotníckom zariadení");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 13:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa odhláslili ako VELITEĽ ZÁCHRANKY");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 14:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa odhláslili ako VODIČ ZÁCHRANKY");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 15:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa odhláslili ako LEKÁR ZÁCHRANKY");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 16:
                JOptionPane.showMessageDialog(null, "Úspešne ste sa odhláslili ako ZÁCHRANÁR ZÁCHRANKY");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 17:
                JOptionPane.showMessageDialog(null, "Odlásil sa špecialny zamestnanec");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 18:
                JOptionPane.showMessageDialog(null, "Prihlásil sa špeciálny zamestnanec");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;
            case 21:
                JOptionPane.showMessageDialog(null, "Sanitka je na zásahu opakujte prihlásenie keď bude Sanitka na stanici");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;

            case 22:
                JOptionPane.showMessageDialog(null, "Nie je môžné sa prihlásiť do posádky, lebo posádka nie je vedená ako RLP. \n Kozultuje problém s veliteľom záchranky. ");
                jTextFieldUserName.setText("");
                jPasswordFieldUserPasswd.setText("");
                this.repaint();
                break;
            case 20:
                JOptionPane.showMessageDialog(null, "Má viac ako jednu sanitku");
                Show_posadkyLogin(getPosadkyLogin(zamestnavatel, zamestnanec));
                this.setVisible(false);
                JFLogin.setVisible(true);
                //JFLogin.repaint();
                break;

        }


    }//GEN-LAST:event_jButtonLoginActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        Show_Posadky_In_Table(getPosadkyListZaklad());
        Show_aktualneZasahy(getAktualneZasahy());
    }//GEN-LAST:event_formWindowOpened

    private void jB_NZ_HladajPosadkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_NZ_HladajPosadkyActionPerformed
        String obecZasahu = "";
        try {
            if (jCB_NZ_obce.getSelectedItem().toString().isEmpty()) {
                throw new IllegalArgumentException("INVALID");
            }
            obecZasahu = jCB_NZ_obce.getSelectedItem().toString();
            ShowPosadkyNovyZasah(getPosadkyNovyZasah(obecZasahu));
        } catch (IllegalArgumentException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste miesto zasahu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (StringIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Taká miesto zásahu neexistuje", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "1" + e.getMessage() + e.getClass().getCanonicalName());
        }
    }//GEN-LAST:event_jB_NZ_HladajPosadkyActionPerformed

    private void jB_NZ_vydajZasahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_NZ_vydajZasahActionPerformed
        String errType = "";
        String nezadane = "";
        String id_posadky_spz = "nevybrany";
        String pom = jCB_NZ_obce.getSelectedItem().toString();
        //JOptionPane.showMessageDialog(null, pom);
        try {
            int i = JTB_NZ_POS.getSelectedRow();
            if (i >= 0) {
                id_posadky_spz = JTB_NZ_POS.getValueAt(i, 1).toString();
            } else {
                errType = "posádku";
                //throw new Exception();
                throw new IllegalArgumentException("INVALID");
            }

            if (jTF_NZ_tel.getText().isEmpty()) {
                errType = " telefónne číslo \n";
                //throw new Exception();
                throw new IllegalArgumentException("INVALID");
            }
            if (jTF_NZ_meno.getText().isEmpty()) {
                nezadane += "meno,";
                jTF_NZ_meno.setText("neznáme");
                //throw new Exception();
            }
            if (jTF_NZ_rodCis.getText().isEmpty()) {
                nezadane += " rodné číslo,";
                jTF_NZ_rodCis.setText("NEZ" + jTextCAS.getText().substring(11));
            }
            if (jTF_NZ_priezvisko.getText().isEmpty()) {
                nezadane += " priezvisko,";
                jTF_NZ_priezvisko.setText("neznáme");
                //throw new Exception();
            }
            if (jTF_NZ_ulica1.getText().isEmpty()) {
                errType = "ulica";
                //throw new Exception();
                throw new IllegalArgumentException("INVALID");
            }
            if (jCB_NZ_obce.getSelectedItem().toString().isEmpty()) {
                errType = "obec";
                // throw new Exception();
                throw new IllegalArgumentException("INVALID");
            }
            if (jTF_NZ_popCis.getText().isEmpty()) {
                nezadane += " popisné číslo,";
                jTF_NZ_popCis.setText(" ");
                //throw new Exception();
            }
            if (jTF_NZ_pacientInfo.getText().isEmpty()) {
                nezadane += " info o pacientovi,";
                jTF_NZ_pacientInfo.setText(jTF_NZ_pacientInfo.getText() + "neznáme");
                //throw new Exception();
            }
            if (jCB_NZ_diagnozy.getSelectedIndex() == -1) {
                nezadane += " diagnoza,";
                jCB_NZ_diagnozy.setSelectedItem("NEZADANÁ");
                //throw new Exception();
            }
            //if (id_posadky_spz.equals("nevybrany")) {
            //    errType = "posadku";
            //    throw new Exception();
            //}

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Nezadali ste : " + errType, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            return;
        }

        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete vydať rozkaz na zásah aj napriek  týmto nezadaným údajom: " + nezadane, "Vydanie rozkazu na zásahu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            String tel_cislo = jTF_NZ_tel.getText();
            String meno = jTF_NZ_meno.getText();
            String priezvisko = jTF_NZ_priezvisko.getText();
            String rod_cislo = jTF_NZ_rodCis.getText();
            String info_o_pac = jTF_NZ_pacientInfo.getText();
            String ulica = jTF_NZ_ulica1.getText();
            String pc = jTF_NZ_popCis.getText();
            String obec = jCB_NZ_obce.getSelectedItem().toString();

            String diagnoza = jCB_NZ_diagnozy.getSelectedItem().toString();
            System.out.println("************");
            //String adr = obec + "|" + ulica + "|" + pc;
            String adr = obec + "%7c" + ulica + "%7c" + pc;
            System.out.println(adr);
            System.out.println("************");
            adr = adr.replace(" ", "+");
            System.out.println(adr);
            System.out.println("************");
            Adresa adr1 = new Adresa();
            try {
                adr1.setLocationsByAdress(adr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
            }
//            } catch (IOException ex) {
//                Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ParseException ex) {
//                Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
//            }
            String lng = adr1.getLng();
            String lat = adr1.getLat();
            //System.out.println("************");
            //System.out.println(lng);
            //System.out.println("************");
            //System.out.println(lat);
            //System.out.println("************");  
            //System.out.println(id_posadky_spz);
            String gps = lng + " " + lat;
            System.out.println(gps);
            jTF_NZ_tel.setText("");
            jTF_NZ_meno.setText("");
            jTF_NZ_priezvisko.setText("");
            jTF_NZ_rodCis.setText("");
            jTF_NZ_pacientInfo.setText("");
            jTF_NZ_ulica1.setText("");
            jTF_NZ_popCis.setText("");
            jCB_NZ_obce.setSelectedItem(null);
            jCB_NZ_diagnozy.setSelectedItem(null);

            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_CREATE_ZASAH_NAHOTOVO (?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                stored_pro.setString(1, tel_cislo);
                stored_pro.setString(2, meno);
                stored_pro.setString(3, priezvisko);
                stored_pro.setString(4, rod_cislo);
                stored_pro.setString(5, info_o_pac);
                stored_pro.setString(6, ulica);
                stored_pro.setString(7, pc);
                stored_pro.setString(8, obec);
                stored_pro.setString(9, id_posadky_spz);
                stored_pro.setString(10, jTextCAS.getText());
                stored_pro.setString(11, diagnoza);
                stored_pro.setString(12, gps);
                stored_pro.setInt(13, zamestnanec);
                stored_pro.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();

            }
            JOptionPane.showMessageDialog(null, "Úspešne ste vytvorili zásah pre :\nMeno: " + meno + "\nPriezvisko :"
                    + priezvisko + "\nMiesto zásahu: " + obec, "Vytvorenie Zásahu", JOptionPane.INFORMATION_MESSAGE);

            DefaultTableModel model = (DefaultTableModel) JTB_NZ_POS.getModel();
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }

        } else {
            System.out.println("No action");
        }


    }//GEN-LAST:event_jB_NZ_vydajZasahActionPerformed

    private void AdministráciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AdministráciaStateChanged

        int i = Administrácia.getSelectedIndex();

        if (i == 1) {
            Show_aktualneZasahy(getAktualneZasahy());
            Color jbb = jPAktualneZasahy.getBackground();
            jButton8.setBackground(jbb);
            jButton8.setEnabled(false);
            jFOknoOperator.repaint();

        }
        if (i == 2) {
            int result = JOptionPane.showOptionDialog(this, "Naozaj chcete vytvoriť nový zásah ?", "Vytvorenie zásahu ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
                jTextCAS.setText(dateFormat.format(date).toString());
                DefaultTableModel model2 = (DefaultTableModel) JTB_NZ_POS.getModel();
                for (int j = model2.getRowCount() - 1; j >= 0; j--) {
                    model2.removeRow(j);
                }
                jTF_NZ_tel.setText("");
                jTF_NZ_meno.setText("");
                jTF_NZ_priezvisko.setText("");
                jTF_NZ_rodCis.setText("");
                jTF_NZ_pacientInfo.setText("");
                jTF_NZ_ulica1.setText("");
                jTF_NZ_popCis.setText("");
                jCB_NZ_obce.setSelectedItem(null);
                jCB_NZ_diagnozy.setSelectedItem(null);
            } else {
                Administrácia.setSelectedIndex(1);
                Color jbb = jPAktualneZasahy.getBackground();
                jButton8.setBackground(jbb);
                jButton8.setEnabled(false);
                jFOknoOperator.repaint();
            }

        }
        if (i == 3) {
            int result = JOptionPane.showOptionDialog(this, "Naozaj chcete odísť z vytvorenia zásahu ?", "Vytvorenie zásahu ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Ano a vymazať údaje", "Áno a zachovať údaje ", "Nie"}, JOptionPane.YES_NO_CANCEL_OPTION);

        }

    }//GEN-LAST:event_AdministráciaStateChanged

    private void JBLoginFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBLoginFinalActionPerformed
        System.out.println("INFO POVODNE :" + info);
        String id_posadky_spz;
        String typ_posadky;
        String stav_posadky;

        try {
            int i = jTLogin.getSelectedRow();
            id_posadky_spz = jTLogin.getValueAt(i, 0).toString();
            typ_posadky = jTLogin.getValueAt(i, 1).toString();
            stav_posadky = jTLogin.getValueAt(i, 2).toString();

            int result = JOptionPane.showOptionDialog(this, "Naozaj sa  chcete prihlásiť do vybraného vozidla :" + id_posadky_spz, "Prihlásenie do posádky",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    if (stav_posadky.equals("Pripravena")) {
                        conn = DBConnection.DBConnection();
                        stored_pro = conn.prepareCall("{call PROC_LOGIN_DO_POSADKY (?,?,?,?,?,?)}");
                        stored_pro.setInt(1, zamestnanec);
                        stored_pro.setInt(2, zamestnavatel);
                        stored_pro.setString(3, id_posadky_spz);
                        stored_pro.setString(4, stav_posadky);
                        stored_pro.setString(5, typ_posadky);
                        stored_pro.registerOutParameter(6, java.sql.JDBCType.INTEGER);
                        stored_pro.executeUpdate();
                        System.out.println("INFO POVODNE 2 :" + info);
                        info = stored_pro.getInt(6);
                        JFLogin.setVisible(false);
                        System.out.println("Prihlasenie ok");
                        System.out.println("DUFAM ZE KONECNE SPRAVNE INFO : " + info);

                        switch (info) {
                            case 7:
                                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako VODIČ ZÁCHRANKY");
                                jTextFieldUserName.setText("");
                                jPasswordFieldUserPasswd.setText("");
                                this.repaint();
                                this.setVisible(true);
                                break;

                            case 8:
                                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako LEKÁR ZÁCHRANKY");
                                jTextFieldUserName.setText("");
                                jPasswordFieldUserPasswd.setText("");
                                this.repaint();
                                this.setVisible(true);
                                break;

                            case 9:
                                JOptionPane.showMessageDialog(null, "Úspešne ste sa prihláslili ako ZÁCHRANÁR ZÁCHRANKY");
                                jTextFieldUserName.setText("");
                                jPasswordFieldUserPasswd.setText("");
                                this.repaint();
                                this.setVisible(true);
                                break;
                            case 21:
                                JOptionPane.showMessageDialog(null, "Sanitka je na zásahu opakujte prihlásenie keď bude Sanitka na stanici");
                                jTextFieldUserName.setText("");
                                jPasswordFieldUserPasswd.setText("");
                                this.repaint();
                                this.setVisible(true);
                                JFLogin.setVisible(false);
                                break;

                            case 22:
                                JOptionPane.showMessageDialog(null, "Nie je môžné sa prihlásiť do posádky, lebo posádka nie je vedená ako RLP. \n Kozultuje problém s veliteľom záchranky. ");
                                jTextFieldUserName.setText("");
                                jPasswordFieldUserPasswd.setText("");
                                this.repaint();
                                this.setVisible(true);
                                JFLogin.setVisible(false);
                                break;

                        }
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "Vozidlo s ŠPZ : " + id_posadky_spz + "Nie je na stanici", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR :" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }
            } else {
                System.out.println("No Option");
                return;
            }
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste posádku ", "Error", JOptionPane.ERROR_MESSAGE);
            JFLogin.setAlwaysOnTop(false);
            ex.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


    }//GEN-LAST:event_JBLoginFinalActionPerformed
    /**
     * Metoda, na potvrdenie nastavenia navratu posadky na stanicu
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stavu zasahu,id_posadky_spz a stav_posadky PROC_UPDATE_ZASAH_NAVRAT
     * (id_zasahu) nastavi : datum_zaciatku_navratu na DB sysdate , stav_posadky
     * na "NaCesteNaStanicu"
     *
     *
     * @param evt - kliknutie
     */
    private void jB_AZ_PrijazdDoNemocnice2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice2ActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTabNemocnicaZasah.getSelectedRow();
            id_zasahu = Integer.parseInt(JTabNemocnicaZasah.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!stav_posadky.equals("OdovzdavaniePacienta")) {
                JOptionPane.showMessageDialog(null, "Sanitka s:" + posadka + " zásahu : " + id_zasahu + "neodovzdába pacienta v nemocnici", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať odchod Sanitke :" + posadka + "\n vykonávajúcej zásah :" + id_zasahu + "?", "Zapísanie odjazdu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_NAVRAT (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            ShowZasahNemocnice(getZasahPreNemocnicu(zamestnavatel));
            JFNemocnicaGUI.repaint();
        } else {
            System.out.println("nie");
        }
    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice2ActionPerformed
    /**
     * Metoda, na potvrdenie prijazdu zasahu do nemocnice
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stav (zasahu), id_posadky_spz stav_posadky
     * PROC_UPDATE_ZASAH_DATUM_PRIJAZDU_DO_ZAR (id_zasahu) nastavi:
     * datum_prijazdu_do_zar na DB sysdate , stav_posadky na
     * "OdovzdavaniePacienta"
     *
     *
     * @param evt - kliknutie
     */
    private void jB_AZ_PrijazdDoNemocnice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice1ActionPerformed
        int id_zasahu = -1;
        String errType = "chyba";
        String stav = "";
        String posadka = "";
        String stavPosadky = "";
        try {
            int i = JTabNemocnicaZasah.getSelectedRow();
            id_zasahu = Integer.parseInt(JTabNemocnicaZasah.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stavPosadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!stavPosadky.equals("NaCesteDoNemocnice")) {
                JOptionPane.showMessageDialog(null, "Sanitka s:" + posadka + " zásahu : " + id_zasahu + "nie je na ceste do nemocnice", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať príjazd Sanitky :" + posadka
                + "\n vykonávajúcej zásah :" + id_zasahu + "?", "Zapísanie príjazdu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_PRIJAZDU_DO_ZAR (?,?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.setInt(2, zamestnanec);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            ShowZasahNemocnice(getZasahPreNemocnicu(zamestnavatel));
            JFNemocnicaGUI.repaint();
        } else {
            System.out.println("nic");

        }
    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice1ActionPerformed

    private void jB_AZ_PrijazdDoNemocnice3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice3ActionPerformed
        JEP_NEM_TextArea.setText("");
        int id_zasahu;
        String s = "";
        try {
            int i = JTabNemocnicaZasah.getSelectedRow();
            id_zasahu = Integer.parseInt(JTabNemocnicaZasah.getValueAt(i, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_INFA_NEMOCNICA (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                s = "<html>"
                        + "<body>"
                        + "<p><b>[Dátum tiesňového volania: " + (rs.getString(2)) + "][Id zásahu :" + (rs.getString(1)) + "</b>]<br>"
                        + "<b>[" + (rs.getString(6)) + "," + (rs.getString(7)) + "," + (rs.getString(8)) + "]</b>"
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodná diagnóza :</b><br>"
                        + "" + (rs.getString(9)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodné info o pacientovi :</b><br>"
                        + "" + (rs.getString(5)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Konečná diagnóza diagnóza :</b><br>"
                        + "" + (rs.getString(4)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Info o zasahu :</b><br>"
                        + "" + (rs.getString(3)) + ""
                        + "</p>"
                        + "<hr>"
                        + "</body>"
                        + "</html>";
            }
            rs.close();
            stored_pro.close();
            conn.close();
            JEP_NEM_TextArea.setText(s);
            System.out.println(s);
            JFNemocnicaGUI.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }


    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice3ActionPerformed

    private void jButtonOdhlasenieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOdhlasenieActionPerformed
        String pom = jTextFieldPrihlasenyAKo.getText();
        String pom1 = jTextFieldZdravotnickeZariadenie.getText();

        int result = JOptionPane.showOptionDialog(this, pom + ",naozaj sa chcete odhlásiť zo služby \n v zariadení :" + pom1 + "?", "Odlásenie zo služby",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            jComboBoxUserType.setSelectedItem("Odhlásenie");
            JFNemocnicaGUI.setVisible(false);
            this.setVisible(true);

        } else {
            System.out.println("noAction");
        }


    }//GEN-LAST:event_jButtonOdhlasenieActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            boolean complete = JEP_NEM_TextArea.print();
            //jTA_NEM_ZASAH.print();
            if (complete) {
                JOptionPane.showMessageDialog(null, "Done Printing", "Information", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(null, "Printing", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void JTAktualneZasahyZachrankyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTAktualneZasahyZachrankyMouseClicked
        String stav = "";
        String posadka = "";
        String stavPosadky = "";
        int id_zasahu;
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stavPosadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
        } catch (Exception e) {
            return;
        }
        if ("nový".equals(stav)) {
            jB_AZ_PrijazdDoNemocnice15.setBackground(Color.red);
            jB_AZ_PrijazdDoNemocnice15.setText("Vytlač výjazdový formulár");
        }
        if (!"nový".equals(stav)) {
            jB_AZ_PrijazdDoNemocnice15.setBackground(Color.GRAY);
            jB_AZ_PrijazdDoNemocnice15.setText("Vytlač Správa o zásahu");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_JTAktualneZasahyZachrankyMouseClicked

    private void jB_AZ_PrijazdDoNemocnice5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice5ActionPerformed

        int id_zasahu;
        String s = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_OPRAV_ZASAH (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                jTF_OZ_tel.setText(rs.getString(1));
                jTF_OZ_meno.setText(rs.getString(2));
                jTF_OZ_priezvisko.setText(rs.getString(3));
                jTF_OZ_rodCislo.setText(rs.getString(4));
                jTF_OZ_ulica.setText(rs.getString(5));
                jTF_OZ_PopisCislo.setText(rs.getString(6));
                jCB_OZ_obce.setSelectedItem(rs.getString(7).toString());
                jCB_OZ_Pp_Diag.setSelectedItem(rs.getString(8).toString());
                JTA_OZ_infopacient.setText(rs.getString(9));
                JTA_OZ_infozasah.setText(rs.getString(10));
                jCB_OZ_Konecna_Diag.setSelectedItem(rs.getString(11).toString());
                if (rs.getString(12) == null) {
                    jCBNemocnice1.addItem("nezadana");
                    jCBNemocnice1.setSelectedItem("nezadana");
                } else {
                    jCBNemocnice1.setSelectedItem(rs.getString(12));
                }
                //jCBNemocnice1.setSelectedItem(rs.getString(12).toString());
                jTF_OZ_IdZasahu.setText(rs.getString(13));
            }
            rs.close();
            stored_pro.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        jFOpravZasah.setVisible(true);
    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice5ActionPerformed
    /**
     * Metoda, na potvrdenie prijazdu posadky na miesto zasahu
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stav (zasahu),id_posadky_spz a stav_posadky
     * PROC_UPDATE_ZASAH_DATUM_PRIJAZDU (id_zasahu) nastavi :
     * datum_prijazdu_k_pacientovi na DB systime, stav_posadky
     * na"ZasahujeNaMieste"
     *
     * @param evt - kliknutie
     */
    private void jB_FZV_PrijazdPosadkyKPacientoviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FZV_PrijazdPosadkyKPacientoviActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            System.out.println(stav + posadka + stav_posadky + "");
            stored_pro.close();
            rs.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
            return;
        }
        if (!stav_posadky.equals("NaCesteKPacientovi")) {
            JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " nie je na ceste k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete nastaviť príchod k pacientovi zásahu :" + id_zasahu, "Prichod k pacientovi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_PRIJAZDU (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nezadali ste : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
            JFZachrankaVelitel.repaint();
        } else {
            System.out.println("nie");
        }
    }//GEN-LAST:event_jB_FZV_PrijazdPosadkyKPacientoviActionPerformed
    /**
     * Metoda, na potvrdenie prevozu pacienta do nemocnice
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stav (zasahu),id_posadky_spz a stav_posadky
     * PROC_UPDATE_ZASAH_ID_ZARIADENIA_BY_NAME (id_zasahu,nazov_zariadenia)
     * nastaví zásahu : id_zariadenia,datum_vyjazdu_od_pacienta na DBsysdate,
     * stav_posadky na 'NaCesteDoNemocnice' nazov_zariadenia je ziskane z
     * jCBNemocnice2
     *
     * @param evt - kliknutie
     */
    private void jB_FZV_Zapis_prevozDoNemocniceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FZV_Zapis_prevozDoNemocniceActionPerformed
        int id_zasahu = -1;
        String errType = "chyba";
        String posadka = "";
        String stavPosadky = "";
        String stav = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stavPosadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!stavPosadky.equals("ZasahujeNaMieste")) {
                JOptionPane.showMessageDialog(null, "Posádka zásahu :" + id_zasahu + " nezasahuje u pacienta", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nazovZariadenia = jCBNemocnice2.getSelectedItem().toString();
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať prevoz pre zásah :" + id_zasahu + "\n do zariadenia :" + nazovZariadenia + "?", "Vytvorenie prevozu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                //if (id_zasahu == -1) {
                //    errType = "zásah";
                //    throw new Exception();
                //}
                if (jCB_FOP_Nemocnice.getSelectedItem().toString().isEmpty()) {
                    errType = "nemocnicu";
                    throw new Exception();
                }
                System.out.println("" + nazovZariadenia + "," + id_zasahu);
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_ID_ZARIADENIA_BY_NAME(?,?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.setString(2, nazovZariadenia);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nezadali ste : " + errType, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
            JFZachrankaVelitel.repaint();
        } else {
            System.out.println("nic");
        }
    }//GEN-LAST:event_jB_FZV_Zapis_prevozDoNemocniceActionPerformed

    private void jButtonOdhlasenie1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOdhlasenie1ActionPerformed
        String pom = jTextFieldPrihlasenyAKo2.getText();
        String pom1 = jTextFieldZachranka.getText();

        int result = JOptionPane.showOptionDialog(this, pom + ",naozaj sa chcete odhlásiť zo služby \n v zariadení :" + pom1 + "?", "Odlásenie zo služby",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            jComboBoxUserType.setSelectedItem("Odhlásenie");
            JFZachrankaVelitel.setVisible(false);
            this.setVisible(true);

        } else {
            System.out.println("noAction");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonOdhlasenie1ActionPerformed
    /**
     * Metoda, na potvrdenie prijazdu zasahu do nemocnice
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie
     * stavu zasahu,id_posadky_spz a stavu_posadku
     * PROC_UPDATE_ZASAH_DATUM_PRIJAZDU_DO_ZAR (id_zasahu) nastavi :
     * datum_prijazdu_do_zar na DB sysdate , stav_posadky na
     * "OdovzdavaniePacienta:
     *
     *
     * @param evt - kliknutie
     */
    private void jB_FZV_PrijazdDoZariadeniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FZV_PrijazdDoZariadeniaActionPerformed
        int id_zasahu = -1;
        String errType = "chyba";
        String stav = "";
        String posadka = "";
        String stavPosadky = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stavPosadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!stavPosadky.equals("NaCesteDoNemocnice")) {
                JOptionPane.showMessageDialog(null, "Posádka zásahu :" + id_zasahu + " nie je na ceste do nemocnice ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať príjazd do nemocnice pre vybraný zásah :" + id_zasahu + "?", "Zapísanie préjazdu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_PRIJAZDU_DO_ZAR (?,?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.setInt(2, 0);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
            JFZachrankaVelitel.repaint();
        } else {
            System.out.println("nic");

        }
    }//GEN-LAST:event_jB_FZV_PrijazdDoZariadeniaActionPerformed
    /**
     * Metoda, na potvrdenie nastavenia navratu posadky na stanicu
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stavu zasahu,id_posadky_spz a stav_posadky PROC_UPDATE_ZASAH_NAVRAT
     * (id_zasahu) nastavi : datum_zaciatku_navratu na DB sysdate , stav_posadky
     * na "NaCesteNaStanicu"
     *
     *
     * @param evt - kliknutie
     */
    private void jB_FZV_zapisNavratNaStanicuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FZV_zapisNavratNaStanicuActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if ((!stav_posadky.equals("OdovzdavaniePacienta") && !stav_posadky.equals("NaCesteKPacientovi")) && !stav_posadky.equals("ZasahujeNaMieste")) {
            JOptionPane.showMessageDialog(null, "Nie je možné nastaviť posádku na cestu na stanicu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete nastaviť návrat  posádky zásahu :" + id_zasahu, "Návrat posádky na stanicu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_NAVRAT (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
            JFZachrankaVelitel.repaint();
        } else {
            System.out.println("nie");
        }
    }//GEN-LAST:event_jB_FZV_zapisNavratNaStanicuActionPerformed
    /**
     * Metoda, na potvrdenie odjazdu posadky na zasah PROC_SELECT_STAVY_ZASAHU
     * (id_zasahu)- zdielana procedura na zistenie : stav (stav
     * zasahu),id_posadky_spz a stav_posadky
     * PROC_UPDATE_ZASAH_DATUM_VYJAZDU_SANITKY (id_zasahu) nastavi zasah: stav
     * na "prebiehajúci", datum_vyjazdu_posadky na DB systime
     *
     *
     * @param evt - kliknutie
     */
    private void jB_FZV_vyjazdPosadkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FZV_vyjazdPosadkyActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            //System.out.println("stav>" + stav + "idZASAHU>" + id_zasahu + "posadka>" + posadka);
            if (!stav.equals("nový") || !stav_posadky.equals("NaCesteKPacientovi")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " už vyrazila na zásah ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete nastaviť odchod posádky : " + posadka + " zásahu :" + id_zasahu + " k pacientovi ?", "Výjazdu posádky zo stanice",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_VYJAZDU_SANITKY (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "ChYba : "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
//            DefaultTableModel model2 = (DefaultTableModel) JTAktualneZasahyZachranky.getModel();
//            for (int j = model2.getRowCount() - 1; j >= 0; j--) {
//                model2.removeRow(j);
//            }
            Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
            JFZachrankaVelitel.repaint();
        } else {
            System.out.println("nie");
        }
    }//GEN-LAST:event_jB_FZV_vyjazdPosadkyActionPerformed
    /**
     * Metoda, na potvrdenie prijazdu zachranky spat na stanicu
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stav (zasahu),id_posadky_spz a stav_posadky
     * PROC_UPDATE_ZASAH_DATUM_UKONCENIA (id_zasahu) nastavi :
     * datum_ukoncenia_zasahu na DB sysdate,stav na "ukončený", stav_posadky na
     * "Pripravena"
     *
     * @param evt - kliknutie
     */
    private void jB_FZV_PrijazdNaStanicuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FZV_PrijazdNaStanicuActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (stav.equals("prebiehajúci") && !stav_posadky.equals("NaCesteNaStanicu")) {
            JOptionPane.showMessageDialog(null, "Posádka nie je na ceste na späť na stanicu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať príjazd posádky : " + posadka + " zásahu : " + id_zasahu + "na stanicu", "Príjazd posádky na stanicu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_UKONCENIA (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
            JFZachrankaVelitel.repaint();

        } else {
            System.out.println("nie");
        }
    }//GEN-LAST:event_jB_FZV_PrijazdNaStanicuActionPerformed

    private void jB_AZ_PrijazdDoNemocnice15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice15ActionPerformed
        int id_zasahu = -1;
        String errType = "chyba";
        String pom;
        String stav = "";
        String posadka;
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            stav = JTAktualneZasahyZachranky.getValueAt(i, 8).toString();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());

            if (!stav.equals("nový")) {
                int result = JOptionPane.showOptionDialog(this, "Naozaj chcete vytlačiť Správu o zásahu pre zásah :" + id_zasahu + "?", "Vytlač správu zásahu",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    String query
                            = "select\n"
                            + "(zach.nazov_zachranky + ' - '+ob1.nazov) as zachranka,\n"
                            + "id_zasahu as zasah,\n"
                            + "(ou.meno +' '+ou.priezvisko) as pacient,\n"
                            + "ou.rod_cislo,\n"
                            + "ob.nazov,\n"
                            + "(adr.ulica+' '+adr.cislo) as adresa,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd/MM/yyyy HH:mm:ss'))) as datum_zavolania,\n"
                            + "ISNULL((dg1.id_diagnozy+' '+Cast(dg1.nazov as nvarchar(50))),'NEZADANA')as diagPacient,\n"
                            + "ISNULL(pac.info_o_pacientovi,' ') as infoPacient,\n"
                            + "ISNULL(zas.info_o_zasahu,' ') as infoZasah,\n"
                            + "ISNULL((dg2.id_diagnozy+' '+Cast(dg2.nazov as nvarchar(50))),'NEZADANA')as diagZasah,\n"
                            + "ISNULL(zz.nazov_zariadenia,' ')as nemocnica,\n"
                            + "ISNULL(ou2.meno+ou2.priezvisko,ISNULL(ouv.meno+' '+ouv.priezvisko,' '))  as vodic,\n"
                            + "ISNULL(ou4.meno+ou4.priezvisko,ISNULL(ouz.meno+' '+ouz.priezvisko,' '))  as zachranar,\n"
                            + "ISNULL(ou3.meno+ou3.priezvisko,ISNULL(oul.meno+' '+oul.priezvisko,' '))  as lekar,\n"
                            + "ISNULL(ou1.meno+' '+ou1.priezvisko,' ') as operator,\n"
                            + "ISNULL(ou5.meno+' '+ou5.priezvisko,' ')  as prijem,\n"
                            + "ISNULL(ou6.meno+' '+ou6.priezvisko,' ')  as uzavrel,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd/MM/yyyy HH:mm:ss'))) as datum_zavolania,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_posadky,'dd/MM/yyyy HH:mm:ss'))) as datum_vyjazdu_posadky,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_k_pacientovi,'dd/MM/yyyy HH:mm:ss'))) as datum_prijazdu_k_pacientovi,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_od_pacienta,'dd/MM/yyyy HH:mm:ss'))) as datum_vyjazdu_od_pacienta,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_do_zar,'dd/MM/yyyy HH:mm:ss'))) as datum_prijazdu_do_zar,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_zaciatku_navratu,'dd/MM/yyyy HH:mm:ss'))) as datum_zaciatku_navratu,\n"
                            + "(CONVERT(varchar(19),FORMAT(zas.datum_ukoncenia_zasahu,'dd/MM/yyyy HH:mm:ss'))) as datum_ukoncenia_zasahu\n"
                            + "from zasah zas\n"
                            + "join posadka pos on (zas.id_posadky_spz =pos.id_posadky_spz)\n"
                            + "join zachranka zach on (pos.id_zachranky=zach.id_zachranky)\n"
                            + "join adresa adr1 on (zach.id_adresy=adr1.id_adresy)\n"
                            + "join obec ob1 on (adr1.id_obce =ob1.id_obce)\n"
                            + "join pacient pac on(zas.id_pacienta = pac.id_pacienta)\n"
                            + "join os_udaje ou on(pac.rod_cislo = ou.rod_cislo)\n"
                            + "join adresa adr on (zas.id_adresy_zasahu=adr.id_adresy)\n"
                            + "join obec ob on (adr.id_obce = ob.id_obce)\n"
                            + "full outer join zamestnanec_ucet zu1 on (zas.id_operator=zu1.id_zamestnanca)\n"
                            + "full outer join zamestnanec_ucet zu2 on (zas.id_vodic=zu2.id_zamestnanca)\n"
                            + "full outer join zamestnanec_ucet zu3 on (zas.id_lekar=zu3.id_zamestnanca)\n"
                            + "full outer join zamestnanec_ucet zu4 on (zas.id_zachranar=zu4.id_zamestnanca)\n"
                            + "full outer join zamestnanec_ucet zu5 on (zas.id_prijem=zu5.id_zamestnanca)\n"
                            + "full outer join zamestnanec_ucet zu6 on (zas.id_uzavrel=zu6.id_zamestnanca)\n"
                            + "full outer join os_udaje ou1 on (zu1.rod_cislo=ou1.rod_cislo)\n"
                            + "full outer join os_udaje ou2 on (zu2.rod_cislo=ou2.rod_cislo)\n"
                            + "full outer join os_udaje ou3 on (zu3.rod_cislo=ou3.rod_cislo)\n"
                            + "full outer join os_udaje ou4 on (zu4.rod_cislo=ou4.rod_cislo)\n"
                            + "full outer join os_udaje ou5 on (zu5.rod_cislo=ou5.rod_cislo)\n"
                            + "full outer join os_udaje ou6 on (zu6.rod_cislo=ou6.rod_cislo)\n"
                            + "full outer  join diagnozy dg1 on (pac.id_diagnozy =dg1.id_diagnozy)\n"
                            + "full outer  join diagnozy dg2 on (zas.id_diagnozy =dg2.id_diagnozy)\n"
                            + "full outer join zdravotnicke_zariadenie zz on (zas.id_zariadenia = zz.id_zariadenia)\n"
                            + "full outer join zamestnanec_ucet zul on(pos.id_zamestnanca_lekar=zul.id_zamestnanca)\n"
                            + "full outer join os_udaje oul on(zul.rod_cislo = oul.rod_cislo)\n"
                            + "full outer join zamestnanec_ucet pzv on(pzv.id_zamestnanca=pos.id_zamestnanca_vodic)\n"
                            + "full outer join zamestnanec_ucet pzz on(pzz.id_zamestnanca=pos.id_zamestnanca_zachranar)\n"
                            + "full outer join os_udaje ouv on (ouv.rod_cislo = pzv.rod_cislo)\n"
                            + "full outer join os_udaje ouz on (ouz.rod_cislo = pzz.rod_cislo)\n"
                            + "where zas.id_zasahu=" + id_zasahu;
                    System.out.println(query);
                    try {
                        conn = DBConnection.DBConnection();
                        JasperDesign jd = JRXmlLoader.load(System.getProperty("user.home") + "\\desktop\\ZZS\\SpravaOZasahu.jrxml");
                        JRDesignQuery nQ = new JRDesignQuery();
                        nQ.setText(query);
                        jd.setQuery(nQ);
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
                        JasperViewer.viewReport(jp, false);

                        return;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                        return;
                    }
                } else {
                    System.out.println("nie");
                    return;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
            return;
        }

        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete vytlačiť Výjazdový formulár pre zásah :" + id_zasahu + "?", "Výjazdu posádky zo stanice",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            conn = DBConnection.DBConnection();
            try {
                JasperDesign jd = JRXmlLoader.load(System.getProperty("user.home") + "\\desktop\\ZZS\\VyjazdovyFormular.jrxml");
                String sql = "select\n"
                        + "(zach.nazov_zachranky + ' - '+ob1.nazov) as zachranka ,\n"
                        + "id_zasahu as zasah,\n"
                        + "zas.id_posadky_spz as posadka,\n"
                        + "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd/MM/yyyy HH:mm:ss'))) as datum_zavolania,\n"
                        + "(ou.meno +' '+ou.priezvisko) as pacient,\n"
                        + "ou.rod_cislo,\n"
                        + "ob.nazov,\n"
                        + "(adr.ulica+' '+adr.cislo) as adresa,\n"
                        + "('['+substring(CONVERT(VARCHAR(20),CAST(adr.gps.Lat AS decimal(18,14))),0,10) + ' , '\n"
                        + "		+ substring(CONVERT(VARCHAR(20),CAST(adr.gps.Long AS decimal(18,14))),0,10)+']') as gps,\n"
                        + "(ouv.priezvisko+' '+ouv.meno) as vodic ,\n"
                        + "(ouz.priezvisko+' '+ouz.meno) as zachranar,\n"
                        + "(ISNULL(oul.priezvisko,'')+' '+ISNULL(oul.meno,'')) as lekar,\n"
                        + "pac.info_o_pacientovi as info\n"
                        + " from zasah zas\n"
                        + "join posadka pos on (zas.id_posadky_spz =pos.id_posadky_spz)\n"
                        + "join zachranka zach on (pos.id_zachranky=zach.id_zachranky)\n"
                        + "join adresa adr1 on (zach.id_adresy=adr1.id_adresy)\n"
                        + "join obec ob1 on (adr1.id_obce =ob1.id_obce)\n"
                        + "join pacient pac on(zas.id_pacienta = pac.id_pacienta)\n"
                        + "join os_udaje ou on(pac.rod_cislo = ou.rod_cislo)\n"
                        + "join adresa adr on (zas.id_adresy_zasahu=adr.id_adresy)\n"
                        + "join obec ob on (adr.id_obce = ob.id_obce)\n"
                        + "full outer join zamestnanec_ucet zul\n"
                        + "on(pos.id_zamestnanca_lekar=zul.id_zamestnanca)\n"
                        + "full outer  join os_udaje oul\n"
                        + "on(zul.rod_cislo = oul.rod_cislo)\n"
                        + "join zamestnanec_ucet pzv on(pzv.id_zamestnanca=pos.id_zamestnanca_vodic)\n"
                        + "join zamestnanec_ucet pzz on(pzz.id_zamestnanca=pos.id_zamestnanca_zachranar)\n"
                        + "join os_udaje ouv on (ouv.rod_cislo = pzv.rod_cislo)\n"
                        + "join os_udaje ouz on (ouz.rod_cislo = pzz.rod_cislo)\n"
                        + "where zas.id_zasahu=" + id_zasahu;
                System.out.println(sql);
                JRDesignQuery nQ = new JRDesignQuery();
                nQ.setText(sql);
                jd.setQuery(nQ);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
                JasperViewer.viewReport(jp, false);
            } catch (Exception e) {
                return;
            }

        } else {
            System.out.println("nie");
            return;
        }


    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice15ActionPerformed
    /**
     * Metoda, na uzavretie zasahu PROC_SELECT_STAVY_ZASAHU (id_zasahu)-
     * zdielana procedura na zistenie : stav (zasahu),id_posadky_spz a
     * stav_posadky PROC_UPDATE_ZASAH_NAVRAT (id_zasahu) nastavi :
     * datum_ukoncenia_zasahu na DB sysdate,stav na "ukončený", stav_posadky na
     * "Pripravena"
     *
     * @param evt - kliknutie
     */
    private void jB_FZV_UzavriZasahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FZV_UzavriZasahActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky;
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " nie je možné uzavrieť lebo je : " + stav, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("prebiehajúci")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " nie je možné uzavrieť lebo je : " + stav, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete uzavrieť zásah :" + id_zasahu, "Uzvretie zásahu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_UZAVRETIE (?,?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.setInt(2, zamestnanec);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
            JFZachrankaVelitel.repaint();
        } else {
            System.out.println("nie");
        }
    }//GEN-LAST:event_jB_FZV_UzavriZasahActionPerformed

    private void jTextCASActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextCASActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextCASActionPerformed

    private void jButtonOdhlasenie3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOdhlasenie3ActionPerformed
        String pom = JTFOSPrih.getText();
        //String pom1 = jTextFieldZdravotnickeZariadenie.getText();

        int result = JOptionPane.showOptionDialog(this, pom + ",naozaj sa chcete odhlásiť zo služby \n v operačnom stredisku ?", "Odlásenie zo služby",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            jComboBoxUserType.setSelectedItem("Odhlásenie");
            jFOknoOperator.setVisible(false);
            this.setVisible(true);

        } else {
            System.out.println("noAction");
        }
    }//GEN-LAST:event_jButtonOdhlasenie3ActionPerformed

    private void jCKB_pozicia_vodicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_pozicia_vodicActionPerformed
        /**
         * jCKB_pozicia_vsetci.setSelected(false);
         * jCKB_pozicia_lekar.setSelected(false);
         * jCKB_pozicia_velitel.setSelected(false);
         * jCKB_pozicia_zachranar.setSelected(false);*
         */
    }//GEN-LAST:event_jCKB_pozicia_vodicActionPerformed

    private void jCKB_pozicia_lekarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_pozicia_lekarActionPerformed
        /**
         * jCKB_pozicia_vsetci.setSelected(false);
         * jCKB_pozicia_vodic.setSelected(false);
         * jCKB_pozicia_velitel.setSelected(false);
         * jCKB_pozicia_zachranar.setSelected(false);*
         */
    }//GEN-LAST:event_jCKB_pozicia_lekarActionPerformed

    private void jCKB_pozicia_zachranarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_pozicia_zachranarActionPerformed
        /**
         * jCKB_pozicia_vsetci.setSelected(false);
         * jCKB_pozicia_vodic.setSelected(false);
         * jCKB_pozicia_velitel.setSelected(false);
         * jCKB_pozicia_lekar.setSelected(false);*
         */
    }//GEN-LAST:event_jCKB_pozicia_zachranarActionPerformed

    private void jCKB_pozicia_velitelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_pozicia_velitelActionPerformed
        /**
         * jCKB_pozicia_vsetci.setSelected(false);
         * jCKB_pozicia_vodic.setSelected(false);
         * jCKB_pozicia_zachranar.setSelected(false);
         * jCKB_pozicia_lekar.setSelected(false);*
         */
    }//GEN-LAST:event_jCKB_pozicia_velitelActionPerformed

    private void jB_AZ_ZobrazZamestnancovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_ZobrazZamestnancovActionPerformed
        jCB_zamestnanci.removeAllItems();

        String stav = "";
        String pozicia = "(";
        if (jCKB_stav_v_sluzbe.isSelected()) {
            stav = "('pracuje')";

        }
        if (jCKB_stav_volno.isSelected()) {
            stav = "('nepracuje')";

        }
        if (jCKB_stav_vsetkych.isSelected()) {
            stav = "('pracuje','nepracuje')";

        }
        if (jCKB_stav_vsetkych.isSelected() == false
                && jCKB_stav_volno.isSelected() == false
                && jCKB_stav_v_sluzbe.isSelected() == false) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste stav zamestnanca", "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        if (jCKB_pozicia_lekar.isSelected() == false
                && jCKB_pozicia_vodic.isSelected() == false
                && jCKB_pozicia_zachranar.isSelected() == false
                && jCKB_pozicia_velitel.isSelected() == false) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste pracovnú pozíciu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (jCKB_pozicia_lekar.isSelected()) {
            pozicia += "'lekar zachranky',";
        }
        if (jCKB_pozicia_vodic.isSelected()) {
            pozicia += "'vodic',";
        }
        if (jCKB_pozicia_zachranar.isSelected()) {
            pozicia += "'zachranar',";

        }
        if (jCKB_pozicia_velitel.isSelected()) {
            pozicia += "'velitel zachranky',";

        }
        //System.out.println(pozicia);
        pozicia = pozicia.substring(0, pozicia.length() - 1) + ")";
        //System.out.println(pozicia);

        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            rs = st.executeQuery(
                    "	select ou.meno +' '+ ou.priezvisko from zachranka zach\n"
                    + "	join pracujuci_zamestnanci pz on (zach.id_zachranky=pz.id_zachranky)\n"
                    + "	join zamestnanec_ucet zu on (pz.id_zamestnanca= zu.id_zamestnanca)\n"
                    + "	join os_udaje ou on (zu.rod_cislo = ou.rod_cislo)\n"
                    + " where pz.stav_zamestnanca in " + stav
                    + "	and zu.prac_pozicia in"
                    + pozicia
                    + "and zach.id_zachranky = " + zamestnavatel
            );

            while (rs.next()) {
                //jCB_NZ_typDiagnozy.addItem("všetky"); 
                jCB_zamestnanci.addItem(rs.getString(1));

            }
            jCB_zamestnanci.addItem("Nikto");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jB_AZ_ZobrazZamestnancovActionPerformed

    private void jCKB_stav_vsetkychActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_stav_vsetkychActionPerformed
        if (jCKB_stav_vsetkych.isSelected()) {
            jCKB_stav_v_sluzbe.setSelected(true);
            jCKB_stav_volno.setSelected(true);
        } else {
            jCKB_stav_v_sluzbe.setSelected(false);
            jCKB_stav_volno.setSelected(false);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jCKB_stav_vsetkychActionPerformed

    private void jCKB_stav_v_sluzbeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_stav_v_sluzbeActionPerformed
        if (jCKB_stav_v_sluzbe.isSelected()) {
            jCKB_stav_vsetkych.setSelected(false);
            jCKB_stav_volno.setSelected(false);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jCKB_stav_v_sluzbeActionPerformed

    private void jCKB_stav_volnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_stav_volnoActionPerformed
        if (jCKB_stav_volno.isSelected()) {
            jCKB_stav_v_sluzbe.setSelected(false);
            jCKB_stav_vsetkych.setSelected(false);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jCKB_stav_volnoActionPerformed

    private void jB_AZ_PrijazdDoNemocnice8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice8ActionPerformed
        int info;
        String meno = "";
        String spz = "";
        String typ_posadky = "";
        String pozicia = "";
        String stavPom = "";
        int j = -1;
        int i = -1;
        try {
            i = jTablePosadkyZachranky.getSelectedRow();
            j = jTablePosadkyZachranky.getSelectedColumn();
            meno = jCB_zamestnanci.getSelectedItem().toString();
            spz = jTablePosadkyZachranky.getValueAt(i, 0).toString();

            typ_posadky = jTablePosadkyZachranky.getValueAt(i, 1).toString();
            System.out.println("*****************************");
            System.out.println("i=" + i);
            System.out.println("j=" + j);
            System.out.println("*****************************");
            System.out.println(meno);
            System.out.println(spz);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste posádku alebo náhradu", "Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            String s = "Select stav_posadky from posadka"
                    + " where id_posadky_spz like '" + spz + "'";
            System.out.println(s);
            rs = st.executeQuery(s);
            while (rs.next()) {
                stavPom = rs.getString(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
            return;
        }
        System.out.println("z databazy" + stavPom);
        System.out.println("vybrany" + meno);
        if (j == 5) {
            if (stavPom.equals("Pripravena") || stavPom.equals("MimoPrevadzky")) {//||(meno.equals("MimoPrevadzky")|| meno.equals("Pripravena")) 
                if (stavPom.equals(meno)) {
                    JOptionPane.showMessageDialog(null, "Zmena stavu posádky nie je možná, lebo už sa nachádza v požadovanom stave", "Zmena typu posádky", JOptionPane.INFORMATION_MESSAGE);
                    ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                    JFZachrankaVelitel.repaint();
                    return;
                }
                try {
                    conn = DBConnection.DBConnection();
                    st = conn.createStatement();
                    String s = "Update posadka \n"
                            + "set stav_posadky ='" + meno + "'"
                            + "where id_posadky_spz ='" + spz + "'";
                    st.executeUpdate(s);
                    ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                    JFZachrankaVelitel.repaint();
                    st.close();
                    conn.close();
                    JOptionPane.showMessageDialog(null, "Úspešne ste zmenili stav posádky", "Zmena stab posádky", JOptionPane.INFORMATION_MESSAGE);
                    ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                    JFZachrankaVelitel.repaint();
                    return;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zmena stavu posádky nie je možná, lebo posádka sa nachádza priamo na výjazde", "Zmena stavu posádky", JOptionPane.ERROR_MESSAGE);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
            }
        }
        if (j == 1) {
            if (typ_posadky.equals(meno)) {
                JOptionPane.showMessageDialog(null, "Zmena typu posádky sa neuskutočnila,lebo ste zadali rovnaký typ posádky aký bol", "Chyba pri zmene typu posádky", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!jTablePosadkyZachranky.getValueAt(i, 4).toString().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Zmena typu posádky sa neuskutočnila,lebo je pri posádke zapísaný lekár", "Chyba pri zmene typu posádky", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                conn = DBConnection.DBConnection();
                st = conn.createStatement();
                String s = "Update posadka \n"
                        + "set typ_posadky ='" + meno + "'"
                        + "where id_posadky_spz ='" + spz + "'";
                st.executeUpdate(s);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));

                st.close();
                conn.close();
                JOptionPane.showMessageDialog(null, "Úspešne ste zmenili typ posádky", "Zmena typu posádky", JOptionPane.INFORMATION_MESSAGE);
                return;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                return;
            }

        }
        if (j >= 2 && j <= 4) {
            if (j == 4 && jTablePosadkyZachranky.getValueAt(i, 1).toString().equals("RZP")) {
                JOptionPane.showMessageDialog(null, "Nie je možné prideliť lekára, lebo posádka nie je RLP ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (j == 2) {
                pozicia = "vodic";
            }
            if (j == 3) {
                pozicia = "zachranar";
            }
            if (j == 4) {
                pozicia = "lekar zachranky";
            }
            try {
                System.out.println(pozicia);
                System.out.println(spz);
                System.out.println(meno);
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_SELECT_VYMENA_ZAMESTNANCOV_V_POSADKE (?,?,?,?)}");
                stored_pro.setString(1, pozicia);
                stored_pro.setString(2, spz);
                stored_pro.setString(3, meno);
                stored_pro.registerOutParameter(4, java.sql.JDBCType.INTEGER);
                System.out.println("'" + pozicia + "','" + spz + "','" + meno + "'");
                stored_pro.executeUpdate();
                info = stored_pro.getInt(4);
                System.out.println(info);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                return;
            }
            if (info == 4) {
                JOptionPane.showMessageDialog(null, "Výmena bola dokončená", "Výmena zamestnanca", JOptionPane.INFORMATION_MESSAGE);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
                return;
            }
            if (info == 5) {
                JOptionPane.showMessageDialog(null, "Odhlásenie zamestnanca bolo úspešné", "Odhlásenie zamestnanca", JOptionPane.INFORMATION_MESSAGE);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
                return;
            }
            if (info == 1) {
                JOptionPane.showMessageDialog(null, "Výmena nebola vykonaná, lebo vybraní osoba nie je vodič", "Info", JOptionPane.INFORMATION_MESSAGE);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
                return;
            }
            if (info == 2) {
                JOptionPane.showMessageDialog(null, "Výmena nebola vykonaná, lebo vybraní osoba nie je lekár", "Info", JOptionPane.INFORMATION_MESSAGE);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
                return;
            }
            if (info == 3) {
                JOptionPane.showMessageDialog(null, "Výmena nebola vykonaná, lebo vybraní osoba nie je záchranár", "Info", JOptionPane.INFORMATION_MESSAGE);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
                return;
            }
            if (info == 7) {
                JOptionPane.showMessageDialog(null, "Výmena nebola vykonaná, lebo vybraná osoba už pracuje", "Info", JOptionPane.INFORMATION_MESSAGE);
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
                return;
            }

        }
    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice8ActionPerformed

    private void jCKB_pozicia_vsetciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCKB_pozicia_vsetciActionPerformed
        if (jCKB_pozicia_vsetci.isSelected()) {
            jCKB_pozicia_lekar.setSelected(true);
            jCKB_pozicia_velitel.setSelected(true);
            jCKB_pozicia_vodic.setSelected(true);
            jCKB_pozicia_zachranar.setSelected(true);
        } else {
            jCKB_pozicia_lekar.setSelected(false);
            jCKB_pozicia_velitel.setSelected(false);
            jCKB_pozicia_vodic.setSelected(false);
            jCKB_pozicia_zachranar.setSelected(false);
        }
    }//GEN-LAST:event_jCKB_pozicia_vsetciActionPerformed

    private void jB_Velitel_nova_posadkaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_Velitel_nova_posadkaActionPerformed

        if (jB_Velitel_nova_posadka.getText().equals("Vytvor novú sanitku")) {
            jB_Velitel_nova_posadka.setText("Pridaj sanitku");
            jCB_zamestnanci.setEditable(true);
            jCB_zamestnanci.removeAllItems();
            jCB_zamestnanci.setSelectedItem("Sem zadaj novú špz vozidla");
            jCB_RLP.setVisible(true);
            jCB_RZP.setVisible(true);

        } else {
            String typ = "";
            String spz;
            //try {
            spz = jCB_zamestnanci.getSelectedItem().toString();
            if (jCB_RLP.isSelected()) {
                typ = "RLP";
            }
            if (jCB_RZP.isSelected()) {
                typ = "RZP";
            }
            //else {
            //      JOptionPane.showMessageDialog(null, "Nevybrali ste typ novej posádky", "Error", JOptionPane.ERROR_MESSAGE);
            //      return;
            // }
            //} catch (Exception e) {
            //   return;
            //}

            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_PRIDAJ_SANITKU (?,?,?,?)}");
                stored_pro.setInt(1, zamestnavatel);
                stored_pro.setString(2, spz);
                stored_pro.setString(3, typ);
                stored_pro.registerOutParameter(4, java.sql.JDBCType.INTEGER);
                System.out.println("'" + zamestnavatel + "','" + spz + "','" + typ + "'");
                stored_pro.executeUpdate();
                info = stored_pro.getInt(4);
                System.out.println(info);
                jCB_RLP.setVisible(false);
                jCB_RZP.setVisible(false);
                jCB_zamestnanci.setEditable(false);
                jCB_zamestnanci.removeAllItems();
                jCB_zamestnanci.setSelectedItem("");
                jB_Velitel_nova_posadka.setText("Vytvor novú sanitku");
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));

            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "Nezadali ste : " + errType, "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

        }


    }//GEN-LAST:event_jB_Velitel_nova_posadkaActionPerformed

    private void jCB_zamestnanciMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCB_zamestnanciMouseClicked
        if (jCB_zamestnanci.isEditable()) {
            jCB_zamestnanci.removeAllItems();
            jCB_zamestnanci.setSelectedItem("");
        }
    }//GEN-LAST:event_jCB_zamestnanciMouseClicked

    private void jCB_RLPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_RLPActionPerformed
        if (jCB_RLP.isSelected()) {
            jCB_RZP.setSelected(false);
        }
    }//GEN-LAST:event_jCB_RLPActionPerformed

    private void jCB_RZPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_RZPActionPerformed
        if (jCB_RZP.isSelected()) {
            jCB_RLP.setSelected(false);
        }
    }//GEN-LAST:event_jCB_RZPActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ShowZasahNemocnice(getZasahPreNemocnicu(zamestnavatel));
        JEP_NEM_TextArea.setText("");
        JFNemocnicaGUI.repaint();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
        jEPHistoriaVelitel.setText("");
        JFZachrankaVelitel.repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed
    int counter = 0;
    private void JFZachrankaVelitelWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_JFZachrankaVelitelWindowOpened
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Show_aktualneZasahyZachranky(getAktualneZasahyZachranky(zamestnavatel));
                ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
                JFZachrankaVelitel.repaint();
                //System.out.println("TimerTask executing counter is: " + counter);
                //counter++;//increments the counter
            }
        };
        Timer timer = new Timer("MyTimer");//create a new Timer

        timer.scheduleAtFixedRate(timerTask, 30, 60 * 1000);
    }//GEN-LAST:event_JFZachrankaVelitelWindowOpened

    private void JFNemocnicaGUIWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_JFNemocnicaGUIWindowOpened
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ShowZasahNemocnice(getZasahPreNemocnicu(zamestnavatel));   // naplnenie JTable            
                JFNemocnicaGUI.repaint();
                System.out.println("TimerTask executing counter is: " + counter);
                counter++;//increments the counter
            }
        };
        Timer timer = new Timer("MyTimer");//create a new Timer
        timer.scheduleAtFixedRate(timerTask, 30, 60 * 1000);        // TODO add your handling code here:
    }//GEN-LAST:event_JFNemocnicaGUIWindowOpened

    private void jB_AZ_PrijazdDoNemocnice9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice9ActionPerformed
        JEP_NEM_TextArea.setText("");
        int id_zasahu;
        String s = "";
        try {
            int i = JTabNemocnicaZasah.getSelectedRow();
            id_zasahu = Integer.parseInt(JTabNemocnicaZasah.getValueAt(i, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_INFA_NEMOCNICA_HISTORIA (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                s += "<html>"
                        + "<body>"
                        + "<p><b>[Dátum tiesňového volania: " + (rs.getString(2)) + "][Id zásahu :" + (rs.getString(1)) + "</b>]<br>"
                        + "<b>[" + (rs.getString(9)) + "," + (rs.getString(7)) + "," + (rs.getString(8)) + "]</b>"
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodná diagnóza :</b><br>"
                        + "" + (rs.getString(5)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodné info o pacientovi :</b><br>"
                        + "" + (rs.getString(6)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Konečná diagnóza diagnóza :</b><br>"
                        + "" + (rs.getString(4)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Info o zasahu :</b><br>"
                        + "" + (rs.getString(3)) + ""
                        + "</p>"
                        + "<hr>";
                //+ "</body>"
                //+ "</html>";
                while (rs.next()) {
                    s += "<p><b>[Id zásahu :" + (rs.getString(1)) + "]<br>[Dátum tiesňového volania: " + (rs.getString(2)) + "]<br>"
                            + "<b>[" + (rs.getString(9)) + ", " + (rs.getString(7)) + ", " + (rs.getString(8)) + "]</b>"
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Pôvodná diagnóza :</b><br>"
                            + "" + (rs.getString(5)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Pôvodné info o pacientovi :</b><br>"
                            + "" + (rs.getString(6)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Konečná diagnóza diagnóza :</b><br>"
                            + "" + (rs.getString(4)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Info o zasahu :</b><br>"
                            + "" + (rs.getString(3)) + ""
                            + "</p>"
                            + "<hr>";
                }
                //}
                s += "</body>"
                        + "</html>";
            }
            rs.close();
            stored_pro.close();
            conn.close();
            JEP_NEM_TextArea.setText(s);
            System.out.println(s);
            JFNemocnicaGUI.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice9ActionPerformed

    private void jFOpravZasahWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jFOpravZasahWindowOpened

        jCB_OZ_Pp_Diag.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                String pom = jCB_OZ_Pp_Diag.getEditor().getItem().toString();
                if (evt.getKeyCode() >= 65 && evt.getKeyCode() <= 90 || evt.getKeyLocation() > 96 && evt.getKeyCode() <= 105 || evt.getKeyCode() == 8) {
                    jCB_OZ_Pp_Diag.setModel(getDiagnozy(pom, "všetky"));
                    if (jCB_OZ_Pp_Diag.getItemCount() > 0) {
                        jCB_OZ_Pp_Diag.showPopup();
                        if (evt.getKeyCode() != 8) {
                            ((JTextComponent) jCB_OZ_Pp_Diag.getEditor().getEditorComponent()).select(pom.length(), jCB_OZ_Pp_Diag.getEditor().getItem().toString().length());
                        } else {
                            jCB_OZ_Pp_Diag.getEditor().setItem(pom);
                        }
                    } else {
                        jCB_OZ_Pp_Diag.addItem(pom);
                    }
                }
            }
        });
        jCB_OZ_Konecna_Diag.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                String pom = jCB_OZ_Konecna_Diag.getEditor().getItem().toString();
                if (evt.getKeyCode() >= 65 && evt.getKeyCode() <= 90 || evt.getKeyLocation() > 96 && evt.getKeyCode() <= 105 || evt.getKeyCode() == 8) {
                    jCB_OZ_Konecna_Diag.setModel(getDiagnozy(pom, "všetky"));
                    if (jCB_OZ_Konecna_Diag.getItemCount() > 0) {
                        jCB_OZ_Konecna_Diag.showPopup();
                        if (evt.getKeyCode() != 8) {
                            ((JTextComponent) jCB_OZ_Konecna_Diag.getEditor().getEditorComponent()).select(pom.length(), jCB_OZ_Konecna_Diag.getEditor().getItem().toString().length());
                        } else {
                            jCB_OZ_Konecna_Diag.getEditor().setItem(pom);
                        }
                    } else {
                        jCB_OZ_Konecna_Diag.addItem(pom);
                    }
                }
            }
        });


    }//GEN-LAST:event_jFOpravZasahWindowOpened

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_OPRAV_ZASAH (?)}");
            stored_pro.setInt(1, Integer.parseInt(jTF_OZ_IdZasahu.getText()));
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                jTF_OZ_tel.setText(rs.getString(1));
                System.out.println(rs.getString(1));
                jTF_OZ_meno.setText(rs.getString(2));
                jTF_OZ_priezvisko.setText(rs.getString(3));
                jTF_OZ_rodCislo.setText(rs.getString(4));
                jTF_OZ_ulica.setText(rs.getString(5));
                jTF_OZ_PopisCislo.setText(rs.getString(6));
                jCB_OZ_obce.setSelectedItem(rs.getString(7));
                jCB_OZ_Pp_Diag.setSelectedItem(rs.getString(8));
                JTA_OZ_infopacient.setText(rs.getString(9));
                JTA_OZ_infozasah.setText(rs.getString(10));
                jCB_OZ_Konecna_Diag.setSelectedItem(rs.getString(11));
                jCBNemocnice1.setSelectedItem(rs.getString(12));
                jTF_OZ_IdZasahu.setText(rs.getString(13));
            }
            rs.close();
            stored_pro.close();
            conn.close();
            jFOpravZasah.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        jFOpravZasah.setVisible(true); // TODO add your handling code here:      // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCBNemocnice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBNemocnice1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBNemocnice1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String errType = "";
        try {
            if (jTF_OZ_tel.getText().isEmpty()) {
                errType = "telefon";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTF_OZ_meno.getText().isEmpty()) {
                errType += "meno, ";
                jTF_OZ_meno.setText("");
                //throw new IllegalArgumentException("INVALID");
            }
            if (jTF_OZ_priezvisko.getText().isEmpty()) {
                errType += "priezvisko, ";
                jTF_OZ_priezvisko.setText("");
                //throw new IllegalArgumentException("INVALID");
            }
            if (jTF_OZ_rodCislo.getText().isEmpty()) {
                errType = "rodCislo";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTF_OZ_ulica.getText().isEmpty()) {
                errType = "ulica";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTF_OZ_PopisCislo.getText().isEmpty()) {
                errType += "PopisCislo, ";
                //throw new IllegalArgumentException("INVALID");
                jTF_OZ_PopisCislo.setText("");
            }
            if (jCB_OZ_obce.getSelectedIndex() == -1 && jCB_OZ_obce.getSelectedItem().toString().isEmpty()) {
                errType = "Obec";
                throw new IllegalArgumentException("INVALID");
            }
            if (JTA_OZ_infozasah.getText().isEmpty()) {
                errType += "InfoOZasahu, ";
                JTA_OZ_infozasah.setText("");
                //throw new IllegalArgumentException("INVALID");
            }
            if (JTA_OZ_infopacient.getText().isEmpty()) {
                errType += "InfoOPacientovi, ";
                JTA_OZ_infopacient.setText("");
                //throw new IllegalArgumentException("INVALID");
            }
            if (jCBNemocnice1.getSelectedIndex() == -1) {
                errType += "Nemocnica, ";
                jCBNemocnice1.setSelectedItem("NEZADANÁ");
                //throw new IllegalArgumentException("INVALID");
            }
            if (jCB_OZ_Pp_Diag.getSelectedIndex() == -1) {
                errType += "Počiatočná diagnóza, ";
                //throw new IllegalArgumentException("INVALID");
                jCB_OZ_Pp_Diag.setSelectedItem("NEZADANÁ");
            }
            if (jCB_OZ_Konecna_Diag.getSelectedIndex() == -1) {
                errType += "Konečná diagnóza, ";
                jCB_OZ_Konecna_Diag.setSelectedItem("NEZADANÁ");
                //throw new IllegalArgumentException("INVALID");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Nezadali ste: " + errType + " ak neviete opraviť údaje, \n pokračujte stlačením \"refresh\" ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete opraviť zásah: " + jTF_OZ_IdZasahu.getText()
                + "\n aj napriek nezadaným údajom:" + errType + "?",
                "Oprava zásahu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_CELY_ZASAH(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                stored_pro.setInt(1, Integer.parseInt(jTF_OZ_IdZasahu.getText()));
                stored_pro.setString(2, jTF_OZ_tel.getText());
                stored_pro.setString(3, jTF_OZ_meno.getText());
                stored_pro.setString(4, jTF_OZ_priezvisko.getText());
                stored_pro.setString(5, jTF_OZ_rodCislo.getText());
                stored_pro.setString(6, jTF_OZ_ulica.getText());
                stored_pro.setString(7, jTF_OZ_PopisCislo.getText());
                stored_pro.setString(8, jCB_OZ_obce.getSelectedItem().toString());

                stored_pro.setString(9, jCBNemocnice1.getSelectedItem().toString());
                //stored_pro.setString(9, jCBNemocnice1.getSelectedItem().toString());
                stored_pro.setString(10, JTA_OZ_infozasah.getText());
                stored_pro.setString(11, JTA_OZ_infopacient.getText());
                stored_pro.setString(12, jCB_OZ_Pp_Diag.getSelectedItem().toString());
                stored_pro.setString(13, jCB_OZ_Konecna_Diag.getSelectedItem().toString());
                stored_pro.executeUpdate();
                System.out.println(stored_pro.toString());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage() + e.getClass().getCanonicalName());
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOpravZasah.setVisible(false);
        } else {
            System.out.println("no action");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTF_OZ_priezviskoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTF_OZ_priezviskoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTF_OZ_priezviskoActionPerformed

    private void jTF_OZ_menoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTF_OZ_menoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTF_OZ_menoActionPerformed

    private void jTF_OZ_rodCisloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTF_OZ_rodCisloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTF_OZ_rodCisloActionPerformed

    private void jB_AZ_PrijazdDoNemocnice11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice11ActionPerformed
        SimpleDateFormat dcn = new SimpleDateFormat("dd.MM.yyyy");
        String datumOd = "";
        String datumDo = "";
        String zamestnanec = "";

        try {
            datumOd = dcn.format(jDateChooserOD2.getDate());
            datumDo = dcn.format(jDateChooserDO2.getDate());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste dátum");
        }
        try {
            zamestnanec = jCB_zamestnanci.getSelectedItem().toString();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zamestnanca");
        }
        String query
                = "Declare @dat1 date \n"
                + "Declare @dat2 date\n"
                + "Declare @meno nVarchar(max)\n"
                + "Declare @id_zamestnavatela int \n"
                + "set @dat1 =CONVERT(date,'" + datumOd + "',104)\n"
                + "set @dat2 =CONVERT(date,'" + datumDo + "',104) \n"
                + "set @meno ='" + zamestnanec + "'\n"
                + "set @id_zamestnavatela =" + zamestnavatel + " \n"
                + "select pz.id_zamestnanca, CONCAT(ou.meno,' ',ou.priezvisko) as meno,\n"
                + "FORMAT(datum_prichodu,'dd/MM/yyyy hh:mm') as prichod,\n"
                + "FORMAT(datum_odchodu,'dd/MM/yyyy hh:mm') as odchod,\n"
                + "DATEDIFF(minute,datum_prichodu,datum_odchodu) as rozdiel,\n"
                + "concat (zach.nazov_zachranky,'-',obzach.nazov) as zachranka\n"
                + "from dochadzka do \n"
                + "join zamestnanec_ucet zu on (do.id_zamestnanca=zu.id_zamestnanca)\n"
                + "join pracujuci_zamestnanci pz on (zu.id_zamestnanca=pz.id_zamestnanca)\n"
                + "join os_udaje ou on (zu.rod_cislo=ou.rod_cislo)\n"
                + "join zachranka zach on (pz.id_zachranky = zach.id_zachranky)\n"
                + "join adresa adrzach on (zach.id_adresy =adrzach.id_adresy)\n"
                + "join obec obzach on (adrzach.id_obce = obzach.id_obce)\n"
                + "where pz.id_zachranky =@id_zamestnavatela\n"
                + "and CONVERT(date,do.datum_odchodu) between \n"
                + "@dat1 and @dat2\n"
                + "and ou.meno+' '+ou.priezvisko like @meno";
        conn = DBConnection.DBConnection();
        try {
            JasperDesign jd = JRXmlLoader.load(System.getProperty("user.home") + "\\desktop\\ZZS\\Dochadzka.jrxml");
            //JasperDesign jd = JRXmlLoader.load("C:\\Users\\Peter Rendek\\Documents\\NetBeansProjects\\Praca\\src\\praca\\Dochadzka.jrxml");
            JRDesignQuery nQ = new JRDesignQuery();
            nQ.setText(query);
            jd.setQuery(nQ);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }


    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice11ActionPerformed

    private void jB_AZ_PrijazdDoNemocnice10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice10ActionPerformed

        if (jB_AZ_PrijazdDoNemocnice10.getText().equals("Vytvor Zamestnanca")) {
            jTextField10.setText("Meno");
            jTextField11.setText("Priezvisko");
            jTextField12.setText("Rodné číslo (XXXXXX/XXXX)");
            jTextField13.setText("Číslo OP");
            jTextField14.setText("Obec");
            jTextField15.setText("Ulica");
            jTextField16.setText("Popisné číslo");
            jTextField10.setVisible(true);
            jTextField11.setVisible(true);
            jTextField12.setVisible(true);
            jTextField13.setVisible(true);
            jTextField14.setVisible(true);
            jTextField15.setVisible(true);
            jTextField16.setVisible(true);
            jTextField12.setEditable(true);
            jCB_zamestnanci.setEditable(false);
            jCB_zamestnanci.removeAllItems();
            jCB_zamestnanci.setSelectedItem("Vyber pracovnú pozíciu");
            try {
                conn = DBConnection.DBConnection();
                st = conn.createStatement();
                rs = st.executeQuery(
                        "select zu.prac_pozicia from zamestnanec_ucet zu\n"
                        + "join pracujuci_zamestnanci pz on (zu.id_zamestnanca = pz.id_zamestnanca)\n"
                        + "where pz.id_zachranky is not null\n"
                        + "group by (zu.prac_pozicia)");
                while (rs.next()) {
                    jCB_zamestnanci.addItem(rs.getString(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            jB_AZ_PrijazdDoNemocnice10.setText("Pridaj Zamestnanca");
            return;
        }

        if (jB_AZ_PrijazdDoNemocnice10.getText().equals("Pridaj Zamestnanca")) {
            String errType = "";
            //int infoo = 3;
            try {
                if (jCB_zamestnanci.getSelectedItem().toString().isEmpty()) {
                    errType += " Pracovná pozícia\n";
                    throw new IllegalArgumentException("INVALID");
                }

                if (jTextField10.getText().isEmpty() || jTextField10.getText().equals("Meno")) {
                    errType += " Meno \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField11.getText().isEmpty() || jTextField11.getText().equals("Priezvisko")) {
                    errType += " Priezvisko \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if ((jTextField12.getText().isEmpty() || jTextField12.getText().equals("Rodné číslo (XXXXXX/XXXX)")) || jTextField12.getText().length() != 11) {
                    errType += " Zle zadané číslo \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if ((jTextField13.getText().isEmpty() || jTextField13.getText().equals("Číslo OP")) || jTextField13.getText().length() != 8) {
                    errType += " Zle zadané Číslo OP\n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField14.getText().isEmpty() || jTextField14.getText().equals("Obec")) {
                    errType += " Obec \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField15.getText().isEmpty() || jTextField15.getText().equals("Ulica")) {
                    errType += " Ulica \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField16.getText().isEmpty() || jTextField16.getText().equals("Popisné číslo")) {
                    errType += " Popisné číslo \n";
                    throw new IllegalArgumentException("INVALID");
                }
                String pozicia = jCB_zamestnanci.getSelectedItem().toString();
                String meno = jTextField10.getText();
                String priezvisko = jTextField11.getText();
                String rodne_cislo = jTextField12.getText();
                String cisloOP = jTextField13.getText();
                String obec = jTextField14.getText();
                String ulica = jTextField15.getText();
                String PopisnéCis = jTextField15.getText();
                String adr = obec + "%7c" + ulica + "%7c" + PopisnéCis;
                System.out.println(adr);
                adr = adr.replace(" ", "+");
                Adresa adr1 = new Adresa();
                try {
                    adr1.setLocationsByAdress(adr);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                }

                String lng = adr1.getLng();
                String lat = adr1.getLat();
                String GPS = lng + " " + lat;
                System.out.println(GPS);

                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_CREATE_ZAMESTNANEC_SKRATENE (?,?,?,?,?,?,?,?,?,?,?)}");
                stored_pro.setString(1, pozicia);
                stored_pro.setString(2, meno);
                stored_pro.setString(3, priezvisko);
                stored_pro.setString(4, rodne_cislo);
                stored_pro.setString(5, cisloOP);
                stored_pro.setString(6, obec);
                stored_pro.setString(7, ulica);
                stored_pro.setString(8, PopisnéCis);
                stored_pro.setInt(9, zamestnavatel);
                stored_pro.setString(10, GPS);
                stored_pro.registerOutParameter(11, java.sql.Types.INTEGER);
                stored_pro.executeUpdate();
                int infoo = stored_pro.getInt(11);
                stored_pro.close();
                conn.close();
                jTextField10.setVisible(false);
                jTextField11.setVisible(false);
                jTextField12.setVisible(false);
                jTextField13.setVisible(false);
                jTextField14.setVisible(false);
                jTextField15.setVisible(false);
                jTextField16.setVisible(false);

                jB_AZ_PrijazdDoNemocnice10.setText("Vytvor Zamestnanca");

                jTextField10.setText("Meno");
                jTextField11.setText("Priezvisko");
                jTextField12.setText("Rodné číslo formát XXXXXX/XXXX");
                jTextField13.setText("Číslo OP");
                jTextField14.setText("Obec");
                jTextField15.setText("Ulica");
                jTextField15.setText("Popisné číslo");
                String login = rodne_cislo.substring(0, 2);
                login = meno + login;
                String heslo = rodne_cislo.substring(7, 11);
                if (infoo == 1) {

                    JOptionPane.showMessageDialog(null, "Zamestnanec úspešne pridaný"
                            + "\n Prihlasovacie údaje +=> login:" + login
                            + "\n heslo:"+heslo, "Úspešne pridaný zamestnanec", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (infoo == 2) {
                    JOptionPane.showMessageDialog(null, "Zamestnanec úspešne pridaný bez adresy"
                            + "\n Prihlasovacie údaje +=> login:" + login
                            + "\n heslo:"+heslo, "Úspešne pridaný zamestnanec", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Nezadali ste všetky potrebné údaje, chýba:" + errType,
                        "Chýbajú potrebné údaje", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (StringIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Rodné číslo nie je v požadovanom tvare ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                return;
            }

            jTextField10.setVisible(false);
            jTextField11.setVisible(false);
            jTextField12.setVisible(false);
            jTextField13.setVisible(false);
            jTextField14.setVisible(false);
            jTextField15.setVisible(false);
            jTextField16.setVisible(false);

            jB_AZ_PrijazdDoNemocnice10.setText("Vytvor Zamestnanca");

            jTextField10.setText("Meno");
            jTextField11.setText("Priezvisko");
            jTextField12.setText("Rodné číslo formát XXXXXX/XXXX");
            jTextField13.setText("Číslo OP");
            jTextField14.setText("Obec");
            jTextField15.setText("Ulica");
            jTextField15.setText("Popisné číslo");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice10ActionPerformed

    private void jFOknoOperatorWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jFOknoOperatorWindowOpened
        jDateChooserOD.setVisible(false);
        jDateChooserDO.setVisible(false);
        jOd.setVisible(false);
        jDo.setVisible(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Show_Posadky_In_Table(getPosadkyListZaklad());
                Show_aktualneZasahy(getAktualneZasahy());
                jFOknoOperator.repaint();
            }
        };
        Timer timer = new Timer("MyTimer");//create a new Timer
        timer.scheduleAtFixedRate(timerTask, 30, 60 * 1000);           // TODO add your handling code here:
    }//GEN-LAST:event_jFOknoOperatorWindowOpened

    /**
     *
     * @param query
     * @return
     */
    public DefaultTableModel DajStatistiku(String query) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            conn = DBConnection.DBConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            System.out.println("" + columnCount);
            Object[] hlavicky = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                hlavicky[i] = rsmd.getColumnName(i + 1);
                //System.out.println(""+hlavicky[i]);
            }
            model.setColumnIdentifiers(hlavicky);
            //ResultSet rst = st.executeQuery(query);
            while (rs.next()) {
                Object[] data = new Object[model.getColumnCount()];
                for (int i = 0; i < model.getColumnCount(); i++) {
                    data[i] = rs.getObject(i + 1);
                    // System.out.println(""+data[i]);
                }
                model.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return model;
    }

    /**
     * Metóda slúžiaca na vyplnenie Tabuľky štatistiky ZZS
     *
     * @param evt-kliknutie
     */
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        SimpleDateFormat dcn = new SimpleDateFormat("dd.MM.yyyy");      //inicializacia prem
        String datumOd = "";
        String datumDo = "";
        String datum = "";
        String zachranka = "";
        String nemocnica = "";
        String typDiagnozy = "";
        String diagnoza = "";
        String zhoda = "";

        String query = "select \n" // zaciatok SQL príkazu
                + "zas.id_zasahu as zasah,\n"
                + "zas.info_o_zasahu as infoZasah,\n"
                + "zas.stav as stav,\n"
                + "pa.rod_cislo as rodneCislo,\n"
                + "pa.info_o_pacientovi as infoPac,\n"
                + "ISNULL(dgpa.id_diagnozy,'') as PotencionalnaDiagnoza,\n"
                + "ISNULL(dgzas.id_diagnozy,'') as KonecnaDiagnoza,\n"
                + "obzas.nazov as miestoZasahu,\n"
                + "pos.id_posadky_spz as spz,\n"
                + "pos.typ_posadky as typ,\n"
                + "CONCAT(zach.nazov_zachranky,' ',obzach.nazov) as zachranka,\n"
                + "ISNULL(zz.nazov_zariadenia,'nic')  as nemocnica,";
        if (jCheckBox2.isSelected()) {  //ak chceme mená jednotlivých aktérov obsluhujúcich zásah
            query += "ISNULL(ou1.meno+' '+ou1.priezvisko,'NEZNÁMY') as operator,\n"
                    + "ISNULL(ou2.meno+' '+ou2.priezvisko,'NEZNÁMY')  as vodic,\n"
                    + "ISNULL(ou3.meno+' '+ou3.priezvisko,'NEZNÁMY')  as lekar, \n"
                    + "ISNULL(ou4.meno+' '+ou4.priezvisko,'NEZNÁMY')  as zachranar,\n"
                    + "ISNULL(ou5.meno+' '+ou5.priezvisko,'NEZNÁMY')  as prijem, \n"
                    + "ISNULL(ou6.meno+' '+ou6.priezvisko,'NEZNÁMY')  as uzavrel,";
        }
        if (jCheckBox1.isSelected()) { //ak chceme časy jednotlivých príchodov
            query += "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd.MM.yyyy HH:mm'))) as datum_zavolania,\n"
                    + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_posadky,'dd.MM.yyyy HH:mm'))) as datum_vyjazdu_posadky,\n"
                    + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_k_pacientovi,'dd.MM.yyyy HH:mm'))) as datum_prijazdu_k_pacientovi,\n"
                    + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_od_pacienta,'dd.MM.yyyy HH:mm'))) as datum_vyjazdu_od_pacienta,\n"
                    + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_do_zar,'dd.MM.yyyy HH:mm'))) as datum_prijazdu_do_zar,\n"
                    + "(CONVERT(varchar(19),FORMAT(zas.datum_zaciatku_navratu,'dd.MM.yyyy HH:mm'))) as datum_zaciatku_navratu,\n"
                    + "(CONVERT(varchar(19),FORMAT(zas.datum_ukoncenia_zasahu,'dd.MM.yyyy HH:mm'))) as datum_ukoncenia_zasahu,";
        }
        query = query.substring(0, query.length() - 1); // odstránenie posledného znaku ","
        query += "\n from zasah zas\n" //pridanie potrebných sql joinov
                + "join pacient pa on (zas.id_pacienta=pa.id_pacienta)\n"
                + "full outer join diagnozy dgpa on (pa.id_diagnozy=dgpa.id_diagnozy)\n"
                + "full outer join diagnozy dgzas on (pa.id_diagnozy=dgzas.id_diagnozy)\n"
                + "join adresa adrzas on (zas.id_adresy_zasahu = adrzas.id_adresy)\n"
                + "join obec obzas on (adrzas.id_obce = obzas.id_obce)\n"
                + "join posadka pos on (zas.id_posadky_spz = pos.id_posadky_spz)\n"
                + "join zachranka zach on (pos.id_zachranky = zach.id_zachranky)\n"
                + "join adresa adrzach on (zach.id_adresy = adrzach.id_adresy)\n"
                + "join obec obzach on (adrzach.id_obce = obzach.id_obce)\n"
                + "full outer join zdravotnicke_zariadenie zz on (zas.id_zariadenia = zz.id_zariadenia)\n";
        if (jCheckBox2.isSelected()) {  //potrebné joiny ak chceme mená jednotlivých aktérov obsluhujúcich zásah
            query += "full outer join  zamestnanec_ucet zu1 on (zas.id_operator=zu1.id_zamestnanca)\n"
                    + "full outer join  zamestnanec_ucet zu2 on (zas.id_vodic=zu2.id_zamestnanca)\n"
                    + "full outer join zamestnanec_ucet zu3 on (zas.id_lekar=zu3.id_zamestnanca)\n"
                    + "full outer join  zamestnanec_ucet zu4 on (zas.id_zachranar=zu4.id_zamestnanca)\n"
                    + "full outer join  zamestnanec_ucet zu5 on (zas.id_prijem=zu5.id_zamestnanca)\n"
                    + "full outer join zamestnanec_ucet zu6 on (zas.id_uzavrel=zu6.id_zamestnanca)\n"
                    + "full outer join os_udaje ou1 on (zu1.rod_cislo=ou1.rod_cislo)\n"
                    + "full outer join  os_udaje ou2 on (zu2.rod_cislo=ou2.rod_cislo)\n"
                    + "full outer join os_udaje ou3 on (zu3.rod_cislo=ou3.rod_cislo)\n"
                    + "full outer join os_udaje ou4 on (zu4.rod_cislo=ou4.rod_cislo)\n"
                    + "full outer join os_udaje ou5 on (zu5.rod_cislo=ou5.rod_cislo)\n"
                    + "full outer join os_udaje ou6 on (zu6.rod_cislo=ou6.rod_cislo)\n";
        }
        query += "where zas.id_zasahu is not null "; // odstranienie riadkov kde id_zasahu je NULL kvôli (full outer join) 
        //nastavenie a pridávanie podmienok SQL príkazu
        if (jCBDATUM.isSelected()) {                //medzi dátumami
            try {
                datumOd = dcn.format(jDateChooserOD.getDate());
                datumDo = dcn.format(jDateChooserDO.getDate());
                datum = "\n and CONVERT(date,zas.datum_zavolanie) between "
                        + "CONVERT(date,'" + datumOd + "',104) and CONVERT(date,'" + datumDo + "',104)";
                query += datum;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nevybrali ste dátum");
            }
        } else {                                    //všetky
            Date date = new Date();
            datumOd = "01.01.2000";
            datumDo = dcn.format(date);
            datum = "\n and CONVERT(date,zas.datum_zavolanie) between "
                    + "CONVERT(date,'" + datumOd + "',104) and CONVERT(date,'" + datumDo + "',104)";
            query += datum;
        }
        if (jCBZachranka.isSelected()) {        // vyhľadávanie podľa záchranky
            zachranka = "\n and zach.nazov_zachranky+'-'+obzach.nazov like '"
                    + jCB_FOP_Zachranky.getSelectedItem().toString() + "'";
            query += zachranka;
        }
        if (jCBNemocnice.isSelected()) {       // vyhľadávanie podľa nemocnice
            nemocnica = "\n and zz.nazov_zariadenia like '"
                    + jCB_FOP_Nemocnice1.getSelectedItem().toString() + "'";
            query += nemocnica;
        }
        if (jCBTypdiagnozy.isSelected()) {   // vyhľadávanie podľa typy diagnozy
            typDiagnozy = "\n and dgzas.typ like '"
                    + jCB_NZ_typDiagnozy1.getSelectedItem().toString() + "'";
            query += typDiagnozy;
        }
        if (jCBDiagnozy.isSelected()) {     // vyhľadávanie podľa názvu diagnózy
            try {
                diagnoza = "\n and (dgzas.id_diagnozy+' '+Cast(dgzas.nazov as nvarchar(163))) like '"
                        + jCB_NZ_diagnozy1.getSelectedItem().toString() + "%'";
                query += diagnoza;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nevybrali ste diagnozu");
            }
        }
        if (jCBTPDAno.isSelected() == true && jCBTPDNie.isSelected() == false) { // vyhľadávanie zhoda potenciálne a konečnej diagnózy
            //zhoda = "\n and zas.id_diagnozy like pa.id_diagnozy ";
            zhoda = "\n and zas.id_diagnozy like pa.id_diagnozy or"
                    + " zas.id_diagnozy is null and pa.id_diagnozy is null";
        }
        if (jCBTPDAno.isSelected() == false && jCBTPDNie.isSelected() == true) { // vyhľadávanie nezhoda potenciálne a konečnej diagnózy 
            zhoda = "\n and zas.id_diagnozy not like pa.id_diagnozy";
        }
        if ((jCBTPDAno.isSelected() == true && jCBTPDNie.isSelected() == true) // vyhľadávanie na zhode/nezhode potenciálne a konečnej diagnózy nezáleží  
                || (jCBTPDAno.isSelected() == false && jCBTPDNie.isSelected() == false)) {
            zhoda = "";
        }
        query += zhoda;
        //System.out.println("******************");
        System.out.println(query);
        jTableStatistika.setModel(DajStatistiku(query));
        jFOknoOperator.repaint();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jCBZachrankaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBZachrankaStateChanged

        if (jCBZachranka.isSelected()) {
            jCB_FOP_Zachranky.setVisible(true);
        } else {
            jCB_FOP_Zachranky.setVisible(false);
        }

    }//GEN-LAST:event_jCBZachrankaStateChanged

    private void jCBDATUMStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBDATUMStateChanged
        if (jCBDATUM.isSelected()) {
            jDateChooserOD.setVisible(true);
            jDateChooserDO.setVisible(true);
            jOd.setVisible(true);
            jDo.setVisible(true);
        } else {
            jDateChooserOD.setVisible(false);
            jDateChooserDO.setVisible(false);
            jOd.setVisible(false);
            jDo.setVisible(false);
        }
    }//GEN-LAST:event_jCBDATUMStateChanged

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jCBZachranka1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBZachranka1StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBZachranka1StateChanged

    private void jCBDATUM2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBDATUM2StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBDATUM2StateChanged

    private void jCBTypdiagnozyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBTypdiagnozyStateChanged
        if (jCBTypdiagnozy.isSelected()) {
            jCB_NZ_typDiagnozy1.setVisible(true);
        } else {
            jCB_NZ_typDiagnozy1.setVisible(false);
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jCBTypdiagnozyStateChanged

    private void jCBNemocniceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBNemocniceStateChanged
        if (jCBNemocnice.isSelected()) {
            jCB_FOP_Nemocnice1.setVisible(true);
        } else {
            jCB_FOP_Nemocnice1.setVisible(false);
        }
    }//GEN-LAST:event_jCBNemocniceStateChanged

    private void jCBDiagnozyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBDiagnozyStateChanged
        if (jCBDiagnozy.isSelected()) {
            jCB_NZ_diagnozy1.setVisible(true);
        } else {
            jCB_NZ_diagnozy1.setVisible(false);
        }
    }//GEN-LAST:event_jCBDiagnozyStateChanged

    private void jCBTPDAnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBTPDAnoActionPerformed

        jCBTPDNie.setSelected(false);


    }//GEN-LAST:event_jCBTPDAnoActionPerformed

    private void jPAktualneZasahyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPAktualneZasahyMouseClicked
        // Color jbb = jPAktualneZasahy.getBackground();
        // jButton8.setBackground(jbb);
        //jButton8.setEnabled(false); // TODO add your handling code here:
        // jCBTPDAno.setSelected(false);
    }//GEN-LAST:event_jPAktualneZasahyMouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String posadka = "";
        try {
            int i = JTablePosadky.getSelectedRow();
            posadka = JTablePosadky.getValueAt(i, 1).toString();
            int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zmeniť stav posádke: " + posadka, "Príjazd posádky na stanicu",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
            System.out.println("posadka >" + posadka);
            if (result == JOptionPane.YES_OPTION) {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_ZMENA_STAVU_POSADKY (?,?)}");
                stored_pro.setString(1, posadka);
                stored_pro.registerOutParameter(2, java.sql.Types.INTEGER);
                stored_pro.executeUpdate();
                int infoo = stored_pro.getInt(2);
                stored_pro.close();
                conn.close();
                System.out.println("infooooo >" + info);
                if (infoo == 1) {
                    JOptionPane.showMessageDialog(null, "Úspešne ste nastavili zmenu stavu posádke : " + posadka, "Zmena stavu posádky", JOptionPane.INFORMATION_MESSAGE);
                    Show_Posadky_In_Table(getPosadkyListZaklad());
                    return;
                }
                if (infoo == 4) {
                    JOptionPane.showMessageDialog(null, "Zmena stavu posádky sa neuskutočnila", "Error", JOptionPane.ERROR_MESSAGE);
                    Show_Posadky_In_Table(getPosadkyListZaklad());
                    return;
                }

            } else {
                System.out.println("nie");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste posádku ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void JTablePosadkyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTablePosadkyMouseClicked
        int i = JTablePosadky.getSelectedRow();
        String stav = JTablePosadky.getValueAt(i, 3).toString();
        System.out.print(stav);
        if ("Pripravena".equals(stav)) {
            jButton8.setText("Mimo prevádzky");
            jButton8.setBackground(Color.red);
            jButton8.setEnabled(true);
            return;
        }
        if ("MimoPrevadzky".equals(stav)) {
            jButton8.setText("Pripravená");
            jButton8.setBackground(Color.GREEN);
            jButton8.setEnabled(true);
            return;
        } else {
            Color jbb = jPAktualneZasahy.getBackground();
            jButton8.setBackground(jbb);
            jButton8.setEnabled(false);
        }

    }//GEN-LAST:event_JTablePosadkyMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Show_aktualneZasahy(getAktualneZasahy());
        Show_Posadky_In_Table(getPosadkyListZaklad());
        Color jbb = jPAktualneZasahy.getBackground();
        jButton8.setBackground(jbb);
        jEPHistoriaOperat.setText("");
        jFOknoOperator.repaint();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jB_AZ_PrijazdDoNemocnice16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice16ActionPerformed
        int i = JTableAktualneZasahy.getSelectedRow();
        String zasah = JTableAktualneZasahy.getValueAt(i, 0).toString();
        String query
                = "select\n"
                + "(zach.nazov_zachranky + ' - '+ob1.nazov) as zachranka,\n"
                + "id_zasahu as zasah,\n"
                + "(ou.meno +' '+ou.priezvisko) as pacient,\n"
                + "ou.rod_cislo,\n"
                + "ob.nazov,\n"
                + "(adr.ulica+' '+adr.cislo) as adresa,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd/MM/yyyy HH:mm:ss'))) as datum_zavolania,\n"
                + "ISNULL((dg1.id_diagnozy+' '+Cast(dg1.nazov as nvarchar(50))),'NEZADANA')as diagPacient,\n"
                + "ISNULL(pac.info_o_pacientovi,' ') as infoPacient,\n"
                + "ISNULL(zas.info_o_zasahu,' ') as infoZasah,\n"
                + "ISNULL((dg2.id_diagnozy+' '+Cast(dg2.nazov as nvarchar(50))),'NEZADANA')as diagZasah,\n"
                + "ISNULL(zz.nazov_zariadenia,' ')as nemocnica,\n"
                + "ISNULL(ou2.meno+ou2.priezvisko,ISNULL(ouv.meno+' '+ouv.priezvisko,' '))  as vodic,\n"
                + "ISNULL(ou4.meno+ou4.priezvisko,ISNULL(ouz.meno+' '+ouz.priezvisko,' '))  as zachranar,\n"
                + "ISNULL(ou3.meno+ou3.priezvisko,ISNULL(oul.meno+' '+oul.priezvisko,' '))  as lekar,\n"
                + "ISNULL(ou1.meno+' '+ou1.priezvisko,' ') as operator,\n"
                + "ISNULL(ou5.meno+' '+ou5.priezvisko,' ')  as prijem,\n"
                + "ISNULL(ou6.meno+' '+ou6.priezvisko,' ')  as uzavrel,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd/MM/yyyy HH:mm:ss'))) as datum_zavolania,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_posadky,'dd/MM/yyyy HH:mm:ss'))) as datum_vyjazdu_posadky,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_k_pacientovi,'dd/MM/yyyy HH:mm:ss'))) as datum_prijazdu_k_pacientovi,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_od_pacienta,'dd/MM/yyyy HH:mm:ss'))) as datum_vyjazdu_od_pacienta,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_do_zar,'dd/MM/yyyy HH:mm:ss'))) as datum_prijazdu_do_zar,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_zaciatku_navratu,'dd/MM/yyyy HH:mm:ss'))) as datum_zaciatku_navratu,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_ukoncenia_zasahu,'dd/MM/yyyy HH:mm:ss'))) as datum_ukoncenia_zasahu\n"
                + "from zasah zas\n"
                + "join posadka pos on (zas.id_posadky_spz =pos.id_posadky_spz)\n"
                + "join zachranka zach on (pos.id_zachranky=zach.id_zachranky)\n"
                + "join adresa adr1 on (zach.id_adresy=adr1.id_adresy)\n"
                + "join obec ob1 on (adr1.id_obce =ob1.id_obce)\n"
                + "join pacient pac on(zas.id_pacienta = pac.id_pacienta)\n"
                + "join os_udaje ou on(pac.rod_cislo = ou.rod_cislo)\n"
                + "join adresa adr on (zas.id_adresy_zasahu=adr.id_adresy)\n"
                + "join obec ob on (adr.id_obce = ob.id_obce)\n"
                + "full outer join zamestnanec_ucet zu1 on (zas.id_operator=zu1.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu2 on (zas.id_vodic=zu2.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu3 on (zas.id_lekar=zu3.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu4 on (zas.id_zachranar=zu4.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu5 on (zas.id_prijem=zu5.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu6 on (zas.id_uzavrel=zu6.id_zamestnanca)\n"
                + "full outer join os_udaje ou1 on (zu1.rod_cislo=ou1.rod_cislo)\n"
                + "full outer join os_udaje ou2 on (zu2.rod_cislo=ou2.rod_cislo)\n"
                + "full outer join os_udaje ou3 on (zu3.rod_cislo=ou3.rod_cislo)\n"
                + "full outer join os_udaje ou4 on (zu4.rod_cislo=ou4.rod_cislo)\n"
                + "full outer join os_udaje ou5 on (zu5.rod_cislo=ou5.rod_cislo)\n"
                + "full outer join os_udaje ou6 on (zu6.rod_cislo=ou6.rod_cislo)\n"
                + "full outer  join diagnozy dg1 on (pac.id_diagnozy =dg1.id_diagnozy)\n"
                + "full outer  join diagnozy dg2 on (zas.id_diagnozy =dg2.id_diagnozy)\n"
                + "full outer join zdravotnicke_zariadenie zz on (zas.id_zariadenia = zz.id_zariadenia)\n"
                + "full outer join zamestnanec_ucet zul on(pos.id_zamestnanca_lekar=zul.id_zamestnanca)\n"
                + "full outer join os_udaje oul on(zul.rod_cislo = oul.rod_cislo)\n"
                + "full outer join zamestnanec_ucet pzv on(pzv.id_zamestnanca=pos.id_zamestnanca_vodic)\n"
                + "full outer join zamestnanec_ucet pzz on(pzz.id_zamestnanca=pos.id_zamestnanca_zachranar)\n"
                + "full outer join os_udaje ouv on (ouv.rod_cislo = pzv.rod_cislo)\n"
                + "full outer join os_udaje ouz on (ouz.rod_cislo = pzz.rod_cislo)\n"
                + "where zas.id_zasahu=" + zasah;
        try {
            conn = DBConnection.DBConnection();
            JasperDesign jd = JRXmlLoader.load(System.getProperty("user.home") + "\\desktop\\ZZS\\SpravaOZasahu.jrxml");
            JRDesignQuery nQ = new JRDesignQuery();
            nQ.setText(query);
            jd.setQuery(nQ);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + e.getClass().getCanonicalName());
            return;
        }

    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice16ActionPerformed

    /**
     * Metoda, na potvrdenie prijazdu zachranky spat na stanicu
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stav (zasahu),id_posadky_spz a stav_posadky
     * PROC_UPDATE_ZASAH_DATUM_UKONCENIA (id_zasahu) nastavi :
     * datum_ukoncenia_zasahu na DB sysdate,stav na "ukončený", stav_posadky na
     * "Pripravena"
     *
     *
     * @param evt - kliknutie
     */
    private void jB_FOP_PrijazdNaStanicuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FOP_PrijazdNaStanicuActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (stav.equals("prebiehajúci") && !stav_posadky.equals("NaCesteNaStanicu")) {
            JOptionPane.showMessageDialog(null, "Posádka nie je na ceste na späť na stanicu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať príjazd posádky : " + posadka + " zásahu : " + id_zasahu + "na stanicu", "Príjazd posádky na stanicu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_UKONCENIA (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOknoOperator.repaint();

        } else {
            System.out.println("nie");
        }
    }//GEN-LAST:event_jB_FOP_PrijazdNaStanicuActionPerformed

    /**
     * Metoda, na potvrdenie odjazdu posadky na zasah PROC_SELECT_STAVY_ZASAHU
     * (id_zasahu)- zdielana procedura na zistenie stav (zasahu),id_posadky_spz
     * a stav_posadky PROC_UPDATE_ZASAH_DATUM_VYJAZDU_SANITKY (id_zasahu)
     * nastavi zasah : stav na "prebiehajúci", datum_vyjazdu_posadky na DB
     * systime
     *
     * @param evt - kliknutie
     */
    private void jB_FOP_zapisVyjazdKPacientoviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FOP_zapisVyjazdKPacientoviActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            //System.out.println("stav>" + stav + "idZASAHU>" + id_zasahu + "posadka>" + posadka);
            if (!stav.equals("nový") || !stav_posadky.equals("NaCesteKPacientovi")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " už vyrazila na zásah ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete nastaviť odchod posádky : " + posadka + " zásahu :" + id_zasahu + " k pacientovi ?", "Výjazdu posádky zo stanice",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_VYJAZDU_SANITKY (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ChYba : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOknoOperator.repaint();
        } else {
            System.out.println("nie");
        }
    }
//GEN-LAST:event_jB_FOP_zapisVyjazdKPacientoviActionPerformed
    /* PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stavu zasahu,id_posadky_spz a stav_posadky PROC_UPDATE_ZASAH_NAVRAT
     * (id_zasahu) nastavi : datum_zaciatku_navratu na DB sysdate , stav_posadky
     * na "NaCesteNaStanicu"
     *
     *
     * @param evt - kliknutie
     */
    private void jB_FOP_zapisNavratNaStanicuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FOP_zapisNavratNaStanicuActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((!stav_posadky.equals("OdovzdavaniePacienta") && !stav_posadky.equals("NaCesteKPacientovi")) && !stav_posadky.equals("ZasahujeNaMieste")) {
            JOptionPane.showMessageDialog(null, "Nie je možné nastaviť posádku na cestu na stanicu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete nastaviť návrat  posádky zásahu :" + id_zasahu, "Návrat posádky na stanicu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_NAVRAT (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOknoOperator.repaint();
        } else {
            System.out.println("nie");
        }
    }
//GEN-LAST:event_jB_FOP_zapisNavratNaStanicuActionPerformed
    /**
     * Metoda, na potvrdenie prijazdu zasahu do nemocnice
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stav (zasahu), id_posadky_spz stav_posadky
     * PROC_UPDATE_ZASAH_DATUM_PRIJAZDU_DO_ZAR (id_zasahu) nastavi:
     * datum_prijazdu_do_zar na DB sysdate , stav_posadky na
     * "OdovzdavaniePacienta"
     *
     * @param evt - kliknutie
     */
    private void jB_FOP_PrijazdDoZariadeniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FOP_PrijazdDoZariadeniaActionPerformed
        int id_zasahu = -1;
        String errType = "chyba";
        String stav = "";
        String posadka = "";
        String stavPosadky = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stavPosadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!stavPosadky.equals("NaCesteDoNemocnice")) {
                JOptionPane.showMessageDialog(null, "Posádka zásahu :" + id_zasahu + " nie je na ceste do nemocnice ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať príjazd do nemocnice pre vybraný zásah :" + id_zasahu + "?", "Zapísanie préjazdu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_PRIJAZDU_DO_ZAR (?,?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.setInt(2, 0);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                //e.printStackTrace();
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOknoOperator.repaint();
        } else {
            System.out.println("nic");

        }
    }
//GEN-LAST:event_jB_FOP_PrijazdDoZariadeniaActionPerformed
    /**
     * Metoda, na potvrdenie prevozu pacienta do nemocnice
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie :
     * stav (zasahu),id_posadky_spz a stav_posadky
     * PROC_UPDATE_ZASAH_ID_ZARIADENIA_BY_NAME (id_zasahu,nazov_zariadenia)
     * nastaví zásahu : id_zariadenia,datum_vyjazdu_od_pacienta na DBsysdate,
     * stav_posadky na "NaCesteDoNemocnice" nazov_zariadenia je ziskane z
     * jCB_FOP_Nemocnice
     *
     * @param evt - kliknutie
     */
    private void jB_FOP_ZapisPrevozDoNemocniceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FOP_ZapisPrevozDoNemocniceActionPerformed
        int id_zasahu = -1;
        String errType = "chyba";
        String posadka = "";
        String stavPosadky = "";
        String stav = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stavPosadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!stavPosadky.equals("ZasahujeNaMieste")) {
                JOptionPane.showMessageDialog(null, "Posádka zásahu :" + id_zasahu + " nezasahuje u pacienta", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
            return;
        }
        String nazovZariadenia = jCB_FOP_Nemocnice.getSelectedItem().toString();
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete zapísať prevoz pre zásah :" + id_zasahu + "\n do zariadenia :" + nazovZariadenia + "?", "Vytvorenie prevozu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                //if (id_zasahu == -1) {
                //    errType = "zásah";
                //    throw new Exception();
                //}
                if (jCB_FOP_Nemocnice.getSelectedItem().toString().isEmpty()) {
                    errType = "nemocnicu";
                    throw new Exception();
                }
                System.out.println("" + nazovZariadenia + "," + id_zasahu);
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_ID_ZARIADENIA_BY_NAME(?,?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.setString(2, nazovZariadenia);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nezadali ste :" + errType, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOknoOperator.repaint();
        } else {
            System.out.println("nic");
        }
    }
//GEN-LAST:event_jB_FOP_ZapisPrevozDoNemocniceActionPerformed
    /**
     * Metoda, na potvrdenie prijazdu posadky na miesto zasahu
     * PROC_SELECT_STAVY_ZASAHU (id_zasahu)- zdielana procedura na zistenie stav
     * (zasahu),id_posadky_spz a stav_posadky PROC_UPDATE_ZASAH_DATUM_PRIJAZDU
     * (id_zasahu) nastavi datum_prijazdu_k_pacientovi na DB systime,
     * stav_posadky na 'ZasahujeNaMieste'
     *
     * @param evt - kliknutie
     */
    private void jB_FOP_PrijazdPosadkyKPacientoviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FOP_PrijazdPosadkyKPacientoviActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            System.out.println(stav + posadka + stav_posadky + "");
            stored_pro.close();
            rs.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " ešte nevyrazila k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("ukončený")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol ukončený ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
            return;
        }
        if (!stav_posadky.equals("NaCesteKPacientovi")) {
            JOptionPane.showMessageDialog(null, "Posádka : " + posadka + " zásahu : " + id_zasahu + " nie je na ceste k pacientovi ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete nastaviť príchod k pacientovi zásahu :" + id_zasahu, "Prichod k pacientovi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_DATUM_PRIJAZDU (?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nezadali ste : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOknoOperator.repaint();
        } else {
            System.out.println("nie");
        }
    }
//GEN-LAST:event_jB_FOP_PrijazdPosadkyKPacientoviActionPerformed
    /**
     * Metoda, na uzavretie zasahu PROC_SELECT_STAVY_ZASAHU (id_zasahu)-
     * zdielana procedura na zistenie : stav (zasahu),id_posadky_spz a
     * stav_posadky PROC_UPDATE_ZASAH_NAVRAT (id_zasahu) nastavi :
     * datum_ukoncenia_zasahu na DB sysdate,stav na "ukončený", stav_posadky na
     * "Pripravena"
     *
     * @param evt - kliknutie
     */
    private void jB_FOP_UzavriZasahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_FOP_UzavriZasahActionPerformed
        int id_zasahu = -1;
        String stav = "";
        String posadka = "";
        String stav_posadky;
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{ call PROC_SELECT_STAVY_ZASAHU (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {
                stav = rs.getString(1);
                posadka = rs.getString(2);
                stav_posadky = rs.getString(3);
            }
            rs.close();
            stored_pro.close();
            conn.close();
            if (stav.equals("nový")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " nie je možné uzavrieť lebo je : " + stav, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("prebiehajúci")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " nie je možné uzavrieť lebo je : " + stav, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (stav.equals("uzavretý")) {
                JOptionPane.showMessageDialog(null, "Zásah : " + id_zasahu + " už bol uzavretý", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showOptionDialog(this, "Naozaj chcete uzavrieť zásah :" + id_zasahu, "Uzvretie zásahu",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZASAH_UZAVRETIE (?,?)}");
                stored_pro.setInt(1, id_zasahu);
                stored_pro.setInt(2, zamestnanec);
                stored_pro.execute();
                stored_pro.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return;
            }
            Show_aktualneZasahy(getAktualneZasahy());
            jFOknoOperator.repaint();
        } else {
            System.out.println("nie");
        }
    }
//GEN-LAST:event_jB_FOP_UzavriZasahActionPerformed
    /**
     * Metóda slúžiaca na otvorenie okna opravzásah od operátora ZZS
     * PROC_SELECT_OPRAV_ZASAH (id_zasahu) poskytuje
     * tel_cislo,meno,priezvisko,rod_Cislo, ulica, cislo, nazov obce info o
     * zasahu a pacientovi, potencialnu a konecnu diagnozu,nazov_zariadenia
     *
     * @param evt
     */
    private void jB_AZ_PrijazdDoNemocnice6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_PrijazdDoNemocnice6ActionPerformed
        int id_zasahu;
        String s = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();  //zistenie id_zasahu
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_OPRAV_ZASAH (?)}");//zavolanie procedúry
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) {                                                 //naplnenie datami
                jTF_OZ_tel.setText(rs.getString(1));
                System.out.println(rs.getString(1));
                jTF_OZ_meno.setText(rs.getString(2));
                jTF_OZ_priezvisko.setText(rs.getString(3));
                jTF_OZ_rodCislo.setText(rs.getString(4));
                jTF_OZ_ulica.setText(rs.getString(5));
                jTF_OZ_PopisCislo.setText(rs.getString(6));
                jCB_OZ_obce.setSelectedItem(rs.getString(7));
                jCB_OZ_Pp_Diag.setSelectedItem(rs.getString(8));
                JTA_OZ_infopacient.setText(rs.getString(9));
                JTA_OZ_infozasah.setText(rs.getString(10));
                jCB_OZ_Konecna_Diag.setSelectedItem(rs.getString(11));
                if (rs.getString(12) == null) {
                    jCBNemocnice1.addItem("nezadana");
                    jCBNemocnice1.setSelectedItem("nezadana");
                } else {
                    jCBNemocnice1.setSelectedItem(rs.getString(12));
                }
                jTF_OZ_IdZasahu.setText(rs.getString(13));
            }
            rs.close();
            stored_pro.close();
            conn.close();
            jFOpravZasah.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        jFOpravZasah.setVisible(true);
    }//GEN-LAST:event_jB_AZ_PrijazdDoNemocnice6ActionPerformed
    /**
     * Metóda slúžiaca na zobrazenie histórie o pacientovi pre operátora ZZS
     * Zdieľaná procedúra PROC_SELECT_INFA_NEMOCNICA_HISTORIA (id_zasahu) vracia
     * , datum_zavolanie, info o zasahu a pacientovi, potencialnu a konecnu
     * diagnozu, meno, priezvisko, rodné cislo
     *
     * @param evt-kliknutie
     */
    private void jB_AZ_ZobrazHistoriuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_AZ_ZobrazHistoriuActionPerformed
        jEPHistoriaOperat.setText("");
        int id_zasahu;
        String s = "";
        try {
            int i = JTableAktualneZasahy.getSelectedRow();
            id_zasahu = Integer.parseInt(JTableAktualneZasahy.getValueAt(i, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_INFA_NEMOCNICA_HISTORIA (?)}");
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery(); //zavolanie procedúry
            while (rs.next()) {             //naplnenie ziskanými dátami
                s += "<html>"
                        + "<body>"
                        + "<p><b>[Id zásahu :" + (rs.getString(1)) + "]<br>[Dátum tiesňového volania: " + (rs.getString(2)) + "]<br>"
                        + "<b>[" + (rs.getString(9)) + ", " + (rs.getString(7)) + ", " + (rs.getString(8)) + "]</b>"
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodná diagnóza :</b><br>"
                        + "" + (rs.getString(5)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodné info o pacientovi :</b><br>"
                        + "" + (rs.getString(6)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Konečná diagnóza diagnóza :</b><br>"
                        + "" + (rs.getString(4)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Info o zasahu :</b><br>"
                        + "" + (rs.getString(3)) + ""
                        + "</p>"
                        + "<hr>";
                //+ "</body>"
                //+ "</html>";
                //if(rs.next()){
                while (rs.next()) {
                    s += "<p><b>[Id zásahu :" + (rs.getString(1)) + "]<br>[Dátum tiesňového volania: " + (rs.getString(2)) + "]<br>"
                            + "<b>[" + (rs.getString(9)) + ", " + (rs.getString(7)) + ", " + (rs.getString(8)) + "]</b>"
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Pôvodná diagnóza :</b><br>"
                            + "" + (rs.getString(5)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Pôvodné info o pacientovi :</b><br>"
                            + "" + (rs.getString(6)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Konečná diagnóza diagnóza :</b><br>"
                            + "" + (rs.getString(4)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Info o zasahu :</b><br>"
                            + "" + (rs.getString(3)) + ""
                            + "</p>"
                            + "<hr>";
                }
                //}
                s += "</body>"
                        + "</html>";

            }
            rs.close();
            stored_pro.close();
            conn.close();
            jEPHistoriaOperat.setText(s);
            System.out.println(s);
            JTableAktualneZasahy.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }//GEN-LAST:event_jB_AZ_ZobrazHistoriuActionPerformed
    /**
     * Metóda slúžiaca na zobrazenie histórie o pacientovi pre veliteľa zásahu
     * Zdieľaná procedúra PROC_SELECT_INFA_NEMOCNICA_HISTORIA (id_zasahu) vracia
     * , datum_zavolanie, info o zasahu a pacientovi, potencialnu a konecnu
     * diagnozu, meno, priezvisko, rodné cislo
     *
     * @param evt-kliknutie
     */
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        jEPHistoriaVelitel.setText("");
        int id_zasahu;
        String s = "";
        try {
            int i = JTAktualneZasahyZachranky.getSelectedRow();         // získanie id_zásahu
            id_zasahu = Integer.parseInt(JTAktualneZasahyZachranky.getValueAt(i, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_SELECT_INFA_NEMOCNICA_HISTORIA (?)}");    //zavolanie procedúry
            stored_pro.setInt(1, id_zasahu);
            rs = stored_pro.executeQuery();
            while (rs.next()) { // naplnenie reťazca príslušnými dátami
                s += "<html>"
                        + "<body>"
                        + "<p><b>[Id zásahu: " + (rs.getString(1)) + "]<br>[Dátum tiesňového volania: " + (rs.getString(2)) + "]<br>"
                        + "<b>[" + (rs.getString(9)) + ", " + (rs.getString(7)) + ", " + (rs.getString(8)) + "]</b>"
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodná diagnóza :</b><br>"
                        + "" + (rs.getString(5)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Pôvodné info o pacientovi :</b><br>"
                        + "" + (rs.getString(6)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Konečná diagnóza diagnóza :</b><br>"
                        + "" + (rs.getString(4)) + ""
                        + "</p>"
                        + "<hr>"
                        + "<p>"
                        + "<b>Info o zasahu :</b><br>"
                        + "" + (rs.getString(3)) + ""
                        + "</p>"
                        + "<hr>";
                //+ "</body>"
                //+ "</html>";
                while (rs.next()) {
                    s += "<p><b>[Id zásahu :" + (rs.getString(1)) + "]<br>[Dátum tiesňového volania: " + (rs.getString(2)) + "]<br>"
                            + "<b>[" + (rs.getString(9)) + ", " + (rs.getString(7)) + ", " + (rs.getString(8)) + "]</b>"
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Pôvodná diagnóza :</b><br>"
                            + "" + (rs.getString(5)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Pôvodné info o pacientovi :</b><br>"
                            + "" + (rs.getString(6)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Konečná diagnóza diagnóza :</b><br>"
                            + "" + (rs.getString(4)) + ""
                            + "</p>"
                            + "<hr>"
                            + "<p>"
                            + "<b>Info o zasahu :</b><br>"
                            + "" + (rs.getString(3)) + ""
                            + "</p>"
                            + "<hr>";
                }
                //}
                s += "</body>"
                        + "</html>";

            }
            rs.close();
            stored_pro.close();
            conn.close();
            jEPHistoriaVelitel.setText(s);  //zapisanie reťaca do jEditorPan-a
            System.out.println(s);
            jFOknoOperator.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    /**
     * Metóda slúžiaca na vytvorenie export XLS súboru z jTableStatistika *
     * Výsledny súbor sa ukladá na plochu do priečinka ZZS s nazvom -
     * EXPORTyyyyMMdd.xls OknoOperator/založka Administrácia
     *
     * @param evt kliknutie
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            TableModel model = jTableStatistika.getModel();
            TableColumnModel tmodel = jTableStatistika.getColumnModel();
            if (jTableStatistika.getColumnCount() <= 0 || jTableStatistika.getRowCount() <= 0) {   //ošetrenie, či sa niečo nachádza v tabuľke
                JOptionPane.showMessageDialog(null, "Tabuľka so štatistikou  neobsahuje dáta");
                return;
            }
            HSSFWorkbook fWorkbook = new HSSFWorkbook();            //pripravenie štruktury
            HSSFSheet fSheet = fWorkbook.createSheet("new Sheet");
            HSSFFont sheetTitleFont = fWorkbook.createFont();
            String export = "\\desktop\\ZZS\\EXPORT";                   //pripravenie názvu súboru  a cesty    
            DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMdd");
            export += timeStampPattern.format(java.time.LocalDateTime.now());
            export += ".xls";
            String exportVys = System.getProperty("user.home") + export;
            System.out.println(exportVys);
            File file = new File(exportVys);
            if (file.exists() && !file.isDirectory()) { //Ošetrenie prepisnia exportov
                int result = JOptionPane.showOptionDialog(this, "Taký súbor už existuje: " + export + "\n,naozaj ho chcete prepísať?", "Prepísanie súboru",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new String[]{"Ano", "Nie"}, JOptionPane.NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            HSSFCellStyle cellStyle = fWorkbook.createCellStyle();      //spracovanie hlavičiek
            sheetTitleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFRow hfRow = fSheet.createRow((short) 0);
            for (int j = 0; j < model.getColumnCount(); j++) {
                HSSFCell cell = hfRow.createCell((short) j);
                cell.setCellValue(tmodel.getColumn(j).getHeaderValue().toString());
                cell.setCellStyle(cellStyle);
            }
            for (int i = 0; i < model.getRowCount(); i++) {             //spracovanie dát
                HSSFRow fRow = fSheet.createRow((short) i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object o = model.getValueAt(i, j);
                    String s = (o == null ? "" : o.toString());
                    System.out.println(s);
                    HSSFCell cell = fRow.createCell((short) j);
                    cell.setCellValue(s);
                    cell.setCellStyle(cellStyle);
                }
            }
            FileOutputStream fileOutputStream;                          //zapisanie
            fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            fWorkbook.write(bos);
            bos.close();
            fileOutputStream.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    /**
     * Metóda slúžia na zozbrazenie Správy o zásahu OknoOperator/založka
     * Administrácia
     *
     * @param evt -kliknutie
     */
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        int id_zasahu = -1;
        try {                                                             //získanie id_Zásahu      
            int i = jTableStatistika.getSelectedRow();
            id_zasahu = Integer.parseInt(jTableStatistika.getValueAt(i, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste zásah ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String query //sql prikaz na vygenerovanie spravy
                = "select\n"
                + "(zach.nazov_zachranky + ' - '+ob1.nazov) as zachranka,\n"
                + "id_zasahu as zasah,\n"
                + "(ou.meno +' '+ou.priezvisko) as pacient,\n"
                + "ou.rod_cislo,\n"
                + "ob.nazov,\n"
                + "(adr.ulica+' '+adr.cislo) as adresa,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd/MM/yyyy HH:mm:ss'))) as datum_zavolania,\n"
                + "ISNULL((dg1.id_diagnozy+' '+Cast(dg1.nazov as nvarchar(50))),'NEZADANA')as diagPacient,\n"
                + "ISNULL(pac.info_o_pacientovi,' ') as infoPacient,\n"
                + "ISNULL(zas.info_o_zasahu,' ') as infoZasah,\n"
                + "ISNULL((dg2.id_diagnozy+' '+Cast(dg2.nazov as nvarchar(50))),'NEZADANA')as diagZasah,\n"
                + "ISNULL(zz.nazov_zariadenia,' ')as nemocnica,\n"
                + "ISNULL(ou2.meno+ou2.priezvisko,ISNULL(ouv.meno+' '+ouv.priezvisko,' '))  as vodic,\n"
                + "ISNULL(ou4.meno+ou4.priezvisko,ISNULL(ouz.meno+' '+ouz.priezvisko,' '))  as zachranar,\n"
                + "ISNULL(ou3.meno+ou3.priezvisko,ISNULL(oul.meno+' '+oul.priezvisko,' '))  as lekar,\n"
                + "ISNULL(ou1.meno+' '+ou1.priezvisko,' ') as operator,\n"
                + "ISNULL(ou5.meno+' '+ou5.priezvisko,' ')  as prijem,\n"
                + "ISNULL(ou6.meno+' '+ou6.priezvisko,' ')  as uzavrel,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_zavolanie,'dd/MM/yyyy HH:mm:ss'))) as datum_zavolania,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_posadky,'dd/MM/yyyy HH:mm:ss'))) as datum_vyjazdu_posadky,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_k_pacientovi,'dd/MM/yyyy HH:mm:ss'))) as datum_prijazdu_k_pacientovi,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_vyjazdu_od_pacienta,'dd/MM/yyyy HH:mm:ss'))) as datum_vyjazdu_od_pacienta,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_prijazdu_do_zar,'dd/MM/yyyy HH:mm:ss'))) as datum_prijazdu_do_zar,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_zaciatku_navratu,'dd/MM/yyyy HH:mm:ss'))) as datum_zaciatku_navratu,\n"
                + "(CONVERT(varchar(19),FORMAT(zas.datum_ukoncenia_zasahu,'dd/MM/yyyy HH:mm:ss'))) as datum_ukoncenia_zasahu\n"
                + "from zasah zas\n"
                + "join posadka pos on (zas.id_posadky_spz =pos.id_posadky_spz)\n"
                + "join zachranka zach on (pos.id_zachranky=zach.id_zachranky)\n"
                + "join adresa adr1 on (zach.id_adresy=adr1.id_adresy)\n"
                + "join obec ob1 on (adr1.id_obce =ob1.id_obce)\n"
                + "join pacient pac on(zas.id_pacienta = pac.id_pacienta)\n"
                + "join os_udaje ou on(pac.rod_cislo = ou.rod_cislo)\n"
                + "join adresa adr on (zas.id_adresy_zasahu=adr.id_adresy)\n"
                + "join obec ob on (adr.id_obce = ob.id_obce)\n"
                + "full outer join zamestnanec_ucet zu1 on (zas.id_operator=zu1.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu2 on (zas.id_vodic=zu2.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu3 on (zas.id_lekar=zu3.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu4 on (zas.id_zachranar=zu4.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu5 on (zas.id_prijem=zu5.id_zamestnanca)\n"
                + "full outer join zamestnanec_ucet zu6 on (zas.id_uzavrel=zu6.id_zamestnanca)\n"
                + "full outer join os_udaje ou1 on (zu1.rod_cislo=ou1.rod_cislo)\n"
                + "full outer join os_udaje ou2 on (zu2.rod_cislo=ou2.rod_cislo)\n"
                + "full outer join os_udaje ou3 on (zu3.rod_cislo=ou3.rod_cislo)\n"
                + "full outer join os_udaje ou4 on (zu4.rod_cislo=ou4.rod_cislo)\n"
                + "full outer join os_udaje ou5 on (zu5.rod_cislo=ou5.rod_cislo)\n"
                + "full outer join os_udaje ou6 on (zu6.rod_cislo=ou6.rod_cislo)\n"
                + "full outer  join diagnozy dg1 on (pac.id_diagnozy =dg1.id_diagnozy)\n"
                + "full outer  join diagnozy dg2 on (zas.id_diagnozy =dg2.id_diagnozy)\n"
                + "full outer join zdravotnicke_zariadenie zz on (zas.id_zariadenia = zz.id_zariadenia)\n"
                + "full outer join zamestnanec_ucet zul on(pos.id_zamestnanca_lekar=zul.id_zamestnanca)\n"
                + "full outer join os_udaje oul on(zul.rod_cislo = oul.rod_cislo)\n"
                + "full outer join zamestnanec_ucet pzv on(pzv.id_zamestnanca=pos.id_zamestnanca_vodic)\n"
                + "full outer join zamestnanec_ucet pzz on(pzz.id_zamestnanca=pos.id_zamestnanca_zachranar)\n"
                + "full outer join os_udaje ouv on (ouv.rod_cislo = pzv.rod_cislo)\n"
                + "full outer join os_udaje ouz on (ouz.rod_cislo = pzz.rod_cislo)\n"
                + "where zas.id_zasahu=" + id_zasahu;
        try {
            conn = DBConnection.DBConnection(); //vytvorenie pripojenia
            JasperDesign jd = JRXmlLoader.load(System.getProperty("user.home") + "\\desktop\\ZZS\\SpravaOZasahu.jrxml"); //načítanie sablony
            JRDesignQuery nQ = new JRDesignQuery();     //nastavenie query
            nQ.setText(query);
            jd.setQuery(nQ);
            JasperReport jr = JasperCompileManager.compileReport(jd); // prekompilovanie sablony
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn); // vyplenie 
            JasperViewer.viewReport(jp, false);//zobrazenie JasperWiewera
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }

    }//GEN-LAST:event_jButton10ActionPerformed
    /**
     * Nastaví jCBTPDAno na false
     *
     * @param evt
     */
    private void jCBTPDNieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBTPDNieActionPerformed
        jCBTPDAno.setSelected(false);
    }//GEN-LAST:event_jCBTPDNieActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jCBTypdiagnozyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBTypdiagnozyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBTypdiagnozyActionPerformed

    private void jCheckBox3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox3StateChanged
        if (jCheckBox3.isSelected()) {
            jTextField1.setVisible(true);
            jTextField2.setVisible(true);
            jTextField3.setVisible(true);
            jTextField4.setVisible(true);
            jTextField5.setVisible(true);
            jTextField6.setVisible(true);
            jTextField7.setVisible(true);
            jTextField8.setVisible(true);
            jTextField9.setVisible(true);
            jBVytvorZachranku.setVisible(true);

        } else {
            jTextField1.setVisible(false);
            jTextField2.setVisible(false);
            jTextField3.setVisible(false);
            jTextField4.setVisible(false);
            jTextField5.setVisible(false);
            jTextField6.setVisible(false);
            jTextField7.setVisible(false);
            jTextField8.setVisible(false);
            jTextField9.setVisible(false);
            jBVytvorZachranku.setVisible(false);
            jTextField1.setText("Názov");
                jTextField2.setText("Obec");
                jTextField3.setText("Ulica");
                jTextField4.setText("Popisné číslo");
                jTextField5.setText("Meno Šéfa");
                jTextField6.setText("Priezvisko");
                jTextField7.setText("Rodné číslo formát:\"XXXXXX/XXXX\"");
                jTextField8.setText("telefón1");
                jTextField9.setText("telefón2");
        }
    }//GEN-LAST:event_jCheckBox3StateChanged

    private void jBVytvorZachrankuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVytvorZachrankuActionPerformed
        String errType = "";
        //int infoo = 3;
        try {
            if (jTextField1.getText().isEmpty() || jTextField1.getText().equals("Názov")) {
                errType += " Názov záchranky \n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField2.getText().isEmpty() || jTextField2.getText().equals("Obec")) {
                errType += " obec \n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField3.getText().isEmpty() || jTextField3.getText().equals("Ulica")) {
                errType += " ulicu \n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField4.getText().isEmpty() || jTextField4.getText().equals("Popisné číslo")) {
                errType += " popisné číslo\n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField5.getText().isEmpty() || jTextField5.getText().equals("Meno Šéfa")) {
                errType += " meno šéfa \n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField6.getText().isEmpty() || jTextField6.getText().equals("Priezvisko")) {
                errType += " priezvisko \n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField7.getText().isEmpty() || jTextField7.getText().equals("Rodné číslo formát:\"XXXXXX/XXXX\"")) {
                errType += " rodné cislo \n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField8.getText().isEmpty() || jTextField8.getText().equals("telefón1")) {
                errType += " telefónne číslo1 \n";
                throw new IllegalArgumentException("INVALID");
            }
            if (jTextField9.getText().isEmpty() || jTextField9.getText().equals("telefón2")) {
                errType += " telefónne číslo2 \n";
                throw new IllegalArgumentException("INVALID");
            }
            String zachranka = jTextField1.getText();
            String obec = jTextField2.getText();
            String ulica = jTextField3.getText();
            String cislo = jTextField4.getText();
            String meno = jTextField5.getText();
            String priezvisko = jTextField6.getText();
            String rod_cislo = jTextField7.getText();
            String tel1 = jTextField8.getText();
            String tel2 = jTextField9.getText();

            String adr = obec + "%7c" + ulica + "%7c" + cislo;
            System.out.println(adr);
            adr = adr.replace(" ", "+");
            Adresa adr1 = new Adresa();
            try {
                adr1.setLocationsByAdress(adr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
            }

            String lng = adr1.getLng();
            String lat = adr1.getLat();
            String GPS = lng + " " + lat;
            System.out.println(GPS);
            conn = DBConnection.DBConnection();
            stored_pro = conn.prepareCall("{call PROC_CREATE_ZACHRANKU (?,?,?,?,?,?,?,?,?,?,?)}");
            stored_pro.setString(1, zachranka);
            stored_pro.setString(2, obec);
            stored_pro.setString(3, ulica);
            stored_pro.setString(4, cislo);
            stored_pro.setString(5, meno);
            stored_pro.setString(6, priezvisko);
            stored_pro.setString(7, rod_cislo);
            stored_pro.setString(8, tel1);
            stored_pro.setString(9, tel2);
            stored_pro.setString(10, GPS);
            stored_pro.registerOutParameter(11, java.sql.Types.INTEGER);
            stored_pro.executeUpdate();
            int infoo = stored_pro.getInt(11);
            stored_pro.close();
            conn.close();
            if (infoo == 1) {
                JOptionPane.showMessageDialog(null, "Záchranka s takým menom existuje", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (infoo == 2) {
                JOptionPane.showMessageDialog(null, "Taká obec neexistuje", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (infoo == 3) {
                String login = rod_cislo.substring(0, 2);
                login = meno + login;
                String heslo = rod_cislo.substring(7, 11);
                JOptionPane.showMessageDialog(null, "Úspešne ste založili záchranku: " + zachranka
                        + "\n Prihlasovacie údaje +=> login:" + login
                        + "\n heslo: " + heslo, "Uspešne založená záchranka", JOptionPane.INFORMATION_MESSAGE);
                jTextField1.setText("Názov");
                jTextField2.setText("Obec");
                jTextField3.setText("Ulica");
                jTextField4.setText("Popisné číslo");
                jTextField5.setText("Meno Šéfa");
                jTextField6.setText("Priezvisko");
                jTextField7.setText("Rodné číslo formát:\"XXXXXX/XXXX\"");
                jTextField8.setText("telefón1");
                jTextField9.setText("telefón2");
                jTextField1.setVisible(false);
                jTextField2.setVisible(false);
                jTextField3.setVisible(false);
                jTextField4.setVisible(false);
                jTextField5.setVisible(false);
                jTextField6.setVisible(false);
                jTextField7.setVisible(false);
                jTextField8.setVisible(false);
                jTextField9.setVisible(false);
                jBVytvorZachranku.setVisible(false);
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Nezadali ste všetky potrebné údaje, chýba:" + errType,
                    "Chýbajú potrebné údaje", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (StringIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Rodné číslo nie je v požadovanom tvare ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
            return;
        }
    }//GEN-LAST:event_jBVytvorZachrankuActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        ShowZlozeniaPosadkyZachranky(getPosadkyZachranky(zamestnavatel));
        jTextField10.setVisible(false);
        jTextField11.setVisible(false);
        jTextField12.setVisible(false);
        jTextField12.setEditable(true);
        jTextField13.setVisible(false);
        jTextField14.setVisible(false);
        jTextField15.setVisible(false);
        jTextField16.setVisible(false);
        jB_VZ_uprav_zamestnanca.setText("Uprav Zamestnanca");
        jB_AZ_PrijazdDoNemocnice10.setText("Vytvor Zamestnanca");

        jTextField10.setText("Meno");
        jTextField11.setText("Priezvisko");
        jTextField12.setText("Rodné číslo formát XXXXXX/XXXX");
        jTextField13.setText("Číslo OP");
        jTextField14.setText("Obec");
        jTextField15.setText("Ulica");
        jTextField15.setText("Popisné číslo");
        JFZachrankaVelitel.repaint();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTablePosadkyZachrankyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePosadkyZachrankyMouseClicked
        jCB_zamestnanci.removeAllItems();
        int j = jTablePosadkyZachranky.getSelectedColumn();
        if (j == 1) {
            jCKB_stav_volno.setSelected(false);
            jCKB_stav_vsetkych.setSelected(false);
            jCKB_stav_v_sluzbe.setSelected(false);
            jCKB_pozicia_velitel.setSelected(false);
            jCKB_pozicia_vodic.setSelected(false);
            jCKB_pozicia_lekar.setSelected(false);
            jCKB_pozicia_zachranar.setSelected(false);
            try {
                conn = DBConnection.DBConnection();
                st = conn.createStatement();
                rs = st.executeQuery(
                        "select typ_posadky from posadka \n"
                        + "group by(typ_posadky)"
                );
                while (rs.next()) {
                    jCB_zamestnanci.addItem(rs.getString(1));
                }
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                return;
            }
        }
        if (j == 5) {

            jCB_zamestnanci.addItem("MimoPrevadzky");
            jCB_zamestnanci.addItem("Pripravena");
            //jCB_zamestnanci.addItem("Vyradená");
            jCKB_stav_volno.setSelected(false);
            jCKB_stav_vsetkych.setSelected(false);
            jCKB_stav_v_sluzbe.setSelected(false);
            jCKB_pozicia_velitel.setSelected(false);
            jCKB_pozicia_vodic.setSelected(false);
            jCKB_pozicia_lekar.setSelected(false);
            jCKB_pozicia_zachranar.setSelected(false);
        }
        if (j == 2) {
            jCKB_stav_volno.setSelected(true);
            jCKB_stav_vsetkych.setSelected(false);
            jCKB_stav_v_sluzbe.setSelected(false);
            jCKB_pozicia_vodic.setSelected(true);
            jCKB_pozicia_lekar.setSelected(false);
            jCKB_pozicia_zachranar.setSelected(false);
            jCKB_pozicia_velitel.setSelected(false);
        }
        if (j == 3) {
            jCKB_stav_volno.setSelected(true);
            jCKB_stav_vsetkych.setSelected(false);
            jCKB_stav_v_sluzbe.setSelected(false);
            jCKB_pozicia_velitel.setSelected(false);
            jCKB_pozicia_vodic.setSelected(false);
            jCKB_pozicia_lekar.setSelected(false);
            jCKB_pozicia_zachranar.setSelected(true);
        }
        if (j == 4) {
            jCKB_stav_volno.setSelected(true);
            jCKB_stav_vsetkych.setSelected(false);
            jCKB_stav_v_sluzbe.setSelected(false);
            jCKB_pozicia_velitel.setSelected(false);
            jCKB_pozicia_vodic.setSelected(false);
            jCKB_pozicia_lekar.setSelected(true);
            jCKB_pozicia_zachranar.setSelected(false);
        }

        String stav = "";
        String pozicia = "(";
        if (jCKB_stav_v_sluzbe.isSelected()) {
            stav = "('pracuje')";
        }
        if (jCKB_stav_volno.isSelected()) {
            stav = "('nepracuje')";
        }
        if (jCKB_stav_vsetkych.isSelected()) {
            stav = "('pracuje','nepracuje')";
        }
        if (jCKB_stav_vsetkych.isSelected() == false
                && jCKB_stav_volno.isSelected() == false
                && jCKB_stav_v_sluzbe.isSelected() == false
                && j != 5
                && j != 0) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste stav zamestnanca", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (jCKB_pozicia_lekar.isSelected() == false
                && jCKB_pozicia_vodic.isSelected() == false
                && jCKB_pozicia_zachranar.isSelected() == false
                && jCKB_pozicia_velitel.isSelected() == false
                && j != 5
                && j != 0) {
            JOptionPane.showMessageDialog(null, "Nevybrali ste pracovnú pozíciu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (jCKB_pozicia_lekar.isSelected()) {
            pozicia += "'lekar zachranky',";
        }
        if (jCKB_pozicia_vodic.isSelected()) {
            pozicia += "'vodic',";
        }
        if (jCKB_pozicia_zachranar.isSelected()) {
            pozicia += "'zachranar',";
        }
        if (jCKB_pozicia_velitel.isSelected()) {
            pozicia += "'velitel zachranky',";
        }
        System.out.println(pozicia);
        pozicia = pozicia.substring(0, pozicia.length() - 1) + ")";
        System.out.println(pozicia);
        if ((2 <= j) && (j <= 4)) {
            try {
                conn = DBConnection.DBConnection();
                st = conn.createStatement();
                rs = st.executeQuery(
                        "	select ou.meno +' '+ ou.priezvisko from zachranka zach\n"
                        + "	join pracujuci_zamestnanci pz on (zach.id_zachranky=pz.id_zachranky)\n"
                        + "	join zamestnanec_ucet zu on (pz.id_zamestnanca= zu.id_zamestnanca)\n"
                        + "	join os_udaje ou on (zu.rod_cislo = ou.rod_cislo)\n"
                        + " where pz.stav_zamestnanca in " + stav
                        + "	and zu.prac_pozicia in"
                        + pozicia
                        + "and zach.id_zachranky = " + zamestnavatel
                );
                while (rs.next()) {
                    jCB_zamestnanci.addItem(rs.getString(1));
                }
                jCB_zamestnanci.addItem("Nikto");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                return;
            }
        }


    }//GEN-LAST:event_jTablePosadkyZachrankyMouseClicked

    private void jB_VZ_uprav_zamestnancaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jB_VZ_uprav_zamestnancaActionPerformed
        String celemeno = "";
        if (jB_VZ_uprav_zamestnanca.getText().equals("Uprav Zamestnanca")) {
            try {
                celemeno = jCB_zamestnanci.getSelectedItem().toString();
                if (jCB_zamestnanci.getSelectedItem().toString().equals("RLP")) {
                    JOptionPane.showMessageDialog(null, "Nevybrali ste zamestnanca", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (jCB_zamestnanci.getSelectedItem().toString().equals("RZP")) {
                    JOptionPane.showMessageDialog(null, "Nevybrali ste zamestnanca", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (jCB_zamestnanci.getSelectedItem().toString().equals("Pripravena")) {
                    JOptionPane.showMessageDialog(null, "Nevybrali ste zamestnanca", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (jCB_zamestnanci.getSelectedItem().toString().equals("MimoPrevadzky")) {
                    JOptionPane.showMessageDialog(null, "Nevybrali ste zamestnanca", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nevybrali ste zamestnanca", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                conn = DBConnection.DBConnection();
                st = conn.createStatement();
                rs = st.executeQuery("select ou.meno,ou.priezvisko,ou.rod_cislo,ou.op_cislo,ob.nazov,adr.ulica,adr.cislo,zu.prac_pozicia from os_udaje ou\n"
                        + "join zamestnanec_ucet zu on (ou.rod_cislo = zu.rod_cislo)\n"
                        + "join pracujuci_zamestnanci pz on(zu.id_zamestnanca = pz.id_zamestnanca)\n"
                        + "full outer join adresa adr  on (adr.id_adresy=ou.id_adresy)\n"
                        + "full outer join obec ob on (adr.id_obce= ob.id_obce)\n"
                        + "where ou.meno+' '+ou.priezvisko like '" + celemeno + "'"
                        + "and pz.id_zachranky =" + zamestnavatel);
                while (rs.next()) {
                    jTextField10.setText(rs.getString(1));
                    jTextField11.setText(rs.getString(2));
                    jTextField12.setText(rs.getString(3));
                    jTextField13.setText(rs.getString(4));
                    jTextField14.setText(rs.getString(5));
                    jTextField15.setText(rs.getString(6));
                    jTextField16.setText(rs.getString(7));
                    jCB_zamestnanci.setEditable(false);
                    jCB_zamestnanci.removeAllItems();
                    jCB_zamestnanci.addItem(rs.getString(8));
                    jCB_zamestnanci.setSelectedItem(rs.getString(8));
                }
                rs = st.executeQuery(
                        "select zu.prac_pozicia from zamestnanec_ucet zu\n"
                        + "join pracujuci_zamestnanci pz on (zu.id_zamestnanca = pz.id_zamestnanca)\n"
                        + "where pz.id_zachranky is not null"
                        + "\n and zu.prac_pozicia not like '"
                        + jCB_zamestnanci.getSelectedItem().toString() + "'"
                        + "group by (zu.prac_pozicia)");
                while (rs.next()) {
                    jCB_zamestnanci.addItem(rs.getString(1));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                return;
            }
            jTextField10.setVisible(true);
            jTextField11.setVisible(true);
            jTextField12.setVisible(true);
            jTextField13.setVisible(true);
            jTextField14.setVisible(true);
            jTextField15.setVisible(true);
            jTextField16.setVisible(true);
            jTextField12.setEditable(false);
            jB_VZ_uprav_zamestnanca.setText("Zapíš opravu");
            return;
        }
        if (jB_VZ_uprav_zamestnanca.getText().equals("Zapíš opravu")) {

            String errType = "";
            //int infoo = 3;
            try {
                if (jCB_zamestnanci.getSelectedItem().toString().isEmpty()) {
                    errType += " Pracovná pozícia\n";
                    throw new IllegalArgumentException("INVALID");
                }

                if (jTextField10.getText().isEmpty() || jTextField10.getText().equals("Meno")) {
                    errType += " Meno \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField11.getText().isEmpty() || jTextField11.getText().equals("Priezvisko")) {
                    errType += " Priezvisko \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField12.getText().isEmpty() || jTextField12.getText().equals("Rodné číslo (XXXXXX/XXXX)")) {
                    errType += " Rodné číslo \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField13.getText().isEmpty() || jTextField13.getText().equals("Číslo OP")) {
                    errType += " Číslo OP\n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField14.getText().isEmpty() || jTextField14.getText().equals("Obec")) {
                    errType += " Obec \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField15.getText().isEmpty() || jTextField15.getText().equals("Ulica")) {
                    errType += " Ulica \n";
                    throw new IllegalArgumentException("INVALID");
                }
                if (jTextField16.getText().isEmpty() || jTextField16.getText().equals("Popisné číslo")) {
                    errType += " Popisné číslo \n";
                    throw new IllegalArgumentException("INVALID");
                }
                String pozicia = jCB_zamestnanci.getSelectedItem().toString();
                String meno = jTextField10.getText();
                String priezvisko = jTextField11.getText();
                String rodne_cislo = jTextField12.getText();
                String cisloOP = jTextField13.getText();
                String obec = jTextField14.getText();
                String ulica = jTextField15.getText();
                String PopisnéCis = jTextField15.getText();
                String adr = obec + "%7c" + ulica + "%7c" + PopisnéCis;
                System.out.println(adr);
                adr = adr.replace(" ", "+");
                Adresa adr1 = new Adresa();
                try {
                    adr1.setLocationsByAdress(adr);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                }

                String lng = adr1.getLng();
                String lat = adr1.getLat();
                String GPS = lng + " " + lat;
                System.out.println(GPS);

                conn = DBConnection.DBConnection();
                stored_pro = conn.prepareCall("{call PROC_UPDATE_ZAMESTNANEC_SKRATENE (?,?,?,?,?,?,?,?,?,?,?)}");
                stored_pro.setString(1, pozicia);
                stored_pro.setString(2, meno);
                stored_pro.setString(3, priezvisko);
                stored_pro.setString(4, rodne_cislo);
                stored_pro.setString(5, cisloOP);
                stored_pro.setString(6, obec);
                stored_pro.setString(7, ulica);
                stored_pro.setString(8, PopisnéCis);
                stored_pro.setInt(9, zamestnavatel);
                stored_pro.setString(10, GPS);
                stored_pro.registerOutParameter(11, java.sql.Types.INTEGER);
                stored_pro.executeUpdate();
                int infoo = stored_pro.getInt(11);
                stored_pro.close();
                conn.close();

                jTextField10.setText("Meno");
                jTextField11.setText("Priezvisko");
                jTextField12.setText("Rodné číslo (XXXXXX/XXXX)");
                jTextField13.setText("Číslo OP");
                jTextField14.setText("Obec");
                jTextField15.setText("Ulica");
                jTextField16.setText("Popisné číslo");
                jTextField10.setVisible(true);
                jTextField11.setVisible(true);
                jTextField12.setVisible(true);
                jTextField13.setVisible(true);
                jTextField14.setVisible(true);
                jTextField15.setVisible(true);
                jTextField16.setVisible(true);
                jTextField12.setEditable(true);
                jB_VZ_uprav_zamestnanca.setText("Uprav Zamestnanca");

                if (infoo == 1) {
                    JOptionPane.showMessageDialog(null, "Zamestnancovi boli upravené len osobné údaje a prac pozícia"
                            + "lebo taká obec neexistuje", "Úspešne upravený zamestnanec", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (infoo == 2) {
                    JOptionPane.showMessageDialog(null, "Zamestnancobi boli upravené osobné údaje,"
                            + "\n prac pozícia a adresa", "Úspešne upravený zamestnanec", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (infoo == 3) {
                    JOptionPane.showMessageDialog(null, "Zamestnancobi boli upravené osobné údaje,"
                            + "\n prac pozícia a pridaná nová adresa", "Úspešne upravený zamestnanec", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass().getCanonicalName() + ">" + e.getMessage());
                return;
            }

        }

// TODO add your handling code here:
    }//GEN-LAST:event_jB_VZ_uprav_zamestnancaActionPerformed

    private void jTextField10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField10MouseClicked
        if (jTextField10.getText().equals("Meno")) {
            jTextField10.setText("");
            repaint();
            revalidate();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10MouseClicked

    private void jTextField11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField11MouseClicked

        if (jTextField11.getText().equals("Priezvisko")) {
            jTextField11.setText("");
            repaint();
            revalidate();
        }
    }//GEN-LAST:event_jTextField11MouseClicked

    private void jTextField12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField12MouseClicked
        if (jTextField12.getText().equals("Rodné číslo (XXXXXX/XXXX)")) {
            jTextField12.setText("");
            repaint();
            revalidate();
        }
    }//GEN-LAST:event_jTextField12MouseClicked

    private void jTextField13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField13MouseClicked

        if (jTextField13.getText().equals("Číslo OP")) {
            jTextField13.setText("");
            repaint();
            revalidate();
        } // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13MouseClicked

    private void jTextField14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField14MouseClicked

        jTextField15.setText("Ulica");
        jTextField16.setText("Popisné číslo");
        if (jTextField14.getText().equals("Obec")) {
            jTextField14.setText("");
            repaint();
            revalidate();
        }// TODO add your handling code here:
    }//GEN-LAST:event_jTextField14MouseClicked

    private void jTextField15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField15MouseClicked
        jTextField16.setText("Popisné číslo");
        if (jTextField15.getText().equals("Ulica")) {
            jTextField15.setText("");
            repaint();
            revalidate();
        }// TODO add your handling code here:       
    }//GEN-LAST:event_jTextField15MouseClicked

    private void jTextField16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField16MouseClicked
        if (jTextField16.getText().equals("Popisné číslo")) {
            jTextField16.setText("");
            repaint();
            revalidate();
        }// TODO add your handling code here:     
    }//GEN-LAST:event_jTextField16MouseClicked

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked

        if (jTextField2.getText().equals("Obec")) {
            jTextField2.setText("");
            repaint();
            revalidate();
        }// TODO add your handling code here:
    }//GEN-LAST:event_jTextField2MouseClicked

    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked

        if (jTextField3.getText().equals("Ulica")) {
            jTextField3.setText("");
            repaint();
            revalidate();
        }
    }//GEN-LAST:event_jTextField3MouseClicked

    private void jTextField4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField4MouseClicked
        if (jTextField4.getText().equals("Popisné číslo")) {
            jTextField4.setText("");
            repaint();
            revalidate();
        }  // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4MouseClicked

    private void jTextField5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField5MouseClicked
        if (jTextField5.getText().equals("Meno Šéfa")) {
            jTextField5.setText("");
            repaint();
            revalidate();
        }
    }//GEN-LAST:event_jTextField5MouseClicked

    private void jTextField6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField6MouseClicked
        if (jTextField6.getText().equals("Priezvisko")) {
            jTextField6.setText("");
            repaint();
            revalidate();
        }
    }//GEN-LAST:event_jTextField6MouseClicked

    private void jTextField7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField7MouseClicked
        if (jTextField7.getText().equals("Rodné číslo formát:\"XXXXXX/XXXX\"")) {
            jTextField7.setText("");
            repaint();
            revalidate();
        }
    }//GEN-LAST:event_jTextField7MouseClicked

    private void jTextField8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField8MouseClicked
        if (jTextField8.getText().equals("telefón1")) {
            jTextField8.setText("");
            repaint();
            revalidate();
        }
    }//GEN-LAST:event_jTextField8MouseClicked

    private void jTextField9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField9MouseClicked
        if (jTextField9.getText().equals("telefón2")) {
            jTextField9.setText("");
            repaint();
            revalidate();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9MouseClicked

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed
    /**
     * Metóda slúžiaca na vymazanie textu z jTextField1 ()
     * @param evt 
     */
    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        
        if (jTextField1.getText().equals("Názov")) {
            jTextField1.setText("");
            repaint();
            revalidate();
        }       // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1MouseClicked

    /**
     * Main metóda na spustenie pro
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginGUI().setVisible(true);
            }
        });

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Administrácia;
    private javax.swing.JButton JBLoginFinal;
    private javax.swing.JEditorPane JEP_NEM_TextArea;
    private javax.swing.JFrame JFLogin;
    private javax.swing.JFrame JFNemocnicaGUI;
    private javax.swing.JFrame JFZachrankaVelitel;
    private javax.swing.JTextArea JTA_OZ_infopacient;
    private javax.swing.JTextArea JTA_OZ_infozasah;
    private javax.swing.JTable JTAktualneZasahyZachranky;
    private javax.swing.JTable JTB_NZ_POS;
    private javax.swing.JTextField JTFOSPrih;
    private javax.swing.JTable JTabNemocnicaZasah;
    private javax.swing.JTable JTableAktualneZasahy;
    private javax.swing.JTable JTablePosadky;
    private javax.swing.JButton jBVytvorZachranku;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice1;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice10;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice11;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice15;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice16;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice2;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice3;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice5;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice6;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice8;
    private javax.swing.JButton jB_AZ_PrijazdDoNemocnice9;
    private javax.swing.JButton jB_AZ_ZobrazHistoriu;
    private javax.swing.JButton jB_AZ_ZobrazZamestnancov;
    private javax.swing.JButton jB_FOP_PrijazdDoZariadenia;
    private javax.swing.JButton jB_FOP_PrijazdNaStanicu;
    private javax.swing.JButton jB_FOP_PrijazdPosadkyKPacientovi;
    private javax.swing.JButton jB_FOP_UzavriZasah;
    private javax.swing.JButton jB_FOP_ZapisPrevozDoNemocnice;
    private javax.swing.JButton jB_FOP_zapisNavratNaStanicu;
    private javax.swing.JButton jB_FOP_zapisVyjazdKPacientovi;
    private javax.swing.JButton jB_FZV_PrijazdDoZariadenia;
    private javax.swing.JButton jB_FZV_PrijazdNaStanicu;
    private javax.swing.JButton jB_FZV_PrijazdPosadkyKPacientovi;
    private javax.swing.JButton jB_FZV_UzavriZasah;
    private javax.swing.JButton jB_FZV_Zapis_prevozDoNemocnice;
    private javax.swing.JButton jB_FZV_vyjazdPosadky;
    private javax.swing.JButton jB_FZV_zapisNavratNaStanicu;
    private javax.swing.JButton jB_NZ_HladajPosadky;
    private javax.swing.JButton jB_NZ_vydajZasah;
    private javax.swing.JButton jB_VZ_uprav_zamestnanca;
    private javax.swing.JButton jB_Velitel_nova_posadka;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonOdhlasenie;
    private javax.swing.JButton jButtonOdhlasenie1;
    private javax.swing.JButton jButtonOdhlasenie3;
    private javax.swing.JCheckBox jCBDATUM;
    private javax.swing.JCheckBox jCBDATUM2;
    private javax.swing.JCheckBox jCBDiagnozy;
    private javax.swing.JCheckBox jCBNemocnice;
    private javax.swing.JComboBox<String> jCBNemocnice1;
    private javax.swing.JComboBox<String> jCBNemocnice2;
    private javax.swing.JCheckBox jCBTPDAno;
    private javax.swing.JCheckBox jCBTPDNie;
    private javax.swing.JCheckBox jCBTypdiagnozy;
    private javax.swing.JCheckBox jCBZachranka;
    private javax.swing.JCheckBox jCBZachranka1;
    private javax.swing.JComboBox<String> jCB_FOP_Nemocnice;
    private javax.swing.JComboBox<String> jCB_FOP_Nemocnice1;
    private javax.swing.JComboBox<String> jCB_FOP_Zachranky;
    private javax.swing.JComboBox<String> jCB_FOP_Zachranky1;
    private javax.swing.JComboBox<String> jCB_NZ_diagnozy;
    private javax.swing.JComboBox<String> jCB_NZ_diagnozy1;
    private javax.swing.JComboBox<String> jCB_NZ_diagnozy2;
    private javax.swing.JComboBox<String> jCB_NZ_obce;
    private javax.swing.JComboBox<String> jCB_NZ_typDiagnozy;
    private javax.swing.JComboBox<String> jCB_NZ_typDiagnozy1;
    private javax.swing.JComboBox<String> jCB_NZ_typDiagnozy2;
    private javax.swing.JComboBox<String> jCB_OZ_Konecna_Diag;
    private javax.swing.JComboBox<String> jCB_OZ_Pp_Diag;
    private javax.swing.JComboBox<String> jCB_OZ_obce;
    private javax.swing.JCheckBox jCB_RLP;
    private javax.swing.JCheckBox jCB_RZP;
    private javax.swing.JComboBox<String> jCB_zamestnanci;
    private javax.swing.JCheckBox jCKB_pozicia_lekar;
    private javax.swing.JCheckBox jCKB_pozicia_velitel;
    private javax.swing.JCheckBox jCKB_pozicia_vodic;
    private javax.swing.JCheckBox jCKB_pozicia_vsetci;
    private javax.swing.JCheckBox jCKB_pozicia_zachranar;
    private javax.swing.JCheckBox jCKB_stav_v_sluzbe;
    private javax.swing.JCheckBox jCKB_stav_volno;
    private javax.swing.JCheckBox jCKB_stav_vsetkych;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBoxUserType;
    private com.toedter.calendar.JDateChooser jDateChooserDO;
    private com.toedter.calendar.JDateChooser jDateChooserDO1;
    private com.toedter.calendar.JDateChooser jDateChooserDO2;
    private com.toedter.calendar.JDateChooser jDateChooserOD;
    private com.toedter.calendar.JDateChooser jDateChooserOD1;
    private com.toedter.calendar.JDateChooser jDateChooserOD2;
    private javax.swing.JLabel jDo;
    private javax.swing.JLabel jDo1;
    private javax.swing.JEditorPane jEPHistoriaOperat;
    private javax.swing.JEditorPane jEPHistoriaVelitel;
    private javax.swing.JFrame jFOknoOperator;
    private javax.swing.JFrame jFOpravZasah;
    private javax.swing.JLabel jL_NZ_PopisneCislo;
    private javax.swing.JLabel jL_NZ_Priezvisko;
    private javax.swing.JLabel jL_NZ_TelCislo;
    private javax.swing.JLabel jL_NZ_TelCislo10;
    private javax.swing.JLabel jL_NZ_TelCislo11;
    private javax.swing.JLabel jL_NZ_TelCislo12;
    private javax.swing.JLabel jL_NZ_TelCislo13;
    private javax.swing.JLabel jL_NZ_TelCislo14;
    private javax.swing.JLabel jL_NZ_TelCislo15;
    private javax.swing.JLabel jL_NZ_TelCislo16;
    private javax.swing.JLabel jL_NZ_TelCislo17;
    private javax.swing.JLabel jL_NZ_TelCislo18;
    private javax.swing.JLabel jL_NZ_TelCislo19;
    private javax.swing.JLabel jL_NZ_TelCislo2;
    private javax.swing.JLabel jL_NZ_TelCislo20;
    private javax.swing.JLabel jL_NZ_TelCislo3;
    private javax.swing.JLabel jL_NZ_TelCislo6;
    private javax.swing.JLabel jL_NZ_TelCislo7;
    private javax.swing.JLabel jL_NZ_TelCislo8;
    private javax.swing.JLabel jL_NZ_TelCislo9;
    private javax.swing.JLabel jL_NZ_Ulica;
    private javax.swing.JLabel jL_NZ_meno;
    private javax.swing.JLabel jL_NZ_obec;
    private javax.swing.JLabel jL_NZ_pacientInfo;
    private javax.swing.JLabel jL_NZ_rodCis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelPrihlasenyAko;
    private javax.swing.JLabel jLabelPrihlasenyAko2;
    private javax.swing.JLabel jLabelPrihlasenyAko3;
    private javax.swing.JLabel jLabelUserName;
    private javax.swing.JLabel jLabelUserPasswd;
    private javax.swing.JLabel jLabelZdravotnickeZariadenie1;
    private javax.swing.JLabel jLabelZdravotnickeZariadenie2;
    private javax.swing.JLabel jLabelZdravotnickeZariadenie3;
    private javax.swing.JLabel jOd;
    private javax.swing.JLabel jOd1;
    private javax.swing.JLabel jOd2;
    private javax.swing.JLabel jOd3;
    private javax.swing.JPanel jPAktualneZasahy;
    private javax.swing.JPanel jPNovyZasah;
    private javax.swing.JPanel jPTestovaci;
    private javax.swing.JPanel jPZasahUdaje;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordFieldUserPasswd;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTFOS;
    private javax.swing.JTextField jTF_NZ_meno;
    private javax.swing.JTextArea jTF_NZ_pacientInfo;
    private javax.swing.JTextField jTF_NZ_popCis;
    private javax.swing.JTextField jTF_NZ_priezvisko;
    private javax.swing.JTextField jTF_NZ_rodCis;
    private javax.swing.JTextField jTF_NZ_tel;
    private javax.swing.JTextField jTF_NZ_ulica1;
    private javax.swing.JTextField jTF_OZ_IdZasahu;
    private javax.swing.JTextField jTF_OZ_PopisCislo;
    private javax.swing.JTextField jTF_OZ_meno;
    private javax.swing.JTextField jTF_OZ_priezvisko;
    private javax.swing.JTextField jTF_OZ_rodCislo;
    private javax.swing.JTextField jTF_OZ_tel;
    private javax.swing.JTextField jTF_OZ_ulica;
    private javax.swing.JTable jTLogin;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTablePosadkyZachranky;
    private javax.swing.JTable jTableStatistika;
    private javax.swing.JTextField jTextCAS;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField jTextFieldPrihlasenyAKo;
    private javax.swing.JTextField jTextFieldPrihlasenyAKo2;
    private javax.swing.JTextField jTextFieldUserName;
    private javax.swing.JTextField jTextFieldZachranka;
    private javax.swing.JTextField jTextFieldZdravotnickeZariadenie;
    // End of variables declaration//GEN-END:variables
}
