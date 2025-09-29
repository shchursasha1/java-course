public abstract class Stone implements Comparable<Stone> {
    private final String name;
    private final double carats;
    private final double transparency; // 0.0 - 1.0
    private final double baseValue; // intrinsic value factor

    protected Stone(String name, double carats, double transparency, double baseValue) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (carats <= 0) {
            throw new IllegalArgumentException("Carats must be positive");
        }
        if (transparency < 0.0 || transparency > 1.0) {
            throw new IllegalArgumentException("Transparency must be in [0.0, 1.0]");
        }
        if (baseValue <= 0) {
            throw new IllegalArgumentException("Base value must be positive");
        }
        this.name = name;
        this.carats = carats;
        this.transparency = transparency;
        this.baseValue = baseValue;
    }

    public String getName() {
        return name;
    }

    public double getCarats() {
        return carats;
    }

    public double getTransparency() {
        return transparency;
    }

    public double getBaseValue() {
        return baseValue;
    }

    /**
     * Price depends on base value, transparency, type-specific factor.
     */
    public double getPrice() {
        double typeFactor = getTypeFactor();
        double transparencyFactor = 0.5 + transparency; // ranges [0.5, 1.5]
        return baseValue * typeFactor * transparencyFactor * carats;
    }

    protected abstract double getTypeFactor();

    @Override
    public int compareTo(Stone other) {
        // Sort by price descending (more valuable first)
        return -Double.compare(this.getPrice(), other.getPrice());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", carats=" + carats +
                ", transparency=" + transparency +
                ", price=" + String.format("%.2f", getPrice()) +
                '}';
    }
}


