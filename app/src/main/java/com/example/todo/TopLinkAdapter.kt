package com.example.todo


import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService

import com.bumptech.glide.Glide
import com.example.todo.models.RecentLink
import com.example.todo.models.TopLink

class TopLinkAdapter(private val context: Context, private val dataList: List<TopLink>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            holder = ViewHolder()
            holder.title = view.findViewById(R.id.list_item_sampleLink)
            holder.total_clicks = view.findViewById(R.id.list_item_clicks)
            holder.orignal_image = view.findViewById(R.id.list_item_img)
            holder.web_link = view.findViewById(R.id.list_item_web_link)
            holder.created_at = view.findViewById(R.id.list_item_date)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val item = dataList[position]

        holder.title?.text = item.title
        holder.web_link?.text = item.web_link
        holder.total_clicks?.text = item.total_clicks.toString()
        val img_url = item.original_image
        val imgview = holder.orignal_image
        if (imgview != null) {
            Glide.with(this.context)
                .load(img_url)
                .into(imgview)
        }
        val str = item.created_at
        val selectedPart = str.substring(0, 10)
        holder.created_at?.text = selectedPart

        return view!!

    }
    private class ViewHolder {
        var title: TextView? = null
        var total_clicks: TextView? = null
        var orignal_image: ImageView? = null
        var created_at: TextView? = null
        var web_link: TextView? = null

    }

}