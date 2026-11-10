package com.luax.esp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

data class P2(val x: Float, val y: Float)
data class DemoTarget(
    val centerX: Float, val top: Float, val width: Float, val height: Float,
    val meters: Int, val joints: List<P2>, val bones: List<Pair<Int, Int>>
)

class EspOverlayView(context: Context) : View(context) {
    private val prefs = context.getSharedPreferences("luax", Context.MODE_PRIVATE)
    private val cornerPaint = Paint(3).apply {
        color = Color.WHITE; style = Paint.Style.STROKE; strokeCap = Paint.Cap.SQUARE
    }
    private val skeletonPaint = Paint(3).apply {
        color = Color.WHITE; style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND
    }
    private val fillPaint = Paint(3).apply { color = Color.argb(28, 255, 255, 255); style = Paint.Style.FILL }
    private val textPaint = Paint(3).apply {
        color = Color.WHITE; textSize = 30f; setShadowLayer(5f, 1f, 1f, Color.BLACK)
    }

    // Coordenadas de demonstração normalizadas. Substituir por dados autorizados do próprio jogo.
    private val target = DemoTarget(.50f, .25f, .20f, .43f, 20,
        listOf(P2(.50f,.29f), P2(.50f,.37f), P2(.43f,.39f), P2(.57f,.39f),
            P2(.46f,.51f), P2(.54f,.51f), P2(.46f,.65f), P2(.54f,.65f)),
        listOf(0 to 1, 1 to 2, 1 to 3, 2 to 4, 3 to 5, 4 to 6, 5 to 7))

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        val left = (target.centerX - target.width / 2) * width
        val right = (target.centerX + target.width / 2) * width
        val top = target.top * height
        val bottom = (target.top + target.height) * height
        val corner = minOf(right-left, bottom-top) * .22f
        val thickness = prefs.getInt("boxWidth", 2).toFloat()
        cornerPaint.strokeWidth = thickness
        skeletonPaint.strokeWidth = prefs.getInt("skWidth", 2).toFloat()

        if (prefs.getBoolean("filled", false)) c.drawRect(left, top, right, bottom, fillPaint)
        if (prefs.getBoolean("box", true)) {
            // Box segmentada nos quatro cantos, sem topo/base completos e sem preenchimento padrão.
            c.drawLine(left, top, left+corner, top, cornerPaint)
            c.drawLine(left, top, left, top+corner, cornerPaint)
            c.drawLine(right-corner, top, right, top, cornerPaint)
            c.drawLine(right, top, right, top+corner, cornerPaint)
            c.drawLine(left, bottom-corner, left, bottom, cornerPaint)
            c.drawLine(left, bottom, left+corner, bottom, cornerPaint)
            c.drawLine(right-corner, bottom, right, bottom, cornerPaint)
            c.drawLine(right, bottom-corner, right, bottom, cornerPaint)
        }
        if (prefs.getBoolean("skeleton", true)) {
            target.bones.forEach { (a,b) ->
                if (a in target.joints.indices && b in target.joints.indices) {
                    val p = target.joints[a]; val q = target.joints[b]
                    c.drawLine(p.x*width, p.y*height, q.x*width, q.y*height, skeletonPaint)
                }
            }
        }
        if (prefs.getBoolean("distance", true) && target.meters <= prefs.getInt("maxDistance", 300)) {
            c.drawText("${target.meters} m", left, bottom + 34f, textPaint)
        }
        postInvalidateDelayed(50)
    }
}
