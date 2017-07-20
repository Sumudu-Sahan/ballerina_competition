package wso2hackethon.finite4.trash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by WAKENSYS on 7/19/2017.
 */

public class ViewProfileActivityForTractor extends Activity {
    Context context;
    ImageView backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_activity_for_tractor);
        context = ViewProfileActivityForTractor.this;

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonOption();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){
        Intent i = new Intent(context, MainMenuForTractorActivity.class);
        startActivity(i);
        finish();
    }
}
