package wso2hackethon.finite4.trash;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Sumudu on 7/22/2017.
 */

public class CustomAllNotificationAdapter extends BaseAdapter {
    Context context;
    Activity activity;

    String[]
            notification_id,
            notification_mainValue,
            notification_subValue1,
            notification_subValue2,
            notification_subValue3,
            notification_subValue4,
            notification_added_datetime;

    public CustomAllNotificationAdapter(
            Context context,
            Activity activity,

            String[] notification_id,
            String[] notification_mainValue,
            String[] notification_subValue1,
            String[] notification_subValue2,
            String[] notification_subValue3,
            String[] notification_subValue4,
            String[] notification_added_datetime){

        this.context = context;
        this.activity = activity;

        this.notification_id = notification_id;
        this.notification_mainValue = notification_mainValue;
        this.notification_subValue1 = notification_subValue1;
        this.notification_subValue2 = notification_subValue2;
        this.notification_subValue3 = notification_subValue3;
        this.notification_subValue4 = notification_subValue4;
        this.notification_added_datetime = notification_added_datetime;
    }

    @Override
    public int getCount() {
        return notification_id.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
