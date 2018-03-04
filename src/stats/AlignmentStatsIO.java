package stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import alignment.Alignment;
import scoring.ScoreFunction;
import utils.Utilities;

public class AlignmentStatsIO {

	Collection<Alignment> alignments_;
	int numGroups_ = 10;
	double granularity_ = .25;
	ScoreFunction f_;

	public AlignmentStatsIO(Collection<Alignment> alignments, ScoreFunction f) {
		alignments_ = alignments;
		f_ = f;
	}

	public void saveAlignmentStats(File f) {
		CorrelationAlignmentStats cStats = new CorrelationAlignmentStats();
		// print gap frequency distribution
		HashMap<Integer, Integer> gapFreqs = cStats.getGapFrequencies(alignments_);
		// System.out.println("gap frequencies: ");
		String gapFreqsStr = getFormattedII("", "", gapFreqs);

		// print offset distribution
		HashMap<Double, Integer> offsets = cStats.getOffsetDistribution(alignments_, .25);
		// System.out.println("offset distribution: ");
		String offsetDistStr = getFormattedDI("", "", offsets);

		// print score distribution
		HashMap<Double, Integer> scoreDist = cStats.getScoreDistribution(alignments_, 0.05, f_);
		// System.out.println("score distribution: ");
		String scoreDistStr = getFormattedDI("", "", scoreDist);

		// print gap open distribution
		double[] gapOpenFreqs = cStats.getGapOpenPositionFreqs(alignments_, numGroups_);
		String gapOpenFreqsStr = Utilities.mathematicaFormattedArray(gapOpenFreqs);

		try {
			PrintWriter pw = new PrintWriter(f);
			pw.println(gapFreqsStr);
			pw.println(offsetDistStr);
			pw.println(scoreDistStr);
			pw.println(gapOpenFreqsStr);
			pw.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getFormattedDI(String prefix, String suffix, HashMap<Double, Integer> map) {

		List<Double> keyset = new ArrayList<>(map.keySet());
		Collections.sort(keyset);
		String str = "{";
		for (Double key : keyset) {
			str += "{" + key + "," + map.get(key) + "},";
		}
		str = str.substring(0, str.length() - 1);
		str += "}";
		return prefix + "" + str + "" + suffix;
	}

	private String getFormattedII(String prefix, String suffix, HashMap<Integer, Integer> map) {

		List<Integer> keyset = new ArrayList<>(map.keySet());
		Collections.sort(keyset);
		String str = "{";
		for (Integer key : keyset) {
			str += "{" + key + "," + map.get(key) + "},";
		}
		str = str.substring(0, str.length() - 1);
		str += "}";
		return prefix + "" + str + "" + suffix;
	}
}
