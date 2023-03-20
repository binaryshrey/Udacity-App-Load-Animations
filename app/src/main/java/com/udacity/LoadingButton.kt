package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var value = 0.0f
    private var width = 0.0f
    private var sweepAngle = 0.0f
    private var widthSize = 0
    private var heightSize = 0
    private lateinit var buttonText: String
    private var valueAnimator = ValueAnimator()

    //setting button color
    private var paintCustomButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.app_button_color)
    }

    private var paintLoadingButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimary)
    }

    //setting text properties
    private val paintButtonText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = resources.getDimension(R.dimen.default_text_size)
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
    }

    // Paint object for coloring and styling
    private var paintButtonCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorAccent)
    }

    //listen to button state changes
    var customButtonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Default) { _, _, new ->

        // set the button text based on the state of the button
        buttonText = context.getString(customButtonState.buttonText)

        when (new) {
            ButtonState.Default->{
                paintButtonCircle.color = context.getColor(R.color.colorAccent)
            }

            ButtonState.Completed-> {
                valueAnimator.cancel()
                paintButtonCircle.color = context.getColor(R.color.colorPrimary)
                value = 0f
                invalidate()
            }

            ButtonState.Loading -> {
                paintButtonCircle.color = context.getColor(R.color.colorAccent)
                valueAnimator = ValueAnimator.ofFloat(0.0f, measuredWidth.toFloat()).setDuration(2000).apply {
                        addUpdateListener { valueAnimator ->
                            value = valueAnimator.animatedValue as Float
                            sweepAngle = value / 8
                            width = value * 4
                            invalidate()
                        }
                    }
                valueAnimator.start()
            }
        }
    }

    init {
        customButtonState = ButtonState.Default
    }

    //over-riding onDraw
    override fun onDraw(customCanvas: Canvas?) {
        super.onDraw(customCanvas)

        //Draw rectangle
        customCanvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paintCustomButton)
        customCanvas?.drawRect(0f, 0f, width, heightSize.toFloat(), paintLoadingButton)

        //Draw text
        val textHeight: Float = paintButtonText.descent() - paintButtonText.ascent()
        val textOffset: Float = textHeight / 2 - paintButtonText.descent()
        customCanvas?.drawText(buttonText, widthSize.toFloat() / 2, heightSize.toFloat() / 2 + textOffset, paintButtonText)

        customCanvas?.drawArc(widthSize - 145f, heightSize / 2 - 35f, widthSize - 75f, heightSize / 2 + 35f, 0F, width, true, paintButtonCircle)
    }

    //over-riding onMeasure
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}