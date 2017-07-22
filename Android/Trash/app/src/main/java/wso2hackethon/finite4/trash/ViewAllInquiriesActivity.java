package wso2hackethon.finite4.trash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Sumudu on 7/22/2017.
 */

public class ViewAllInquiriesActivity extends Activity {
    DBOperations dbOperations = new DBOperations();
    Data data = new Data();
    NetworkStatChecker n = new NetworkStatChecker();

    Context context;

    ListView
            inquiryListView;

    LinearLayout
            hiddenLayout;

    SwipeRefreshLayout
            swiper;

    ImageView backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_inquries_layout);
        context = ViewAllInquiriesActivity.this;

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonOption();
            }
        });

        hiddenLayout = (LinearLayout) findViewById(R.id.hiddenLayout);
        inquiryListView = (ListView) findViewById(R.id.inquiryListView);

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setColorSchemeColors(data.getColorsForSwiper());
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiper.setRefreshing(true);
                new GetAllInquiries().execute();
            }
        });

        new GetAllInquiries().execute();
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){
        Intent i = new Intent(context, MainMenuForPeopleActivity.class);
        startActivity(i);
        finish();
    }

    class GetAllInquiries extends AsyncTask<String, Void, String[][]> {
        boolean internetAvailable = false;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                String[][] res = dbOperations.getAllInquiries();
                return res;
            }
            else{
                this.internetAvailable = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] result) {
            if(result != null){
                try{
                    String[] garbage_post_id = result[0];
                    String[] garbage_post_title = result[1];
                    String[] garbage_post_description = result[2];
                    String[] garbage_post_image = result[3];
                    String[] garbage_post_lat = result[4];
                    String[] garbage_post_lon = result[5];
                    String[] garbage_post_status = result[6];
                    String[] garbage_post_status_note = result[7];
                    String[] garbage_post_added_datetime = result[8];

                    String[] user_email = result[9];
                    String[] user_account_type = result[10];
                    String[] user_name = result[11];
                    String[] user_image = result[12];

                    CustomAllInquiriesAdapter customAllInquiriesAdapter = new CustomAllInquiriesAdapter(
                            context,
                            ViewAllInquiriesActivity.this,

                            garbage_post_id,
                            garbage_post_title,
                            garbage_post_description,
                            garbage_post_image,
                            garbage_post_lat,
                            garbage_post_lon,
                            garbage_post_status,
                            garbage_post_status_note,
                            garbage_post_added_datetime,
                            user_email,
                            user_account_type,
                            user_name,
                            user_image
                    );
                    customAllInquiriesAdapter.notifyDataSetChanged();
                    inquiryListView.setAdapter(customAllInquiriesAdapter);

                    inquiryListView.setVisibility(View.VISIBLE);
                    hiddenLayout.setVisibility(View.GONE);
                }
                catch (Exception e){
                    if(this.internetAvailable){
                        Toast.makeText(context, "No inquiries found", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                    inquiryListView.setVisibility(View.GONE);
                    hiddenLayout.setVisibility(View.VISIBLE);
                }
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "No inquiries found", Toast.LENGTH_SHORT).show();
                inquiryListView.setVisibility(View.GONE);
                hiddenLayout.setVisibility(View.VISIBLE);
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                inquiryListView.setVisibility(View.GONE);
                hiddenLayout.setVisibility(View.VISIBLE);
            }

            try{
                swiper.setRefreshing(false);
                progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }
}
