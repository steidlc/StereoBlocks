package com.stereopsis.stereoblocks;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import com.stereopsis.stereoblocks.SceneManager.SceneType;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.GLES20;
import android.preference.PreferenceManager;



public class OffsetScene extends BaseScene implements IOnSceneTouchListener {
	final static String TAG = "GameScene";
	private SceneType previousSceneType = SceneType.SCENE_GAME;
//	GameActivity activity;
	private ResourcesManager resourcesManager;
	GameManager Game;
	private static OffsetScene instance;
	
	public HUD sceneHUD;
	
//	private Rectangle staticLayer;
//	private Rectangle movingLayer;
	
	public enum offsetLayerType
	{
		OFFSET_LAYER_LAZY_EYE,
		OFFSET_LAYER_DOMINANT_EYE
	}
	
	public offsetLayerType offsetLayerSelect;
		
	private Rectangle lazyEyeLayer;
	private Rectangle lazyEyeCrossLayer;
	private Rectangle dominantEyeLayer;
	private Rectangle dominantEyeCrossLayer;
	
	private Sprite resetBtn;
	private static final float RESET_BUTTON_SCALE = 0.4f;
	
	private float lazyEyeOrigX;
	private float lazyEyeOrigY;

	private float dominantEyeOrigX;
	private float dominantEyeOrigY;
	
	private float previousX;
	private float previousY;
	
//	private float Game.lazyEyeMaxX;
//	private float Game.lazyEyeMaxY;
//	private float Game.dominantEyeMaxX;
//	private float Game.dominantEyeMaxY;
	
	private Text offsetXText;
	private Text offsetYText;
	
	
    public static OffsetScene getInstance()
    {
        return instance;
    }
    
	@Override
	public void createScene() {
		//creatScene is getting called before the variables are initialized
		instance = this;
		resourcesManager = ResourcesManager.getInstance();
	    Game = GameManager.getSharedInstance();
	    //Game = new GameManager();
	    //Game.setScene(this);
	  
	    

	    setOffsetLayerSelect();
	    createBackground();
	    createHUD();
	    createShapes();
	    
	    setOnSceneTouchListener(this);
//	    registerUpdateHandler(new GameLoopUpdateHandler());      

	}

	public void reloadScene()
	{
		setOffsetLayerSelect();
		camera.setHUD(sceneHUD);
		setColorShapes();
	}
    
	private void setOffsetLayerSelect()
	{
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(resourcesManager.activity);
	    String offsetLayerString = prefs.getString("prefOffsetLayerLast", "lazy");
	    if(offsetLayerString.equals("lazy"))
	    {
	        
	        offsetLayerSelect = offsetLayerType.OFFSET_LAYER_LAZY_EYE;
	    }
	    else
	    {
	        offsetLayerSelect = offsetLayerType.OFFSET_LAYER_DOMINANT_EYE;	    	
	    }
	}
	
    private void createBackground()
    {
        setBackground(new Background(Color.BLACK));
    }
    private void createHUD()
    {
        sceneHUD = new HUD();
        
        // CREATE SCORE TEXT
        offsetXText = new Text(0.4f*camera.getWidth(), 5, resourcesManager.fontGame, "Horizontal: -0123456789 px", new TextOptions(HorizontalAlign.LEFT), vbom);
        offsetYText = new Text(0.4f*camera.getWidth(),30, resourcesManager.fontGame, "Vertical: -0123456789 px", new TextOptions(HorizontalAlign.LEFT), vbom);
        
        //offsetXText.setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);
        //offsetYText.setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);

        this.setOffsetText();
        sceneHUD.attachChild(offsetXText);
        sceneHUD.attachChild(offsetYText);
        
        camera.setHUD(sceneHUD);
    }
    private void setOffsetText()
    {
		if(offsetLayerSelect == offsetLayerType.OFFSET_LAYER_LAZY_EYE)
		{
	        offsetXText.setColor((float)Game.falling_color_r/255, (float)Game.falling_color_g/255, (float)Game.falling_color_b/255);
	        offsetYText.setColor((float)Game.falling_color_r/255, (float)Game.falling_color_g/255, (float)Game.falling_color_b/255);

            offsetXText.setText(String.format("Horizontal: %.2f px", Game.fallingOffsetX));
            offsetYText.setText(String.format("Vertical: %.2f px", Game.fallingOffsetY));
		}
		else
		{
	        offsetXText.setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);
	        offsetYText.setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);

            offsetXText.setText(String.format("Horizontal: %.2f px", Game.staticOffsetX));
            offsetYText.setText(String.format("Vertical: %.2f px", Game.staticOffsetY));	        		
		}
    }

    private void createShapes()
    {
    	float squareSize = 200f;
    	float squareMiddle = squareSize/2.0f;
    	float crossHalfSize = 40f;
    	float crossThickness = 10f;
    	float crossHalfThickness = crossThickness/2.0f;
    	
    	float smallSquarePad = 5f;
    	float smallSquareSize = crossHalfSize - crossThickness - smallSquarePad;
    	
//    	Game.lazyEyeMaxX = Game.boardOffsetX;
//    	Game.lazyEyeMaxY = Game.boardOffsetY;
//    	Game.dominantEyeMaxX = Game.boardOffsetX;
//      Game.dominantEyeMaxY = Game.boardOffsetY;
    	
    	dominantEyeLayer = new Rectangle(0,0,squareSize,squareSize, resourcesManager.vbom);
    	
    	//Create the dominant eye border
    	Rectangle dETopBorder = new Rectangle(0, 0, squareSize, crossThickness, resourcesManager.vbom);
    	Rectangle dEBottomBorder = new Rectangle(0, (squareSize-crossThickness), squareSize , crossThickness, resourcesManager.vbom);
    	Rectangle dELeftBorder = new Rectangle(0, crossThickness, crossThickness, (squareSize - 2.0f*crossThickness), resourcesManager.vbom);    	
    	Rectangle dERightBorder = new Rectangle(squareSize-crossThickness, crossThickness, crossThickness, (squareSize - 2.0f*crossThickness), resourcesManager.vbom);
    	
    	dominantEyeLayer.attachChild(dETopBorder);
    	dominantEyeLayer.attachChild(dEBottomBorder);
    	dominantEyeLayer.attachChild(dELeftBorder);
    	dominantEyeLayer.attachChild(dERightBorder);
    	
    	// Create the small squares that are on each side of the cross
    	Rectangle dEUpperLeftSq = new Rectangle((squareMiddle-(crossHalfThickness+smallSquarePad+smallSquareSize)), (squareMiddle-(smallSquareSize+smallSquarePad+crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	Rectangle dEUpperRightSq = new Rectangle((squareMiddle+(crossHalfThickness+smallSquarePad)), (squareMiddle-(smallSquareSize + smallSquarePad + crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	Rectangle dELowerLeftSq = new Rectangle((squareMiddle-(crossHalfThickness+smallSquarePad+smallSquareSize)), (squareMiddle+(smallSquarePad + crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	Rectangle dELowerRightSq = new Rectangle((squareMiddle+(crossHalfThickness+smallSquarePad)), (squareMiddle+(smallSquarePad + crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	
    	dominantEyeLayer.attachChild(dEUpperLeftSq);
    	dominantEyeLayer.attachChild(dEUpperRightSq);
    	dominantEyeLayer.attachChild(dELowerLeftSq);
    	dominantEyeLayer.attachChild(dELowerRightSq);
    	
    	dominantEyeCrossLayer = new Rectangle((squareMiddle - crossHalfThickness), (squareMiddle-crossHalfSize+crossHalfThickness), crossHalfSize, crossHalfSize, resourcesManager.vbom)
 	    {
 	        @Override
 	        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
 	        {
 	        	if (pSceneTouchEvent.isActionDown())
 	        	{
 	        		ResourcesManager.getInstance().playPressButtonSound();
 	        		dominantEyeCrossLayer.setScale(1.2f); //Make bigger to emphasize selection
 	        		this.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {
 	        			@Override
 	        			public void onTimePassed(final TimerHandler pTimerHandler) {
 	        				unregisterUpdateHandler(pTimerHandler);
 	        				dominantEyeCrossLayer.setScale(1.0f);
 	        			}
 	        		}));
 	        		previousX = pSceneTouchEvent.getX();
 	   		        previousY = pSceneTouchEvent.getY();
 	        		instance.selectOffsetLayer(offsetLayerType.OFFSET_LAYER_DOMINANT_EYE);
 	        		return true;
 	        	}
// 	        	else
// 	        	{
// 	        		dominantEyeLayer.setScale(1.0f); //Back to normal size
// 	        	}
 	            return false; //Let the scene touch event take over
 	        };
 	    };
        this.registerTouchArea(dominantEyeCrossLayer);    
        
    	Rectangle dominantEyeBlockVertical = new Rectangle(0,0,crossThickness,(crossHalfSize - crossThickness),resourcesManager.vbom);
    	Rectangle dominantEyeBlockHorizontal = new Rectangle(crossThickness, (crossHalfSize - crossThickness), (crossHalfSize - crossThickness), crossThickness, resourcesManager.vbom);
    	
    	dominantEyeCrossLayer.attachChild(dominantEyeBlockVertical);
    	dominantEyeCrossLayer.attachChild(dominantEyeBlockHorizontal);

    	dominantEyeCrossLayer.setColor(0.0f,0.0f,0.0f);
    	dominantEyeCrossLayer.setAlpha(0.0f); //Make the layer transparent.
    	
    	dominantEyeLayer.attachChild(dominantEyeCrossLayer); //Attach this last
    	
  //  	dominantEyeLayer.setPosition((resourcesManager.camera.getWidth()/2.0f - (crossThickness/2.0f)), (resourcesManager.camera.getHeight()/2.0f - crossHalfSize + crossThickness/2.0f));
    	//Set the location of the cross lazy eye layer
//    	dominantEyeOrigX = (resourcesManager.camera.getWidth()/2.0f - (crossThickness/2.0f));
//    	dominantEyeOrigY = (resourcesManager.camera.getHeight()/2.0f - crossHalfSize + (crossThickness/2.0f));

    	dominantEyeOrigX = (resourcesManager.camera.getWidth()/2.0f - squareMiddle);
    	dominantEyeOrigY = (resourcesManager.camera.getHeight()/2.0f - squareMiddle);

    	dominantEyeLayer.setPosition(Game.staticOffsetX + dominantEyeOrigX, Game.staticOffsetY + dominantEyeOrigY);
    	
    	dominantEyeLayer.setColor(0.0f,0.0f,0.0f);
    	dominantEyeLayer.setAlpha(0.0f); //Make the layer transparent.
    	
    	//Set block color to lazy eye color
//    	dominantEyeBlockVertical.setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);
//    	dominantEyeBlockHorizontal.setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);
    	    	
    	this.attachChild(dominantEyeLayer);
    	
    	//Create a cross. Upper right is dominant eye
    	//Lower left is the lazy eye
    	lazyEyeLayer = new Rectangle(0,0,squareSize,squareSize, resourcesManager.vbom);
    	
    	//Create the dominant eye border
    	Rectangle lETopBorder = new Rectangle(0, 0, squareSize, crossThickness, resourcesManager.vbom);
    	Rectangle lEBottomBorder = new Rectangle(0, (squareSize-crossThickness), squareSize , crossThickness, resourcesManager.vbom);
    	Rectangle lELeftBorder = new Rectangle(0, crossThickness, crossThickness, (squareSize - 2.0f*crossThickness), resourcesManager.vbom);    	
    	Rectangle lERightBorder = new Rectangle(squareSize-crossThickness, crossThickness, crossThickness, (squareSize - 2.0f*crossThickness), resourcesManager.vbom);
    	
    	lazyEyeLayer.attachChild(lETopBorder);
    	lazyEyeLayer.attachChild(lEBottomBorder);
    	lazyEyeLayer.attachChild(lELeftBorder);
    	lazyEyeLayer.attachChild(lERightBorder);
    	
    	// Create the small squares that are on each side of the cross
    	Rectangle lEUpperLeftSq = new Rectangle((squareMiddle-(crossHalfThickness+smallSquarePad+smallSquareSize)), (squareMiddle-(smallSquareSize+smallSquarePad+crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	Rectangle lEUpperRightSq = new Rectangle((squareMiddle+(crossHalfThickness+smallSquarePad)), (squareMiddle-(smallSquareSize + smallSquarePad + crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	Rectangle lELowerLeftSq = new Rectangle((squareMiddle-(crossHalfThickness+smallSquarePad+smallSquareSize)), (squareMiddle+(smallSquarePad + crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	Rectangle lELowerRightSq = new Rectangle((squareMiddle+(crossHalfThickness+smallSquarePad)), (squareMiddle+(smallSquarePad + crossHalfThickness)), smallSquareSize, smallSquareSize, resourcesManager.vbom);
    	
    	lazyEyeLayer.attachChild(lEUpperLeftSq);
    	lazyEyeLayer.attachChild(lEUpperRightSq);
    	lazyEyeLayer.attachChild(lELowerLeftSq);
    	lazyEyeLayer.attachChild(lELowerRightSq);
    	
    	lazyEyeCrossLayer = new Rectangle(squareMiddle-crossHalfSize+crossHalfThickness, squareMiddle-crossHalfThickness, crossHalfSize, crossHalfSize, resourcesManager.vbom)
 	    {
 	        @Override
 	        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
 	        {
 	        	if (pSceneTouchEvent.isActionDown())
 	        	{
 	        		ResourcesManager.getInstance().playPressButtonSound();
 	        		lazyEyeCrossLayer.setScale(1.2f); //Make bigger to emphasize selection
 	        		this.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {
 	        			@Override
 	        			public void onTimePassed(final TimerHandler pTimerHandler) {
 	        				unregisterUpdateHandler(pTimerHandler);
 	        				lazyEyeCrossLayer.setScale(1.0f);
 	        			}
 	        		}));
 	        		previousX = pSceneTouchEvent.getX();
 	   		        previousY = pSceneTouchEvent.getY();
 	   		        
 	        		instance.selectOffsetLayer(offsetLayerType.OFFSET_LAYER_LAZY_EYE);
 	        		return true;
 	        	}
// 	        	else
// 	        	{
// 	        		lazyEyeLayer.setScale(1.0f); //Back to normal size
// 	        	}
 	            return false; //Let the scene touch event take over
 	        };
 	    };
        this.registerTouchArea(lazyEyeCrossLayer);    
        
    	Rectangle lazyEyeBlockVertical = new Rectangle((crossHalfSize - crossThickness), crossThickness, crossThickness, (crossHalfSize - crossThickness),resourcesManager.vbom);
    	Rectangle lazyEyeBlockHorizontal = new Rectangle(0, 0, crossHalfSize-crossThickness, crossThickness, resourcesManager.vbom);
    	
    	lazyEyeCrossLayer.attachChild(lazyEyeBlockVertical);
    	lazyEyeCrossLayer.attachChild(lazyEyeBlockHorizontal);

    	lazyEyeCrossLayer.setColor(0.0f,0.0f,0.0f);
    	lazyEyeCrossLayer.setAlpha(0.0f); //Make the layer transparent.
    	
    	lazyEyeLayer.attachChild(lazyEyeCrossLayer);
    	
    	lazyEyeLayer.setColor(0.0f,0.0f,0.0f);
    	lazyEyeLayer.setAlpha(0.0f); //Make the layer transparent.
    	
    	//Set block color to lazy eye color
//    	lazyEyeBlockVertical.setColor((float)Game.falling_color_r/255, (float)Game.falling_color_g/255, (float)Game.falling_color_b/255);
//    	lazyEyeBlockHorizontal.setColor((float)Game.falling_color_r/255, (float)Game.falling_color_g/255, (float)Game.falling_color_b/255);
    	    	
    	this.attachChild(lazyEyeLayer);
    	
    	//Set the location of the cross lazy eye layer
    	lazyEyeOrigX = (resourcesManager.camera.getWidth()/2.0f - squareMiddle);
    	lazyEyeOrigY = (resourcesManager.camera.getHeight()/2.0f - squareMiddle);
    	
    	lazyEyeLayer.setPosition(Game.fallingOffsetX + lazyEyeOrigX, Game.fallingOffsetY + lazyEyeOrigY);
	
    	/**********************************************************************
		Setup the rectangle so that it adds the colors of objects underneath it
		This will make the red moving object turn to purple for any part that 
		overlap with the static blue shapes.
		**********************************************************************/
    	lazyEyeLayer.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lazyEyeCrossLayer.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lazyEyeBlockVertical.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lazyEyeBlockHorizontal.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	
    	lETopBorder.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lEBottomBorder.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lELeftBorder.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lERightBorder.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	
    	lEUpperLeftSq.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lEUpperRightSq.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lELowerLeftSq.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	lELowerRightSq.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
    	
 	    resetBtn= new Sprite(0, 0, resourcesManager.reset_region, vbom)
 	    {
 	        @Override
 	        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
 	        {
 	        	if (pSceneTouchEvent.isActionDown())
 	        	{
 	        		ResourcesManager.getInstance().playPressButtonSound();
 	        		resetBtn.setScale(1.2f*RESET_BUTTON_SCALE); //Make bigger to emphasize selection
 	        		this.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {
 	        			@Override
 	        			public void onTimePassed(final TimerHandler pTimerHandler) {
 	        				unregisterUpdateHandler(pTimerHandler);
 	        				resetBtn.setScale(RESET_BUTTON_SCALE);
 	        			}
 	        		}));

 	        		return true;
 	        	}
 	        	else
 	        	{
 	        		//Reset the offset to zero
// 	        		resetBtn.setScale(RESET_BUTTON_SCALE); //Back to normal size
 	        		Game.fallingOffsetX = 0.0f;
 	        		Game.fallingOffsetY = 0.0f;
 	        		Game.staticOffsetX = 0.0f;
 	        		Game.staticOffsetY = 0.0f;
 	        		dominantEyeLayer.setPosition(dominantEyeOrigX, dominantEyeOrigY);
 	        		lazyEyeLayer.setPosition(lazyEyeOrigX, lazyEyeOrigY);	        		
 	        	}
 	            return false; //Let the scene touch event take over
 	        };
 	    };

 	    this.setColorShapes();
 	   
 	    this.attachChild(resetBtn);
   	    this.registerTouchArea(resetBtn);
 	    resetBtn.setScale(RESET_BUTTON_SCALE); //Scale the button
 	    
 	    //Set position to 5,5 upper left of screen
 	    //Button is scaled to need to set position left and up from where a full scale 100% would be
 	    resetBtn.setPosition(5 + -0.5f*(resetBtn.getWidth()-resetBtn.getWidthScaled()),5 + -0.5f*(resetBtn.getHeight()-resetBtn.getHeightScaled()));

    }
    private void setColorShapes()
    {
    	for(int i=0; i<dominantEyeLayer.getChildCount(); i++)
    	{
    		dominantEyeLayer.getChildByIndex(i).setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);
    	}

    	for(int i=0; i<dominantEyeCrossLayer.getChildCount(); i++)
    	{
    		dominantEyeCrossLayer.getChildByIndex(i).setColor((float)Game.set_block_color_r/255, (float)Game.set_block_color_g/255, (float)Game.set_block_color_b/255);
    	}
    	
    	for(int i=0; i<lazyEyeLayer.getChildCount(); i++)
    	{
    		lazyEyeLayer.getChildByIndex(i).setColor((float)Game.falling_color_r/255, (float)Game.falling_color_g/255, (float)Game.falling_color_b/255);
    	}
    	
    	for(int i=0; i<lazyEyeCrossLayer.getChildCount(); i++)
    	{
    		lazyEyeCrossLayer.getChildByIndex(i).setColor((float)Game.falling_color_r/255, (float)Game.falling_color_g/255, (float)Game.falling_color_b/255);
    	}
    	
    	//Make sure these layers are set back to black, so that the color blending works correctly
    	lazyEyeLayer.setColor(0.0f,0.0f,0.0f);
    	lazyEyeCrossLayer.setColor(0.0f,0.0f,0.0f);

    }
    private void selectOffsetLayer(offsetLayerType offsetSelected)
    {
    	this.offsetLayerSelect = offsetSelected;
    	this.setOffsetText();
    }
    
	@Override
	public void onBackKeyPressed() {
		if(previousSceneType == SceneType.SCENE_GAME)
		{
			SceneManager.getInstance().loadGameScene(resourcesManager.engine);
		}
		else
		{
			SceneManager.getInstance().loadMenuScene(resourcesManager.engine);
		}
		Game.paint_scene(); //Update the block offsets.
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_OFFSET;
	}

	@Override
	public void disposeScene() {
		
		camera.setHUD(null);
		
//		lazyEyeLayer.detachSelf();
//		lazyEyeLayer.dispose();
//		dominantEyeLayer.detachSelf();
//		dominantEyeLayer.dispose();
//		
//        this.detachSelf();
//        this.dispose();
		
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown())
		{
			previousX = pSceneTouchEvent.getX();
			previousY = pSceneTouchEvent.getY();
			return true;
		}
		else if(pSceneTouchEvent.isActionMove())
		{
			float currentX = pSceneTouchEvent.getX();
			float currentY = pSceneTouchEvent.getY();
			this.addOffsetXY(currentX - previousX, currentY - previousY);
			previousX = currentX;
			previousY = currentY;
						
			this.setOffsetText();
			return true;
		}
		else if(pSceneTouchEvent.isActionUp())
		{
			this.setOffsetText();
    		setOffsetSharedPrefereences();
			return true;
		}
		return false;
	}
	
	public void addOffsetXY(float addOffsetX, float addOffsetY)
	{
		if(offsetLayerSelect == offsetLayerType.OFFSET_LAYER_LAZY_EYE)
		{
			Game.fallingOffsetX = offsetErrorCheck("Lazy Eye", "Horizontal", Game.fallingOffsetX + addOffsetX, -1.0f*Game.lazyEyeMaxX, Game.lazyEyeMaxX);
			Game.fallingOffsetY = offsetErrorCheck("Lazy Eye", "Vertical", Game.fallingOffsetY + addOffsetY, -1.0f*Game.lazyEyeMaxY, Game.lazyEyeMaxY);
			lazyEyeLayer.setPosition(Game.fallingOffsetX + lazyEyeOrigX, Game.fallingOffsetY + lazyEyeOrigY);
		}
		else //Dominant eye
		{
			Game.staticOffsetX = offsetErrorCheck("Dominant Eye", "Horizontal", Game.staticOffsetX + addOffsetX, -1.0f*Game.dominantEyeMaxX, Game.dominantEyeMaxX);
			Game.staticOffsetY = offsetErrorCheck("Dominant Eye", "Vertical", Game.staticOffsetY + addOffsetY, -1.0f*Game.dominantEyeMaxY, Game.dominantEyeMaxY);
			dominantEyeLayer.setPosition(Game.staticOffsetX + dominantEyeOrigX, Game.staticOffsetY + dominantEyeOrigY);			
		}
		this.setOffsetText();
	}

    private float offsetErrorCheck(String eyeTypeM, String offsetType, float offset, float Min, float Max)
    {

    	//Make sure value is within size of the screen
    	
//        String ErrorM = "";
        if(offset < Min)
        {
        	offset = Min;
//        	ErrorM = eyeTypeM + " " + offsetType + " offset outside of game bounds. Offset will be set to " + String.valueOf(offset);
        }
        else if(offset > Max)
        {
        	offset = Max;
//        	ErrorM = eyeTypeM + " " + offsetType + " offset outside of game bounds. Offset will be set to " + String.valueOf(offset);
        
        }
        
//        if(ErrorM.length() > 0)
//        { //There was an error, print the error message
//
//        	errorMessage(ErrorM, Toast.LENGTH_LONG);
//        }
        
        return offset;
    	
    }
    
    public void errorMessage(CharSequence text, int duration)
    {
//    	Context context = getApplicationContext();   	
//    	Toast.makeText(context, text, duration).show();
    	resourcesManager.activity.errorMessage(text, duration);
    }
    
	private void setOffsetSharedPrefereences()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(resourcesManager.activity);
		Editor editor = prefs.edit();
		editor.putString("prefMovingXOffset",  String.format("%.2f", Game.fallingOffsetX));
		editor.putString("prefMovingYOffset",  String.format("%.2f", Game.fallingOffsetY));

		editor.putString("prefStaticXOffset",  String.format("%.2f", Game.staticOffsetX));
		editor.putString("prefStaticYOffset",  String.format("%.2f", Game.staticOffsetY));
		
		if(offsetLayerSelect == offsetLayerType.OFFSET_LAYER_LAZY_EYE)
		{
			editor.putString("prefOffsetLayerLast",  "lazy");
		}
		else
		{
			editor.putString("prefOffsetLayerLast",  "dominant");
		}
		editor.commit();
	}
}
