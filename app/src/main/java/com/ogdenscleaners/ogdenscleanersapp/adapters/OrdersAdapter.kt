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
    private val selectionMode: SelectionMode = SelectionMode.MULTIPLE
) : ListAdapter<Order, OrdersAdapter.OrderViewHolder>(OrderDiffCallback()) {

    enum class SelectionMode {
        SINGLE, MULTIPLE
    }

    private var singleSelectedOrder: Order? = null
    private val selectedOrders = mutableSetOf<Order>()

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val orderId: TextView = view.findViewById(R.id.order_id)
        private val orderDate: TextView = view.findViewById(R.id.order_date)
        private val orderPieces: TextView = view.findViewById(R.id.order_pieces)
        private val orderTotal: TextView = view.findViewById(R.id.order_total)

        fun bind(
            order: Order,
            isSelected: Boolean,
            onClick: () -> Unit,
            onLongClick: () -> Unit
        ) {
            orderId.text = order.id
            orderDate.text = order.dropOffDate
            orderPieces.text = order.numOfPieces.toString()
            orderTotal.text = "$${"%.2f".format(order.orderTotal)}"

            itemView.setBackgroundColor(
                if (isSelected)
                    itemView.context.getColor(R.color.selectedobject)
                else
                    itemView.context.getColor(R.color.buttonbackground)
            )

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

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        val isSelected = when (selectionMode) {
            SelectionMode.SINGLE -> order == singleSelectedOrder
            SelectionMode.MULTIPLE -> selectedOrders.contains(order)
        }
        holder.bind(
            order = order,
            isSelected = isSelected,
            onClick = {
                toggleSelection(order)
            },
            onLongClick = {
                onMoreInfoClick(order)
            }
        )
    }

    private fun toggleSelection(order: Order) {
        when (selectionMode) {
            SelectionMode.SINGLE -> {
                singleSelectedOrder = if (singleSelectedOrder == order) null else order
                notifyDataSetChanged()
            }
            SelectionMode.MULTIPLE -> {
                if (selectedOrders.contains(order)) {
                    selectedOrders.remove(order)
                } else {
                    selectedOrders.add(order)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun getSelectedOrder(): Order? = singleSelectedOrder

    fun getSelectedOrders(): List<Order> =
        if (selectionMode == SelectionMode.SINGLE) {
            listOfNotNull(singleSelectedOrder)
        } else {
            selectedOrders.toList()
        }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelectedOrders() {
        selectedOrders.clear()
        singleSelectedOrder = null
        notifyDataSetChanged()
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
}
