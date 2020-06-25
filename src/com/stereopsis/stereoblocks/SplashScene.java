package com.stereopsis.stereoblocks;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.stereopsis.stereoblocks.BaseScene;
import com.stereopsis.stereoblocks.SceneManager.SceneType;


/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class SplashScene extends BaseScene
{
    private Sprite splash;
    
    @Override
    public void createScene()
    {
    	splash = new Sprite(0, 0, resourcesManager.splash_region, vbom)
    	{
    	    @Override
    	    protected void preDraw(GLState pGLState, Camera pCamera) 
    	    {
    	       super.preDraw(pGLState, pCamera);
    	       pGLState.enableDither();
    	    }
    	};
    	
//    	int widthCenter = resourcesManager.activity.CAMERA_WIDTH/2;
//    	int heightCenter = resourcesManager.activity.CAMERA_WIDTH/2;
//    	int widthCenter = this.activity.CAMERA_WIDTH/2;
//    	int heightCenter = this.activity.CAMERA_WIDTH/2;    	
    	

    	splash.setScale(1.5f);
    	
    	//splash.setPosition(240, 400);
//    	splash.setPosition(240-128, 400-128);
    	//Put the splash graphic in the middle of the camera
    	splash.setPosition((resourcesManager.camera.getWidth()-(splash.getWidth()))/2.0f, (resourcesManager.camera.getHeight()-(splash.getHeight()))/2.0f);
    	
//    	splash.setPosition(widthCenter, heightCenter);
    	attachChild(splash);
    }

    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene()
    {
        splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }
}