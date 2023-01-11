package blockchain.Blockchain;

import java.util.regex.Pattern;

/**
 * Represents a block of a blockchain.
 */
public class Block {
    private final int id;
    private final long timestamp;
    private final String previousHash;
    private final String hash;
    private int magicNumber;
    private long genDuration;
    private final Pattern PATTERN;


    /**
     * @param prevBlockId  the id of the block.
     * @param previousHash the hash-value of the previous block.
     */
    public Block(int prevBlockId, String previousHash, int zerosHash) {
        this.id = prevBlockId;
        this.timestamp = setTimestamp();
        this.previousHash = previousHash;
        PATTERN = Pattern.compile("^0{" + zerosHash + "}[a-zA-z1-9][a-zA-z0-9]*");
        this.hash = setHash();
    }

    /**
     * Generates a hash for the block.
     *
     * @return the hash of the block.
     */
    private String setHash() {
        String hash;
        long startTime = System.currentTimeMillis();
        do {
            magicNumber = generateMagicNumber();
            hash = StringUtil.applySha256(
                    id + timestamp + previousHash + magicNumber
            );
        } while (!hash.matches(PATTERN.pattern()));
        this.genDuration = System.currentTimeMillis() - startTime;
        return hash;
    }

    /**
     * @return the hash of the block.
     */
    public String getHash() {
        return hash;
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

    /**
     * @return a string representation of the block
     */
    @Override
    public String toString() {
        return """
                Block:
                Id: %d
                Timestamp: %d
                Magic number: %d
                Hash of the previous block:
                %s
                Hash of the block:
                %s
                Block was generating for %d seconds
                """
                .formatted(
                        this.id,
                        this.timestamp,
                        this.magicNumber,
                        this.previousHash,
                        this.hash,
                        this.genDuration
                );
    }
}
