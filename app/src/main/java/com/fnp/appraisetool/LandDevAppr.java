package com.fnp.appraisetool;

import android.util.Log;

import java.util.Arrays;

public class LandDevAppr {

    public static final int TAIPEI_CITY = 1;
    public static final int NEW_TAIPEI = 2;

    private static final int[] FLOOR_RANGE = new int[] {3, 30};
    private static final double[] TAIPEI_CITY_PRICE_STEP = new double[] { 10, 30, 45, 60, 75, 90,
            180, 1000 };
    private static final int[][] TAIPEI_CITY_PRICE_TABLE = new int[][] {
            new int[] { 45000, 55000, 55000, 70000, 72000, 75000, 77000, 80000, 82000, 84000,
                    87000, 89000, 92000, 101000, 104000, 106000, 109000, 111000, 114000, 117000,
                    119000, 122000, 124000, 127000, 130000, 132000, 135000, 137000 },
            new int[] { 55000, 65000, 65000, 77500, 80000, 82500, 85000, 87500, 90000, 92500,
                    95000, 97500, 100000, 101000, 104000, 106000, 109000, 111000, 114000, 117000,
                    119000, 122000, 124000, 127000, 130000, 132000, 135000, 137000 },
            new int[] { 60000, 70000, 70000, 87500, 90500, 93000, 95500, 98000, 101000, 104000,
                    106500, 109000, 111500, 114500, 117500, 120000, 122500, 125000, 128000, 131000,
                    133000, 136000, 138500, 141500, 144500, 147000, 149500, 152000 },
            new int[] { 67500, 80000, 80000, 97500, 100500, 103500, 106500, 109500, 112500, 115500,
                    118500, 121500, 124500, 127500, 130500, 133500, 136500, 139500, 142500, 145500,
                    148500, 151500, 154500, 157500, 160500, 163500, 166500, 169500 },
            new int[] { 72500, 90000, 90000, 110000, 114000, 118000, 122000, 126000, 130000,
                    134000, 138000, 142000, 146000, 150000, 154000, 158000, 162000, 166000, 170000,
                    174000, 178000, 182000, 186000, 190000, 194000, 198000, 202000, 206000 },
            new int[] { 77500, 95000, 95000, 115000, 119000, 123000, 127000, 131000, 135000,
                    139000, 143000, 147000, 151000, 155000, 159000, 163000, 167000, 171000, 175000,
                    179000, 183000, 187000, 191000, 195000, 199000, 203000, 207000, 211000 },
            new int[] { 90000, 110000, 110000, 130000, 135000, 140000, 145000, 150000, 155000,
                    160000, 165000, 170000, 175000, 180000, 185000, 190000, 195000, 200000, 205000,
                    210000, 215000, 220000, 225000, 230000, 235000, 240000, 245000, 250000 },
            new int[] { 90000, 110000, 110000, 130000, 135000, 140000, 145000, 150000, 155000,
                    160000, 165000, 170000, 175000, 180000, 185000, 190000, 195000, 200000, 205000,
                    210000, 215000, 220000, 225000, 230000, 235000, 240000, 245000, 250000 } };

    private static final double[] NEW_TAIPEI_PRICE_STEP = new double[] { 5, 15, 20, 30, 50, 90 };
    private static final int[][] NEW_TAIPEI_PRICE_TABLE = new int[][] {
            new int[] { 45000, 45000, 45000, 50000, 51300, 52600, 53900, 55200, 56500, 57800,
                    59100, 60400, 61700, 69000, 70400, 71800, 73200, 74600, 82500, 84000, 85500,
                    87000, 88500, 90000, 91500, 93000, 94500, 96000 },
            new int[] { 50500, 50500, 50500, 57500, 58850, 60200, 61550, 62900, 64250, 65600,
                    66950, 68300, 69650, 69000, 70400, 71800, 73200, 74600, 82500, 84000, 85500,
                    87000, 88500, 90000, 91500, 93000, 94500, 96000 },
            new int[] { 54500, 54500, 54500, 63000, 64450, 65900, 67350, 68800, 70250, 70200,
                    73150, 74600, 76050, 77500, 78950, 80400, 81850, 83300, 82500, 84000, 85500,
                    87000, 88500, 90000, 91500, 93000, 94500, 96000 },
            new int[] { 61000, 61000, 61000, 69500, 71050, 72600, 74150, 75700, 77250, 78800,
                    80350, 81900, 83450, 85000, 86550, 88100, 89650, 91200, 92750, 94300, 95850,
                    97400, 98950, 100500, 102050, 103600, 105150, 106700 },
            new int[] { 65000, 65000, 65000, 75000, 76600, 78200, 79800, 81400, 83000, 84600,
                    86200, 87800, 89400, 91000, 92600, 94200, 95800, 97400, 99000, 100600, 102200,
                    103800, 105400, 107000, 108600, 110200, 111800, 113400 },
            new int[] { 73000, 73000, 73000, 83000, 84600, 86200, 87800, 89400, 91000, 92600,
                    94200, 95800, 97400, 99000, 100000, 102200, 103800, 105400, 107000, 108600,
                    110200, 111800, 113400, 115000, 116600, 118200, 119800, 121400 } };


    static public LandDevResult calculate(int area, double housePrice,
                           double buildingParamBaseValue,
                           double buildingParamCurrentValue,
                           double floorAreaRate,
                           double elecEquip,
                           int _floor,
                           double floorAreaRateBonus) {
        double[] priceStep = null;
        int[][] priceTable = null;
        switch(area) {
            case TAIPEI_CITY:
                priceStep = TAIPEI_CITY_PRICE_STEP;
                priceTable = TAIPEI_CITY_PRICE_TABLE;
                break;
            case NEW_TAIPEI:
                priceStep = NEW_TAIPEI_PRICE_STEP;
                priceTable = NEW_TAIPEI_PRICE_TABLE;
                break;
        }

        int idx = Arrays.binarySearch(priceStep, housePrice);
        if (idx < 0) idx = -idx - 1;

        int floor = Math.min(Math.max(0, _floor - FLOOR_RANGE[0]), FLOOR_RANGE[1] - FLOOR_RANGE[0]);

        double buildFee = 0;
        if (idx == 0 || idx == priceStep.length) {
            idx = Math.min(priceStep.length - 1, idx);
            buildFee = priceTable[idx][floor];
        } else {
            double a1 = priceTable[idx - 1][floor];
            double a2 = priceTable[idx][floor];
            double b1 = priceStep[idx - 1];
            double b2 = priceStep[idx];
            buildFee = (housePrice-b1)/(b2-b1) * a2 + (b2-housePrice)/(b2-b1) * a1;
        }

        double refinedBuildFee = buildFee * buildingParamCurrentValue / buildingParamBaseValue;
        double landPrice = (housePrice / 1.3 - Math.round(refinedBuildFee / 100.0) / 100.0)
                * floorAreaRate * elecEquip * 1.15 * 0.3025 * 10000;

        double cityLandPrice = Math.round(landPrice * (1.0 + floorAreaRateBonus / 200.0));

        return new LandDevResult(
                (int) Math.round(refinedBuildFee / 100.0) * 100,
                (int) Math.round(landPrice),
                (int) Math.round(Math.round(landPrice) / 10.0) * 10,
                (int) Math.round(cityLandPrice),
                (int) Math.round(cityLandPrice / 10.0) * 10);
    }

    public static class LandDevResult {
        int buildingFee;
        int landPrice;
        int intLandPrice;
        int cityLandPrice;
        int intCityLandPrice;

        public LandDevResult(int bf, int lp, int ilp, int clp, int iclp) {
            buildingFee = bf;
            landPrice = lp;
            intLandPrice = ilp;
            cityLandPrice = clp;
            intCityLandPrice = iclp;
        }
    }
}
