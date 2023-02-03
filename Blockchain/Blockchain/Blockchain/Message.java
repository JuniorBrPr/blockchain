package blockchain.Blockchain.Blockchain;

import java.io.Serializable;

public record Message(String message, int minerId) implements Serializable {

    @Override
    public String toString() {
        return "\tMessage {\n\t\tmessage=\n\t\t\t" + message +"\n\t\tminerId=\n\t\t\t" + minerId + "\n\t}";
    }
}

