package com.fnp.appraisetool;

import java.io.IOException;
import java.io.InterruptedIOException;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

public class LandDevApprFragment extends Fragment implements TextWatcher, OnClickListener,
OnItemSelectedListener {

    private EditText mElecEquipText;
    private EditText mFloorText;
    private EditText mFloorAreaRateText;
    private EditText mHousePriceText;
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
    private double mHousePrice = 0;
    private double mFloorAreaRateBonus = 0;

    private int mArea = LandDevAppr.TAIPEI_CITY;

    private boolean mIsDownloading = false;


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
        mHousePriceText = (EditText) mainView.findViewById(R.id.building_price);
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
        mHousePriceText.addTextChangedListener(this);
        mFloorAreaRateBonusText.addTextChangedListener(this);

        downloadBuildingParam();

        return mainView;
    }

    private void getBuildingParamFromLocal() {
        SharedPreferences pref = getActivity()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Resources res = getActivity().getResources();
        mBuildingParamBaseValueText.setText(String.valueOf(
                pref.getFloat(PREF_BUILD_PARAM_BASE_VALUE,
                Float.parseFloat(res.getString(R.string.default_building_param_base_value)))));
        mBuildingParamCurrentValueText.setText(String.valueOf(
                pref.getFloat(PREF_BUILD_PARAM_CURRENT_VALUE,
                Float.parseFloat(res.getString(R.string.default_building_param_curr_value)))));
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
        mBuildingParamBaseTimeText.setText(res.getString(R.string.base_value) + " ("
                + String.valueOf(mBuildingParamBaseTime) + ")");
        mBuildingParamCurrentTimeText.setText(res.getString(R.string.current_value) + " ("
                + String.valueOf(mBuildingParamCurrentTime) + ")");
    }

    private void downloadBuildingParam() {
        getBuildingParamFromLocal();
        updateBuildingParamView();

        if (mIsDownloading) return;

        mIsDownloading = true;
        final AsyncTask<Void, Void, BuildingParamApi.BuildingParam> task
                = new AsyncTask<Void, Void, BuildingParamApi.BuildingParam>() {
            String errorMsg = null;
            ProgressDialog mProgress = null;

            @Override
            protected void onPreExecute() {
                mProgress = ProgressDialog.show(getActivity(), "",getString(R.string.updating));
                mProgress.setCancelable(true);
                mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                    }
                });
            }

            @Override
            protected BuildingParamApi.BuildingParam doInBackground(Void... params) {
                BuildingParamApi api = new BuildingParamApi();
                try {
                    return api.getLastestParam();
                } catch (BuildingParamApi.ParsingErrorException e) {
                    errorMsg = getString(R.string.parsing_err);
                } catch (IOException e) {
                    if (e instanceof InterruptedIOException) return null;
                    errorMsg = getString(R.string.network_err);
                }
                return null;
            }

            @Override
            protected void onPostExecute(BuildingParamApi.BuildingParam res) {
                mIsDownloading = false;
                mProgress.dismiss();
                if (res == null && errorMsg != null) {
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                mBuildingParamCurrentTime = String.format("%4d.%2d", res.year, res.month);
                mBuildingParamCurrentTimeText.setText(mBuildingParamCurrentTime);
                mBuildingParamCurrentValueText.setText(String.valueOf((float) res.buildingParam));

                saveBuildingParam();
                updateBuildingParamView();
            }
        };
        task.execute();
    }

    @Override
    public void afterTextChanged(Editable s) {
        Resources res = getActivity().getResources();
        try {
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
        } catch (NumberFormatException e) {}

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
            mHousePrice = Float.parseFloat(mHousePriceText.getText().toString());
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
        LandDevAppr.LandDevResult res = LandDevAppr.calculate(mArea, mHousePrice,
                mBuildingParamBaseValue, mBuildingParamCurrentValue,
                mFloorAreaRate, mElecEquip, mFloor, mFloorAreaRateBonus);

        mBuildingFeeText.setText(String.format("$%,8d", res.buildingFee));
        mLandPriceText.setText(String.format("$%,8d", res.landPrice));
        mIntLandPriceText.setText(String.format("$%,8d", res.intLandPrice));

        mCityLandPriceText.setText(String.format("$%,8d", res.cityLandPrice));
        mCityIntLandPriceText.setText(String.format("$%,8d", res.intCityLandPrice));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
        switch (position) {
            case 0:
                mArea = LandDevAppr.TAIPEI_CITY;
                break;
            case 1:
                mArea = LandDevAppr.NEW_TAIPEI;
                break;
        }
        if (validate()) calculate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {}

}
