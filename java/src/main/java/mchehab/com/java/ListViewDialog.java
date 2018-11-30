package mchehab.com.java;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class ListViewDialog {

    private PlaceSelectedListener placeSelectedListener;
    private List<GooglePlace> list;
    private Context context;
    private Dialog dialog;
    private View view;
    private ListView listView;
    private Button buttonDismiss;

    public ListViewDialog(Context context, List<GooglePlace> list, PlaceSelectedListener placeSelectedListener){
        this.context = context;
        this.list = list;
        this.placeSelectedListener = placeSelectedListener;
        setupDialog();
        setupUI();
        setupListView();
        setButtonClickListener();
        setListViewItemSelectedListener();
    }

    public void showDialog(){
        dialog.show();
    }

    private void setupDialog(){
        dialog = new Dialog(context);
        view = LayoutInflater.from(context).inflate(R.layout.listview_dialog, null);
        dialog.setContentView(view);
    }

    private void setupUI(){
        listView = view.findViewById(R.id.listView);
        buttonDismiss = view.findViewById(R.id.buttonDismiss);
    }

    private void setupListView() {
        ArrayAdapter<GooglePlace> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
    }

    private void setButtonClickListener() {
        buttonDismiss.setOnClickListener(e -> dialog.dismiss());
    }

    private void setListViewItemSelectedListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            GooglePlace googlePlace = list.get(position);
            placeSelectedListener.getPlace(googlePlace);
        });
    }
}