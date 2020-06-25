package com.stereopsis.stereoblocks;
import org.andengine.entity.primitive.Rectangle;
public class spiriteModel {
	public boolean filled;
	public boolean StaticColorFlag=true;
	
	public enum spriteColorType
	{
	    SPRITECOLOR_STATIC,
	    SPRITECOLOR_MOVING,
	    SPRITECOLOR_MIXED
	}
	
	public spriteColorType currentColorType = spriteColorType.SPRITECOLOR_STATIC;

	public float StaticColorR;
	public float StaticColorG;
	public float StaticColorB;
	
	public float MovingColorR;
	public float MovingColorG;
	public float MovingColorB;
	
	public float CurrentColorR;
	public float CurrentColorG;
	public float CurrentColorB;
	
//	public int color;
	public int r,g,b;
	
	public Rectangle rect;
	public int SceneIndex;
	
	public void setStaticColor(int ColorR, int ColorG, int ColorB)
	{
	    this.StaticColorR = (float)ColorR/255;
	    this.StaticColorG = (float)ColorG/255;
	    this.StaticColorB = (float)ColorB/255;
	}
	
	public void setMovingColor(int ColorR, int ColorG, int ColorB)
	{
	    this.MovingColorR = (float)ColorR/255;
	    this.MovingColorG = (float)ColorG/255;
	    this.MovingColorB = (float)ColorB/255;
	}	
	
	public void useStaticColor()
	{
		this.currentColorType = spriteColorType.SPRITECOLOR_STATIC;
		this.refreshCurrentColor();
	}
	
	public void useMovingColor()
	{
		this.currentColorType = spriteColorType.SPRITECOLOR_MOVING;
		this.refreshCurrentColor();
	}
	
	public void useMixedColor()
	{
		this.currentColorType = spriteColorType.SPRITECOLOR_MIXED;
		this.refreshCurrentColor();
	}
	
	public void refreshCurrentColor()
	{
		switch (this.currentColorType)
		{
		    case SPRITECOLOR_STATIC: 
		    	this.CurrentColorR = this.StaticColorR;
		    	this.CurrentColorG = this.StaticColorG;
		    	this.CurrentColorB = this.StaticColorB;
		        break;
		    case SPRITECOLOR_MOVING: 
		    	this.CurrentColorR = this.MovingColorR;
		    	this.CurrentColorG = this.MovingColorG;
		    	this.CurrentColorB = this.MovingColorB;
		        break;
		    case SPRITECOLOR_MIXED: 
		    	//Add the colors together
		    	this.CurrentColorR = this.StaticColorR + this.MovingColorR;
		    	this.CurrentColorG = this.StaticColorG + this.MovingColorG;
		    	this.CurrentColorB = this.StaticColorB + this.MovingColorB;
		        break;
		    default:
			    break;
	    }
		this.rect.setColor(this.CurrentColorR,this.CurrentColorG,this.CurrentColorB);
	}
//	public spriteModel()
//	{
//		
//	}

}
