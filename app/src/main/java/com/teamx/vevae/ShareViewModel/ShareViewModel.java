package com.teamx.vevae.ShareViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teamx.vevae.Models.UserDetails.UserDetails;
import com.teamx.vevae.Models.storeprofile.Data;
import com.teamx.vevae.Models.storeprofile.StoreProfile;
import com.teamx.vevae.repository.ApiRepository;

public class ShareViewModel extends ViewModel {


    ApiRepository repository ;
    MutableLiveData<UserDetails> userDetailData;
    MutableLiveData<StoreProfile> storeProfile;
    private final MutableLiveData<Data> storeData = new MutableLiveData<>();
    MutableLiveData<String> s = new MutableLiveData<>("Yes");




    public ShareViewModel(){

        repository = new ApiRepository();
    }

    public LiveData<UserDetails> getUserDetails(){

        if (userDetailData == null){

            userDetailData = repository.getUserDetails();

        }

        return userDetailData;
    }


  /*  public LiveData<StoreProfile> getStoreProfile(int storeId){

        if (storeProfile == null){

            storeProfile = repository.getStoreProfile(storeId);
        }

        return storeProfile;
    }
*/


    public void setStringValue(String str){
        s.setValue(str);
    }
    public  MutableLiveData<String> getStringValue(){
        return s;
    }




    public void setStoreProfileData(Data data) {
       storeData.setValue(data);
    }

    public MutableLiveData<Data> getStoreData() {
        return storeData;
    }
}
