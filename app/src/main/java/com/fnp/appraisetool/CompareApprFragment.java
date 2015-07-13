package com.fnp.appraisetool;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class CompareApprFragment extends Fragment implements TextWatcher {

    private EditText mComparePriceText;
    private EditText mSituationParamText;
    private EditText mDateParamText;
    private EditText mLocationParamText;
    private EditText mIdenticalParamText;
    private EditText mSizeParamPlusText;
    private EditText mSizeParamRealText;
    private EditText mFloorAreaRateBonusText;
    private EditText mCommonLoadingText;

    private TextView mLandPriceText;
    private TextView mCityLandPriceText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_compare_appr, container, false);

        mComparePriceText = (EditText) mainView.findViewById(R.id.compare_price);
        mSituationParamText = (EditText) mainView.findViewById(R.id.situation_param);
        mDateParamText = (EditText) mainView.findViewById(R.id.data_param);
        mLocationParamText = (EditText) mainView.findViewById(R.id.location_param);
        mIdenticalParamText = (EditText) mainView.findViewById(R.id.identical_param);
        mSizeParamPlusText = (EditText) mainView.findViewById(R.id.size_param_plus);
        mSizeParamRealText = (EditText) mainView.findViewById(R.id.size_param_real);
        mFloorAreaRateBonusText = (EditText) mainView.findViewById(R.id.floor_area_rate_bonus);
        mCommonLoadingText = (EditText) mainView.findViewById(R.id.common_loading);

        mLandPriceText = (TextView) mainView.findViewById(R.id.land_price);
        mCityLandPriceText = (TextView) mainView.findViewById(R.id.city_land_price);

        mComparePriceText.addTextChangedListener(this);
        mSituationParamText.addTextChangedListener(this);
        mDateParamText.addTextChangedListener(this);
        mLocationParamText.addTextChangedListener(this);
        mIdenticalParamText.addTextChangedListener(this);
        mSizeParamPlusText.addTextChangedListener(this);
        mSizeParamRealText.addTextChangedListener(this);
        mFloorAreaRateBonusText.addTextChangedListener(this);
        mCommonLoadingText.addTextChangedListener(this);

        return mainView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        try {
            double comparePrice = Double.parseDouble(mComparePriceText.getText().toString());
            double situationParam = Double.parseDouble(mSituationParamText.getText().toString());
            double dateParam = Double.parseDouble(mDateParamText.getText().toString());
            double locationParam = Double.parseDouble(mLocationParamText.getText().toString());
            double identicalParam = Double.parseDouble(mIdenticalParamText.getText().toString());
            double sizeParamPlus = Double.parseDouble(mSizeParamPlusText.getText().toString());
            double sizeParamReal = Double.parseDouble(mSizeParamRealText.getText().toString());

            double result = comparePrice
                    * (1 + situationParam / 100)
                    * (1 + dateParam / 100)
                    * (1 + locationParam / 100)
                    * (1 + identicalParam / 100)
                    * sizeParamReal
                    / sizeParamPlus;
            int resultInt = (int) (Math.round(Math.round(result) / 10.0) * 10);
            mLandPriceText.setText(String.format("$%,8d", resultInt));

            try {
                double floorAreaBonux = Double.parseDouble(mFloorAreaRateBonusText.getText().toString());
                double commonLoading = Double.parseDouble(mCommonLoadingText.getText().toString());
                double cityRenew = result * (1 + floorAreaBonux / 100 * (1 - commonLoading / 100));
                int cityRenewInt = (int) (Math.round(Math.round(cityRenew) / 10.0) * 10);
                mCityLandPriceText.setText(String.format("$%,8d", cityRenewInt));
            } catch (NumberFormatException e) {
                mCityLandPriceText.setText(R.string.not_a_number);
            }
        } catch (NumberFormatException e) {
            mLandPriceText.setText(getText(R.string.not_a_number));
        }
    }
}
