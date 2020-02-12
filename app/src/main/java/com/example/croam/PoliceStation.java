package com.example.croam;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class PoliceStation extends ListActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);

        new GetPlaces(this, getListView()).execute();

    }

    class GetPlaces extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Context context;
        private String[] placeName;
        private ListView listView;

        public GetPlaces(Context context, ListView listView) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listView = listView;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();

            listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, placeName));
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesService service = new PlacesService("AIzaSyAzX52Sv2TH5rvGYIQxLzRL7aZNwFqBzjM");
            List<Place> findPlaces = service.findPlaces(22.625071, 88.329514, "atm");

            placeName = new String[findPlaces.size()];

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);

                System.out.println(placeDetail.getName());
                placeName[i] = placeDetail.getName();

            }
            return null;
        }

    }
}
