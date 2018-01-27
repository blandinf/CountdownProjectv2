package jaaxaw.countdownprojectv2.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="register";
    public static final String TABLE_NAME="register";
    public static final String ID="id";
    public static final String FIRSTNAME="firstname";
    public static final String LASTNAME="lastname";
    public static final String USERNAME="username";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REGISTER_TABLE = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,Lastname TEXT,Firstname TEXT,Username TEXT,Email TEXT, Password TEXT)";
        db.execSQL(CREATE_REGISTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
}
