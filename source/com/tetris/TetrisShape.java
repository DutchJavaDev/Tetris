/**
 * 1 mei 2019
 */
package com.tetris;

import java.awt.Color;
import java.awt.Graphics2D;

import com.Engine.Display.Graphics.RenderBatch;
import com.Engine.Display.Graphics.DefaultShapes.BaseShape;

/**
 * @author Boris Mulder
 *
 */
public class TetrisShape extends BaseShape
{
	private int shapeIndex;
	private int shapeRotation = 0;
	private int rotationLeght = 0;
	private boolean[][] shapeMatrix;
	
	private float counter = 0f;
	private float updateTime = 12.5f;

	public TetrisShape(int x, int y)
	{
		super(x, y);
		CreateShape();
	}
	
	private void CreateShape()
	{
		shapeIndex = TetrisHelper.GetRandomShapeNumber();
		shapeMatrix = TetrisHelper.GetShape(shapeIndex);
		shapeRotation = 0;
		rotationLeght = TetrisHelper.GetRotationLenght(shapeIndex);
	}
	
	public void Update(float delta)
	{
		if(counter >= updateTime)
		{
			counter = 0;
			y+=10;
		}
		else
		{
			counter += (delta * 1);
		}
	}
	
	/**
	 * Updates the rotation of the shape
	 */
	public void UpdateRotation()
	{
		shapeRotation++;
		
		if(shapeRotation >= rotationLeght)
			shapeRotation = 0;
		
		shapeMatrix = TetrisHelper.GetShapeWithRotation(shapeIndex, shapeRotation);
	}

	/**
	 * Draws the shape
	 */
	@Override
	public void Draw(Graphics2D gfx, RenderBatch batch)
	{
		int xpos = x;
		int ypos = y;

		batch.Begin();

		for (int r = 0; r < shapeMatrix.length; r++)
		{
			for (int c = 0; c < shapeMatrix[r].length; c++)
			{

				if (shapeMatrix[r][c])
				{
					batch.setColor(Color.green);
					batch.DrawString("?", xpos, ypos, "tetris_shape_font");
				} else
				{
					//batch.setColor(Color.blue);
					//batch.DrawString("*", xpos, ypos, "tetris_shape_font");
				}

				xpos += 16;
			}
			xpos = x;
			ypos += 16;
		}
		batch.End();
	}
}
