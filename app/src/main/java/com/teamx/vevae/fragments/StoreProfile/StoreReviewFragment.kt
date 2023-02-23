package com.teamx.vevae.fragments.StoreProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.vevae.DummyModel.Review
import com.teamx.vevae.Models.storeprofile.Data
import com.teamx.vevae.Models.storeprofile.StoreProfile
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.adapter.ReviewAdapter
import com.teamx.vevae.databinding.FragmentStoreReviewBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnOrderClickListener
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.addreview.ReviewData
import com.teamx.vevae.Models.storeprofile.Rating
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreReviewFragment : BaseFragment(), OnOrderClickListener {


    lateinit var  storeReviewBinding: FragmentStoreReviewBinding
    lateinit var reviewAdapter: ReviewAdapter
    private  var storeId : Int=0;
    private  lateinit var storeType : String
    private lateinit var userToken  :String
    private lateinit var data: Data


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_store_review, container, false)
        storeReviewBinding = FragmentStoreReviewBinding.inflate(inflater,container,false)


         if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {
            lang = LocaleManager.LANGUAGE_ARABIC
        } else {
            lang = LocaleManager.LANGUAGE_ENGLISH
        }


        storeId = Prefs.getInt("id",0)
        storeType = Prefs.getString("storeType","")





        storeReviewBinding.btnAddReview.setOnClickListener{

            userToken = Prefs.getString("userToken","");


            if (userToken.isNotEmpty()){

                navBuilder.setEnterAnim(R.anim.slide_in)
                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
                navController.navigate(R.id.addReviewFragment, null,navBuilder.build())
            }else{
                showLoginBottomSheet(R.id.addReviewFragment, true)
            }

        }


        if (InternetConnection.checkConnection(requireContext())){
            getReviews()
        }else{
            storeReviewBinding. rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
        }



        return storeReviewBinding.root




    }

    private fun getReviews() {


        WebServiceFactory.getInstance().getStoreDetails(storeId,storeType,lang).enqueue(object :
            Callback<StoreProfile> {
            override fun onResponse(call: Call<StoreProfile>, response: Response<StoreProfile>) {
                if (response.body()!!.flag==1){
                    data = response.body()!!.data;
                    setData(response.body()!!.data)
                }else{
                    storeReviewBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<StoreProfile>, t: Throwable) {

            }

        })
    }

    private fun setData(data: Data) {



        val comment = Prefs.getString("comment","");
        val userImage = Prefs.getString("userImage","");
        val reviewRating = Prefs.getString("reviewRating","");
        val reviewDate = Prefs.getString("reviewDate","");
        val username = Prefs.getString("userName","");

        if (comment.isNotEmpty()){
            data.ratings.add(0, Rating(username,userImage,comment,reviewRating,reviewDate))

            Prefs.putString("comment","");
            Prefs.putString("userImage","");
            Prefs.putString("reviewRating","");
            Prefs.putString("reviewDate","");
            Prefs.putString("userName","");
        }

        val layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        storeReviewBinding.reviewRecyclerview.layoutManager = layoutManager

        reviewAdapter = ReviewAdapter(requireContext(),data.ratings,this)

        storeReviewBinding.reviewRecyclerview.adapter = reviewAdapter


    }

    override fun onItemClickListener(position: Int) {

    }


}