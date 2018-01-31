package jaaxaw.countdownprojectv2.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jaaxaw.countdownprojectv2.R;
import jaaxaw.countdownprojectv2.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText _username;
    private EditText _password;
    private Button _connect_button;
    private SQLiteOpenHelper onHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        onHelper = new DatabaseHelper(this);

        _username = (EditText) findViewById(R.id.username);
        _password = (EditText) findViewById(R.id.password);
        _connect_button = (Button) findViewById(R.id.connect_btn);

        _connect_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db = onHelper.getReadableDatabase();
                String username = _username.getText().toString();
                String password = _password.getText().toString();
                String[] args = {username, password};
                cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE username = ?" + "AND password=?", args);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToNext();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    } else
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
