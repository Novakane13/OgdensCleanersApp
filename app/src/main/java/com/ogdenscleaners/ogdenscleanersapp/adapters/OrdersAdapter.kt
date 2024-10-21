package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.models.Order

class OrdersAdapter(
    private val onMoreInfoClick: (Order) -> Unit,
    private val onAddToPaymentClick: (Order) -> Unit
) : ListAdapter<Order, OrdersAdapter.OrderViewHolder>(OrderDiffCallback()) {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.order_id)
        val orderDate: TextView = view.findViewById(R.id.order_date)
        val orderPieces: TextView = view.findViewById(R.id.order_pieces)
        val orderTotal: TextView = view.findViewById(R.id.order_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.orderId.text = order.id
        holder.orderDate.text = order.date
        holder.orderPieces.text = order.items.size.toString()
        holder.orderTotal.text = "$${order.total}"

        // Click listeners for more info and payment actions
        holder.itemView.setOnClickListener {
            onMoreInfoClick(order)
        }

        holder.itemView.setOnLongClickListener {
            onAddToPaymentClick(order)
            true
        }
    }
}

// DiffUtil for better performance and efficient list updates
class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}
