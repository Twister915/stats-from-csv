package me.twister915.stats;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatsFromCSV {
    public static void main(String[] args) throws IOException {
        List<DescriptiveStatistics> statsFromFile = calculateFrom(new File(args[0]));
        int x = 0;
        for (DescriptiveStatistics stat : statsFromFile)
            System.out.printf("#%d\n" +
                    "  Stats:\n" +
                    "\tStd Dev: %.02f\n" +
                    "\tMean: %.02f\n" +
                    "\tCount: %d\n" +
                    "\t25 percentile: %.02f\n" +
                    "\t75 percentile: %.02f\n\n",
                    ++x,
                    stat.getStandardDeviation(),
                    stat.getMean(), stat.getN(),
                    stat.getPercentile(0.25),
                    stat.getPercentile(0.75));
    }

    private static List<DescriptiveStatistics> calculateFrom(File file) throws IOException {
        List<DescriptiveStatistics> stats = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int line = 0;
            String s;
            while ((s = reader.readLine()) != null) {
                if (s.length() == 0 || s.startsWith("#")) continue;
                String[] split = s.split(",");
                for (int i = 0; i < split.length; i++) {
                    DescriptiveStatistics descStats;
                    if (stats.size() == i) {
                        descStats = new DescriptiveStatistics();
                        stats.add(descStats);
                    } else if (stats.size() < i) throw new IllegalStateException("We're not going in order!");
                    else descStats = stats.get(i);
                    try {
                        descStats.addValue(Double.parseDouble(split[i]));
                    } catch (Exception e) {
                        System.err.println("Failed to read " + i + ", " + line);
                    }
                }
                line++;
            }
        }
        return stats;
    }
}
