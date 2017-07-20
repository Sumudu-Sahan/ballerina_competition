package wso2hackethon.finite4.trash;

import android.graphics.Color;

/**
 * Created by Sumudu on 7/15/2017.
 */

public class Data {

    private String[] NAVIGATION_ITEM_NAME = new String[]{"Home", "Notifications", "My Account", "Logout"};
    private int[] NAVIGATION_ITEM_ICON = new int[]{R.drawable.ic_home, R.drawable.ic_bell, R.drawable.ic_account, R.drawable.ic_logout};

    private int[] colorsForSwiper = new int[]{Color.parseColor("#ff8000"), Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"), Color.parseColor("#000000")};

    private int FACEBOOK = 1;
    private int GOOGLEPLUS = 2;
    private int DEFAULTEMAIL = 3;

    private String SERVER_PHP_ROOT_PATH = "http://192.168.1.11/trash_hunter/PHP/";
    private String SERVER_IMG_ROOT_PATH= "http://192.168.1.11/trash_hunter/img/";

    public String getSERVER_PHP_ROOT_PATH() {
        return SERVER_PHP_ROOT_PATH;
    }

    public String getSERVER_IMG_ROOT_PATH() {
        return SERVER_IMG_ROOT_PATH;
    }

    public String[] getNAVIGATION_ITEM_NAME() {
        return NAVIGATION_ITEM_NAME;
    }

    public int[] getNAVIGATION_ITEM_ICON() {
        return NAVIGATION_ITEM_ICON;
    }

    public int getFACEBOOK() {
        return FACEBOOK;
    }

    public int getGOOGLEPLUS() {
        return GOOGLEPLUS;
    }

    public int getDEFAULTEMAIL() {
        return DEFAULTEMAIL;
    }

    public int[] getColorsForSwiper() {
        return colorsForSwiper;
    }
}
