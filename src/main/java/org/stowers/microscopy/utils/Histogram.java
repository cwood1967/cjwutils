package org.stowers.microscopy.utils;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import net.imglib2.Cursor;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Created by cjw on 7/27/16.
 */
public class Histogram  < T extends RealType< T> & NativeType< T >> {

    double[] counts;
    double[] bins;
    double[] data;

    Iterable< T > idata;

    int nbins;
    double dataMin;
    double dataMax;
    double startx;
    double endx;
    double hmax;

    public Histogram(double[] data, int xnbins) {

        this.idata = arrayToList(data);
        this.nbins = xnbins;
        this.run();

    }

    public Histogram(Iterable< T > idata, int xnbins) {

        this.idata = idata;
        this.nbins = xnbins;
        this.run();
    }

    protected void run() {
        findDataMinMax();
        makeBins();
        counts = new double[nbins];
        System.out.println(dataMin + " " + dataMax);
        doHist();
    }

    protected ArrayList<T> arrayToList(double[] data) {
        ArrayList< T > list = new ArrayList<>();
        for (int i =0; i < data.length; i++) {
            list.add((T)(new DoubleType(data[i])));
        }

        return list;
    }
    public double[] doHist() {

        for (final T type : idata) {

            double x = type.getRealDouble();
            int k = (int)(nbins*(x - startx)/(endx - startx));
            try {
                counts[k]++;
            }
            catch (Exception e) {
                System.out.println(x + " " + k + " " + startx + " " + endx);
            }
        }
//        for (int i = 0; i < data.length; i++) {
//
//            double x = data[i].getRealDouble();
//            int k = (int)(nbins*(x - startx)/(endx - startx));
//            try {
//                counts[k]++;
//            }
//            catch (Exception e) {
//                System.out.println(x + " " + k + " " + startx + " " + endx);
//            }
////            System.out.println(x + " " + k);
//        }

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

        Double min = Double.MAX_VALUE;
        Double max = -Double.MAX_VALUE;

        final Iterator< T > iterator = idata.iterator();

        T type = iterator.next();
        T tmin = type.createVariable();
        T tmax = tmin.copy();

        tmin.set(type);
        tmax.set(type);

        while (iterator.hasNext()) {
            type = iterator.next();

            if (type.compareTo(tmin) < 0) {
                tmin.set(type);
            }
            if (type.compareTo(tmax) > 0) {
                tmax.set(type);
            }
        }

        dataMin = tmin.getRealDouble();
        dataMax = tmax.getRealDouble();
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

