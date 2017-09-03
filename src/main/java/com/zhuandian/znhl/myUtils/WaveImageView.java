package com.zhuandian.znhl.myUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class WaveImageView extends ImageView {
      
    private Paint mPaint;
    private Paint mPathPaint;  
    private Path mPath;
    private Bitmap mMaskBitmap;
    private static final Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private float mRectFWidth;  
    private int mWidth;  
    private int mHeight;  
      
    public WaveImageView(Context context) {
        this(context, null);  
    }  
  
    public WaveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);  
        init(context, attrs);  
    }  
      
    private void init(final Context context, final AttributeSet attrs) {  
        mPaint = new Paint();  
        mPath = new Path();  
        mPathPaint = new Paint();  
        mPathPaint.setAntiAlias(true);  
        mPathPaint.setColor(0xff000000);  
        mPathPaint.setStyle(Paint.Style.FILL);  
    }  
      
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        // 获取图片宽度和高度  
        mWidth = getMeasuredWidth();   
        mHeight = getMeasuredHeight();  
        mRectFWidth = mWidth / 20.0f; mRectFWidth = mRectFWidth - mRectFWidth%1;//去掉小数部分  
    }  
  
      
    @SuppressLint("DrawAllocation")
    @Override  
    protected void onDraw(Canvas canvas) {
        // 拿到Drawable  
        Drawable drawable = getDrawable();
        // 获取drawable的宽和高  
        int dWidth = drawable.getIntrinsicWidth();  
        int dHeight = drawable.getIntrinsicHeight();  
        if (drawable != null) {  
            // 创建bitmap  
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            float scale = 1.0f;  
            // 创建画布  
            Canvas drawCanvas = new Canvas(bitmap);  
            // 按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；  
            scale = getWidth() * 1.0F / Math.min(dWidth, dHeight);  
            // 根据缩放比例，设置bounds，相当于缩放图片了  
            drawable.setBounds(0, 0, (int) (scale * dWidth), (int) (scale * dHeight));  
            // 1. 先把原图(dst)绘制上  
            drawable.draw(drawCanvas);  
            // 2. 设置Xfermode  
            mPaint.setXfermode(mXfermode);  
            // 3. 把mask(src)绘制上  
            if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {  
                mMaskBitmap = getMaskBitmap();  
            }  
            drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);  
  
            // 将准备好的bitmap绘制出来  
            canvas.drawBitmap(bitmap, 0, 0, null);  
        }  
    }  
      
    /** 
     * 绘制形状 
     *  
     * @return 
     */  
    public Bitmap getMaskBitmap() {  
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);  
        Canvas canvas = new Canvas(bitmap);  
          
        RectF oval = new RectF(0, 0, mRectFWidth, mRectFWidth);
        mPath.addArc(oval, 0, 180);  
        mPath.lineTo(0, -(mHeight-mRectFWidth));  
        mPath.lineTo(mRectFWidth, -(mHeight-mRectFWidth));  
        mPath.lineTo(mRectFWidth, 0);  
          
        canvas.translate(0, mHeight-mRectFWidth);  
          
        canvas.drawPath(mPath, mPathPaint);  
          
        float sum = mRectFWidth;  
        while(sum < mWidth){  
            canvas.translate(mRectFWidth, 0);  
            canvas.drawPath(mPath, mPathPaint);  
            sum += mRectFWidth;  
        }  
          
        return bitmap;  
    }  
}  