import java.util.List;

public class B04_05 {
    public static void main(String[] args) {
        Necklace necklace = new Necklace();

        // Build necklace with precious and semi-precious stones
        necklace.addStone(new PreciousStone("Diamond D1", 1.2, 0.98, 5000, PreciousStone.Kind.DIAMOND));
        necklace.addStone(new PreciousStone("Ruby R1", 0.8, 0.85, 2200, PreciousStone.Kind.RUBY));
        necklace.addStone(new PreciousStone("Emerald E1", 1.0, 0.8, 2400, PreciousStone.Kind.EMERALD));
        necklace.addStone(new SemiPreciousStone("Amethyst A1", 2.5, 0.7, 400, SemiPreciousStone.Kind.AMETHYST));
        necklace.addStone(new SemiPreciousStone("Topaz T1", 1.7, 0.75, 450, SemiPreciousStone.Kind.TOPAZ));

        System.out.println("Necklace built with stones:");
        for (Stone s : necklace.getStones()) {
            System.out.println("  " + s);
        }

        System.out.println("\nTotal weight (carats): " + String.format("%.2f", necklace.getTotalCarats()));
        System.out.println("Total price: $" + String.format("%.2f", necklace.getTotalPrice()));

        // Sort by value (price) using Comparable in abstract superclass
        necklace.sortByValueDescending();
        System.out.println("\nSorted by value (descending):");
        for (Stone s : necklace.getStones()) {
            System.out.println("  " + s);
        }

        // Find stones by transparency range
        double minT = 0.75;
        double maxT = 0.9;
        List<Stone> range = necklace.findByTransparencyRange(minT, maxT);
        System.out.println("\nStones with transparency in [" + minT + ", " + maxT + "]:");
        for (Stone s : range) {
            System.out.println("  " + s);
        }
    }
}


