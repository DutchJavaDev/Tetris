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
	
	public static final byte EMPTY_SPACE = 0; // empty space
	public static final byte ACTIVE_SPACE = 1; // shape thats moves
	public static final byte NONE_ACTIVE_SPACE = 2; // shape that has been placed in game

	private final byte[][] GAME_MATRIX;

	private int shapeIndex;
	private int shapePosition = 0;
	private int shapeRotation = 0;
	private int shapeRotationLeght = 0;
	private byte[][] shapeMatrix;

	private int shapeX = 4;
	private int shapeY = 0;
	private float counter = 0f;
	private final float updateTime = 25f;

	Tetris()
	{
		super("Tetris with a twist");
		ResizeAble = false;
		GAME_MATRIX = new byte[18][12];
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
		for (int r = shapeY; r < shapeY + 4; r++)
		{
			for (int cell = shapeX; cell < shapeX + 4; cell++)
			{
				if(GAME_MATRIX[r][cell] == NONE_ACTIVE_SPACE)
				{
					// Weird bug with replacing active_space with non_active_space
					System.out.println("Fking helll");
				}
			}
		}
	}

	private void SetShape()
	{
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
		if(IsAtBottom() || HasCollision())
		{
			PlaceShape();
			CreateShape();
			return;
		}
		
		// Drop the shape after an amount of time
		if (counter >= updateTime)
		{
			// clear matrix if shape is not at the bottom
			if (!IsAtBottom())
				Clear();

			counter = 0;
			
			shapeY += 1;

			SetShape();
		} else
		{
			counter += (Delta * 1);
		}
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
		if (key_code == KeyInput.SPACE && released && shapeIndex != 3)
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
	
	
	byte[][] TryShiftPositionTo(int direction, byte[][] shape)
	{
		final boolean leftBlock = (shape[0][0] == ACTIVE_SPACE || 
									  shape[1][0] == ACTIVE_SPACE || 
									  shape[2][0] == ACTIVE_SPACE || 
									  shape[3][0] == ACTIVE_SPACE);
		
		final boolean rightBlock = (shape[0][shape[0].length-1] == ACTIVE_SPACE||
									   shape[1][shape[1].length-1] == ACTIVE_SPACE || 
									   shape[2][shape[2].length-1] == ACTIVE_SPACE || 
									   shape[3][shape[3].length-1] == ACTIVE_SPACE);
		
		// checks if the first cell of each sub array is true
		// if so then return the current shape
		if(direction == -1 && leftBlock)
			return shape;
		
		// checks if the last cell of each sub array is true
	    // if so then return the current shape
		if(direction == 1 && rightBlock)
			return shape;
		
		final byte[][] n_shape = new byte[shape.length][shape[0].length];
		
		for(int r = 0; r < n_shape.length; r++)
		{
			for(int c = 0; c < n_shape[r].length; c++)
			{
				if(direction == 1)
				{
					// go right
					if(shape[r][c] == ACTIVE_SPACE && c+1 <= n_shape[r].length)
					{
						n_shape[r][c+direction] = shape[r][c];
					}
				}
				else
				{
					// go left
					if(shape[r][c] == ACTIVE_SPACE && c-1 >= 0)
					{
						n_shape[r][c+direction] = shape[r][c];
					}
				}
			}
		}
		return n_shape;
	}
	
	boolean IsAtBottom()
	{
		return shapeY + 4 == GAME_MATRIX.length;
	}
	
	boolean HasCollision()
	{
		int collisionFound = 0;
		
		byte[] shape_bottom = GAME_MATRIX[shapeY + 3];
		byte[] bottom_check = GAME_MATRIX[shapeY + 4];
		
		for(int i = shapeX; i < shapeX+4; i++)
		{
			if(shape_bottom[i] == ACTIVE_SPACE && bottom_check[i] == NONE_ACTIVE_SPACE)
				collisionFound++;
		}
		
		return collisionFound > 0;
	}
	
	void PlaceShape()
	{
		for(int r = shapeY; r < shapeY + 4; r++)
		{
			for(int c = shapeX; c < shapeX + 4; c++)
			{
				if(GAME_MATRIX[r][c] == ACTIVE_SPACE)
				{
					GAME_MATRIX[r][c] = NONE_ACTIVE_SPACE;
				}
			}
		}
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
				if (GAME_MATRIX[r][c] == ACTIVE_SPACE)
				{
					batch.setColor(Color.green);
					batch.DrawString("*", x, y, "tetris_shape_font");
				}
				else if(GAME_MATRIX[r][c] == NONE_ACTIVE_SPACE)
				{
					batch.setColor(Color.blue);
					batch.DrawString("*", x, y, "tetris_shape_font");
				}
				else if(GAME_MATRIX[r][c] == EMPTY_SPACE)
				{
					batch.setColor(Color.pink);
					batch.DrawString("*", x, y, "tetris_shape_font");
				}
				else
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
