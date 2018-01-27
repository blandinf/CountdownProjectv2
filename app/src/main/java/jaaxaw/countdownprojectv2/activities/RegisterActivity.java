package jaaxaw.countdownprojectv2.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import jaaxaw.countdownprojectv2.R;
import jaaxaw.countdownprojectv2.sql.DatabaseHelper;

import static android.R.attr.data;
import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

public class RegisterActivity extends Activity{

    private EditText _lastname, _firstname, _username, _email, _password;
    private Button _register_button;
    private LoginButton loginButton;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private CallbackManager mCallbackManager;

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

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("fb_connexion", "facebook:onSuccess:" + loginResult);
                //handleFacebookAccessToken(loginResult.getAccessToken());
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
