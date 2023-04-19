package dev.app.mobileapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PortsAdapter (private val portsList: ArrayList<String>) : RecyclerView.Adapter<PortsAdapter.PortsViewHolder>() {


    private lateinit var mListener: OnItemClickListener
    private var lastClickedPosition = -1
    var selectedPosition = -1

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class PortsViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val port_name = itemView.findViewById<RadioButton>(R.id.portItemRB)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(adapterPosition)
                }
            }
        }
    }




    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.port_list_item, parent, false)

        return PortsViewHolder(view,mListener)

    }

    override fun onBindViewHolder(holder: PortsViewHolder, position: Int) {
        val name = portsList[holder.adapterPosition]
        holder.port_name.text = name

        //check if the item is already selected
        holder.port_name.isChecked = holder.adapterPosition == lastClickedPosition

        //set the click listener
        holder.port_name.setOnClickListener {
            //uncheck the previous item
            if (lastClickedPosition != -1) {
                notifyItemChanged(lastClickedPosition)
                selectedPosition = -1
            }
            //set the current position to "selected"
            lastClickedPosition = holder.adapterPosition
            selectedPosition = holder.adapterPosition
        }
    }

    override fun getItemCount(): Int {
        return portsList.size
    }
}
