package blockchain.Blockchain.Blockchain;

public class Message {
    private final String message;
    private final int minerId;

    public Message(String message, int minerId) {
        this.message = message;
        this.minerId = minerId;
    }

    public String getMessage() {
        return message;
    }

    public int getMinerId() {
        return minerId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", minerId=" + minerId +
                '}';
    }
}

