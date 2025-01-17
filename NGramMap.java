package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private HashMap<String, TimeSeries> words;
    private TimeSeries counts;
    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        words = new HashMap<>();
        counts = new TimeSeries();
        In in = new In(wordsFilename);
        //TimeSeries num = new TimeSeries();
        while (!in.isEmpty()) {
            String nextLine = in.readLine();
            String[] splitline = nextLine.split("\t");
            String word = splitline[0];
            int year = Integer.parseInt(splitline[1]);
            double val = Double.parseDouble(splitline[2]);
            TimeSeries num = words.getOrDefault(word, new TimeSeries());
            num.put(year, val);
            words.put(word, num);
        }
        In in1 = new In(countsFilename);
        while (!in1.isEmpty()) {
            String nextLine = in1.readLine();
            String[] splitline = nextLine.split(",");
            int year = Integer.parseInt(splitline[0]);
            double val = Double.parseDouble(splitline[1]);
            counts.put(year, val);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries ts = this.words.get(word);
        if (ts != null) {
            return new TimeSeries(ts, startYear, endYear);
        } else {
            return new TimeSeries();
        }
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        TimeSeries ts = this.words.get(word);
        if (ts != null) {
            return ts;
        } else {
            return new TimeSeries();
        }
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return counts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries ts = countHistory(word, startYear, endYear);
        TimeSeries total = new TimeSeries(counts, startYear, endYear);
        if (ts != null) {
            return ts.dividedBy(counts);
        } else {
            return new TimeSeries();
        }
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries whistory = countHistory(word);
        if (whistory != null) {
            return whistory.dividedBy(counts);
        } else {
            return new TimeSeries();
        }
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> qwords,
                                          int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();
        for (String word : qwords) {
            TimeSeries iweight = weightHistory(word, startYear, endYear);
            ts = ts.plus(iweight);
        }
        return ts;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> qwords) {
        TimeSeries ts = new TimeSeries();
        for (String word : qwords) {
            TimeSeries iweight = weightHistory(word);
            ts = ts.plus(iweight);
        }
        return ts;
    }
}
