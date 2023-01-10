package com.example.canvasdemo_02.PaintTools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.example.canvasdemo_02.MainActivity;


public class PaintBoard extends SurfaceView {
    Canvas canvas_temp = new Canvas();
    int color_temp=Color.argb(0xff, 0xd5, 0xfb, 0xfb);
    int point_color=Color.argb(0x00,0xff,0x00,0x00);
    int screen_widths;
    int screen_height;
    int paint_board_widths;
    int paint_board_height;
    Point point=new Point(100,300);
    Point text_position=new Point(105,305);
    boolean canvas_isempty=true;

    public PaintBoard(Context context, AttributeSet attrs){
        super(context, attrs);
        screen_height= MainActivity.screen_height;
        screen_widths=MainActivity.screen_widths;
        paint_board_height= (int) Math.round(screen_height*0.9221);
        paint_board_widths=screen_widths;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(canvas_isempty){
            canvas_temp=canvas;
            canvas_isempty=false;
        }
        Paint paint = new Paint();

        //paint background color
        canvas.drawColor(color_temp);

        //paint a circle
        paint.setColor(point_color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        canvas.drawCircle(point.x,point.y,10,paint);
        paint.setTextSize(40);
        canvas.drawText("("+point.x+","+point.y+")",text_position.x,text_position.y,paint);
        //paint string
//        paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(10);
//        canvas.drawLine(0,0,1100,1850,paint);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        int x1=(int)paint_board_widths/3-15,x2=(int)paint_board_widths/3+15,x3=(int)paint_board_widths/3*2-15,x4=(int)paint_board_widths/3*2+15;
        int y1=(int)paint_board_height/4-15,y2=(int)paint_board_height/4+15,y3=(int)paint_board_height/4*2-15,y4=(int)paint_board_height/4*2+15;
        int y5=(int)paint_board_height/4*3-15,y6=(int)paint_board_height/4*3+15;


        //canvas.drawLine(60,0,60,1823,paint);
        canvas.drawLine(x1,0,x1,y1,paint);
        canvas.drawLine(x2,0,x2,y3,paint);
        canvas.drawLine(x1,y2,x1,y5,paint);
       // canvas.drawLine(390,y2,390,1380,paint);
        canvas.drawLine(x1,y6,x1,paint_board_height,paint);
        canvas.drawLine(x2,y4,x2,paint_board_height,paint);
        canvas.drawLine(x3,0,x3,y3,paint);
        canvas.drawLine(x4,0,x4,y1,paint);
        //canvas.drawLine(730,480,730,1380,paint);
        canvas.drawLine(x4,y2,x4,y5,paint);
        canvas.drawLine(x3,y4,x3,paint_board_height,paint);
        canvas.drawLine(x4,y6,x4,paint_board_height,paint);

        canvas.drawLine(0,y1,x1,y1,paint);
        canvas.drawLine(0,y2,x1,y2,paint);
        canvas.drawLine(x4,y1,paint_board_widths,y1,paint);
        canvas.drawLine(x4,y2,paint_board_widths,y2,paint);
        canvas.drawLine(x2,y3,x3,y3,paint);
        canvas.drawLine(x2,y4,x3,y4,paint);
        canvas.drawLine(0,y5,x1,y5,paint);
        canvas.drawLine(0,y6,x1,y6,paint);
        canvas.drawLine(x4,y5,paint_board_widths,y5,paint);
        canvas.drawLine(x4,y6,paint_board_widths,y6,paint);


    }
    protected void Refresh(Canvas canvas){

        Paint paint = new Paint();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText("My name is zzg!",10,20,paint);
    }

    protected void setColor(int color){
        this.color_temp=color;
        invalidate();
    }
    protected void setPoint(Point point,int color){
        this.point=point;
        if(point.x>paint_board_widths/2){
            text_position.set(point.x-180,text_position.y);
        }else {
            text_position.set(point.x+10,text_position.y);
        }
        if(point.y>paint_board_height/2){
            text_position.set(text_position.x,point.y-30);
        }else {
            text_position.set(text_position.x,point.y+50);
        }
        this.point_color=color;
        invalidate();
    }

    protected Canvas getCanvas_temp(){
        if(!canvas_isempty){
            return canvas_temp;
        }
        return null;
    }
}