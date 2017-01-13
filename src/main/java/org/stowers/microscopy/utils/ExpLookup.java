package org.stowers.microscopy.utils;

import java.util.Map.Entry;
import java.util.TreeMap;

public class ExpLookup {
	
	double[] X;
	double xmin;
	double xmax;
	double del = 1e-2;
	int size = 100000;
	
	TreeMap<Double, Double> expMap = null;
	
	public ExpLookup() {
		
		int size = (int)(100.*(xmax - xmin));
		expMap = new TreeMap<>();
		fillExp();
	}
	
	
	public void fillExp() {
		
		double x = 0;
		xmin = x;
		xmax = x;
		expMap.put(0., 1.);
		x += del*1.0000001;
		
		while (x <= 1024*16) {
//			System.out.println(x + " " + del);
			double fp = Math.exp(x);
			double fn = Math.exp(-x);
			expMap.put(x, fp);
			expMap.put(-x, fn);
			
			if (x > xmax) {
				xmax = x;
				xmin = -x;
			}
			del *= 1 + del;
			if (del > .05) {
				del = .05;
			}
			x += del;
		}
		
//		if (lastx < xmax) {
//			double f = Math.exp(xmax);
//			expMap.put(xmax, f);
//		}
		
		System.out.println(expMap.size());
		System.out.println("hh: " + x + " " + del);
	}
	
	public double texp(double x) {
		
		if (x > xmax) {
			return Float.POSITIVE_INFINITY;
		}
		
		if (x < xmin) {
			return 0;
		}
		
		Entry<Double, Double> entry1 = expMap.floorEntry(x);
		Entry<Double, Double> entry2 = expMap.ceilingEntry(x);
//		Entry<Double, Double> entry2 = expMap.floorEntry(x + del);
		double k1 = entry1.getKey();
		double k2 = entry2.getKey();
		
		double d1 = x - k1;
		double d2 = k2 - x;
		
		double dx;
		double v0;
		if (d1 < d2) {
			dx = d1;
			v0 = entry1.getValue();
		} else {
			dx = -d2;
			v0 = entry2.getValue();
		}
		
		double res = v0*(1 + dx + dx*dx/2.);
	
		return res;
	}
	
	public double exp(double x) {
		
		if (x > xmax) {
			return Float.POSITIVE_INFINITY;
		}
		
		if (x < xmin) {
			return 0.;
		}
		
		Entry<Double, Double> entry1 = expMap.floorEntry(x);
		Entry<Double, Double> entry2 = expMap.ceilingEntry(x);
//		Entry<Double, Double> entry2 = expMap.floorEntry(x + del);
		double k1 = entry1.getKey();
		double k2 = entry2.getKey();
		double v1 = entry1.getValue();
		double v2 = entry2.getValue();
		
		System.out.println(k1 + ", " + v1 + ", " + k2 + ", " + v2);
		double m = (v2 - v1)/(k2 - k1);
		double y = m*(x - k1) + v1;
		return y;
	}
	
	public static void main(String[] args) {
		
		ExpLookup exp = new ExpLookup();
		double z = -123.6;
		System.out.println(exp.exp(z));
		System.out.println(Math.exp(z));
		System.out.println(exp.texp(z));
		
		exp = null;
	}
}
