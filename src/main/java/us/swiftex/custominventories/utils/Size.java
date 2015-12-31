package us.swiftex.custominventories.utils;

public enum Size {
    ONE_LINE(9),
    TWO_LINE(18),
    THREE_LINE(27),
    FOUR_LINE(36),
    FIVE_LINE(45),
    SIX_LINE(54);

    private final int size;

    private Size(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static Size fit(int slots) {
        if (slots < 10) {
            return ONE_LINE;
        } else if (slots < 19) {
            return TWO_LINE;
        } else if (slots < 28) {
            return THREE_LINE;
        } else if (slots < 37) {
            return FOUR_LINE;
        } else if (slots < 46) {
            return FIVE_LINE;
        } else {
            return SIX_LINE;
        }
    }
}
