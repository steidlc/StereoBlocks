package com.stereopsis.stereoblocks;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
//import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import com.stereopsis.stereoblocks.GameActivity;

import android.graphics.Color;



/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;
    

    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    //Splash Scene
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;
    //Menu Scene
//    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
//    public ITextureRegion resume_region;
    public ITextureRegion restart_region;
    public ITextureRegion options_region;
    public ITextureRegion calibrate_region;
    public ITextureRegion quit_region;
    
    public ITextureRegion reset_region;
    
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    private BuildableBitmapTextureAtlas offsetTextureAtlas;
    
    public Font font;
    public Font fontGame;
    public Font fontTitle;
    
    //Sound
    public boolean soundEnabled;
    public Sound pressButtonSound;
    public Sound moveSound;
    public Sound rowCompleteSound;
    public Sound shipDownSound;
    public Sound gameOverSound;
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }
    
    public void loadOffsetResources()
    {
        loadOffsetGraphics();

    }
    private void loadOffsetGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	offsetTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR); 
    	reset_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(offsetTextureAtlas, activity, "reset.png");

    	try 
    	{
    	    this.offsetTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.offsetTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    }
    
    private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
//    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
//    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "falling_blocks_background.png");
    	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
//    	resume_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "resume.png");
    	restart_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "restart.png");
    	options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "settings.png");
    	calibrate_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "offset.png");
    	quit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "exit.png");
        
    	
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
        
    }
    
    private void loadMenuAudio()
    {
        
    }
    
    private void loadMenuFonts()
    {
    	//Load the fonts for the loading... game scene
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

//        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.BLACK, 2, Color.WHITE);
        font.load();
    }

    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }
    
    private void loadGameGraphics()
    {
        
    }
    
    private void loadGameFonts()
    {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        final ITexture titleFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        
//        fontGame = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 24, true, Color.YELLOW, 2, Color.BLACK);
        fontGame = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 24, true, Color.WHITE, 2, Color.BLACK);
//        fontTitle = FontFactory.createFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50f, true, Color.WHITE);

        fontTitle = FontFactory.createStrokeFromAsset(activity.getFontManager(), titleFontTexture, activity.getAssets(), "font.ttf", 50f, true, Color.WHITE, 2, Color.BLACK);

        fontGame.load();
        fontTitle.load();
//    	font3 = new Font(this.getEngine().getFontManager(), fontTexture3, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.YELLOW);  
    }
    
    private void loadGameAudio()
    {
    	SoundFactory.setAssetBasePath("mfx/");
    	
    	try {
    		this.pressButtonSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), this.activity, "button-30-ShipDown.mp3");
    		this.pressButtonSound.setVolume(0.05f);
    	} catch (final IOException e) {
//    		e.printStackTrace();
    		Debug.e(e);
    	}
    	
    	try {
    		this.moveSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), this.activity, "button-21-MoveShip.mp3");
            this.moveSound.setVolume(0.02f);
    	} catch (final IOException e) {
//    		e.printStackTrace();
    		Debug.e(e);
    	}
    	
    	try {
    		this.rowCompleteSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), this.activity, "button-27-RowComplete.mp3");
    		this.rowCompleteSound.setVolume(0.05f);
    	} catch (final IOException e) {
//    		e.printStackTrace();
    		Debug.e(e);
    	}

    	try {
    		this.shipDownSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), this.activity, "button-30-ShipDown.mp3");
            this.shipDownSound.setVolume(0.05f);
    	} catch (final IOException e) {
//    		e.printStackTrace();
    		Debug.e(e);
    	}
    	
    	try {
    		this.gameOverSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), this.activity, "button-4-GameOver.mp3");
    		this.gameOverSound.setVolume(0.05f);
    	} catch (final IOException e) {
//    		e.printStackTrace();
    		Debug.e(e);
    	}
    	
    }
    
    private void playSound(Sound soundToPlay)
    {
    	if((this.soundEnabled) && (soundToPlay != null))
    	{
    		soundToPlay.play();
    	}
    }
    
    public void playPressButtonSound()
    {
    	playSound(this.pressButtonSound);
    }

    public void playMoveShipSound()
    {
    	playSound(this.moveSound);
    }

    public void playRowCompleteSound()
    {
    	playSound(this.rowCompleteSound);
    }

    public void playShipDownSound()
    {
    	playSound(this.shipDownSound);
    }

    public void playGameOverSound()
    {
    	playSound(this.gameOverSound);
    }
    
    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    }
    
    public void loadSplashScreen()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "StereopsisSplash.png", 0, 0);
//    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "StereoSoft.png", 0, 0);
    	
    	splashTextureAtlas.load();    
    }
    
    public void unloadSplashScreen()
    {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
    

    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}
