package com.example.ocasio_genie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ChatAdapter(private val context: Context, private val messages: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_chat_message, parent, false)
            holder = ViewHolder()
            holder.messageTextView = view.findViewById(R.id.messageTextView)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val message = messages[position]
        holder.messageTextView.text = message

        return view
    }

    private class ViewHolder {
        lateinit var messageTextView: TextView
    }
}
