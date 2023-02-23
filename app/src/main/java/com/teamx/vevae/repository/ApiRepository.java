package com.teamx.vevae.repository;

import androidx.lifecycle.MutableLiveData;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.UserDetails.UserDetails;
import com.teamx.vevae.Models.storeprofile.StoreProfile;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository extends BaseFragment {



    public MutableLiveData<UserDetails> getUserDetails(){

        if (App.localeManager.getLanguage() != LocaleManager.Companion.getLANGUAGE_ENGLISH()) {
            lang = LocaleManager.Companion.getLANGUAGE_ARABIC();
        } else {
            lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();
        }
        final  MutableLiveData<UserDetails> userDetails = new MutableLiveData<>();

        WebServiceFactory.getInstance().getUserDetail("lang").enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.body().getFlag()==1){

                    userDetails.setValue(response.body());

                }else {

                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {

            }
        });
        return userDetails;
    }



    public MutableLiveData<StoreProfile> getStoreProfile(int storeId ){

        final MutableLiveData<StoreProfile> storeProfileMutableLiveData = new MutableLiveData<>();

      /*  WebServiceFactory.getInstance().getStoreDetails(storeId,"product").enqueue(new Callback<StoreProfile>() {
            @Override
            public void onResponse(Call<StoreProfile> call, Response<StoreProfile> response) {
                if (response.body().getFlag()==1){
                    storeProfileMutableLiveData.setValue(response.body());
                }else {

                }
            }

            @Override
            public void onFailure(Call<StoreProfile> call, Throwable t) {

            }
        });*/

        return storeProfileMutableLiveData;
    }


}
