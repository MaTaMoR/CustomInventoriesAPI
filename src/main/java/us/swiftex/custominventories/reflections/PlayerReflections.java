package us.swiftex.custominventories.reflections;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.FieldAccessor;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;

public class PlayerReflections {

    private static final Class<?> CRAFT_PLAYER_CLASS = Reflections.getClass("{obc}.entity.CraftPlayer");
    private static final Class<?> ENTITY_PLAYER_CLASS = Reflections.getClass("{nms}.EntityPlayer");

    private static final MethodInvoker HANDLE_CRAFT_PLAYER_METHOD = Reflections.getMethod(CRAFT_PLAYER_CLASS, "getHandle");
    private static final FieldAccessor<Integer> PLAYER_PING_FIELD = Reflections.getField(ENTITY_PLAYER_CLASS, "ping", int.class);

    private static Object getCraftPlayer(Player player) {
        return HANDLE_CRAFT_PLAYER_METHOD.invoke(player);
    }

    public static int getPing(Player player) {
        return PLAYER_PING_FIELD.get(getCraftPlayer(player));
    }
}