package com.dirkdirk.wordbone;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;

public class InputWordActivity extends Activity {
	
	private TextView label;
	private EditText wordToAdd;
	private Button checkInDictionary;
	private Button submit;
	private int player;
	private SQLiteDatabase db;
	
	String[] columnsForChecking = {BoneListSQLiteHelper.COLUMN_PLAYER, BoneListSQLiteHelper.COLUMN_STRIPPED_WORD, BoneListSQLiteHelper.COLUMN_DATE};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.input_word_activity);

		BoneListSQLiteHelper b = new BoneListSQLiteHelper(this);
		db = b.getWritableDatabase();
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		int player = extras.getInt("player");
		String playerText;
		this.player = player;
		if (player==1) {
			playerText = "Dirk";
		}
		else {
			playerText = "Emma";
		}
		
		label = (TextView)findViewById(R.id.word_to_add_player_label);
		label.setText(playerText);
		wordToAdd = (EditText)findViewById(R.id.word_to_add);
		submit = (Button)findViewById(R.id.word_to_add_submit);
		submit.setOnClickListener(submitListener);
		checkInDictionary = (Button)findViewById(R.id.check_in_dictionary);
		checkInDictionary.setOnClickListener(dictionaryListener);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (db.isOpen()) db.close();
	}
	
	private void addWordToDatabase(String word, String strippedWord, int player) {
		ContentValues values = new ContentValues();
		String unixTime = String.valueOf(System.currentTimeMillis() / 1000L);
		values.put(BoneListSQLiteHelper.COLUMN_PLAYER, player);
		values.put(BoneListSQLiteHelper.COLUMN_DATE, unixTime);
		values.put(BoneListSQLiteHelper.COLUMN_STRIPPED_WORD, strippedWord);
		values.put(BoneListSQLiteHelper.COLUMN_WORD, word);
		db.insert(BoneListSQLiteHelper.TABLE_BONELIST, null, values);
	}
	
	private String stripWord(String word) {
		return word.replaceAll("\\s", "").toLowerCase();
	}
	
	private OnClickListener submitListener = new OnClickListener() {
		public void onClick(View v) {
			String strippedWord = stripWord(wordToAdd.getText().toString());
			
			if (strippedWord == "")
			{
				Toast t = Toast.makeText(v.getContext(), "Please enter a word.", Toast.LENGTH_SHORT);
				t.show();
			}
			else 
			{
				// Check if the word has been used
				Cursor cursor = db.query(BoneListSQLiteHelper.TABLE_BONELIST, columnsForChecking, BoneListSQLiteHelper.COLUMN_STRIPPED_WORD + " = '" + strippedWord + "'", null, null, null, null);
				int count = cursor.getCount();
				int claimedPlayer = -1;
				if (count > 0) {
					cursor.moveToFirst();
					claimedPlayer = cursor.getInt(1);
				}
				cursor.close();
				if (count > 0) {
					// This word already exists
					String nameOfPlayer;
					if (claimedPlayer == player) nameOfPlayer = "you";
					else {
						if (claimedPlayer == 1) nameOfPlayer = "Dirk";
						else nameOfPlayer = "Emma";
					}
					Toast t = Toast.makeText(v.getContext(), "That word is already claimed by " + nameOfPlayer + ".", Toast.LENGTH_LONG);
					t.show();
				}
				else {
					// This word doesn't exist
					String nameOfPlayer;
					if (claimedPlayer == 1) nameOfPlayer = "Dirk";
					else nameOfPlayer = "Emma";
					Toast t = Toast.makeText(v.getContext(), "One word bone to " + nameOfPlayer + "!", Toast.LENGTH_LONG);
					t.show();
					addWordToDatabase(wordToAdd.getText().toString(), strippedWord, player);
					finish();
				}
			}
		}
	};
	
	private OnClickListener dictionaryListener = new OnClickListener() {
		public void onClick(View v) {
			String url = "http://dictionary.reference.com/browse/" + wordToAdd.getText();
			Intent goToDictionary = new Intent();
			goToDictionary.setAction(Intent.ACTION_VIEW);
			goToDictionary.setData(Uri.parse(url));
			startActivity(goToDictionary);			
		}
	};
	
}
