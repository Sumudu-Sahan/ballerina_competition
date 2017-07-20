package wso2hackethon.finite4.trash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Sumudu on 7/15/2017.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Data data = new Data();
    NetworkStatChecker n = new NetworkStatChecker();
    DBOperations dbOperations = new DBOperations();

    Context context;
    CallbackManager callbackManager;

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    CardView googleButton, facebookButton, signInButton;

    EditText userNameField, passwordField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_activity_layout);
        AppEventsLogger.activateApp(getApplication());
        context = LoginActivity.this;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */) //.enableAutoManage(FragmentActivity, OnConnectionFailedListener);
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelled to Login Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                Toast.makeText(LoginActivity.this, "Error to Login Facebook", Toast.LENGTH_SHORT).show();
            }
        });

        googleButton = (CardView) findViewById(R.id.googleButton);
        facebookButton = (CardView) findViewById(R.id.facebookButton);
        signInButton = (CardView) findViewById(R.id.signInButton);

        userNameField = (EditText) findViewById(R.id.userNameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userNameField.getText().toString().trim();
                String pass = passwordField.getText().toString().trim();

                if(email.equalsIgnoreCase("") || pass.equalsIgnoreCase("")){
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();

                    if(email.equalsIgnoreCase("")){
                        userNameField.setError("Please fill this field");
                    }
                    else{
                        userNameField.setError(null);
                    }

                    if(pass.equalsIgnoreCase("")){
                        passwordField.setError("Please fill this field");
                    }
                    else{
                        passwordField.setError(null);
                    }
                }

                else if(isValidEmail(email)){
                    new CheckCredentials(email, pass).execute();
                }
                else{
                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

       // userImageDisplay = (ImageView) findViewById(R.id.userImageDisplay);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            //emailDisplayFB.setText(jsonObject.getString("email"));
            // genderDisplayFB.setText(jsonObject.getString("gender"));
            // resultText.setText(jsonObject.getString("name"));

            System.out.println("FB JSON: " + jsonObject.toString());

            //Bitmap bitmap = getFacebookProfilePicture(jsonObject.getString("id"));


            new SendLoginDetailsSocialLogin(data.getFACEBOOK(), "Facebook", jsonObject.getString("name"), jsonObject.getString("email"), "https://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large").execute();
            // profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            // profilePictureView.setProfileId(jsonObject.getString("id"));
            // profilePictureView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sharePhotoToFacebook(){
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("Give me my codez or I will ... you know, do that thing you don't like!")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //emailDisplayGoogle.setText(acct.getEmail());
            //genderDisplayGoogle.setText(acct.getFamilyName());
            //resultText.setText(acct.getDisplayName());

            System.out.println("gname: " + acct.getDisplayName() + '\n' + "email: " + acct.getEmail());
            new SendLoginDetailsSocialLogin(data.getGOOGLEPLUS(), "Google Plus", acct.getDisplayName(), acct.getEmail(), acct.getPhotoUrl().toString()).execute();
            //profileIMG.setVisibility(View.VISIBLE);
          /*  Picasso
                    .with(context)
                    .load(acct.getPhotoUrl())
                    .fit()
                    .into(userImageDisplay);*/

            updateUI(true);
        } else {
            System.out.println("gname: " + result.getStatus() + '\n' + "email: " + result.toString());
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        // Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean signedIn) {
       /* if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.disconnect).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            userNameDisplayGoogle.setText("");
            genderDisplayGoogle.setText("");
            emailDisplayGoogle.setText("");
            profileIMG.setVisibility(View.INVISIBLE);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.disconnect).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }*/
    }

  /*  @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect:
                revokeAccess();
                break;
        }
    }*/

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){
        System.exit(0);
    }

    class SendLoginDetailsSocialLogin extends AsyncTask<String, Void, String> {
        boolean internetAvailable = false;
        ProgressDialog progressDialog;

        int
                loginType;
        String
                loginTypeAsString,
                userName,
                userEmail,
                userImage;

        public SendLoginDetailsSocialLogin(
                int loginType,
                String loginTypeAsString,
                String userName,
                String userEmail,
                String userImage){

            this.loginType = loginType;

            this.loginTypeAsString = loginTypeAsString;
            this.userName = userName;
            this.userEmail = userEmail;
            this.userImage = userImage;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected String doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                SharedPreferences logcatStart = getSharedPreferences("userCredentials", MODE_PRIVATE);
                String token = logcatStart.getString("userToken", "");
                String res = dbOperations.sendLoginDetailsSocial(loginType, userName, userEmail, userImage, token);
                return res;
            }
            else{
                this.internetAvailable = false;
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.equalsIgnoreCase("")){
                SharedPreferences spForLogin = getSharedPreferences("loginCredentials", MODE_PRIVATE);
                SharedPreferences.Editor spEditorForLogin = spForLogin.edit();
                spEditorForLogin.putString("user_id", result);
                spEditorForLogin.putString("user_email", userEmail);
                spEditorForLogin.putString("user_name", userName);
                spEditorForLogin.putString("user_role", "People");
                spEditorForLogin.putString("user_account_type", loginTypeAsString);
                spEditorForLogin.putString("user_image", userImage);
                spEditorForLogin.apply();

                Intent i = new Intent(context, MainMenuForPeopleActivity.class); // Edit this line
                startActivity(i);
                finish();
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "Invalid login information", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

            try{
                progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }

    class CheckCredentials extends AsyncTask<String, Void, String[]> {
        boolean internetAvailable = false;
        ProgressDialog progressDialog;

        String
                userName,
                userPassword;

        public CheckCredentials(String userName, String userPassword){
            this.userName = userName;
            this.userPassword = userPassword;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected String[] doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                SharedPreferences logcatStart = getSharedPreferences("userCredentials", MODE_PRIVATE);
                String token = logcatStart.getString("userToken", "");
                String[] res = dbOperations.checkCredentials(userName, userPassword, token);
                return res;
            }
            else{
                this.internetAvailable = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(result != null){
                try{
                    String user_role = "";
                    String user_id = result[0];
                    String user_email = result[1];
                    String user_name = result[2];
                    String user_account_type = result[4];
                    String user_image = result[5];

                    SharedPreferences spForLogin = getSharedPreferences("loginCredentials", MODE_PRIVATE);
                    SharedPreferences.Editor spEditorForLogin = spForLogin.edit();
                    spEditorForLogin.putString("user_id", user_id);
                    spEditorForLogin.putString("user_email", user_email);
                    spEditorForLogin.putString("user_name", user_name);

                    switch(result[3]){
                        case "1" :
                            user_role = "People";
                            break;

                        case "2" :
                            user_role = "MC";
                            break;

                        case "3" :
                            user_role = "Tractor";
                            break;
                    }

                    spEditorForLogin.putString("user_role", user_role);
                    spEditorForLogin.putString("user_account_type", user_account_type);
                    spEditorForLogin.putString("user_image", user_image);
                    spEditorForLogin.apply();

                    switch (result[3]){
                        case "1" :
                            Intent i1 = new Intent(context, MainMenuForPeopleActivity.class);
                            startActivity(i1);
                            finish();
                            break;

                        case "2" :
                            Intent i2 = new Intent(context, MainMenuForMCActivity.class);
                            startActivity(i2);
                            finish();
                            break;

                        case "3" :
                            Intent i3 = new Intent(context, MainMenuForTractorActivity.class);
                            startActivity(i3);
                            finish();
                            break;
                    }
                }
                catch (Exception e){
                    if(this.internetAvailable){
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

            try{
                progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }
}
