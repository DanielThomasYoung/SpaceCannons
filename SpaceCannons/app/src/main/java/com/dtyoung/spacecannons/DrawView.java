package com.dtyoung.spacecannons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

public class DrawView extends View {
    Paint paint = new Paint();
    Paint paintCyan = new Paint();
    Paint paintRed = new Paint();
    Paint paintBlue = new Paint();
    Paint paintBlack = new Paint();
    boolean redTurn = true;
    public float redAngle = 0;
    public float redPower = 0;
    public float blueAngle = 0;
    public float bluePower = 0;
    public ArrayList<Integer[]> redProjectiles = new ArrayList<Integer[]>();
    public ArrayList<Integer[]> blueProjectiles = new ArrayList<Integer[]>();
    public ArrayList<Float[]> planets = new ArrayList<Float[]>();
    public float blueX = 0;
    public float blueY = 0;
    public float redX = 0;
    public float redY = 0;
    float tempX = 0;
    float tempY = 0;
    float sizeP = 0;
    public int numP = 1;

    private void init() {
        paint.setColor(Color.WHITE);
        paintCyan.setColor(Color.rgb(171,255,220));
        paintRed.setColor(Color.rgb(255,120,120));
        paintBlue.setColor(Color.rgb(120, 120, 255));
        paintBlack.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paintRed.setStrokeWidth(10);
        paintBlue.setStrokeWidth(10);
    }

    public void SetRandom() {
        planets.clear();
        redPower = 0;
        redAngle = 0;
        blueX = (float) (Math.random()*this.getWidth());
        blueY = (float) (Math.random()*this.getHeight());
        redX = (float) (Math.random()*this.getWidth());
        redY = (float) (Math.random()*this.getHeight());
        // Make sure red and blue are not too close
        while (Math.abs(redX - blueX) < 500 || Math.abs(redY - blueY) < 500) {
            redX = (float) (Math.random()*this.getWidth());
            redY = (float) (Math.random()*this.getHeight());
        }
        numP = (int) (Math.random()*5) + 1;
        for (int i=0; i < this.numP; i++) {
            tempX = (float) (Math.random()*this.getWidth());
            tempY = (float) (Math.random()*this.getHeight());
            sizeP = (float) (Math.random()*100 + 50);
            while (Math.abs(tempX - blueX) < sizeP || Math.abs(tempY - blueY) < sizeP || Math.abs(tempX - redX) < sizeP || Math.abs(tempY - redY) < sizeP) {
                tempX = (float) (Math.random()*this.getWidth());
                tempY = (float) (Math.random()*this.getHeight());
                sizeP = (float) (Math.random()*75 + 25);
            }
            this.planets.add(new Float[]{tempX, tempY, sizeP});
        }
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sizeP == 0) {
            SetRandom();
        }

        // Red and blue cannons
        if (redTurn) {
            canvas.drawCircle(redX, redY, 200, paint);
            canvas.drawCircle(redX, redY, 198, paintBlack);
        } else {
            canvas.drawCircle(blueX, blueY, 200, paint);
            canvas.drawCircle(blueX, blueY, 198, paintBlack);
        }
        canvas.drawLine(redX, redY, redX+2*this.redPower *(float) Math.cos(this.redAngle), redY+2*this.redPower *(float) Math.sin(this.redAngle), paintRed);
        canvas.drawLine(blueX, blueY, blueX+2*this.bluePower *(float) Math.cos(this.blueAngle), blueY+2*this.bluePower *(float) Math.sin(this.blueAngle), paintBlue);
        canvas.drawCircle(redX, redY, 20, paintRed);
        canvas.drawCircle(blueX, blueY, 20, paintBlue);

        // Planets
        for (Float[] p : this.planets) {
            canvas.drawCircle(p[0], p[1], p[2], paintCyan);
        }

        for (Integer[] p : redProjectiles) {
            canvas.drawCircle(p[0], p[1], 4, paintRed);
        }

        for (Integer[] p : blueProjectiles) {
            canvas.drawCircle(p[0], p[1], 4, paintBlue);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}

