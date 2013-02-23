package com.dirkdirk.wordbone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is a helper class for the bonelist table, it creates the database if
 * necessary and upgrades when necessary
 */
public class BoneListSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_BONELIST = "bonelist";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WORD = "word";
	public static final String COLUMN_STRIPPED_WORD = "stripped_word";
	public static final String COLUMN_PLAYER = "player";
	public static final String COLUMN_DATE = "date";
	
	private static final String DATABASE_NAME = "wordbone.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table " + TABLE_BONELIST + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_WORD + " text," +
			COLUMN_STRIPPED_WORD + " text," + 
			COLUMN_PLAYER + " integer," + 
			COLUMN_DATE + " text);";

	public BoneListSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_BONELIST);
		onCreate(database);
	}

}
