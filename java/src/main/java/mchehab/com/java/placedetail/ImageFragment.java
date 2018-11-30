package mchehab.com.java.placedetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import mchehab.com.java.R;

import java.util.List;

public class ImageFragment extends Fragment {

    private ListView listView;
    private TextView textView;
    private List<String> listURLs;

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        listView = view.findViewById(R.id.listView);
        textView = view.findViewById(R.id.textView);

        if (getArguments() == null){
            showTextView();
        }else{
            listURLs = getArguments().getStringArrayList("photos");
            if (listURLs == null || listURLs.size() == 0)
                showTextView();
            else
                listView.setAdapter(new ImageAdapter());
        }

        return view;
    }

    private void showTextView(){
        textView.setText("There are no images for this place at the moment");
        textView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private class ImageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listURLs.size();
        }

        @Override
        public Object getItem(int position) {
            return listURLs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.image_row, null);

                viewHolder = new ViewHolder();
                viewHolder.imageView = convertView.findViewById(R.id.imageView);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Picasso.get()
                    .load(listURLs.get(position))
                    .resize(600, 600)
                    .into(viewHolder.imageView);
            return convertView;
        }

        private class ViewHolder{
            ImageView imageView;
        }
    }
}