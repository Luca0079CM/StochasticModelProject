package unifi.lucal.stochasticmodelproject;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

public class TimeIntervalsRatesArrivals {	
	public static void main(String args[]) {
		ArrayList<ArrayList<Double>> totalMeanArrayList = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> lambdaArrayList = new ArrayList<Double>();
		double T = 10, N = 8, μ=0.7;
		
		for(double λ=0.1; λ<(μ+0.05); λ+=0.05) {
//			totalMeanArrayList.add(run(12, 15, λ, 0.8));
			totalMeanArrayList.add(run(T, N, λ, μ));
			lambdaArrayList.add(λ);
		}
		
		String string = "Rate di arrivo alla seconda coda per intervalli di tempo al variare di λ-T="+T+", N="+N+", μ="+μ; 
		String xName = "Intervallo di tempo";
		String yName = "Rate di arrivo seconda coda";
		ArrayList<Double> iArrayList = new ArrayList<Double>();
		for(int i = 0; i<totalMeanArrayList.get(0).size(); i++) {
			iArrayList.add((double) i);
		}
		
		
		MultipleLineChartMaker chart = new MultipleLineChartMaker(string, xName, yName, iArrayList, 
				totalMeanArrayList, lambdaArrayList);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
	
	public static ArrayList<Double> run(double T, double N, double λ0, double μ0) {
		double λ = λ0;   // arrivi/secondo
		double μ = μ0;   // arrivi/secondo;
		
		int nRun = 200;
		ArrayList<double[]> results = new ArrayList<double[]>();
		ArrayList<Double> meanArrayList = new ArrayList<Double>();
		
		for (int i = 0; i < nRun; i++)
			results.add(simulation(T, N, λ, μ));
		System.out.println();
		for (int i = 0; i < results.get(0).length; i++) {
			double mean = 0;
			for (double[] r : results) {
				mean += r[i];
			}
			System.out.println("Istante di tempo n°: " + (i + 1));
			System.out.println("valore medio su " + nRun + " simulazioni:" + mean / nRun);
			System.out.println("Numero di valori:"+results.get(0).length);
			System.out.println();
			meanArrayList.add(mean / nRun);
		}
		System.out.println(meanArrayList.get(1));
		System.out.println(meanArrayList.get(3));
		return meanArrayList;
	}
	
	public static double[] simulation(double T, double N, double λ, double μ) {
		ArrayList<Arrival> queueArrivals = new ArrayList<Arrival>();
		double time = 0;
		double t;
		double n;
		double[] results = new double[2];
		double result2;
		double totalArrival = 0; 
		
		int i = 0;
		int nSaved = 0;
		int maxTime = 100000;
		int timeStep = maxTime/100;
		double[] valueSaved = new double[maxTime/timeStep+1];
		double PN = Utility.pCalculator(T, N, λ);
		
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
				queueArrivals.remove(0);
				time += result2;
			}
			
//			System.out.println("Coda con "+queueArrivals.size()+" elementi");
			if(time >= timeStep*(nSaved+1)-T && time <= timeStep*(nSaved+1)+T) {
				nSaved++;
				valueSaved[nSaved] = totalArrival/time;
			}
			i++;
//			System.out.println("------------------------------");
		}
		return valueSaved;
	}
	
}


