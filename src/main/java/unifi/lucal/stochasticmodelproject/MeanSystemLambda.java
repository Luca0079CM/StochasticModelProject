package unifi.lucal.stochasticmodelproject;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

public class MeanSystemLambda {	
	public static void main(String args[]) {
		run(12, 15, 0.1, 0.8);
	}
	
	public static void run(double T, double N, double λ0, double μ0) {
		int nRun = 100;
		
		for (double μ = μ0; μ < 0.9; μ += 0.1) {
			ArrayList<Double> lambdaArrayList = new ArrayList<Double>();
			ArrayList<Double> meanArrayList = new ArrayList<Double>();
			
			for (double λ = λ0; λ < 0.85; λ += 0.05) {
				double ro;
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
				lambdaArrayList.add(Utility.round(λ, 2));
				System.out.println("Media dei tempi di sistema con λ=" + Utility.round(λ, 1) + " e μ=" + Utility.round(μ, 2) + ": " + Utility.round(mean, 2));
				double rateMean = 0;
				for (int i = 0; i < rateArrayList.size(); i++) {
					rateMean += rateArrayList.get(i);
				}
				ro = (rateMean/rateArrayList.size())/μ;
				System.out.println("Numero di arrivi medio alla seconda coda:"+Utility.round(rateMean/rateArrayList.size(), 2));
				System.out.println("Ro:"+Utility.round(ro, 2)+"\n");
			}
			String string = "Tempo medio di sistema e rate di arrivo con rate di servizio=" + Utility.round(μ, 2)+" T:"+T+" N:"+N;
			String xName = "Rate di arrivo";
			String yName = "Tempo medio di sistema (secondi)";
			LineChartMaker chart = new LineChartMaker(string, xName, yName, lambdaArrayList, meanArrayList);
			chart.pack();
			RefineryUtilities.centerFrameOnScreen(chart);
			chart.setVisible(true);
		}
	}

}


