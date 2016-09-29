package org.stowers.microscopy.utils;

/**
 * Created by cjw on 7/27/16.
 */

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;

import java.awt.*;
import java.util.Arrays;

public class HistogramPlot {

    JFrame frame;
    ChartPanel chartpanel;
    JFreeChart chart;

    Histogram h;

    boolean logY;

    String title = "Title";
    String xlabel = "X";
    String ylabel = "Y";

    public HistogramPlot(Histogram h) {
        this.h = h;
    }

//    public HistogramPlot(double[] d, int nbins) {
//        Histogram  h = new Histogram(d, nbins);
//    }

    public void plotHist() {
        frame = new JFrame("Histogram");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        makeChart(title, xlabel, ylabel);

        frame.add(chartpanel);
        frame.setSize(800, 600);
        frame.pack();
        frame.setVisible(true);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setXLabel(String xlabel) {
        this.xlabel = xlabel;
    }

    public void setYLabel(String ylabel) {
        this.ylabel = ylabel;
    }

    public ChartPanel makeChart(String title, String xlabel, String ylabel) {

        DefaultXYDataset dataset = new DefaultXYDataset();
        double[][] hdata = h.getHistogramArray(logY);

        dataset.addSeries("Hist", hdata);
        double barWidth = 0.8*(h.getEndX() - h.getStartX())/h.getNBins();
        XYBarDataset xybar = new XYBarDataset(dataset, barWidth);

        chart = ChartFactory.createXYBarChart(title, xlabel, false, ylabel, xybar);

        XYPlot plot = (XYPlot)chart.getPlot();

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setSeriesPaint(0, Color.orange);

        if (logY) {
            LogAxis log = new LogAxis("log(Counts)");
            log.setRange(.1, h.getHmax());
            plot.setRangeAxis(log);
        }
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setOutlineVisible(false);
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

        chartpanel = new ChartPanel(chart);


        return chartpanel;

    }

    public void setLogY(boolean logY) {
        this.logY = logY;
    }

    public static void main(String[] args) {

        double[] x = new double[100];
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < x.length; i++) {
//            x[i] = rnd.nextGaussian();
            x[i] = -Math.log(rnd.nextDouble());
        }


        Histogram h = new Histogram(Arrays.asList(x), 10);
        HistogramPlot p = new HistogramPlot(h);
//        p.setLogY(true);
//        p.plotHist();
        HistogramPlot p1 = new HistogramPlot(h);
        p1.setLogY(false);
        p1.plotHist();

    }

}
