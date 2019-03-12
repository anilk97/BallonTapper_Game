package com.example.anil.balloontapper;
//import tjulkune.balloonpop.R; // old eclipse import

import java.util.Random;
import java.util.Vector;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

//game engine
class GameView extends View implements OnTouchListener
{

    private Random randGen = new Random();
    private Balloon ball;
    private Canvas canvas;
    private short level = 1;
    private int score;
    private int popCounter = 1;
    private int health = 200;
    private Vector<Balloon> balls = new Vector<Balloon>();
    private double startTime = System.currentTimeMillis();
    private double curTime;

    // consider refactoring
    private Paint greenPaint = new Paint();
    private Paint redPaint = new Paint();
    private Paint defaultPaint = new Paint();
    //private Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);

    public Canvas getCanvas()
    {
        return this.canvas;
    }

    public GameView(Context context)
    {
        super(context);
        this.canvas = new Canvas();
        // for HUD text
        defaultPaint.setTextSize(30);
        defaultPaint.setColor(Color.BLACK);
        greenPaint.setTextSize(30);
        greenPaint.setColor(Color.rgb(0, 61, 0));
        redPaint.setTextSize(30);
        redPaint.setColor(Color.rgb(128, 0, 0));
        this.setOnTouchListener(this);

    }

    // get the screen size when layout is created
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        createBalloons(100);
    }

    // construct balloon objects and push them into Vector
    public void createBalloons(int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            // maximum value for sway
            int randomSwayLimit = randGen.nextInt(100) + 1;
            // sway direction
            boolean swaySwitch = randGen.nextBoolean();

            // set balloon co-ordinates randomly within the confines of screen
            if (randGen.nextInt(10) > 7) // 2/10 ratio on evil and regular balloons
                ball = new Balloon(this.getContext(), randGen.nextInt(this.getMeasuredWidth() - 40), this.getMeasuredHeight() - 50, true,
                        BitmapFactory.decodeResource(getResources(), R.drawable.evilballoon), randomSwayLimit, swaySwitch);
            else
                ball = new Balloon(this.getContext(), randGen.nextInt(this.getMeasuredWidth() - 40), this.getMeasuredHeight() - 50, false,
                        BitmapFactory.decodeResource(getResources(), R.drawable.balloon), randomSwayLimit, swaySwitch);
            balls.add(ball);
        }
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        // iterate through balloons and let balloon handle the event
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            for (int j = 0; j < balls.size(); j++)
            {
                if (balls.get(j).isPopped(event))
                {
                    popCounter++;
                    if (balls.get(j).isEvil())
                    {
                        health -= 5;
                        score -= 5;
                    } else score += 10;

                    if (popCounter % 20 == 0)
                    {
                        level += 1;
                    }
                    return true;
                }

            }
        }
        return false;
    }

    // render the scene
    @Override
    public void onDraw(Canvas canvas)
    {
        //canvas.drawColor(Color.rgb(102, 204, 255));
        canvas.drawColor(Color.rgb(204, 224, 255));

        canvas.drawText("Score: " + score, 0, 30, greenPaint);
        canvas.drawText("Health: " + health, 0, 62, redPaint);
        canvas.drawText("Level: " + level, 0, 92, defaultPaint);
        int randTime = randGen.nextInt(300) + 50;
        // System.out.println(randTime);

        int waveSize = 0;
        // iterate through balloon vector
        for (int k = 0; k < balls.size() && health >= 0; k++)
        {
            curTime = System.currentTimeMillis() - startTime;
            balls.get(k).setSpeed(level);

            // release more balloons at random interval, only release max 10 balls per wave
            if (curTime % randTime == 0 && waveSize <= 10)
            {
                balls.get(k).makeActive();
                waveSize++;

            }
            // draw and move active balloons
            if (balls.get(k).isActive())
            {
                balls.get(k).move();
                // if ball is inactive (has reached upper edge after moving): reduce health
                if (!balls.get(k).isActive() && !balls.get(k).isEvil()) health -= 5;
                balls.get(k).draw(canvas);
            }

        }
        this.invalidate(); //to cause a rendering loop

        // game over condition
        if (health < 0)
        {
            canvas.drawText("Game Over!", 50, 300, defaultPaint);
            //((GameActivity)getContext()).highScores(this.score);
            ((GameActivity) getContext()).startNameview(this.score);
        }
    }
}

