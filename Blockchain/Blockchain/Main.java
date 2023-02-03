package blockchain.Blockchain;

import blockchain.Blockchain.Blockchain.Block;
import blockchain.Blockchain.Blockchain.Blockchain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutorService;

import static java.lang.System.out;


public class Main {
    static int threads = 4;
    static ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(threads);

    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, NoSuchProviderException, IOException, ClassNotFoundException {
        Blockchain blockchain = new Blockchain();

        for (int i = 0; i < threads; i++) {
            executorService.submit(new Miner(i, blockchain));
        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//            Thread.sleep(500);
//        }
//        if (executorService.isTerminated()) {
//            blockchain.getBlocks().values().forEach((block) -> {
//                out.println(block);
//                if (block.getGenDuration() < 30) {
//                    out.println("N was increased to " + (block.getZerosHash() + 1) + "\n");
//                } else if (block.getGenDuration() > 45) {
//                    out.println("N was decreased by 1\n");
//                } else {
//                    out.println("N stays the same\n");
//                }
//            });
//            for (Block block : blockchain.getBlocks()) {
//                out.print(block);
//                if (block.getGenDuration() < 30) {
//                    out.println("N was increased to " + (block.getZerosHash() + 1) + "\n");
//                } else if (block.getGenDuration() > 45) {
//                    out.println("N was decreased by 1\n");
//                } else {
//                    out.println("N stays the same\n");
//                }
//            }
//        }
    }
}
