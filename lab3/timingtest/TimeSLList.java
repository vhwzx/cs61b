package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        SLList<Integer> test = new SLList<>();
        int N = 1000;
        int M = 10000;
        int cur = 0;

        while(N <= 128000){
            Ns.addLast(N);
            while(cur < N){
                test.addLast(1);
                cur += 1;
            }
            double start_time = System.currentTimeMillis();
            for(int i = 0; i < M; i += 1)
                test.getLast();
            double t = System.currentTimeMillis() - start_time;
            times.addLast(t/1000);
            opCounts.addLast(M);
            N *= 2;

        }
        printTimingTable(Ns, times, opCounts);
    }

}
