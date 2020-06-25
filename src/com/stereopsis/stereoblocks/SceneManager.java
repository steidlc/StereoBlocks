package com.stereopsis.stereoblocks;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.stereopsis.stereoblocks.BaseScene;


/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class SceneManager
{
    //---------------------------------------------
    // SCENES
    //---------------------------------------------
    
    private BaseScene splashScene;
    private BaseScene menuScene;
    private GameScene gameScene;
    private BaseScene loadingScene;
    private OffsetScene OffsetScene;
    
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
//    private SceneType previousSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene currentScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    
    private boolean firstTime = true;
    
    GameManager Game;
    
    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_GAME,
        SCENE_LOADING,
        SCENE_OFFSET
    }
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void setScene(BaseScene scene)
    {
//    	previousSceneType = currentSceneType;
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            case SCENE_OFFSET:
            	setScene(OffsetScene);
            default:
                break;
        }
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }
    
    public void createMenuScene()
    {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
    }
    
    public void loadMenuScene(final Engine mEngine)
    {
    	//Load Menu scene when currently on game scene
        setScene(loadingScene);
        gameScene.disposeScene();
        ResourcesManager.getInstance().unloadGameTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuTextures();
                setScene(menuScene);
            }
        }));
    }
    
    public void loadGameScene(final Engine mEngine)
    {
    	
    	if(currentSceneType == SceneType.SCENE_OFFSET) //Calibrate Scene
        {
        	//setScene(loadingScene);
        	setScene(gameScene);
        	disposeOffsetScene();
        	gameScene.reloadScene();
 //       	ResourcesManager.getInstance().camera.setHUD(gameScene.gameHUD);
        }
    	else
        {
    		if (firstTime)
            {
    			ResourcesManager.getInstance().loadMenuResources();
    		    loadingScene = new LoadingScene();
            }
        	setScene(loadingScene);
            //ResourcesManager.getInstance().unloadMenuTextures();
        
	        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
	        {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                
	                if (firstTime)
	                {
		                ResourcesManager.getInstance().loadGameResources();
		    	    	ResourcesManager.getInstance().loadOffsetResources();
		    	    	
		    	    	//Get errors unless Game Manager is initialized inside of GameScene.OnCreate()
//		    		    Game = new GameManager();
	                    gameScene = new GameScene();
//	                    setScene(gameScene);
	                    
	                    //Need to initialized OffsetScene after gameScene so that the 
	                    //Game Manager object is initialized
	                    gameScene.dispose(); // Run the same things as loading the Offset Scene
	                    
		    	    	OffsetScene = new OffsetScene();
//		    	    	setScene(OffsetScene);
		    	    	
		    	    	//Need to set the HUD back to the game scene
		    	        OffsetScene.disposeScene();
		    	    	gameScene.reloadScene();

		    	    	firstTime = false;
	                    setScene(gameScene);
	                    Game = GameManager.getSharedInstance();
	                    Game.paint_scene();
	                }
	                else
	                {
	                	setScene(gameScene);
	                	//TODO: Game might pause automatically when back is pressed and MenuScene is loaded
	                	Game.game_started=true;
	                	
	                	gameScene.reloadScene();
	            		Game.game_paused=false;
	            	    Game.calculate_is_performed=false;
	                }
	            }
	        }));
        }

    }
    
    public void loadOffsetScene()
    {
    	gameScene.disposeScene();
//    	ResourcesManager.getInstance().loadOffsetResources();
//    	OffsetScene = new OffsetScene();
    	OffsetScene.reloadScene();
 //   	ResourcesManager.getInstance().camera.setHUD(OffsetScene.sceneHUD);
    	setScene(OffsetScene);
    	
    }
    
    public void disposeOffsetScene()
    {
        //ResourcesManager.getInstance().unloadSplashScreen();
        OffsetScene.disposeScene();
        //OffsetScene = null;
    }
}