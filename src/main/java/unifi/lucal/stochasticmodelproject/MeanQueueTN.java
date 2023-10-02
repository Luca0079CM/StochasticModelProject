package unifi.lucal.stochasticmodelproject;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

public class MeanQueueTN {	
	public static void main(String args[]) {
		run(10, 8, 0.5, 0.4);
	}

	public static void run(double T0, double N0, double λ, double μ) {
		double T, N;
		int nRun = 20;
		
		for (N = N0; N < 20; N++) {
			ArrayList<Double> tArrayList = new ArrayList<Double>();
			ArrayList<Double> meanArrayList = new ArrayList<Double>();
			
			for (T = T0; T < 25; T++) {
				ArrayList<Double> results = new ArrayList<Double>();
				
				for (int i = 0; i < nRun; i++)
					results.add(simulation(T, N, λ, μ));
				System.out.println();
				double mean = 0;
				for (int i = 0; i < results.size(); i++) {
					mean += results.get(i);
				}
				mean /= nRun;
				meanArrayList.add(mean);
				tArrayList.add(T);
				System.out.println("Media dei tempi in coda con T=" + T + " e N=" + N + ":" + Utility.round(mean, 2));
			}
			String string = "Tempo medio in coda e T con N:"+N+" rate di arrivo: "+ Utility.round(λ, 1) + "rate di servizio=" + Utility.round(μ, 1);
			String xName = "T";
			String yName = "Tempo medio in coda";
			LineChartMaker chart = new LineChartMaker(string, xName, yName, tArrayList, meanArrayList);
			chart.pack();
			RefineryUtilities.centerFrameOnScreen(chart);
			chart.setVisible(true);
		}
	}
	
	public static double simulation(double T, double N, double λ, double μ) {
		ArrayList<Arrival> queueArrivals = new ArrayList<Arrival>();
		double time = 0;
		int served = 0;
		double meanWaitingTime = 0;
		double t;
		double n;
		double[] results = new double[2];
		double result2;
		
		int i = 0;
		int maxTime = 100000;
		double PN = Utility.pCalculator(T, N, λ);
		double totalArrival = 0; 
		
		while(time < maxTime) {
//			System.out.println("Iterazione n°"+ (i+1));
			results = Utility.firstPhase(T, N, λ, PN);
			t = results[0];
			n = results[1];
			if (queueArrivals.size() > 0) {
				result2 = Utility.secondPhase(μ);
			}else {
				// Se la coda è vuota setto il tempo del servizio a T+1
				// così entro per forza nel primo if
				result2 = T+1;
			}
			
			if(t <= result2) {
//				System.out.println("Arriva un batch nella seconda coda");
				for(int j = 0; j < n; j++)
					queueArrivals.add(new Arrival(time));
				time += t;
				totalArrival += n;
			}else {
//				System.out.println("Servo un arrivo nella seconda coda");
				Arrival a = queueArrivals.remove(0);
				served++;
				meanWaitingTime += a.getWaitingTime(time);
				time += result2;
			}
			i++;
		}
		
//		System.out.println("Numero di arrivi nel tempo:"+totalArrival/time);
//		System.out.println();
//		System.out.println("Tempo finale: "+ time);
//		System.out.println("Arrivi rimasti in coda: "+queueArrivals.size());
//		System.out.println("Numero di iterazioni: "+i+"\n");
		
		return meanWaitingTime/served;
	}
	
}


