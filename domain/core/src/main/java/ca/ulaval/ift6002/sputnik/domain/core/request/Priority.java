package ca.ulaval.ift6002.sputnik.domain.core.request;

public enum Priority {
    VERYHIGH, HIGH, NORMAL, LOW, VERYLOW;

    public static Priority fromInteger(int x) {
        switch(x) {
            case 1:
                return VERYHIGH;
            case 2:
                return HIGH;
            case 3:
                return NORMAL;
            case 4:
                return LOW;
            case 5:
                return VERYLOW;
        }
        return null;
    }
}
