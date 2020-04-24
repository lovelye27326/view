package top.vchao.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * @ Create_time: 2020/4/24 on 11:57.
 * @ description: 自定义 角标 View
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class CornerMarkView extends View {

    public static final int MODE_TOP_LEFT = 0;
    public static final int MODE_TOP_RIGHT = 1;
    public static final int MODE_BOTTOM_LEFT = 2;
    public static final int MODE_BOTTOM_RIGHT = 3;
    public static final int MODE_TOP_LEFT_TRIANGLE = 4;
    public static final int MODE_TOP_RIGHT_TRIANGLE = 5;
    public static final int MODE_BOTTOM_LEFT_TRIANGLE = 6;
    public static final int MODE_BOTTOM_RIGHT_TRIANGLE = 7;

    public static final int ROTATE_ANGLE = 45;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private float mSlantedLength = 40;
    private float mTextSize = 16;
    private int mSlantedBackgroundColor = Color.TRANSPARENT;
    private int mTextColor = Color.WHITE;
    private String mSlantedText = "";
    private int mMode = MODE_TOP_LEFT;

    public CornerMarkView(Context context) {
        this(context, null);
    }

    public CornerMarkView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CornerMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CornerMarkView);

        mTextSize = array.getDimension(R.styleable.CornerMarkView_cornerTextSize, mTextSize);
        mTextColor = array.getColor(R.styleable.CornerMarkView_cornerTextColor, mTextColor);
        mSlantedLength = array.getDimension(R.styleable.CornerMarkView_cornerBgWidth, mSlantedLength);
        mSlantedBackgroundColor = array.getColor(R.styleable.CornerMarkView_cornerBgColor, mSlantedBackgroundColor);

        if (array.hasValue(R.styleable.CornerMarkView_cornerText)) {
            mSlantedText = array.getString(R.styleable.CornerMarkView_cornerText);
        }

        if (array.hasValue(R.styleable.CornerMarkView_cornerMode)) {
            mMode = array.getInt(R.styleable.CornerMarkView_cornerMode, 0);
        }
        array.recycle();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSlantedBackgroundColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawText(canvas);
    }


    private void drawBackground(Canvas canvas) {
        Path path = new Path();
        int w = getWidth();
        int h = getHeight();

        if (w != h) throw new IllegalStateException("CornerMarkView's width must equal to height");

        switch (mMode) {
            case MODE_TOP_LEFT:
                getModeLeftPath(path, w, h);
                break;
            case MODE_TOP_RIGHT:
                getModeRightPath(path, w, h);
                break;
            case MODE_BOTTOM_LEFT:
                getModeLeftBottomPath(path, w, h);
                break;
            case MODE_BOTTOM_RIGHT:
                getModeRightBottomPath(path, w, h);
                break;
            case MODE_TOP_LEFT_TRIANGLE:
                getModeLeftTrianglePath(path, w, h);
                break;
            case MODE_TOP_RIGHT_TRIANGLE:
                getModeRightTrianglePath(path, w, h);
                break;
            case MODE_BOTTOM_LEFT_TRIANGLE:
                getModeLeftBottomTrianglePath(path, w, h);
                break;
            case MODE_BOTTOM_RIGHT_TRIANGLE:
                getModeRightBottomTrianglePath(path, w, h);
                break;
        }
        path.close();
        canvas.drawPath(path, mPaint);
        canvas.save();
    }

    private void getModeLeftPath(Path path, int w, int h) {
        path.moveTo(w, 0);
        path.lineTo(0, h);
        path.lineTo(0, h - mSlantedLength);
        path.lineTo(w - mSlantedLength, 0);
    }

    private void getModeRightPath(Path path, int w, int h) {
        path.lineTo(w, h);
        path.lineTo(w, h - mSlantedLength);
        path.lineTo(mSlantedLength, 0);
    }

    private void getModeLeftBottomPath(Path path, int w, int h) {
        path.lineTo(w, h);
        path.lineTo(w - mSlantedLength, h);
        path.lineTo(0, mSlantedLength);
    }

    private void getModeRightBottomPath(Path path, int w, int h) {
        path.moveTo(0, h);
        path.lineTo(mSlantedLength, h);
        path.lineTo(w, mSlantedLength);
        path.lineTo(w, 0);
    }

    private void getModeLeftTrianglePath(Path path, int w, int h) {
        path.lineTo(0, h);
        path.lineTo(w, 0);
    }

    private void getModeRightTrianglePath(Path path, int w, int h) {
        path.lineTo(w, 0);
        path.lineTo(w, h);
    }

    private void getModeLeftBottomTrianglePath(Path path, int w, int h) {
        path.lineTo(w, h);
        path.lineTo(0, h);
    }

    private void getModeRightBottomTrianglePath(Path path, int w, int h) {
        path.moveTo(0, h);
        path.lineTo(w, h);
        path.lineTo(w, 0);
    }

    private void drawText(Canvas canvas) {
        int w = (int) (canvas.getWidth() - mSlantedLength / 2);
        int h = (int) (canvas.getHeight() - mSlantedLength / 2);
        float[] xy = calculateXY(canvas, w, h);
        float toX = xy[0];
        float toY = xy[1];
        float centerX = xy[2];
        float centerY = xy[3];
        float angle = xy[4];

        canvas.rotate(angle, centerX, centerY);

        canvas.drawText(mSlantedText, toX, toY, mTextPaint);
    }

    private float[] calculateXY(Canvas canvas, int w, int h) {
        float[] xy = new float[5];
        Rect rect;
        RectF rectF;
        int offset = (int) (mSlantedLength / 2);
        switch (mMode) {
            case MODE_TOP_LEFT_TRIANGLE:
            case MODE_TOP_LEFT:
                rect = new Rect(0, 0, w, h);
                rectF = new RectF(rect);
                rectF.right = mTextPaint.measureText(mSlantedText, 0, mSlantedText.length());
                rectF.bottom = mTextPaint.descent() - mTextPaint.ascent();
                rectF.left += (rect.width() - rectF.right) / 2.0f;
                rectF.top += (rect.height() - rectF.bottom) / 2.0f;
                xy[0] = rectF.left;
                xy[1] = rectF.top - mTextPaint.ascent();
                xy[2] = w >> 1;
                xy[3] = h >> 1;
                xy[4] = -ROTATE_ANGLE;
                break;
            case MODE_TOP_RIGHT_TRIANGLE:
            case MODE_TOP_RIGHT:
                rect = new Rect(offset, 0, w + offset, h);
                rectF = new RectF(rect);
                rectF.right = mTextPaint.measureText(mSlantedText, 0, mSlantedText.length());
                rectF.bottom = mTextPaint.descent() - mTextPaint.ascent();
                rectF.left += (rect.width() - rectF.right) / 2.0f;
                rectF.top += (rect.height() - rectF.bottom) / 2.0f;
                xy[0] = rectF.left;
                xy[1] = rectF.top - mTextPaint.ascent();
                xy[2] = (w >> 1) + offset;
                xy[3] = h >> 1;
                xy[4] = ROTATE_ANGLE;
                break;
            case MODE_BOTTOM_LEFT_TRIANGLE:
            case MODE_BOTTOM_LEFT:
                rect = new Rect(0, offset, w, h + offset);
                rectF = new RectF(rect);
                rectF.right = mTextPaint.measureText(mSlantedText, 0, mSlantedText.length());
                rectF.bottom = mTextPaint.descent() - mTextPaint.ascent();
                rectF.left += (rect.width() - rectF.right) / 2.0f;
                rectF.top += (rect.height() - rectF.bottom) / 2.0f;

                xy[0] = rectF.left;
                xy[1] = rectF.top - mTextPaint.ascent();
                xy[2] = w >> 1;
                xy[3] = h / 2f + offset;
                xy[4] = ROTATE_ANGLE;
                break;
            case MODE_BOTTOM_RIGHT_TRIANGLE:
            case MODE_BOTTOM_RIGHT:
                rect = new Rect(offset, offset, w + offset, h + offset);
                rectF = new RectF(rect);
                rectF.right = mTextPaint.measureText(mSlantedText, 0, mSlantedText.length());
                rectF.bottom = mTextPaint.descent() - mTextPaint.ascent();
                rectF.left += (rect.width() - rectF.right) / 2.0f;
                rectF.top += (rect.height() - rectF.bottom) / 2.0f;
                xy[0] = rectF.left;
                xy[1] = rectF.top - mTextPaint.ascent();
                xy[2] = (w >> 1) + offset;
                xy[3] = (h >> 1) + offset;
                xy[4] = -ROTATE_ANGLE;
                break;
        }
        return xy;
    }

    public CornerMarkView setText(String str) {
        mSlantedText = str;
        postInvalidate();
        return this;
    }

    public CornerMarkView setText(int res) {
        String str = getResources().getString(res);
        if (!TextUtils.isEmpty(str)) {
            setText(str);
        }
        return this;
    }

    public String getText() {
        return mSlantedText;
    }

    public CornerMarkView setSlantedBackgroundColor(int color) {
        mSlantedBackgroundColor = color;
        mPaint.setColor(mSlantedBackgroundColor);
        postInvalidate();
        return this;
    }

    public CornerMarkView setTextColor(int color) {
        mTextColor = color;
        mTextPaint.setColor(mTextColor);
        postInvalidate();
        return this;
    }

    /**
     * @param mode :
     *             CornerMarkView.MODE_LEFT : top left
     *             CornerMarkView.MODE_RIGHT :top right
     * @return this
     */
    public CornerMarkView setMode(int mode) {
        if (mMode > MODE_BOTTOM_RIGHT_TRIANGLE || mMode < 0)
            throw new IllegalArgumentException(mode + "is illegal argument ,please use right value");
        this.mMode = mode;
        postInvalidate();
        return this;
    }

    public int getMode() {
        return mMode;
    }

    public CornerMarkView setTextSize(int size) {
        this.mTextSize = size;
        mTextPaint.setTextSize(mTextSize);
        postInvalidate();
        return this;
    }

    /**
     * set slanted space length
     *
     * @param length :
     * @return this
     */
    public CornerMarkView setSlantedLength(int length) {
        mSlantedLength = length;
        postInvalidate();
        return this;
    }

}