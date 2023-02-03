package blockchain.Blockchain;

import blockchain.Blockchain.Blockchain.Blockchain;

import java.util.concurrent.ExecutorService;


public class Main {
    static int THREADS = 4;
    static ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(THREADS);

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();

        for (int i = 0; i < THREADS; i++) {
            executorService.submit(new Miner(i, blockchain));
        }
        executorService.shutdown();

        int seconds = 0;
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(100);
                seconds++;
                if (seconds == 60) {
                    executorService.shutdownNow();
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("\n Blockchain :\n"+blockchain);
    }
}
