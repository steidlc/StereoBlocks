package com.stereopsis.stereoblocks;
 
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.stereopsis.stereoblocks.R;
 
public class UserSettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String KEY_PREF_SYNC_CONN = "pref_syncConnectionType";
	public static final String KEY_PREF_MOVING_SPEED = "prefMovingSpeed";
	public static final String KEY_PREF_EXTRA_PIECE = "prefExtraPiece";
	public static final String KEY_PREF_COLOR_MIX = "prefColorMix";
	public static final String KEY_PREF_GAME_BORDER_MODE = "prefGameBorderMode";
	
	public static final String KEY_PREF_LAZY_EYE_COLOR = "prefMovingColor";
	public static final String KEY_PREF_LAZY_EYE_COLOR_SATURATION = "prefMovingSaturation";
	public static final String KEY_PREF_LAZY_EYE_OFFSET_X = "prefMovingXOffset";
	public static final String KEY_PREF_LAZY_EYE_OFFSET_Y = "prefMovingYOffset";

	public static final String KEY_PREF_DOMINANT_EYE_COLOR = "prefStaticColor";
	public static final String KEY_PREF_DOMINANT_EYE_COLOR_SATURATION = "prefStaticSaturation";
	public static final String KEY_PREF_DOMINANT_EYE_OFFSET_X = "prefStaticXOffset";
	public static final String KEY_PREF_DOMINANT_EYE_OFFSET_Y = "prefStaticYOffset";
	public static final String KEY_PREF_RESET_DEFAULT = "resetDialog";
	
	GameActivity activity;
	GameManager Game;
	UserSettingActivity instance;

	//A handle to the Preference that resets all values to their defaults.  
    private Preference resetDialogPreference;  
    //An intent object, that holds the intent that started this Activity.  
    private Intent startIntent;  

	
    //TODO: New method for launching settings activity
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        instance = this;
        ResourcesManager.getInstance();
	    Game = GameManager.getSharedInstance();
	    
        addPreferencesFromResource(R.xml.settings);
        
//        //Get this application SharedPreferences editor  
//        SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();  
//        //Clear all the saved preference values.  
//        preferencesEditor.clear();  
//        //Read the default values and set them as the current values.  
//        PreferenceManager.setDefaultValues(this, R.xml.settings, true);  
//        //Commit all changes.  
//        preferencesEditor.commit();  
        
        //Implemt the reset to default setting
        //http://www.41post.com/4924/programming/android-reset-to-default-preference-dialog
        //Initialize the preference object by obtaining a handle to the ResetDefDiagPref object as a Preference  
        this.resetDialogPreference = getPreferenceScreen().findPreference(KEY_PREF_RESET_DEFAULT);  
  
        //Store the Intent that started this Activity at this.startIntent.  
        this.startIntent = getIntent();  
        
        //Set the OnPreferenceChangeListener for the resetDialogPreference  
        this.resetDialogPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener()   
        {  
            @Override  
            public boolean onPreferenceChange(Preference preference, Object newValue)   
            {  
                //Both enter and exit animations are set to zero, so no transition animation is applied  
                overridePendingTransition(0, 0);  
                //Call this line, just to make sure that the system doesn't apply an animation  
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  
                //Close this Activity  
                finish();  
                //Again, don't set an animation for the transition  
                overridePendingTransition(0, 0);  
                //Start the activity by calling the Intent that have started this same Activity  
                startActivity(startIntent);  
                //Return false, so that nothing happens to the preference values  
                return false;  
            }  
        });  
        
        //Update the summary text
        this.userSettingsUpdate();
    }
    
    public UserSettingActivity getSharedInstance()
    {
    	return instance;
    }
    
    public void userSettingsUpdate()
    {
    	//Update the summary info for all settings.
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    	Map<String,?> keys = sharedPrefs.getAll();
    	for(Map.Entry<String,?> entry : keys.entrySet()){
    		this.onSharedPreferenceChanged(sharedPrefs, entry.getKey());
    	}    	
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
        //Error checking and summary text change when a variable changes.
    	Preference connectionPref = findPreference(key);
    	
        if (key.equals(KEY_PREF_COLOR_MIX) || key.equals(KEY_PREF_GAME_BORDER_MODE)) 
        {
        	String ColorMixType;
            ColorMixType = sharedPrefs.getString(key, "None");	        
	        connectionPref.setSummary(ColorMixType);	        
        }
        else if (key.equals(KEY_PREF_MOVING_SPEED)) 
        {
        	float speedPercentage;
            speedPercentage = Float.parseFloat(sharedPrefs.getString(KEY_PREF_MOVING_SPEED, "50"))/100;
            //Update the summary to the value set
            connectionPref.setSummary((String.format("%.0f", speedPercentage*100.0f)) + " Percent");
        }
        else if (key.equals(KEY_PREF_LAZY_EYE_COLOR) || key.equals(KEY_PREF_DOMINANT_EYE_COLOR)) 
        {
	        connectionPref.setSummary(sharedPrefs.getString(key, "Unknown"));
        }

        else if (key.equals(KEY_PREF_LAZY_EYE_COLOR_SATURATION) || key.equals(KEY_PREF_DOMINANT_EYE_COLOR_SATURATION)) 
        {
        	float saturationPercentage;
        	saturationPercentage = Float.parseFloat(sharedPrefs.getString(key, "50"));
            connectionPref.setSummary((String.format("%.0f", saturationPercentage)) + " Percent");
        }
        else if (key.equals(KEY_PREF_LAZY_EYE_OFFSET_X))
        {
        	this.offsetErrorCheck(sharedPrefs, key, "Lazy Eye", "Horizontal", -1.0f*Game.lazyEyeMaxX, Game.lazyEyeMaxX);
        }
        else if (key.equals(KEY_PREF_LAZY_EYE_OFFSET_Y))
        {
        	this.offsetErrorCheck(sharedPrefs, key, "Lazy Eye", "Virtical", -1.0f*Game.lazyEyeMaxY, Game.lazyEyeMaxY);
        }
        else if (key.equals(KEY_PREF_DOMINANT_EYE_OFFSET_X))
        {
        	this.offsetErrorCheck(sharedPrefs, key, "Dominant Eye", "Horizontal", -1.0f*Game.dominantEyeMaxX, Game.lazyEyeMaxX);
        }
        else if (key.equals(KEY_PREF_DOMINANT_EYE_OFFSET_Y))
        {
        	this.offsetErrorCheck(sharedPrefs, key, "Dominant Eye", "Vertical", -1.0f*Game.dominantEyeMaxY, Game.lazyEyeMaxY);
        }       
    }
    
    @SuppressWarnings("deprecation")
    private void offsetErrorCheck(SharedPreferences sharedPrefs, String key, String eyeTypeM, String offsetType, float Min, float Max)
    {
        String ErrorM = "";
        String OffsetStr;
        float offset;
        //Make sure value is within size of the screen
    	Preference connectionPref = findPreference(key);
    	
//    	float offset = Float.parseFloat(sharedPrefs.getString(key, "0"));
    	OffsetStr = sharedPrefs.getString(key, "0");
//    	OffsetStr = "0,00"; // force error
    	OffsetStr = OffsetStr.replace(",", "."); // Replace comma with decimal point to try and prevent convert to float error

        try {
            offset = Float.parseFloat(OffsetStr);        
        } catch (NumberFormatException e) {
            // Handle The Exception During  Parsing
    	    offset = 0.0f;
        	ErrorM = eyeTypeM + " " + offsetType + " offset is an invalid numeric number. Offset will be set to " + String.valueOf(offset);    	    
        }   	

        if(offset < Min)
        {
        	offset = Min;
        	ErrorM = eyeTypeM + " " + offsetType + " offset outside of game bounds. Offset will be set to " + String.valueOf(offset);
        }
        else if(offset > Max)
        {
        	offset = Max;
        	ErrorM = eyeTypeM + " " + offsetType + " offset outside of game bounds. Offset will be set to " + String.valueOf(offset);
        
        }
        
        connectionPref.setSummary((String.format("%.2f px", offset)));
        
        if(ErrorM.length() > 0)
        { //There was an error, print the error message
        	Editor editor = sharedPrefs.edit();
        	editor.putString(key, String.valueOf(offset));
        	editor.commit();
        	errorMessage(ErrorM, Toast.LENGTH_LONG);
        }
    	
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
    
    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
//        getPreferenceScreen().getSharedPreferences()
//                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        
//        getPreferenceScreen().getSharedPreferences()
//                .unregisterOnSharedPreferenceChangeListener(this);
        
    }
    
}