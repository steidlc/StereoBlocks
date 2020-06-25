package com.stereopsis.stereoblocks;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.util.color.Color;

import com.stereopsis.stereoblocks.BaseScene;
import com.stereopsis.stereoblocks.SceneManager.SceneType;

//import android.content.Intent;


/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener
{
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_CALIBRATE = 2;
	private final int MENU_QUIT = 3;
	
	GameActivity activity;
    
    @Override
    public void createScene()
    {
    	activity = GameActivity.getSharedInstance();
    	
        createBackground();
        createMenuChildScene();
    }

    @Override
    public void onBackKeyPressed()
    {
    	//activity.finish() doesn't always work
    	System.exit(0);
    	
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_MENU;
    }

    @Override
    public void disposeScene()
    {

        this.detachSelf();
        this.dispose();
    }
    
    private void createBackground()
    {
    	setBackground(new Background(Color.BLACK));
//        attachChild(new Sprite(0, 0, resourcesManager.menu_background_region, vbom)
//                {
//            @Override
//            protected void preDraw(GLState pGLState, Camera pCamera) 
//            {
//                super.preDraw(pGLState, pCamera);
//                pGLState.enableDither();
//            }
//        });
    }
    
    private void createMenuChildScene()
    {
        menuChildScene = new MenuScene(camera);
//        menuChildScene.setPosition(240-128, 400-128);
        
        menuChildScene.setPosition(0, 0);
        
        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.2f, 1);
        final IMenuItem calibrateMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_CALIBRATE, resourcesManager.calibrate_region, vbom), 1.2f, 1);
        final IMenuItem quitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_QUIT, resourcesManager.quit_region, vbom), 1.2f, 1);
        
        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(optionsMenuItem);
        menuChildScene.addMenuItem(calibrateMenuItem);
        menuChildScene.addMenuItem(quitMenuItem);
        
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);
        
//        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + 10);
//        optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 110);

//      playMenuItem.setPosition(playMenuItem.getX()-100, playMenuItem.getY());
//      optionsMenuItem.setPosition(optionsMenuItem.getX()-100, optionsMenuItem.getY());
        menuChildScene.setOnMenuItemClickListener(this);
        
        setChildScene(menuChildScene);
    }
    
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
    {
            switch(pMenuItem.getID())
            {
            case MENU_PLAY:
            	//Load Game Scene!
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_OPTIONS:
            	activity.startSettingsActivity();
//            	Intent i = new Intent(activity, UserSettingActivity.class);
//            	activity.startActivities(i);
//            	activity.userSettingsUpdate();

                return true;
            case MENU_CALIBRATE:
            	//TODO: launch the calibrate position screen
            	
            	return true;
            case MENU_QUIT:
            	System.exit(0);
            	
            default:
                return false;
        }
    }

}