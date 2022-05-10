package com.example.agendadevideojuegos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.agendadevideojuegos.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(),OnClickListener {
    private lateinit var mBinding: ActivityMainBinding

    var listajuegos=ArrayList<Videogame>()
    private lateinit var mAdapter: VideoGameAdapter
    private lateinit var mGridLayout: GridLayoutManager

    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.button.setOnClickListener{
            if (mBinding.spinner.selectedItem.toString().compareTo("Todas")==0){
                obtenerDatos()
            }else{
                obtenerDatosEspecificos(mBinding.spinner.selectedItem.toString())
            }
        }
        mBinding.floatingActionButton.setOnClickListener {
            val dialog = layoutInflater.inflate(R.layout.agregarjuego, null)

            var dialogq = MaterialAlertDialogBuilder(this).apply {
                setTitle("Agregar Juego")
                setView(dialog)
                setPositiveButton("Agregar") { _, i ->

                    subirjuego(dialog.findViewById<EditText>(R.id.NombreEditText).text.toString(),
                        dialog.findViewById<Spinner>(R.id.spinner2).selectedItem.toString(),
                        dialog.findViewById<Spinner>(R.id.spinner3).selectedItem.toString())

                }
            }.show()

        }
        setupRecyclerView()
        }

    private fun setupRecyclerView() {
        mAdapter = VideoGameAdapter(ArrayList(),this)
        mGridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))
        obtenerDatos()
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun obtenerDatos() {
        listajuegos.clear()
        val docRef = db.collection("Juegos")
        docRef.get()
            .addOnSuccessListener { document ->
                for (i in document.documents){
                    var juego:Videogame= Videogame(i.id,i.get("Estado").toString(),i.get("Plataforma").toString())

                    listajuegos.add(juego)
                }
                mAdapter.setStores(listajuegos)
            }
            .addOnFailureListener { exception ->
            }
    }
    private fun obtenerDatosEspecificos(plat:String) {
        listajuegos.clear()
        val docRef = db.collection("Juegos")
        docRef.get()
            .addOnSuccessListener { document ->

                for (i in document.documents){
                    if (i.get("Plataforma").toString().compareTo(plat)==0){
                        var juego:Videogame= Videogame(i.id,i.get("Estado").toString(),i.get("Plataforma").toString())
                        listajuegos.add(juego)
                    }
                }
                if (listajuegos!=null){
                    mAdapter.setStores(listajuegos)
                }else{
                    Toast.makeText(this,"No Hay juegos de esta plataforma",Toast.LENGTH_LONG)
                }

            }
            .addOnFailureListener { exception ->
            }




    }
    fun subirjuego(nombre:String,Estado:String,Plat:String) {
        Log.d("LLega","Si")
        db.collection("Juegos").document(nombre)
            .set(
                hashMapOf(
                    "Estado" to Estado,
                    "Plataforma" to Plat

                )
            ).addOnSuccessListener { documentReference ->
                Toast.makeText(applicationContext,"Juego registrado con éxito",Toast.LENGTH_SHORT).show()
                setupRecyclerView()

            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext,"Error al registrar el juego",Toast.LENGTH_SHORT).show()
            }

    }

    override fun onDeleteGame(videogame: Videogame) {
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Desea eliminar ${videogame.nombre}?")
            .setPositiveButton("Confirmar",{ dialogInterface,i ->
                mAdapter.delete(videogame)
                db.collection("Juegos").document(videogame.nombre).delete()
                    .addOnSuccessListener { Toast.makeText(this,"Se eliminó correctamente",Toast.LENGTH_LONG).show() }
                    .addOnFailureListener { Toast.makeText(this,"No se pudo eliminar",Toast.LENGTH_LONG).show() }
            })
            .setNegativeButton("Cancelar",null)
            .show()

    }

    override fun onEditGame(videogame: Videogame) {
        val dialog = layoutInflater.inflate(R.layout.agregarjuego, null)
        dialog.findViewById<EditText>(R.id.NombreEditText).setText(videogame.nombre)
        dialog.findViewById<EditText>(R.id.NombreEditText).isEnabled=false


        var dialogq = MaterialAlertDialogBuilder(this).apply {
            setTitle("Editar Juego")
            setView(dialog)
            setPositiveButton("Editar") { _, i ->

                subirjuego(dialog.findViewById<EditText>(R.id.NombreEditText).text.toString(),
                    dialog.findViewById<Spinner>(R.id.spinner2).selectedItem.toString(),
                    dialog.findViewById<Spinner>(R.id.spinner3).selectedItem.toString())

            }.setNegativeButton("Cancelar",null)
        }.show()
    }
}