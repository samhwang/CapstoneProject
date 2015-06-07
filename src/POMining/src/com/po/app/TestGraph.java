package com.po.app;

import static com.googlecode.charts4j.Color.*;
import static com.googlecode.charts4j.UrlUtil.normalize;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.BarChart;
import com.googlecode.charts4j.BarChartPlot;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.po.parser.Story;
import com.po.parser.StoryManager;

public class TestGraph {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Logger.global.setLevel(Level.ALL);
	}

	@Test
	public String example1(StoryManager sM, String strMode,
			String strCurrentTopic, ObservableList<String> listItems) {

		// Tally up the "Good" and "Bad" feedback for each state FOR THE
		// SELECTED TOPIC and store the values. This
		// also tracks the feedback that didn't have a state associated with
		// them.
		// Stuart Barker, 31/05/2015

		int nsw = 0;
		int vic = 0;
		int act = 0;
		int qld = 0;
		int tas = 0;
		int sa = 0;
		int nt = 0;
		int wa = 0;
		int other = 0;

		int nswBad = 0;
		int vicBad = 0;
		int actBad = 0;
		int qldBad = 0;
		int tasBad = 0;
		int saBad = 0;
		int ntBad = 0;
		int waBad = 0;
		int otherBad = 0;

		Set<String> setIDgood = new HashSet<String>();
		setIDgood = sM.GetIDsByTopic("Good", strCurrentTopic);

		for (String id : setIDgood) {
			if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_NSW) > 0)) {
				nsw++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_VIC) > 0)) {
				vic++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_ACT) > 0)) {
				act++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_QLD) > 0)) {
				qld++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_TAS) > 0)) {
				tas++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_SA) > 0)) {
				sa++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_NT) > 0)) {
				nt++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_WA) > 0)) {
				wa++;
			} else {
				other++;
			}
		}

		Set<String> setIDbad = new HashSet<String>();
		setIDbad = sM.GetIDsByTopic("Bad", strCurrentTopic);

		for (String id : setIDbad) {
			if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_NSW) > 0)) {
				nswBad++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_VIC) > 0)) {
				vicBad++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_ACT) > 0)) {
				actBad++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_QLD) > 0)) {
				qldBad++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_TAS) > 0)) {
				tasBad++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_SA) > 0)) {
				saBad++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_NT) > 0)) {
				ntBad++;
			} else if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & Story.AU_STATE_FLAG_WA) > 0)) {
				waBad++;
			} else {
				otherBad++;
			}
		}

		// Calculate total reviews of each state - HuyHuynh 7/6/2015

		int nswTotal = nsw + nswBad;
		int vicTotal = vic + vicBad;
		int actTotal = act + actBad;
		int qldTotal = qld + qldBad;
		int tasTotal = tas + tasBad;
		int saTotal = sa + saBad;
		int ntTotal = nt + ntBad;
		int waTotal = wa + waBad;
		int otherTotal = other + otherBad;

		// Find maximum value. See line 297 - Huy Huynh 7/6/2015
		int max = findMax(nswTotal, vicTotal, actTotal, qldTotal, tasTotal,
				saTotal, ntTotal, waTotal, otherTotal);

		// Algorithm explanation: See line 328 - Huy Huynh 7/6/2015
		// Values for plotting in the charts
		// Also note that current NT value are too small to fit in the charts...

		int nswPlot = nsw * 100 / max;
		int nswBadPlot = nswBad * 100 / max;
		int vicPlot = vic * 100 / max;
		int vicBadPlot = vicBad * 100 / max;
		int actPlot = act * 100 / max;
		int actBadPlot = actBad * 100 / max;
		int qldPlot = qld * 100 / max;
		int qldBadPlot = nsw * 100 / max;
		int tasPlot = tas * 100 / max;
		int tasBadPlot = tasBad * 100 / max;
		int saPlot = sa * 100 / max;
		int saBadPlot = saBad * 100 / max;
		int ntPlot = nt * 100 / max;
		int ntBadPlot = ntBad * 100 / max;
		int waPlot = wa * 100 / max;
		int waBadPlot = waBad * 100 / max;
		int ohterPlot = other * 100 / max; // Currently not added
		int otherBadPlot = otherBad * 100 / max; // Currently not added

		// These printing statements are for testing purposes to ensure that the
		// graph is accurate based on
		// at least the first few state feedback values. They should be
		// commented out on final delivery.
		// Stuart Barker, 31/05/2015

		/*
		 * System.out.println(String.format("VIC Good/Bad Total: %d/%d", vic,
		 * vicBad));
		 * System.out.println(String.format("NSW Good/Bad Total: %d/%d", nsw,
		 * nswBad));
		 * System.out.println(String.format("ACT Good/Bad Total: %d/%d", act,
		 * actBad));
		 * System.out.println(String.format("QLD Good/Bad Total: %d/%d", qld,
		 * qldBad));
		 */

		/*
		 * System.out.println(String.format("STATE Good Total: %d",
		 * nsw+vic+act+qld+tas+sa+nt+wa));
		 * System.out.println(String.format("OTHER Good Total: %d", other));
		 */

		// FOR TESTING PURPOSES.
		// EXAMPLE CODE START
		// Defining data plots.

		// Hard coded data. Will be replaced later. - Huy Huynh 7/6/2015
		// BarChartPlot good = Plots.newBarChartPlot(
		// Data.newData(90/2, 80/2, 40/2, 50/2, 25/2, 43/2, 12/2, 30/2),
		// BLUEVIOLET, "Good");
		// BarChartPlot bad = Plots.newBarChartPlot(
		// Data.newData(40/2, 50/2, 30/2, 20/2, 10/2, 35/2, 11/2, 05/2),
		// ORANGERED, "Bad");

		// CURRENTLY COMMENTED OUT
		// The following is an example of plugging in the found values to the
		// graph. Currently not fully functional.
		// INTENDED FUNCTIONALITY CHANGE: Add a column for "Other" or
		// "Unspecified" state feedback.
		// Stuart Barker, 31/05/2015

		// Huy's improved part of the chart - Huy Huynh 7/6/2015
		// Algorithm explanation: See line 328 - Huy Huynh 7/6/2015
		BarChartPlot good = Plots.newBarChartPlot(Data.newData(vicPlot,
				nswPlot, actPlot, qldPlot, saPlot, waPlot, ntPlot, tasPlot),
				BLUEVIOLET, "Good");
		BarChartPlot bad = Plots.newBarChartPlot(Data.newData(vicBadPlot,
				nswBadPlot, actBadPlot, qldBadPlot, saBadPlot, waBadPlot,
				ntBadPlot, tasBadPlot), ORANGERED, "Bad");

		// END Stuart Barker code

		// Instantiating chart.
		BarChart chart = GCharts.newBarChart(good, bad);

		// Defining axis info and styles
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 13,
				AxisTextAlignment.CENTER);
		AxisLabels review = AxisLabelsFactory.newAxisLabels("Reviews", 50.0);
		review.setAxisStyle(axisStyle);
		AxisLabels state = AxisLabelsFactory.newAxisLabels("State", 50.0);
		state.setAxisStyle(axisStyle);

		// Adding axis info to chart.
		chart.addXAxisLabels(AxisLabelsFactory.newAxisLabels("VIC", "NSW",
				"ACT", "QLD", "SA", "WA", "NT", "TAS"));
		chart.addYAxisLabels(AxisLabelsFactory
				.newNumericRangeAxisLabels(0, max));
		chart.addYAxisLabels(review);
		chart.addXAxisLabels(state);

		chart.setSize(600, 450);
		chart.setBarWidth(40);
		chart.setSpaceWithinGroupsOfBars(20);
		chart.setDataStacked(true);
		chart.setTitle("PatientOpinions.org.au Feedback", BLACK, 16);
		chart.setGrid(250, 10, 3, 2);
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);
		String url = chart.toURLString();

		/*
		 * EXAMPLE CODE END. Use this url string in your web or Internet
		 * application. Logger.global.info(url); String expectedString =
		 * "http://chart.apis.google.com/chart?chf=bg,s,F0F8FF|c,lg,0,E6E6FA,1.0,FFFFFF,0.0&chs=600x450&chd=e:QAbhHrTN,FIWZHCDN,GaMzTNTN&chtt=Team+Scores&chts=000000,16&chg=100.0,10.0,3,2&chxt=y,y,x,x&chxr=0,0.0,100.0|1,0.0,100.0|3,0.0,100.0&chxl=1:|Score|2:|2002|2003|2004|2005|3:|Year&chxp=1,50.0|3,50.0&chxs=1,000000,13,0|3,000000,13,0&chdl=Team+A|Team+B|Team+C&chco=8A2BE2,FF4500,32CD32&chbh=100,20,8&cht=bvs"
		 * ; assertEquals("Junit error", normalize(expectedString),
		 * normalize(url));
		 */
		return url;

	}

	// Currently isn't in use. But we need to move the complete method to this
	// one. - Huy Huynh 7/6/2015
	// currently just return a string saying "Hello"
	public String GenerateChart() {// Huy Huynh 14/5/2015
		String a = "Hello";
		return a;
		/*
		 * //Generate URL to output the chart. // Defining data plots.
		 * BarChartPlot good = Plots.newBarChartPlot( Data.newData(90/2, 80/2,
		 * 40/2, 50/2, 25/2, 43/2, 12/2, 30/2), BLUEVIOLET, "Good");
		 * BarChartPlot bad = Plots.newBarChartPlot( Data.newData(40/2, 50/2,
		 * 30/2, 20/2, 10/2, 35/2, 11/2, 05/2), ORANGERED, "Bad");
		 * 
		 * // Instantiating chart. BarChart chart = GCharts.newBarChart(good,
		 * bad);
		 * 
		 * // Defining axis info and styles AxisStyle axisStyle =
		 * AxisStyle.newAxisStyle(BLACK, 13, AxisTextAlignment.CENTER);
		 * AxisLabels review = AxisLabelsFactory.newAxisLabels("Reviews", 50.0);
		 * review.setAxisStyle(axisStyle); AxisLabels state =
		 * AxisLabelsFactory.newAxisLabels("State", 50.0);
		 * state.setAxisStyle(axisStyle);
		 * 
		 * // Adding axis info to chart.
		 * chart.addXAxisLabels(AxisLabelsFactory.newAxisLabels("VIC", "NSW",
		 * "ACT", "QLD", "SA", "WA", "NT", "TAS"));
		 * chart.addYAxisLabels(AxisLabelsFactory .newNumericRangeAxisLabels(0,
		 * 100)); chart.addYAxisLabels(review); chart.addXAxisLabels(state);
		 * 
		 * chart.setSize(600, 450); chart.setBarWidth(40);
		 * chart.setSpaceWithinGroupsOfBars(20); chart.setDataStacked(true);
		 * chart.setTitle("PationOpinions.org.au Reviews", BLACK, 16);
		 * chart.setGrid(250, 10, 3, 2);
		 * chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));
		 * LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER,
		 * 100); fill.addColorAndOffset(WHITE, 0); chart.setAreaFill(fill);
		 * String url = chart.toURLString(); System.out.println(url);
		 * 
		 * return url;
		 */
	}

	/*
	 * Find the maximum value within 9 columns. - Huy Huynh 7/6/2015 Simple
	 * algorithm: - Place every value into array. - put first value as maximum -
	 * if next value is larger then max, then max = that value. - repeat
	 */
	public int findMax(int nswTotal, int vicTotal, int actTotal, int qldTotal,
			int tasTotal, int saTotal, int ntTotal, int waTotal, int otherTotal) {
		int max;

		int[] arr = { nswTotal, vicTotal, actTotal, qldTotal, tasTotal,
				saTotal, ntTotal, waTotal, otherTotal };
		int len = arr.length;
		max = arr[0];
		for (int i = 0; i < len; i++) {
			if (max < arr[i]) {
				max = arr[i];
			}
		}
		return max;
	}

}

/*
 * So in the end, what this does is generate an url that will eventually leads
 * to a chart We need to find a way to put this in the actual app. Huy Huynh,
 * 28/04/2015 Source:
 * https://code.google.com/p/charts4j/source/browse/tags/v1.2/
 * example/com/googlecode/charts4j/BarChartExample.java#51 Starts at line 51 to
 * 90
 */
/*
 * Problem with the chart: The value will only be presented in the sense of
 * 0-100 So what we will do is: Find the maximum value of all the columns Then
 * set the highest of y-axis (vertical) to be that value. Every value will be
 * calculated as a percentage on top of that value.
 */