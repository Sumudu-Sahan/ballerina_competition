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
 * Created by WAKENSYS on 7/20/2017.
 */

public class NotificationActivity extends Activity {
    Data data = new Data();
    DBOperations dbOperations = new DBOperations();
    NetworkStatChecker n = new NetworkStatChecker();

    Context context;
    ImageView backButton;

    SwipeRefreshLayout swiper;
    ListView notificationListView;

    LinearLayout hiddenLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notifications_layout);
        context = NotificationActivity.this;

        hiddenLayout = (LinearLayout) findViewById(R.id.hiddenLayout);
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonOption();
            }
        });

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setColorSchemeColors(data.getColorsForSwiper());
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        hiddenLayout = (LinearLayout) findViewById(R.id.hiddenLayout);
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

    class GetAllNotifications extends AsyncTask<String, Void, String[][]> {
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
                String[][] res = dbOperations.getAllNotifications();
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
                    String[] notification_id = result[0];
                    String[] notification_mainValue = result[1];
                    String[] notification_subValue1 = result[2];
                    String[] notification_subValue2 = result[3];
                    String[] notification_subValue3 = result[4];
                    String[] notification_subValue4 = result[5];
                    String[] notification_added_datetime = result[6];

                    CustomAllNotificationAdapter customAllInquiriesAdapter = new CustomAllNotificationAdapter(
                            context,
                            NotificationActivity.this,

                            notification_id,
                            notification_mainValue,
                            notification_subValue1,
                            notification_subValue2,
                            notification_subValue3,
                            notification_subValue4,
                            notification_added_datetime
                    );
                    customAllInquiriesAdapter.notifyDataSetChanged();
                    notificationListView.setAdapter(customAllInquiriesAdapter);

                    notificationListView.setVisibility(View.VISIBLE);
                    hiddenLayout.setVisibility(View.GONE);
                }
                catch (Exception e){
                    if(this.internetAvailable){
                        Toast.makeText(context, "No notifications found", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                    notificationListView.setVisibility(View.GONE);
                    hiddenLayout.setVisibility(View.VISIBLE);
                }
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "No notifications found", Toast.LENGTH_SHORT).show();
                notificationListView.setVisibility(View.GONE);
                hiddenLayout.setVisibility(View.VISIBLE);
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                notificationListView.setVisibility(View.GONE);
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
