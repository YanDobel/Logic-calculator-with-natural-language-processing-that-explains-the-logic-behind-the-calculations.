package Trees.SegmentTree;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        for (int i = 0; i < t; i++) {
            int N= sc.nextInt();
            int Q = sc.nextInt();

            int p2n = 1;
            while (p2n < N) {
                p2n *= 2;
            }

            int array[] = new int[p2n];
            for (int j = 0; j < N; j++) {
                array[j] = sc.nextInt();
            }
            SegmentTree seg = new SegmentTree(array, p2n);

            int res = seg.getRoot();
            System.out.println(res + " " + res(res, i));

            for (int k = 0; k < Q; k++) {
                int pos = sc.nextInt();
                int novo = sc.nextInt();
                seg.update(pos - 1, novo);
                res = seg.getRoot();
                System.out.println(res + " " + res(res, i));
            }
        }
        sc.close();
    }
    public static String res(int resultado, int t) {
        boolean isOdd = (resultado % 2 != 0);
        String p1 = (t % 2 == 0) ? "Rusa" : "Sanches";
        String p2 = (p1.equals("Rusa")) ? "Sanches" : "Rusa";

        return isOdd ? p1 : p2;
    }
}
