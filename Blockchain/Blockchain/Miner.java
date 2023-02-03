package blockchain.Blockchain;

import blockchain.Blockchain.Blockchain.Block;
import blockchain.Blockchain.Blockchain.Blockchain;

public class Miner implements Runnable {
    private final int id;
    private final Blockchain blockchain;

    public Miner(int id, Blockchain blockchain) {
        this.id = id;
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        while (blockchain.getBlocks().size() < 15) {
            if (blockchain.getBlocks().size() == 15) {
                break;
            }
            if (blockchain.getLastBlock() != null) {
                Block lastBlock = blockchain.getLastBlock();
                Block block = new Block(
                        lastBlock.getId() + 1,
                        lastBlock.getHash(),
                        blockchain.getZerosHash(),
                        id);
                if (blockchain.getLastBlock() == lastBlock && blockchain.getBlocks().size() < 15) {
                    blockchain.addBlock(block);
                }
            }
        }
    }
}

