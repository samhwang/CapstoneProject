package com.po.app;

import static com.googlecode.charts4j.Color.*;
import static com.googlecode.charts4j.UrlUtil.normalize;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class TestGraph {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Logger.global.setLevel(Level.ALL);
	}

	@Test
	public void example1() { // FOR TESTING PURPOSES.
		// EXAMPLE CODE START
		// Defining data plots.
		BarChartPlot good = Plots.newBarChartPlot(
				Data.newData(90/2, 80/2, 40/2, 50/2, 25/2, 43/2, 12/2, 30/2), BLUEVIOLET, "Good");
		BarChartPlot bad = Plots.newBarChartPlot(
				Data.newData(40/2, 50/2, 30/2, 20/2, 10/2, 35/2, 11/2, 05/2), ORANGERED, "Bad");
		// BarChartPlot team3 = Plots.newBarChartPlot(
		// Data.newData(10, 20, 30, 30), LIMEGREEN, "Team C");

		// Instantiating chart.
		// BarChart chart = GCharts.newBarChart(team1, team2, team3);
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
				.newNumericRangeAxisLabels(0, 100));
		chart.addYAxisLabels(review);
		chart.addXAxisLabels(state);

		chart.setSize(600, 450);
		chart.setBarWidth(40);
		chart.setSpaceWithinGroupsOfBars(20);
		chart.setDataStacked(true);
		chart.setTitle("PationOpinions.org.au Reviews", BLACK, 16);
		chart.setGrid(250, 10, 3, 2);
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);
		String url = chart.toURLString();
		System.out.println(url);

		// EXAMPLE CODE END. Use this url string in your web or
		// Internet application.
		Logger.global.info(url);
		// String expectedString =
		// "http://chart.apis.google.com/chart?chf=bg,s,F0F8FF|c,lg,0,E6E6FA,1.0,FFFFFF,0.0&chs=600x450&chd=e:QAbhHrTN,FIWZHCDN,GaMzTNTN&chtt=Team+Scores&chts=000000,16&chg=100.0,10.0,3,2&chxt=y,y,x,x&chxr=0,0.0,100.0|1,0.0,100.0|3,0.0,100.0&chxl=1:|Score|2:|2002|2003|2004|2005|3:|Year&chxp=1,50.0|3,50.0&chxs=1,000000,13,0|3,000000,13,0&chdl=Team+A|Team+B|Team+C&chco=8A2BE2,FF4500,32CD32&chbh=100,20,8&cht=bvs";
		// assertEquals("Junit error", normalize(expectedString),
		// normalize(url));

	}
	
	
	public String GenerateChart(){//Huy Huynh 14/5/2015
		//Generate URL to output the chart.
		// Defining data plots.
		BarChartPlot good = Plots.newBarChartPlot(
				Data.newData(90/2, 80/2, 40/2, 50/2, 25/2, 43/2, 12/2, 30/2), BLUEVIOLET, "Good");
		BarChartPlot bad = Plots.newBarChartPlot(
				Data.newData(40/2, 50/2, 30/2, 20/2, 10/2, 35/2, 11/2, 05/2), ORANGERED, "Bad");
	
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
				.newNumericRangeAxisLabels(0, 100));
		chart.addYAxisLabels(review);
		chart.addXAxisLabels(state);
	
		chart.setSize(600, 450);
		chart.setBarWidth(40);
		chart.setSpaceWithinGroupsOfBars(20);
		chart.setDataStacked(true);
		chart.setTitle("PationOpinions.org.au Reviews", BLACK, 16);
		chart.setGrid(250, 10, 3, 2);
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);
		String url = chart.toURLString();
		System.out.println(url);
		
		return url;
	}

}

/*
 * So in the end, what this does is generate an url that will eventually leads
 * to a chart We need to find a way to put this in the actual app. Huy Huynh,
 * 28/04/2015 Source:
 * https://code.google.com/p/charts4j/source/browse/tags/v1.2/example/com/googlecode/charts4j/BarChartExample.java#51 
 * Starts at line 51 to 90
 */
