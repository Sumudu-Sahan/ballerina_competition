package wso2hackethon.finite4.trash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Sumudu on 7/22/2017.
 */

public class CustomNavigationAdapter extends BaseAdapter {
    Context context;
    Activity activity;

    String[] navigationItemList;
    int[] navigationIconList;

    public CustomNavigationAdapter(Context context, Activity activity, String[] navigationItemList, int[] navigationIconList){
        this.context = context;
        this.activity = activity;

        this.navigationItemList = navigationItemList;
        this.navigationIconList = navigationIconList;
    }

    @Override
    public int getCount() {
        return navigationItemList.length;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gvContent = inflater.inflate(R.layout.navigation_item_layout, null);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        gvContent.startAnimation(animation);

        final ImageView navigationImageDisplay = (ImageView) gvContent.findViewById(R.id.navigationImageDisplay);
        Picasso
                .with(context)
                .load(navigationIconList[position])
                .fit()
                .into(navigationImageDisplay);

        final TextView textDisplay = (TextView) gvContent.findViewById(R.id.navigationItemDisplay);

        final CardView navigationItem = (CardView) gvContent.findViewById(R.id.navigationItem);
        navigationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0 :
                        SharedPreferences spForLogin = context.getSharedPreferences("loginCredentials", 0);
                        String userAccountType = spForLogin.getString("user_account_type", "");
                        switch (userAccountType){
                            case "People" :
                                Intent i1 = new Intent(context, MainMenuForPeopleActivity.class);
                                context.startActivity(i1);
                                activity.finish();
                                break;

                            case "MC" :
                                Intent i2 = new Intent(context, MainMenuForMCActivity.class);
                                context.startActivity(i2);
                                activity.finish();
                                break;

                            case "Tractor" :
                                Intent i3 = new Intent(context, MainMenuForTractorActivity.class);
                                context.startActivity(i3);
                                activity.finish();
                                break;

                            default:
                                Intent i4 = new Intent(context, LoginActivity.class);
                                context.startActivity(i4);
                                activity.finish();
                                break;
                        }
                        break;

                    case 1 :
                        break;

                    case 2 :
                        break;

                    case 3 :
                        SharedPreferences spForLogin1 = context.getSharedPreferences("loginCredentials", 0);
                        SharedPreferences.Editor spEditorForLogin1 = spForLogin1.edit();
                        spEditorForLogin1.putString("user_id", "");
                        spEditorForLogin1.putString("user_email", "");
                        spEditorForLogin1.putString("user_name", "");
                        spEditorForLogin1.putString("user_role", "");
                        spEditorForLogin1.putString("user_account_type", "");
                        spEditorForLogin1.putString("user_image", "");
                        spEditorForLogin1.apply();

                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i
                        );
                        activity.finish();
                        break;
                }
            }
        });

        textDisplay.setText(navigationItemList[position]);
        return gvContent;
    }
}
