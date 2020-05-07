package net.vrgsoft.roundedimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    private val topLeft: Float
    private val topRight: Float
    private val bottomRight: Float
    private val bottomLeft: Float

    private val corners: Float

    private var path: Path? = null
    private var maskBitmap: Bitmap? = null
    private val shaderCanvas = Canvas()

    private val antiAliasPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        isFilterBitmap = true
    }

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.RoundedImageView, 0, defStyle
        )
        topLeft = a.getDimension(R.styleable.RoundedImageView_rivTopLeft, 0f)
        topRight = a.getDimension(R.styleable.RoundedImageView_rivTopRight, 0f)
        bottomLeft = a.getDimension(R.styleable.RoundedImageView_rivBottomLeft, 0f)
        bottomRight = a.getDimension(R.styleable.RoundedImageView_rivBottomRight, 0f)
        corners = a.getDimension(R.styleable.RoundedImageView_rivCorners, 0f)
        a.recycle()
        init()
    }

    private fun init() {
        path = Path()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        invalidatePath(measuredWidth, measuredHeight)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        invalidatePath(measuredWidth, measuredHeight)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        invalidatePath(measuredWidth, measuredHeight)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        invalidatePath(measuredWidth, measuredHeight)
    }

    private fun invalidatePath(w: Int, h: Int) {
        path = getPath(w, h).also {
            if (drawable != null && w > 0 && h > 0) {
                maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                shaderCanvas.setBitmap(maskBitmap)
                antiAliasPaint.shader = BitmapShader(
                    maskBitmap!!,
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP
                )
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        invalidatePath(w, h)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)
        path?.let {
            if (drawable != null) {
                super.onDraw(shaderCanvas)
                canvas.drawPath(it, antiAliasPaint)
            }
        }
    }

    private fun getPath(w: Int, h: Int): Path {

        val path = Path()
        val radii = FloatArray(8)

        radii[0] = if (topLeft == 0f) corners else topLeft
        radii[1] = if (topLeft == 0f) corners else topLeft

        radii[2] = if (topRight == 0f) corners else topRight
        radii[3] = if (topRight == 0f) corners else topRight

        radii[4] = if (bottomRight == 0f) corners else bottomRight
        radii[5] = if (bottomRight == 0f) corners else bottomRight

        radii[6] = if (bottomLeft == 0f) corners else bottomLeft
        radii[7] = if (bottomLeft == 0f) corners else bottomLeft

        path.addRoundRect(
            RectF(0f, 0f, w.toFloat(), h.toFloat()),
            radii, Path.Direction.CW
        )
        return path
    }
}