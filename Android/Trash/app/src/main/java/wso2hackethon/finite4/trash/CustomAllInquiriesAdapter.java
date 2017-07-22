package wso2hackethon.finite4.trash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by WAKENSYS on 5/29/2017.
 */

public class CustomAllInquiriesAdapter extends BaseAdapter {
    Context context;
    Activity activity;

    String[]
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
            user_image;

    public CustomAllInquiriesAdapter(
            Context context,
            Activity activity,

            String[] garbage_post_id,
            String[] garbage_post_title,
            String[] garbage_post_description,
            String[] garbage_post_image,
            String[] garbage_post_lat,
            String[] garbage_post_lon,
            String[] garbage_post_status,
            String[ ]garbage_post_status_note,
            String[] garbage_post_added_datetime,
            String[] user_email,
            String[] user_account_type,
            String[] user_name,
            String[] user_image){

        this.context = context;
        this.activity = activity;

        this.garbage_post_id = garbage_post_id;
        this.garbage_post_title = garbage_post_title;
        this.garbage_post_description = garbage_post_description;
        this.garbage_post_image = garbage_post_image;
        this.garbage_post_lat = garbage_post_lat;
        this.garbage_post_lon = garbage_post_lon;
        this.garbage_post_status = garbage_post_status;
        this.garbage_post_status_note = garbage_post_status_note;
        this.garbage_post_added_datetime = garbage_post_added_datetime;

        this.user_email = user_email;
        this.user_account_type = user_account_type;
        this.user_name = user_name;
        this.user_image = user_image;
    }

    @Override
    public int getCount() {
        return garbage_post_id.length;
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
        final View gvContent = inflater.inflate(R.layout.inquiry_item_layout, null);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        gvContent.startAnimation(animation);

        final ImageView inquiryImageDisplay = (ImageView) gvContent.findViewById(R.id.inquiryImageDisplay);
        final ImageView viewOnMapButton = (ImageView) gvContent.findViewById(R.id.viewOnMapButton);
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Picasso
                .with(context)
                .load(garbage_post_image[position])
                .fit()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(inquiryImageDisplay, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso
                                .with(context)
                                .load(garbage_post_image[position])
                                .fit()
                                .into(inquiryImageDisplay);
                    }
                });

        final TextView inquiryTitleDisplay = (TextView) gvContent.findViewById(R.id.inquiryTitleDisplay);
        final TextView inquiryDescriptionDisplay = (TextView) gvContent.findViewById(R.id.inquiryDescriptionDisplay);
        final TextView inquiryAddedUserNameDisplay = (TextView) gvContent.findViewById(R.id.inquiryAddedUserNameDisplay);
        final TextView inquiryDateTimeDisplay = (TextView) gvContent.findViewById(R.id.inquiryDateTimeDisplay);
        final TextView inquiryResultDisplay = (TextView) gvContent.findViewById(R.id.inquiryResultDisplay);

        final CardView inquiryItemButton = (CardView) gvContent.findViewById(R.id.inquiryItemButton);
        inquiryItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog alertDialog = new Dialog(context);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(R.layout.inquiry_full_view_layout);
                alertDialog.setCancelable(true);

                Window window = alertDialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                final ImageView inquiryFullViewImageDisplay = (ImageView) alertDialog.findViewById(R.id.inquiryFullViewImageDisplay);
                Picasso
                        .with(context)
                        .load(garbage_post_image[position])
                        .fit()
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(inquiryFullViewImageDisplay, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso
                                        .with(context)
                                        .load(garbage_post_image[position])
                                        .fit()
                                        .into(inquiryFullViewImageDisplay);
                            }
                        });

                final TextView inquiryFullViewTitleDisplay = (TextView) alertDialog.findViewById(R.id.inquiryFullViewTitleDisplay);
                final TextView inquiryFullViewDescriptionDisplay = (TextView) alertDialog.findViewById(R.id.inquiryFullViewDescriptionDisplay);
                final TextView inquiryFullViewAddedUserNameDisplay = (TextView) alertDialog.findViewById(R.id.inquiryFullViewAddedUserNameDisplay);
                final TextView inquiryFullViewAddedDateTimeDisplay = (TextView) alertDialog.findViewById(R.id.inquiryFullViewAddedDateTimeDisplay);

                final TextView statusDisplay = (TextView) alertDialog.findViewById(R.id.statusDisplay);
                final TextView statusNoteDisplay = (TextView) alertDialog.findViewById(R.id.statusNoteDisplay);

                inquiryFullViewTitleDisplay.setText(garbage_post_title[position]);
                inquiryFullViewDescriptionDisplay.setText(garbage_post_description[position]);
                inquiryFullViewAddedUserNameDisplay.setText(user_name[position]);
                inquiryFullViewAddedDateTimeDisplay.setText(garbage_post_added_datetime[position]);

                statusDisplay.setText(garbage_post_status[position]);
                statusNoteDisplay.setText(garbage_post_status_note[position]);

                alertDialog.show();
            }
        });

        inquiryTitleDisplay.setText(garbage_post_title[position]);
        inquiryDescriptionDisplay.setText(garbage_post_description[position]);
        inquiryAddedUserNameDisplay.setText(user_name[position]);
        inquiryDateTimeDisplay.setText(garbage_post_added_datetime[position]);

        switch (garbage_post_status[position]) {
            case "Pending":
                inquiryResultDisplay.setText("Pending");
                inquiryResultDisplay.setTextColor(Color.parseColor("#ff8000"));
                break;

            case "Solved":
                inquiryResultDisplay.setText("Solved");
                inquiryResultDisplay.setTextColor(Color.parseColor("#00aa00"));
                break;

            case "Rejected":
                inquiryResultDisplay.setText("Rejected");
                inquiryResultDisplay.setTextColor(Color.parseColor("#aa0000"));
                break;

            default:
                inquiryResultDisplay.setText("Unknown");
                inquiryResultDisplay.setTextColor(Color.parseColor("#000000"));
                break;
        }
        return gvContent;
    }
}
