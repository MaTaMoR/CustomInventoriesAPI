package us.swiftex.custominventories.utils;

public interface CalculateEvent {

    enum CalculateType {

        NAME,
        LORE

    }

    String calculate(CalculateType type, String string);

}
