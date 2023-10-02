package unifi.lucal.stochasticmodelproject;

import java.util.ArrayList;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.ojalgo.random.Erlang;
import org.ojalgo.random.Poisson;
import org.ojalgo.random.Uniform;

public class Utility {
	public static double pCalculator(double T, double N, double λ) {
		int N2 = (int) (N-1);
		double c = Math.pow(λ, N) / (CombinatoricsUtils.factorial(N2));
		double PN = c*Math.pow(T, N)*Math.exp(-λ*T);
		return PN;
	}
	
	
	public static double[] firstPhase(double T, double N, double λ, double PN) {
//		System.out.println("Prima fase:");
		Uniform uniform = new Uniform(0, 1);
		
		double[] result = new double[2];
		
		double u = uniform.doubleValue();
//		System.out.println("u: "+u);
		if(u <= PN) {
			// caso in cui saturo la coda con N arrivi e ho tempo aleatorio
//			System.out.println("Primo caso");
			// Sorteggio η (eta) secondo Erlang (Condizionata ad essere < T)
			/// Aumento il tempo di η
			Erlang erlang = new Erlang((int) N, λ);
			double t;
			do {
				t = erlang.doubleValue();
			}while(t >= T);      // Con il ciclo condiziono ad essere < T
			result[0] = t;
			// Aumento gli arrivi di N
			result[1] = N;
		}else { 
			// caso in cui raggiungo il tempo massimo T e ho arrivi aleatori
//			System.out.println("Secondo caso");
			
			// Sorteggio ζ (zeta) secondo Poisson (Condizionata ad essere < N)
			// Aumento poi gli arrivi di ζ
			Poisson poisson = new Poisson(λ*T); // N° di arrivi in tempo T
			double n;
			int counter = 0;
			do {
				n = poisson.doubleValue();
				counter += 1;
//				if(counter == 10) {
//					n= N-1;
//				}
			}while(n >= N);   // Con il ciclo condiziono ad essere < N
			// Avanzo di T il tempo
			result[0] = T;
			result[1] = n;	
		}
//		System.out.printf("Tempo: "+"%.3f \n", result[0]);
//		System.out.printf("Arrivi: "+"%.3f \n", result[1]);
		return result;
	}

	public static double secondPhase(double μ) {
//		System.out.println("Seconda Fase:");
		// Applico il metodo di inversione per calcolare il tempo di servizio
		
		// Genero un numero casuale tra 0 e 1, distibuito uniformemente
		Uniform uniform = new Uniform(0, 1);
		double u = uniform.doubleValue();
		
		// Usando il metodo dell'inversione calcoliamo l'inversa dell'esponenziale
		// e calcoliamo il valore casuale usando come argomento il risultato
		// del campionamento uniforme di sopra
		double exp = - Math.log(1 - u) / μ;
		
//		System.out.printf("%.3f \n", exp);
		return exp;
	}
	
	public static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
	public static double simulation(double T, double N, double λ, double μ, ArrayList<Double> rateArrayList) {
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
		
		rateArrayList.add(totalArrival/time);
//		System.out.println("Numero di arrivi nel tempo:"+totalArrival/time);
//		System.out.println();
//		System.out.println("Tempo finale: "+ time);
//		System.out.println("Arrivi rimasti in coda: "+queueArrivals.size());
//		System.out.println("Numero di iterazioni: "+i+"\n");
		
		return meanWaitingTime/served;
	}
	
}
