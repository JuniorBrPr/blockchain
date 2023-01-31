package blockchain.blockchain;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a blockchain.
 */
public class Blockchain implements Serializable {
    private Map<Integer, Block> blocks;
    private volatile int zerosHash;
    private volatile Message[] data;
    private final String keyPath = System.getProperty("user.dir") + "blockchain\\data\\keypair.ser";
    private final String dataPath = System.getProperty("user.dir") + "blockchain\\data\\chain.ser";

    /**
     * Creates a new blockchain.
     */
    public Blockchain() throws IOException, ClassNotFoundException {
        if (BlockchainUtil.deserialize(dataPath) != null) {
            Blockchain b = (Blockchain) BlockchainUtil.deserialize(dataPath);
            assert b != null;
            this.blocks = b.getBlocks();
            this.zerosHash = b.getZerosHash();
            this.data = b.getData();
        } else {
            this.blocks = new ConcurrentHashMap<>();
            this.zerosHash = 5;
            this.data = new Message[0];
            addBlock(new Block(1, "0", zerosHash, 99));
        }
    }

    /**
     * Add a new block and adds it to the blockchain.
     */
    public synchronized boolean addBlock(Block block) {
        System.out.println(block.getGenDuration());
        System.out.println(zerosHash);
        if (isValid(block)) {
            if (blocks.put(block.getId(), block) != null) {
                System.out.println("Miner " + block.getMinerId() + " found block " + block.getId() +
                        " with hash " + block.getHash());
                System.out.println(block.getGenDuration());
                if (block.getGenDuration() < 30) {
                    zerosHash++;
                } else if (block.getGenDuration() > 60) {
                    zerosHash--;
                }
                if (blocks.size() == 15) {
                    try {
                        BlockchainUtil.serialize(this, dataPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                data = new Message[]{};
                return true;
            }
        }
        return false;
    }

    /**
     * @return the blocks of the blockchain.
     */
    public Map<Integer, Block> getBlocks() {
        return blocks;
    }

    public boolean isValid(Block block) {
        String hash = BlockchainUtil.applySha256(
                (blocks.size() + 1) +
                        Long.toString(block.getTimestamp()) +
                        (blocks.isEmpty() ? "0" : blocks.get(blocks.size()).getHash()) +
                        block.getMagicNumber() +
                        block.getMinerId() +
                        Arrays.toString(data));
        return block.getHash().equals(hash) &&
                block.getHash().matches("^0{" + zerosHash + "}[a-zA-z1-9][a-zA-z0-9]*$");

    }

    /**
     * @return the last block of the blockchain.
     */
    public Block getLastBlock() {
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.get(blocks.size());
    }

    /**
     * @return the number of zeros the hash must start with.
     */
    public int getZerosHash() {
        return zerosHash;
    }

    public synchronized void addData(String sender, String receiver, String message) throws Exception {
        Message[] newData = new Message[data.length + 1];
        System.arraycopy(data, 0, newData, 0, data.length);
        Message m = new Message(sender + receiver + message, keyPath);
        newData[data.length] = m;
        data = newData;
    }

    public Message[] getData() {
        return data;
    }

    public String getDataPath() {
        return dataPath;
    }

    private void serializeBlockchain() {
        try {
            BlockchainUtil.serialize(this, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject();
        oos.writeObject(blocks);
        oos.writeObject(data);
        oos.writeObject(zerosHash);
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= blocks.size() + 1; i++) {
            sb.append(blocks.get(i)).append("\n");
        }

        return sb.toString();
    }

    public void setBlocks(Map<Integer, Block> blocks) {
        this.blocks = blocks;
    }
}



