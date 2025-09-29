import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Necklace {
    private final List<Stone> stones = new ArrayList<>();

    public void addStone(Stone stone) {
        if (stone == null) {
            throw new IllegalArgumentException("Stone cannot be null");
        }
        stones.add(stone);
    }

    public List<Stone> getStones() {
        return Collections.unmodifiableList(stones);
    }

    public double getTotalCarats() {
        double sum = 0.0;
        for (Stone s : stones) {
            sum += s.getCarats();
        }
        return sum;
    }

    public double getTotalPrice() {
        double sum = 0.0;
        for (Stone s : stones) {
            sum += s.getPrice();
        }
        return sum;
    }

    public void sortByValueDescending() {
        Collections.sort(stones); // uses Comparable: price desc
    }

    public List<Stone> findByTransparencyRange(double minInclusive, double maxInclusive) {
        if (minInclusive > maxInclusive) {
            throw new IllegalArgumentException("minInclusive must be <= maxInclusive");
        }
        List<Stone> result = new ArrayList<>();
        for (Stone s : stones) {
            double t = s.getTransparency();
            if (t >= minInclusive && t <= maxInclusive) {
                result.add(s);
            }
        }
        return result;
    }
}


