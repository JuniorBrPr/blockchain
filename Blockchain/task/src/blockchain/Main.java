package blockchain;

import blockchain.Blockchain.Block;
import blockchain.Blockchain.Blockchain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutorService;

public class Main {
    static int threads = 16;
    static ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(threads);

    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, NoSuchProviderException, IOException, ClassNotFoundException {
        Blockchain blockchain = new Blockchain();

        for (int i = 0; i < threads; i++) {
            executorService.submit(new Miner(i, blockchain));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(500);
        }
        if (executorService.isTerminated()) {
            for (Block block : blockchain.getBlocks()) {
                System.out.print(block);
                if (block.getGenDuration() < 30) {
                    System.out.println("N was increased to " + (block.getZerosHash() + 1) + "\n");
                } else if (block.getGenDuration() > 45) {
                    System.out.println("N was decreased by 1\n");
                } else {
                    System.out.println("N stays the same\n");
                }
            }
        }
    }
}
