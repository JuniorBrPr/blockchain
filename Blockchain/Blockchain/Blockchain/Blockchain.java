package blockchain.Blockchain.Blockchain;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a blockchain.
 */
public class Blockchain implements Serializable {
    private final ConcurrentHashMap<Integer, Block> blocks;
    //    private volatile Block[] blocks;
    private volatile int zerosHash;
    private final ConcurrentHashMap<Integer, Message> data;
    private final String keyPath = "C:\\Users\\junio\\OneDrive\\Bureaublad\\OOP1\\Blockchain\\Blockchain\\task\\src" +
            "\\blockchain\\data\\privateKey";
    private final String dataPath = "C:\\Users\\junio\\OneDrive\\Bureaublad\\OOP1\\Blockchain\\Blockchain\\task\\src" +
            "\\blockchain\\data\\chaindata.ser";

    /**
     * Creates a new blockchain.
     */
    public Blockchain() throws IOException, ClassNotFoundException {
//        if (SerializationUtil.deserialize(dataPath) != null) {
//            Blockchain b = (Blockchain) SerializationUtil.deserialize(dataPath);
//            this.blocks = b.getBlocks();
//            this.zerosHash = b.getZerosHash();
//            this.data = b.getData();
//        } else {
        this.blocks = new ConcurrentHashMap<>();
//            this.blocks = new Block[0];
        this.zerosHash = 0;
        this.data = new ConcurrentHashMap<>();
        addBlock(new Block(1, "0", zerosHash, 99));
//        }
    }

    /**
     * Add a new block and adds it to the blockchain.
     */
    public synchronized void addBlock(Block block) {
//        System.out.println("Miner " + block.getMinerId() + " found block " + block.getId() + " with hash " + block.getHash());
//            synchronized (this) {
//                Block[] newBlocks = new Block[blocks.length + 1];
//                System.arraycopy(blocks, 0, newBlocks, 0, blocks.length);
//                newBlocks[blocks.length] = block;
        System.out.println("Miner " + block.getMinerId() + " found block " + block.getId() + " with hash " + block.getHash());
        if (isValid(block)) {
//                blocks = newBlocks;
//                blocks[blocks.length - 1].setData(data);
            block.setData(data.values().toArray(new Message[0]));
            blocks.put(block.getId(), block);
            data.clear();
            addData("Block " + block.getId() + " is added to the blockchain", block.getMinerId());
            if (blocks.size() == 15) {
                try {
                    SerializationUtil.serialize(this, dataPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (block.getGenDuration() < 30) {
                ++this.zerosHash;
            } else if (block.getGenDuration() > 45) {
                --this.zerosHash;
            }
        }
//            }

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

    public synchronized boolean isValid(Block block) {
        String tempHash = BlockchainUtil.applySha256(
                (blocks.size() + 1) +
                        block.getTimestamp() +
                        (blocks.size() > 0 ? blocks.get(blocks.size()).getHash() : "0") +
                        block.getMagicNumber() +
                        block.getMinerId()
        );
        System.out.println("Temp hash: " + tempHash);
        System.out.println("Block " + block.getId() + " is valid: " + tempHash.matches("^0{" + zerosHash + "}[a-zA-z1-9][a-zA-z0-9]*$"));
        return tempHash.matches("^0{" + zerosHash + "}[a-zA-z1-9][a-zA-z0-9]*$") || blocks.size() == 0;
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

    public HashMap<Integer, Message> getData() {
        return data.values()
                .stream()
                .collect(HashMap::new, (m, v) -> m.put(m.size(), v), HashMap::putAll);
    }

    public String getDataPath() {
        return dataPath;
    }

    private void serializeBlockchain() {
        try {
            SerializationUtil.serialize(this, dataPath);
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
        sb.append(blocks.values().stream().sorted().toString());
//        for (Block block : blocks) sb.append(block).append("\n");
        return sb.toString();
    }
}
