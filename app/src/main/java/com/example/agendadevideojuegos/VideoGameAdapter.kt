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

class VideoGameAdapter(private var videogames:ArrayList<Videogame>,private var listener:OnClickListener) :
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
            setListener(character)

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
    fun delete(videogame: Videogame) {
        val index=videogames.indexOf(videogame)
        if(index!=-1){
            videogames.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    fun setStores(stores: ArrayList<Videogame>) {
        this.videogames=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= VideogameBinding.bind(view)

        fun setListener(videogame:Videogame){
            with(binding.root){
                setOnLongClickListener(){
                    listener.onDeleteGame(videogame)
                    true
                }
            }
            binding.editButton.setOnClickListener{
                listener.onEditGame(videogame)
                true
            }
        }

    }

    override fun getItemCount(): Int =videogames.size

}