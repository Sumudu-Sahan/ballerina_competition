package wso2hackethon.finite4.trash;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sumudu on 7/22/2017.
 */
public class NetworkStatChecker {
    static Data data = new Data();
    public Boolean isConnected(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()){
            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "ping.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                System.out.println("response Code: " + conn.getResponseCode());

                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                   return true;
                }
                else{
                    Toast.makeText(context, String.valueOf(conn.getResponseCode()), Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }
    public Boolean isWifiConnected(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
    }

    public Boolean isEthernetConnected(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET);
    }
}
