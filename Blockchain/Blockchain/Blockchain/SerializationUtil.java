package blockchain.Blockchain.Blockchain;

import java.io.*;

public class SerializationUtil {
    /**
     * Serialize the given object to the file
     */
    public static void serialize(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
//        System.out.println("Serialized data is saved in " + fileName);
        oos.close();
    }

    /**
     * Deserialize to an object from the file
     */
    public static Object deserialize(String fileName){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            ois.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
//            System.out.println(e.getMessage());
            return null;
        }

    }
}