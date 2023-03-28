package com.codecue.fakechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.codecue.fakechat.ImageLengthFinder
import com.codecue.fakechat.databinding.ActivityCurrentUserImageBinding

class CurrentUserImage : AppCompatActivity() {
    private lateinit var binding: ActivityCurrentUserImageBinding
    private val SELECT_IMAGE_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentUserImageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setOnClickListeners()

    }

    private fun setOnClickListeners() {

        binding.currentUserImageView.setOnClickListener { pickImage() }

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.saveImageButton.setOnClickListener { saveImage() }

    }

    private fun saveImage() {

    }


    /** FUN: Pick Image From Device And Set Image To An ImageView **/

    private fun pickImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), SELECT_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK) {

            val profileImageURI = data?.data

            if (profileImageURI?.let {
                    ImageLengthFinder.getLength(
                        this@CurrentUserImage,
                        it
                    )
                } != null &&
                ImageLengthFinder.getLength(this@CurrentUserImage, profileImageURI)!! > 1200) {

                // ErrorBottomSheet.loadSheet(this@CreateShopActivity, "Image size must be less than 1 MB.")
                Toast.makeText(this, "Image Size Is Too Large ", Toast.LENGTH_LONG).show()

                return@onActivityResult
            }

            binding.currentUserImageView.setImageURI(data?.data)

        }
    }


}