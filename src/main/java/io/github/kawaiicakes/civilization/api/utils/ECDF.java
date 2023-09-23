package io.github.kawaiicakes.civilization.api.utils;

import java.util.Arrays;

/**
 * An ECDF (empirical cumulative distributive function). This mod uses these to empirically determine how much a city
 * should receive in terms of production - among other stats - based on how well the server as a whole is doing.
 */
public class ECDF {
    private final double[] values;

    public ECDF(double[] values) {
        this.values = values;
    }

    public double getMean() {
        return ((Arrays.stream(this.values).sum()) / this.values.length);
    }

    public double getMedian() {
        boolean isEven = (this.values.length % 2) == 0;

        return isEven
                ? (this.values[(this.values.length / 2)] + this.values[(this.values.length / 2) + 1]) / 2
                : this.values[(this.values.length / 2)];
    }

    public double getVariance() {
        return Arrays.stream(this.values).map(i -> Math.pow(i - this.getMean(), 2)).sum() / this.values.length;
    }

    public double getStdDev() {
        return Math.sqrt(this.getVariance());
    }

    /**
     * The quantile measures as a percentage how many of the data points given in <code>this.values</code>
     * fall under it. For example, the 79th quantile is the point where 79% of the data falls underneath.
     * @param quantile the quantile to look for data under. It MUST be a value from 0.0 to 1.0 inclusive.
     * @return  if nq is an integer, an array of 2 <code>double</code>s representing the range of values (exclusive)
     *          in <code>this.values</code> under which it is said the quantile percentage of data falls. It is
     *          ordered from lowest to highest bound at index 0 and 1 respectively.
     *          if nq is not an integer, an array of 1 <code>double</code> representing the value under which it
     *          is said the quantile percentage of data falls.
     * @throws IllegalArgumentException if the quantile is not a value between 0.00 and 1.00 inclusive.
     */
    public double[] getQuantile(double quantile) throws IllegalArgumentException {
        if (quantile < 0.00 || quantile > 1.00) {
            throw new IllegalArgumentException("Quantile must be between 0.00 and 1.00 inclusive!");
        }

        boolean nqIsInt = (this.values.length * quantile) == (int)(this.values.length * quantile);

        return nqIsInt
                ? new double[]{this.values[(int) (this.values.length * quantile)], this.values[this.doesNotExceedLength((this.values.length * quantile) + 1)]}
                : new double[]{this.values[(int) Math.ceil((this.values.length * quantile))]};
    }

    private int doesNotExceedLength(double value) {
        return value <= this.values.length ? (int) value : this.values.length;
    }
}
