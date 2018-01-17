package com.example.showinputcharacterlengthedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * created by Sunxiaxia on 2018/1/17 17:04
 * description:
 */
public class DeplayInputCharacterLenghtEditText extends EditText {
    public DeplayInputCharacterLenghtEditText(Context context) {
        super(context);
        initAttribute(context, null);
    }

    public DeplayInputCharacterLenghtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);
    }

    public DeplayInputCharacterLenghtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
    }

    private int mNumTextColor = Color.parseColor("#b1b1b1");
    private int mNumTextSize = 12;
    public final static int GRAVITY_LEFT = 1;//字数显示在左边，当数字位数增多时向右扩展
    public final static int GRAVITY_RIGHT = 2;//字数显示在右边，当数字位数增多时向左扩展
    private int grivity = GRAVITY_RIGHT;
    private Context context;
    private Paint textPaint;
    private int width;
    private int height;
    private int maxLength;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int bottom = 0;

    /**
     * 初始化画笔
     */
    private void initPaint() {
        if (grivity == GRAVITY_LEFT) {
            textPaint = createLeftPaint(mNumTextColor, mNumTextSize, Paint.Style.FILL, 2);
        } else {
            textPaint = createRightPaint(mNumTextColor, mNumTextSize, Paint.Style.FILL, 2);
        }

    }

    /**
     * @param context
     * @param attrs   初始化属性
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DeplayInputCharacterLenghtEditText);
        try {
            mNumTextSize = (int) ta.getDimension(R.styleable.DeplayInputCharacterLenghtEditText_numTextSize, DensityUtils.sp2px(context, 12));
            mNumTextColor = ta.getColor(R.styleable.DeplayInputCharacterLenghtEditText_numTextColor, Color.parseColor("#b1b1b1"));
            grivity = ta.getInt(R.styleable.DeplayInputCharacterLenghtEditText_numTextGravity, grivity);
            maxLength = getMaxLength();
        } finally {
            ta.recycle();
        }
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        bottom = DensityUtils.dip2px(context, 10);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + DensityUtils.dip2px(context, 10));
        initPaint();

    }


    /**
     * 判断是否为emoji表情
     *
     * @param codePoint 要校验的字符
     * @return 是否为表情
     */
    private boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD));
    }

    /**
     * 创建一个画笔
     *
     * @param paintColor 画笔颜色
     * @param textSize   文字大小
     * @param style      画笔样式
     * @param roundWidth 画笔宽度
     * @return
     */
    private Paint createLeftPaint(int paintColor, int textSize, Paint.Style style, int roundWidth) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(roundWidth);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    /**
     * 创建一个画笔
     *
     * @param paintColor 画笔颜色
     * @param textSize   文字大小
     * @param style      画笔样式
     * @param roundWidth 画笔宽度
     * @return
     */
    private Paint createRightPaint(int paintColor, int textSize, Paint.Style style, int roundWidth) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(roundWidth);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawNumberText(canvas);
    }

    /**
     * @param canvas 画输入框中 字数/最大字数
     */
    private void drawNumberText(Canvas canvas) {

        int length = getText().length();
        String str = length + "/" + maxLength;
        int x = 0;
        if (grivity == GRAVITY_LEFT) {
            x = 0 + paddingLeft;
        } else {
            x = width - paddingRight;
        }
        int y = height - bottom;
        canvas.drawText(str, x, y, textPaint);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        postInvalidate();
    }

    /**
     * @return length
     * 获取editText的MaxLength的属性值
     */
    public int getMaxLength() {
        int length = 0;
        try {
            InputFilter[] inputFilters = this.getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 获取文本的宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}
