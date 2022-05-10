package com.example.agendadevideojuegos

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agendadevideojuegos.databinding.ActivityMainBinding
import com.example.agendadevideojuegos.databinding.VideogameBinding
import com.google.firebase.database.collection.LLRBNode

class VideoGameAdapter(private var videogames:ArrayList<Videogame>) :
RecyclerView.Adapter<VideoGameAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.videogame,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=videogames.get(position)
        with(holder){

            binding.tvName.text=character.nombre
            binding.tvplat.text=character.plataforma
            if (character.estado.compareTo("Pendiente")==0){
                binding.fondo.setBackgroundColor(Color.RED)
            }
            else if (character.estado.compareTo("Jugando")==0){
                binding.fondo.setBackgroundColor(Color.YELLOW)
            }
            else if (character.estado.compareTo("Completado")==0){
                binding.fondo.setBackgroundColor(Color.GREEN)
            }else {
                binding.fondo.setBackgroundColor(Color.BLUE)
            }




        }
    }
    fun setStores(stores: ArrayList<Videogame>) {
        this.videogames=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= VideogameBinding.bind(view)

    }

    override fun getItemCount(): Int =videogames.size

}