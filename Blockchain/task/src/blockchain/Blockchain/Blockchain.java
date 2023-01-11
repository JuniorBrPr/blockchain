package blockchain.Blockchain;

import java.util.Scanner;

/**
 * Represents a blockchain.
 */
public class Blockchain {
    private Block[] blocks;

    /**
     * Creates a new blockchain.
     */
    public Blockchain() {
        System.out.println("Enter how many zeros the hash must start with: ");
        int zerosHash = new Scanner(System.in).nextInt();

        blocks = new Block[1];
        blocks[0] = new Block(1, "0", zerosHash);
    }



    /**
     * Generates a new block and adds it to the blockchain.
     */
    public void generateBlock(){
        Block[] newBlocks = new Block[blocks.length + 1];
        System.arraycopy(this.blocks, 0, newBlocks, 0, blocks.length);
        newBlocks[this.blocks.length] = new Block(
                blocks.length + 1,
                blocks[blocks.length - 1].getHash(),
                5
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
