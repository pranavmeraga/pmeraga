package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import helper.WordNet;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private NGramMap nGram;
    private WordNet wordNet;
    public HyponymsHandler(NGramMap n, WordNet w) {
        this.nGram = n;
        this.wordNet = w;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int k = q.k();
        int startyear = q.startYear();
        int endyear = q.endYear();
        NgordnetQueryType type = q.ngordnetQueryType();
        Set<String> hyponyms = wordNet.makeHyponym(words.get(0), type);
        for (int i = 1; i < words.size(); i++) {
            hyponyms.retainAll(wordNet.makeHyponym(words.get(i), type));
        }
        List<String> hyponym = new ArrayList<>(hyponyms);
        Collections.sort(hyponym);
        if (k == 0) {
            return hyponym.toString();
        }
        List<Double> hyponymCounts = new ArrayList<>();
        List<String> hyponymStrings = new ArrayList<>();
        for (String h : hyponym) {
            TimeSeries count = nGram.countHistory(h, startyear, endyear);
            double sum = 0.0;
            for (double x : count.data()) {
                sum += x;
            }
            hyponymCounts.add(sum);
            hyponymStrings.add(h);
        }
        List<String> sort = new ArrayList<>();
        int numHyponyms = Math.min(k, hyponymStrings.size());
        for (int x = 0; x < numHyponyms; x++) {
            double max = Collections.max(hyponymCounts);
            if (max > 0) {
                sort.add(hyponymStrings.get(hyponymCounts.indexOf(max)));
                hyponymCounts.set(hyponymCounts.indexOf(max), -1.0);
            }
        }
        Collections.sort(sort);
        return sort.toString();
    }
}
