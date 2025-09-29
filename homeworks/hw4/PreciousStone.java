public class PreciousStone extends Stone {
    public enum Kind {
        DIAMOND, RUBY, SAPPHIRE, EMERALD
    }

    private final Kind kind;

    public PreciousStone(String name, double carats, double transparency, double baseValue, Kind kind) {
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
            case DIAMOND:
                return 3.0;
            case RUBY:
                return 2.2;
            case SAPPHIRE:
                return 2.0;
            case EMERALD:
                return 2.4;
            default:
                return 1.0;
        }
    }
}


