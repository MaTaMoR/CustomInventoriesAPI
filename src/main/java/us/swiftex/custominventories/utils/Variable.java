package us.swiftex.custominventories.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.swiftex.custominventories.reflections.PlayerReflections;

import java.text.DecimalFormat;
import java.util.*;

public abstract class Variable implements LocalVariable {

    private static final Set<Variable> variables = new HashSet<>();

    public static final Variable NAME = register(new Variable("{name}") {

        @Override
        public String getReplacement(Player player) {
            return player.getName();
        }

    });

    public static final Variable ONLINE_PLAYERS = register(new Variable("{online_players}", false) {

        @Override
        public String getReplacement(Player player) {
            return String.valueOf(Players.getOnlinePlayers());
        }

    });

    public static final Variable MAX_HEALTH = register(new Variable("{max_health}") {

        @Override
        public String getReplacement(Player player) {
            return format(player.getMaxHealth());
        }

    });

    public static final Variable HEALTH = register(new Variable("{health") {

        @Override
        public String getReplacement(Player player) {
            return format(player.getHealth());
        }

    });

    public static final Variable LEVEL = register(new Variable("{level}") {

        @Override
        public String getReplacement(Player player) {
            return String.valueOf(player.getLevel());
        }

    });

    public static final Variable LOCATION = register(new Variable("{location}") {

        @Override
        public String getReplacement(Player player) {
            Location location = player.getLocation();
            return "XYZ: " + format(location.getX()) + ", " + format(location.getY()) + ", " + format(location.getZ());
        }

    });

    public static final Variable PING = register(new Variable("{ping}") {
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
        return this.text;
    }

    public boolean needPlayer() {
        return needPlayer;
    }

    public static Variable register(Variable variable) {
        Validate.notNull(variable, "Variable can't be null!");
        Validate.isFalse(variables.contains(variable), "Variable already registered!");

        variables.add(variable);

        return variable;
    }

    public static void unregister(Variable variable) {
        Validate.notNull(variable, "Variable can't be null");

        variables.remove(variable);
    }

    public static String replace(String text) {
        Validate.notNull(text, "Text can't be null");

        for (Variable variable : variables) {
            if (variable.needPlayer() || !text.contains(variable.getText())) continue;

            String replacement = variable.getReplacement(null);
            if (replacement == null) continue;

            text = text.replace(variable.getText(), replacement);
        }

        return text;
    }

    public static String replace(String text, Player player) {
        Validate.notNull(text, "Text can't be null");
        Validate.notNull(player, "Player can't be null");

        for (Variable variable : variables) {
            if (!text.contains(variable.getText())) continue;

            String replacement = variable.getReplacement(player);
            if (replacement == null) continue;

            text = text.replace(variable.getText(), replacement);
        }

        return text;
    }

    public static Variable[] values() {
        return variables.toArray(new Variable[variables.size()]);
    }

    private static final DecimalFormat format = new DecimalFormat("##");

    private static String format(double value) {
        return format.format(value);
    }
}
