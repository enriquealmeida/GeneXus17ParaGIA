package com.genexus.android.core.usercontrols.matrixgrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.view.View;

import com.genexus.android.core.usercontrols.R;

public class SelectedMarkerView extends View {

//	private Bitmap mBitmap;
//	private int x1;
//	private int y1;
//	private int x2;
//	private int y2;
//	private int x3;
//	private int y3;
	private final BitmapShader mBitmapShader;
	private final Paint paint;
	private final Path path;

	public SelectedMarkerView(Context context, Bitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3) {
		super(context);
//		mBitmap = bmp;
//		this.x1 = x1;
//		this.y1 = y1;
//		this.x2 = x2;
//		this.y2 = y2;
//		this.x3 = x3;
//		this.y3 = y3;
		if (bmp == null)
		   bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bgrow);
		mBitmapShader = new BitmapShader(bmp,TileMode.CLAMP ,TileMode.CLAMP  );
		paint = new Paint();
		path = new Path();
		paint.setStyle(Style.FILL);
		path.reset();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x1,y1);
		path.lineTo(x2,y2);
		path.lineTo(x3,y3);
		path.close();
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setShader(mBitmapShader);
		canvas.drawPath(path, paint);
		paint.setShader(null);
	}

}
