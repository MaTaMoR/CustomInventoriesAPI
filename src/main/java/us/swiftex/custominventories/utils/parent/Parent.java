package us.swiftex.custominventories.utils.parent;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public abstract class Parent<T> {

    private final T object;

    public Parent(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }

    public abstract File getDataFolder();

    public abstract InputStream getResource(String name);

    public abstract void saveResource(String name, boolean value);

    public abstract Logger getLogger();
}
