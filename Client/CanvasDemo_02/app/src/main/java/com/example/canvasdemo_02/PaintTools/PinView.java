package com.example.canvasdemo_02.PaintTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.canvasdemo_02.Beans.OtherPoint;
import com.example.canvasdemo_02.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PinView extends SubsamplingScaleImageView {

    private Bitmap currentPin;
    private Bitmap otherUserPin;

    private PointF currentPoint;

    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();

    private List<PointF> otherUserPoints;

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public PinView(Context context) {
        super(context);
        initialise();
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        setMaximumDpi((int) density);

        currentPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.location_marker);
        float w = (density / 2000f) * currentPin.getWidth();
        float h = (density / 2000f) * currentPin.getHeight();
        currentPin = Bitmap.createScaledBitmap(currentPin, (int) w, (int) h, true);

        otherUserPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.location_marker_red);
        float w_ = (density / 2000f) * otherUserPin.getWidth();
        float h_ = (density / 2000f) * otherUserPin.getHeight();
        otherUserPin = Bitmap.createScaledBitmap(otherUserPin, (int) w_, (int) h_, true);
    }

    public void setCurrentTPosition(PointF p) {
        this.currentPoint = p;
        invalidate();
    }

//    public void setFingerprintPoints(List<PointF> result) {
//        if (otherUserPoints == null)
//            otherUserPoints = new LinkedList<>();
//        else
//            otherUserPoints.clear();
//
//        for (PointF f : result) {
//            otherUserPoints.add(f);
//        }
//
//        invalidate();
//    }

    public void setFingerprintPoints(List<OtherPoint> result) {
        if (otherUserPoints == null)
            otherUserPoints = new LinkedList<>();
        else
            otherUserPoints.clear();
        for (OtherPoint f : result) {
            otherUserPoints.add(new PointF(f.getLocation_x(),f.getLocation_y()));
        }

        invalidate();
    }
    public void setFingerprintPoints2(List<String> result) {
        List<OtherPoint> temp_re = new ArrayList<>();
        for(int i=0;i<result.size();i++){
            OtherPoint one = new OtherPoint(result.get(i));
            temp_re.add(one);
        }
        if (otherUserPoints == null)
            otherUserPoints = new LinkedList<>();
        else
            otherUserPoints.clear();
        for (OtherPoint f : temp_re) {
            otherUserPoints.add(new PointF(f.getLocation_x(),f.getLocation_y()));
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }
        paint.setAntiAlias(true);

        if (currentPoint != null && currentPin != null) {
            sourceToViewCoord(currentPoint, vPin);
            System.out.println(vPin.x+","+vPin.y);
            float vX = vPin.x - (currentPin.getWidth() / 2);
            float vY = vPin.y - (currentPin.getHeight() / 2);
            System.out.println("=====================================");

            canvas.drawBitmap(currentPin, vX, vY, paint);
        }

        if (otherUserPoints != null && otherUserPin != null)
            for (PointF pointF : otherUserPoints) {
                if (pointF!=null&&otherUserPin!=null){
                    sourceToViewCoord(pointF, vPin);
                    System.out.println("vPin:"+vPin.x+","+vPin.y);
                    float vX = vPin.x - (otherUserPin.getWidth() / 2);
                    float vY = vPin.y - (otherUserPin.getHeight() / 2);
                    System.out.println("=====================================");
                    canvas.drawBitmap(otherUserPin, vX, vY, paint);
                }
            }

    }

}
