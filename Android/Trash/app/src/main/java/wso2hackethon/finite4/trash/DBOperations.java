package wso2hackethon.finite4.trash;

import android.net.Uri;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sumudu on 7/15/2017.
 */

public class DBOperations {
    Data data = new Data();

    public boolean addNewPost(String userID, String title, String description, String lat, String lon, String fileName){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "addNewPost.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userID", userID)
                        .appendQueryParameter("title", title)
                        .appendQueryParameter("description", description)
                        .appendQueryParameter("lat", lat)
                        .appendQueryParameter("lon", lon)
                        .appendQueryParameter("fileName", fileName);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                try {
                    conn.connect();
                    return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);

                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return false;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public String[][] getAllNotifications(){
        String
                line = "",
                result = "",

                notification_id = "",
                notification_mainValue = "",
                notification_subValue1 = "",
                notification_subValue2 = "",
                notification_subValue3 = "",
                notification_subValue4 = "",
                notification_added_datetime = "";

        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "getAllNotifications.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONArray jArray = new JSONArray(result);
                            int count = jArray.length();

                            for (int i = 0; i < count; i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                notification_id += jObject.getString("notification_id") + "#";
                                notification_mainValue += jObject.getString("notification_mainValue") + "#";
                                notification_added_datetime += jObject.getString("notification_added_datetime") + "#";

                                if (jObject.getString("notification_subValue1").isEmpty() || jObject.getString("notification_subValue1").equalsIgnoreCase("") || jObject.getString("notification_subValue1").equalsIgnoreCase("null")) {
                                    notification_subValue1 += "-#";
                                } else {
                                    notification_subValue1 += jObject.getString("notification_subValue1") + "#";
                                }

                                if (jObject.getString("notification_subValue2").isEmpty() || jObject.getString("notification_subValue2").equalsIgnoreCase("") || jObject.getString("notification_subValue2").equalsIgnoreCase("null")) {
                                    notification_subValue2 += "-#";
                                } else {
                                    notification_subValue2 += jObject.getString("notification_subValue2") + "#";
                                }

                                if (jObject.getString("notification_subValue3").isEmpty() || jObject.getString("notification_subValue3").equalsIgnoreCase("") || jObject.getString("notification_subValue3").equalsIgnoreCase("null")) {
                                    notification_subValue3 += "-#";
                                } else {
                                    notification_subValue3 += jObject.getString("notification_subValue3") + "#";
                                }

                                if (jObject.getString("notification_subValue4").isEmpty() || jObject.getString("notification_subValue4").equalsIgnoreCase("") || jObject.getString("notification_subValue4").equalsIgnoreCase("null")) {
                                    notification_subValue4 += "-#";
                                } else {
                                    notification_subValue4 += jObject.getString("notification_subValue4") + "#";
                                }

                            }

                            return new String[][]{
                                    notification_id.split("#"),
                                    notification_mainValue.split("#"),
                                    notification_subValue1.split("#"),
                                    notification_subValue2.split("#"),
                                    notification_subValue3.split("#"),
                                    notification_subValue4.split("#"),
                                    notification_added_datetime.split("#")};
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }



    public String[][] getAllInquiries(){
        String
                line = "",
                result = "",

                garbage_post_id = "",
                user_email = "",
                user_account_type = "",
                user_name = "",
                user_image = "",
                garbage_post_title = "",
                garbage_post_description = "",
                garbage_post_image = "",
                garbage_post_lat = "",
                garbage_post_lon = "",
                garbage_post_status = "",
                garbage_post_status_note = "",
                garbage_post_added_datetime = "";

        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "getAllInquiries.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONArray jArray = new JSONArray(result);
                            int count = jArray.length();

                            for (int i = 0; i < count; i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                user_email += jObject.getString("user_email") + "#";
                                user_account_type += jObject.getString("user_account_type") + "#";
                                user_name += jObject.getString("user_name") + "#";

                                if (jObject.getString("user_image").isEmpty() || jObject.getString("user_image").equalsIgnoreCase("") || jObject.getString("user_image").equalsIgnoreCase("null")) {
                                    user_image += "-#";
                                } else {
                                    user_image += jObject.getString("user_image") + "#";
                                }

                                garbage_post_id += jObject.getString("garbage_post_id") + "#";
                                garbage_post_title += jObject.getString("garbage_post_title") + "#";
                                if (jObject.getString("garbage_post_description").isEmpty() || jObject.getString("garbage_post_description").equalsIgnoreCase("") || jObject.getString("garbage_post_description").equalsIgnoreCase("null")) {
                                    garbage_post_description += "-#";
                                } else {
                                    garbage_post_description += jObject.getString("garbage_post_description") + "#";
                                }

                                if (jObject.getString("garbage_post_image").isEmpty() || jObject.getString("garbage_post_image").equalsIgnoreCase("") || jObject.getString("garbage_post_image").equalsIgnoreCase("null")) {
                                    garbage_post_image += "-#";
                                } else {
                                    garbage_post_image += jObject.getString("garbage_post_image") + "#";
                                }

                                garbage_post_lat += jObject.getString("garbage_post_lat") + "#";
                                garbage_post_lon += jObject.getString("garbage_post_lon") + "#";

                                garbage_post_added_datetime += jObject.getString("garbage_post_added_datetime") + "#";

                                if (jObject.getString("garbage_post_status_note").isEmpty() || jObject.getString("garbage_post_status_note").equalsIgnoreCase("") || jObject.getString("garbage_post_status_note").equalsIgnoreCase("null")) {
                                    garbage_post_status_note += "-#";
                                } else {
                                    garbage_post_status_note += jObject.getString("garbage_post_status_note") + "#";
                                }

                                switch (jObject.getString("garbage_post_status")) {
                                    case "1":
                                        garbage_post_status += "Pending#";
                                        break;

                                    case "2":
                                        garbage_post_status += "Solved#";
                                        break;

                                    case "3":
                                        garbage_post_status += "Rejected#";
                                        break;

                                    default:
                                        garbage_post_status += "Unknown#";
                                        break;
                                }
                            }

                            return new String[][]{
                                    garbage_post_id.split("#"),
                                    garbage_post_title.split("#"),
                                    garbage_post_description.split("#"),
                                    garbage_post_image.split("#"),
                                    garbage_post_lat.split("#"),
                                    garbage_post_lon.split("#"),
                                    garbage_post_status.split("#"),
                                    garbage_post_status_note.split("#"),
                                    garbage_post_added_datetime.split("#"),
                                    user_email.split("#"),
                                    user_account_type.split("#"),
                                    user_name.split("#"),
                                    user_image.split("#")};
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }

    public String[][] getAllTractors() {
        String
                line = "",
                result = "",

                tractor_image = "",
                tractor_id = "",
                tractor_lat = "",
                tractor_lon = "",
                tractor_type = "";

        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "getAllTractors.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONArray jArray = new JSONArray(result);
                            int count = jArray.length();

                            for (int i = 0; i < count; i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                tractor_id += jObject.getString("tractor_id") + "#";
                                tractor_image += jObject.getString("tractor_image") + "#";
                                tractor_lat += jObject.getString("tractor_lat") + "#";
                                tractor_lon += jObject.getString("tractor_lon") + "#";

                                switch (jObject.getString("tractor_type")) {
                                    case "1":
                                        tractor_type += "Recycler#";
                                        break;

                                    case "2":
                                        tractor_type += "Non-Recycler#";
                                        break;
                                }
                            }

                            return new String[][]{tractor_id.split("#"), tractor_image.split("#"), tractor_lat.split("#"), tractor_lon.split("#"), tractor_type.split("#")};
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }

    public String[][] getAllNextGarbagePoints(String driverID){
        String
                line = "",
                result = "",

                garbage_collecting_point_id = "",
                garbage_collecting_point_name = "",
                garbage_collecting_point_description = "",
                garbage_collecting_point_lat = "",
                garbage_collecting_point_lon = "",
                garbage_point_driver_tractor_merge_status = "";

        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "getAllNextGarbagePoints.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("driverID", driverID);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONArray jArray = new JSONArray(result);
                            int count = jArray.length();

                            for (int i = 0; i < count; i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                garbage_collecting_point_id += jObject.getString("garbage_collecting_point_id") + "#";
                                garbage_collecting_point_name += jObject.getString("garbage_collecting_point_name") + "#";

                                if (jObject.getString("garbage_collecting_point_description").isEmpty() || jObject.getString("garbage_collecting_point_description").equalsIgnoreCase("") || jObject.getString("garbage_collecting_point_description").equalsIgnoreCase("null")) {
                                    garbage_collecting_point_description += "-#";
                                } else {
                                    garbage_collecting_point_description += jObject.getString("garbage_collecting_point_description") + "#";
                                }

                                garbage_collecting_point_lat += jObject.getString("garbage_collecting_point_lat") + "#";
                                garbage_collecting_point_lon += jObject.getString("garbage_collecting_point_lon") + "#";

                                garbage_point_driver_tractor_merge_status += jObject.getString("garbage_point_driver_tractor_merge_status") + "#";
                            }

                            return new String[][]{
                                    garbage_collecting_point_id.split("#"),
                                    garbage_collecting_point_name.split("#"),
                                    garbage_collecting_point_description.split("#"),
                                    garbage_collecting_point_lat.split("#"),
                                    garbage_collecting_point_lon.split("#"),
                                    garbage_point_driver_tractor_merge_status.split("#")};
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }

    public String[][] getAllGarbagePoints() {
        String
                line = "",
                result = "",

                garbage_post_id = "",
                garbage_post_user_id = "",
                garbage_post_title = "",
                garbage_post_description = "",
                garbage_post_image = "",
                garbage_post_lat = "",
                garbage_post_lon = "",
                garbage_post_status = "";

        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "getAllGarbagePoints.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONArray jArray = new JSONArray(result);
                            int count = jArray.length();

                            for (int i = 0; i < count; i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                garbage_post_id += jObject.getString("garbage_post_id") + "#";
                                garbage_post_user_id += jObject.getString("garbage_post_user_id") + "#";
                                garbage_post_title += jObject.getString("garbage_post_title") + "#";
                                if (jObject.getString("garbage_post_description").isEmpty() || jObject.getString("garbage_post_description").equalsIgnoreCase("") || jObject.getString("garbage_post_description").equalsIgnoreCase("null")) {
                                    garbage_post_description += "-#";
                                } else {
                                    garbage_post_description += jObject.getString("garbage_post_description") + "#";
                                }

                                if (jObject.getString("garbage_post_image").isEmpty() || jObject.getString("garbage_post_image").equalsIgnoreCase("") || jObject.getString("garbage_post_image").equalsIgnoreCase("null")) {
                                    garbage_post_image += "-#";
                                } else {
                                    garbage_post_image += jObject.getString("garbage_post_image") + "#";
                                }

                                garbage_post_lat += jObject.getString("garbage_post_lat") + "#";
                                garbage_post_lon += jObject.getString("garbage_post_lon") + "#";

                                switch (jObject.getString("garbage_post_status")) {
                                    case "1":
                                        garbage_post_status += "Pending#";
                                        break;

                                    case "2":
                                        garbage_post_status += "Solved#";
                                        break;

                                    case "3":
                                        garbage_post_status += "Rejected#";
                                        break;

                                    default:
                                        garbage_post_status += "Unknown#";
                                        break;
                                }
                            }

                            return new String[][]{
                                    garbage_post_id.split("#"),
                                    garbage_post_user_id.split("#"),
                                    garbage_post_title.split("#"),
                                    garbage_post_description.split("#"),
                                    garbage_post_image.split("#"),
                                    garbage_post_lat.split("#"),
                                    garbage_post_lon.split("#"),
                                    garbage_post_status.split("#")};
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }

    public String[][] getAllGarbageRegularPoints() {
        String
                line = "",
                result = "",

                garbage_collecting_point_id = "",
                garbage_collecting_point_name = "",
                garbage_collecting_point_description = "",
                garbage_collecting_point_lat = "",
                garbage_collecting_point_lon = "";

        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "getAllGarbageRegularPoints.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONArray jArray = new JSONArray(result);
                            int count = jArray.length();

                            for (int i = 0; i < count; i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                garbage_collecting_point_id += jObject.getString("garbage_collecting_point_id") + "#";
                                garbage_collecting_point_name += jObject.getString("garbage_collecting_point_name") + "#";
                                if (jObject.getString("garbage_collecting_point_description").isEmpty() || jObject.getString("garbage_collecting_point_description").equalsIgnoreCase("") || jObject.getString("garbage_collecting_point_description").equalsIgnoreCase("null")) {
                                    garbage_collecting_point_description += "-#";
                                } else {
                                    garbage_collecting_point_description += jObject.getString("garbage_collecting_point_description") + "#";
                                }
                                garbage_collecting_point_lat += jObject.getString("garbage_collecting_point_lat") + "#";
                                garbage_collecting_point_lon += jObject.getString("garbage_collecting_point_lon") + "#";
                            }

                            return new String[][]{
                                    garbage_collecting_point_id.split("#"),
                                    garbage_collecting_point_name.split("#"),
                                    garbage_collecting_point_description.split("#"),
                                    garbage_collecting_point_lat.split("#"),
                                    garbage_collecting_point_lon.split("#")};
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }

    public boolean setCollectingResponse(String pointID, String results){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "setCollectingResponse.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("pointID", pointID)
                        .appendQueryParameter("results", results);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                try {
                    conn.connect();
                    return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);

                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return false;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return false;
        }
    }

    public String sendLoginDetailsSocial(int loginType, String userName, String userEmail, String userImage, String token) {
        String
                line,
                result,

                user_id = "";
        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "sendLoginDetailsSocial.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("loginType", String.valueOf(loginType))
                        .appendQueryParameter("userName", userName)
                        .appendQueryParameter("userEmail", userEmail)
                        .appendQueryParameter("userImage", userImage)
                        .appendQueryParameter("token", token);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONObject jObject = new JSONObject(result);
                            user_id += jObject.getString("userID");
                            return user_id;
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }

    public String[] checkCredentials(String userName, String password, String token) {
        String
                line,
                result,

                user_id = "", user_email = "", user_name = "", user_role = "", user_account_type = "", user_image = "";
        InputStream is;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH().trim() + "checkCredentials.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userName", userName)
                        .appendQueryParameter("password", password)
                        .appendQueryParameter("token", token);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                try {
                    conn.connect();
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        is.close();
                        try {
                            JSONArray jArray = new JSONArray(result);
                            int count = jArray.length();

                            for (int i = 0; i < count; i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                user_id += jObject.getString("user_id");
                                user_email += jObject.getString("user_email");
                                user_name += jObject.getString("user_name");

                                user_role += jObject.getString("user_role");

                                switch (jObject.getString("user_account_type")) {
                                    case "1":
                                        user_account_type += "Facebook";
                                        break;

                                    case "2":
                                        user_account_type += "Google+";
                                        break;

                                    case "3":
                                        user_account_type += "Normal";
                                        break;
                                }

                                if (jObject.getString("user_image").equalsIgnoreCase("") || jObject.getString("user_image").isEmpty() || jObject.getString("user_image").equalsIgnoreCase("null")) {
                                    user_image += "-";
                                } else {
                                    user_image += jObject.getString("user_image");
                                }
                            }

                            return new String[]{user_id, user_email, user_name, user_role, user_account_type, user_image};
                        } catch (Exception ex) {
                            System.out.println("Ex " + ex.toString());
                            return null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Ex " + ex.toString());
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Ex " + e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println("Ex " + ex.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Ex " + ex.toString());
            return null;
        }
    }
}
