package com.stereopsis.stereoblocks;

//import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

//import com.stereopsis.stereoblocks.SceneManager.SceneType;

public class GameLoopUpdateHandler extends TimerHandler
{
	public GameLoopUpdateHandler(float pTimerSeconds, boolean pAutoReset,
			ITimerCallback pTimerCallback) {
		super(pTimerSeconds, pAutoReset, pTimerCallback);
		// TODO Auto-generated constructor stub
	}

}


//public class GameLoopUpdateHandler implements IUpdateHandler {
//    float pSecondsTotal = 0;
////    private GameManager Game = GameManager.getSharedInstance();;
//	private GameActivity activity = GameActivity.getSharedInstance();
//	private SceneManager sceneManager = SceneManager.getInstance();
//	private GameScene gameScene = GameScene.getInstance();
//	
//	@Override
//	public void onUpdate(float pSecondsElapsed) {
////		GameManager Game;
////		GameActivity activity;
////		SceneManager sceneManager;
////		GameScene gameScene;
////		Game = GameManager.getSharedInstance();		
//		activity = GameActivity.getSharedInstance();
//		sceneManager = SceneManager.getInstance();
//		
//		if (sceneManager.getCurrentSceneType() == SceneType.SCENE_GAME)
//		{	
//			
////		    Game.paint_scene();
//			pSecondsTotal = pSecondsTotal + pSecondsElapsed;
//			if (pSecondsTotal > activity.SpeedWaitMills/1000.0)
//			{		
//				pSecondsTotal = 0;
//				
//				gameScene = (GameScene) sceneManager.getCurrentScene();
//				gameScene.updateScene();
//			}
//		}
//	}
//
//	@Override
//	public void reset() {
//		// TODO Auto-generated method stub
//
//	}
//
//}
