package wso2hackethon.finite4.trash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Sumudu on 7/22/2017.
 */

public class PostFullViewMapActivity extends Activity {
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_full_view_map_activity);
        context = PostFullViewMapActivity.this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backButtonOption(){
        Intent i = new Intent(context, MainMenuForPeopleActivity.class);
        startActivity(i);
        finish();
    }
}
