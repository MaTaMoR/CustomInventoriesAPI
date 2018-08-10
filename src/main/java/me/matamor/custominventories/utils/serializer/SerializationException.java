package me.matamor.custominventories.utils.serializer;

public class SerializationException extends Exception {

    public SerializationException() {
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
