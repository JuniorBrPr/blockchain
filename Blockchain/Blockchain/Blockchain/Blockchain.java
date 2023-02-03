package blockchain.Blockchain.Blockchain;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a blockchain.
 */
public class Blockchain implements Serializable {
    private final ConcurrentHashMap<Integer, Block> blocks;
    private volatile int zerosHash;
    private final ConcurrentHashMap<Integer, Message> data;
    private final String dataPath;
    private final transient Object lock = new Object();
    private final transient Object lock1 = new Object();

    /**
     * Creates a new blockchain.
     */
    public Blockchain() {
        this.dataPath = System.getProperty("user.dir") + "\\Blockchain\\data\\blockchain.ser";
        System.out.println(dataPath);
//        if (SerializationUtil.deserialize(dataPath) != null) {
//            Blockchain b = (Blockchain) SerializationUtil.deserialize(dataPath);
//            this.blocks = b.getBlocks();
//            this.zerosHash = b.getZerosHash();
//            this.data = b.getData();
//        } else {
        this.blocks = new ConcurrentHashMap<>();
        this.zerosHash = 0;
        this.data = new ConcurrentHashMap<>();
        addBlock(new Block(1, "0", zerosHash, 99));
    }

    /**
     * Add a new block and adds it to the blockchain.
     */
    public void addBlock(Block block) {
        if (isValid(block)) {
            block.setData(data.values().toArray(new Message[0]));
            blocks.put(block.getId(), block);
            data.clear();
            addData("Block " + block.getId() + " has been added to the blockchain", block.getMinerId());
            if (blocks.size() == 15) {
                try {
                    SerializationUtil.serialize(this, dataPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            synchronized (this.lock) {
                if (block.getGenDuration() < 30) {
                    System.out.println("[Blockchain] N was increased to " + this.zerosHash + " + 1");
                    ++this.zerosHash;
                } else if (block.getGenDuration() > 45) {
                    System.out.println("[Blockchain] N was decreased by 1");
                    --this.zerosHash;
                } else {
                    System.out.println("[Blockchain] N stays the same");
                }
            }
        }

    }

    /**
     * @return the blocks of the blockchain.
     */
    public HashMap<Integer, Block> getBlocks() {
        return this.blocks
                .values()
                .stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll);
    }

    public boolean isValid(Block block) {
        synchronized (this.lock1){
            String tempHash = BlockchainUtil.applySha256(
                    (blocks.size() + 1) +
                            block.getTimestamp() +
                            (blocks.size() > 0 ? blocks.get(blocks.size()).getHash() : "0") +
                            block.getMagicNumber() +
                            block.getMinerId()
            );
            return tempHash.matches("^0{" + zerosHash + "}[a-zA-z1-9][a-zA-z0-9]*$");
        }
    }

    /**
     * @return the last block of the blockchain.
     */
    public synchronized Block getLastBlock() {
        return blocks.isEmpty() ? null : blocks.get(blocks.size());
    }

    /**
     * @return the number of zeros the hash must start with.
     */
    public int getZerosHash() {
        return zerosHash;
    }

    public synchronized void addData(String message, int minerId) {
        data.put(data.size() + 1, new Message(message, minerId));
    }

//    private void serializeBlockchain() {
//        try {
//            SerializationUtil.serialize(this, dataPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Block block : blocks.values()) {
            sb.append(block.toString()).append("\n");
        }
        return sb.toString();
    }
}
