package com.stereopsis.stereoblocks;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;



import com.stereopsis.stereoblocks.BaseScene;
import com.stereopsis.stereoblocks.GameManager;
import com.stereopsis.stereoblocks.SceneManager.SceneType;

//import android.util.Log;


//import com.android.Game.FallingBlocks.GameActivity;



public class GameScene extends BaseScene implements IOnSceneTouchListener, IOnMenuItemClickListener {
	final static String TAG = "GameScene";
//	GameActivity activity;
	private ResourcesManager resourcesManager;
	private static final GameScene INSTANCE = new GameScene();
	
	GameManager Game;
	GameScene instance;
//	private static GameLoopUpdateHandler updateHandler;

	private static TimerHandler updateHandler;

	public HUD gameHUD;
	public Text scoreText;
	public Text pauseText;
	public Text nextText;
	
	public Rectangle pauseRectangle;
	
//	private int score = 0;
	
	// ===CONSTANTS===//
	final static int OFFSET = 15;
	private static final int NEXT_BLOCK_LOC_X = 400;
	
	private final int MENU_RESUME = 0;
	private final int MENU_RESTART = 1;
	private final int MENU_OPTIONS = 2;
	private final int MENU_OFFSET = 3;
	private final int MENU_QUIT = 4;
	
//	public long startTime;
//	public long elapsedTime;
	
	// ===CONSTRUCTOR===/
//	public GameScene() {
//	    activity = GameActivity.getSharedInstance();
//	    Game = GameManager.getSharedInstance();
//	    
//	  
//	    setOnSceneTouchListener(this);
//	    registerUpdateHandler(new GameLoopUpdateHandler());
//	}
	
    public static GameScene getInstance()
    {
        return INSTANCE;
    }
    
    @Override
    public void createScene()
    {
    	instance = this;
    	resourcesManager = ResourcesManager.getInstance();
//	    Game = GameManager.getSharedInstance();
	    Game = new GameManager();
	    
	    Game.setScene(this);
	    activity.initGame();
	    
	    createBackground();
	    createHUD();
	    
	    createPauseBtn();
		
	    activity.userSettingsUpdate();
	    
	    //Start the game
	    Game.game_started=true;
		Game.game_paused=false;
	    Game.generate_new_ship();
	    Game.calculate_is_performed=false;
	    
	    //IMPORTANT
	    //The update handler must be created and registered before the game is paused
	    //and the update handler is unregistered.
	    //If the update handler is unregistered before it is created, then the loop
	    //runs twice as fast for some reason. Seems like a bug.
	    //updateHandler = new GameLoopUpdateHandler();
//	    startTime = System.nanoTime();
	    updateHandler = new TimerHandler(activity.SpeedWaitMills/1000.0f, true, new ITimerCallback() {

	    	@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {		
//	    		Log.d("GameScene", String.format("Time Set: %f Time Elapsed: %f", updateHandler.getTimerSeconds(), updateHandler.getTimerSecondsElapsed()));
//	    		elapsedTime = System.nanoTime() - startTime;
//	    		Log.d("GameScene", String.format("Estimated Time: %f sec", elapsedTime/1.0E9f));
//	    		startTime = System.nanoTime();
//				if (SceneManager.getInstance().getCurrentSceneType() == SceneType.SCENE_GAME)
//				{	
					updateScene();
//				}

			}
		});
		
//		try{
//			unregisterUpdateHandler(updateHandler);
//		}
//		finally{			
//		}

		registerUpdateHandler(updateHandler); 

	    loadPauseMenuScene();
	    
	    setOnSceneTouchListener(this);
    }

    private void createBackground()
    {
        setBackground(new Background(Color.BLACK));
    }
    
    private void createHUD()
    {
        gameHUD = new HUD();
        
        // CREATE SCORE TEXT
        scoreText = new Text(40, 5, resourcesManager.fontGame, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        nextText = new Text(NEXT_BLOCK_LOC_X, 5, resourcesManager.fontGame , "Next :", vbom);
        
        
        //scoreText.setAnchorCenter(0, 0);    
//        monScore.setText("0");
        this.addToScore(0);
        gameHUD.attachChild(scoreText);
        
        gameHUD.attachChild(nextText);
        
        camera.setHUD(gameHUD);
    }
    
    private void createPauseBtn()
    {
    	Rectangle LeftSidePause;
    	Rectangle RightSidePause;
    	final float RECTANGLE_WIDTH = 50.0f;
    	final float RECTANGLE_HEIGHT = 30.0f;
    	float pauseWidth;
    	float pauseHeight;
    	float pauseSeparation;
    	
//        pauseText = new Text(5, 5, resourcesManager.fontGame, "PAUSE", new TextOptions(HorizontalAlign.LEFT), vbom);

 	    pauseRectangle = new Rectangle(camera.getCenterX() + 20, 5, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, vbom)
 	    {
 	        @Override
 	        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
 	        {
 	            if (pSceneTouchEvent.isActionUp())
 	            {
 	            	resourcesManager.playPressButtonSound();
 	                // execute action
 	            	loadPauseMenuScene();
 	            }
 	            return true;
 	        };
 	    };

         this.registerTouchArea(pauseRectangle);        
         this.attachChild(pauseRectangle);
 	    
//         pauseRectangle.setColor(0,0,0); //Black rectangle         
         pauseRectangle.setColor((float)Game.set_block_color_r/255,(float)Game.set_block_color_g/255,(float)Game.set_block_color_b/255); 
         
//         pauseWidth = 0.1f*RECTANGLE_WIDTH;
         pauseWidth = 8.0f;
         pauseHeight = 0.6f*RECTANGLE_HEIGHT;
         pauseSeparation = 0.5f*pauseWidth;
         
         LeftSidePause = new Rectangle(RECTANGLE_WIDTH/2.0f - pauseWidth - pauseSeparation/2.0f, (RECTANGLE_HEIGHT-pauseHeight)/2.0f, pauseWidth, pauseHeight, vbom);
         RightSidePause = new Rectangle(RECTANGLE_WIDTH/2.0f + pauseSeparation/2.0f, (RECTANGLE_HEIGHT-pauseHeight)/2.0f, pauseWidth, pauseHeight, vbom);
         
         LeftSidePause.setColor(0,0,0); //Black rectangle
         RightSidePause.setColor(0,0,0);
         
//         pauseText.setColor(0,0,0); //Black font
//         pauseRectangle.attachChild(pauseText);
         
         pauseRectangle.attachChild(LeftSidePause);
         pauseRectangle.attachChild(RightSidePause);
    	
    }
    public void loadPauseMenuScene()
    {
    	Game = GameManager.getSharedInstance();
 		//Game is running, so text should be Pause
      	//Pause the game
//     	Game.game_paused=true;
//     	Game.calculate_is_performed=true;
     	this.pauseGame();
     	
 		//Launch the pause menu scene
      	this.pauseRectangle.setVisible(false);
      	
      	//Create the menu scene
      	instance.setChildScene(pauseScene(), false, true, true);    	
    }
    
	private MenuScene pauseScene(){
		float scaleButton = 0.5f;
		float yOffset;

		float buttonXLocation;
		float buttonHeight;
		float titleHeight = 100.0f;
		
		Text titleText;
		Entity titleRectangle;
		
		final MenuScene pauseGame= new MenuScene(camera);
		pauseGame.setPosition(0, 0);
        
		resourcesManager.loadMenuTextures();
		
		titleText = new Text(40, 40, resourcesManager.fontTitle, "STEREO BLOCKS", new TextOptions(HorizontalAlign.LEFT), vbom);
//		titleText = new Text(30, 30, resourcesManager.fontGame, "FALLING BLOCKS", new TextOptions(HorizontalAlign.LEFT), vbom);

		titleText.setColor((float)Game.falling_color_r/255,(float)Game.falling_color_g/255,(float)Game.falling_color_b/255);
		
//		titleRectangle = new Rectangle(0, 0, camera.getWidth(), titleHeight, vbom);
		titleRectangle = new Entity();
		
		pauseGame.attachChild(titleRectangle);
 	    
		//titleRectangle.setColor(0,0,0); //Black rectangle
		//titleRectangle.setAlpha(0.0f); //Transparent
        
		titleRectangle.attachChild(titleText);
		
		final ScaleMenuItemDecorator btnResume = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RESUME, resourcesManager.play_region,vbom), 1.2f*scaleButton, scaleButton);
		final ScaleMenuItemDecorator btnRestart = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RESTART, resourcesManager.restart_region,vbom), 1.2f*scaleButton, scaleButton);
		final ScaleMenuItemDecorator btnOptions = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.2f*scaleButton, scaleButton);
		final ScaleMenuItemDecorator btnCalibrate = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OFFSET, resourcesManager.calibrate_region,vbom), 1.2f*scaleButton, scaleButton);
		final ScaleMenuItemDecorator btnQuit = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_QUIT, resourcesManager.quit_region,vbom), 1.2f*scaleButton, scaleButton);

//		btnResume.setScale(scaleButton);
//		btnRestart.setScale(scaleButton);
//		btnOptions.setScale(scaleButton);
//		btnCalibrate.setScale(scaleButton);
//		btnQuit.setScale(scaleButton);
		
		buttonXLocation = (camera.getWidth()-btnResume.getWidth())/2;
		buttonHeight = btnResume.getHeight() * scaleButton;
		
		yOffset = titleHeight + 5;
		btnResume.setPosition(buttonXLocation, yOffset);
		btnRestart.setPosition(buttonXLocation, yOffset + 5 + buttonHeight);
		btnOptions.setPosition(buttonXLocation, yOffset + 2*(5 + buttonHeight));
		btnCalibrate.setPosition(buttonXLocation, yOffset + 3*(5 + buttonHeight));
		btnQuit.setPosition(buttonXLocation, yOffset + 4*(5 + buttonHeight));	

		//		btnResume.setScale(2);
		
		pauseGame.addMenuItem(btnResume);
		pauseGame.addMenuItem(btnRestart);
		pauseGame.addMenuItem(btnOptions);
		pauseGame.addMenuItem(btnCalibrate);
		pauseGame.addMenuItem(btnQuit);
		
		//pauseGame.buildAnimations();
		
		pauseGame.setBackgroundEnabled(false);
//		pauseGame.setOnMenuItemClickListener(this);
		pauseGame.setOnMenuItemClickListener(this);
		
		return pauseGame;
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		boolean handledClick = true; //Default to pass
		
		switch(arg1.getID()){
		case MENU_RESUME: //Resume
			this.resumeGame();
			break;
		case MENU_RESTART:
			if(this.hasChildScene()){
//				this.clearChildScene();
//				this.pauseRectangle.setVisible(true);
				
				Game.clear_board();
				this.resumeGame();
				
//         		//Resume the game
//         		Game.game_paused=false;
//	            Game.calculate_is_performed=false;
			}			
			break;
		case MENU_OPTIONS://Options
			activity.startSettingsActivity();
			break;
		case MENU_OFFSET:
			SceneManager.getInstance().loadOffsetScene();
			break;
		case MENU_QUIT:
			System.exit(0);
			break;
		default:
			handledClick = false;
		}
		
		if (handledClick)
		{
			ResourcesManager.getInstance().playPressButtonSound();
		}
		return handledClick;
	}

    public void pauseGame()
    {
    	Game.game_paused=true;
     	Game.calculate_is_performed=true;
    	unregisterUpdateHandler(updateHandler);
    }
    
	public void resumeGame()
	{
		if(this.hasChildScene()){
			this.clearChildScene();
//			this.pauseRectangle.setVisible(true);
//			
//     		//Resume the game
//     		Game.game_paused=false;
//            Game.calculate_is_performed=false;
		}	
		
		this.pauseRectangle.setVisible(true);
 		//Resume the game
 		Game.game_paused=false;
        Game.calculate_is_performed=false;
        
        //Make sure time is updated with user setting or auto speed change.
        updateHandler.setTimerSeconds(activity.SpeedWaitMills/1000.0f); // Make sure speed is updated
//        updateHandler.setTimerSeconds(10.0f);
        
//        Log.d("GameScene", String.format("Timer Seconds: %f SpeedWaitMills: %d", updateHandler.getTimerSeconds(), activity.SpeedWaitMills));
        registerUpdateHandler(updateHandler);
	}
	
    @Override
    public void onBackKeyPressed()
    {
    	if(Game.game_paused)
    	{
    		//Should already be on the paused scene, so quit the program
    		//activity.finish(); //doesn't always work
        	System.exit(0);
    	}
    	else
    	{
    		//Game is being played, so pause the game and load the menu pop-up
    		this.loadPauseMenuScene();
    	}
//    	//Pause the game
//    	Game.game_paused=true;
//    	Game.calculate_is_performed=true; 
//    	
//        //load the menu scene
//    	SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	camera.setHUD(null);
         
    	//Pause the game
//    	Game.game_paused=true;
//    	Game.calculate_is_performed=true; 
    	this.pauseGame();

        // TODO code responsible for disposing scene
        // removing all game scene objects.    
    }
	
    private void addToScore(int i)
    {
        Game.score += i;
        scoreText.setText("Score: " + Game.score);
    }
    
    public void reloadScene()
    {
    	camera.setHUD(gameHUD);
    }
    
    public void updateScene ()
    {
        if(Game.calculate_is_performed==false)       
        {	
//        	if(!Game.downShip())Game.chape_cant_down();
        	Game.move_down_Ship(false);
        	this.addToScore(0);
        	
            Game.paint_scene();
			//Game.last_down=System.currentTimeMillis();	
        }
    }
	// ===IMPLEMENTED INTERFACE===//
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//		Log.d(TAG, "onSceneTouchEvent - Beginning");
		//if(Game.calculate_is_performed==true)return super.onTouchEvent(pSceneTouchEvent);
		//Don't do anything during a game calculation
		if(Game.calculate_is_performed==true){
//			Log.d(TAG, "onSceneTouchEvent - calculate_is_performed==true");
			return true;
		}
//		if(pSceneTouchEvent.isActionMove() == false) {
//				activity.x=pSceneTouchEvent.getX(); activity.y=pSceneTouchEvent.getY();
//				Log.d(TAG, "onSceneTouchEvent - isActionMove==false");
//				return true;
//				//return super.onTouchEvent(pSceneTouchEvent);
//		}
//	   	if(System.currentTimeMillis()-activity.time_to_move_left_down_up<200)
//	   	{
//	   		Log.d(TAG, "onSceneTouchEvent - Wait 200mS");
//	   		return true;
//	   		//return super.onTouchEvent(pSceneTouchEvent);	
//	   	}
        
		/****************************************************************** 
		 * When the user presses down on the screen record the x,y position
		 * Then move once the user lifts off the screen.
		 *****************************************************************/
		if(pSceneTouchEvent.isActionDown()) {
			activity.x=pSceneTouchEvent.getX(); activity.y=pSceneTouchEvent.getY();
//			Log.d(TAG, "onSceneTouchEvent - isActionMove==false");
			return true;
			//return super.onTouchEvent(pSceneTouchEvent);
	    }
		else if(pSceneTouchEvent.isActionUp()) {
//	   	Log.d(TAG, "onSceneTouchEvent - Move block");
	   	
	   	//if(pSceneTouchEvent.getButtonState())
	   	activity.time_to_move_left_down_up=System.currentTimeMillis();
//		int mycase=0;
		
	   	activity.lastx=pSceneTouchEvent.getX();activity.lasty=pSceneTouchEvent.getY();
		float defx,defy;
		final float MIN_MOVE=1;
		defx=activity.lastx-activity.x;
		defx*=defx;
		defy=activity.lasty-activity.y;
		defy*=defy;
		//Weigh x movements more. Most movements will be left or right
		if( 1.1*defx>defy)
		{
			if(activity.lastx-activity.x > MIN_MOVE){
				Game.move_to_right_Ship();
			}
			else if(activity.lastx-activity.x < -1*MIN_MOVE){
				Game.move_to_left_Ship();
			}	
			
		}
		else
		{
			if(activity.lasty-activity.y > MIN_MOVE){
				Game.move_down_Ship(true);				
//				if(!Game.downShip()){Game.chape_cant_down();}
//				Game.last_down=System.currentTimeMillis();	
			}
			//Only rotate
			else if(activity.lasty-activity.y < -1*5){
				Game.rotate_ship();
			}
		}
	/*switch(pSceneTouchEvent.getAction())
	{
	
		
	case MotionEvent.EDGE_TOP :
		Game.rotate_ship();
		              break;	              
	case MotionEvent.EDGE_LEFT:
		Game.move_to_left_Ship();
		              break;
	case MotionEvent.EDGE_RIGHT:
		Game.move_to_right_Ship();
		              break;
	
	}*/
		
		activity.x=pSceneTouchEvent.getX(); activity.y=pSceneTouchEvent.getY();		
		Game.paint_scene();	
	    return true;
		}
		
//	return super.onTouchEvent(pSceneTouchEvent);
	return true;
	
	}
}
