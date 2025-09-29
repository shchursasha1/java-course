import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class B03_14 {

    /**
     * Subscriber entity.
     * Fields: Full name, Credit, Local call minutes, Long-distance call minutes.
     */
    public static final class Subscriber {
        private final String fullName;
        private final double credit;
        private final int localMinutes;
        private final int longDistanceMinutes;

        public Subscriber(String fullName, double credit, int localMinutes, int longDistanceMinutes) {
            if (fullName == null || fullName.isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be null or empty");
            }
            if (localMinutes < 0 || longDistanceMinutes < 0) {
                throw new IllegalArgumentException("Minutes cannot be negative");
            }
            this.fullName = fullName;
            this.credit = credit;
            this.localMinutes = localMinutes;
            this.longDistanceMinutes = longDistanceMinutes;
        }

        public String getFullName() {
            return fullName;
        }

        public double getCredit() {
            return credit;
        }

        public int getLocalMinutes() {
            return localMinutes;
        }

        public int getLongDistanceMinutes() {
            return longDistanceMinutes;
        }

        @Override
        public String toString() {
            return "Subscriber{" +
                    "fullName='" + fullName + '\'' +
                    ", credit=" + credit +
                    ", localMinutes=" + localMinutes +
                    ", longDistanceMinutes=" + longDistanceMinutes +
                    '}';
        }
    }

    /**
     * Returns subscribers whose local call minutes exceed the given limit,
     * sorted by credit in ascending order.
     */
    public static Subscriber[] filterByLocalTimeAndSortByCredit(Subscriber[] subscribers, int localLimitExclusive) {
        if (subscribers == null || subscribers.length == 0) {
            return new Subscriber[0];
        }
        List<Subscriber> result = new ArrayList<>();
        for (Subscriber s : subscribers) {
            if (s != null && s.getLocalMinutes() > localLimitExclusive) {
                result.add(s);
            }
        }
        result.sort(Comparator.comparingDouble(Subscriber::getCredit));
        return result.toArray(new Subscriber[0]);
    }

    /**
     * Returns subscribers who used long-distance calls (minutes > 0).
     */
    public static Subscriber[] withLongDistanceUsage(Subscriber[] subscribers) {
        if (subscribers == null || subscribers.length == 0) {
            return new Subscriber[0];
        }
        List<Subscriber> result = new ArrayList<>();
        for (Subscriber s : subscribers) {
            if (s != null && s.getLongDistanceMinutes() > 0) {
                result.add(s);
            }
        }
        return result.toArray(new Subscriber[0]);
    }

    public static void main(String[] args) {
        Subscriber[] subscribers = new Subscriber[] {
                new Subscriber("John Michael Smith", 1200.0, 350, 0),
                new Subscriber("Peter Andrew Johnson", 800.5, 120, 45),
                new Subscriber("Olivia Grace Davis", 1500.0, 500, 10),
                new Subscriber("Mary Ann Brown", 950.0, 80, 0),
                new Subscriber("Andrew Charles Miller", 600.0, 200, 5)
        };

        int localLimit = 180; // local call limit (minutes)
        Subscriber[] overLimitSortedByCredit = filterByLocalTimeAndSortByCredit(subscribers, localLimit);
        System.out.println("a) Subscribers with local calls over the limit, sorted by credit:");
        System.out.println(Arrays.toString(overLimitSortedByCredit));

        Subscriber[] usedLongDistance = withLongDistanceUsage(subscribers);
        System.out.println("b) Subscribers who used long-distance calls:");
        System.out.println(Arrays.toString(usedLongDistance));
    }
}


