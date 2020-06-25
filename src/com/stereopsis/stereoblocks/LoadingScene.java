package com.stereopsis.stereoblocks;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
//import org.andengine.util.adt.color.Color;
import org.andengine.util.color.Color;

import com.stereopsis.stereoblocks.BaseScene;
import com.stereopsis.stereoblocks.SceneManager.SceneType;


public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
        setBackground(new Background(Color.BLACK));
        attachChild(new Text(100, 400, resourcesManager.font, "Loading...", vbom));
    }

    @Override
    public void onBackKeyPressed()
    {
    	//ignor back key press
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }
}
