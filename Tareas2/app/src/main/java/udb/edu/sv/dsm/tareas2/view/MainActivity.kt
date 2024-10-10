package udb.edu.sv.dsm.tareas2.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import udb.edu.sv.dsm.tareas2.R
import udb.edu.sv.dsm.tareas2.model.Resource
import udb.edu.sv.dsm.tareas2.network.RetrofitClient
import udb.edu.sv.dsm.tareas2.ui.ResourceAdapter
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resourceAdapter: ResourceAdapter
    private val resourceList = mutableListOf<Resource>()
    private lateinit var buscarEditText: EditText
    private lateinit var buscarbtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        resourceAdapter = ResourceAdapter(resourceList) { resource -> showResourceDialog(resource) }
        recyclerView.adapter = resourceAdapter
        buscarEditText = findViewById(R.id.etBuscador)
         buscarbtn = findViewById(R.id.buscar)


        buscarbtn.setOnClickListener{
            val id = buscarEditText.text.toString()
            buscarResourceById(id)
        }



        fetchResources()

        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener { showAddResourceDialog() }
    }


    private fun showAddResourceDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_resource, null)
        builder.setView(dialogView)

        val titleInput = dialogView.findViewById<EditText>(R.id.editTitle)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.editDescription)
        val tipoInput = dialogView.findViewById<EditText>(R.id.editType)
        val urlRecursoInput = dialogView.findViewById<EditText>(R.id.editUrlRecurso)
        val urlImagenInput = dialogView.findViewById<EditText>(R.id.editUrlImagen)

        builder.setPositiveButton("Agregar") { dialog, which ->
            val newResource = Resource(
                id = "",
                title = titleInput.text.toString(),
                description = descriptionInput.text.toString(),
                tipo = tipoInput.text.toString(),
                urlRecurso = urlRecursoInput.text.toString(),
                urlImagen = urlImagenInput.text.toString()
            )
            addResource(newResource)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun showResourceDialog(resource: Resource) {
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setTitle("¿Qué desea? Escoja algo que no sea su ex o su crush.")
        builder.setItems(arrayOf("Modificar", "Eliminar", "Cancelar")) { _, which ->
            when (which) {
                0 -> showModifyResourceDialog(resource)
                1 -> confirmDeleteResource(resource.id)
            }
        }
        builder.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showModifyResourceDialog(resource: Resource) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_modify_resource, null)
        builder.setView(dialogView)

        val titleInput = dialogView.findViewById<EditText>(R.id.editTitle)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.editDescription)
        val tipoInput = dialogView.findViewById<EditText>(R.id.editType)
        val urlRecursoInput = dialogView.findViewById<EditText>(R.id.editUrlRecurso)
        val urlImagenInput = dialogView.findViewById<EditText>(R.id.editUrlImagen)

        titleInput.setText(resource.title)
        descriptionInput.setText(resource.description)
        tipoInput.setText(resource.tipo)
        urlRecursoInput.setText(resource.urlRecurso)
        urlImagenInput.setText(resource.urlImagen)

        builder.setPositiveButton("Modificar") { dialog, which ->
            val updatedResource = resource.copy(
                title = titleInput.text.toString(),
                description = descriptionInput.text.toString(),
                tipo = tipoInput.text.toString(),
                urlRecurso = urlRecursoInput.text.toString(),
                urlImagen = urlImagenInput.text.toString()
            )
            updateResources(resource.id, updatedResource)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun confirmDeleteResource(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Eliminación")
        builder.setMessage("¿Estás seguro de que deseas eliminar este recurso?")
        builder.setPositiveButton("Eliminar") { _, _ -> deleteResources(id) }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun fetchResources() {
        RetrofitClient.instance.getResources().enqueue(object : Callback<List<Resource>> {
            override fun onResponse(call: Call<List<Resource>>, response: Response<List<Resource>>) {
                if (response.isSuccessful) {
                    val resources = response.body() ?: emptyList()
                    resourceAdapter.updateResources(resources)
                } else {
                    Toast.makeText(this@MainActivity, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Resource>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addResource(resource: Resource) {
        RetrofitClient.instance.addResources(resource).enqueue(object : Callback<Resource> {
            override fun onResponse(call: Call<Resource>, response: Response<Resource>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Recurso agregado", Toast.LENGTH_SHORT).show()
                    fetchResources()
                } else {
                    Toast.makeText(this@MainActivity, "Error al agregar el recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Resource>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateResources(id: String, resource: Resource) {
        RetrofitClient.instance.updateResources(id, resource).enqueue(object : Callback<Resource> {
            override fun onResponse(call: Call<Resource>, response: Response<Resource>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Recurso modificado", Toast.LENGTH_SHORT).show()
                    fetchResources()
                } else {
                    Toast.makeText(this@MainActivity, "Error al modificar el recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Resource>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun deleteResources(id: String) {
        RetrofitClient.instance.deleteResources(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                    fetchResources()
                } else {
                    Toast.makeText(this@MainActivity, "Error al eliminar el recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun buscarResourceById(id: String) {
        RetrofitClient.instance.getResourceById(id).enqueue(object : Callback<Resource> {
            override fun onResponse(call: Call<Resource>, response: Response<Resource>) {
                if (response.isSuccessful) {
                    val resource = response.body()
                    if (resource != null) {
                        resourceAdapter.updateResources(Collections.singletonList(resource))                    } else {
                        Toast.makeText(this@MainActivity, "Recurso no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Resource>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}
