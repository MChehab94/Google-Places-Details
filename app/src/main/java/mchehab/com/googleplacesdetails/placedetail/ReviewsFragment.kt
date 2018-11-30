package mchehab.com.googleplacesdetails.placedetail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import mchehab.com.googleplacesdetails.R


class ReviewsFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var textView: TextView

    private val listReviews = mutableListOf<Review>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reviews, container, false)
        textView = view.findViewById(R.id.textView)
        listView = view.findViewById(R.id.listView)
        val listReviews = arguments?.getStringArrayList("reviews")
        if (listReviews == null || listReviews.size == 0){
            showTextView()
        }else{
            listView.adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, listReviews)
        }
        return view
    }

    private fun showTextView(){
        textView.text = "This place doesn't have any photos at the moment"
        textView.visibility = View.VISIBLE
        listView.visibility = View.GONE
    }

    private inner class ReviewsAdapter: BaseAdapter(){

        override fun getCount(): Int {
            return listReviews.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return listReviews[position]
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            var viewHolder = ViewHolder()
            if (view == null){
                view = LayoutInflater.from(activity).inflate(R.layout.reviews_row, null)

                viewHolder.textViewReview = view.findViewById(R.id.textViewReview)
                viewHolder.textViewRating = view.findViewById(R.id.textViewRating)
                viewHolder.imageViewProfile = view.findViewById(R.id.imageViewProfile)

                view.tag = viewHolder
            }else{
                viewHolder = view.tag as ViewHolder
            }

            val review = listReviews[position]
            viewHolder.textViewReview.text = review.review
            viewHolder.textViewRating.text = "${review.rating}/5.0"
            Picasso.get()
                    .load(review.profilePicUrl)
                    .into(viewHolder.imageViewProfile)

            return view!!
        }

        private inner class ViewHolder{
            lateinit var textViewReview: TextView
            lateinit var textViewRating: TextView
            lateinit var imageViewProfile: ImageView
        }
    }
}