package mchehab.com.googleplacesdetails

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class ListViewDialog(private val context: Context, private val list: List<GooglePlace>,
                     private val placeSelectedListener: PlaceSelectedListener) {
    private var dialog: Dialog? = null
    private var view: View? = null
    private var listView: ListView? = null
    private var buttonDismiss: Button? = null

    init {
        setupDialog()
        setupUI()
        setupListView()
        setButtonClickListener()
        setListViewItemClickListener()
    }

    fun showDialog() {
        dialog!!.show()
    }

    private fun setupDialog() {
        dialog = Dialog(context)
        view = LayoutInflater.from(context).inflate(R.layout.listview_dialog, null)
        dialog!!.setContentView(view!!)
    }

    private fun setupUI() {
        listView = view!!.findViewById(R.id.listView)
        buttonDismiss = view!!.findViewById(R.id.buttonDismiss)
    }

    private fun setupListView() {
        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        listView!!.adapter = arrayAdapter
    }

    private fun setButtonClickListener() {
        buttonDismiss!!.setOnClickListener { e -> dialog!!.dismiss() }
    }

    private fun setListViewItemClickListener() {
        listView!!.setOnItemClickListener { parent, view, position, id ->
            placeSelectedListener.getPlace(list[position])
        }
    }
}