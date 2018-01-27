package jaaxaw.countdownprojectv2.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jaaxaw.countdownprojectv2.R;
import jaaxaw.countdownprojectv2.sql.DatabaseHelper;

public class RegisterActivity extends Activity{

    private EditText _lastname, _firstname, _username, _email, _password;
    private Button _register_button;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;

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
