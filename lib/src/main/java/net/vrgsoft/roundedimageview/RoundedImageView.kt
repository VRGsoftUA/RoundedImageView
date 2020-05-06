package net.vrgsoft.roundedimageview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import net.vrgsoft.toBitmap

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

    private fun invalidatePath(w: Int, h: Int) {
        path = getPath(w, h).also {
            if (drawable != null && isAttachedToWindow) {
                antiAliasPaint.shader = null
                maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply {
                    val c = Canvas(this)
                    c.drawPath(it, antiAliasPaint)
                    antiAliasPaint.shader =
                        BitmapShader(
                            drawable.toBitmap(w, h),
                            Shader.TileMode.CLAMP,
                            Shader.TileMode.CLAMP
                        )
                }
            } else {
                maskBitmap?.recycle()
                maskBitmap = null
                path = null
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
                if (imageMatrix == null && paddingTop == 0 && paddingLeft == 0) {
                    canvas.drawPath(it, antiAliasPaint)
                } else {
                    val saveCount = canvas.saveCount
                    canvas.save()
                    if (cropToPadding) {
                        canvas.clipRect(
                            scrollX + paddingLeft, scrollY + paddingTop,
                            scrollX + right - left - paddingRight,
                            scrollY + bottom - top - paddingBottom
                        )
                    }
                    canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
                    if (imageMatrix != null) {
                        canvas.concat(imageMatrix)
                    }
                    canvas.drawPath(it, antiAliasPaint)
                    canvas.restoreToCount(saveCount)
                }
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