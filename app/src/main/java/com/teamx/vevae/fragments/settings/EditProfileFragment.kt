package com.teamx.vevae.fragments.settings

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.UpdateProfile.UpdateProfile
import com.teamx.vevae.Models.UserDetails.UserDetails
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.ShareViewModel.ShareViewModel
import com.teamx.vevae.Utils.ImageResizer
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.databinding.FragmentEditProfileBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.Utils.snackbar
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


class EditProfileFragment : BaseFragment() {

    lateinit var fragmentEditProfileBinding: FragmentEditProfileBinding
    lateinit var userDetails: UserDetails
    var isUserMale = -1
    lateinit var builder: MultipartBody.Builder

    var file : File? = null

    private var selectedImage: Uri? = null

    private var bitmap: Bitmap? = null


    companion object {
        private const val REQUEST_IMAGE_PICK =  100
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
        fragmentEditProfileBinding = FragmentEditProfileBinding.inflate(inflater,container,false)

        builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        fragmentEditProfileBinding.btnBack.setOnClickListener{

            popUpStack()
        }


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentEditProfileBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            fragmentEditProfileBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC

        }else{
            fragmentEditProfileBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            fragmentEditProfileBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))

            lang = LocaleManager.LANGUAGE_ENGLISH

        }



        fragmentEditProfileBinding.btnAddPicture.setOnClickListener{
            getPermission()
        }

        shareViewModel = ViewModelProvider(this).get(ShareViewModel::class.java)

        shareViewModel.userDetails.observe(requireActivity(),
            Observer {
                userDetails = it
                setData(it)

        })






        fragmentEditProfileBinding.btnDateOfBirth.setOnClickListener{

            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val day: Int = cal.get(Calendar.DAY_OF_MONTH)
            val dp = DatePickerDialog(requireActivity(), R.style.DialogTheme, { view, year, monthOfYear, dayOfMonth ->
                fragmentEditProfileBinding.textDOB.text = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                }, year, month, day
            )
            dp.show()
        }



        fragmentEditProfileBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioMale -> {
                    isUserMale = 1
                }
                R.id.radioFemale -> {
                    isUserMale = 0


                }
            }
        }

        fragmentEditProfileBinding.btnUpdate.setOnClickListener{


            if (InternetConnection.checkConnection(context)){
                if (validateFields()){
                    getDataFromFields()
                }
            }else{
                showInternetToast()
            }


        }






        return fragmentEditProfileBinding.root


    }


    private fun validateFields(): Boolean {



        if (fragmentEditProfileBinding.editName.text.toString().isEmpty()){
            fragmentEditProfileBinding.rootLayout.snackbar(getString(R.string.enter_name))
            return  false
        }



        if (isUserMale == -1){
            fragmentEditProfileBinding.rootLayout.snackbar(getString(R.string.select_gender))
            return  false
        }

        if (fragmentEditProfileBinding.editEmail.text.toString().isEmpty()){
            fragmentEditProfileBinding.rootLayout.snackbar(getString(R.string.enter_email))
            return  false
        }

        if (fragmentEditProfileBinding.editPhone.text.toString().isEmpty()){
            fragmentEditProfileBinding.rootLayout.snackbar(getString(R.string.enter_phone_number))
            return  false
        }

       /* if (fragmentEditProfileBinding.editAddress.text.toString().isEmpty()){
            fragmentEditProfileBinding.rootLayout.snackbar(getString(R.string.birth_date))
            return  false
        }
*/


        return true
    }

    private fun getDataFromFields() {


        val name = fragmentEditProfileBinding.editName.text.toString()
//        val  dob = fragmentEditProfileBinding.textDOB.text.toString()
        val email = fragmentEditProfileBinding.editEmail.text.toString()
        val  phoneNumber = fragmentEditProfileBinding.editPhone.text.toString()
//        val  address = fragmentEditProfileBinding.editAddress.text.toString()




            builder.addFormDataPart("user_name",name)
            builder.addFormDataPart("phone",phoneNumber)
//            builder.addFormDataPart("address",address)
//            builder.addFormDataPart("userBirthDate",dob)
            builder.addFormDataPart("email",email)
            builder.addFormDataPart("lang",lang)


            if (file != null){
                builder.addFormDataPart(
                    "image",
                    file!!.name,
                    file!!.asRequestBody()
                )
            }
            builder.addFormDataPart("IsUserMale", isUserMale.toString())

            updateProfile(builder)

        }

    private fun updateProfile(builder: MultipartBody.Builder) {

        progressbar("Updating","Please wait")

        WebServiceFactory.getInstance().updateProfile(builder.build()).enqueue(object :
            Callback<UpdateProfile>{
            override fun onResponse(call: Call<UpdateProfile>, response: Response<UpdateProfile>) {
                mProgressBar.dismiss()
                if (response.body()!!.flag==1){
                    fragmentEditProfileBinding.rootLayout.snackbar(getString(R.string.profile_Updated))

                    Prefs.putString("userName", response.body()!!.data.userName)
                    Prefs.putString("userEmail", response.body()!!.data.userEmail)
                    Prefs.putString("userImage", response.body()!!.data.userImage)

                }else{
                    fragmentEditProfileBinding.rootLayout.snackbar(response.body()!!.message)

                }
            }

            override fun onFailure(call: Call<UpdateProfile>, t: Throwable) {
                mProgressBar.dismiss()
            }
        })

    }

    private fun getPermission() {
            Dexter.withContext(activity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            selectUserImage()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                    }
                }).check()

    }

    private fun selectUserImage() {

        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val miniTypes = arrayOf("image/jpeg","image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES,miniTypes)
            startActivityForResult(it, REQUEST_IMAGE_PICK)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            when(requestCode){
                REQUEST_IMAGE_PICK -> {
                    selectedImage = data!!.data

                    //setImage in imageview
                    fragmentEditProfileBinding.profilePicture.setImageURI(selectedImage)

                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = requireActivity()!!.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)

                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()

                    file = File(picturePath)
                    bitmap = BitmapFactory.decodeFile(picturePath)
                    bitmap =  modifyOrientation(bitmap!!,picturePath)

                    val bitmapImageReducer: Bitmap = ImageResizer.reduceBitmapSize(bitmap, 500000)
                    file = getBitMapFile(bitmapImageReducer) //create path from uri

                }
            }
        }
    }



    private fun setData(userDetails: UserDetails) {

        fragmentEditProfileBinding.editName.setText(userDetails.data.name)
//        fragmentEditProfileBinding.textDOB.text = userDetails.data.userBirthDate
        fragmentEditProfileBinding.editEmail.setText(userDetails.data.email)
        fragmentEditProfileBinding.editPhone.setText(userDetails.data.phone)
//        fragmentEditProfileBinding.editAddress.setText(userDetails.data.userAddress)


        imageLoaderPicasso(userDetails.data.image,fragmentEditProfileBinding.profilePicture)
//        Picasso.get().load(AppConstants.imagePath(userDetails.data.image)).into(fragmentEditProfileBinding.profilePicture)

        val isGender = userDetails.data.isUserMale


        if (isGender !=null){
            if(isGender==1)
            {
                fragmentEditProfileBinding.radioMale.isChecked = true
                fragmentEditProfileBinding.radioFemale.isChecked = false
                isUserMale = 1
            }
            else
            {
                fragmentEditProfileBinding.radioMale.isChecked = false
                fragmentEditProfileBinding.radioFemale.isChecked = true
                isUserMale = 0
            }

        }else{
            fragmentEditProfileBinding.radioMale.isChecked = false
            fragmentEditProfileBinding.radioFemale.isChecked = false
        }

    }


    @Throws(IOException::class)
    fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String?): Bitmap? {
        val ei = ExifInterface(image_absolute_path!!)
        val orientation: Int = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, true, false)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)
            else -> bitmap
        }
    }

    fun rotate(bitmap: Bitmap, degrees: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
        val matrix = Matrix()
        matrix.preScale((if (horizontal) -1 else 1.toFloat()) as Float,
            (if (vertical) -1 else 1.toFloat()) as Float
        )
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}