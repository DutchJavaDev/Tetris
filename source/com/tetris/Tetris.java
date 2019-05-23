/**
 * 5 mrt. 2019
 */
package com.tetris;

import java.awt.Color;
import java.awt.Font;

import com.Engine.Application.GameApplication;
import com.Engine.Application.Input.KeyInput;
import com.Engine.Display.GameClient;
import com.Engine.Display.Graphics.RenderBatch;
import com.Engine.Display.Graphics.ResourceManager;

/**
 * @author Boris Mulder
 *
 */
public class Tetris extends GameApplication
{
	public static final int GAME_WIDTH = 420;
	public static final int GAME_HEIGHT = 640;

	private final boolean[][] GAME_MATRIX;

	private int shapeIndex;
	private int shapePosition = 0;
	private int shapeRotation = 0;
	private int shapeRotationLeght = 0;
	private boolean[][] shapeMatrix;

	private int shapeX = 4;
	private int shapeY = 0;
	private float counter = 0f;
	private final float updateTime = 80f;

	Tetris()
	{
		super("Tetris with a twist");
		ResizeAble = false;
		GAME_MATRIX = new boolean[18][12];
		CreateShape();
	}

	private void CreateShape()
	{
		shapeIndex = TetrisHelper.GetRandomShapeNumber();
		shapeMatrix = TetrisHelper.GetShape(shapeIndex);
		shapeRotation = 0;
		shapeRotationLeght = TetrisHelper.GetRotationLenght(shapeIndex);

		shapeX = 4;
		shapeY = 0;

		SetShape();
	}

	private void Clear()
	{
		for (int r = shapeY; r < shapeY + shapeMatrix.length; r++)
		{
			for (int cell = shapeX; cell < shapeX + shapeMatrix[0].length; cell++)
			{
				GAME_MATRIX[r][cell] = false;
			}
		}
	}

	private void CheckBottomBounds()
	{
		if (shapeY + 4 > GAME_MATRIX.length)
			CreateShape();
	}

	private void SetShape()
	{
		CheckBottomBounds();
		Clear();

		System.arraycopy(shapeMatrix[0], 0, GAME_MATRIX[shapeY], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(shapeMatrix[1], 0, GAME_MATRIX[shapeY + 1], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(shapeMatrix[2], 0, GAME_MATRIX[shapeY + 2], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(shapeMatrix[3], 0, GAME_MATRIX[shapeY + 3], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
	}

	@Override
	public void LoadResources(ResourceManager manager)
	{
		TetrisHelper.Init();
		manager.LoadFont("tetris_shape_font", "Comic Sans MS", Font.BOLD, 64);
	}

	@Override
	public void mousePressed(ApplicationEvent button, int x, int y)
	{
	}

	@Override
	public void mouseReleased(ApplicationEvent button, int x, int y)
	{
	}

	@Override
	protected void UpdateGame()
	{
		// Drop the shape after an amount of time
		if (counter >= updateTime)
		{
			// clear matrix if shape is not at the bottom
			if (!(shapeY + 4 > GAME_MATRIX.length))
				Clear();
			
			counter = 0;
			shapeY += 1;

			SetShape();
		} else
		{
			counter += (Delta * 1);
		}
		
		CheckBottomBounds();
	}

	@Override
	public void keyboardEvent(ApplicationEvent event, int key_code)
	{
		final boolean released = event == ApplicationEvent.KEYRELEASED;

		// Move to the left
		if (key_code == KeyInput.LEFT && released)
		{
			Clear();
			
			if(shapeX == 0)
			{
				// check if the position of the shape can be moved to the left
				shapeMatrix = TryShiftPositionTo(-1, shapeMatrix);
				SetShape();
				shapePosition = -1;
				return;
			}
			
			
			shapeX--;
			if (shapeX < 0)
				shapeX = 0;

			SetShape();
		}

		// Move to the right
		if (key_code == KeyInput.RIGHT && released)
		{
			Clear();
			
			if(shapeX + 4 == GAME_MATRIX[0].length)
			{
				// check if the position of the shape can be moved to the right
				shapeMatrix = TryShiftPositionTo(1, shapeMatrix);
				SetShape();
				shapePosition = 1;
				return;
			}
			
			shapeX++;
			if (shapeX + 4 > GAME_MATRIX[0].length)
				shapeX = GAME_MATRIX[0].length - 4;

			SetShape();
		}

		// Rotate the shape
		if (key_code == KeyInput.SPACE && released)
		{
			shapeRotation++;

			if (shapeRotation >= shapeRotationLeght)
				shapeRotation = 0;

			shapeMatrix = TetrisHelper.GetShapeWithRotation(shapeIndex, shapeRotation);
			
			  if(shapePosition == -1) 
				  shapeMatrix = TryShiftPositionTo(shapePosition, shapeMatrix);
			  
			  // yea this is weird but it works
			  // why? idkn it yust works
			  if(shapePosition == 1)
			  {
				  shapeMatrix = TryShiftPositionTo(shapePosition, shapeMatrix);
				  shapeMatrix = TryShiftPositionTo(shapePosition, shapeMatrix);
			  }
			  
			SetShape();
		}
	}
	
	
	boolean[][] TryShiftPositionTo(int direction, boolean[][] shape)
	{
		// checks if the first cell of each sub array is true
		// if so then return the current shape
		if(direction == -1 && 
				shape[0][0] ||
				shape[1][0] ||
				shape[2][0] ||
				shape[3][0])
			return shape;
		
		// checks if the last cell of each sub array is true
	    // if so then return the current shape
		if(direction == 1 && 
				shape[0][shape[0].length-1] ||
				shape[1][shape[1].length-1] ||
				shape[2][shape[2].length-1] ||
				shape[3][shape[3].length-1])
			return shape;
		
		final boolean[][] n_shape = new boolean[shape.length][shape[0].length];
		
		for(int r = 0; r < n_shape.length; r++)
		{
			for(int c = 0; c < n_shape[r].length; c++)
			{
				if(direction == 1)
				{
					// go right
					if(shape[r][c] && c+1 <= n_shape[r].length)
					{
						n_shape[r][c+direction] = shape[r][c];
					}
				}
				else
				{
					// go left
					if(shape[r][c] && c-1 >= 0)
					{
						n_shape[r][c+direction] = shape[r][c];
					}
				}
			}
		}
		return n_shape;
	}
	
	@Override
	protected void RenderGame(RenderBatch batch)
	{
		int x = 10;
		int y = 80;

		batch.Begin();
		for (int r = 0; r < GAME_MATRIX.length; r++)
		{
			for (int c = 0; c < GAME_MATRIX[r].length; c++)
			{
				if (GAME_MATRIX[r][c])
				{
					batch.setColor(Color.green);
					batch.DrawString("*", x, y, "tetris_shape_font");
				} else
				{
					batch.setColor(Color.red);
					batch.DrawString("*", x, y, "tetris_shape_font");
				}
				x += 32;
			}

			x = 10;
			y += 32;
		}
		batch.End();
	}

	public static void main(String[] args)
	{
		new GameClient(new Tetris(), GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
	}
}
