package blockchain.Blockchain;

import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a blockchain.
 */
public class Blockchain implements Serializable {
    private transient volatile int length = 0;
    private volatile Block[] blocks;
    private volatile int zerosHash;
    private volatile Message[] data;
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
            this.blocks = new Block[0];
            this.zerosHash = 0;
            this.data = new Message[0];
            addBlock(new Block(1, "0", zerosHash, 99));
//        }
    }

    /**
     * Add a new block and adds it to the blockchain.
     */
    public synchronized void addBlock(Block block) {
//        System.out.println("Miner " + block.getMinerId() + " found block " + block.getId() + " with hash " + block.getHash());
        if (block.getHash().matches("^0{" + zerosHash + "}[a-zA-z1-9][a-zA-z0-9]*$") && isValid()) {
            synchronized (this) {
                Block[] newBlocks = new Block[blocks.length + 1];
                System.arraycopy(blocks, 0, newBlocks, 0, blocks.length);
                newBlocks[blocks.length] = block;
                if (isValid()) {
                    blocks = newBlocks;
                    blocks[blocks.length - 1].setData(data);
                    if (blocks.length == 15) {
                        try {
                            SerializationUtil.serialize(this, dataPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    data = new Message[]{};
                    if (block.getGenDuration() < 30){
                        ++this.zerosHash;
                    } else if (block.getGenDuration() > 45){
                        --this.zerosHash;
                    }
                }
            }
        }
    }

    /**
     * @return the blocks of the blockchain.
     */
    public Block[] getBlocks() {
        return blocks;
    }

    public synchronized boolean isValid() {
        for (int i = 1; i < length; i++) {
            if (!blocks[i].getPreviousHash().equals(blocks[i - 1].getHash())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the last block of the blockchain.
     */
    public synchronized Block getLastBlock() {
        if (blocks.length == 0) {
            return null;
        }
        return blocks[blocks.length - 1];
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
            SerializationUtil.serialize(this, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Serial
//    private void writeObject(ObjectOutputStream oos) throws Exception {
//        oos.defaultWriteObject();
//        oos.writeObject(blocks);
//        oos.writeObject(data);
//        oos.writeObject(zerosHash);
//    }
//
//    @Serial
//    private void readObject(ObjectInputStream ois) throws Exception {
//        ois.defaultReadObject();
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Block block : blocks) {
            sb.append(block).append("\n");
        }
        return sb.toString();
    }
}
