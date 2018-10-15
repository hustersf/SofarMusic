package com.sf.demo.view.linechart;

/**
 * Created by sufan on 17/7/7.
 */

public class BillUtil {

    public static float getMaxBillValue(float[] bills) {
        if (bills.length < 1) {
            throw new IllegalArgumentException("bill's array length should > 0");
        }

        if (bills.length == 1) {
            return bills[0];
        }

        float maxBillValue = bills[0];

        for (int i = 1; i < bills.length; i++) {
            if (bills[i] > maxBillValue) {
                maxBillValue = bills[i];
            }
        }

        return maxBillValue;
    }

    public static float getMinBillValue(float[] bills) {
        if (bills.length < 1) {
            throw new IllegalArgumentException("bill's array length should > 0");
        }

        if (bills.length == 1) {
            return bills[0];
        }

        float minBillValue = bills[0];

        for (int i = 1; i < bills.length; i++) {
            if (bills[i] < minBillValue) {
                minBillValue = bills[i];
            }
        }

        return minBillValue;
    }
}
