package unifi.lucal.stochasticmodelproject;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

public class RatesArrivals {	
	public static void main(String args[]) {
		run(10, 8, 0.1, 0.7);
		run(12, 15, 0.1, 0.8);
	}
	

	public static void run(double T, double N, double λ0, double μ0) {		
		int nRun = 100;
		
		for (double μ = μ0; μ < (μ0+0.1); μ += 0.1) {
			ArrayList<Double> lambdaArrayList = new ArrayList<Double>();
			ArrayList<Double> meanArrayList = new ArrayList<Double>();
			
			for (double λ = λ0; λ < (λ0+0.75); λ += 0.05) {
				ArrayList<Double> results = new ArrayList<Double>();	
				ArrayList<Double> rateArrayList = new ArrayList<Double>();
				
				for (int i = 0; i < nRun; i++)
					results.add(Utility.simulation(T, N, λ, μ, rateArrayList));
				double mean = 0;
				for (int i = 0; i < results.size(); i++) {
					mean += results.get(i);
				}
				mean /= nRun;
				System.out.println("Media dei tempi in coda con λ=" + Utility.round(λ, 2) + " e μ=" + Utility.round(μ, 2) + ": " + Utility.round(mean, 2));
				double rateMean = 0;
				for (int i = 0; i < rateArrayList.size(); i++) {
					rateMean += rateArrayList.get(i);
				}
				meanArrayList.add(Utility.round(rateMean/rateArrayList.size(), 2));
				lambdaArrayList.add(Utility.round(λ, 2));
				System.out.println("Rate di arrivo medio alla seconda coda:"+Utility.round(rateMean/rateArrayList.size(), 2)+"\n");
			}
			String string = "Rate di arrivo medio alla seconda coda e rate di arrivo alla prima coda con rate di servizio=" + Utility.round(μ, 2)+" T:"+T+" N:"+N;
			String xName = "Rate di arrivo alla prima coda";
			String yName = "Rate di arrivo medio alla seconda coda";
			LineChartMaker chart = new LineChartMaker(string, xName, yName, lambdaArrayList, meanArrayList);
			chart.pack();
			RefineryUtilities.centerFrameOnScreen(chart);
			chart.setVisible(true);
		}
	}
	
}


