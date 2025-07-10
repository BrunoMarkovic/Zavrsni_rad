import java.util.List;
import java.util.Random;

public class probavamo {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        int brojRedaka = 7;
        int[][] matrica = new int[brojRedaka][brojRedaka];
        double sansa = 0.5;
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
        for(int i = 0; i < brojRedaka; i++){
            for(int j = 0; j < brojRedaka; j++){
                System.out.print(matrica[i][j] + " ");
            }
            System.out.println();
        }
        int[][] mat = new int[2][6];
        System.out.println(mat.length);
        System.out.println(mat[0].length);
        System.out.println();
        int[][] druga = new int[10][2];
        System.out.println(druga.length);
        System.out.println(druga[0].length);
        List<String> lista = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        lista.size();


    }
}
