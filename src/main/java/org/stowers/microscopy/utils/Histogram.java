package org.stowers.microscopy.utils;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by cjw on 7/27/16.
 */
public class Histogram {

    double[] counts;
    double[] bins;
    double[] data;

    int nbins;
    double dataMin;
    double dataMax;
    double startx;
    double endx;
    double hmax;

    public Histogram(double[] data, int xnbins) {
        this.data = data;
        this.nbins = xnbins;
        findDataMinMax();
        makeBins();
        counts = new double[nbins];
        System.out.println(dataMin + " " + dataMax);
        doHist();
    }

    public double[] doHist() {

        for (int i = 0; i < data.length; i++) {

            double x = data[i];
            int k = (int)(nbins*(x - startx)/(endx - startx));
            try {
                counts[k]++;
            }
            catch (Exception e) {
                System.out.println(x + " " + k + " " + startx + " " + endx);
            }
//            System.out.println(x + " " + k);
        }

        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > hmax) {
                hmax = counts[i];
            }
        }
        return counts;
    }

    public void setBins(double[] cbins) {
        bins = cbins;
        nbins = bins.length;
    }

    public void setStartX(double x) {
        startx = x;
    }

    public void setEndX(double x) {
        endx = x;
    }

    public double[][] getHistogramArray(boolean logY) {

        double[][] res =  new double[2][counts.length];

        if (!logY) {
            res[1] = counts;
            for (int i = 1; i <= counts.length; i++) {
                res[0][i - 1] = (bins[i - 1] + bins[i]) / 2.;
            }
        }
        else {
            List<double[]> c = new ArrayList<>();
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > 0) {
                    double b =  (bins[i] + bins[i + 1]) / 2.;
                    c.add(new double[] {b, counts[i]});
                }
            }
            for (int i = 0; i < c.size(); i++) {
                double[] temp = c.get(i);
                res[0][i] = temp[0];
                res[1][i] = temp[1];
            }
        }
        return res;
    }

    public double getHmax() {
        return hmax;
    }
    public double[] getBins() {
        return bins;
    }

    public double binMean(int interval) {

        int i1 = interval;
        int i2 = interval + 1;

        double md = (bins[i2] + bins[i1])/2.;
        System.out.println(interval + " " + md);
        return md;
    }

    protected void findDataMinMax() {

        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {

            double x = data[i];
            if (x > max) {
                max = x;
            }
            if ( x < min) {
                min = x;
            }
        }

        dataMin = min;
        dataMax = max;
        startx = dataMin - .01*(dataMax - dataMin);
        endx = dataMax + .01*(dataMax - dataMin);;
    }

    protected void makeBins() {

        double d = (endx - startx)/nbins;
        bins = new double[nbins + 1];

        for (int i = 0; i < bins.length; i++) {
            bins[i] = startx + i*d;
        }
    }

    public double getStartX() {
        return startx;
    }

    public double getEndX() {
        return endx;
    }

    public double getNBins() {
        return nbins;
    }
}

