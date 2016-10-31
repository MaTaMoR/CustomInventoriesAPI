package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.FieldAccessor;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

import java.util.ArrayList;
import java.util.List;

public class NBTTagList extends NBTTag<List<NBTTag>> {

    private static final FieldAccessor<List> GETTER = Reflections.getField("{nms}.NBTTagList", "list", List.class);

    public NBTTagList(List<NBTTag> value) {
        super(NBTType.LIST, value);
    }

    public NBTTagList(Object object) throws RuntimeException {
        super(NBTType.LIST, new ArrayList<NBTTag>());

        List<Object> list = (List<Object>) GETTER.get(object);
        for(Object value : list) {
            NBTType type = NBTType.byNBT(value);

            if(type == null) continue;

            add(type.fromNBT(value));
        }
    }

    public void add(NBTTag tag) {
        value.add(tag);
    }

    public void remove(NBTTag tag) {
        value.remove(tag);
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}
