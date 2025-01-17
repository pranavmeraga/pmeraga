package ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if (ts != null) {
            NavigableMap<Integer, Double> contains = ts.subMap(startYear, true, endYear, true);
            for (Map.Entry<Integer, Double> entry : contains.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> x = new ArrayList<>();
        for (Map.Entry<Integer, Double> z : this.entrySet()) {
            x.add(z.getKey());
        }
        return x;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> x = new ArrayList<>();
        for (Map.Entry<Integer, Double> z : this.entrySet()) {
            x.add(z.getValue());
        }
        return x;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries ts1 = new TimeSeries();
        for (Map.Entry<Integer, Double> entry : this.entrySet()) {
            ts1.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            if (ts1.containsKey(entry.getKey())) {
                ts1.put(entry.getKey(), ts1.get(entry.getKey()) + entry.getValue());
            } else {
                ts1.put(entry.getKey(), entry.getValue());
            }
        }
        return ts1;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries ts1 = new TimeSeries();
        for (Map.Entry<Integer, Double> entry : this.entrySet()) {
            if (ts.containsKey(entry.getKey()) && ts.get(entry.getKey()) != 0.0) {
                ts1.put(entry.getKey(), entry.getValue() / ts.get(entry.getKey()));
            } else {
                throw new IllegalArgumentException();
            }
        }
        return ts1;
    }
}
