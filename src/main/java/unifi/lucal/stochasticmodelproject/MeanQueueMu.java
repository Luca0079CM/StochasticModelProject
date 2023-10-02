package unifi.lucal.stochasticmodelproject;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

public class MeanQueueMu {	
	public static void main(String args[]) {
		run(10, 8, 0.5, 0.2);
	}

	public static void run(double T, double N, double λ0, double μ0) {
		int nRun = 100;
		
		for (double λ = λ0; λ < 0.8; λ += 0.1) {
			ArrayList<Double> muArrayList = new ArrayList<Double>();
			ArrayList<Double> meanArrayList = new ArrayList<Double>();
			
			for (double μ = μ0; μ < 1.5; μ += 0.05) {
				ArrayList<Double> results = new ArrayList<Double>();	
				ArrayList<Double> rateArrayList = new ArrayList<Double>();
				
				for (int i = 0; i < nRun; i++)
					results.add(Utility.simulation(T, N, λ, μ, rateArrayList));
				double mean = 0;
				for (int i = 0; i < results.size(); i++) {
					mean += results.get(i);
				}
				mean /= nRun;
				meanArrayList.add(mean);
				muArrayList.add(Utility.round(μ, 2));
				System.out.println("Media dei tempi in coda con λ=" + Utility.round(λ, 2) + " e μ=" + Utility.round(μ, 2) + ": " + Utility.round(mean, 2));
				double rateMean = 0;
				for (int i = 0; i < rateArrayList.size(); i++) {
					rateMean += rateArrayList.get(i);
				}
				System.out.println("Tasso di arrivi medio alla seconda coda:"+Utility.round(rateMean/rateArrayList.size(), 2)+"\n");
			}
			String string = "Tempo medio in coda e rate di servizio con rate di arrivo=" + Utility.round(λ, 2);
			String xName = "Rate di servizio μ";
			String yName = "Tempo medio in coda (secondi)";
			LineChartMaker chart = new LineChartMaker(string, xName, yName, muArrayList, meanArrayList);
			chart.pack();
			RefineryUtilities.centerFrameOnScreen(chart);
			chart.setVisible(true);
		}
	}
	
}


