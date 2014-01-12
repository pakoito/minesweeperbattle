package com.zengate.minesweeperbattle;

import com.zengate.minesweeperbattle.Engine.RenderableEntity;
import com.zengate.minesweeperbattle.Engine.SceneManager;

public class Boarder extends RenderableEntity {

	public Boarder(int _index){
		super("Boarder");
		visible = true;
		
		switch (_index){
			case 0:
				setUpTexture("sprBoarder_T_L","data/pack0/pack0.pack");
				break;
			case 1:
				setUpTexture("sprBoarder_T_M","data/pack0/pack0.pack");
				break;
			case 2:
				setUpTexture("sprBoarder_T_R","data/pack0/pack0.pack");
				break;
			case 3:
				setUpTexture("sprBoarder_R_T","data/pack0/pack0.pack");
				break;
			case 4:
				setUpTexture("sprBoarder_R_M","data/pack0/pack0.pack");
				break;
			case 5:
				setUpTexture("sprBoarder_R_B","data/pack0/pack0.pack");
				break;
			case 6:
				setUpTexture("sprBoarder_B_L","data/pack0/pack0.pack");
				break;
			case 7:
				setUpTexture("sprBoarder_B_M","data/pack0/pack0.pack");
				break;
			case 8:
				setUpTexture("sprBoarder_B_R","data/pack0/pack0.pack");
				break;
			case 9:
				setUpTexture("sprBoarder_L_T","data/pack0/pack0.pack");
				break;
			case 10:
				setUpTexture("sprBoarder_L_M","data/pack0/pack0.pack");
				break;
			case 11:
				setUpTexture("sprBoarder_L_B","data/pack0/pack0.pack");
				break;
			case 12:
				setUpTexture("sprBoarderCorner_TL","data/pack0/pack0.pack");
				break;
			case 13:
				setUpTexture("sprBoarderCorner_TR","data/pack0/pack0.pack");
				break;
			case 14:
				setUpTexture("sprBoarderCorner_BL","data/pack0/pack0.pack");
				break;
			case 15:
				setUpTexture("sprBoarderCorner_BR","data/pack0/pack0.pack");
				break;
		
		}
		
		SceneManager.Scene().addEntity(this);
	}
}
