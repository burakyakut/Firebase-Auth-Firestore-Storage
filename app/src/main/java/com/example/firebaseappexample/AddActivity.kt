package com.example.firebaseappexample

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.firebaseappexample.databinding.ActivityAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp
import java.util.UUID

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedImage: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage=Firebase.storage
        firestore=Firebase.firestore
        auth=Firebase.auth
        selectImagePermission()
        registerLauncher()
        saveButton()
    }

    private fun selectImagePermission(){
        binding.addPhotoImageViewAddActivity.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }else{
                val intetToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intetToGallery)
            }
        }
    }

    private fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode==RESULT_OK){
                val intentFromResult=result.data
                if (intentFromResult!=null){
                    selectedImage=intentFromResult.data
                    selectedImage?.let {
                        binding.addPhotoImageViewAddActivity.setImageURI(it)
                    }

                }
            }

        }

        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result){
                val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }

    }

    private fun saveButton(){
        binding.saveButton.setOnClickListener {
            val title=binding.titleEditTextAddActivity.text.toString()
            val detail=binding.detailEditTextAddActivity.text.toString()
            if (title.isNotEmpty() && detail.isNotEmpty()){
                val uuid=UUID.randomUUID()
                val randomImageId="$uuid.jpg"
                val reference=storage.reference
                val imageReference=reference.child("images").child(randomImageId)
                if (selectedImage!=null){
                    imageReference.putFile(selectedImage!!).addOnSuccessListener {
                        imageReference.downloadUrl.addOnSuccessListener {
                            val downloadUrl=it.toString()

                            val noteMap= hashMapOf<String,Any>()
                            noteMap["downloadUrl"] = downloadUrl
                            noteMap["title"] = binding.titleEditTextAddActivity.text.toString()
                            noteMap["detail"] = binding.detailEditTextAddActivity.text.toString()
                            noteMap["date"] = com.google.firebase.Timestamp.now()

                            firestore.collection("Notes").add(noteMap).addOnSuccessListener {
                                finish()
                            }
                        }

                    }
                }
            }


        }
    }
}