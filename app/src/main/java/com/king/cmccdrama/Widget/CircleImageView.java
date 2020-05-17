package com.king.cmccdrama.Widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.annotation.Nullable;

/**
 * 圆形图片
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    //圆形图片的半径
    private int radius;
    private Paint paint = new Paint();
    private Matrix matrix = new Matrix();

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 宽高保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        radius = size / 2;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Drawable drawable = getDrawable();
        if (null != drawable) {
            // TODO,bitmap
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            //初始化BitmapShader，传入bitmap对象
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            //计算缩放比例
            float scale = (radius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());
            matrix.setScale(scale, scale);
            bitmapShader.setLocalMatrix(matrix);
            paint.setShader(bitmapShader);
            //画圆形，指定好坐标，半径，画笔
            canvas.drawCircle(radius, radius, radius, paint);
        } else {
            super.onDraw(canvas);
        }
    }
}
