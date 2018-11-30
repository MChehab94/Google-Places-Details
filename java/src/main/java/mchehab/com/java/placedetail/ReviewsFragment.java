package mchehab.com.java.placedetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import mchehab.com.java.R;

import java.util.List;

public class ReviewsFragment extends Fragment {

    private List<String> listReviews;

    private TextView textView;
    private ListView listView;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        textView = view.findViewById(R.id.textView);
        listView = view.findViewById(R.id.listView);

        if (getArguments() == null){
            showTextView();
        }else{
            listReviews = getArguments().getStringArrayList("reviews");
            if (listReviews == null || listReviews.size() == 0)
                showTextView();
            else
                listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listReviews));
        }

        return view;
    }

    private void showTextView(){
        textView.setText("This place doesn't have any reviews at the moment");
        textView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }
}