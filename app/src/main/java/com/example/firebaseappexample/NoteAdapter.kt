package com.example.firebaseappexample

import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseappexample.databinding.RecyclerrowBinding
import com.squareup.picasso.Picasso

class NoteAdapter(private val noteList:ArrayList<NoteModel>):RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    class NoteHolder(val recyclerrowBinding: RecyclerrowBinding):RecyclerView.ViewHolder(recyclerrowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(RecyclerrowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note=noteList[position]
        holder.recyclerrowBinding.recyclerRowTitleTextView.text=note.title
        holder.recyclerrowBinding.recyclerRowDetailTextView.text=note.detail

        Picasso.get().load(note.downloadUrl).into(holder.recyclerrowBinding.recyclerRowImageView)


    }
}