package com.github.flickrsample.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.github.flickrsample.R

/**
 * A custom Image View to show rounded corner images and photos,
 * Also relayout's based on height ratio
 *
 * Created by gk
 */
class RoundedCornerImageView : ImageView {

    private var mRadius = 8.0f
    private var mPath: Path? = null
    private var mRect: RectF? = null
    private var mInitRect: Boolean = false

    private var mHeightRatio: Float = 0.0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context,attrs)
    }

    private fun init(){
        mPath = Path()
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        mPath = Path()

        //get the attributes specified in attrs.xml using the name we included
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs,
                    R.styleable.RoundedCornerImageView, 0, 0)
            if (typedArray != null && typedArray.length() > 0) {
                try {
                    //get the text and colors specified using the names in attrs.xml
                    this.mRadius = typedArray.getDimension(R.styleable.RoundedCornerImageView_corner, 8.0f)
                } finally {
                    typedArray.recycle()
                }
            }
        }
    }

    //Here we will set the aspect ratio
    fun setHeightRatio(ratio: Float) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio
            requestLayout()
        }
    }

    fun getHeightRatio(): Float {
        return mHeightRatio
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mHeightRatio > 0.0) {
            // set the image views size
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            val height = (width * mHeightRatio).toInt()
            mInitRect = false
            invalidate()
            setMeasuredDimension(width, height)
        } else {
            invalidate()
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if(!mInitRect) {
            mRect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
            mInitRect = true
        }

        mPath!!.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CW)
        canvas.clipPath(mPath!!)
        super.onDraw(canvas)
    }

}