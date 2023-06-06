package com.example.firebaseappexample

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseappexample.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var noteList:ArrayList<NoteModel>
    private lateinit var noteAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        firestore=Firebase.firestore
        storage=Firebase.storage
        noteList=ArrayList<NoteModel>()
        getNote()
        binding.recyclerview.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        noteAdapter= NoteAdapter(noteList)
        binding.recyclerview.adapter=noteAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.addNote){
            val intent=Intent(this,AddActivity::class.java)
            startActivity(intent)
        }else{
            auth.signOut()
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNote(){
        firestore.collection("Notes").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error!=null){
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else{
                if (value!=null){
                        val documents=value.documents
                        noteList.clear()
                        for (document in documents){
                            val downloadUrl=document.get("downloadUrl") as? String
                            val title=document.get("title") as String
                            val detail=document.get("detail") as String

                            val noteModel= downloadUrl?.let { NoteModel(it,title,detail) }
                            if (noteModel != null) {
                                noteList.add(noteModel)
                            }
                            noteAdapter.notifyDataSetChanged()
                        }
                }
            }
        }

    }
}