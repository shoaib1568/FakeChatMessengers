package com.codecue.fakechat.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codecue.fakechat.*
import com.codecue.fakechat.ads.Admob2
import com.codecue.fakechat.databinding.ActivityAddNewUserBinding
import com.google.android.gms.ads.interstitial.InterstitialAd
import java.text.SimpleDateFormat
import java.util.*


class AddNewUser : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null

    private lateinit var binding: ActivityAddNewUserBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var imageView: ImageView
    private var flag : Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListeners()
        requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, "Storage", STORAGE_PERMISSION_CODE)

        databaseHelper = DatabaseHelper(this)


    }

    companion object {
        const val PICK_PHOTO = 100
        private const val STORAGE_PERMISSION_CODE = 100
        private var flag : Boolean = false
    }

    /** Request For Permission **/
    private fun requestPermission (permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            when {
                ContextCompat.checkSelfPermission(applicationContext, permission)
                        == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name Permission Granted", Toast.LENGTH_SHORT).show()
                }

                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }

    }

    /** Dialog When Permission Is Not Granted **/
    private fun showDialog(permission: String, name: String, requestCode: Int) {

        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission To Access $name Required To Use This App")
            setTitle("Permission Required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@AddNewUser, arrayOf(permission), requestCode)
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun check (name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name Permission Not Granted ", Toast.LENGTH_SHORT).show()
                flag = false
            }
            when (requestCode) {
                STORAGE_PERMISSION_CODE -> check("STORAGE")
            }
        }
    }

    /** FUN: To Pick Image CALLED When ImageView CLicked **/
    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO && resultCode == RESULT_OK) {

            val profileImageURI = data?.data

            if (profileImageURI?.let { ImageLengthFinder.getLength(this@AddNewUser, it) } != null &&
                ImageLengthFinder.getLength(this@AddNewUser, profileImageURI)!! > 1200) {

                // ErrorBottomSheet.loadSheet(this@CreateShopActivity, "Image size must be less than 1 MB.")
                Toast.makeText(this, "Image Size Is Too Large ", Toast.LENGTH_LONG).show()

                return@onActivityResult
            }

            binding.currentUserProfile.setImageURI(data?.data)

        }
    }

    /** Fun: To Add New Chat **/

    private fun addNewChat() {

        val name = binding.userNameEdt.text.toString()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = Date()
        val currentTime = formatter.format(date)

        val getImageFromView = (binding.currentUserProfile.drawable as BitmapDrawable).bitmap
        val convertedImage: ByteArray = DbBitmapUtility.getBytes(getImageFromView)


        val newChat = Chat(0, name, currentTime, convertedImage)
        val isInserted: Boolean = databaseHelper.insertChat(newChat)
        databaseHelper.close()

        if (isInserted) {
            startActivity(Intent(this, ChatListActivity::class.java))
            this.finish()

        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }


    }

    /** CLick Listeners **/
    private fun setOnClickListeners() {
        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.currentUserProfile.setOnClickListener { pickImage()}

        binding.addUserButton.setOnClickListener {
            if (Admob2.mInterstitialAd != null) {
                Admob2.mInterstitialAd.show(this@AddNewUser)
            } else {


            }

            val mUserName = binding.userNameEdt.text.toString()

            if (mUserName.isEmpty()) {
                Toast.makeText(this, "Username is Empty", Toast.LENGTH_SHORT).show()
            } else {
                addNewChat()
            }
        }


    }

}
