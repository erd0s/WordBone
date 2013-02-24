package com.dirkdirk.wordbone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;

public class MainActivity extends Activity {

	Button player1Button;
	Button player2Button;

	private SQLiteDatabase db;
	
	final Context context = this;
	
	private OnClickListener addWordListener = new OnClickListener() {
		public void onClick(View v) {
			// Figure out which button this was
			int player;
			if (player1Button.getId() == ((Button)v).getId()) {
				player = 1;
			}
			else {
				player = 2;
			}
			
			Intent intent = new Intent(v.getContext(), InputWordActivity.class);
			intent.putExtra("player", player);
			startActivity(intent);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		BoneListSQLiteHelper b = new BoneListSQLiteHelper(this);
		db = b.getWritableDatabase();

		player1Button = (Button) findViewById(R.id.playerButton1);
		player2Button = (Button) findViewById(R.id.playerButton2);
		
		player1Button.setOnClickListener(addWordListener);
		player2Button.setOnClickListener(addWordListener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		updateButtonCounts();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.clear_database:
            clearDatabase();
            return true;
        case R.id.word_list:
        	showWordList();
        	return true;
		}
        return false;
	}
		
	private void clearDatabase() {
		// Double check before deleting
		AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
		confirmBuilder.setTitle("Delete words?");
		confirmBuilder.setCancelable(false);
		confirmBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Actually clear the database
				db.delete(BoneListSQLiteHelper.TABLE_BONELIST, null, null);
				Toast t = Toast.makeText(context, "Bone List Cleared!", Toast.LENGTH_SHORT);
				t.show();
				// Refresh buttons
				updateButtonCounts();
			}
		});
		confirmBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog	alertDialog = confirmBuilder.create();
		alertDialog.show();
	}
	
	private void showWordList() {
		Intent intent = new Intent(this, WordListActivity.class);
		startActivity(intent);
	}
	
	private void updateButtonCounts() {
		Cursor cursorPlayer1 = db.query(BoneListSQLiteHelper.TABLE_BONELIST, InputWordActivity.columnsForChecking, BoneListSQLiteHelper.COLUMN_PLAYER + " = 1", null, null, null, null);
		Cursor cursorPlayer2 = db.query(BoneListSQLiteHelper.TABLE_BONELIST, InputWordActivity.columnsForChecking, BoneListSQLiteHelper.COLUMN_PLAYER + " = 2", null, null, null, null);
		
		int player1count = cursorPlayer1.getCount();
		int player2count = cursorPlayer2.getCount();

		player1Button.setText("Dirk (" + player1count + ")");
		player2Button.setText("Emma (" + player2count + ")");
		
		cursorPlayer1.close();
		cursorPlayer2.close();
	}
}
