package udb.edu.sv.dsm.tareas2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import udb.edu.sv.dsm.tareas2.R
import udb.edu.sv.dsm.tareas2.model.Resource

class ResourceAdapter(
    private val resourceList: MutableList<Resource>,
    private val onResourceClick: (Resource) -> Unit
) : RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.resourceTitle)
        val description: TextView = itemView.findViewById(R.id.resourceDescription)
        val tipo: TextView = itemView.findViewById(R.id.resourceType)
        val urlRecurso: TextView = itemView.findViewById(R.id.urlResouce)
        val image: ImageView = itemView.findViewById(R.id.resourceImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resource, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resource = resourceList[position]
        holder.title.text = resource.title
        holder.description.text = resource.description
        holder.tipo.text = resource.tipo
        holder.urlRecurso.text = resource.urlRecurso

        Glide.with(holder.itemView.context)
            .load(resource.urlImagen)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onResourceClick(resource)
        }
    }

    override fun getItemCount(): Int = resourceList.size

    fun updateResources(newResources: List<Resource>) {
        resourceList.clear()
        resourceList.addAll(newResources)
        notifyDataSetChanged()
    }


}
