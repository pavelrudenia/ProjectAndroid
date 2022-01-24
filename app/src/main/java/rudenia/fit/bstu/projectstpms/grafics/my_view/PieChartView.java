package rudenia.fit.bstu.projectstpms.grafics.my_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.View;
import rudenia.fit.bstu.projectstpms.grafics.file_helper.FileHelper;


public class PieChartView extends View {

    private final Paint paint;
    private static int[] colors;
    private float[] dataPoints;
    private int width;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        colors = new int[FileHelper.countLineInFile(context, "colors.txt")];
    }



    @Override
    protected void onDraw(Canvas canvas) {
        if (this.dataPoints != null) {
            RectF rectf = new RectF(0, 0, getWidth(), getWidth());

            float[] scaledValues = scale();
            float sliceStartPoint = 0;
            for (int i = 0; i < scaledValues.length; i++) {
                paint.setColor(colors[i]);
                canvas.drawArc(rectf, sliceStartPoint, scaledValues[i], true, paint);
                sliceStartPoint += scaledValues[i];
            }
            paint.setColor(Color.WHITE);
            rectf = new RectF(width,
                    width,
                    getWidth() - width,
                    getWidth() - width);
            canvas.drawArc(rectf, 0, 360, true, paint);
        }
    }

    public void setDataPoints(float[] datapoints) {
        this.dataPoints = datapoints;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private float[] scale() {
        float[] scaledValues = new float[this.dataPoints.length];
        float total = getTotal();
        for (int i = 0; i < this.dataPoints.length; i++) {
            scaledValues[i] = (this.dataPoints[i] / total) * 360;
        }
        return scaledValues;
    }

    private float getTotal() {
        float total = 0;
        for (float val : this.dataPoints)
            total += val;
        return total;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int getColor(Context context, int index) {
        String file[] = FileHelper.readFile(context, "colors.txt");
        for(int i = 0; i < FileHelper.countLineInFile(context, "colors.txt"); i++) {
            colors[i] = Color.parseColor(file[i]);
        }
        return colors[index];
    }
}