package unifi.lucal.stochasticmodelproject;

import org.jfree.chart.ChartPanel;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChartMaker extends ApplicationFrame{
	
	private static final long serialVersionUID = 1L;
	ArrayList<Double> xArrayList;
	ArrayList<Double> yArrayList;

	public LineChartMaker(String chartTitle, String xName, String yName, ArrayList<Double> xArrayList, ArrayList<Double> yArrayList) {
	      super(chartTitle);
	      this.xArrayList = xArrayList;
	      this.yArrayList = yArrayList;
	      JFreeChart lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         xName, yName,
	         createDataset(),
	         PlotOrientation.VERTICAL,
	         true,true,false);
	      
	      ChartPanel chartPanel = new ChartPanel(lineChart);
	      chartPanel.setPreferredSize(new java.awt.Dimension(1080, 640));
	      setContentPane(chartPanel);
	   }

	   private DefaultCategoryDataset createDataset() {
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      for(int i = 0; i<xArrayList.size(); i++) {
	    	  String xString = Double.toString(xArrayList.get(i));
	    	  dataset.addValue(yArrayList.get(i), "tempo_medio", xString);
	      }
	      
	      return dataset;
	   }
	   
}  
