package blockchain.Blockchain;


/**
 * Represents a block of a blockchain.
 */
public class Block {
    private final int id;
    private final long timestamp;
    private final String previousHash;
    private final String hash;

    /**
     * @param id the id of the block.
     * @param timestamp the timestamp at which the block was created.
     * @param previousHash the hash-value of the previous block.
     */
    public Block(int id, long timestamp, String previousHash) {
        this.id = id;
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    /**
     * Generates a hash for the block.
     *
     * @return the hash of the block.
     */
    public String calculateHash() {
        return StringUtil.applySha256(id + previousHash + timestamp);
    }

    /**
     * @return the hash of the block.
     */
    public String getHash() {
        return hash;
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
                Hash of the previous block:
                %s
                Hash of the block:
                %s
                """.formatted(this.id, this.timestamp, this.previousHash, this.hash);
    }
}
