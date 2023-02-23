package com.teamx.vevae.fragments.navfragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.DeleteSearch;
import com.teamx.vevae.Models.Logout;
import com.teamx.vevae.Models.recentsearch.RecentSearch;
import com.teamx.vevae.Networking.InternetConnection;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.UtilsKt;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.activities.MainActivity;
import com.teamx.vevae.adapter.RecentlyViewedAdapter;
import com.teamx.vevae.adapter.SearchResultAdapter;
import com.teamx.vevae.databinding.FragmentSearchBinding;
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment;
import com.teamx.vevae.onClick.OnOrderClickListener;
import com.teamx.vevae.onClick.OnSearchResultClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends BaseFragment implements OnOrderClickListener, OnSearchResultClickListener {

    private FragmentSearchBinding fragmentSearchBinding;
    private RecentlyViewedAdapter  recentlyViewedAdapter;
    private  SearchResultAdapter searchResultAdapter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double currentLat = 0;
    private double currentLng = 0;
    String searchText ;
    private RecentSearch recentSearch;
    private com.teamx.vevae.Models.search.SearchResult searchResult;
    private String userToken;


    String [] tags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false);
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater,container,false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {

                  lang = LocaleManager.Companion.getLANGUAGE_ARABIC();

        }else{
           lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();

        }


        userToken = Prefs.getString("userToken","");
        if (!userToken.isEmpty()){
            fragmentSearchBinding.btnNotification.setVisibility(View.VISIBLE);
            fragmentSearchBinding.recentlyViewedlayout.setVisibility(View.VISIBLE);
            fragmentSearchBinding.notificationCount.setVisibility(View.VISIBLE);
        }else {
            fragmentSearchBinding.btnNotification.setVisibility(View.GONE);
            fragmentSearchBinding.recentlyViewedlayout.setVisibility(View.GONE);
            fragmentSearchBinding.notificationCount.setVisibility(View.GONE);
        }

        ((MainActivity)getActivity()).showNavBar();



        if (InternetConnection.checkConnection(requireContext())){


            if (!userToken.isEmpty()){
                getRecentSearch();
            }
            getPermission();

        }else {
            UtilsKt.snackbar(requireView(),getString(R.string.please_check_your_internet_connection));
        }

        fragmentSearchBinding.btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.notificationFragment, null);
            }
        });


       fragmentSearchBinding.searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   if (InternetConnection.checkConnection(requireContext())){
                       if (isValidate()){
                           fragmentSearchBinding.recommendedLayout.setVisibility(View.GONE);
                           fragmentSearchBinding.recentlyViewedlayout.setVisibility(View.GONE);
                           fragmentSearchBinding.resultRecyclerview.setVisibility(View.VISIBLE);

                           searchText  = fragmentSearchBinding.searchText.getText().toString();
                           getSearchResult(searchText);
                       }
                   }else {
                       UtilsKt.snackbar(requireView(),getString(R.string.please_check_your_internet_connection));
                   }
                   return true;
               }
               return false;
           }

        });


        fragmentSearchBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(requireContext())){
                    if (isValidate()){
                        fragmentSearchBinding.recommendedLayout.setVisibility(View.GONE);
                        fragmentSearchBinding.recentlyViewedlayout.setVisibility(View.GONE);
                        fragmentSearchBinding.resultRecyclerview.setVisibility(View.VISIBLE);

                        searchText  = fragmentSearchBinding.searchText.getText().toString();
                        getSearchResult(searchText);
                    }
                }else {
                    UtilsKt.snackbar(requireView(),getString(R.string.please_check_your_internet_connection));
                }
            }
        });




        //chipGroup();

     /*   fragmentSearchBinding.

     .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<fragmentSearchBinding.chipGroup.getChildCount();i++){
                    Chip chip = (Chip)fragmentSearchBinding.chipGroup.getChildAt(i);
                    showToast(chip.getText().toString());
                }
            }
        });*/




        fragmentSearchBinding.btnClearHistory.setOnClickListener(v -> {

            if (InternetConnection.checkConnection(requireContext())){

                deleteHistory();

            }else {
                UtilsKt.snackbar(requireView(),getString(R.string.please_check_your_internet_connection));
            }
        });


        return fragmentSearchBinding.getRoot();

    }

    private void deleteHistory() {

        progressbar("Delete","please wait");
        WebServiceFactory.getInstance().deleteSearchHistory().enqueue(new Callback<DeleteSearch>() {
            @Override
            public void onResponse(Call<DeleteSearch> call, Response<DeleteSearch> response) {
                if (response.body().getFlag()==1){
                    recentSearch.getData().clear();
                    fragmentSearchBinding.recentlyViewedlayout.setVisibility(View.GONE);
                    mProgressBar.dismiss();
                    UtilsKt.snackbar(requireView(), getString(R.string.deleted));
                }else {
                    UtilsKt.snackbar(requireView(),response.body().getMessage());
                    mProgressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DeleteSearch> call, Throwable t) {
                mProgressBar.dismiss();
            }
        });
    }

    private void getRecentSearch() {

        WebServiceFactory.getInstance().getRecentSearch(lang).enqueue(new Callback<RecentSearch>() {
            @Override
            public void onResponse(Call<RecentSearch> call, Response<RecentSearch> response) {
                if (response.code()==401){
                    showToast(getString(R.string.session_expired));
                    clearFragmentsLogout();
                }else {
                    if (response.body().getFlag()==1){
                        recentSearch = response.body();

                        if (response.body().getData() != null){
                            fragmentSearchBinding.recentlyViewedlayout.setVisibility(View.VISIBLE);
                            recentlyViewed(recentSearch);
                        }else {
                            fragmentSearchBinding.recentlyViewedlayout.setVisibility(View.GONE);
                        }


                    }else {
                        UtilsKt.snackbar(requireView(),response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<RecentSearch> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).showNavBar();
    }


    private void getPermission() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    myCurrentLocation();


                } else {
                    showToast(getString(R.string.give_Location_permission));
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();


    }

    private void myCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

    }

    private boolean isValidate() {
        if (fragmentSearchBinding.searchText.getText().toString().trim().isEmpty()){
            UtilsKt.snackbar(requireView(),getString(R.string.enter_keyword_search));
            return false;
        }
        return true;

    }

    private void getSearchResult(String searchText) {
        progressbar("Searching", "Please wait...");
        WebServiceFactory.getInstance().getSearchResult(searchText,currentLat,currentLng,lang).enqueue(new Callback<com.teamx.vevae.Models.search.SearchResult>() {
            @Override
            public void onResponse(Call<com.teamx.vevae.Models.search.SearchResult> call, Response<com.teamx.vevae.Models.search.SearchResult> response) {
                if (response.body().getFlag()==1){
                    searchResult = response.body();
                    if (response.body().getData() != null){

                        setData(response.body());
                    }else {

                        UtilsKt.snackbar(requireView(),response.body().getMessage());
                    }
                    mProgressBar.dismiss();
                }else{
                    UtilsKt.snackbar(requireView(),response.body().getMessage());
                    mProgressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<com.teamx.vevae.Models.search.SearchResult> call, Throwable t) {
                mProgressBar.dismiss();
            }
        });


    }

    private void setData(com.teamx.vevae.Models.search.SearchResult result) {


        //search result recyclerview
        LinearLayoutManager layoutManager = new  LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        fragmentSearchBinding.resultRecyclerview.setLayoutManager(layoutManager);

        searchResultAdapter = new SearchResultAdapter(requireContext(), result.getData(), this);
        fragmentSearchBinding.resultRecyclerview.setAdapter( searchResultAdapter);



    }

    private void chipGroup() {
        tags = new String[]{"Syed Muhammad Ahsan", "Muhammad Noman", "The Hanzala Raza", "Ahsan", "Noman", "Hanzala Hanzala", "Ahsan", "Noman", "Hanzala", "Ahsan", "Noman", "Hanzala"};
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (String text : tags){
            Chip chip = (Chip)inflater.inflate(R.layout.item_chip,null,false);
            chip.setText(text);
            fragmentSearchBinding.chipGroup.addView(chip);
        }


    }

    private void recentlyViewed(RecentSearch recentSearch) {

        LinearLayoutManager layoutManager =new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false);
        fragmentSearchBinding.recentlyViewrecyelerView.setLayoutManager(layoutManager);

        recentlyViewedAdapter = new RecentlyViewedAdapter(requireContext(),recentSearch.getData(),this);
        fragmentSearchBinding.recentlyViewrecyelerView.setAdapter(recentlyViewedAdapter);
    }


    @Override
    public void onItemClickListener(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",recentSearch.getData().get(position).getId());

        if (recentSearch.getData().get(position).getSearchtype().equalsIgnoreCase("product")){

            bundle.putString("storeType",recentSearch.getData().get(position).getSearchtype());
            Prefs.putString("storeType",recentSearch.getData().get(position).getSearchtype());
            NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
            navController.navigate(R.id.storeProfileFragment, bundle,navBuilder.build());

        }
        else if (recentSearch.getData().get(position).getSearchtype().equalsIgnoreCase("services")){

            bundle.putString("storeType",recentSearch.getData().get(position).getSearchtype());
            Prefs.putString("storeType",recentSearch.getData().get(position).getSearchtype());
            NavController navController = Navigation.findNavController((Activity) requireActivity(), R.id.nav_host_Fragmnet);
            navController.navigate(R.id.storeProfileFragment, bundle,navBuilder.build());

        }
        else if (recentSearch.getData().get(position).getSearchtype().equalsIgnoreCase("storeservices")){
            NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
            navController.navigate(R.id.servicesFragment, bundle,navBuilder.build());

        }
        else if (recentSearch.getData().get(position).getSearchtype().equalsIgnoreCase("storeproduct")){

            NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
            navController.navigate(R.id.productPreviewFragment, bundle,navBuilder.build());
        }
    }

    @Override
    public void OnSearchedItemClick(int position) {

        addSearchHistory(position,
                searchResult.getData().get(position).getName(),
                searchResult.getData().get(position).getType(),
                searchResult.getData().get(position).getId());

    }

    private void addSearchHistory(int position, String name, String type, Integer id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",searchResult.getData().get(position).getId());
        Prefs.putInt("id",searchResult.getData().get(position).getId());

        if (!userToken.isEmpty()){
            WebServiceFactory.getInstance().addSearch(name,type,id,lang).enqueue(new Callback<Logout>() {
                @Override
                public void onResponse(Call<Logout> call, Response<Logout> response) {
                    if (response.body().getFlag()==1){
                        if (searchResult.getData().get(position).getType().equalsIgnoreCase("product")){

                            bundle.putString("storeType",searchResult.getData().get(position).getType());
                            Prefs.putString("storeType",searchResult.getData().get(position).getType());
                            NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
                            navController.navigate(R.id.storeProfileFragment, bundle,navBuilder.build());

                        }
                        else if (searchResult.getData().get(position).getType().equalsIgnoreCase("services")){

                            bundle.putString("storeType",searchResult.getData().get(position).getType());
                            Prefs.putString("storeType",searchResult.getData().get(position).getType());
                            NavController navController = Navigation.findNavController((Activity) requireActivity(), R.id.nav_host_Fragmnet);
                            navController.navigate(R.id.storeProfileFragment, bundle,navBuilder.build());

                        }
                        else if (searchResult.getData().get(position).getType().equalsIgnoreCase("storeservices")){
                            NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
                            navController.navigate(R.id.servicesFragment, bundle,navBuilder.build());

                        }
                        else if (searchResult.getData().get(position).getType().equalsIgnoreCase("storeproduct")){

                            NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
                            navController.navigate(R.id.productPreviewFragment, bundle,navBuilder.build());
                        }
                    }else {

                        UtilsKt.snackbar(requireView(),response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Logout> call, Throwable t) {

                }
            });
        }else {
            if (searchResult.getData().get(position).getType().equalsIgnoreCase("product")){

                Prefs.putString("storeType", searchResult.getData().get(position).getType());
                bundle.putString("storeType",searchResult.getData().get(position).getType());
                NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.storeProfileFragment, bundle,navBuilder.build());

            }
            else if (searchResult.getData().get(position).getType().equalsIgnoreCase("services")){
                Prefs.putString("storeType", searchResult.getData().get(position).getType());
                bundle.putString("storeType",searchResult.getData().get(position).getType());
                NavController navController = Navigation.findNavController((Activity) requireActivity(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.storeProfileFragment, bundle,navBuilder.build());

            }
            else if (searchResult.getData().get(position).getType().equalsIgnoreCase("storeservices")) {
                NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.servicesFragment, bundle,navBuilder.build());

            }
            else if (searchResult.getData().get(position).getType().equalsIgnoreCase("storeproduct")){

                NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.productPreviewFragment, bundle,navBuilder.build());
            }
        }


    }


}