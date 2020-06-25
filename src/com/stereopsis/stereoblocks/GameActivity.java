package com.stereopsis.stereoblocks;


import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;

import org.andengine.ui.activity.BaseGameActivity;

import com.stereopsis.stereoblocks.GameManager;
import com.stereopsis.stereoblocks.GameManager.GameBorderType;
import com.stereopsis.stereoblocks.GameManager.MixColorType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.preference.Preference;
import android.preference.PreferenceManager;

import android.view.KeyEvent;
import android.widget.Toast;


public class GameActivity extends BaseGameActivity {
//public class GameActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

//	private static final int CAMERA_WIDTH = 640;
//	private static final int CAMERA_HEIGHT = 480;
	
//	private static final int CAMERA_WIDTH = 800;
//	private static final int CAMERA_HEIGHT = 480;
	
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 800;
	
	// Put buttons on the far right
//	private static final int BUTTON_LOC_X = CAMERA_WIDTH - 120;
//	private static final int SCORE_LOC_X = 470;
//	private static final int NEXT_BLOCK_LOC_X = 400;
	
	public static boolean switch_color=true;
	public GameManager Game;
	
	private static final int SPEED_WAIT_MIN = 10;
	private static final int SPEED_WAIT_MAX = 1500;
	
	public int SpeedWaitStart = 750;
	public int SpeedWaitMills = SpeedWaitStart;
	public int SpeedWaitDecrease = 0; //SpeedUp of the game.
	public boolean SpeedWaitAuto = false;
	
//    private static final String GAMETEL_NAME =    "gametel";
//    private static final String GAMETEL_PACKAGE = "com.fructel.gametel";

    private BroadcastReceiver mReceiver;
	
//	private HUD gameHUD;
//	private Text scoreText;
	
//	public  Text monScore;
//	public  Text myline;
//	public  Text mybestLine;
//	public  Text monBestScore;
//	public  Text monTexteoff;
	// ===========================================================
//	private TextureManager textManager;
//	private BitmapTextureAtlas btnTexture;
//	private TextureRegion btn_playRegion;
//	private Sprite pauseBtn;
//	
//	private BitmapTextureAtlas saveTexture;
//	private TextureRegion btn_saveRegion;
//	private Sprite saveBtn;
//
//	private BitmapTextureAtlas settingTexture;
//	private TextureRegion btn_settingRegion;
//	private Sprite settingBtn;
//	
//	private BitmapTextureAtlas loadTexture;
//	private TextureRegion btn_loadRegion;
//	private Sprite loadBtn;
//	
//	
//	private BitmapTextureAtlas exitTexture;
//	private TextureRegion btn_exitRegion;
//	private Sprite exitBtn;
//	
//	
//	private BitmapTextureAtlas classicTexture;
//	private TextureRegion btn_classicRegion;
//	private Sprite classicBtn;
	//***********************************************************
	public int best_score;
	public int best_line;
	
	public float lastx,lasty,x,y;
	public long time_to_move_left_down_up;
	boolean pressed;
	
	private static final int RESULT_SETTINGS = 1;
	private static final int FRAMES_PER_SECOND_LIMIT = 30;
	
	public static GameActivity instance;
	public Scene mCurrentScene;
//	private ResourcesManager resourcesManager;
	// ===========================================================
	
	///=============================================================

//	private org.andev.andengine.engine.camera.Camera mCamera;
//	public org.andengine.engine.camera.Camera mCamera;
    public BoundCamera mCamera;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	//===IMPLEMENTED INTERFACE===//
    
    //Override these functions to try and eliminate the problem with the program
    //not starting started in landscape orientation mode
    //Also set the orientation to portrait for the activity in the manifest
    @Override
    public void onResumeGame() {  	
        /* Register a listener to detect when Gametel devices connects/disconnects */
        IntentFilter filter = new IntentFilter();
        /* For devices in RFCOMM mode (which uses the InputMethod) */
        filter.addAction(Intent.ACTION_INPUT_METHOD_CHANGED); 
        /* For devices in HID mode */
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED); 
        registerReceiver(mReceiver, filter);
        
        /* Check if there are any Gametel devices connected */
//        updateGametelStatus();
        
        super.onResumeGame();

 //       this.enableAccelerationSensor(this);
    }

    @Override
    public void onPauseGame() {
    	unregisterReceiver(mReceiver);
        super.onPauseGame();

//        this.disableAccelerometerSensor();
    }
    
    /* Function to check the current Gametel status.
     * The status field will say 'connected' if either:
     *     - A Gametel device in HID Gamepad mode is connected
     *     - The current InputMethod is set to 'Gametel'
     */
//    private void updateGametelStatus()
//    {
//        boolean gametelAvailable = false;
//
//        /* Check if there are any Gametels connected as HID gamepad */
//        if (isHIDGametelConnected())
//            gametelAvailable = true;
//        
//        /* Check if the Gametel InputMethod is active */
//        if (isGametelIMEActive())
//            gametelAvailable = true;
//        
//        Log.d("GameActivity.java", "gametelAvailable: " + gametelAvailable);

       // mStatus.setText("Gametel " + (gametelAvailable ? "connected" : "not connected"));
//    }
    
//    /* Function that checks if the Gametel InputMethod is currently active */
//    private boolean isGametelIMEActive() {
//        String activeIme = Settings.Secure.getString(getContentResolver(), 
//                                                     Settings.Secure.DEFAULT_INPUT_METHOD);
//        return activeIme.startsWith(GAMETEL_PACKAGE);
//    }    
//
//    /* Function that checks if there are any Gametels in HID gamepad mode currently connected */
//    private boolean isHIDGametelConnected() {
//        
//        int ids[] = InputDevice.getDeviceIds();
//        
//        for (int i = 0; i < ids.length; i++) {
//            InputDevice dev = InputDevice.getDevice(ids[i]);
//            if (dev.getName().toLowerCase().contains(GAMETEL_NAME))
//                return true;
//        }
//        
//        return false;
//    }
    
//    @Override 
//    public void onCreate(final Bundle savedInstanceState)
//    {
//    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    	super.onCreate(savedInstanceState);
//    }
    
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) 
    {
        return new LimitedFPSEngine(pEngineOptions, FRAMES_PER_SECOND_LIMIT);
    }
    
	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;
        /* This receiver is used to detect Gametel connect/disconnect */
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	//Pass;
//                updateGametelStatus();
            }
        };
//		mCamera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
//		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
//				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
//		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
//		return engineOptions;
		
		mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		//mCamera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.mCamera);		
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		//TODO get error for getConfigChooserOptions
//		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

		return engineOptions;
	}
	
	public static GameActivity getSharedInstance() {
		return instance;
	}
	
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.settings, menu);
//        return true;
//    }
// 
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//        
//        case R.id.menu_play:
//			if(Game.game_started==false)
//			{
//				Game.game_started=true;
//				Game.game_paused=false;
//			 Game.generate_new_ship();
//			 Game.calculate_is_performed=false;
//			}
//			
//			else {
//				
//			      if(Game.game_paused==false)
//			      {
//			    	Game.game_paused=true;Game.calculate_is_performed=true;  
//			      }
//			      else {
//			    	Game.game_paused=false;Game.calculate_is_performed=false;
//			      }
//			
//			}
//			break; 
//			
//        case R.id.menu_settings:
//            Intent i = new Intent(this, UserSettingActivity.class);
//            startActivityForResult(i, RESULT_SETTINGS);
//            break;
//        
//        case R.id.menu_quit:
//   			Game.calculate_is_performed=true;
//   			try {
//   				finish();
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}        	
//        }
// 
//        return true;
//    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SETTINGS:
            userSettingsUpdate();
            break;
        }
    }

    public void startSettingsActivity()
    {
    	Intent i = new Intent(this, UserSettingActivity.class);
        startActivityForResult(i, RESULT_SETTINGS);
    }
    
    public void initGame()
    {
    	Game = GameManager.getSharedInstance();
    }
    
    public void increasGameSpeed(float speedPercentage){
    	int speedDecrease = (int) ((SPEED_WAIT_MAX-SPEED_WAIT_MIN)*(speedPercentage));
    	if((SpeedWaitStart-SpeedWaitDecrease-speedDecrease) < SPEED_WAIT_MIN)
    	{ 
    		//Set SpeedWaitDecrease to highest possible value
    		//So that Speed WaitMills will be set to minimum value
    		SpeedWaitDecrease = SpeedWaitStart - SPEED_WAIT_MIN;   		
    	}
    	else
    	{
    		SpeedWaitDecrease = SpeedWaitDecrease + speedDecrease; 
    	}
    	this.setGameSpeedCurrent();
    }
    
    public void setGameSpeedCurrent(){
    	SpeedWaitMills = SpeedWaitStart - SpeedWaitDecrease;
    }
    
    public void userSettingsUpdate() {
    	float speedPercentage;
    	
    	Map<String, Integer> colorRGBVals = new HashMap<String, Integer>();
    	
        String movingColor;
        float movingSaturation;
        
        String staticColor;
        float staticSaturation;
        
//        float XMin;
//        float XMax;
//        float YMin;
//        float YMax;
        
//        UserSettingActivity settingActivity = new UserSettingActivity();     	
//        settingActivity.userSettingsUpdate();
        
        //Game = GameManager.getSharedInstance();
        
        if (Game != null)
        {
        	String ColorMixType;
        	String gameBorderMode;
        	
        	//When Menu Scene is first loaded, the game manager isn't yet loaded
	        SharedPreferences sharedPrefs = PreferenceManager
	                .getDefaultSharedPreferences(this);
//	        Log.d("GameActivity.java", "UserName: " + sharedPrefs.getString("prefUsername", "NULL"));
	        
	        Game.extrat_mode = sharedPrefs.getBoolean("prefExtraPiece", false);
	        
	        ResourcesManager.getInstance().soundEnabled = sharedPrefs.getBoolean("prefSoundEnabled", true);        
	        
	        Game.previousMixColorType = Game.currentMixColorType;
	        ColorMixType = sharedPrefs.getString("prefColorMix", "Static Only");
	        
	        if (ColorMixType.equals("Static Only")) Game.currentMixColorType = MixColorType.MIXCOLOR_STATIC_ONLY;
	        else if (ColorMixType.equals("Static and Moving")) Game.currentMixColorType = MixColorType.MIXCOLOR_STATIC_MOVING;
	        else if (ColorMixType.equals("Moving Only")) Game.currentMixColorType = MixColorType.MIXCOLOR_MOVING_ONLY;
	        else if(ColorMixType.equals("No Mixing")) Game.currentMixColorType = MixColorType.MIXCOLOR_NONE;
	        else Game.currentMixColorType = MixColorType.MIXCOLOR_NONE;
	        
	        Game.previousGameBorderType = Game.currentGameBorderType;
	        gameBorderMode = sharedPrefs.getString("prefGameBorderMode", "Dominant Only");
	        
	        if (gameBorderMode.equals("Dominant Only")) Game.currentGameBorderType = GameBorderType.GAME_BORDER_DOMINANT;
	        else if (gameBorderMode.equals("Both")) Game.currentGameBorderType = GameBorderType.GAME_BORDER_BOTH;
	        else if (gameBorderMode.equals("Alternating")) Game.currentGameBorderType = GameBorderType.GAME_BORDER_ALTERNATING;
	        else if(gameBorderMode.equals("Mixed")) Game.currentGameBorderType = GameBorderType.GAME_BORDER_MIXED;
	        else Game.currentGameBorderType = GameBorderType.GAME_BORDER_DOMINANT;
	        
	        //0 = Slowest, 100 = Fastest
	        speedPercentage = Float.parseFloat(sharedPrefs.getString("prefMovingSpeed", "50"))/100;
	        SpeedWaitStart = (int) ((SPEED_WAIT_MAX-SPEED_WAIT_MIN)*(1-speedPercentage)) + SPEED_WAIT_MIN;	  
	        
	        SpeedWaitAuto = sharedPrefs.getBoolean("prefSpeedAuto", false);
	        if(!SpeedWaitAuto)
	        {
	        	SpeedWaitDecrease = 0; //Back to default of 0
	        }
	        
	        this.setGameSpeedCurrent();
	        //SpeedWaitMills = SpeedWaitStart - SpeedWaitDecrease;
	            
	        movingColor = sharedPrefs.getString("prefMovingColor", "Red");
	        movingSaturation = Float.parseFloat(sharedPrefs.getString("prefMovingSaturation", "50"))/100;
	        colorRGBVals = getRGBOneColorInt(movingColor, movingSaturation);
	    	//Color of the falling blocks
	    	Game.falling_color_r=colorRGBVals.get("Red");
	    	Game.falling_color_g=colorRGBVals.get("Green");
	    	Game.falling_color_b=colorRGBVals.get("Blue");
	    	
//	    	Game.fallingOffsetX = Float.parseFloat(sharedPrefs.getString("prefMovingXOffset", "0"));
//	    	Game.fallingOffsetY = Float.parseFloat(sharedPrefs.getString("prefMovingYOffset", "0"));

	    	Game.fallingOffsetX = this.offsetErrorCheck(sharedPrefs, "prefMovingXOffset", "Lazy Eye", "Horizontal");
	    	Game.fallingOffsetY = this.offsetErrorCheck(sharedPrefs, "prefMovingYOffset", "Lazy Eye", "Vertical");
	    	
//	        //TODO Need to add error checking for non float value
//	        boolean ErrorOffsetX = false;
//	        String ErrorM = "";
//	        try {
//	            Game.fallingOffsetX = Float.parseFloat(sharedPrefs.getString("prefMovingXOffset", "0"));
//	            
//	        } catch (NumberFormatException e) {
//	            // Handle The Exception During  Parsing
//	        	Game.fallingOffsetX = 0.0f;
//	        	ErrorOffsetX = true;
//	        	ErrorM = "Invalid numeric value for lazy eye X Offset. Offset will be set to 0.0";
//	        }
//	        
//	        if (!ErrorOffsetX)
//	        {
//	        	//Make sure value is within size of the screen
//		        XMin = -1.0f*this.mCamera.getWidth()/2.0f;
//		        XMax = this.mCamera.getWidth()/2.0f;
//		        
//	            if(Game.fallingOffsetX < XMin)
//	            {
//	            	Game.fallingOffsetX = XMin;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Lazy eye X Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.fallingOffsetX);
//	            }
//	            else if(Game.fallingOffsetX > XMax)
//	            {
//	            	Game.fallingOffsetX = XMax;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Lazy eye X Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.fallingOffsetX);
//	            }
//	        }
//            
//            if(ErrorOffsetX)
//            {
//	        	Editor editor = sharedPrefs.edit();
//	        	editor.putString("prefMovingXOffset", String.valueOf(Game.fallingOffsetX));
//	        	editor.commit();
//	        	errorMessage(ErrorM, Toast.LENGTH_LONG);
//            }
//	        
//            //Set Y Offset
//            boolean ErrorOffsetY = false;
//	        ErrorM = "";
//	        
//	        try {
//	        	Game.fallingOffsetY = Float.parseFloat(sharedPrefs.getString("prefMovingYOffset", "0"));
//
//	        } catch (NumberFormatException e) {
//	        	// Handle The Exception During  Parsing
//	        	Game.fallingOffsetY = 0.0f;
//	        	ErrorOffsetY = true;
//	        	ErrorM = "Invalid numeric value for lazy eye Y Offset. Offset will be set to 0.0";
//	        }
//	        
//	        if (!ErrorOffsetY)
//	        {
//	        	//Make sure value is within size of the screen
//		        YMin = -1.0f*this.mCamera.getHeight()/2.0f;
//		        YMax = this.mCamera.getHeight()/2.0f;
//		        
//	            if(Game.fallingOffsetY < YMin)
//	            {
//	            	Game.fallingOffsetY = YMin;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Lazy eye Y Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.fallingOffsetY);
//	            }
//	            else if(Game.fallingOffsetY > YMax)
//	            {
//	            	Game.fallingOffsetY = YMax;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Lazy eye Y Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.fallingOffsetY);
//	            }
//	        }
//	        
//	        if(ErrorOffsetY)
//            {
//	        	Editor editor = sharedPrefs.edit();
//	        	editor.putString("prefMovingYOffset", String.valueOf(Game.fallingOffsetY));
//	        	editor.commit();
//	        	errorMessage(ErrorM, Toast.LENGTH_LONG);
//            }
	
	        staticColor = sharedPrefs.getString("prefStaticColor", "Blue");
	        staticSaturation = Float.parseFloat(sharedPrefs.getString("prefStaticSaturation", "50"))/100;
	        colorRGBVals = getRGBOneColorInt(staticColor, staticSaturation);
	        //Color of the blocks that are set at the bottom of the puzzle
	    	Game.set_block_color_r=colorRGBVals.get("Red");
	    	Game.set_block_color_g=colorRGBVals.get("Green");
	    	Game.set_block_color_b=colorRGBVals.get("Blue");     
	    	
//	    	Game.staticOffsetX = Float.parseFloat(sharedPrefs.getString("prefStaticXOffset", "0"));
//	    	Game.staticOffsetY = Float.parseFloat(sharedPrefs.getString("prefStaticYOffset", "0"));

	    	Game.staticOffsetX = this.offsetErrorCheck(sharedPrefs, "prefStaticXOffset", "Dominant Eye", "Horizontal");
	    	Game.staticOffsetY = this.offsetErrorCheck(sharedPrefs, "prefStaticYOffset", "Dominant Eye", "Vertical");

//	        ErrorOffsetX = false;
//	        ErrorM = "";
//	        try {
//	            Game.staticOffsetX = Float.parseFloat(sharedPrefs.getString("prefStaticXOffset", "0"));
//	            
//	        } catch (NumberFormatException e) {
//	            // Handle The Exception During  Parsing
//	        	Game.staticOffsetX = 0.0f;
//	        	ErrorOffsetX = true;
//	        	ErrorM = "Invalid numeric value for dominant eye X Offset. Offset will be set to 0.0";
//	        }
//	        
//	        if (!ErrorOffsetX)
//	        {
//	        	//Make sure value is within size of the screen
//		        XMin = -1.0f*this.mCamera.getWidth()/2.0f;
//		        XMax = this.mCamera.getWidth()/2.0f;
//		        
//	            if(Game.staticOffsetX < XMin)
//	            {
//	            	Game.staticOffsetX = XMin;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Dominant eye X Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.staticOffsetX);
//	            }
//	            else if(Game.staticOffsetX > XMax)
//	            {
//	            	Game.staticOffsetX = XMax;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Dominant eye X Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.staticOffsetX);
//	            }
//	        }
//            
//            if(ErrorOffsetX)
//            {
//	        	Editor editor = sharedPrefs.edit();
//	        	editor.putString("prefStaticXOffset", String.valueOf(Game.staticOffsetX));
//	        	editor.commit();
//	        	errorMessage(ErrorM, Toast.LENGTH_LONG);
//            }
//	        
//            //Set Y Offset
//            ErrorOffsetY = false;
//	        ErrorM = "";
//	        
//	        try {
//	        	Game.staticOffsetY = Float.parseFloat(sharedPrefs.getString("prefStaticYOffset", "0"));
//
//	        } catch (NumberFormatException e) {
//	        	// Handle The Exception During  Parsing
//	        	Game.staticOffsetY = 0.0f;
//	        	ErrorOffsetY = true;
//	        	ErrorM = "Invalid numeric value for dominant Y Offset. Offset will be set to 0.0";
//	        }
//	        
//	        if (!ErrorOffsetY)
//	        {
//	        	//Make sure value is within size of the screen
//		        YMin = -1.0f*this.mCamera.getHeight()/2.0f;
//		        YMax = this.mCamera.getHeight()/2.0f;
//		        
//	            if(Game.staticOffsetY < YMin)
//	            {
//	            	Game.staticOffsetY = YMin;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Dominant eye Y Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.staticOffsetY);
//	            }
//	            else if(Game.staticOffsetY > YMax)
//	            {
//	            	Game.staticOffsetY = YMax;
//	            	ErrorOffsetX = true;
//	            	ErrorM = "Dominant eye Y Offset outside the bounds of the screen. Offset will be set to " + String.valueOf(Game.staticOffsetY);
//	            }
//	        }
//	        
//	        if(ErrorOffsetY)
//            {
//	        	Editor editor = sharedPrefs.edit();
//	        	editor.putString("prefStaticYOffset", String.valueOf(Game.staticOffsetY));
//	        	editor.commit();
//	        	errorMessage(ErrorM, Toast.LENGTH_LONG);
//            }	   
	    	
	    	Game.set_color_blocks();
	    	Game.paint_scene();
        }
        
        
// Apply settings to global variables
        
//        StringBuilder builder = new StringBuilder();
// 
//        builder.append("\n Username: "
//                + sharedPrefs.getString("prefUsername", "NULL"));
// 
//        builder.append("\n Send report:"
//                + sharedPrefs.getBoolean("prefSendReport", false));
// 
//        builder.append("\n Sync Frequency: "
//                + sharedPrefs.getString("prefSyncFrequency", "NULL"));
// 
//        TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);
// 
//        settingsTextView.setText(builder.toString());
    }

    private float offsetErrorCheck(SharedPreferences sharedPrefs, String key, String eyeTypeM, String offsetType)
    {
        String ErrorM = "";
        String OffsetStr;
        float offset;
    	
//    	float offset = Float.parseFloat(sharedPrefs.getString(key, "0"));
    	OffsetStr = sharedPrefs.getString(key, "0");
//    	OffsetStr = "#abjkl"; // force error
    	OffsetStr = OffsetStr.replace(",", "."); // Replace comma with decimal point to try and prevent convert to float error
        
        try {
            offset = Float.parseFloat(OffsetStr);        
        } catch (NumberFormatException e) {
            // Handle The Exception During  Parsing
    	    offset = 0.0f;
        	ErrorM = eyeTypeM + " " + offsetType + " offset is an invalid numeric number. Offset will be set to " + String.valueOf(offset);    	    
        }   
        

        //Toast is not working
        if(ErrorM.length() > 0)
        { //There was an error
        	     	
    	    Editor editor = sharedPrefs.edit();
    	    editor.putString(key, String.valueOf(offset));
    	    editor.commit();
//        	this.errorMessage(ErrorM, Toast.LENGTH_LONG);
        }
        
        return offset;
    }

    public void errorMessage(CharSequence text, int duration)
    {
    	Context context = getApplicationContext();
    	//CharSequence text = "Hello toast!";
    	//int duration = Toast.LENGTH_SHORT;
    	//Toast toast = Toast.makeText(context, text, duration);
    	//toast.show();
    	
    	Toast.makeText(context, text, duration).show();
    }
    public Map<String, Integer> getRGBOneColorInt(String color, float saturationLevel){
    	//Map colorRGBVals = new Map();
    	Map<String, Integer> colorRGBVals = new HashMap<String, Integer>();
    	int satLevel = (int) (255 * saturationLevel);
    	colorRGBVals.put("Red", 0);
    	colorRGBVals.put("Green", 0);
    	colorRGBVals.put("Blue", 0);
    	if(color.equals("Red")) colorRGBVals.put("Red", satLevel);
    	else if(color.equals("Green")) colorRGBVals.put("Green", satLevel);
    	else if(color.equals("Blue")) colorRGBVals.put("Blue", satLevel);
    	
    	return colorRGBVals;
    }
	@Override
//	public void onLoadResources() {
//	public void onCreateResources(){
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
		ResourcesManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());
//	    resourcesManager = ResourcesManager.getInstance();
	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	    
//		textManager = getEngine().getTextureManager();	
		
		//**************save btn

////		saveTexture= new BitmapTextureAtlas(textManager, 128,64,TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
////	   btn_saveRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(saveTexture, this, "gfx/save.png",0,0);
////	      getEngine().getTextureManager().loadTexture(saveTexture);
//		
//	      btnTexture= new BitmapTextureAtlas(textManager, 128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
//	      btn_playRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnTexture, this, "gfx/start.png",0 ,0);
////			btn_playRegion.setTextureSize(64,32);
//			getEngine().getTextureManager().loadTexture(btnTexture);
//		
//		//**************setting btn
//		settingTexture= new BitmapTextureAtlas(textManager, 128,64,TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
//		btn_settingRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(settingTexture, this, "gfx/settings.png",0,0);
//		getEngine().getTextureManager().loadTexture(settingTexture);
				
			
//			  exitTexture= new BitmapTextureAtlas(textManager, 128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//				btn_exitRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(exitTexture, this, "gfx/exit.png",0 ,0);
//				getEngine().getTextureManager().loadTexture(exitTexture);
//				
//				 loadTexture= new BitmapTextureAtlas(textManager, 128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//					btn_loadRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(loadTexture, this, "gfx/load.png",0 ,0);
//					getEngine().getTextureManager().loadTexture(loadTexture);
//					
//					classicTexture= new BitmapTextureAtlas(textManager, 128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//					btn_classicRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(classicTexture, this, "gfx/classic.png",0 ,0);
//					getEngine().getTextureManager().loadTexture(classicTexture);
//					onLoadComplete();
	}


	@Override
//	public Scene onLoadScene() {
//	protected Scene onCreateScene() {
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
        //Log the Frames Per Second
		//this.mEngine.registerUpdateHandler(new FPSLogger());
		
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		
		//TODO: Need to move this somewhere else like the GameScene.
		// Probably should move GameManager to game scene.
		
       	time_to_move_left_down_up=System.currentTimeMillis();	
	}
	
//	protected void OldonCreateScene() {
//		this.mEngine.registerUpdateHandler(new FPSLogger());
//
//		Game = new GameManager();
//		
//		//@SuppressWarnings("deprecation")
//		//final Scene scene = new Scene(1);
//		mCurrentScene = new GameScene();
//		
//		Game.setScene(mCurrentScene);
//		userSettingsUpdate();
//		
//		if(switch_color==true){switch_color=false;
//		//mCurrentScene.setBackground(new ColorBackground(0, 0, 0));
//		mCurrentScene.setBackground(new Background(0, 0, 0));
//							}
//		else {switch_color=true;
//		//mCurrentScene.setBackground(new ColorBackground(0, 0, 0));
//		mCurrentScene.setBackground(new Background(0, 0, 0));
//		} 
//		
//		//************load mes image
////		final BitmapTextureAtlas textureImage = new BitmapTextureAtlas(512, 128,TextureOptions.BILINEAR);
////		regionImage = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureImage, this, "gfx/me.png", 0, 0);	
////			
////		this.getEngine().getTextureManager().loadTexture(textureImage);
////		final Sprite sprite = new Sprite(300,0, regionImage);
////		mCurrentScene.attachChild(sprite);
//		
//		
//		//********************botton click
//		
//		pauseBtn= new Sprite(300, 300, this.btn_playRegion, mEngine.getVertexBufferObjectManager()){
//			@Override
//			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
//	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
//	   				// this.setVisible(false);
//	   				if(Game.game_started==false)
//	   				{
//	   					Game.game_started=true;
//	   					Game.game_paused=false;
//	   				 Game.generate_new_ship();
//	   				 Game.calculate_is_performed=false;
//	   				}
//	   				
//	   				else {
//	   					
//	   				      if(Game.game_paused==false)
//	   				      {
//	   				    	Game.game_paused=true;Game.calculate_is_performed=true;  
//	   				      }
//	   				      else {
//	   				    	Game.game_paused=false;Game.calculate_is_performed=false;
//	   				      }
//	   				
//	   				}
//	   				
//	   				 
//	   			 }
//	   			
//	   			 return true;
//       	}};
//       	
//       	//pauseBtn.setScale(2);
//       	mCurrentScene.attachChild(pauseBtn);
//       	mCurrentScene.registerTouchArea(pauseBtn);
//
//   	//*********************Settings btn
//   	settingBtn= new Sprite(300, 356, this.btn_settingRegion, mEngine.getVertexBufferObjectManager()){
//		@Override
//		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
//   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){   
//   				 
//   	            Intent i = new Intent(instance, UserSettingActivity.class);
//   	            startActivityForResult(i, RESULT_SETTINGS);  				 
//   			 }
//			 return true;
//	}};
//	mCurrentScene.attachChild(settingBtn);
//	mCurrentScene.registerTouchArea(settingBtn);       	
//       	
////       	//*********************save btn
////       	saveBtn= new Sprite(300, 356, this.btn_saveRegion, mEngine.getVertexBufferObjectManager()){
////			@Override
////			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
////	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
////	   				
////	   			 //***********save to file
////	   			String FILENAME = "titris_scoring_file.red";
////	   			
////	   			
////	   			int magic=1986;
////
////	   			FileOutputStream fos;
////				try {
////					if(best_score<Game.score){best_line=Game.lines;best_score=Game.score;}
////					
////					fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
////					fos.write((Integer.toString(magic)+"\n").getBytes());
////					fos.write((Integer.toString(best_score)+"\n").getBytes());
////					fos.write((Integer.toString(best_line)+"\n").getBytes());
////					mybestLine.setText(Integer.toString  (best_line));
////					monBestScore.setText(Integer.toString  (best_score));
////					
////					
////					
////					fos.write((Integer.toString(Game.score)+"\n").getBytes());
////					fos.write((Integer.toString(Game.lines)+"\n").getBytes());
////					
////					while (Game.calculate_is_performed==true){Thread.sleep(40,0);}
////					
////					Game.calculate_is_performed=true;
////					for(int i=0;i<21;i++)
////						for(int j=1;j<11;j++)
////							if(Game.mymodel[i][j].filled)
////							{
////							fos.write((Integer.toString(1)+"\n").getBytes());		
////							fos.write((Integer.toString(Game.mymodel[i][j].r)+"\n").getBytes());
////							fos.write((Integer.toString(Game.mymodel[i][j].g)+"\n").getBytes());	
////							fos.write((Integer.toString(Game.mymodel[i][j].b)+"\n").getBytes());		
////							}
////							else fos.write((Integer.toString(0)+"\n").getBytes());	
////					
////					
////					
////					
////					Game.calculate_is_performed=false;
////					
////		   			fos.close();
////				} catch (FileNotFoundException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////				catch (InterruptedException e) {e.printStackTrace();}	
////	   			 //**********************
////	   			 
////	   			 															}
////	   			 
////	   			 return true;
////       	}};
////       		mCurrentScene.attachChild(saveBtn);
////       	mCurrentScene.registerTouchArea(saveBtn);
////     //====================================================================================================================  	
////       	loadBtn= new Sprite(300, 356, this.btn_loadRegion, mEngine.getVertexBufferObjectManager()){
////			@Override
////			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
////	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
////	   				
////	   				String FILENAME = "titris_scoring_file.red";
////	   	   			
////	   	   			
////
////	   	   			FileInputStream fos;
////	   				try {
////	   					fos = openFileInput(FILENAME);
////	   					
////	   					InputStream in = null;
////	   					   
////	   					     in = new BufferedInputStream(fos);
////	   					    //DataInputStream din=new DataInputStream(in);
////	   					    BufferedReader din = new BufferedReader(new InputStreamReader(in));
////	   					
////	   					    while (Game.calculate_is_performed==true)
////	 					{
////	 						 try {
////	 			                   
////	 		                    	Thread.sleep(40,0);
////	 		                    	
////	 		                       }	
////	 		                       catch (InterruptedException e) {e.printStackTrace();}	
////	 			 					
////	 					}
////	   					 Game.calculate_is_performed=true;
////	   					 
////	   					 int magic=0;
////	   					magic=Integer.parseInt(din.readLine());	 
////	   				if(magic==1986){
////	   				best_score=Integer.parseInt(din.readLine());	   
////	   				best_line=Integer.parseInt(din.readLine());
////	   					 
////	   				Game.score=Integer.parseInt(din.readLine());/**/	     
////	   				Game.lines=Integer.parseInt(din.readLine());
////				
////						int filled,r,g,b;
////						
////						filled=0;
////						for(int i=0;i<21;i++)
////							for(int j=1;j<11;j++)
////								{
////							 filled=Integer.parseInt(din.readLine());
////								if(filled==1)
////								{   Game.mymodel[i][j].filled=true;
////									r=Integer.parseInt(din.readLine());		
////									g=Integer.parseInt(din.readLine());
////									b=Integer.parseInt(din.readLine());	
////								Game.mymodel[i][j].r=r;
////								Game.mymodel[i][j].g=g;
////								Game.mymodel[i][j].b=b;
////								Game.mymodel[i][j].rect.setColor((float)r/255,(float)g/255,(float)b/255);
////										
////								}
////								else {Game.mymodel[i][j].filled=false;	}
////	   					 
////								}
////	   					 
////						
////	   				}
////						 if(Game.game_started==false)
////		 	   				{
////		 	   					Game.game_started=true;
////		 	   					Game.game_paused=false;
////		 	   				 Game.generate_new_ship();
////		 	   				
////		 	   				}   
////						 else {Game.game_paused=false;Game.centerPosX=0;Game.centerPosY=4;}
////	   					    
////						 Game.paint_scene();
////						 Game.calculate_is_performed=false;
////	   		   			fos.close();
////	   				} catch (FileNotFoundException e) {
////	   					// TODO Auto-generated catch block
////	   					e.printStackTrace();
////	   				} catch (IOException e) {
////	   					// TODO Auto-generated catch block
////	   					e.printStackTrace();
////	   				}	
////	   				 
////	   				 
////	   			 }
////	   			 
////	   			 return true;
////       	}};
////       		mCurrentScene.attachChild(loadBtn);
////       	mCurrentScene.registerTouchArea(loadBtn);
//     //====================================================================================================================  	
////       	exitBtn= new Sprite(300, 412, this.btn_exitRegion, mEngine.getVertexBufferObjectManager()){
////			@Override
////			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
////	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
////	   				
////	   			 //***********save to file
////	   			
////	   			
////	   			
////	   			
////	   			Game.calculate_is_performed=true;
////	   			try {
////	   				finish();
////				} catch (Throwable e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////	   			 														}
////	   			 return true;
////       	}};
////       	//==================================================================================================================
////       		mCurrentScene.attachChild(exitBtn);
////       	mCurrentScene.registerTouchArea(exitBtn);
//       	//************
////     	classicBtn= new Sprite(300, 412, this.btn_classicRegion, mEngine.getVertexBufferObjectManager()){
////			@Override
////			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
////	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
////	   				
////	   			if(Game.extrat_mode==true){Game.extrat_mode=false;monTexteoff.setText("OFF");  } 
////	   			else{ Game.extrat_mode=true;monTexteoff.setText("ON"); }
////	   			
////				
////	   			 														}
////	   			 return true;
////       	}};
////       	//==================================================================================================================
////       		mCurrentScene.attachChild(classicBtn);
////       	mCurrentScene.registerTouchArea(classicBtn);
//       	//************
//
//       	pauseBtn.setScale(0.75f);
//       	settingBtn.setScale(0.75f);
////       	exitBtn.setScale(0.75f);
//		//*********************
//       	pauseBtn.setPosition(BUTTON_LOC_X,0);
//     	settingBtn.setPosition(BUTTON_LOC_X,56); //Settings Button
////       	exitBtn.setPosition(BUTTON_LOC_X,132);
//
////     	exitBtn.setPosition(BUTTON_LOC_X,CAMERA_HEIGHT-56);
//       	
//       	
//       	
////       	pauseBtn.setPosition(BUTTON_LOC_X,199);
////       	saveBtn.setPosition(BUTTON_LOC_X,255);
////       	loadBtn.setPosition(BUTTON_LOC_X,311);
////       	classicBtn.setPosition(BUTTON_LOC_X,367);
////       	exitBtn.setPosition(BUTTON_LOC_X,423);
//       			
//       	
//       	
//       	time_to_move_left_down_up=System.currentTimeMillis();	
//       	onLoadComplete();
////		return mCurrentScene;
//	}
	
////	@Override
//	public void onLoadComplete() {
//		
////		Game = new GameManager(this.getEngine().getScene());
////		Game = new GameManager(mCurrentScene);
//		//Game.generate_new_ship();
//		Game.game_started=false;
//		// start the thread to force the user to down the ship
//
//		 
//		 //Font font,font1,font2,font3,font4;
//		 Font font,font1,font2,font3;
//		 final BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(this.getEngine().getTextureManager(), 256, 256, TextureOptions.BILINEAR);
//		 final BitmapTextureAtlas fontTexture1 = new BitmapTextureAtlas(this.getEngine().getTextureManager(), 256, 256, TextureOptions.BILINEAR);
//		 final BitmapTextureAtlas fontTexture2 = new BitmapTextureAtlas(this.getEngine().getTextureManager(), 256, 256, TextureOptions.BILINEAR);
//		 final BitmapTextureAtlas fontTexture3 = new BitmapTextureAtlas(this.getEngine().getTextureManager(), 256, 256, TextureOptions.BILINEAR);
//		 
//		 this.getEngine().getTextureManager().loadTexture(fontTexture);
//		 this.getEngine().getTextureManager().loadTexture(fontTexture1);
//		 this.getEngine().getTextureManager().loadTexture(fontTexture2);
//		 this.getEngine().getTextureManager().loadTexture(fontTexture3);
//		 
//		 //===================================================================================================
//		 font = new Font(this.getEngine().getFontManager(), fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.GREEN);
//		 this.getEngine().getFontManager().loadFont(font);
//		 final Text monTexteBestscore = new Text(SCORE_LOC_X,60, font , "Best Score   :", this.getEngine().getVertexBufferObjectManager());
//		 							monBestScore = new Text(SCORE_LOC_X + 170,60, font , "00000000000", this.getEngine().getVertexBufferObjectManager());
//		 //===================================================================================================							
//		 font1 = new Font(this.getEngine().getFontManager(), fontTexture1, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.RED);
//		 			this.getEngine().getFontManager().loadFont(font1);							
//		 final Text monTexteBestline = new Text(SCORE_LOC_X,90, font1 ,  "Best Lines   :", this.getEngine().getVertexBufferObjectManager());
//		 									mybestLine= new Text(SCORE_LOC_X + 170,90, font1 , "00000000000", this.getEngine().getVertexBufferObjectManager());
//		 //===================================================================================================							
////		 font2 = new Font(this.getEngine().getFontManager(), fontTexture2, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.BLUE);
////		 		this.getEngine().getFontManager().loadFont(font2);	 
////		 		final Text monTextescore = new Text(SCORE_LOC_X,120, font2 ,    "Actual Score :", this.getEngine().getVertexBufferObjectManager());
////		 	monScore = new Text(SCORE_LOC_X + 170,120, font2 , "00000000000", this.getEngine().getVertexBufferObjectManager());
//		 //===================================================================================================							
//		font3 = new Font(this.getEngine().getFontManager(), fontTexture3, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.YELLOW);
//			 		this.getEngine().getFontManager().loadFont(font3);	 	
//		final Text monTexteline = new Text(SCORE_LOC_X,150, font3 ,     "Actual Lines  :", this.getEngine().getVertexBufferObjectManager());
//		 							myline= new Text(SCORE_LOC_X + 170,150, font3 , "00000000000", this.getEngine().getVertexBufferObjectManager());
//		
//		final Text monTexteNext = new Text(NEXT_BLOCK_LOC_X, 200, font3 , "Next :", this.getEngine().getVertexBufferObjectManager());
//		 monTexteoff = new Text(BUTTON_LOC_X+130,384, font1 , "OFF", this.getEngine().getVertexBufferObjectManager());
//		 //***********************************read from file*****************************
//	 	
//	 	// Create the Heads Up Display
//	    gameHUD = new HUD();
//	    
//	    // CREATE SCORE TEXT
//		 font2 = new Font(this.getEngine().getFontManager(), fontTexture2, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.BLUE);
//	 		this.getEngine().getFontManager().loadFont(font2);	 
////	 		final Text monTextescore = new Text(SCORE_LOC_X,120, font2 ,    "Score :", this.getEngine().getVertexBufferObjectManager());
////	 	monScore = new Text(SCORE_LOC_X + 170,120, font2 , "00000000000", this.getEngine().getVertexBufferObjectManager());
//	 		
//	 	final Text monTextescore = new Text(40, 0, font3, "Score:", new TextOptions(HorizontalAlign.LEFT), this.getEngine().getVertexBufferObjectManager());
//	    monScore = new Text(120, 0, font3, "0123456789", new TextOptions(HorizontalAlign.LEFT), this.getEngine().getVertexBufferObjectManager());
//	    //scoreText.setAnchorCenter(0, 0);    
//	    monScore.setText("0");
//	    gameHUD.attachChild(monTextescore);
//	    gameHUD.attachChild(monScore);
//	    
//	    gameHUD.attachChild(monTexteNext);
//	    
//	    mCamera.setHUD(gameHUD);
//	    
//	    
//			best_score=0;best_line=0;
//			String FILENAME = "titris_scoring_file.red";
//   			
//   			
//
//   			FileInputStream fos;
//			try {
//				fos = openFileInput(FILENAME);
//				
//				InputStream in = null;
//				   
//				     in = new BufferedInputStream(fos);
//				    //DataInputStream din=new DataInputStream(in);
//				    BufferedReader din = new BufferedReader(new InputStreamReader(in));
//				    int magic=0;
//				    magic=Integer.parseInt(din.readLine());
//				   if(magic==1986){
//					   best_score =Integer.parseInt(din.readLine());
//					   best_line=Integer.parseInt(din.readLine());
//				   }
//	   			fos.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//			mybestLine.setText(Integer.toString  (best_line));
//			monBestScore.setText(Integer.toString  (best_score));
//		//****************************************************************
////			 this.getEngine().getScene().attachChild(monTexteNext);
////			
////			
////		 mCurrentScene.attachChild(monTextescore); 
////	     mCurrentScene.attachChild( monScore); 
//			
////		 mCurrentScene.attachChild(monTexteline);
////		 mCurrentScene.attachChild( monTexteBestline); 
////		 mCurrentScene.attachChild(monTexteBestscore); 
////
////		 mCurrentScene.attachChild( myline); 
////		 mCurrentScene.attachChild(mybestLine); 
////		 mCurrentScene.attachChild( monBestScore);
////		 mCurrentScene.attachChild( monTexteoff);
//		 
//	}
	

	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
	    mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                // load menu resources, create menu scene
	                // set menu scene using scene manager
	                // disposeSplashScene();
	                // READ NEXT ARTICLE FOR THIS PART.
	                //SceneManager.getInstance().createMenuScene();
	                SceneManager.getInstance().loadGameScene(mEngine);
	            }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	
	//=========================================================================================
	@Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (!handleKeyEvent(keyCode, event))
        {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }
	
	private boolean handleKeyEvent(int keyCode, KeyEvent event) {
		boolean onGameScene = SceneManager.getInstance().getCurrentSceneType() == SceneManager.SceneType.SCENE_GAME;;
		boolean onOffsetScene = SceneManager.getInstance().getCurrentSceneType() == SceneManager.SceneType.SCENE_OFFSET;
        OffsetScene offsetScene = null;		
		//Only operate when on the game scene
        Game = GameManager.getSharedInstance();
        time_to_move_left_down_up=System.currentTimeMillis();
        if(onOffsetScene)
        {
        	offsetScene = OffsetScene.getInstance();
        }
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(onGameScene)
			{
//				if(!Game.downShip()){Game.chape_cant_down();}
//				Game.last_down=System.currentTimeMillis();
				Game.move_down_Ship(true);
			}
			else if(onOffsetScene)
	        {
                offsetScene.addOffsetXY(0.0f, 1.0f);
	        }
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(onGameScene) Game.move_to_left_Ship();
			else if(onOffsetScene)
	        {
                offsetScene.addOffsetXY(-1.0f, 0.0f);
	        }
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(onGameScene) Game.move_to_right_Ship();
			else if(onOffsetScene)
	        {
                offsetScene.addOffsetXY(1.0f, 0.0f);
	        }
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if(onGameScene) Game.rotate_ship();
			else if(onOffsetScene)
	        {
                offsetScene.addOffsetXY(0.0f, -1.0f);
	        }
			break;
			/* Start button */
        case KeyEvent.KEYCODE_BUTTON_START:
        	if(onGameScene) 
        	{	
	        	//Resume the game
	        	GameScene gameScene;
	        	gameScene = GameScene.getInstance();
	        	if(Game.game_paused)
	        	{	
	        	    gameScene.resumeGame();
	        	}
	        	else //Pause the game
	        	{
	        		//Pause the game
	             	Game.game_paused=true;
	             	Game.calculate_is_performed=true;
	             	//TODO: The Menu is not loading
	        	    gameScene.loadPauseMenuScene();	
	        	}
	        	return true;
        	}
            //break;
	    /* Right action button - can either be BACK+ALT or BUTTON_C depending on device mode */
        case KeyEvent.KEYCODE_BACK:
            if(!event.isAltPressed()) {
    			//Execute the back key press method of the currently loaded scene
    	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
    	        return true;
            }
            break;	
		default:
            return false;
		}
		
		//TODO: use new update score method
		
//		monScore.setText(Integer.toString  (Game.score));
//		myline.setText(Integer.toString  (Game.lines));
		Game.paint_scene();
		
		return true;	
//		return super.onKeyDown(keyCode, event);
		//return mHasWindowFocused; 					
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	    System.exit(0);	
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}


