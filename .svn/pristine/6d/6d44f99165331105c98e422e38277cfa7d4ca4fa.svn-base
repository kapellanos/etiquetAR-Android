/**
 * LinearLayoutOutlined.java
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 *
 * 14/08/13
 * <definicion de la clase>
 */
package es.uc3m.moc.etiquetar.utilities.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LinearLayoutOutlined extends LinearLayout {

    public Canvas canvas;
    public boolean drawRect = true;

    public LinearLayoutOutlined(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public LinearLayoutOutlined(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        if(drawRect) {
            Paint strokePaint = new Paint();
            strokePaint.setARGB(255, 0, 0, 0);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(4);
            Rect r = canvas.getClipBounds() ;
            Rect outline = new Rect( 1,1,r.right-1, r.bottom-1) ;
            canvas.drawRect(outline, strokePaint) ;
        }
    }

    public void restoreCanvasToOriginal() {
        invalidate();
        drawRect = false;
    }

    public void restoreCanvas() {
        invalidate();
        drawRect = true;
    }
}
