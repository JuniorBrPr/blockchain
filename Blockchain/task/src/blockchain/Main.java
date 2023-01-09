package blockchain;

import blockchain.Blockchain.Block;
import blockchain.Blockchain.Blockchain;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();

        for (int i = 0; i < 4; i++) {
            blockchain.generateBlock();
        }

        for (Block block : blockchain.getBlocks()) {
            System.out.println(block.toString());
        }
    }
}
