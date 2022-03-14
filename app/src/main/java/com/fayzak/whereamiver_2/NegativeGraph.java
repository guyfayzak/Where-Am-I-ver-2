package com.fayzak.whereamiver_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class NegativeGraph extends View {

    // axis members
    private Rect xAxis;
    private Rect yAxis;
    private int axisWidth;
    private Paint axisPaint;
    private int lowestY;

    // graph members
    private List<GraphPoint> graphPoints;
    private Path currLine;
    private Paint graphPaint;
    private Paint textPaint;

    public NegativeGraph(Context context) {
        super(context);

        init();
    }

    public NegativeGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public NegativeGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public NegativeGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    public void init(){
        this.xAxis = new Rect();
        this.yAxis = new Rect();
        this.axisWidth = 6;
        // to make it look smother and not pixely
        this.axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.currLine = new Path();
        this.graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.lowestY = 1;
        this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void loadPoints(List<GraphPoint> graphPoints){
        this.graphPoints = graphPoints;
        for (GraphPoint point: graphPoints){
            if (lowestY < point.getY())
                lowestY = point.getY();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw the graph itself
        if (this.graphPoints.size() != 0) {
            int xUnit = getRight() / (this.graphPoints.size()+1);
            int yUnit = getBottom() / (lowestY+2);
            axisPaint.setColor(Color.BLACK);
            // draw the X axis
            xAxis.top = 0;
            xAxis.left = 0;
            xAxis.bottom = axisWidth;
            xAxis.right = getRight() ;
            canvas.drawRect(xAxis, axisPaint);

            // draw the Y axis
            yAxis.top = 0;
            yAxis.left = 0;
            yAxis.right = axisWidth;
            yAxis.bottom = getBottom();
            canvas.drawRect(yAxis, axisPaint);

            // grid
            textPaint.setStrokeWidth(20f);
            textPaint.setColor(Color.RED);
            textPaint.setTextSize(30);
            textPaint.setTextAlign(Paint.Align.CENTER);
            axisPaint.setStrokeWidth(1f);
            for (int i = 1; i < this.graphPoints.size(); i++) {
                canvas.drawLine(i * xUnit, 0, i * xUnit, yAxis.bottom, axisPaint);
                canvas.drawText("" + i, i*xUnit, 30, textPaint);
            }
            for (int i = 1; i <= lowestY; i++) {
                canvas.drawLine(0, i * yUnit, xAxis.right, i * yUnit, axisPaint);
                canvas.drawText("" + i, 20, yUnit*i,textPaint);
            }

            // graph
            GraphPoint prev = graphPoints.get(0);
            graphPaint.setColor(Color.parseColor("#519872"));
            graphPaint.setStrokeWidth(12f);
            for (GraphPoint curr : graphPoints){
                canvas.drawLine((prev.getX() * xUnit), (prev.getY()*yUnit),
                        (curr.getX()*xUnit), (curr.getY()*yUnit), graphPaint);
                prev = curr;
            }
        }
    }
}
