package com.lxj.sendmail.file

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.GridLayoutManager
import com.lxj.sendmail.MyApplication

import com.lxj.sendmail.R
import kotlinx.android.synthetic.main.activity_file.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File

class FileActivity : AppCompatActivity(), ListAdapter.ListAdapterListener {

    private lateinit var mAdapter: ListAdapter

    companion object{
        const val FILE_PATH  = "file_path"

        fun openFileActivity(context: Context, filePath: String){
          context.startActivity<FileActivity>(
              FILE_PATH to filePath
          )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)


        mAdapter = ListAdapter()
        rv_recycler.layoutManager = GridLayoutManager(this, 4)
        rv_recycler.adapter = mAdapter
        mAdapter.setList(getSdCardFile())
        mAdapter.setListAdapterListener(this)
        if (getSdCardFile().isNotEmpty()) {
            rv_recycler.visibility = VISIBLE
            tv_empty.visibility = GONE
        } else {
            rv_recycler.visibility = GONE
            tv_empty.visibility - VISIBLE
        }
    }

    override fun itemOnClick(fileName: String) {
        val filePath = intent.getStringExtra(FILE_PATH) + "/" + fileName
        if (File(filePath).isDirectory) {
            openFileActivity(this, filePath)
        } else {
            MyApplication.setFileName(filePath)
            toast("选择了【$fileName】")
        }
    }


    fun getSdCardFile(): List<FileModel> {
        val list = mutableListOf<FileModel>()
        File(intent.getStringExtra(FILE_PATH)).listFiles().forEach {
            list.add(FileModel(it.isDirectory, it.name))
        }
        return list
    }
}
