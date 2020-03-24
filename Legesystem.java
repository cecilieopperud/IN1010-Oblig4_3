import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Legesystem{
    private static Liste<Pasient> pasientListe = new Lenkeliste<Pasient>();
    private static Liste<Lege> legeListe = new Lenkeliste<Lege>();
    private static Liste<Legemiddel> legemiddelListe = new Lenkeliste<Legemiddel>();
    private static Liste<Resepter> reseptListe = new Lenkeliste<Resepter>();
    private String fil;
    public Legesystem(String f){
      fil = f;
      gaaGjennom();
    }

    public void gaaGjennom() {
      File filen = new File(fil);
      Scanner innlesing = null;

      try { //try og catch uttalelse for aa sjekke input til innlesing.
        innlesing = new Scanner(filen);
        System.out.println("Fant fil og henter data");
      }

      catch (FileNotFoundException e) { //Dersom det blir error blir stringen under skrevet ut.
        System.out.println("Fant desverre ikke filen");

      }

      int teller = 0;
      while(innlesing.hasNextLine()){
        String denneLinja = innlesing.nextLine();

        if(denneLinja.startsWith("#")){
        teller ++;
        denneLinja = innlesing.nextLine();
      }
        if(teller == 1){

            String[] biter= denneLinja.split(",");
            if(biter.length == 2){
              String navn = biter[0];
              String fnr = biter[1];
              Pasient pasient = new Pasient(navn, fnr);
              pasientListe.leggTil(pasient);

         }
         else{
          feilmelding();
          }
          }

        if(teller == 2){
          int tellern = 0;
          tellern++;
          System.out.print(tellern + " gaar gjennom legemidler\n");
          String[] biter = denneLinja.split(",");
          if (biter.length == 5){
          String navn = biter[0];
          String type = biter[1];
          double pris = Double.parseDouble(biter[2]);
          double virkestoff = Double.parseDouble(biter[3]);
          int styrke = Integer.parseInt(biter[4]);

          if(type.equals("narkotisk")){
            Narkotisk narkotisk = new Narkotisk(navn, pris, virkestoff, styrke);
            legemiddelListe.leggTil(narkotisk);

          }

          else if(type.equals("vanedannende")){
            Vanedannende vanedannende = new Vanedannende(navn, pris, virkestoff, styrke);
            legemiddelListe.leggTil(vanedannende);

          }
        }
          else if(biter.length == 4 && biter[1].equals("vanlig")){
            String navn = biter[0];
            double pris = Double.parseDouble(biter[2]);
            double virkestoff = Double.parseDouble(biter[3]);
            Vanlig vanlig = new Vanlig(navn, pris, virkestoff);
              legemiddelListe.leggTil(vanlig);

          }

        else{
          feilmelding();
        }

        }

        if(teller == 3){
          int tellern = 0;
          tellern++;
          System.out.print(tellern + " gaar gjennom lege \n");
          String [] biter = denneLinja.split(",");
          if(biter.length == 2){
          String navn = biter[0];
          int kontrollid = Integer.parseInt(biter[1]);


          if(kontrollid == 0){
            Lege lege = new Lege(navn);
            legeListe.leggTil(lege);

        }

          else{
            Spesialist spesialist = new Spesialist(navn, kontrollid);
            legeListe.leggTil(spesialist);

          }
        }

        else{
          feilmelding();
        }

      }

        if(teller == 4){
          int tellern = 0;
          tellern++;
          System.out.print(tellern + " gaar gjennom resept\n");
          String [] biter = denneLinja.split(",");

              int legemiddelNummer = Integer.parseInt(biter[0]);
              String legeNavn = biter[1];
              int pasientID = Integer.parseInt(biter[2]);
              String type = biter[3];


          Legemiddel legemiddelet = legemiddelListe.hent(legemiddelNummer);
          Pasient riktigPasient = pasientListe.hent(pasientID);
          Lege rettLege = null;

          for(int i = 0; i < legeListe.stoerrelse(); i++){
            Lege legen = legeListe.hent(i);
            if(legen.hentNavn().equals(legeNavn)){
              rettLege = legen;
            }
          }

          Resepter resepten = null;
          try{
            if(type.equals("blaa")){
                resepten = rettLege.skrivBlaaResept(legemiddelet, riktigPasient, Integer.parseInt(biter[4]));
                System.out.print("Printer ut BlaaResept");
              }

          else if(type.equals("hvit")){
                resepten =  rettLege.skrivHvitResept(legemiddelet, riktigPasient, Integer.parseInt(biter[4]));
                System.out.print("Printer ut HvitResept");
                }

          else if(type.equals("militaer")){
              resepten =  rettLege.skrivMillitaerResept(legemiddelet, riktigPasient, Integer.parseInt(biter[4]));
              System.out.print("Printer ut MilitaerResept");
              }

          else if(type.equals("p")){
              resepten = rettLege.skrivPResept(legemiddelet, riktigPasient);
                System.out.print("Printer ut P-resept");
                }
                reseptListe.leggTil(resepten);
              }

          catch(UlovligUtskrift u){
            System.out.println("Ugyldig");
          }
        }
      }
    }
    public Liste<Resepter> retunerReseptListe(){
      return reseptListe;
      }

    public Liste<Legemiddel> retunerLegemiddelListe(){
      return legemiddelListe;
    }

    public Liste<Lege> retunerLegeListe(){
      return legeListe;
    }

    public Liste<Pasient> retunerPasientListe(){
      return pasientListe;
    }
    public void feilmelding(){
      System.out.println("Ugyldig input");
    }

}
