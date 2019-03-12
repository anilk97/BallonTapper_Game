package com.example.anil.balloontapper;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

//main menu
public class MenuActivity extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void highScores(View invokingView)
	{
		Intent scoreIntent = new Intent(this,ScoreActivity.class);
		startActivity(scoreIntent);
	}
    
    public void startGame(View invokingView)
    {
    	Intent gameIntent = new Intent(this,GameActivity.class);
        startActivity(gameIntent);
    }
}
