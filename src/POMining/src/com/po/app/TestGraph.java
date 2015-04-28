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
	public void example1() {
		// EXAMPLE CODE START
		// Defining data plots.
		BarChartPlot team1 = Plots.newBarChartPlot(
				Data.newData(25, 43, 12, 30), BLUEVIOLET, "Team A");
		BarChartPlot team2 = Plots.newBarChartPlot(Data.newData(8, 35, 11, 5),
				ORANGERED, "Team B");
		BarChartPlot team3 = Plots.newBarChartPlot(
				Data.newData(10, 20, 30, 30), LIMEGREEN, "Team C");

		// Instantiating chart.
		BarChart chart = GCharts.newBarChart(team1, team2, team3);

		// Defining axis info and styles
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 13,
				AxisTextAlignment.CENTER);
		AxisLabels score = AxisLabelsFactory.newAxisLabels("Score", 50.0);
		score.setAxisStyle(axisStyle);
		AxisLabels year = AxisLabelsFactory.newAxisLabels("Year", 50.0);
		year.setAxisStyle(axisStyle);

		// Adding axis info to chart.
		chart.addXAxisLabels(AxisLabelsFactory.newAxisLabels("2002", "2003",
				"2004", "2005"));
		chart.addYAxisLabels(AxisLabelsFactory
				.newNumericRangeAxisLabels(0, 100));
		chart.addYAxisLabels(score);
		chart.addXAxisLabels(year);

		chart.setSize(600, 450);
		chart.setBarWidth(100);
		chart.setSpaceWithinGroupsOfBars(20);
		chart.setDataStacked(true);
		chart.setTitle("Team Scores", BLACK, 16);
		chart.setGrid(100, 10, 3, 2);
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);
		String url = chart.toURLString();

		// EXAMPLE CODE END. Use this url string in your web or
		// Internet application.
		Logger.global.info(url);
		String expectedString = "http://chart.apis.google.com/chart?chf=bg,s,F0F8FF|c,lg,0,E6E6FA,1.0,FFFFFF,0.0&chs=600x450&chd=e:QAbhHrTN,FIWZHCDN,GaMzTNTN&chtt=Team+Scores&chts=000000,16&chg=100.0,10.0,3,2&chxt=y,y,x,x&chxr=0,0.0,100.0|1,0.0,100.0|3,0.0,100.0&chxl=1:|Score|2:|2002|2003|2004|2005|3:|Year&chxp=1,50.0|3,50.0&chxs=1,000000,13,0|3,000000,13,0&chdl=Team+A|Team+B|Team+C&chco=8A2BE2,FF4500,32CD32&chbh=100,20,8&cht=bvs";
		assertEquals("Junit error", normalize(expectedString), normalize(url));

	}

}

/*
 * So in the end, what this does is generate an url that will eventually leads
 * to a chart We need to find a way to put this in the actual app. Huy Huynh,
 * 28/04/2015
 */
