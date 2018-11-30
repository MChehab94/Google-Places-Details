package mchehab.com.googleplacesdetails.placedetail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import mchehab.com.googleplacesdetails.R


class ImageFragment : Fragment() {

    val listURLs = ArrayList<String>()

    private lateinit var listView: ListView
    private lateinit var textView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        listView = view.findViewById(R.id.listView)
        textView = view.findViewById(R.id.textView)
        val list = arguments?.getStringArrayList("photos")
        if (list == null || list.size == 0){
            showTextView()
        }else{
            listURLs.addAll(list)
            listView.adapter = ImageAdapter()
        }
        return view
    }

    private fun showTextView(){
        textView.text = "This place doesn't have any photos at the moment"
        textView.visibility = View.VISIBLE
        listView.visibility = View.GONE
    }

    private inner class ImageAdapter: BaseAdapter(){

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val viewHolder: ViewHolder
            var view = convertView
            if (convertView == null){
                view = LayoutInflater.from(activity).inflate(R.layout.image_row, null)

                viewHolder = ViewHolder()
                viewHolder.imageView = view.findViewById(R.id.imageView)

                view.tag = viewHolder
            }else{
                viewHolder = convertView.tag as ViewHolder
            }
            Picasso.get()
                .load(listURLs[position])
                .resize(600, 600)
                .into(viewHolder.imageView)
            return view!!
        }

        override fun getItem(position: Int): Any {
            return listURLs[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listURLs.size
        }

        private inner class ViewHolder{
            lateinit var imageView: ImageView
        }
    }
}