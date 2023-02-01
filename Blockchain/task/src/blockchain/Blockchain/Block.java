package blockchain.Blockchain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Represents a block of a blockchain.
 */
public class Block implements Serializable{
    private final int id;
    private final long timestamp;
    private final String previousHash;
    private final String hash;
    private int magicNumber;
    private long genDuration;
    private final Pattern PATTERN;
    private final int minerId;
    private final int zerosHash;
    private Message[] data;


    /**
     * @param id           the id of the block.
     * @param previousHash the hash-value of the previous block.
     * @param minerId
     */
    public Block(int id, String previousHash, int zerosHash, int minerId) {
        this.id = id;
        this.minerId = minerId;
        this.timestamp = setTimestamp();
        this.previousHash = previousHash;
        this.zerosHash = zerosHash;
        this.PATTERN = Pattern.compile("^0{" + zerosHash + "}[a-zA-z1-9][a-zA-z0-9]*$");
        this.hash = setHash();
    }

    /**
     * Generates a hash for the block.
     *
     * @return the hash of the block.
     */
    public String setHash() {
        String hash;
        long startTime = System.currentTimeMillis();
        do {
            magicNumber = generateMagicNumber();
            hash = BlockChainUtil.applySha256(
                    id + timestamp + previousHash + magicNumber + minerId + Arrays.toString(data)
            );
            if (zerosHash == 0) {
                break;
            }
        } while (!hash.matches(PATTERN.pattern()));
        this.genDuration = System.currentTimeMillis() - startTime;
        return hash;
    }

    /**
     * @return the id of the block.
     */
    public int getId() {
        return id;
    }

    /**
     * @return the hash of the block.
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the hash of the previous block.
     */
    public String getPreviousHash() {
        return previousHash;
    }

    protected void setData(Message[] data) {
        this.data = data;
    }

    /**
     * @return the minerId of the block.
     */
    public int getMinerId() {
        return minerId;
    }

    /**
     * @return the duration of the block generation.
     */
    public long getGenDuration() {
        return genDuration;
    }

    /**
     * Sets the magic number of the block.
     *
     * @return the magic number of the block.
     */
    private int generateMagicNumber() {
        int MAGIC_NUMBER_LENGTH = 100000000;
        return (int) (Math.random() * MAGIC_NUMBER_LENGTH);
    }

    private Long setTimestamp() {
        return System.currentTimeMillis();
    }

    public int getZerosHash() {
        return zerosHash;
    }

    /**
     * @return a string representation of the block
     */
    @Override
    public String toString() {
        StringBuilder blockData = new StringBuilder();
        if (data != null && data.length > 0) {
            for (Message m : data) {
                blockData.append("\n").append(m.toString());
            }
        } else {
            blockData.append("\nNo transactions");
        }
        return """
                Block:
                Created by miner%d
                miner%d gets 100 VC
                Id: %d
                Timestamp: %d
                Magic number: %d
                Hash of the previous block:
                %s
                Hash of the block:
                %s
                Block data: %s
                Block was generating for %d seconds
                """
                .formatted(
                        this.minerId,
                        this.minerId,
                        this.id,
                        this.timestamp,
                        this.magicNumber,
                        this.previousHash,
                        this.hash,
                        blockData.toString(),
                        this.genDuration
                );
    }
}
