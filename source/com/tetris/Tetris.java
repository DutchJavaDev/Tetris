/**
 * 5 mrt. 2019
 */
package com.tetris;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

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
	public static int GAME_WIDTH = 420;
	public static int GAME_HEIGHT = 640;
	
	private final boolean[][] GAME_MATRIX;

	private int shapeIndex;
	private int shapeRotation = 0;
	private int rotationLeght = 0;
	private boolean[][] shapeMatrix;
	private boolean[][] empty;
	
	private int shapeX = 4;
	private int shapeY = 0;
	private float counter = 0f;
	private float updateTime = 20f;
	
	public Tetris()
	{
		super("Tetris with a twist");
		ResizeAble = false;
		GAME_MATRIX = new boolean[18][12];
		empty = new boolean[TetrisHelper.SHAPE_MATRIX_HEIGHT][TetrisHelper.SHAPE_MATRIX_WIDTH];
		CreateShape();
	}
	
	private void CreateShape()
	{
		shapeIndex = TetrisHelper.GetRandomShapeNumber();
		shapeMatrix = TetrisHelper.GetShape(shapeIndex);
		shapeRotation = 0;
		rotationLeght = TetrisHelper.GetRotationLenght(shapeIndex);
		
		shapeX = 4;
		shapeY = 0;
		
		SetShape();
	}
	
	private void Clear()
	{
		System.arraycopy(empty[0], 0, GAME_MATRIX[shapeY], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(empty[1], 0, GAME_MATRIX[shapeY+1], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(empty[2], 0, GAME_MATRIX[shapeY+2], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(empty[3], 0, GAME_MATRIX[shapeY+3], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
	}
	
	private void CheckBottomBounds()
	{
		if(shapeY+4 > GAME_MATRIX.length)
			CreateShape();
	}
	
	private void SetShape()
	{
		CheckBottomBounds();
		Clear();
		
		System.arraycopy(shapeMatrix[0], 0, GAME_MATRIX[shapeY], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(shapeMatrix[1], 0, GAME_MATRIX[shapeY+1], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(shapeMatrix[2], 0, GAME_MATRIX[shapeY+2], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
		System.arraycopy(shapeMatrix[3], 0, GAME_MATRIX[shapeY+3], shapeX, TetrisHelper.SHAPE_MATRIX_WIDTH);
	}
	
	@Override
	public void LoadResources(ResourceManager manager)
	{
		TetrisHelper.Init();
		manager.LoadFont("tetris_shape_font", "Comic Sans MS", Font.BOLD, 64);
	}

	@Override
	public void mousePressed(ApplicationEvent button, int x, int y)
	{}

	@Override
	public void mouseReleased(ApplicationEvent button, int x, int y)
	{}

	
	@Override
	protected void UpdateGame()
	{
		// Drop the shape after an amount of time
		if(counter >= updateTime)
		{
			counter = 0;
			shapeY+=1;
			
			SetShape();
		}
		else
		{
			counter += (Delta * 1);
		}
	}
	
	@Override
	public void keyboardEvent(ApplicationEvent event,int key_code)
	{
		boolean released = event == ApplicationEvent.KEYRELEASED;
		
		// Move to the left
		if(key_code == KeyInput.LEFT && released)
		{
			shapeX--;
			if(shapeX < 0)
				shapeX = 0;
			
			SetShape();
		}
		
		// Move to the right
		if(key_code == KeyInput.RIGHT && released)
		{
			// BUG: when shape is only 3 long in the width
			shapeX++;
			if(shapeX+4 > GAME_MATRIX[0].length)
				shapeX = GAME_MATRIX[0].length-4;
			
			SetShape();
		}
		
		// Rotate the shape
		if(key_code == KeyInput.SPACE && released)
		{
			shapeRotation++;
			
			if(shapeRotation >= rotationLeght)
				shapeRotation = 0;
			
			shapeMatrix = TetrisHelper.GetShapeWithRotation(shapeIndex, shapeRotation);
			
			SetShape();
		}
	}
	
	@Override
	protected void RenderGame(RenderBatch batch)
	{
		int x = 10;
		int y = 80;
		
		batch.Begin();
		for(int r = 0; r < GAME_MATRIX.length; r++)
		{
			for(int c = 0; c < GAME_MATRIX[r].length; c++)
			{
				if(GAME_MATRIX[r][c])
				{
					batch.setColor(Color.green);
					batch.DrawString("*", x, y, "tetris_shape_font");
				}
				else
				{
					batch.setColor(Color.red);
					batch.DrawString("*", x, y, "tetris_shape_font");
				}
				x+=32;
			}
			
			x = 10;
			y += 32;
		}
		batch.End();
	}
	
	


	public static void main(String[] args)
	{
		new GameClient(new Tetris(), GAME_WIDTH, GAME_HEIGHT,GAME_WIDTH,GAME_HEIGHT);
	}
}
