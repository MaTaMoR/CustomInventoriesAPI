package me.matamor.custominventories.reflections;

import me.matamor.custominventories.utils.Reflections;
import org.bukkit.entity.Player;
import me.matamor.custominventories.utils.Validate;

public class PlayerReflections {

    private static final Class<?> CRAFT_PLAYER_CLASS = Reflections.getClass("{obc}.entity.CraftPlayer");
    private static final Class<?> ENTITY_PLAYER_CLASS = Reflections.getClass("{nms}.EntityPlayer");

    private static final Reflections.MethodInvoker HANDLE_CRAFT_PLAYER_METHOD = Reflections.getMethod(CRAFT_PLAYER_CLASS, "getHandle");
    private static final Reflections.FieldAccessor<Integer> PLAYER_PING_FIELD = Reflections.getField(ENTITY_PLAYER_CLASS, "ping", int.class);

    private static Object getCraftPlayer(Player player) {
        Validate.notNull(player, "Player can't be null!");

        return HANDLE_CRAFT_PLAYER_METHOD.invoke(player);
    }

    public static int getPing(Player player) {
        Validate.notNull(player, "Player can't be null!");

        return PLAYER_PING_FIELD.get(getCraftPlayer(player));
    }
}