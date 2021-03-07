package ie.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDesigner extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "donations.db";
	private static final int 	DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE_TABLE_DONATION = "create table donations "
			+ "(id integer primary key autoincrement,"
			+ "amount integer not null,"
			+ "method text not null);";
		
	public DBDesigner(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TABLE_DONATION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBDesigner.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS donations");
		onCreate(db);
	}
}