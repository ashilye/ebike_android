package net.hyntech.common.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import net.hyntech.common.R;

public class IconTextView extends LinearLayout {
    private ImageView vIcon;
    private TextView vText;

    private String mText;
    private int mTextColor;
    private boolean mIconShow = true;


    private Object mIconRes;
    private int mOrientaion;

    public enum TEXT_ALIGN {
        CENTER,
        LEFT,
        RIGHT
    }

    private TEXT_ALIGN mTextAlign;

    public IconTextView(Context context) {
        this(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int tempAlign = 0;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconTextView);
        try {
            mText = ta.getString(R.styleable.IconTextView_text);
            mTextColor = ta.getColor(R.styleable.IconTextView_textColor, ContextCompat.getColor(getContext(), android.R.color.black));
            tempAlign = ta.getInt(R.styleable.IconTextView_textAlignment, 1);

            mIconRes = ta.getResourceId(R.styleable.IconTextView_src, R.drawable.ic_must);
            mIconShow = ta.getBoolean(R.styleable.IconTextView_iconShow, true);
        } finally {
            switch (tempAlign) {
                case 0:
                    mTextAlign = TEXT_ALIGN.CENTER;
                    break;
                case 1:
                    mTextAlign = TEXT_ALIGN.LEFT;
                    break;
                case 2:
                    mTextAlign = TEXT_ALIGN.RIGHT;
                    break;
            }

            ta.recycle();
        }


        initView();
    }

    private void initView() {
        Log.d("TEST", "initView() / " + mOrientaion);
        removeAllViews();
        View parentView = mOrientaion == HORIZONTAL ?
                LayoutInflater.from(getContext()).inflate(R.layout.icon_textview_horizontal, this, true) :
                LayoutInflater.from(getContext()).inflate(R.layout.icon_textview_vertical, this, true);

        vIcon = parentView.findViewById(R.id.iv_icon);
        vText = parentView.findViewById(R.id.tv_text);

        setText(mText);
        setTextColor(mTextColor);
        setIconSrc(mIconRes);
        setTextAlignment(mTextAlign);
        if(mIconShow){
            vIcon.setVisibility(View.VISIBLE);
        }else {
            vIcon.setVisibility(View.INVISIBLE);
        }
    }

    public void setText(String text) {
        vText.setText(mText = text);
    }

    public void setTextColor(int color) {
        vText.setTextColor(mTextColor = color);
    }

    public void setTextColor(String colorCode) {
        vText.setTextColor(Color.parseColor(colorCode));
    }

    public void setTextAlignment(TEXT_ALIGN alignment) {
        if (alignment == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            vText.setTextAlignment(convertAlign(alignment));
        } else {
            int convert = Gravity.CENTER;
            switch (convertAlign(alignment)) {
                case TEXT_ALIGNMENT_CENTER:
                    convert = Gravity.CENTER;
                    break;
                case TEXT_ALIGNMENT_TEXT_START:
                    convert = Gravity.LEFT | Gravity.CENTER;
                    break;
                case TEXT_ALIGNMENT_TEXT_END:
                    convert = Gravity.RIGHT | Gravity.CENTER;
                    break;
            }
            vText.setGravity(convert);
        }
    }

    /**
     * Integer, Drawable, Bitmap, Uri
     *
     * @param resource
     */
    public void setIconSrc(Object resource) {
        try {
            if (resource instanceof Integer) {
                vIcon.setImageResource((int) resource);
            } else if (resource instanceof Drawable) {
                vIcon.setImageDrawable((Drawable) resource);
            } else if (resource instanceof Bitmap) {
                vIcon.setImageBitmap((Bitmap) resource);
            } else if (resource instanceof Uri) {
                vIcon.setImageURI((Uri) resource);
            }
            mIconRes = resource;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOrientation(int orientation) {
        Log.d("TEST", "setOrientation() / " + orientation);
        mOrientaion = orientation;
        initView();
    }

    private int convertAlign(TEXT_ALIGN align) {
        switch (align) {
            case CENTER:
                return TEXT_ALIGNMENT_CENTER;
            case LEFT:
                return TEXT_ALIGNMENT_TEXT_START;
            case RIGHT:
                return TEXT_ALIGNMENT_TEXT_END;
            default:
                return TEXT_ALIGNMENT_TEXT_START;
        }
    }
}
