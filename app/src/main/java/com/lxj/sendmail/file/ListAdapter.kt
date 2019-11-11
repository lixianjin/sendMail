package com.lxj.sendmail.file

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lxj.sendmail.R
import java.io.File

/**
 * @description
 * @author lixianjin
 * create on 2019-11-08 14:58
 */
class ListAdapter: RecyclerView.Adapter<ListAdapter.FileViewHolder>() {

    private var mList: List<FileModel> = listOf()
    private lateinit var mListener: ListAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        with(mList[position]) {
            holder.name.text = name
            if (isDir) {
                holder.icon.setBackgroundResource(R.drawable.dir)
            } else {
                holder.icon.setBackgroundResource(R.drawable.file)
            }
            holder.itemView.setOnClickListener {
                mListener.itemOnClick(name)
            }
        }
    }

    fun setList(list: List<FileModel>) {
        mList = list
        notifyDataSetChanged()
    }

    fun setListAdapterListener(listener: ListAdapterListener){
        mListener = listener
    }

    inner class FileViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.iv_icon)
        val name: TextView = itemView.findViewById(R.id.tv_name)
    }

    interface ListAdapterListener{
        fun itemOnClick(fileName: String)
    }
}