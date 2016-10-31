package us.swiftex.custominventories.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.swiftex.custominventories.CustomInventories;
import us.swiftex.custominventories.reflections.PlayerReflections;

import java.text.DecimalFormat;
import java.util.*;

public abstract class Variable implements LocalVariable {

    private static Map<Plugin, Set<Variable>> variables = new HashMap<>();

    public static final Variable NAME = register(CustomInventories.getPlugin(), new Variable("{name}") {

        @Override
        public String getReplacement(Player player) {
            return player.getName();
        }

    });

    public static final Variable ONLINE_PLAYERS = register(CustomInventories.getPlugin(), new Variable("{online_players}", false) {

        @Override
        public String getReplacement(Player player) {
            return String.valueOf(Players.getOnlinePlayers());
        }

    });

    public static final Variable MAX_HEALTH = register(CustomInventories.getPlugin(), new Variable("{max_health}") {

        @Override
        public String getReplacement(Player player) {
            return format(player.getMaxHealth());
        }

    });

    public static final Variable HEALTH = register(CustomInventories.getPlugin(), new Variable("{health") {

        @Override
        public String getReplacement(Player player) {
            return format(player.getHealth());
        }

    });

    public static final Variable LEVEL = register(CustomInventories.getPlugin(), new Variable("{level}") {

        @Override
        public String getReplacement(Player player) {
            return String.valueOf(player.getLevel());
        }

    });

    public static final Variable LOCATION = register(CustomInventories.getPlugin(), new Variable("{location}") {

        @Override
        public String getReplacement(Player player) {
            Location location = player.getLocation();
            return "XYZ: " + format(location.getX()) + ", " + format(location.getY()) + ", " + format(location.getZ());
        }

    });

    public static final Variable PING = register(CustomInventories.getPlugin(), new Variable("{ping}") {
        @Override
        public String getReplacement(Player player) {
            return String.valueOf(PlayerReflections.getPing(player));
        }
    });

    private final String text;
    private final boolean needPlayer;

    public Variable(String text){
        this(text, true);
    }

    public Variable(String text, boolean needPlayer) {
        Validate.notNull(text, "VariableName can't be null");

        this.text = text;
        this.needPlayer = needPlayer;
    }

    @Override
    public String getText() {
        return text;
    }

    public boolean needPlayer() {
        return needPlayer;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof Variable && text.equals(((Variable) object).getText());
    }

    public static Variable register(Plugin plugin, Variable variable) {
        Validate.notNull(variable, "Variable can't be null");

        Set<Variable> pluginVariables = variables.get(plugin);

        if(pluginVariables == null) {
            pluginVariables = new HashSet<>();
            variables.put(plugin, pluginVariables);
        }

        pluginVariables.add(variable);
        return variable;
    }

    public static void unregister(Plugin plugin, Variable variable) {
        Validate.notNull(variable, "Variable can't be null");

        Set<Variable> pluginVariables = variables.get(plugin);

        if(pluginVariables == null) {
            return;
        }

        pluginVariables.remove(variable);
    }

    public static Set<Variable> getPluginVariables(Plugin plugin) {
        Set<Variable> pluginVariables = variables.get(plugin);
        return (pluginVariables == null ? null : Collections.unmodifiableSet(pluginVariables));
    }

    public static String replace(String text) {
        Validate.notNull(text, "Text can't be null");

        for(Variable variable : values()) {
            if(variable.needPlayer()) {
                continue;
            }

            text = text.replace(variable.getText(), variable.getReplacement(null));
        }

        return text;
    }

    public static String replace(String text, Player player) {
        Validate.notNull(text, "Text can't be null");

        for(Variable variable : values()) {
            text = text.replace(variable.getText(), variable.getReplacement(player));
        }

        return text;
    }

    public static Variable[] values() {
        Set<Variable> values = new HashSet<>();

        for(Set<Variable> variable : variables.values()) {
            values.addAll(variable);
        }

        return values.toArray(new Variable[values.size()]);
    }

    private static final DecimalFormat format = new DecimalFormat("##");

    private static String format(double value) {
        return format.format(value);
    }
}
