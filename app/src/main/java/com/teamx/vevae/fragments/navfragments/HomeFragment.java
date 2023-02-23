package com.teamx.vevae.fragments.navfragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.home.Data;
import com.teamx.vevae.Models.home.Home;
import com.teamx.vevae.Networking.WebServiceFactory;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.activities.MainActivity;
import com.teamx.vevae.adapter.HomeAdapter;
import com.teamx.vevae.databinding.FragmentHomeBinding;
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment;
import com.teamx.vevae.onClick.OnSearchResultClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends BaseFragment implements OnSearchResultClickListener {

    FragmentHomeBinding fragmentHomeBinding;
    private HomeAdapter homeAdapter;
    private GoogleMap mMap;
    private LinearLayoutManager layoutManager;

    private double currentLat = 0;
    private double currentLng = 0;

    private Home home;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment mapFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {
            lang = LocaleManager.Companion.getLANGUAGE_ARABIC();
        }else{
            lang = LocaleManager.Companion.getLANGUAGE_ENGLISH();

        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);


        getPermission();
        setUserData();

        String userToken= Prefs.getString("userToken","");
        if (!userToken.isEmpty()){
            fragmentHomeBinding.btnNotification.setVisibility(View.VISIBLE);
            fragmentHomeBinding.notificationCount.setVisibility(View.VISIBLE);

        }else {
            fragmentHomeBinding.btnNotification.setVisibility(View.GONE);
            fragmentHomeBinding.notificationCount.setVisibility(View.GONE);
        }

        fragmentHomeBinding.btnMyCurrentLocation.setOnClickListener(v -> getPermission());

        fragmentHomeBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.searchFragment, null);
            }
        });


        fragmentHomeBinding.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.profileFragment, null);
            }
        });
        fragmentHomeBinding.btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.notificationFragment, null);
            }
        });


        fragmentHomeBinding.searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
                navController.navigate(R.id.searchFragment, null);
            }
        });

        return fragmentHomeBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showNavBar();
    }

    private void setUserData() {

        String userImage = Prefs.getString("userImage", "");
        String username = Prefs.getString("userName", "");

        imageLoaderPicasso(userImage, fragmentHomeBinding.userProfile);
        fragmentHomeBinding.username.setText(username);
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


    private void getStores(ArrayList<Data> storeList) {
        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        fragmentHomeBinding.homeRecyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(requireContext(), storeList, this);
        fragmentHomeBinding.homeRecyclerView.setAdapter(homeAdapter);
    }

    @Override
    public void OnSearchedItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("id", home.getData().get(position).getId());
        bundle.putString("storeType", home.getData().get(position).getStoreType());

        Prefs.putInt("id",home.getData().get(position).getId());
        Prefs.putString("storeType", home.getData().get(position).getStoreType());

        navBuilder.setEnterAnim(R.anim.slide_in);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet);
        navController.navigate(R.id.storeProfileFragment, bundle, navBuilder.build());

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

                //sync map
                mapFragment.getMapAsync(googleMap -> {
                    mMap = googleMap;

                    LatLng latLng = new LatLng(currentLng, currentLat);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLng), 15));
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(bitmapDescriptorFromVector(requireContext(), R.drawable.current_location)));
                    if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);


                    nearByLocation();
                    mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    requireContext(), R.raw.style__json));

                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

    }

    private void nearByLocation() {
        WebServiceFactory.getInstance().getAllStore(24.9228408, 67.0921051,lang).enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (response.body().getFlag() == 1) {
                    home = response.body();
                    fragmentHomeBinding.shimmerLayout.stopShimmer();
                    fragmentHomeBinding.shimmerLayout.setVisibility(View.GONE);
                    setMarker(home);
                    getStores(home.getData());
                } else {
                    showToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {

            }
        });
    }

    private void setMarker(Home home) {

        if (home.getData() != null) {

            for (int i = 0; i < home.getData().size(); i++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(home.getData().get(i).getLat(), home.getData().get(i).getLng())).title(home.getData().get(i).getStore_name()));
            }


        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(100, 100, vectorDrawable.getIntrinsicWidth() + 100, vectorDrawable.getIntrinsicHeight() + 100);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}