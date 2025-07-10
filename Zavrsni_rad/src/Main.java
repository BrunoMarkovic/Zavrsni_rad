import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        // unos zelite li random generiranu matricu ili putanju do datoteke
        Scanner randomIliPutanja = new Scanner(System.in);
        System.out.print("Unesite 0 ako želite unijeti putanju do datoteke ili 1 ako želite koristiti random generiranu matricu -> ");
        String rPiliB = randomIliPutanja.nextLine();


        int randomIliPutanjaBroj = parseIntOrDefault(rPiliB, -1);
        while (randomIliPutanjaBroj != 0 && randomIliPutanjaBroj != 1) {
            System.out.print("Unesite 0 ili 1 -> ");
            rPiliB = randomIliPutanja.nextLine();
            randomIliPutanjaBroj = parseIntOrDefault(rPiliB, -1);
        }
        System.out.print("Unesite 0 ako želite samo formulu, 1 ako želite Minty-a, 2 ako želite 3. algoritam, 3 ako želite sve -> ");
        String algoritmi1 = randomIliPutanja.nextLine();
        int algoritmi = parseIntOrDefault(algoritmi1, -1);
        boolean greska = false;
        int brojStupaca = -1;
        int brojRedaka = 0;
        double sansa = -1;
        int brojGrafova = 1;
        String path = "";
        boolean greskaAlgoritmi = false;


        // gledamo treba li generirati ili ucitati matricu
        if (randomIliPutanjaBroj == 0) {
            brojGrafova = 1;
            System.out.print("Pošaljite url csv datoteke u kojoj se nalazi matrica susjedstva -> ");
            path = randomIliPutanja.nextLine();
            File f = new File(path);
            Scanner brojac = new Scanner(f);

            while (brojac.hasNextLine()) {
                brojac.nextLine();
                brojRedaka++;
            }


        } else {
            // unos podataka za random generiranu matricu
            if (randomIliPutanjaBroj == 1) {

                System.out.print("Unesite broj redaka i stupaca -> ");
                brojRedaka = randomIliPutanja.nextInt();
                brojStupaca = brojRedaka;
                System.out.print("Unesite šansu za stvaranje brida između dva vrha -> ");
                sansa = randomIliPutanja.nextDouble();
                while (sansa < 0 || sansa > 1) {
                    System.out.print("Šansa mora biti između 0 i 1 -> ");
                    sansa = randomIliPutanja.nextDouble();
                }
                System.out.print("Unesite koliko grafova želite generirati -> ");
                brojGrafova = randomIliPutanja.nextInt();

            }

        }


        // inicijalizacija matrice i potrebnih matrica za formulu
        int matrica[][] = new int[brojRedaka][brojRedaka]; // napravi matricu ako nije isti br redaka i stupaca svakako ce gresku javit kasnije
        int DijagonalnaMatricaStupnja[][] = new int[brojRedaka][brojRedaka];
        int LaPlaceovaMatrica[][] = new int[brojRedaka][brojRedaka];
        int n = 0;


        if (randomIliPutanjaBroj == 0) {
            Scanner citac = new Scanner(new File(path));
            // ucitaj matricu
            while (citac.hasNextLine()) {

                // split po zarezima jer je csv datoteka
                String data[] = citac.nextLine().split(",");

                // provjeri jeli svaki red ima isto stupaca ako nema onda je kriv zapis u csv datoteci
                if (brojStupaca == -1) {
                    brojStupaca = data.length;
                }
                if (brojStupaca != -1 && brojStupaca != data.length) {
                    System.err.println("greška u csv datoteci, nije jednak broj stupaca");
                    greska = true;
                    break;
                }

                for (int i = 0; i < data.length; i++) {
                    matrica[n][i] = Integer.parseInt(data[i].trim()) > 0 ? 1 : 0; // pise normlnu matricu al nam ne trebaju tezine bridova pa ih samo brise tj stavlja na 1
                    DijagonalnaMatricaStupnja[n][n] = Integer.parseInt(data[i].trim()) > 0 ? ++DijagonalnaMatricaStupnja[n][n] : DijagonalnaMatricaStupnja[n][n]; // racuna Dijagonalnu matricu koju cu kasnije koristit za Laplaceovu matricu
                    if (n == i && matrica[n][i] != 0 && !greska) {
                        System.err.println("Greška, nije jednostavan graf");
                        greska = true;

                        break;
                    }
                }
                n++; // sluzi da znam u kojem retku matrice se nalazim
            }

            // ispise gresku ako je kriv upis
            if (!greska) {
                if (brojRedaka != brojStupaca) {
                    System.err.println("Greška nije jednak broj redaka i stupaca");
                    greska = true;
                }
            }

            // provjeri ako je matrica na i,j jednaka matrici na j,i ako nije onda je krivo zadano
            if (!greska) {
                for (int i = 0; i < brojStupaca; i++) {
                    for (int j = i + 1; j < brojStupaca; j++) {
                        if (matrica[i][j] != matrica[j][i]) {
                            System.err.println("Greška, matrica na mjestu [" + i + "][" + j + "] nije ista kao matrica na mjestu [" + j + "][" + i + "]");
                            greska = true;
                        }
                    }
                }
            }
            citac.close();
        }
        System.out.println();

        // sada ponavljamo algoritam broj puta koji je korisnik unio tj generiramo toliko grafova i provjeravamo
        for (int nekiBrojZaBrojGrafova = 0; nekiBrojZaBrojGrafova < brojGrafova; nekiBrojZaBrojGrafova++) {

            // generiranje random matrice
            if (randomIliPutanjaBroj == 1) {
                Random rand = new Random();
                for (int i = 0; i < brojRedaka; i++) {
                    for (int j = i; j < brojRedaka; j++) {
                        if (i == j) {
                            matrica[i][j] = 0;
                        } else {
                            if (rand.nextDouble() < sansa) {
                                matrica[i][j] = 1;
                                matrica[j][i] = 1;
                            } else {
                                matrica[i][j] = 0;
                                matrica[j][i] = 0;
                            }
                        }
                    }
                }
                for (int i = 0; i < brojRedaka; i++) {
                    for (int j = 0; j < brojRedaka; j++) {
                        DijagonalnaMatricaStupnja[i][j] = 0;
                    }
                }
                for (int i = 0; i < brojRedaka; i++) {
                    for (int j = 0; j < brojRedaka; j++) {
                        DijagonalnaMatricaStupnja[i][i] += matrica[i][j];
                    }
                }

                greska = false;
            }

            // provjeri jeli matrica povezana
            if (!Main.DFS(matrica, brojRedaka)) {
                greska = true;
            }
            // ako matrica nije pravilna ispisi gresku i opet krenuti stvaranje tj iduca iteracija id graf
            if (greska) {
                System.err.println("Zadana ili generirana matrica nije ispravna");
                continue;
            }

            // ako nema gresaka idu algoritmi
            if (!greska) {


                // ----------
                // FORMULA
                long pocetakVremenaFormula = System.nanoTime();
                for (int i = 0; i < brojRedaka; i++) {
                    for (int j = 0; j < brojRedaka; j++) {
                        LaPlaceovaMatrica[i][j] = DijagonalnaMatricaStupnja[i][j] - matrica[i][j];
                    }
                }
                int[][] MinoraLaplacove = new int[brojRedaka - 1][brojRedaka - 1];
                minoraMatrice(LaPlaceovaMatrica, MinoraLaplacove, 0, 0, LaPlaceovaMatrica.length);
                Long determinanta = determinantaMatrice(MinoraLaplacove, MinoraLaplacove.length);
                long krajVremenaFormula = System.nanoTime();
                // ---------------
                // 1. ALGORITAM
                // prebroji koliko ima bridova unutar matrice susjedstva da znas koliko ce biti stupaca u matrici incidencije
                int brojacBridova = 0;
                for (int i = 0; i < brojRedaka; i++) {
                    for (int j = i; j < brojRedaka; j++) {
                        if (matrica[i][j] > 0) {
                            brojacBridova++;
                        }
                    }
                }
                // napravi matricu incidencije
                int matricaIncidencije[][] = new int[brojRedaka + 1][brojacBridova];

                // oznaci broj brida u prvi redak matrice incidencije s negativnim brojevima
                for (int i = 0; i < brojacBridova; i++) {
                    matricaIncidencije[0][i] = -i - 1;

                }

                // popuni matricu incidencije
                brojacBridova = 0;
                for (int i = 0; i < brojRedaka; i++) {
                    for (int j = i; j < brojRedaka; j++) {
                        if (matrica[i][j] != 0) {
                            matricaIncidencije[i + 1][brojacBridova] = 1;
                            matricaIncidencije[j + 1][brojacBridova] = 1;
                            brojacBridova++;
                        }
                    }
                }

                List<Integer> list = new LinkedList<>();
                int imeCv = 1;

                // u klasu matrica stavi matricu incidencije
                Matrica KlasaMatrica = new Matrica(brojRedaka + 1, brojacBridova, matricaIncidencije);
                long pocetakMinty = System.nanoTime();
                if (algoritmi == 1 || algoritmi == 3) {
                    rekurzivnaFja(matricaIncidencije, list, brojRedaka + 1, brojacBridova, KlasaMatrica, 0);
                    if (determinanta == 1) {
                        KlasaMatrica.setBrojRazapinjucihStabala(1L);
                    }
                    if (Long.compare(determinanta, KlasaMatrica.getBrojRazapinjucihStabala()) != 0) {
                        greskaAlgoritmi = true;
                    }
                }
                long krajMinty = System.nanoTime();
// -------------------------------------------------------

                // 3. ALGORITAM
                long pocetakAlgoritma3 = System.nanoTime();
                if (algoritmi == 2 || algoritmi == 3) {
                    List<String> listaBridova = new ArrayList<>(); // sada idemo stupac po stupac i dodajemo brid u obliku "Vrh1-Vrh2" bitna je - da mozemo po tome splitat
                    for (int i = 0; i < KlasaMatrica.getBrojStupaca(); i++) {
                        StringBuilder sbBridovi = new StringBuilder();
                        int brJedinica = 0;
                        for (int j = 1; j <= brojRedaka; j++) {
                            if (KlasaMatrica.getMatrica()[j][i] == 1) {
                                if (brJedinica == 0) {
                                    sbBridovi.append((j - 1) + "-");
                                } else {
                                    sbBridovi.append((j - 1));
                                }
                                ++brJedinica;
                            }

                        }
                        listaBridova.add(sbBridovi.toString());
                        sbBridovi.delete(0, sbBridovi.length());
                    }
                    List<List<String>> sveKombinacijeBridova = Combinations.combine(listaBridova, brojRedaka - 1);
// sada kad iman sve kombinacije bridova moram izbacit sve one koje nisu povezane tj moram za svaku listu u listi provuci DFS
// iduci korak onda je pretvoriti liste u listi u matrice i nad tim matricama provuci DFS
// moram pazit kad pretvaran u matricu dobit cu matricu incidencije i onda tu matricu incidencije moram pretvorit nazad u normalnu matricu


                    List<Integer> popisZaIzbacivanje = new LinkedList<>();
                    for (int i = 0; i < sveKombinacijeBridova.size(); i++) {
                        List<String> iteratorLista = sveKombinacijeBridova.get(i);
                        int[][] privremenaMatrica = pretvoriListuUMatricuSusj(iteratorLista, brojRedaka);
                        Boolean JeliOstaje = DFS(privremenaMatrica, privremenaMatrica.length);
                        if (!JeliOstaje) {
                            popisZaIzbacivanje.add(i);
                        }
                    }
                    popisZaIzbacivanje.sort((a1, b1) -> -Integer.compare(a1, b1));
                    // sad kad imamo popis za izbacivanje moramo samo izbacit to sortirali smo ga obrnuto jer da je sortiran normalno kad bi makli prvi onda bi drugi posta prvi
                    for (int index = 0; index < popisZaIzbacivanje.size(); index++) {
                        sveKombinacijeBridova.remove((int) popisZaIzbacivanje.get(index));
                    }
                    if (sveKombinacijeBridova.size() != determinanta) {
                        greskaAlgoritmi = true;
                    }
                }

                long krajAlgoritma3 = System.nanoTime();
                // ZAVRSETAK 3 ALGORITMA


                if (!greskaAlgoritmi && !greska) {
                    System.out.println("Algoritmi su dali isti rezultat -> " + determinanta);
                } else {
                    if (!greska) {
                        greskaAlgoritmi = true;
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println("Algoritmi nisu dali isti rezultat");
                        System.out.println("Broj razapinjućih stabala preko algoritma 1 je -> " + KlasaMatrica.getBrojRazapinjucihStabala());
                        System.out.println("Broj razapinjućih stabala preko determinante je -> " + determinanta);
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println("Matrica je: ");
                        for (int i = 0; i < matrica.length; i++) {
                            for (int j = 0; j < matrica.length; j++) {
                                if (j == matrica.length - 1)
                                    System.out.print(matrica[i][j]);
                                else
                                    System.out.print(matrica[i][j] + ",");
                            }
                            System.out.println();
                        }
                    }
                }
                /*
                File file256 = new File("/Users/bukizo/Bruno-Fer/6.semestar/ZAVRSNI_RAD/rezultati/rezultati.txt");
                PrintWriter writer = new PrintWriter(new FileWriter(file256, true));
                if (!greskaAlgoritmi && !greska) {
                    if (algoritmi == 0) {
                        writer.println(brojRedaka + "," + determinanta + "," + brojacBridova + "," + (krajVremenaFormula - pocetakVremenaFormula) / 1000. + "," + -1000 + "," + -1000);
                    } else {
                        if (algoritmi == 1) {
                            writer.println(brojRedaka + "," + determinanta + "," + brojacBridova + "," + (krajVremenaFormula - pocetakVremenaFormula) / 1000. + "," + (krajMinty - pocetakMinty) / 1000. + "," + -1000);
                        } else {
                            if (algoritmi == 2) {
                                writer.println(brojRedaka + "," + determinanta + "," + brojacBridova + "," + (krajVremenaFormula - pocetakVremenaFormula) / 1000. + "," + -1000 + "," + (krajAlgoritma3 - pocetakAlgoritma3) / 1000.);
                            } else {
                                if (algoritmi == 3) {
                                    writer.println(matrica.length + "," + determinanta + "," + brojacBridova + "," + (krajVremenaFormula - pocetakVremenaFormula) / 1000. + "," + (krajMinty - pocetakMinty) / 1000. + "," + (krajAlgoritma3 - pocetakAlgoritma3) / 1000.);
                                }
                            }
                        }
                    }
                } else {
                    if (!greska)
                        writer.println(-1 + "," + -1 + "," + -1 + "," + -1000 + "," + -1000 + "," + -1000);
                }
                writer.close();
                */


                randomIliPutanja.close();
            }
        }
    }

    public static int xor(int a, int b) {
        if ((a == 0 && b == 0) || (a == 1 && b == 1)) {
            return 0;
        }
        return 1;
    }

    public static boolean DFS(int matrica[][], int brRedakaIStupaca) {

        boolean jesmoLiObisli[] = new boolean[brRedakaIStupaca];
        for (int i = 0; i < jesmoLiObisli.length; i++) {
            jesmoLiObisli[i] = false;
        }
        Stack<Integer> stack = new Stack<>();
        boolean jesuLiSveNule = true;
        stack.add(0);
        if (rekFjaZaDFS(stack, matrica, brRedakaIStupaca, jesmoLiObisli)) {
            for (int i = 0; i < jesmoLiObisli.length; i++) {
                if (jesmoLiObisli[i] == false) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public static boolean rekFjaZaDFS(Stack<Integer> stack, int matrica[][], int n, boolean jesmoObisli[]) {
        int naKojemSmoRetku = stack.pop();
        boolean JeliCijeliRedNula = true;
        for (int i = 0; i < n; i++) {
            if (matrica[naKojemSmoRetku][i] != 0) {
                JeliCijeliRedNula = false;
                if (jesmoObisli[i] == false) {
                    stack.add(i);
                }
            }
        }
        if (JeliCijeliRedNula) {
            return false;
        }
        jesmoObisli[naKojemSmoRetku] = true;
        if (!stack.isEmpty()) {
            rekFjaZaDFS(stack, matrica, n, jesmoObisli);
        }
        return true;
    }

    public static Long determinantaMatrice(int matrica[][], int brojReda) {
        Long det = (long) 0;
        if (brojReda == 1) {
            return (long) matrica[0][0];
        }
        if (brojReda == 2) {
            return (long) (matrica[0][0] * matrica[1][1] - matrica[0][1] * matrica[1][0]);
        }
        int[][] temp = new int[brojReda - 1][brojReda - 1];
        int predznak = 1;


        for (int i = 0; i < brojReda; i++) {
            minoraMatrice(matrica, temp, 0, i, brojReda);
            if (matrica[0][i] != 0)
                det += predznak * matrica[0][i] * determinantaMatrice(temp, brojReda - 1);
            predznak = -predznak;
        }


        return det;
    }

    public static void minoraMatrice(int[][] matrica, int[][] temp, int redPreskoci, int stupacPreskoci, int velicinaMatrice) {
        int red = 0;
        int stupac = 0;
        for (int i = 0; i < velicinaMatrice; i++) {
            for (int j = 0; j < velicinaMatrice; j++) {
                if (i != redPreskoci && j != stupacPreskoci) {
                    temp[red][stupac] = matrica[i][j];
                    stupac++;
                    if (stupac == velicinaMatrice - 1) {
                        stupac = 0;
                        red++;
                    }
                }
            }
        }
    }

    public static void rekurzivnaFja(int[][] NovaMIncidencije,
                                     List<Integer> rjesenja,
                                     int brRed,
                                     int brojSt,
                                     Matrica mat,
                                     int razina) throws FileNotFoundException {
        lijevoRekurzivno(NovaMIncidencije, rjesenja, brRed, brojSt, mat, razina);
        rjesenja.clear();
        desnoRekurzivno(NovaMIncidencije, rjesenja, brRed, brojSt, mat, razina);


    }

    public static void lijevoRekurzivno(int[][] NovaMIncidencije,
                                        List<Integer> rjesenja,
                                        int brRed,
                                        int brojSt,
                                        Matrica mat,
                                        int razina) throws FileNotFoundException {

        if (brojSt == 0) {
            return;
        }
        //uvjet za izlazak
        if (brRed == 2) {
            mat.setBrojRazapinjucihStabala(mat.getBrojRazapinjucihStabala() + 1);
            return;
        }
        if ((rjesenja.size() + brojSt) == (mat.getBrRedaka() - 2)) {
            mat.setBrojRazapinjucihStabala(mat.getBrojRazapinjucihStabala() + 1);
            return;
        }
        //
        int indexOnogKojiSeMjenja = 0;
        int IndexPrveJedinice = 0;
        boolean nasliZadnji = true;
        ;
        //idemo nac pojavu zadnje jedinice (odoZDO)


        for (int i = NovaMIncidencije.length - 1; i > 0; i--) { //provjeri jeli treba brRed - 1 ili samo brRed, i > 1 je zato sto je prvi red samo index brida
            if (NovaMIncidencije[0].length > 0) {
                if (NovaMIncidencije[i][0] == 1 && nasliZadnji) {
                    indexOnogKojiSeMjenja = i;
                    nasliZadnji = false;
                } else {
                    if (NovaMIncidencije[i][0] == 1 && !nasliZadnji) {
                        IndexPrveJedinice = i;
                    }
                }
            }
        }
        int[][] matIncidLijeva = new int[brRed - 1][brojSt - 1];
        int cntIzgubljeniStupac = 0;

        for (int i = 0; i < brRed; i++) {
            for (int j = 1; j < brojSt; j++) {
                if (i == IndexPrveJedinice) {
                    matIncidLijeva[i - cntIzgubljeniStupac][j - 1] = xor(NovaMIncidencije[i][j], NovaMIncidencije[indexOnogKojiSeMjenja][j]);
                } else {
                    if (i == indexOnogKojiSeMjenja) {
                        cntIzgubljeniStupac = 1;
                    } else {
                        matIncidLijeva[i - cntIzgubljeniStupac][j - 1] = NovaMIncidencije[i][j];
                    }
                }
            }
        }
        boolean jeliCijeliStupacNula = true;
        List<Integer> listaStupacaZaIzbacit = new LinkedList<>();
        for (int i = 0; i < matIncidLijeva[0].length; i++) {
            for (int j = 0; j < matIncidLijeva.length; j++) {
                if (matIncidLijeva[j][i] == 1) {
                    jeliCijeliStupacNula = false;
                }
            }
            if (jeliCijeliStupacNula) {
                listaStupacaZaIzbacit.add(i);
            }
            jeliCijeliStupacNula = true;
        }
        rjesenja.add(-NovaMIncidencije[0][0]);

        if (matIncidLijeva.length == 2) {
            mat.setBrojRazapinjucihStabala(mat.getBrojRazapinjucihStabala() + 1);
            return;

        }
        if ((rjesenja.size() + matIncidLijeva[0].length) == (mat.getBrRedaka() - 2)) {
            mat.setBrojRazapinjucihStabala(mat.getBrojRazapinjucihStabala() + 1);
            return;
        }

        int[][] novaMatrica;
        if (listaStupacaZaIzbacit.size() > 0) {
            novaMatrica = removeColumns(matIncidLijeva, listaStupacaZaIzbacit);
        } else {
            novaMatrica = matIncidLijeva;
        }

        int[][] daSeNeBrise = new int[novaMatrica.length][novaMatrica[0].length];
        daSeNeBrise = novaMatrica;
        if ((rjesenja.size() + novaMatrica[0].length) == (mat.getBrRedaka() - 2)) {
            mat.setBrojRazapinjucihStabala(mat.getBrojRazapinjucihStabala() + 1);
            return;
        }
        lijevoRekurzivno(novaMatrica, rjesenja, novaMatrica.length, novaMatrica[0].length, mat, ++razina);
        rjesenja.remove(rjesenja.size() - 1);
        --razina;
        desnoRekurzivno(daSeNeBrise, rjesenja, daSeNeBrise.length, daSeNeBrise[0].length, mat, ++razina);
        --razina;


    }


    public static void desnoRekurzivno(int[][] NovaMIncidencije,
                                       List<Integer> rjesenja,
                                       int brRed,
                                       int brojSt,
                                       Matrica mat,
                                       int razina) throws FileNotFoundException {
        if (brojSt == 0) {
            return;
        }
        // NEKI UVJET IZLASKA IZ PETLJE
        if ((rjesenja.size() + brojSt) == (mat.getBrRedaka() - 2)) {
            mat.setBrojRazapinjucihStabala(mat.getBrojRazapinjucihStabala() + 1);
            return;
        }
        //

        int[][] matIncidDesna = new int[brRed][brojSt - 1];
        for (int i = 0; i < brRed; i++) {
            for (int j = 1; j < brojSt; j++) {
                if (NovaMIncidencije[i][j] != 0) {
                    matIncidDesna[i][j - 1] = NovaMIncidencije[i][j];
                }

            }
        }
        // msm da ode trebam napisati da provjeri DFS i onda mogu izbrisati gornjih par redaka(nisam jos siguran jel mogu izbrisati)
        int[][] susjedstvoZaProvjeruDFS = pretvoriMatIncUSusjedstvo(matIncidDesna);
        if (!DFS(susjedstvoZaProvjeruDFS, susjedstvoZaProvjeruDFS.length)) {
            return;
        }

        if ((rjesenja.size() + matIncidDesna[0].length) == (mat.getBrRedaka() - 2)) {
            mat.setBrojRazapinjucihStabala(mat.getBrojRazapinjucihStabala() + 1);

            return;
        }
        lijevoRekurzivno(matIncidDesna, rjesenja, brRed, brojSt - 1, mat, ++razina);
        --razina;
        if (rjesenja.size() > 0) {
            rjesenja.remove(rjesenja.size() - 1);
        }
//        rjesenja.remove(rjesenja.size() - 1);
        desnoRekurzivno(matIncidDesna, rjesenja, brRed, brojSt - 1, mat, ++razina);
        --razina;


    }

    public static int[][] removeColumns(int[][] matrix, List<Integer> columnsToRemove) {
        int numRows = matrix.length;
        int numCols = matrix[0].length - columnsToRemove.size();
        int[][] newMatrix = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            int newCol = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (!columnsToRemove.contains(j)) {
                    // Copy elements except those in columns to be removed
                    newMatrix[i][newCol] = matrix[i][j];
                    newCol++;
                }
            }
        }
        return newMatrix;
    }

    public static int[][] pretvoriListuUMatricuInc(List<String> listaIterator, int brPotrebnihRedaka) {
        int[][] vrati = new int[brPotrebnihRedaka][listaIterator.size()];
        for (int i = 0; i < listaIterator.size(); i++) {
            String[] str = listaIterator.get(i).split("-");
            int prvi = Integer.parseInt(str[0]);
            int drugi = Integer.parseInt(str[1]);
            vrati[prvi][i] = 1;
            vrati[drugi][i] = 1;
        }
        return vrati;
    }

    public static int[][] pretvoriMatIncUSusjedstvo(int[][] matInc) {


        int[][] vratiSusjeda = new int[matInc.length - 1][matInc.length - 1]; // nisan sig al msm da je -1 jer mi je prvi redak index stupca

        for (int i = 0; i < matInc[0].length; i++) {
            int prvi = -1;
            int drugi = -1;

            for (int j = 0; j < matInc.length; j++) {
                if (matInc[j][i] == 1 && prvi == -1) {
                    prvi = j - 1;
                } else {
                    if (matInc[j][i] == 1 && prvi != -1) {
                        drugi = j - 1;
                    }
                }
            }
            if (prvi != -1 && drugi != -1) {
                vratiSusjeda[prvi][drugi] = 1;
                vratiSusjeda[drugi][prvi] = 1;
            }
        }
        return vratiSusjeda;
    }

    public static int[][] pretvoriListuUMatricuSusj(List<String> listaIterator, int brPotrebnihRedaka) {
        int[][] vrati = new int[brPotrebnihRedaka][brPotrebnihRedaka];
        for (int i = 0; i < listaIterator.size(); i++) {
            String[] str = listaIterator.get(i).split("-");
            int prvi = Integer.parseInt(str[0]);
            int drugi = Integer.parseInt(str[1]);
            vrati[prvi][drugi] = 1;
            vrati[drugi][prvi] = 1;
        }
        return vrati;
    }

    public static int parseIntOrDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;


        }
    }
}