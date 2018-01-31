package jaaxaw.countdownprojectv2.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jaaxaw.countdownprojectv2.R;
import jaaxaw.countdownprojectv2.sql.DatabaseHelper;

import static android.R.attr.data;
//import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

public class RegisterActivity extends Activity{

    private EditText _lastname, _firstname, _username, _email, _password;
    private Button _register_button, _login_direction;
    private LoginButton loginButton;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    public static final String TAG = "login_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        openHelper = new DatabaseHelper(this);

        _lastname = (EditText) findViewById(R.id.lastname);
        _firstname = (EditText) findViewById(R.id.firstname);
        _username = (EditText) findViewById(R.id.username);
        _email = (EditText) findViewById(R.id.email);
        _password = (EditText) findViewById(R.id.password);
        _register_button = (Button) findViewById(R.id.register_btn);
        _login_direction = (Button) findViewById(R.id.login_direction);

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("fb_connexion", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("fb_connexion", "facebook:onError", error);
                // ...
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    goMainScreen();
            }
        };


        _register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db = openHelper.getWritableDatabase();
                String lastname = _lastname.getText().toString();
                String firstname = _firstname.getText().toString();
                String username = _username.getText().toString();
                String email = _email.getText().toString();
                String password = _password.getText().toString();
                insertDatas(lastname,firstname,username,email,password);
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        });

        _login_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }

    private void goMainScreen(){
        Intent home_intent = new Intent(this, HomeActivity.class);
        startActivity(home_intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"You're logged in",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void insertDatas(String lastname, String firstname, String username, String email, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.LASTNAME,lastname);
        contentValues.put(DatabaseHelper.FIRSTNAME,firstname);
        contentValues.put(DatabaseHelper.USERNAME,username);
        contentValues.put(DatabaseHelper.EMAIL,email);
        contentValues.put(DatabaseHelper.PASSWORD,password);
        db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
    }
}
