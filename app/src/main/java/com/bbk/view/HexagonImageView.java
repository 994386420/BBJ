package com.bbk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HexagonImageView extends ImageView {
    private static final Xfermode MASK_XFERMODE;
    private Bitmap mask;
    private Paint paint;

    static {
        PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
        MASK_XFERMODE = new PorterDuffXfermode(localMode);
    }

    public HexagonImageView(Context paramContext) {
        super(paramContext);
    }

    public HexagonImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public HexagonImageView(Context paramContext, AttributeSet paramAttributeSet,
            int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }
    //画圆
    /*public Bitmap createMask() {
        int i = getWidth();
        int j = getHeight();
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        float f1 = getWidth();
        float f2 = getHeight();
        RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
        localCanvas.drawOval(localRectF, localPaint);
        return localBitmap;
    };*/
    
    // 画六边形
    public Bitmap createMask() {
        int width = getWidth();
        int height = getHeight();
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        float centerX = width/2;
        float centerY = height/2;
        float radius = width/2;
        double radian30 = 30*Math.PI/180;
        float a = (float) (radius*Math.sin(radian30));
        float b = (float) (radius*Math.cos(radian30));

        Path localPath = new Path();
        localPath.moveTo(centerX, 0);
        localPath.lineTo(centerX+b, centerY-a);
        localPath.lineTo(centerX+b, centerY+a);
        localPath.lineTo(centerX, height);
        localPath.lineTo(centerX-b, centerY+a);
        localPath.lineTo(centerX-b, centerY-a);
        localPath.close();
        localCanvas.drawPath(localPath, localPaint);
        return localBitmap;
    }
    @Override
    protected void onDraw(Canvas paramCanvas) {
        Drawable localDrawable = getDrawable();
        if (localDrawable == null)
            return;
        try {
            if (this.paint == null) {
                Paint localPaint1 = new Paint();
                this.paint = localPaint1;
                this.paint.setFilterBitmap(false);
                Paint localPaint2 = this.paint;
                Xfermode localXfermode1 = MASK_XFERMODE;
                @SuppressWarnings("unused")
                Xfermode localXfermode2 = localPaint2
                        .setXfermode(localXfermode1);
            }
            float f1 = getWidth();
            float f2 = getHeight();
            int i = paramCanvas.saveLayer(0.0F, 0.0F, f1, f2, null, 31);
            int j = getWidth();
            int k = getHeight();
            localDrawable.setBounds(0, 0, j, k);
            localDrawable.draw(paramCanvas);
            if ((this.mask == null) || (this.mask.isRecycled())) {
                Bitmap localBitmap1 = createMask();
                this.mask = localBitmap1;
            }
            Bitmap localBitmap2 = this.mask;
            Paint localPaint3 = this.paint;
            paramCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint3);
            paramCanvas.restoreToCount(i);
            return;
        } catch (Exception localException) {
        }
    }


}
