package com.dirkdirk.wordbone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	Button player1Button;
	Button player2Button;
	
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

		player1Button = (Button) findViewById(R.id.playerButton1);
		player2Button = (Button) findViewById(R.id.playerButton2);
		
		player1Button.setOnClickListener(addWordListener);
		player2Button.setOnClickListener(addWordListener);
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
		}
        return false;
	}
		
	private void clearDatabase() {
		
	}
	
}
