package com.ogdenscleaners.ogdenscleanersapp.adapters

import android.annotation.SuppressLint
import android.graphics.Color
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
    private val onMoreInfoClick: (Order) -> Unit // Callback for "More Info" action
) : ListAdapter<Order, OrdersAdapter.OrderViewHolder>(OrderDiffCallback()) {

    private val selectedOrders = mutableSetOf<Order>() // Tracks selected orders

    // ViewHolder class for better code encapsulation
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.order_id)
        val orderDate: TextView = view.findViewById(R.id.order_date)
        val orderPieces: TextView = view.findViewById(R.id.order_pieces)
        val orderTotal: TextView = view.findViewById(R.id.order_total)

        fun bind(order: Order, isSelected: Boolean, onClick: () -> Unit, onLongClick: () -> Unit) {
            // Set order data
            orderId.text = order.customerId
            orderDate.text = order.dropOffDate
            orderPieces.text = order.items.size.toString()
            orderTotal.text = "$${order.orderTotal}"

            // Highlight background if the order is selected
            itemView.setBackgroundColor(
                if (isSelected) Color.parseColor("#008080") else Color.WHITE
            )

            // Set click listeners
            itemView.setOnClickListener { onClick() }
            itemView.setOnLongClickListener {
                onLongClick()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(
            order = order,
            isSelected = selectedOrders.contains(order),
            onClick = {
                toggleSelection(order)
                notifyItemChanged(position)
            },
            onLongClick = { onMoreInfoClick(order) }
        )
    }

    // Toggles the selection of an order
    private fun toggleSelection(order: Order) {
        if (selectedOrders.contains(order)) {
            selectedOrders.remove(order)
        } else {
            selectedOrders.add(order)
        }
    }

    // Returns the currently selected orders
    fun getSelectedOrders(): List<Order> = selectedOrders.toList()

    // Clears all selected orders
    @SuppressLint("NotifyDataSetChanged")
    fun clearSelectedOrders() {
        selectedOrders.clear()
        notifyDataSetChanged() // Refresh the list
    }
}

// DiffUtil for efficient updates
class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.id == newItem.id // Compare by unique ID
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem // Compare entire contents
    }
}
