package com.tdtsh.tibubbleandroid;

import org.appcelerator.kroll.common.Log;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.widget.FrameLayout;

import android.util.DisplayMetrics;

/**
 * NativeBubbleView.
 *
 * @author Tadatoshi Hanazaki Copyright (c) 2014 by Tadatoshi Hanazaki. All
 *         Rights Reserved.
 */
public class NativeBubbleView extends FrameLayout {
	private static final String LCAT = "NativeBubbleView ";

	private int color = Color.GREEN;
	private float radius;
	private int bubbleBeak; // LEFT: 0, RIGHT: 1, NONE: 2
	private int bubbleBeakVertical; // LOWER: 0, UPPER: 1

	private Path path;
	private Paint paint;

	int padding = 10;

	public NativeBubbleView(Context context) {
		super(context);

		this.radius = 20.0F;
		this.bubbleBeak = 1;
		this.bubbleBeakVertical = 0;

		setWillNotDraw(false);

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		padding = metrics.widthPixels / 100;
		Log.d(LCAT, "padding = " + padding);
	}

	public void setBubbleColor(int color) {
		this.color = color;
	}

	public void setBubbleRadius(float radius) {
		this.radius = radius;
	}

	public void setBubbleBeak(int beak) {
		bubbleBeak = beak;
	}

	public void setBubbleBeakVertical(int beak) {
		bubbleBeakVertical = beak;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBubblePath(canvas);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);

		canvas.drawPath(path, paint);
	}

	private void drawBubblePath(Canvas canvas) {
		Rect bounds = new Rect();
		getDrawingRect(bounds);

		float top = bounds.top;
		float bottom = bounds.bottom;
		float right = (this.bubbleBeak == 1) ? bounds.right - padding : bounds.right;
		float left = (this.bubbleBeak == 0) ? bounds.left + padding : bounds.left;

		path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(left, top + radius);

		if (bubbleBeak == 1) {
			// right
			if (bubbleBeakVertical == 1) {
				//  upper right beak
				path.arcTo(new RectF(left, top, left + (radius * 2), top + (radius * 2)), 180, 90);

				path.arcTo(new RectF(right - (radius * 2), top, right, top + (radius * 2)), 270, 35);
				path.cubicTo(right - 10, top + 3, right - 10, top + 2, right + (padding), top);
				path.cubicTo(right - 2, top + (radius / 4), right, top + (radius / 4), right, top + radius);
				path.arcTo(new RectF(right - (radius * 2), top, right, top + (radius * 2)), 355, 5);

				path.arcTo(new RectF(right - (radius * 2), bottom - (radius * 2), right, bottom), 0, 90);
				path.arcTo(new RectF(left, bottom - (radius * 2), left + (radius * 2), bottom), 90, 90);
			} else {
				//  lower right beak
				path.arcTo(new RectF(left, top, left + (radius * 2), top + (radius * 2)), 180, 90);
				path.arcTo(new RectF(right - (radius * 2), top, right, top + (radius * 2)), 270, 90);

				path.arcTo(new RectF(right - (radius * 2), bottom - (radius * 2), right, bottom), 0, 5);
				path.cubicTo(right - 2, bottom - (radius / 4), right, bottom - (radius / 4), right + (padding), bottom);
				path.cubicTo(right - 10, bottom - 3, right - 10, bottom - 2, right - (radius / 2),
						bottom - (radius / 5));
				path.arcTo(new RectF(right - (radius * 2), bottom - (radius * 2), right, bottom), 55, 35);

				path.arcTo(new RectF(left, bottom - (radius * 2), left + (radius * 2), bottom), 90, 90);
			}
		} else if (bubbleBeak == 0) {
			// left
			if (bubbleBeakVertical == 1) {
				//  upper left beak
				path.arcTo(new RectF(left, top, left + (radius * 2), top + (radius * 2)), 175, 5);
				path.cubicTo(left + 2, top + (radius / 4), left, top + (radius / 4), left - (padding), top);
				path.cubicTo(left + 10, top + 3, left + 10, top + 2, left + (radius / 2), top + (radius / 5));
				path.arcTo(new RectF(left, top, left + (radius * 2), top + (radius * 2)), 235, 35);

				path.arcTo(new RectF(right - (radius * 2), top, right, top + (radius * 2)), 270, 90);
				path.arcTo(new RectF(right - (radius * 2), bottom - (radius * 2), right, bottom), 0, 90);
				path.arcTo(new RectF(left, bottom - (radius * 2), left + (radius * 2), bottom), 90, 90);
			} else {
				// lower left beak
				path.arcTo(new RectF(left, top, left + (radius * 2), top + (radius * 2)), 180, 90);
				path.arcTo(new RectF(right - (radius * 2), top, right, top + (radius * 2)), 270, 90);
				path.arcTo(new RectF(right - (radius * 2), bottom - (radius * 2), right, bottom), 0, 90);

				path.arcTo(new RectF(left, bottom - (radius * 2), left + (radius * 2), bottom), 90, 35);
				path.cubicTo(left + 10, bottom - 3, left + 10, bottom - 2, left - (padding), bottom);
				path.cubicTo(left + 2, bottom - (radius / 4), left, bottom - (radius / 4), left, bottom - radius);
				path.arcTo(new RectF(left, bottom - (radius * 2), left + (radius * 2), bottom), 175, 5);
			}
		} else if (bubbleBeak == 2) {
			// none
			path.addRoundRect(new RectF(left, top, right, bottom), radius, radius, Direction.CCW);
		}

		path.close();

	}

}
