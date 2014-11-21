package com.fnp.appraisetool;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LandDevApprFragment extends Fragment implements TextWatcher, OnClickListener {

    EditText mElecEquipText;
    EditText mFloorText;
    EditText mFloorAreaRateText;
    EditText mBuildingPriceText;
    EditText mFloorAreaRateBonusText;
    EditText mBuildingParamBaseValueText;
    EditText mBuildingParamCurrentValueText;

    Button mBuildingParamUpdateButton;

    TextView mBuildingParamBaseTimeText;
    TextView mBuildingParamCurrentTimeText;

    TextView mBuildingFeeText;
    TextView mLandPriceText;
    TextView mIntLandPriceText;
    TextView mCityLandPriceText;
    TextView mCityIntLandPriceText;

    float mBuildingParamBaseValue;
    float mBuildingParamCurrentValue;
    String mBuildingParamBaseTime;
    String mBuildingParamCurrentTime;

    private static final String PREF_NAME = "LAND_DEV_APPR_PREF";

    private static final String PREF_BUILD_PARAM_BASE_VALUE = "BUILDING_PARAM_BASE_VALUE";
    private static final String PREF_BUILD_PARAM_CURRENT_VALUE = "BUILDING_PARAM_CURRENT_VALUE";
    private static final String PREF_BUILD_PARAM_BASE_TIME = "BUILDING_PARAM_BASE_TIME";
    private static final String PREF_BUILD_PARAM_CURRENT_TIME = "BUILDING_PARAM_CURRENT_TIME";

    float mElecEquip = 0;
    int mFloor = 0;
    float mFloorAreaRate = 0;
    float mBuildingPrice = 0;
    float mFloorAreaRateBonus = 0;

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
        edit.putFloat(PREF_BUILD_PARAM_BASE_VALUE, mBuildingParamBaseValue);
        edit.putFloat(PREF_BUILD_PARAM_CURRENT_VALUE, mBuildingParamCurrentValue);
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
        } else if (s == mElecEquipText.getEditableText()) {
            mElecEquip = Float.parseFloat(mElecEquipText.getText().toString());
        } else if (s == mFloorText.getEditableText()) {
            mFloor = Integer.parseInt(mFloorText.getText().toString());
        } else if (s == mFloorAreaRateText.getEditableText()) {
            mFloorAreaRate = Float.parseFloat(mFloorAreaRateText.getText().toString());
        } else if (s == mBuildingPriceText.getEditableText()) {
            mBuildingPrice = Float.parseFloat(mBuildingPriceText.getText().toString());
        } else if (s == mFloorAreaRateBonusText.getEditableText()) {
            mFloorAreaRateBonus = Float.parseFloat(mFloorAreaRateBonusText.getText().toString());
        }

        calculate();
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

    private void calculate() {

    }

}
