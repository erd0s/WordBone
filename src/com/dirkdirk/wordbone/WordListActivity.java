package com.dirkdirk.wordbone;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class WordListActivity extends Activity {

	private SQLiteDatabase db;
	private ListView player1ListView;
	private ListView player2ListView;
	
	public String[] fromList = {
			BoneListSQLiteHelper.COLUMN_ID,
			BoneListSQLiteHelper.COLUMN_WORD};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.word_list);
		
		player1ListView = (ListView)findViewById(R.id.player_1_word_list);
		player2ListView = (ListView)findViewById(R.id.player_2_word_list);

		BoneListSQLiteHelper b = new BoneListSQLiteHelper(this);
		db = b.getWritableDatabase();
		
		// Get the list of words for Dirk
		Cursor cursor = db.query(BoneListSQLiteHelper.TABLE_BONELIST, fromList, "player = 1", null, null, null, BoneListSQLiteHelper.COLUMN_WORD);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this, 
				R.layout.row_layout, 
				cursor, 
				new String[] {BoneListSQLiteHelper.COLUMN_WORD}, 
				new int[] {R.id.text1}, 
				0);
		
		player1ListView.setAdapter(adapter);
		
		Cursor cursor2 = db.query(BoneListSQLiteHelper.TABLE_BONELIST, fromList, "player = 2", null, null, null, BoneListSQLiteHelper.COLUMN_WORD);
		SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(
				this, 
				R.layout.row_layout, 
				cursor2, 
				new String[] {BoneListSQLiteHelper.COLUMN_WORD}, 
				new int[] {R.id.text1}, 
				0);
		player2ListView.setAdapter(adapter2);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}
}
