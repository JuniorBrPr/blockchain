package blockchain.Blockchain;

/**
 * Represents a blockchain.
 */
public class Blockchain {
    private Block[] blocks;

    /**
     * Creates a new blockchain.
     */
    public Blockchain() {
        blocks = new Block[1];
        blocks[0] = new Block(1, System.currentTimeMillis(), "0");
    }

    /**
     * Generates a new block and adds it to the blockchain.
     */
    public void generateBlock(){
        Block[] newBlocks = new Block[blocks.length + 1];
        System.arraycopy(this.blocks, 0, newBlocks, 0, blocks.length);
        newBlocks[this.blocks.length] = new Block(
                (blocks.length + 1),
                System.currentTimeMillis(),
                blocks[blocks.length - 1].getHash()
        );
        this.blocks = newBlocks;
    }

    /**
     * @return the blocks of the blockchain.
     */
    public Block[] getBlocks() {
        return blocks;
    }
}
