package at.htl.bank.business;

import at.htl.bank.model.BankKonto;
import at.htl.bank.model.GiroKonto;
import at.htl.bank.model.SparKonto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Legen Sie eine statische Liste "konten" an, in der Sie die einzelnen Konten speichern
 *
 */
public class Main {

  // die Konstanten sind package-scoped wegen der Unit-Tests
  static final double GEBUEHR = 0.02;
  static final double ZINSSATZ = 3.0;

  static final String KONTENDATEI = "erstellung.csv";
  static final String BUCHUNGSDATEI = "buchungen.csv";
  static final String ERGEBNISDATEI = "ergebnis.csv";

  static List<BankKonto> konten = new ArrayList<>();

  
  /**
   * Führen Sie die drei Methoden erstelleKonten, fuehreBuchungenDurch und
   * findKontoPerName aus
   *
   * @param args
   */
  public static void main(String[] args) {

    erstelleKonten(KONTENDATEI);
    fuehreBuchungenDurch(BUCHUNGSDATEI);
    schreibeKontostandInDatei(ERGEBNISDATEI);

  }

  /**
   * Lesen Sie aus der Datei (erstellung.csv) die Konten ein.
   * Je nach Kontentyp erstellen Sie ein Spar- oder Girokonto.
   * Gebühr und Zinsen sind als Konstanten angegeben.
   *
   * Nach dem Anlegen der Konten wird auf der Konsole folgendes ausgegeben:
   * Erstellung der Konten beendet
   *
   * @param datei KONTENDATEI
   */
  private static void erstelleKonten(String datei) {

    try (Scanner scanner = new Scanner(new FileReader(datei))){
      scanner.nextLine();
      while (scanner.hasNextLine()){
        String line = scanner.nextLine();
        String [] array = line.split(";");
        String konto = array[0];
        String name = array[1];
        double anfangsBestand = Double.parseDouble(array[2]);


        if (konto.equals("Sparkonto")) {
          SparKonto sparKonto = new SparKonto(name, anfangsBestand, ZINSSATZ);
          konten.add(sparKonto);
        } else if(konto.equals("Girokonto")) {
          GiroKonto giroKonto = new GiroKonto(name, anfangsBestand, ZINSSATZ);
          konten.add(giroKonto);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }

    System.out.println("Erstellung der Konten beendet.");

  }

  /**
   * Die einzelnen Buchungen werden aus der Datei eingelesen.
   * Es wird aus der Liste "konten" jeweils das Bankkonto für
   * kontoVon und kontoNach gesucht.
   * Anschließend wird der Betrag vom kontoVon abgebucht und
   * der Betrag auf das kontoNach eingezahlt
   *
   * Nach dem Durchführen der Buchungen wird auf der Konsole folgendes ausgegeben:
   * Buchung der Beträge beendet
   *
   * Tipp: Verwenden Sie hier die Methode 'findeKontoPerName(String name)'
   *
   * @param datei BUCHUNGSDATEI
   */
  private static void fuehreBuchungenDurch(String datei) {

    try (Scanner scanner = new Scanner(new FileReader(BUCHUNGSDATEI))) {

      while (scanner.hasNextLine()){
        scanner.nextLine();

        String line = scanner.nextLine();
        String [] array = line.split(";");
        String kontoVon = array[0];
        String kontoNach = array[1];
        double betrag = Double.parseDouble(array[2]);


        BankKonto bankKonto1 = findeKontoPerName(kontoVon);
        bankKonto1.abheben(betrag);

        BankKonto bankKonto2 = findeKontoPerName(kontoNach);
        bankKonto2.einzahlen(betrag);

      }
      System.out.println("Buchung der Beträge beendet");

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

  /**
   * Es werden die Kontostände sämtlicher Konten in die ERGEBNISDATEI
   * geschrieben. Davor werden bei Sparkonten noch die Zinsen dem Konto
   * gutgeschrieben
   *
   * Die Datei sieht so aus:
   *
   * name;kontotyp;kontostand
   * Susi;SparKonto;875.5
   * Mimi;GiroKonto;949.96
   * Hans;GiroKonto;1199.96
   *
   * Vergessen Sie nicht die Überschriftenzeile
   *
   * Nach dem Schreiben der Datei wird auf der Konsole folgendes ausgegeben:
   * Ausgabe in Ergebnisdatei beendet
   *
   * @param datei ERGEBNISDATEI
   */
  private static void schreibeKontostandInDatei(String datei) {



    try (PrintWriter writer = new PrintWriter(new FileWriter(ERGEBNISDATEI))) {
      writer.println("name;kontotyp;kontostand");

      for (int i = 0; i < konten.size(); i++) {

        if (konten.get(i) instanceof SparKonto){
          writer.println(konten.get(i).getName() + ";Sparkonto;" + konten.get(i).getKontoStand());
        }else if (konten.get(i) instanceof GiroKonto){
          writer.println(konten.get(i).getName() + ";Girokonto;" + konten.get(i).getKontoStand());
        }

      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Ausgabe in Ergebnisdatei beendet");
  }

  /**
   */
  /**
   * Durchsuchen Sie die Liste "konten" nach dem ersten Konto mit dem als Parameter
   * übergebenen Namen
   * @param name
   * @return Bankkonto mit dem gewünschten Namen oder NULL, falls der Namen
   *         nicht gefunden wird
   */
  public static BankKonto findeKontoPerName(String name) {

    for (int i = 0; i < konten.size(); i++) {
      if (konten.get(i).getName().equals(name)){
        return konten.get(i);
      }
    }
       return null;
  }

}
