package wso2hackethon.finite4.trash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Sumudu on 7/22/2017.
 */

public class SplashActivity extends Activity {
    Data data = new Data();
    NetworkStatChecker n = new NetworkStatChecker();
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);
        context = SplashActivity.this;

        new CheckInternetConnection().execute();
    }

    private void createDirectory() {
        if (!data.getSTORAGE_PATH().exists()) {
            data.getSTORAGE_PATH().mkdirs();
        }
    }

    private boolean checkDirectoryAvailable() {
        if (!data.getSTORAGE_PATH().exists()) {
            return false;
        } else return true;
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){}

    class CheckInternetConnection extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try{
                boolean dirCheck = checkDirectoryAvailable();
                if(dirCheck){

                }
                else{
                    createDirectory();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return n.isConnected(context);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                SharedPreferences spForLogin = getSharedPreferences("loginCredentials", MODE_PRIVATE);
                String user_role = spForLogin.getString("user_role", "");

                switch(user_role){
                    case "People" :
                        Intent i1 = new Intent(context, MainMenuForPeopleActivity.class);
                        startActivity(i1);
                        finish();
                        break;

                    case "MC" :
                        Intent i2 = new Intent(context, MainMenuForMCActivity.class);
                        startActivity(i2);
                        finish();
                        break;

                    case "Tractor" :
                        Intent i3 = new Intent(context, MainMenuForTractorActivity.class);
                        startActivity(i3);
                        finish();
                        break;

                    default:
                        Intent i4 = new Intent(context, LoginActivity.class);
                        startActivity(i4);
                        finish();
                        break;
                }
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
