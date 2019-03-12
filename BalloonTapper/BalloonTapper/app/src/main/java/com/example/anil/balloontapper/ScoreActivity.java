package com.example.anil.balloontapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

//import tjulkune.balloonpop.R; // old eclipse import
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.content.Context;

// high score table and manipulation
public class ScoreActivity extends ListActivity 
{
	private TreeMap<Integer, String> highscores = new TreeMap<Integer, String>(Collections.reverseOrder());
	private String SCORES = "scores_file";
	private String playerName;
	private int score;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        // DEBUG: hardcoded highscores for testing
        //highscores.put(10,"noob");
        //highscores.put(1000,"progamer");
        //highscores.put(200,"player1");
        //highscores.put(0,"placeholder");
         
        Bundle extras = getIntent().getExtras();
        // read scores from file, put new score in treemap and save updated scores
        if(extras != null) 
        {
        	readScores(); 
            playerName = extras.getString("PLAYERNAME");
            score = extras.getInt("SCORE");
            highscores.put(score,playerName);
            saveScores();
        }
        
        readScores();        
        /* 
         * Show the scores in listView. 
         * This solution simply converts treemap values into ArrayList containing strings.
         * More complicated way would have been creating a custom adapter.
         */
        ArrayList<String> scoreList = new ArrayList<String>();
        Iterator<Entry<Integer, String>> i = highscores.entrySet().iterator();
        int n = 1;
        int playerPos = 0;
        while (i.hasNext() && n <= 15)
        {
            @SuppressWarnings("rawtypes")
			Entry entry = (Entry) i.next();
            scoreList.add(n + ".  " + entry.getValue().toString() + " : " + entry.getKey().toString());
            if (entry.getKey().toString() == playerName)
            {
                playerPos = n;
            }
            n++;
        }
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scoreList));
        this.setSelection(playerPos); //highlight current player's position

        setContentView(R.layout.scores);                   
    }

	// save scores from treemap into file
	public void saveScores()
	{
		Iterator<Entry<Integer, String>> i = highscores.entrySet().iterator(); 		
	    int index = 0;
        while (i.hasNext() && index <= 10)
        {
	    	@SuppressWarnings("rawtypes")
			Entry entry = (Entry) i.next();
	    	try 
	    	{
	    		FileOutputStream scoreStream = openFileOutput(SCORES, Context.MODE_APPEND);
				scoreStream.write(entry.getKey().toString().getBytes());
				scoreStream.write(" ".getBytes());
				scoreStream.write(entry.getValue().toString().getBytes());
				scoreStream.write(" ".getBytes());
				scoreStream.close();
			} 
	    	catch (IOException ioe) 
			{
				ioe.printStackTrace();
			}
	    	
	    }
	}
	
	// read scores from file into treemap
	public void readScores()
	{
		try 
    	{
    		FileInputStream scoreStream = openFileInput(SCORES);
    		Scanner fileScan = new Scanner(scoreStream).useDelimiter(" ");
    		while (fileScan.hasNext())
    		{	
    			int score = Integer.parseInt(fileScan.next());
  	        	String name = fileScan.next();
                name = name.replace("\n", "").replace("\r", ""); //strip garbage
                highscores.put(score, name);
                // System.out.println(""+name+ ":"+score); //DEBUG
            }
            scoreStream.close();
		} 
    	catch (IOException ioe) 
		{
			ioe.printStackTrace();
		}
		
	}
	   
}