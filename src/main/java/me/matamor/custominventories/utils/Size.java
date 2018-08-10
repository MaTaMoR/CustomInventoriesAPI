package me.matamor.custominventories.utils;

public enum Size {

    ONE_LINE(9),
    TWO_LINE(18),
    THREE_LINE(27),
    FOUR_LINE(36),
    FIVE_LINE(45),
    SIX_LINE(54);

    private final int size;

    Size(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getLine() {
        switch (this) {
            case ONE_LINE:
                return 0;
            case TWO_LINE:
                return 1;
            case THREE_LINE:
                return 2;
            case FOUR_LINE:
                return 3;
            case FIVE_LINE:
                return 4;
            case SIX_LINE:
                return 5;
            default:
                return 0;
        }
    }

    public int getPosition() {
        switch (this) {
            case ONE_LINE:
                return 1;
            case TWO_LINE:
                return 2;
            case THREE_LINE:
                return 3;
            case FOUR_LINE:
                return 4;
            case FIVE_LINE:
                return 5;
            case SIX_LINE:
                return 6;
            default:
                return 0;
        }
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
