
import java.io.File;

public class Matrica {
    private int brRedaka;
    private int brojStupaca;
    private int[][] Matrica;
    private Long brojRazapinjucihStabala = (long)0;

    public Long getBrojRazapinjucihStabala() {
        return brojRazapinjucihStabala;
    }
    public void setBrojRazapinjucihStabala(Long brojRazapinjucihStabala) {
        this.brojRazapinjucihStabala = brojRazapinjucihStabala;
    }
    public Matrica(int brRedaka, int brojStupaca, int[][] matrica) {
        super();
        this.brRedaka = brRedaka;
        this.brojStupaca = brojStupaca;
        Matrica = matrica;
    }
    public int getBrRedaka() {
        return brRedaka;
    }
    public void setBrRedaka(int brRedaka) {
        this.brRedaka = brRedaka;
    }
    public int getBrojStupaca() {
        return brojStupaca;
    }
    public void setBrojStupaca(int brojStupaca) {
        this.brojStupaca = brojStupaca;
    }
    public int[][] getMatrica() {
        return Matrica;
    }
    public void setMatrica(int[][] matrica) {
        Matrica = matrica;
    }

}
