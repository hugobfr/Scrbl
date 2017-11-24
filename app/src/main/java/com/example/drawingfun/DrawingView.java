package com.example.drawingfun;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class DrawingView extends View{

	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF000000, paintAlpha = 255;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	//brush sizes
	private float brushSize, lastBrushSize;
	//erase flag
	private boolean erase=false;

	//text placement flag
	private boolean textPlacement= false;
	private EditTextBackEvent editText;

	public DrawingView(Context context, AttributeSet attrs){
		super(context, attrs);
		setupDrawing();
	}

	//setup drawing
	private void setupDrawing(){

		//prepare for drawing and setup paint stroke properties
		brushSize = getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		drawPaint.setTextSize(75);
		canvasPaint = new Paint(Paint.DITHER_FLAG);

		setOnTouchListener(new OnTouchListener()
		{
			private double doublePressInterval = 200,  lastActionDown;
			private boolean hasMoved = false;

			private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onDoubleTap(MotionEvent e) {
					Log.d("TEST", "onDoubleTap");
					MainActivity a = (MainActivity) getContext();
					a.closeOpenMenus();
					return true;
				}
   // implement here other callback methods like onFling, onScroll as necessary
			});

			@Override
			public boolean onTouch(View view, MotionEvent event)
			{
				gestureDetector.onTouchEvent(event);


				float touchX = event.getX();
				float touchY = event.getY();

				if (!textPlacement)
				{
					//respond to down, move and up events
					switch (event.getAction())
					{

						case MotionEvent.ACTION_DOWN:
							lastActionDown = System.currentTimeMillis();
							drawPath.moveTo(touchX, touchY);
							break;
						case MotionEvent.ACTION_MOVE:
							hasMoved = true;
							drawPath.lineTo(touchX, touchY);
							break;
						case MotionEvent.ACTION_UP:
							if (System.currentTimeMillis() - lastActionDown > doublePressInterval || hasMoved)
							{
								drawPath.lineTo(touchX, touchY);
								drawCanvas.drawPath(drawPath, drawPaint);
							}
							drawPath.reset();
							hasMoved = false;
							break;
						default:
							return false;
					}
				}
				else
				{

					editText.setX(touchX);
					editText.setY(touchY);

					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						setTextPlacement(false);
						editText.setBackgroundColor(0x00000000);
					}

				}
				//redraw
				invalidate();
				return true;
			}
		});
	}

	//size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	//draw the view - will be called after touch event
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}

	//update color
	public void setColor(String newColor){
		invalidate();
		//check whether color value or pattern name
		if(newColor.startsWith("#")){
			paintColor = Color.parseColor(newColor);
			drawPaint.setColor(paintColor);
			drawPaint.setShader(null);
		}
	}

	//set brush size
	public void setBrushSize(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	//get and set last brush size
	public void setLastBrushSize(float lastSize){
		lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
		return lastBrushSize;
	}

	//set erase true or false
	public void setErase(boolean isErase){
		erase=isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}

	public void setTextPlacement(boolean isTextPlacement)
	{
		textPlacement = isTextPlacement;
	}

	public void setEditText(EditTextBackEvent e) { editText = e; }

	//start new drawing
	public void startNew(){
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}

	//return current alpha
	public int getPaintAlpha(){
		return Math.round((float)paintAlpha/255*100);
	}

	public int getPaintColor()
	{
		return paintColor;
	}


}
