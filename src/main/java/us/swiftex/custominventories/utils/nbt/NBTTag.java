package us.swiftex.custominventories.utils.nbt;

public abstract class NBTTag<T> {

    protected final NBTType type;
    protected T value;

    protected NBTTag(NBTType type, T value) {
        this.type = type;
        this.value = value;
    }

    public NBTType<T, NBTTag<T>> getType() {
        return type;
    }

    public Object toNBT() {
        return getType().toNBT(value);
    }

    public T getValue() {
        return value;
    }

    private void setValue(T value) {
        this.value = value;
    }
}
