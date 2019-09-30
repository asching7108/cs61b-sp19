import org.knowm.xchart.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hug.
 * @author Hsingyi Lin (filled the methods)
 * date    09/29/2019
 */
public class Experiments {
    public static void experiment1() {
        Random seed = new Random();
        List<Double> yValues = new ArrayList<>();  // avg depth of BST
        List<Double> y2Values = new ArrayList<>();  // avg of optimal BST
        List<Integer> xValues = new ArrayList<>();  // size of BST
        BST<Integer> bst = new BST<>();
        int IPL = 0;
        for (int i = 1; i <= 5000; i++) {
            bst = ExperimentHelper.addRandomKey(bst, 100000);
            xValues.add(i);
            yValues.add(bst.avgDepth());
            y2Values.add(ExperimentHelper.optimalAverageDepth(i));
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("BST size").yAxisTitle("avg depth").build();
        chart.addSeries("random BST", xValues, yValues);
        chart.addSeries("optimal BST", xValues, y2Values);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment2() {
        Random seed = new Random();
        List<Double> yValues = new ArrayList<>();  // avg depth of BST
        List<Integer> xValues = new ArrayList<>();  //number of operations
        // BST initialization
        BST<Integer> bst = new BST<>();
        int IPL = 0;
        for (int i = 1; i <= 5000; i++) {
            bst = ExperimentHelper.addRandomKey(bst, 100000);
        }
        // operations of Knott's experiments
        for (int i = 1; i <= 100000; i++) {
            bst.deleteTakingSuccessor(bst.getRandomKey());
            bst = ExperimentHelper.addRandomKey(bst, 100000);
            xValues.add(i);
            yValues.add(bst.avgDepth());
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("num of operations").yAxisTitle("avg depth").build();
        chart.addSeries("avg depth", xValues, yValues);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment3() {
        Random seed = new Random();
        List<Double> yValues = new ArrayList<>();  // avg depth of BST
        List<Integer> xValues = new ArrayList<>();  //number of operations
        // BST initialization
        BST<Integer> bst = new BST<>();
        int IPL = 0;
        for (int i = 1; i <= 5000; i++) {
            bst = ExperimentHelper.addRandomKey(bst, 100000);
        }
        // operations of Knott's experiments
        for (int i = 1; i <= 100000; i++) {
            bst.deleteTakingRandom(bst.getRandomKey());
            bst = ExperimentHelper.addRandomKey(bst, 100000);
            xValues.add(i);
            yValues.add(bst.avgDepth());
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("num of operations").yAxisTitle("avg depth").build();
        chart.addSeries("avg depth", xValues, yValues);

        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) {
        experiment1();
        experiment2();
        experiment3();
    }
}
