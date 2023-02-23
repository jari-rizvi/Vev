package com.teamx.vevae.fragments.StoreProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.addreview.AddReview
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.localization.LocaleManager.Companion.LANGUAGE_ARABIC
import com.teamx.vevae.Utils.localization.LocaleManager.Companion.LANGUAGE_ENGLISH
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.databinding.FragmentAddReviewBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddReviewFragment : BaseFragment() {

    private lateinit var options: NavOptions
    lateinit var  fragmentAddReviewBinding: FragmentAddReviewBinding

    var rating : Float = 0f
    lateinit var comment : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_review, container, false)
        fragmentAddReviewBinding = FragmentAddReviewBinding.inflate(inflater,container,false)

//        fragmentAddReviewBinding.rating.numStars = 5


        if (!App.localeManager.getLanguage().equals(LANGUAGE_ENGLISH)) {
            lang = LANGUAGE_ARABIC
        } else {
            lang =  LANGUAGE_ENGLISH
        }

        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        fragmentAddReviewBinding.btnBack.setOnClickListener {

            popUpStack()
        }


        fragmentAddReviewBinding.btnAddReview.setOnClickListener{


            if (validate()){
                if (InternetConnection.checkConnection(requireContext())){
                    addReview()
                }else{
                    showInternetToast()
                }
            }
        }


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentAddReviewBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            lang = LANGUAGE_ARABIC

        }else{
            fragmentAddReviewBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LANGUAGE_ENGLISH
        }

        return fragmentAddReviewBinding.root

    }

    private fun addReview() {
        progressbar("Adding Review","Please wait...")
        WebServiceFactory.getInstance().addReview(5,rating,comment,lang).enqueue(object : Callback<AddReview> {
            override fun onResponse(call: Call<AddReview>, response: Response<AddReview>) {
                if (response.body()!!.flag==1){

                    fragmentAddReviewBinding.rootLayout.snackbar("")
                    Prefs.putString("comment",response.body()!!.data.reviewData.comments)
                    Prefs.putString("reviewRating",response.body()!!.data.reviewData.rating)
                    Prefs.putString("reviewDate",response.body()!!.data.reviewData.createdAt)


                    mProgressBar.dismiss()
                    popUpStack()
                }else{
                    fragmentAddReviewBinding.rootLayout.snackbar(response.body()!!.message)
                    mProgressBar.dismiss()
                }
            }
            override fun onFailure(call: Call<AddReview>, t: Throwable) {
               mProgressBar.dismiss()
            }
        })
    }

    fun validate() : Boolean {

        rating =  fragmentAddReviewBinding.ratingBar.rating
        comment = fragmentAddReviewBinding.editReview.text.toString()



        if (rating==0f){
            fragmentAddReviewBinding.rootLayout.snackbar(getString(R.string.give_rating))
            return false
        }

        if (comment.isEmpty()){
            fragmentAddReviewBinding.rootLayout.snackbar(getString(R.string.please_add_review))
            return false
        }
        return true
    }


}