package com.fnp.appraisetool;

import java.util.Arrays;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LandDevApprFragment extends Fragment implements TextWatcher, OnClickListener,
OnItemSelectedListener {

    private EditText mElecEquipText;
    private EditText mFloorText;
    private EditText mFloorAreaRateText;
    private EditText mBuildingPriceText;
    private EditText mFloorAreaRateBonusText;
    private EditText mBuildingParamBaseValueText;
    private EditText mBuildingParamCurrentValueText;

    private Button mBuildingParamUpdateButton;

    private TextView mBuildingParamBaseTimeText;
    private TextView mBuildingParamCurrentTimeText;

    private TextView mBuildingFeeText;
    private TextView mLandPriceText;
    private TextView mIntLandPriceText;
    private TextView mCityLandPriceText;
    private TextView mCityIntLandPriceText;

    private Spinner mSpinner;

    private float mBuildingParamBaseValue;
    private float mBuildingParamCurrentValue;
    private String mBuildingParamBaseTime;
    private String mBuildingParamCurrentTime;

    private static final String PREF_NAME = "LAND_DEV_APPR_PREF";

    private static final String PREF_BUILD_PARAM_BASE_VALUE = "BUILDING_PARAM_BASE_VALUE";
    private static final String PREF_BUILD_PARAM_CURRENT_VALUE = "BUILDING_PARAM_CURRENT_VALUE";
    private static final String PREF_BUILD_PARAM_BASE_TIME = "BUILDING_PARAM_BASE_TIME";
    private static final String PREF_BUILD_PARAM_CURRENT_TIME = "BUILDING_PARAM_CURRENT_TIME";

    private double mElecEquip = 0;
    private int mFloor = 0;
    private double mFloorAreaRate = 0;
    private double mBuildingPrice = 0;
    private double mFloorAreaRateBonus = 0;

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

    private double[] mPriceStep = TAIPEI_CITY_PRICE_STEP;
    private int[][] mPriceTable = TAIPEI_CITY_PRICE_TABLE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_land_dev_appr, container, false);

        mBuildingParamBaseValueText = (EditText) mainView
                .findViewById(R.id.building_param_base_value);
        mBuildingParamCurrentValueText = (EditText) mainView
                .findViewById(R.id.building_param_current_value);
        mBuildingParamBaseTimeText = (TextView) mainView
                .findViewById(R.id.building_param_base_time);
        mBuildingParamCurrentTimeText = (TextView) mainView
                .findViewById(R.id.building_param_current_time);

        mBuildingParamUpdateButton = (Button) mainView
                .findViewById(R.id.building_param_update_button);

        mElecEquipText = (EditText) mainView.findViewById(R.id.elec_equipment);
        mFloorText = (EditText) mainView.findViewById(R.id.floor);
        mFloorAreaRateText = (EditText) mainView.findViewById(R.id.floor_area_rate);
        mBuildingPriceText = (EditText) mainView.findViewById(R.id.building_price);
        mFloorAreaRateBonusText = (EditText) mainView.findViewById(R.id.floor_area_rate_bonus);

        mBuildingFeeText = (TextView) mainView.findViewById(R.id.building_fee);
        mLandPriceText = (TextView) mainView.findViewById(R.id.land_price);
        mIntLandPriceText = (TextView) mainView.findViewById(R.id.int_land_price);
        mCityLandPriceText = (TextView) mainView.findViewById(R.id.city_land_price);
        mCityIntLandPriceText = (TextView) mainView.findViewById(R.id.city_int_land_price);

        mSpinner = (Spinner) mainView.findViewById(R.id.location_spinner);
        mSpinner.setOnItemSelectedListener(this);

        mBuildingParamUpdateButton.setOnClickListener(this);

        mBuildingParamBaseValueText.addTextChangedListener(this);
        mBuildingParamCurrentValueText.addTextChangedListener(this);
        mElecEquipText.addTextChangedListener(this);
        mFloorText.addTextChangedListener(this);
        mFloorAreaRateText.addTextChangedListener(this);
        mBuildingPriceText.addTextChangedListener(this);
        mFloorAreaRateBonusText.addTextChangedListener(this);

        getBuildingParamFromLocal();
        updateBuildingParamView();

        return mainView;
    }

    private void getBuildingParamFromLocal() {
        SharedPreferences pref = getActivity()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Resources res = getActivity().getResources();
        mBuildingParamBaseValue = pref.getFloat(PREF_BUILD_PARAM_BASE_TIME,
                Float.parseFloat(res.getString(R.string.default_building_param_base_value)));
        mBuildingParamCurrentValue = pref.getFloat(PREF_BUILD_PARAM_CURRENT_VALUE,
                Float.parseFloat(res.getString(R.string.default_building_param_curr_value)));
        mBuildingParamBaseTime = pref.getString(PREF_BUILD_PARAM_BASE_TIME,
                res.getString(R.string.default_building_param_base_time));
        mBuildingParamCurrentTime = pref.getString(PREF_BUILD_PARAM_CURRENT_TIME,
                res.getString(R.string.default_building_param_curr_time));
    }

    private void saveBuildingParam() {
        SharedPreferences pref = getActivity()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putFloat(PREF_BUILD_PARAM_BASE_VALUE, (float) mBuildingParamBaseValue);
        edit.putFloat(PREF_BUILD_PARAM_CURRENT_VALUE, (float) mBuildingParamCurrentValue);
        edit.putString(PREF_BUILD_PARAM_BASE_TIME, mBuildingParamBaseTime);
        edit.putString(PREF_BUILD_PARAM_CURRENT_TIME, mBuildingParamCurrentTime);
        edit.commit();
    }

    private void updateBuildingParamView() {
        Resources res = getActivity().getResources();
        mBuildingParamBaseValueText.setText(String.valueOf(mBuildingParamBaseValue));
        mBuildingParamCurrentValueText.setText(String.valueOf(mBuildingParamCurrentValue));
        mBuildingParamBaseTimeText.setText(res.getString(R.string.base_value) + " ("
                + String.valueOf(mBuildingParamBaseTime) + ")");
        mBuildingParamCurrentTimeText.setText(res.getString(R.string.current_value) + " ("
                + String.valueOf(mBuildingParamCurrentTime) + ")");
    }

    private void downloadBuildingParam() {
        getBuildingParamFromLocal();
        updateBuildingParamView();
    }

    @Override
    public void afterTextChanged(Editable s) {

        Resources res = getActivity().getResources();
        if (s == mBuildingParamBaseValueText.getEditableText()) {
            mBuildingParamBaseValue = Float.valueOf(mBuildingParamBaseValueText.getText()
                    .toString());
            mBuildingParamBaseTimeText.setText(res.getString(R.string.base_value) + " ("
                    + res.getString(R.string.manual_typed) + ")");
        } else if (s == mBuildingParamCurrentValueText.getEditableText()) {
            mBuildingParamCurrentValue = Float.valueOf(mBuildingParamCurrentValueText.getText()
                    .toString());
            mBuildingParamCurrentTimeText.setText(res.getString(R.string.current_value) + " ("
                    + res.getString(R.string.manual_typed) + ")");
        }

        if (validate()) {
            calculate();
        } else {
            String notANum = res.getString(R.string.not_a_number);
            mBuildingFeeText.setText(notANum);
            mLandPriceText.setText(notANum);
            mIntLandPriceText.setText(notANum);
            mCityLandPriceText.setText(notANum);
            mCityIntLandPriceText.setText(notANum);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void onClick(View v) {
        downloadBuildingParam();
    }

    private boolean validate() {
        try {
            mBuildingParamBaseValue = Float.valueOf(mBuildingParamBaseValueText.getText()
                    .toString());
            mBuildingParamCurrentValue = Float.valueOf(mBuildingParamCurrentValueText.getText()
                    .toString());
            mElecEquip = Float.parseFloat(mElecEquipText.getText().toString());
            mFloor = Integer.parseInt(mFloorText.getText().toString());
            mFloorAreaRate = Float.parseFloat(mFloorAreaRateText.getText().toString());
            mBuildingPrice = Float.parseFloat(mBuildingPriceText.getText().toString());
        } catch (NumberFormatException e) {
            mFloorAreaRateBonus = 0;
            return false;
        }

        try {
            mFloorAreaRateBonus = Float.parseFloat(mFloorAreaRateBonusText.getText().toString());
        } catch (NumberFormatException e) {
            mFloorAreaRateBonus = 0;
        }
        return mBuildingParamBaseValue != 0;
    }

    private void calculate() {

        int idx = Arrays.binarySearch(mPriceStep, mBuildingPrice);
        if (idx < 0) idx = -idx - 1;

        int floor = Math.min(Math.max(0, mFloor - FLOOR_RANGE[0]), FLOOR_RANGE[1] - FLOOR_RANGE[0]);

        double buildFee = 0;
        if (idx == 0 || idx == mPriceStep.length) {
            idx = Math.min(mPriceStep.length - 1, idx);
            buildFee = mPriceTable[idx][floor];
        } else {
            double a1 = mPriceTable[idx - 1][floor];
            double a2 = mPriceTable[idx][floor];
            double b1 = mPriceStep[idx - 1];
            double b2 = mPriceStep[idx];
            buildFee = (mBuildingPrice-b1)/(b2-b1) * a2 + (b2-mBuildingPrice)/(b2-b1) * a1;
            Log.e("test", a1 + " " + a2);
        }

        double refinedBuildFee = buildFee * mBuildingParamCurrentValue / mBuildingParamBaseValue;
        double landPrice = (mBuildingPrice / 1.3 - Math.round(refinedBuildFee / 100.0) / 100.0)
                * mFloorAreaRate * mElecEquip * 1.15 * 0.3025 * 10000;

        mBuildingFeeText.setText(String.format("$%,8d", Math.round(refinedBuildFee / 100.0) * 100));
        mLandPriceText.setText(String.format("$%,8d", Math.round(landPrice)));
        mIntLandPriceText.setText(String.format("$%,8d",
                Math.round(Math.round(landPrice) / 10.0) * 10));

        Log.e("test", landPrice + "");
        double cityLandPrice = Math.round(landPrice * (1.0 + mFloorAreaRateBonus / 200.0));
        mCityLandPriceText.setText(String.format("$%,8d", Math.round(cityLandPrice)));
        mCityIntLandPriceText
                .setText(String.format("$%,8d", Math.round(cityLandPrice / 10.0) * 10));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
        switch (position) {
            case 0:
                mPriceStep = TAIPEI_CITY_PRICE_STEP;
                mPriceTable = TAIPEI_CITY_PRICE_TABLE;
                break;
            case 1:
                mPriceStep = NEW_TAIPEI_PRICE_STEP;
                mPriceTable = NEW_TAIPEI_PRICE_TABLE;
                break;
        }
        if (validate()) calculate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

}
