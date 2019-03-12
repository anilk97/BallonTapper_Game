package com.example.anil.balloontapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// gameplay activity
public class GameActivity extends Activity implements View.OnClickListener
{
	private EditText nameEdit;
	private Button okButton;
	private String playerName;
    private TextView scoreText;
    private int score;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(new GameView(this));   
	    
	}

	public void onClick(View v) 
	{  	 	
		playerName = nameEdit.getText().toString();	  	  
	  	launchScores(score);
    }
	
	//prompt user for name
	public void startNameview(int score)
	{
        this.score = score;
        setContentView(R.layout.name);
        scoreText = (TextView) findViewById(R.id.scoreText);
        nameEdit = (EditText) findViewById(R.id.nameinp);
        okButton = (Button) findViewById(R.id.OK);
        okButton.setOnClickListener(this);
        nameEdit.requestFocus();
        scoreText.setText("Your score: " + score);
        // setTitle("Enter name");
    }
	    
	// launch score intent
	public void launchScores(int score)
	{	
	     Intent scoreIntent = new Intent(this,ScoreActivity.class);
	   	 scoreIntent.putExtra("PLAYERNAME", playerName);
	     scoreIntent.putExtra("SCORE", score);
	     startActivity(scoreIntent);
	     GameActivity.this.finish();
	} 
}
	