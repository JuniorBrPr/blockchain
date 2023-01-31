package blockchain;

import blockchain.blockchain.Block;
import blockchain.blockchain.Blockchain;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static int THREADS = 8;
    static ExecutorService EXECUTORSERVICE = Executors.newFixedThreadPool(THREADS);

    public static void main(String[] args) {
        try {
            Blockchain blockchain = new Blockchain();

            for (int i = 0; i < THREADS; i++) {
                EXECUTORSERVICE.submit(new Miner(i, blockchain));
            }

            EXECUTORSERVICE.shutdown();

            while (!EXECUTORSERVICE.isTerminated()) {
                Thread.sleep(500);
            }

            if (EXECUTORSERVICE.isTerminated()) {
                for (int i = 1; i < blockchain.getBlocks().size() + 1; i++) {
                    Block block = blockchain.getBlocks().get(i);
                    System.out.println(block);
                    if (block.getGenDuration() < 30) {
                        System.out.println("N was increased to " + (block.getZerosHash() + 1) + "\n");
                    } else if (block.getGenDuration() > 45) {
                        System.out.println("N was decreased by 1\n");
                    } else {
                        System.out.println("N stays the same\n");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
