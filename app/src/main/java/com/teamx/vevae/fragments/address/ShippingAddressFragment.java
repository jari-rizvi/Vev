package com.teamx.vevae.fragments.address;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.AddAddress;
import com.teamx.vevae.Networking.InternetConnection;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.UtilsKt;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.databinding.FragmentShippingAddressBinding;
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShippingAddressFragment extends BaseFragment {

    private FragmentShippingAddressBinding fragmentShippingAddressBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_shipping_address, container, false);

        fragmentShippingAddressBinding = FragmentShippingAddressBinding.inflate(inflater,container,false);

        fragmentShippingAddressBinding.btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    if (InternetConnection.checkConnection(requireContext())){
                        addAddress();
                    }else {
                        UtilsKt.snackbar(requireView(),"Check your internet connection");
                    }
                }

            }
        });

        fragmentShippingAddressBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popUpStack();
            }
        });


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {

            fragmentShippingAddressBinding.btnBack.setImageDrawable(getResources().getDrawable(R.drawable.arabic_screen_back, requireActivity().getTheme()));
            fragmentShippingAddressBinding.buttonBackImage.setImageDrawable(getResources().getDrawable(R.drawable.arabic_button_back, requireActivity().getTheme()));
            lang = LocaleManager.Companion.getLANGUAGE_ARABIC();

        }else{
            fragmentShippingAddressBinding.btnBack.setImageDrawable(getResources().getDrawable(R.drawable.back, requireActivity().getTheme()));
            fragmentShippingAddressBinding.buttonBackImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow, requireActivity().getTheme()));
            lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();

        }





        return fragmentShippingAddressBinding.getRoot();
    }

    private void addAddress() {

        String  houseAddress = fragmentShippingAddressBinding.houseAddress.getText().toString();
        String  streetAddress =  fragmentShippingAddressBinding.streetAddress.getText().toString();
        String  postalCode = fragmentShippingAddressBinding.postalCode.getText().toString();
//        String  countryName = fragmentShippingAddressBinding.countryName.getText().toString();
        String  provinceStateName =fragmentShippingAddressBinding.provinceStateName.getText().toString();
        String  cityname = fragmentShippingAddressBinding.cityname.getText().toString();

        progressbar("Adding address", "Please wait");
        WebServiceFactory.getInstance().addAddress(houseAddress,streetAddress,postalCode,cityname,provinceStateName,0,lang).enqueue(new Callback<AddAddress>() {
            @Override
            public void onResponse(Call<AddAddress> call, Response<AddAddress> response) {
                if (response.body().getFlag()==1){
                    UtilsKt.snackbar(requireView(),getString(R.string.address_added));
                    mProgressBar.dismiss();
                    popUpStack();
                }
                else {
                    String[] message = response.body().getMessage().split("\\.");
                    UtilsKt.snackbar(requireView(),message[0]);
                    mProgressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<AddAddress> call, Throwable t) {
                mProgressBar.dismiss();
            }
        });
    }

    private boolean validate(){

        if (fragmentShippingAddressBinding.houseAddress.getText().toString().isEmpty()){
            UtilsKt.snackbar(requireView(),getString(R.string.enter_house_address));
            return false;
        }

        if (fragmentShippingAddressBinding.streetAddress.getText().toString().isEmpty()){
            UtilsKt.snackbar(requireView(),getString(R.string.enter_street_address));
            return false;
        }

        if (fragmentShippingAddressBinding.postalCode.getText().toString().isEmpty()){
            UtilsKt.snackbar(requireView(),getString(R.string.enter_postal_code));
            return false;
        }


       /* if (fragmentShippingAddressBinding.countryName.getText().toString().isEmpty()){
            UtilsKt.snackbar(requireView(),getString(R.string.enter_country));
            return false;
        }*/

        if (fragmentShippingAddressBinding.provinceStateName.getText().toString().isEmpty()){
            UtilsKt.snackbar(requireView(),getString(R.string.state_province));
            return false;
        }


        if (fragmentShippingAddressBinding.cityname.getText().toString().isEmpty()){
            UtilsKt.snackbar(requireView(),getString(R.string.enter_city));
            return false;
        }



        return true;
    }
}