package unifi.lucal.stochasticmodelproject;

import org.jfree.chart.ChartPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MultipleLineChartMaker extends ApplicationFrame{
	
	private static final long serialVersionUID = 1L;
	ArrayList<Double> xArrayList;

	public MultipleLineChartMaker(String chartTitle, String xName, String yName, 
			ArrayList<Double> xArrayList, ArrayList<ArrayList<Double>> totalMeanArrayList,
			ArrayList<Double> values) {
	      super(chartTitle);
	      this.xArrayList = xArrayList;
	      XYDataset dataset = createDataset(totalMeanArrayList, values);
	      
	      JFreeChart lineChart = ChartFactory.createXYLineChart(chartTitle, 
	              xName, yName, dataset);
	      
	      customizeChart();
	      
	      ChartPanel chartPanel = new ChartPanel(lineChart);
	      chartPanel.setPreferredSize(new java.awt.Dimension(1080, 640));
	      setContentPane(chartPanel);
	      
	   }

	private XYDataset createDataset(ArrayList<ArrayList<Double>> totalMeansArrayList, ArrayList<Double> values) {    // this method creates the data as time seris 
	    XYSeriesCollection dataset = new XYSeriesCollection();
	    
	    int i = 0;
	    for(ArrayList<Double> meanArrayList : totalMeansArrayList) {
	    	XYSeries series = new XYSeries("μ:"+Utility.round(values.remove(0), 2));
	    	int j=0;
	    	for(Double mean : meanArrayList) {
	    		series.add(xArrayList.get(j), mean);
	    		j++;
	    	}
	    	i++;
	    	dataset.addSeries(series);
	    }
	    
	    return dataset;
	}

	private void customizeChart() {   // here we make some customization
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

	    // sets paint color for each series
	    renderer.setSeriesPaint(0, Color.RED);
	    renderer.setSeriesPaint(1, Color.GREEN);
	    renderer.setSeriesPaint(2, Color.YELLOW);
	    renderer.setSeriesPaint(3, Color.MAGENTA);
	    renderer.setSeriesPaint(4, Color.BLUE);

	}
	   
}  
