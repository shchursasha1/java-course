public class SemiPreciousStone extends Stone {
    public enum Kind {
        AMETHYST, TOPAZ, OPAL, GARNET
    }

    private final Kind kind;

    public SemiPreciousStone(String name, double carats, double transparency, double baseValue, Kind kind) {
        super(name, carats, transparency, baseValue);
        if (kind == null) {
            throw new IllegalArgumentException("Kind cannot be null");
        }
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    @Override
    protected double getTypeFactor() {
        switch (kind) {
            case AMETHYST:
                return 1.3;
            case TOPAZ:
                return 1.4;
            case OPAL:
                return 1.2;
            case GARNET:
                return 1.25;
            default:
                return 1.0;
        }
    }
}


