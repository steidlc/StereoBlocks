package com.stereopsis.stereoblocks;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;

import com.stereopsis.stereoblocks.GameActivity;
import com.stereopsis.stereoblocks.spiriteModel;
import com.stereopsis.stereoblocks.spiriteModel.spriteColorType;

import java.util.Random;

import android.opengl.GLES20;


//public class GameManager extends Scene implements IOnSceneTouchListener {
public class GameManager {
	public GameScene myscene;
	private ResourcesManager resourcesManager;
	public Random randomGenerator;
	public Random randomGeneratorColor;
	
	public Entity lazyEyeLayer = new Entity(); //weaker, amblyopic eye layer
	public Entity fellowEyeLayer = new Entity(); //stronger, fellow eye layer
	public spiriteModel [][] mymodel;
	public spiriteModel [][] mymodelLazy;
	
	public  spiriteModel [][] typemodel0;
	public  spiriteModel [][] typemodel1;
	public  spiriteModel [][] typemodel2;
	public  spiriteModel [][] typemodel3;
	public  spiriteModel [][] typemodel4;
	public  spiriteModel [][] typemodel5;
	public  spiriteModel [][] typemodel6;
	///***************************next generation
	public  spiriteModel [][] typemodel7;
	public  spiriteModel [][] typemodel8;
	public  spiriteModel [][] typemodel9;
	public  spiriteModel [][] typemodel10;
	public  spiriteModel [][] typemodel11;
	public  spiriteModel [][] typemodel12;
	public  spiriteModel [][] typemodel13;
	
	
	///********************************************
	public  spiriteModel [][] current_typemodel;
	public  spiriteModel [][] next_typemodel;
	
	public  spiriteModel [][] current_typemodel_lazy;
	public  spiriteModel [][] next_typemodel_lazy;
	
	public  spiriteModel [][] temp_current_typemodel;
	public  spiriteModel [][] temp_current_typemodel_lazy;
	public  spiriteModel [][] temp_current_typemodel_filled;
	
	public  spiriteModel [][] current_typemodel_filled;
	
	///***** Temporary sprites to see if moving and static objects can overlap.
	public  spiriteModel [][] mixedcolor_typemodel;

	
	//Color of the falling blocks
	public int falling_color_r=0;
	public int falling_color_g=0;
	public int falling_color_b=128;

	//Color of the blocks that are set at the bottom of the puzzle
	public int set_block_color_r=128;
	public int set_block_color_g=0;
	public int set_block_color_b=0;
	public boolean set_block_mix_colors = false;
	
    public enum MixColorType
    {
    	//Moving and static blocks are always separate colors
        MIXCOLOR_NONE,
        
        //Moving blocks are all one color.
        //Static blocks are mix of both colors 2 layers below top static blocks.
        //Top 2 layers of static blocks are fellow eye color
        MIXCOLOR_STATIC_ONLY,
        
        //Static blocks can be seen by both eyes
        //Moving blocks are a mixture of blocks that can only be seen by 
        //amblyopic eye, fellow eye, and seen by both 
        MIXCOLOR_STATIC_MOVING,
        
        //Static blocks can be seen by only by fellow eye
        //Moving blocks are a mixture of blocks that can only be seen by 
        //amblyopic eye, fellow eye, and seen by both
        MIXCOLOR_MOVING_ONLY     
    }
    
    public MixColorType currentMixColorType = MixColorType.MIXCOLOR_NONE;
    public MixColorType previousMixColorType = currentMixColorType;
    
    public enum GameBorderType
    {
    	GAME_BORDER_DOMINANT,
    	GAME_BORDER_BOTH,
    	GAME_BORDER_ALTERNATING,
    	GAME_BORDER_MIXED
    }
    
    public GameBorderType currentGameBorderType = GameBorderType.GAME_BORDER_DOMINANT;
    public GameBorderType previousGameBorderType = currentGameBorderType;
    
	private static final int BLOCK_WIDTH = 25;
	private static final int BLOCK_HEIGHT = 25;

	private static final int BLOCK_WIDTH_NEXT = 15;
	private static final int BLOCK_HEIGHT_NEXT = 15;
	
	private static final int BLOCK_WIDTH_NUMBER = 12;
	private static final int BLOCK_HEIGHT_NUMBER = 22;
		
	public int boardOffsetX = 0;
	public int boardOffsetY = 0;
	
	public float fallingOffsetX = 0;
	public float fallingOffsetY = 0;
	
	public float staticOffsetX = 0;
	public float staticOffsetY = 0;
	
	public float lazyEyeMaxX;
	public float lazyEyeMaxY;
	public float dominantEyeMaxX;
	public float dominantEyeMaxY;
	
	public int rowsSinceLastSpeedUp = 0;
	private static final int ROWS_BEFORE_SPEED_UP = 5;
	private static final float SPEED_UP_PERCENTAGE = 0.05f;
	
	public Random randomGeneratorSetBlockColor;
	
	public int current_ship;
	public int current_state;
	public int centerPosX;
	public int centerPosY;
	public boolean first_time;
	public int cocolor;
	public boolean no_chip;
	public long last_down;
	public boolean calculate_is_performed;
	public int score;
	public int lines;
	public int best_lines;
	public int next,current;
	boolean extrat_mode;
	
	private GameActivity activity;
	public static GameManager instance;
	
	public GameManager()
	
	{   activity = GameActivity.getSharedInstance();
	    resourcesManager = ResourcesManager.getInstance();
		extrat_mode=false;
		next=current;
		best_lines=0;
		lines=0;
		calculate_is_performed=false;
		game_paused=false;
		score=0;
		last_down=System.currentTimeMillis();
		first_time=true;
		no_chip=true;

		randomGenerator = new Random(System.currentTimeMillis());next=current=randomGenerator.nextInt(7);
		randomGeneratorColor = new Random(System.currentTimeMillis());
		randomGeneratorSetBlockColor = new Random(System.currentTimeMillis());
		//cocolor = randomGeneratorColor.nextInt(10);
		cocolor = 8; //red
		
		instance = this;
		
		//Center the game board on the game scene
		// (CamerWidth - GameboardWidth)/2
		boardOffsetX = (int) ((resourcesManager.camera.getWidth() - BLOCK_WIDTH_NUMBER*(BLOCK_WIDTH+1))/2);
		// (CamerHeight - GameboardHeight)/2
		boardOffsetY = (int) ((resourcesManager.camera.getHeight() - BLOCK_HEIGHT_NUMBER*(BLOCK_HEIGHT+1))/2);
		
    	lazyEyeMaxX = boardOffsetX;
    	lazyEyeMaxY = boardOffsetY;
    	dominantEyeMaxX = boardOffsetX;
        dominantEyeMaxY = boardOffsetY;
        
		mymodel=new spiriteModel[22][12];
		mymodelLazy=new spiriteModel[22][12];
		typemodel0=new spiriteModel[5][5];
		typemodel1=new spiriteModel[5][5];
		typemodel2=new spiriteModel[5][5];
		typemodel3=new spiriteModel[5][5];
		typemodel4=new spiriteModel[5][5];
		typemodel5=new spiriteModel[5][5];
		typemodel6=new spiriteModel[5][5];
		//*****extra piece*
		typemodel7=new spiriteModel[5][5];
		typemodel8=new spiriteModel[5][5];
		typemodel9=new spiriteModel[5][5];
		typemodel10=new spiriteModel[5][5];
		typemodel11=new spiriteModel[5][5];
		typemodel12=new spiriteModel[5][5];
		typemodel13=new spiriteModel[5][5];
		
		
		
		//******
	    next_typemodel=new spiriteModel[5][5];
		current_typemodel=new spiriteModel[5][5];
		
		next_typemodel_lazy=new spiriteModel[5][5];
		current_typemodel_lazy=new spiriteModel[5][5];
		
		temp_current_typemodel= new spiriteModel[5][5];
		temp_current_typemodel_lazy= new spiriteModel[5][5];
		temp_current_typemodel_filled=new spiriteModel[5][5];
		
		current_typemodel_filled=new spiriteModel[5][5];
		
	    mixedcolor_typemodel=new spiriteModel[5][5];
		
	//	current_ship = randomGenerator.nextInt(3);
		//**********initialise the global game table to void
		for(int i=0;i<22;i++)
			for( int j=0;j<12;j++)
				{ 
				mymodel[i][j]= new spiriteModel();
				mymodel[i][j].filled=false;
				mymodel[i][j].rect=new Rectangle(0, 0, BLOCK_WIDTH, BLOCK_HEIGHT, activity.getVertexBufferObjectManager());	
				
				mymodelLazy[i][j]= new spiriteModel();
				mymodelLazy[i][j].filled=false;
				mymodelLazy[i][j].rect=new Rectangle(0, 0, BLOCK_WIDTH, BLOCK_HEIGHT, activity.getVertexBufferObjectManager());	

				/**********************************************************************
				Setup the rectangle so that it adds the colors of objects underneath it
				This will make the red moving object turn to purple for any part that 
				overlap with the static blue shapes.
				**********************************************************************/
				mymodelLazy[i][j].rect.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
				}
		
				
		
	//	if(1==1) return;
		
		//**********initialise all ship to void
//		int x_next,y_next;
		float xNextStart, yNextStart;
//		x_next=8;
//		y_next=15;
		for(int i=0;i<5;i++)
			for( int j=0;j<5;j++)
			{   
				typemodel0[i][j]=new spiriteModel();
				typemodel1[i][j]=new spiriteModel();
				typemodel2[i][j]=new spiriteModel();
				typemodel3[i][j]=new spiriteModel();
				typemodel4[i][j]=new spiriteModel();
				typemodel5[i][j]=new spiriteModel();
				typemodel6[i][j]=new spiriteModel();
				//***
				typemodel7[i][j]=new spiriteModel();
				typemodel8[i][j]=new spiriteModel();
				typemodel9[i][j]=new spiriteModel();
				typemodel10[i][j]=new spiriteModel();
				typemodel11[i][j]=new spiriteModel();
				typemodel12[i][j]=new spiriteModel();
				typemodel13[i][j]=new spiriteModel();
				//***
				
				
				
				next_typemodel[i][j]=new spiriteModel();
				next_typemodel[i][j].rect=new Rectangle(0, 0, BLOCK_WIDTH_NEXT, BLOCK_HEIGHT_NEXT, activity.getVertexBufferObjectManager());
				next_typemodel[i][j].filled=false;

				next_typemodel_lazy[i][j]=new spiriteModel();
				next_typemodel_lazy[i][j].rect=new Rectangle(0, 0, BLOCK_WIDTH_NEXT, BLOCK_HEIGHT_NEXT, activity.getVertexBufferObjectManager());
				next_typemodel_lazy[i][j].filled=false;

				current_typemodel[i][j]=new spiriteModel();
				current_typemodel[i][j].rect=new Rectangle(0, 0, BLOCK_WIDTH, BLOCK_HEIGHT, activity.getVertexBufferObjectManager());
				current_typemodel[i][j].filled=false;

				current_typemodel_lazy[i][j]=new spiriteModel();
				current_typemodel_lazy[i][j].rect=new Rectangle(0, 0, BLOCK_WIDTH, BLOCK_HEIGHT, activity.getVertexBufferObjectManager());
				current_typemodel_lazy[i][j].filled=false;

				temp_current_typemodel[i][j]=new spiriteModel();
				temp_current_typemodel[i][j].filled=false;
				
				temp_current_typemodel_lazy[i][j]=new spiriteModel();
				temp_current_typemodel_lazy[i][j].filled=false;

				temp_current_typemodel_filled[i][j]=new spiriteModel();
				temp_current_typemodel_filled[i][j].filled=false;			
				
				current_typemodel_filled[i][j]=new spiriteModel();
				current_typemodel_filled[i][j].filled=false;

				
//				next_typemodel[i][j].rect.setPosition((j+y_next)*31, (i+x_next)*31);
//				xNextStart = (float) (BLOCK_WIDTH*12.5);
				
				//On right side
				xNextStart = resourcesManager.camera.getWidth() - 5*(BLOCK_WIDTH_NEXT+1);
//				yNextStart = (float) (BLOCK_HEIGHT*0) + BOARD_HEIGHT_OFFSET*BLOCK_HEIGHT;
				//Below next text
//				yNextStart = myscene.nextText.getHeight() + 5;
				yNextStart = 35;
				next_typemodel[i][j].rect.setPosition(xNextStart + j*(BLOCK_WIDTH_NEXT+1), yNextStart + i*(BLOCK_HEIGHT_NEXT+1));
                
				/**********************************************************************
				Setup the rectangle so that it adds the colors of objects underneath it
				This will make the red moving object turn to purple for any part that 
				overlap with the static blue shapes.
				**********************************************************************/
//				current_typemodel[i][j].rect.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
				current_typemodel_lazy[i][j].rect.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
				next_typemodel_lazy[i][j].rect.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);			
				
				
				mixedcolor_typemodel[i][j]=new spiriteModel();
				mixedcolor_typemodel[i][j].rect=new Rectangle(0, 0, BLOCK_WIDTH, BLOCK_HEIGHT, activity.getVertexBufferObjectManager());
//				mixedcolor_typemodel[i][j].rect.setPosition((j+centerPosY)*(BLOCK_WIDTH+1), boardOffsetY + (i+centerPosX)*(BLOCK_HEIGHT+1));

				mixedcolor_typemodel[i][j].rect.setPosition(boardOffsetX + (j+3)*(BLOCK_WIDTH+1), boardOffsetY + (i+0)*(BLOCK_HEIGHT+1));

				mixedcolor_typemodel[i][j].filled = true;
				
				typemodel0[i][j].filled=false;	
				typemodel1[i][j].filled=false;
				typemodel2[i][j].filled=false;
				typemodel3[i][j].filled=false;
				typemodel4[i][j].filled=false;
				typemodel5[i][j].filled=false;
				typemodel6[i][j].filled=false;
				//**
				typemodel7[i][j].filled=false;
				typemodel8[i][j].filled=false;
				typemodel9[i][j].filled=false;
				typemodel10[i][j].filled=false;
				typemodel11[i][j].filled=false;
				typemodel12[i][j].filled=false;
				typemodel13[i][j].filled=false;
				//**
			}
		
		//fill the first ship	
//		typemodel1[0][2].filled=true;typemodel1[1][2].filled=true;
//		typemodel1[2][2].filled=true;typemodel1[3][2].filled=true;
		
		// #
		// #
		// #
		// #
		fillShipBlock(typemodel1[0][2], spriteColorType.SPRITECOLOR_STATIC); 
		fillShipBlock(typemodel1[1][2], spriteColorType.SPRITECOLOR_MIXED);
		fillShipBlock(typemodel1[2][2], spriteColorType.SPRITECOLOR_MIXED);
		fillShipBlock(typemodel1[3][2], spriteColorType.SPRITECOLOR_MOVING);
		
		
		//fill the second ship	
	//*****************	
//		typemodel2[2][2].filled=true;typemodel2[2][1].filled=true;
//		typemodel2[2][3].filled=true;typemodel2[1][3].filled=true;
		
		//   #
		// ###
		fillShipBlock(typemodel2[1][3], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel2[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel2[2][3], spriteColorType.SPRITECOLOR_MIXED);
		fillShipBlock(typemodel2[2][1], spriteColorType.SPRITECOLOR_MOVING); 				
		//fill the third ship
//		typemodel0[2][2].filled=true;typemodel0[2][1].filled=true;
//		typemodel0[2][3].filled=true;typemodel0[1][1].filled=true;
		
	    // #
		// ###
		fillShipBlock(typemodel0[1][1], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel0[2][1], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel0[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel0[2][3], spriteColorType.SPRITECOLOR_MOVING);

		// fill ship number 4
		//  #
		// ###
//		typemodel3[2][2].filled=true;typemodel3[2][1].filled=true;
//		typemodel3[2][3].filled=true;typemodel3[1][2].filled=true;
		
		fillShipBlock(typemodel3[1][2], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel3[2][1], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel3[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel3[2][3], spriteColorType.SPRITECOLOR_MOVING);	
		
	//**************************************************	
		
		//fill ship number 5
//		typemodel4[1][2].filled=true;typemodel4[1][3].filled=true;
//		typemodel4[2][2].filled=true;typemodel4[2][3].filled=true;
		
		// ##
		// ##
		fillShipBlock(typemodel4[1][2], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel4[1][3], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel4[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel4[2][3], spriteColorType.SPRITECOLOR_MOVING);	
		
		//  #
		// ##
		// #
//		typemodel5[2][2].filled=true;typemodel5[2][3].filled=true;
//		typemodel5[3][2].filled=true;typemodel5[1][3].filled=true;

		fillShipBlock(typemodel5[1][3], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel5[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel5[2][3], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel5[3][2], spriteColorType.SPRITECOLOR_MOVING);	

		// #
		// ##
		//  #
//		typemodel6[2][2].filled=true;typemodel6[3][2].filled=true;
//		typemodel6[2][1].filled=true;typemodel6[1][1].filled=true;
		
		fillShipBlock(typemodel6[1][1], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel6[2][1], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel6[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel6[3][2], spriteColorType.SPRITECOLOR_MOVING);	

		
		//**************fill extra ship
		// #
//		typemodel7[2][2].filled=true;//the point ship
		fillShipBlock(typemodel7[2][2], spriteColorType.SPRITECOLOR_MIXED);

		// #
		// #
		// #
//		typemodel8[2][2].filled=true;
//		typemodel8[1][2].filled=true;
//		typemodel8[3][2].filled=true;
		fillShipBlock(typemodel8[1][2], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel8[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel8[3][2], spriteColorType.SPRITECOLOR_MOVING); 
		
		// ##
		// #
//		typemodel9[2][2].filled=true;
//		typemodel9[1][2].filled=true;
//		typemodel9[1][3].filled=true;
		
		fillShipBlock(typemodel9[1][3], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel9[1][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel9[2][2], spriteColorType.SPRITECOLOR_MOVING); 

		// #
		// ##
//		typemodel10[2][2].filled=true;
//		typemodel10[1][2].filled=true;
//		typemodel10[2][3].filled=true;
		
		fillShipBlock(typemodel10[1][2], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel10[2][2], spriteColorType.SPRITECOLOR_MIXED); 
		fillShipBlock(typemodel10[2][3], spriteColorType.SPRITECOLOR_MOVING); 		
		
		// #
		// #
//		typemodel11[2][2].filled=true;
//		typemodel11[1][2].filled=true;

		fillShipBlock(typemodel11[2][2], spriteColorType.SPRITECOLOR_STATIC);
		fillShipBlock(typemodel11[1][2], spriteColorType.SPRITECOLOR_MOVING); 		

		//
		// ###
		// ###
		// ###		
//		for(int ii=1;ii<4;ii++)
//			for(int jj=1;jj<4;jj++)
//				typemodel12[ii][jj].filled=true;

		for(int jj=1;jj<4;jj++)
			fillShipBlock(typemodel12[1][jj], spriteColorType.SPRITECOLOR_STATIC);
		for(int jj=1;jj<4;jj++)
			fillShipBlock(typemodel12[2][jj], spriteColorType.SPRITECOLOR_MIXED);
		for(int jj=1;jj<4;jj++)
			fillShipBlock(typemodel12[3][jj], spriteColorType.SPRITECOLOR_MOVING);

		
		//#####
		//#####
		//#####
		//#####
		//#####
//		for(int ii=0;ii<5;ii++)
//			for(int jj=0;jj<5;jj++)
//				typemodel13[ii][jj].filled=true;
		
		for(int jj=0;jj<5;jj++)
			fillShipBlock(typemodel13[0][jj], spriteColorType.SPRITECOLOR_STATIC);
		for(int jj=0;jj<5;jj++)
			fillShipBlock(typemodel13[1][jj], spriteColorType.SPRITECOLOR_MIXED);
		for(int jj=0;jj<5;jj++)
			fillShipBlock(typemodel13[2][jj], spriteColorType.SPRITECOLOR_MIXED);
		for(int jj=0;jj<5;jj++)
			fillShipBlock(typemodel13[3][jj], spriteColorType.SPRITECOLOR_MIXED);
		for(int jj=0;jj<5;jj++)
			fillShipBlock(typemodel13[4][jj], spriteColorType.SPRITECOLOR_MOVING);
		
		//****************************************
		
		
		
		
	
	// create the left and right and down wall
//		int r,g,b;
//		r=set_block_color_r;
//		g=set_block_color_g;
//		b=set_block_color_b;
	for(int j=0;j<12;j++)
		{
		mymodel[21][j].filled=true;
//		mymodel[21][j].r=r;mymodel[21][j].g=g;mymodel[21][j].b=b;
		//mymodel[21][j].rect=new Rectangle(0, 0, 21, 21);
		}
	for( int i=0;i<22;i++)	
		{mymodel[i][0].filled=true;
//		mymodel[i][0].r=r;mymodel[i][0].g=g;mymodel[i][0].b=b;
	//	mymodel[i][0].rect=new Rectangle(0, 0, 21, 21);
		}
	for( int i=0;i<22;i++)	
		{mymodel[i][11].filled=true;
//		mymodel[i][11].r=r;mymodel[i][11].g=g;mymodel[i][11].b=b;
		//mymodel[i][11].rect=new Rectangle(0, 0, 21, 21);
		}
	}// my init function

	private void fillShipBlock(spiriteModel block, spriteColorType ColorType)
	{
		//Set the block to filled and set the color type
		//Used when mixing the colors for the falling ships
	    block.filled = true;
	    block.currentColorType = ColorType;
	}
	public void setScene(GameScene _scene){
		myscene=_scene;			
	}
	public static GameManager getSharedInstance() {
		return instance;
	}
	
	public	boolean downShip()
	{
				
		// this code is applicable for each chips, for this we test all the matrices [5][5]   
		
		//Get all filled blocks for the ship
//        spiriteModel [][] typemodelFilled = this.get_ship_by_index(current);
		
		//check all column and find the
		
		boolean find;
		
		int last_find=0;
		boolean can_down=true;
		for(int j=0;j<5;j++)
		{
			find=false;
			for(int i=0;i<5;i++)
			{
				if(current_typemodel_filled[i][j].filled)
				{
					find=true;
					last_find=i;
				}	
			}
			if(find==true)
			{
				// checke the point just under our find
				if(mymodel[last_find+centerPosX+1][centerPosY+j].filled || 
					mymodelLazy[last_find+centerPosX+1][centerPosY+j].filled)
				{
				    can_down=false; 
				    break;
				}	
			}
		}// for j
		
		
		if(can_down)
		{
			centerPosX++;
		}
		else score+=10;
		{
		    return can_down;// you down the object or not
		}
	}
	
	public	boolean move_down_Ship(boolean playSound)
	{	
		boolean shipCanDown;
		
		shipCanDown = downShip();
		if(shipCanDown)
		{
			if (playSound)
			{
			    resourcesManager.playMoveShipSound();
			}
		}
		else
		{
			chape_cant_down();
		}
		last_down=System.currentTimeMillis();
		return shipCanDown;
	}
	
	public	boolean move_to_right_Ship()
	{	
		// this code is applicable for each chips, for this we test all the matrices [5][5]   		
		//check all column and find the

		//Get all filled blocks for the ship
//        spiriteModel [][] typemodelFilled = this.get_ship_by_index(current);

		boolean find;
		int last_find=0;
		boolean can_move_to_right=true;
		
		for(int i=0;i<5;i++)
		{
			find=false;
			for(int j=0;j<5;j++)
			{
			    if(current_typemodel_filled[i][j].filled)
			    {
			    	find=true;
			    	last_find=j;
			    }	
			}
			if(find==true)
			{
			    // checke the point just under our find
				if(mymodel[centerPosX+i][centerPosY+last_find+1].filled || 
				mymodelLazy[centerPosX+i][centerPosY+last_find+1].filled)
				{
					can_move_to_right=false; break;
				}
			}			
		}// for i
		
		if(can_move_to_right) 
		{
			centerPosY++;
			resourcesManager.playMoveShipSound();
		}
		
		return can_move_to_right;// you down the object or not
    }

	public	boolean move_to_left_Ship()
	{	
	//this code is applicable for each chips, for this we test all the matrices [5][5]   
		//Get all filled blocks for the ship
//        spiriteModel [][] typemodelFilled = this.get_ship_by_index(current);

		//check all column and find the
		boolean find;
		int last_find=0;
		boolean can_move_to_left=true;
		
		for(int i=0;i<5;i++)
		{
			find=false;
			for(int j=4;j>=0;j--)
			{
			    if(current_typemodel_filled[i][j].filled)
			    {
			    	find=true;
			    	last_find=j;
			    }	
			}
			if(find==true)
			{
				// checke the point just under our find
				if(mymodel[centerPosX+i][centerPosY+last_find-1].filled || 
				   mymodelLazy[centerPosX+i][centerPosY+last_find-1].filled)
				{
					can_move_to_left=false; break;
				}
			}		
					
		}// for j
		
		if(can_move_to_left)
		{
			centerPosY--;
			resourcesManager.playMoveShipSound();
		}
		
		return can_move_to_left;// you down the object or not
	}


    //Set the color of all visible blocks
	public void set_color_blocks()
	{			
		
		//Game board
		for(int i=0;i<21;i++)
			for(int j=1;j<11;j++)
			{
    			mymodel[i][j].setStaticColor(set_block_color_r, set_block_color_g, set_block_color_b);
    			mymodel[i][j].setMovingColor(falling_color_r, falling_color_g, falling_color_b);
    			mymodel[i][j].useStaticColor();
    			
    			mymodelLazy[i][j].setStaticColor(set_block_color_r, set_block_color_g, set_block_color_b);
    			mymodelLazy[i][j].setMovingColor(falling_color_r, falling_color_g, falling_color_b);
    			mymodelLazy[i][j].useMovingColor();
    			
//
//	    		if (mymodel[i][j].filled)
//	    		{
//	    			
//	    			mymodel[i][j].r=set_block_color_r;
//	    			mymodel[i][j].g=set_block_color_g;
//	    			mymodel[i][j].b=set_block_color_b;
//	    			mymodel[i][j].rect.setColor((float)set_block_color_r/255,(float)set_block_color_g/255,(float)set_block_color_b/255);
//	    		}
			}
		
		
		//Falling Blocks
		for(int i=0;i<5;i++)
	    	for(int j=0;j<5;j++)
	    	{
	    		current_typemodel[i][j].setStaticColor(set_block_color_r, set_block_color_g, set_block_color_b);
	    		current_typemodel[i][j].setMovingColor(falling_color_r, falling_color_g, falling_color_b);
	    		current_typemodel[i][j].useStaticColor();
	    		
	    		current_typemodel_lazy[i][j].setStaticColor(set_block_color_r, set_block_color_g, set_block_color_b);
	    		current_typemodel_lazy[i][j].setMovingColor(falling_color_r, falling_color_g, falling_color_b);
	    		current_typemodel_lazy[i][j].useMovingColor();

//	    		current_typemodel[i][j].rect.setAlpha(1.0f);
//	    		if (current_typemodel[i][j].filled)
//	    		{
//		    		current_typemodel[i][j].r=falling_color_r;
//		    		current_typemodel[i][j].g=falling_color_g;
//		    		current_typemodel[i][j].b=falling_color_b;
//		    		current_typemodel[i][j].rect.setColor((float)falling_color_r/255,(float)falling_color_g/255,(float)falling_color_b/255);
//	    		    
//	    		}
	    	}
		
		for(int i=0;i<5;i++)
	    	for(int j=0;j<5;j++)
	    	{
	    		next_typemodel[i][j].setStaticColor(set_block_color_r, set_block_color_g, set_block_color_b);
	    		next_typemodel[i][j].setMovingColor(falling_color_r, falling_color_g, falling_color_b);
	    		next_typemodel[i][j].useStaticColor();
	    		
	    		next_typemodel_lazy[i][j].setStaticColor(set_block_color_r, set_block_color_g, set_block_color_b);
	    		next_typemodel_lazy[i][j].setMovingColor(falling_color_r, falling_color_g, falling_color_b);
	    		next_typemodel_lazy[i][j].useMovingColor();

//	    		mixedcolor_typemodel[i][j].rect.setAlpha(1.0f);
//	    		if (next_typemodel[i][j].filled)
//	    		{
//	    			next_typemodel[i][j].r=set_block_color_r;
//	    			next_typemodel[i][j].g=set_block_color_g;
//	    			next_typemodel[i][j].b=set_block_color_b;
//	    			next_typemodel[i][j].rect.setColor((float)set_block_color_r/255,(float)set_block_color_g/255,(float)set_block_color_b/255);
//	    		}
	    	}
        
		//Game board outline. Made it a little darker or blended into the background
		int set_r=(int) (set_block_color_r*0.8f);
		int set_g=(int) (set_block_color_g*0.8f);
		int set_b=(int) (set_block_color_b*0.8f);
		
		int falling_r=(int) (falling_color_r*0.8f);
		int falling_g=(int) (falling_color_g*0.8f);
		int falling_b=(int) (falling_color_b*0.8f);
		
		for(int j=1;j<11;j++)
		{   
			mymodel[21][j].setStaticColor(set_r, set_g, set_b);
			mymodel[21][j].setMovingColor(falling_r, falling_g, falling_b);
			mymodel[21][j].useStaticColor();
			
			mymodelLazy[21][j].setStaticColor(set_r, set_g, set_b);
			mymodelLazy[21][j].setMovingColor(falling_r, falling_g, falling_b);
			mymodelLazy[21][j].useMovingColor();

		}
	    for( int i=0;i<22;i++)	
		{  
	    	mymodel[i][0].setStaticColor(set_r, set_g, set_b);
	    	mymodel[i][0].setMovingColor(falling_r, falling_g, falling_b);
	    	mymodel[i][0].useStaticColor();
	    	
			mymodelLazy[i][0].setStaticColor(set_r, set_g, set_b);
			mymodelLazy[i][0].setMovingColor(falling_r, falling_g, falling_b);
			mymodelLazy[i][0].useMovingColor();
		}
	    for( int i=0;i<22;i++)	
		{
	    	mymodel[i][11].setStaticColor(set_r, set_g, set_b);
	    	mymodel[i][11].setMovingColor(falling_r, falling_g, falling_b);
	    	mymodel[i][11].useStaticColor();

			mymodelLazy[i][11].setStaticColor(set_r, set_g, set_b);
			mymodelLazy[i][11].setMovingColor(falling_r, falling_g, falling_b);
			mymodelLazy[i][11].useMovingColor();
		}
	    
		if (previousMixColorType != currentMixColorType)
		{
			this.setStaticBlockFilledLazy();
			this.setFilledCurrentTypeModel();
			this.setFilledNextTypeModel();			
		}
		
		if (previousGameBorderType != currentGameBorderType)
		{
			this.setGameBoarderFilled();
		}
		
	    myscene.scoreText.setColor((float)set_block_color_r/255,(float)set_block_color_g/255,(float)set_block_color_b/255);
//	    myscene.pauseText.setColor((float)set_block_color_r/255,(float)set_block_color_g/255,(float)set_block_color_b/255);
	    myscene.pauseRectangle.setColor((float)set_block_color_r/255,(float)set_block_color_g/255,(float)set_block_color_b/255);

	    myscene.nextText.setColor((float)set_block_color_r/255,(float)set_block_color_g/255,(float)set_block_color_b/255);
	    
//		//Test mixed color
//		for(int i=0;i<2;i++)
//	    	for(int j=0;j<5;j++)
//	    	{
//    			mixedcolor_typemodel[i][j].r=falling_color_r;
//    			mixedcolor_typemodel[i][j].g=falling_color_g;
//    			mixedcolor_typemodel[i][j].b=falling_color_b;
//    			mixedcolor_typemodel[i][j].rect.setColor((float)falling_color_r/255,(float)falling_color_g/255,(float)falling_color_b/255);
//	    	}	
//		int mixed_color_r = falling_color_r + set_block_color_r;
//		int mixed_color_g = falling_color_g + set_block_color_g;
//		int mixed_color_b = falling_color_b + set_block_color_b;
//
//		for(int i=2;i<3;i++)
//	    	for(int j=0;j<5;j++)
//	    	{
//    			mixedcolor_typemodel[i][j].r=mixed_color_r;
//    			mixedcolor_typemodel[i][j].g=mixed_color_g;
//    			mixedcolor_typemodel[i][j].b=mixed_color_b;
//    			mixedcolor_typemodel[i][j].rect.setColor((float)mixed_color_r/255,(float)mixed_color_g/255,(float)mixed_color_b/255);
//	    	}	
//		
//		for(int i=3;i<5;i++)
//	    	for(int j=0;j<5;j++)
//	    	{
//    			mixedcolor_typemodel[i][j].r=set_block_color_r;
//    			mixedcolor_typemodel[i][j].g=set_block_color_g;
//    			mixedcolor_typemodel[i][j].b=set_block_color_b;
//    			mixedcolor_typemodel[i][j].rect.setColor((float)set_block_color_r/255,(float)set_block_color_g/255,(float)set_block_color_b/255);
//	    	}	
	}
	
	private void setGameBoarderFilled()
	{
		if(currentGameBorderType == GameBorderType.GAME_BORDER_DOMINANT)
		{
			for(int j=1;j<11;j++) 
			{	
				mymodel[21][j].filled=true;
				mymodelLazy[21][j].filled=false;
			}
		    for( int i=0;i<22;i++)
		    {
		    	mymodel[i][0].filled=true;
		    	mymodelLazy[i][0].filled=false;
		    	
		    	mymodel[i][11].filled=true;
		    	mymodelLazy[i][11].filled=false;			    	
		    }
		}
		else if(currentGameBorderType == GameBorderType.GAME_BORDER_BOTH)
		{
			for(int j=1;j<11;j++) 
			{	
				mymodel[21][j].filled=true;
				mymodelLazy[21][j].filled=true;
			}
		    for( int i=0;i<22;i++)
		    {
		    	mymodel[i][0].filled=true;
		    	mymodelLazy[i][0].filled=true;
		    	
		    	mymodel[i][11].filled=true;
		    	mymodelLazy[i][11].filled=true;	
		    }

		}
		else if(currentGameBorderType == GameBorderType.GAME_BORDER_ALTERNATING)
		{
			for(int j=1;j<11;j++) 
			{	
				if(0 == (j % 2)) //Even
				{
				    mymodel[21][j].filled=true;
				    mymodelLazy[21][j].filled=false;
				}
				else //odd
				{
				    mymodel[21][j].filled=false;
				    mymodelLazy[21][j].filled=true;					
				}
			}
		    for( int i=0;i<22;i++)
		    {
				if(0 == (i % 2)) //Even
				{
				    mymodel[i][0].filled=true;
				    mymodelLazy[i][0].filled=false;
				    
				    mymodel[i][11].filled=true;
				    mymodelLazy[i][11].filled=false;
				}
				else //odd
				{
				    mymodel[i][0].filled=false;
				    mymodelLazy[i][0].filled=true;	
				    
				    mymodel[i][11].filled=false;
				    mymodelLazy[i][11].filled=true;	
				}
		    }
		}
		else if(currentGameBorderType == GameBorderType.GAME_BORDER_MIXED)
		{
			int remainder;
			
			for(int j=1;j<11;j++) 
			{	
				remainder = j % 3;
				if(remainder == 0) //0 Dominant only
				{
				    mymodel[21][j].filled=true;
				    mymodelLazy[21][j].filled=false;
				}
				else if(remainder == 1)//1 Mixed
				{
				    mymodel[21][j].filled=true;
				    mymodelLazy[21][j].filled=true;					
				}
				else //2 //Lazy only
				{
				    mymodel[21][j].filled=false;
				    mymodelLazy[21][j].filled=true;					
				}
			}
		    for( int i=0;i<22;i++)
		    {
				remainder = i % 3;
				if(remainder == 0) //0 Dominant only
				{
					mymodel[i][0].filled=true;
				    mymodelLazy[i][0].filled=false;
				    
					mymodel[i][11].filled=true;
				    mymodelLazy[i][11].filled=false;
				}
				else if(remainder == 1)//1 Mixed
				{
					mymodel[i][0].filled=true;
				    mymodelLazy[i][0].filled=true;	
				    
					mymodel[i][11].filled=true;
				    mymodelLazy[i][11].filled=true;
				}
				else //2 //Lazy only
				{
					mymodel[i][0].filled=false;
				    mymodelLazy[i][0].filled=true;
				    
					mymodel[i][11].filled=false;
				    mymodelLazy[i][11].filled=true;
				}
			}
		}	
	}
	
	private void setStaticBlockFilledLazy()
	{
		//Game board
		//Static blocks are always all turned on
		
		if(currentMixColorType == MixColorType.MIXCOLOR_STATIC_ONLY)
		{
			this.setStaticBlockFilledMixed();
		}
		else if(currentMixColorType == MixColorType.MIXCOLOR_STATIC_MOVING)
		{
			// Both eyes can see all static blocks set lazy eye blocks visible
			for(int i=0;i<21;i++)
				for(int j=1;j<11;j++)
				{
					mymodelLazy[i][j].filled = mymodel[i][j].filled;
				}
			
		}
//		else if (currentMixColorType == MixColorType.MIXCOLOR_NONE || 
//				currentMixColorType == MixColorType.MIXCOLOR_MOVING_ONLY)				
		else
		{
			// None of the static blocks are visible to the lazy eye
			for(int i=0;i<21;i++)
				for(int j=1;j<11;j++)
				{
					mymodelLazy[i][j].filled = false;
				}
		}


	}

	private void setStaticBlockFilledMixed()
	{
		//Need to mix the colors of all blocks two rows below the top level of blocks
		//Top row is index 1, left most column is index 1
		
		int[] TopRow = new int[11]; //Top row in each column
		
		// Default lazy visible blocks to the same as the fellow eye.
		for(int i=0;i<21;i++)
			for(int j=1;j<11;j++)
			{
				mymodelLazy[i][j].filled = mymodel[i][j].filled;
			}
		
		//Find the first row with filled blocks
		for(int j=1;j<11;j++)
		{
			TopRow[j] = -1; //Default to no blocks in this column
			for(int i=1;i<21;i++)			
			{
				if(mymodel[i][j].filled)
				{
					TopRow[j] = i;
					break; //Top row found, so go the next column
				}
			}
		}
		
		int rowEnd;
		for(int j=1;j<11;j++)
		{
			if(TopRow[j] > 0)
			{
				rowEnd = TopRow[j]+2;
				if(rowEnd > 21) //Don't change the game border fill values
				{
					rowEnd = 21;
				}
				for(int i=TopRow[j];i<rowEnd;i++)			
				{
					//Top two rows visible to fellow eye only
					if(mymodel[i][j].filled)
					{
						//Turn off top two rows to lazy eye.
						mymodelLazy[i][j].filled = false;
					}
				}
//				for(int i=TopRow[j]+2;i<21;i++)			
//				{
//					if(mymodel[i][j].filled)
//					{
//						//Blocks visible to lazy eye too.
//						mymodelLazy[i][j].filled = true;
//					}
//				}
			}
		}
	}

	private void setFilledCurrentTypeModel()
	{
		spiriteModel [][] typemodelFilled = this.get_ship_by_index(current);
		
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
			{ 
		        current_typemodel_filled[i][j].filled = typemodelFilled[i][j].filled;
			}
		
		if (currentMixColorType == MixColorType.MIXCOLOR_NONE || 
				currentMixColorType == MixColorType.MIXCOLOR_STATIC_ONLY)
		{
			for(int i=0;i<5;i++)
				for(int j=0;j<5;j++)
				{   
					current_typemodel[i][j].filled=false;
					current_typemodel_lazy[i][j].filled = typemodelFilled[i][j].filled;
				}
		}
//					else if(currentMixColorType == MixColorType.MIXCOLOR_MOVING_ONLY || 
//							currentMixColorType == MixColorType.MIXCOLOR_STATIC_MOVING)
//					{
		else // Mix the colors of the falling ship
		{
			//Clear all blocks first
			for(int i=0;i<5;i++)
				for(int j=0;j<5;j++)
				{
					current_typemodel[i][j].filled=false;
					current_typemodel_lazy[i][j].filled=false;
				}
			
   
			for(int i=0;i<5;i++)
				for(int j=0;j<5;j++)
				{
					if(typemodelFilled[i][j].filled)
					{
						if(typemodelFilled[i][j].currentColorType == spriteColorType.SPRITECOLOR_MOVING)
						{
							current_typemodel_lazy[i][j].filled=true;
						}	
						else if(typemodelFilled[i][j].currentColorType == spriteColorType.SPRITECOLOR_STATIC)
						{
					        current_typemodel[i][j].filled=true;
						}
						else //Mixed, blocks to both eyes are visible.
						{
					        current_typemodel[i][j].filled=true;
					        current_typemodel_lazy[i][j].filled=true;								
						}
						
					}
				}				

			//Turn blocks for lazy and fellow eye
//			int RandomColorMix;
//			for(int i=0;i<5;i++)
//				for(int j=0;j<5;j++)
//				{
//					if(typemodelFilled[i][j].filled)
//					{	
//						RandomColorMix = randomGeneratorSetBlockColor.nextInt(3);
//			            //Some blocks amblyopic eye only, some blocks fellow eye only, and some blocks both eyes.
//						if(RandomColorMix == 0)
//						{//fellow eye only	
//							current_typemodel[i][j].filled=true;
//						}
//						else if(RandomColorMix == 1)
//						{//lazy eye only
//							current_typemodel_lazy[i][j].filled=true;
//						}
//						else //2
//						{//both eyes
//							current_typemodel[i][j].filled=true;
//							current_typemodel_lazy[i][j].filled=true;
//						}
//					}
//				}

		}
		
	}
	
	private void setFilledNextTypeModel()
	{
        spiriteModel [][] _typemodel = null;		
		_typemodel = this.get_ship_by_index(next);
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
			{
				next_typemodel[i][j].filled=_typemodel[i][j].filled;
				//Static color only for now
				next_typemodel_lazy[i][j].filled = false;
			}
	}	
	
	//****************************************************************************
	public void paint_scene()
	{
		int SceneChildCount=0;
		int FellowChildCount = 0;
		int LazyChildCount = 0;
//		myscene.setBackground(new Background(0, 0, 0));
		//Set scene background white
		//Doesn't work the background must be black
//		myscene.setBackground(new Background(255f, 255f, 255f));
		//
	//	Rectangle rectangle;
		
		if(first_time==true)
		{
			first_time=false;
			//Keep track of the index of each sprite, block, so that it can be set
			//Visible and invisible

			SceneChildCount = (myscene.getChildCount()-1);
			FellowChildCount = (fellowEyeLayer.getChildCount()-1);
			LazyChildCount = (lazyEyeLayer.getChildCount()-1);
			
			myscene.attachChild(fellowEyeLayer);
			SceneChildCount++;
			myscene.attachChild(lazyEyeLayer);
			SceneChildCount++;
			
			//Fellow Eye Game board
			for(int i=0;i<22;i++)
				for(int j=0;j<12;j++)
				{
					fellowEyeLayer.attachChild(mymodel[i][j].rect);
					FellowChildCount++;
			        mymodel[i][j].SceneIndex = FellowChildCount;
				}

			//Lazy Eye Game board
			for(int i=0;i<22;i++)
				for(int j=0;j<12;j++)
				{
					lazyEyeLayer.attachChild(mymodelLazy[i][j].rect);
					LazyChildCount++;
			        mymodelLazy[i][j].SceneIndex = LazyChildCount;
				}

			
			//Next ship is attached to the main scene			
			//Next Falling Piece
			for(int i=0;i<5;i++)
		    	for(int j=0;j<5;j++)
		    	{
		    		myscene.attachChild(next_typemodel[i][j].rect);
		    		SceneChildCount++;
		    		next_typemodel[i][j].SceneIndex = SceneChildCount;
		    	}
			
			//Next Falling Piece
			for(int i=0;i<5;i++)
		    	for(int j=0;j<5;j++)
		    	{
		    		myscene.attachChild(next_typemodel_lazy[i][j].rect);
		    		SceneChildCount++;
		    		next_typemodel_lazy[i][j].SceneIndex = SceneChildCount;
		    	}			


			
//			//Test piece
//			for(int i=0;i<5;i++)
//		    	for(int j=0;j<5;j++)
//		    	{
//		    		fellowEyeLayer.attachChild(mixedcolor_typemodel[i][j].rect);
//		    		ChildCountCurrent++;
//		    		mixedcolor_typemodel[i][j].SceneIndex = ChildCountCurrent;
//		    	}
					
			// Attach current model last so that it is on top of all the other sprites on the scene.
			// Fellow Eye Current falling object 
			for(int i=0;i<5;i++)
		    	for(int j=0;j<5;j++)
		    	{
		    		fellowEyeLayer.attachChild(current_typemodel[i][j].rect);
		    		FellowChildCount++;
		    		current_typemodel[i][j].SceneIndex = FellowChildCount;
		    	}
					
			// Attach current model last so that it is on top of all the other sprites on the scene.
			// Lazy Current falling object
			for(int i=0;i<5;i++)
		    	for(int j=0;j<5;j++)
		    	{
		    		lazyEyeLayer.attachChild(current_typemodel_lazy[i][j].rect);
		    		LazyChildCount++;
		    		current_typemodel_lazy[i][j].SceneIndex = LazyChildCount;
		    	}
		}
		

          lazyEyeLayer.setPosition(fallingOffsetX + boardOffsetX, fallingOffsetY + boardOffsetY);
          fellowEyeLayer.setPosition(staticOffsetX + boardOffsetX, staticOffsetY + boardOffsetY);
		
		  for(int i=0;i<5;i++)
		    	for(int j=0;j<5;j++)
		    	{
		    		if(current_typemodel[i][j].filled==true) 
		    	    {
//		    			current_typemodel[i][j].rect.setPosition(fallingOffsetX + boardOffsetX + (j+centerPosY)*(BLOCK_WIDTH+1), fallingOffsetY + boardOffsetY + (i+centerPosX)*(BLOCK_HEIGHT+1));
//		   		    	myscene.getChildByIndex(current_typemodel[i][j].SceneIndex).setVisible(true);
		    			current_typemodel[i][j].rect.setPosition((j+centerPosY)*(BLOCK_WIDTH+1), (i+centerPosX)*(BLOCK_HEIGHT+1));
		    			fellowEyeLayer.getChildByIndex(current_typemodel[i][j].SceneIndex).setVisible(true);
		    	    }
		    		else {	
		    			
//	    			    myscene.getChildByIndex(current_typemodel[i][j].SceneIndex).setVisible(false);
		    			fellowEyeLayer.getChildByIndex(current_typemodel[i][j].SceneIndex).setVisible(false); 
		    		} 

			  		if(current_typemodel_lazy[i][j].filled==true) 
				    {
			  			current_typemodel_lazy[i][j].rect.setPosition((j+centerPosY)*(BLOCK_WIDTH+1), (i+centerPosX)*(BLOCK_HEIGHT+1));
			  			lazyEyeLayer.getChildByIndex(current_typemodel_lazy[i][j].SceneIndex).setVisible(true);
				    }
					else {	
						lazyEyeLayer.getChildByIndex(current_typemodel_lazy[i][j].SceneIndex).setVisible(false); 
					} 
		    	}
		  for(int i=0;i<5;i++)
		    for(int j=0;j<5;j++)
		    {
	            //Test mixed color blocks
		    	//Mixed blocks work. One eye sees one set of blocks the other eye sees the other
//		    	myscene.getChildByIndex(mixedcolor_typemodel[i][j].SceneIndex).setVisible(true);
		    	//Hide the mixed color blocks
//		    	myscene.getChildByIndex(mixedcolor_typemodel[i][j].SceneIndex).setVisible(false);
		    	
		    	//Set the blocks that are visible. Don't need to move blocks
		    	myscene.getChildByIndex(next_typemodel[i][j].SceneIndex).setVisible(next_typemodel[i][j].filled);
		    	myscene.getChildByIndex(next_typemodel_lazy[i][j].SceneIndex).setVisible(next_typemodel_lazy[i][j].filled);
		    	
//		    	if(next_typemodel[i][j].filled==true) 
//		    	{
//		    	    myscene.getChildByIndex(next_typemodel[i][j].SceneIndex).setVisible(true);
//		    	}
//		    	else {
//		    		myscene.getChildByIndex(next_typemodel[i][j].SceneIndex).setVisible(false);    
//		    	}
		    }
		
	    for(int i=0;i<22;i++)
	    	for(int j=0;j<12;j++)
	    	{
	    		fellowEyeLayer.getChildByIndex(mymodel[i][j].SceneIndex).setVisible(mymodel[i][j].filled);
			    if(mymodel[i][j].filled==true) 
				{
			    	mymodel[i][j].rect.setPosition(j*(BLOCK_WIDTH+1), i*(BLOCK_HEIGHT+1));
				}
			    
			    lazyEyeLayer.getChildByIndex(mymodelLazy[i][j].SceneIndex).setVisible(mymodelLazy[i][j].filled);
			    if(mymodelLazy[i][j].filled==true) 
				{
			    	mymodelLazy[i][j].rect.setPosition(j*(BLOCK_WIDTH+1), i*(BLOCK_HEIGHT+1));
				}
	    	}
//	    		if(mymodel[i][j].filled==true) 
//	    		{	    			
////	    			mymodel[i][j].rect.setPosition(boardOffsetX + j*(BLOCK_WIDTH+1), boardOffsetY + i*(BLOCK_HEIGHT+1));
////	    			myscene.getChildByIndex(mymodel[i][j].SceneIndex).setVisible(true);/**/
//	    		}
//	    		else {	  			
////	    			myscene.getChildByIndex(mymodel[i][j].SceneIndex).setVisible(false);
//	    		}	    
	}

	//*********************************************************************************
	
	public spiriteModel[][] get_ship_by_index(int ShipIndex)
	{
		spiriteModel [][] typemodel = null;
		switch (ShipIndex)
		{
		case 0:
			typemodel = typemodel0;
			break;
		case 1:
			typemodel = typemodel1;
			break;
		case 2:
			typemodel = typemodel2;
			break;
		case 3:
			typemodel = typemodel3;
			break;
		case 4:
			typemodel = typemodel4;
			break;
		case 5:
			typemodel = typemodel5;
			break;
		case 6:
			typemodel = typemodel6;
			break;
		case 7:
			typemodel = typemodel7;
			break;
		case 8:
			typemodel = typemodel8;
			break;
		case 9:
			typemodel = typemodel9;
			break;
		case 10:
			typemodel = typemodel10;
			break;
		case 11:
			typemodel = typemodel11;
			break;
		case 12:
			typemodel = typemodel12;
			break;
		case 13:
			typemodel = typemodel13;
			break;
		}
		return typemodel;
	}
	
	public void generate_new_ship()
	{   
		no_chip=false;// we have now one ship	
		current=next;
		if(this.extrat_mode==true)  next = randomGenerator.nextInt(13);	
								else next = randomGenerator.nextInt(7);	
		
		copy_to_current_ship();
		copy_to_next_ship();
		
//		switch (current)
//		{
//		case 0:
//			copy_to_current_ship(typemodel0);
//			break;
//		case 1:
//			copy_to_current_ship(typemodel1);
//			break;
//		case 2:
//			copy_to_current_ship(typemodel2);
//			break;
//		case 3:
//			copy_to_current_ship(typemodel3);
//			break;
//		case 4:
//			copy_to_current_ship(typemodel4);
//			break;
//		case 5:
//			copy_to_current_ship(typemodel5);
//			break;
//		case 6:
//			copy_to_current_ship(typemodel6);
//			break;
//		case 7:
//			copy_to_current_ship(typemodel7);
//			break;
//		case 8:
//			copy_to_current_ship(typemodel8);
//			break;
//		case 9:
//			copy_to_current_ship(typemodel9);
//			break;
//		case 10:
//			copy_to_current_ship(typemodel10);
//			break;
//		case 11:
//			copy_to_current_ship(typemodel11);
//			break;
//		case 12:
//			copy_to_current_ship(typemodel12);
//			break;
//		case 13:
//			copy_to_current_ship(typemodel13);
//			break;
//		}
	//********************
//		switch (next)
//		{
//		case 0:
//			copy_to_next_ship(typemodel0);
//			break;
//		case 1:
//			copy_to_next_ship(typemodel1);
//			break;
//		case 2:
//			copy_to_next_ship(typemodel2);
//			break;
//		case 3:
//			copy_to_next_ship(typemodel3);
//			break;
//		case 4:
//			copy_to_next_ship(typemodel4);
//			break;
//		case 5:
//			copy_to_next_ship(typemodel5);
//			break;
//		case 6:
//			copy_to_next_ship(typemodel6);
//			break;
//		case 7:
//			copy_to_next_ship(typemodel7);
//			break;
//		case 8:
//			copy_to_next_ship(typemodel8);
//			break;
//		case 9:
//			copy_to_next_ship(typemodel9);
//			break;
//		case 10:
//			copy_to_next_ship(typemodel10);
//			break;
//		case 11:
//			copy_to_next_ship(typemodel11);
//			break;
//		case 12:
//			copy_to_next_ship(typemodel12);
//			break;
//		case 13:
//			copy_to_next_ship(typemodel13);
//			break;
//		}	
			
	}

	//************************************************
	private void copy_to_current_ship() 
	{
//		int r,g,b;
//		r=255;g=255;b=255;//Choose random color
//		
//		
//		/*if(cocolor<0)cocolor*=-1;
//		
//		long coc= System.currentTimeMillis()%10;
//		cocolor=(int) coc;*/
//		switch (cocolor)
//		{
//		case 0: r=254;g=127;b=39;
//		break;
//		case 1: r=254;g=242;b=0;
//		break;
//		case 2: r=181;g=230;b=29;
//		break;
//		case 3: r=254;g=201;b=14;
//		break;
//		case 4: r=0;g=162;b=232;
//		break;
//		case 5: r=163;g=73;b=164;
//		break;
//		case 6: r=255;g=174;b=201;
//		break;
//		//case 7: r=254;g=255;b=0;
//		case 7: r=0;g=0;b=255; //Blue
//		break;
//		case 8: r=falling_color_r;g=falling_color_g;b=falling_color_b; //red
//		break;
//		case 9: r=0;g=245;b=0;
//		break;
//		case 10: r=255;g=255;b=255;
//		break;
//		
//		}
				
		//****************************Fill the model and it's color

//		for(int i=0;i<5;i++)
//			for(int j=0;j<5;j++)
//			{   
//				current_typemodel_filled[i][j].filled=_typemodel[i][j].filled;
//			}			
		
		this.setFilledCurrentTypeModel(); //Set the color of the blocks
		
		//*********give it an initial position
		centerPosX=0;
		//centerPosX=BOARD_HEIGHT_OFFSET;
		centerPosY=4;
		
		//r=181;g=230;b=29;
		//cocolor = randomGeneratorColor.nextInt(10);
		cocolor = 8; //red
		
	}
	
	//************************************
	private void copy_to_next_ship() 
	{	
		this.setFilledNextTypeModel();		
	}
	//************************************
	public void rotate_ship()
	{	
		//Get all filled blocks for the ship
//        spiriteModel [][] typemodelFilled = this.get_ship_by_index(current);

        //Created rotated version of the fellow eye, lazy eye and combined ships
		int x1,y2;
		x1=4;y2=4;
		for(int j=4;j>=0;j--)
			for(int i=0;i<5;i++)			
			{
				temp_current_typemodel_filled[x1][y2].filled=current_typemodel_filled[i][j].filled;				
				temp_current_typemodel[x1][y2].filled=current_typemodel[i][j].filled;
				temp_current_typemodel_lazy[x1][y2].filled=current_typemodel_lazy[i][j].filled;							
				y2--;
				if(y2<0){x1--;y2=4;}
				
			}
		
	
		//Make sure the model can be rotated.
		//Cancel the rotation if
		boolean ok=true;
	    for(int i=0;i<5;i++)
		    for(int j=0;j<5;j++)
		    {
		        if(temp_current_typemodel_filled[i][j].filled && 
		          (mymodel[i+this.centerPosX][j+this.centerPosY].filled || mymodelLazy[i+this.centerPosX][j+this.centerPosY].filled))	
		        {
			        ok=false;break;	
		        }
		    }
	    
		if(ok==false)return;
		
		resourcesManager.playMoveShipSound();
		
		//Set the current fellow eye and lazy eye model equal to the rotated model
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
			{
				current_typemodel_filled[i][j].filled=temp_current_typemodel_filled[i][j].filled;
				current_typemodel[i][j].filled=temp_current_typemodel[i][j].filled;
				current_typemodel_lazy[i][j].filled=temp_current_typemodel_lazy[i][j].filled;	
			}		
	}
	
	public void clear_board()
	{
		calculate_is_performed=true;
		for(int i=1;i<21;i++)
		    for(int j=1;j<11;j++)
		    {
		    	mymodel[i][j].filled=false;
		        mymodelLazy[i][j].filled=false;
		    }
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
			{
				current_typemodel[i][j].filled=false;
				current_typemodel_lazy[i][j].filled=false;
				current_typemodel_filled[i][j].filled=false;
			}
		
		//Clear both current and next ship
		this.generate_new_ship();
		this.generate_new_ship();
		this.paint_scene();   		
		game_paused=true;
		score=0;lines=0;
		//Reset Auto Speed back to what it was at the beginning of the game.
		this.rowsSinceLastSpeedUp = 0;
		activity.SpeedWaitDecrease = 0;
		activity.setGameSpeedCurrent();
	}
	public void chape_cant_down()
	{	
		/******************if the position is 0,0 so game over---> init all data**/	
		if(this.centerPosX==0){
			resourcesManager.playGameOverSound();
			clear_board();

			//Game Over User decides what to do next
			myscene.loadPauseMenuScene();
			return;
		}
		/************************/
		
		//Get all filled blocks for the ship
//        spiriteModel [][] typemodelFilled = this.get_ship_by_index(current);

		no_chip=true;calculate_is_performed=true;
		int next_score=100;
//		int r,g,b;
//		r=set_block_color_r;
//		g=set_block_color_g;
//		b=set_block_color_b;
//		
//		//Mix and match left and right colors for the set blocks to 
//		//force each eye to work together to see the fallen blocks too.
//		int color_choice = randomGeneratorSetBlockColor.nextInt(2);
//		if(set_block_mix_colors && (0 < color_choice))
//		{
//			r=falling_color_r;
//			g=falling_color_g;
//			b=falling_color_b;
//		}
		
		//Set the static blocks under the current falling block item to filled
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
			{
				if(current_typemodel_filled[i][j].filled)
				{
					//Fellow eye static blocks are always visible
				    mymodel[centerPosX+i][centerPosY+j].filled=true;
				    //Lazy eye blocks are set further down in this function
				}
			}
		
		//Check for rows that are completely filled and need to be deleted
		boolean ok;
		boolean all_checked;
		all_checked=true;
		while(all_checked)
		{ 
			all_checked=false;
		    for(int i=20;i>0;i--)			
			{
				//Check for filled rows
				ok=true;
				for(int j=1;j<11;j++)
				{
				if(mymodel[i][j].filled==false)	{ok=false;break;}
				}	
			    
				//Row is filled, so it needs to be deleted.
				if(ok==true)
				{   
					resourcesManager.playRowCompleteSound();
					lines++;
					score+=next_score;
					next_score+=100;
					rowsSinceLastSpeedUp++;
					if(rowsSinceLastSpeedUp >= ROWS_BEFORE_SPEED_UP)
					{
						rowsSinceLastSpeedUp = 0;
						activity.increasGameSpeed(SPEED_UP_PERCENTAGE);
					}
					
					all_checked=true;
					for(int k=i;k>0;k--)
					{
						for(int kj=1;kj<11;kj++)
						{
							mymodel[k][kj].filled=mymodel[k-1][kj].filled;
							if(mymodel[k][kj].filled==true)
							{
								mymodel[k][kj].currentColorType = mymodel[k-1][kj].currentColorType;
								mymodel[k][kj].refreshCurrentColor();
								
//								mymodel[k][kj].r=mymodel[k-1][kj].r;
//								mymodel[k][kj].g=mymodel[k-1][kj].g;
//								mymodel[k][kj].b=mymodel[k-1][kj].b;
//								mymodel[k][kj].rect.setColor((float)mymodel[k][kj].r/255, (float)mymodel[k][kj].g/255, (float)mymodel[k][kj].b/255);
							}
						}
						
					}
					for(int kj=1;kj<11;kj++)
					{
						mymodel[0][kj].filled=false;
					}
					
				}//if ok==true			
			}//for i	
		}//while
		
		//This sets which lazy eye static blocks are visible
		this.setStaticBlockFilledLazy();
		
//		if(currentMixColorType == MixColorType.MIXCOLOR_STATIC_ONLY)
//		{
//			this.setStaticBlockFilledMixed();
//		}
		
		resourcesManager.playShipDownSound();
		this.generate_new_ship();
		this.paint_scene();
		calculate_is_performed=false;
    }
		
    public boolean game_started;
    public boolean game_paused;

// ===IMPLEMENTED INTERFACE===//
//@Override
//public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//	this.move_to_right_Ship();
//	return true;
//}

}//my class









