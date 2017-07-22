package wso2hackethon.finite4.trash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Sumudu on 7/22/2017.
 */

public class MainMenuForTractorActivity extends AppCompatActivity{
    Data data = new Data();
    NetworkStatChecker n = new NetworkStatChecker();
    DBOperations dbOperations = new DBOperations();

    Context context;
    ImageView nav;

    CardView viewMap, viewProfile;

    DrawerLayout drawerLayout; // drawer_layout
    NavigationView navigationView; // whatYouWantInLeftDrawer

    TextView userNameDisplay, userRoleDisplay, userEmailAddressDisplay, userAccountTypeDisplay;
    ImageView userImageDisplay;
    ListView navigationListView;

    String userName = "", userEmail = "", userID = "", userRole = "", userAccountType = "", userImage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractor_dashboard_activity);
        context = MainMenuForTractorActivity.this;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
        userRoleDisplay = (TextView) findViewById(R.id.userRoleNameDisplay);
        userEmailAddressDisplay = (TextView) findViewById(R.id.userEmailAddressDisplay);
        userAccountTypeDisplay = (TextView) findViewById(R.id.userAccountTypeDisplay);

        navigationListView = (ListView) findViewById(R.id.navigationListView);

        userImageDisplay = (ImageView) findViewById(R.id.userImageDisplay);

        SharedPreferences spForLogin = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        userID += spForLogin.getString("user_id", "");
        userEmail += spForLogin.getString("user_email", "");
        userName += spForLogin.getString("user_name", "");
        userRole += spForLogin.getString("user_role", "");
        userAccountType += spForLogin.getString("user_account_type", "");
        userImage += spForLogin.getString("user_image", "");

        userNameDisplay.setText(userName);
        userRoleDisplay.setText(userRole);
        userEmailAddressDisplay.setText(userEmail);
        userAccountTypeDisplay.setText(userAccountType);

        Picasso
                .with(context)
                .load(userImage)
                .error(R.drawable.default_user_image)
                .fit()
                .into(userImageDisplay);

        CustomNavigationAdapter customNavigationAdapter = new CustomNavigationAdapter(context, MainMenuForTractorActivity.this, data.getNAVIGATION_ITEM_NAME(), data.getNAVIGATION_ITEM_ICON());
        customNavigationAdapter.notifyDataSetChanged();
        navigationListView.setAdapter(customNavigationAdapter);

        nav = (ImageView) findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        viewProfile = (CardView) findViewById(R.id.viewProfile);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewProfileActivityForTractor.class);
                startActivity(i);
                finish();
            }
        });

        viewMap = (CardView) findViewById(R.id.viewMap);
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NextGarbagePointMapActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you really want to exit?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        System.exit(0);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
