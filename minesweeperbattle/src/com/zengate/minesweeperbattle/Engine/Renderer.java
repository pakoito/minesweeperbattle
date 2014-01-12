package com.zengate.minesweeperbattle.Engine;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
public class Renderer {
	
	private static OrthographicCamera camera;
	private static SpriteBatch spriteBatch;
	private static ShapeRenderer theShapeRenderer = new ShapeRenderer();
	
	//pixels per unit
	private static float ppux;
	private static float ppuy;
	
	private static Array<RenderSprite> renderPool; 
	private static Array<RenderSprite> renderList;
	private static Array<RenderShape> rectangleList;
	private static Array<RenderShape> rectangleFilledList;
	private static Array<RenderShape> triangleFilledList;
	private static Array<RenderShape> lineList;
	private static Array<RenderText> textList;
	private static Array<RenderText> screenTextList;
	private static Array<RenderText> textPool;
	private static Array<RenderShape> renderShapePool;
	
	private static Vector2 cameraSize = new Vector2();
	
	private static Array<String> layerOrder;
	private static Array<Layer> layers;
	
	private static int w1;
	private static int h1;
	private static float aspect;
	private static float invAspect;
	
	private static Vector2 winPos = new Vector2();
	private static Vector2 screenScale = new Vector2();
	private static Vector2 screenEdge = new Vector2();
	
	private static Vector2 cameraPos = new Vector2();
	
	private static Vector2 cameraScale = new Vector2();
	
	public static void Init(int cameraWidth, int cameraHeight){
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		  
		cameraSize.x = cameraWidth; 
		cameraSize.y = cameraHeight;
		
		//calculate pixels per unit
		ppux = w / cameraSize.x;
		ppuy = h / cameraSize.y;
		
		camera = new OrthographicCamera(cameraWidth,  cameraHeight);
		
		spriteBatch = new SpriteBatch(200);
		
		renderPool = new Array<RenderSprite>();
		renderList = new Array<RenderSprite>();
		rectangleList = new Array<RenderShape>();
		rectangleFilledList = new Array<RenderShape>();
		triangleFilledList = new Array<RenderShape>();
		lineList = new Array<RenderShape>();
		textList = new Array<RenderText>();
		screenTextList = new Array<RenderText>();
		textPool = new Array<RenderText>();
		renderShapePool = new Array<RenderShape>();
		layerOrder = new Array<String>();
		layers = new Array<Layer>();
		
		cameraPos.x = 0;
		cameraPos.y = 0;
		
		loadLayers();
		scaleAspectRation();
		Gdx.graphics.setVSync(false);
	}
	
	static private void loadLayers()
	{
		addLayer("BackGround", true);
		addLayer("SectionBackGroundFurther1", true);
		addLayer("Turbines", true);
		addLayer("SectionBackGroundFurther", true);
		addLayer("SectionBackGround", true);
		addLayer("SmallWalls" , true);
		addLayer("Default", true);
		addLayer("Player", true);
		addLayer("CloudFront", true);
		addLayer("Walls", true);
		addLayer("Bullets", true);
		addLayer("HUD" , true);
		addLayer("PauseMenu" , true);
	}

	static void addLayer(String name, Boolean active)
	{
		layers.add(new Layer(name, active));
		layerOrder.add(name);
	}
	
	static void removeSprite(RenderSprite _aRs){
		renderList.removeValue(_aRs,true);
	}
	
	static RenderSprite createRenderSprite(){
		if (renderPool.size > 0){
			RenderSprite aRs = renderPool.get(0);
			renderPool.removeIndex(0);
			return aRs;
		}else{
			return new RenderSprite();	
		}
	}
	
	static RenderShape createRenderShape(){
		if (renderShapePool.size > 0){
			RenderShape aRs = renderShapePool.get(0);
			renderShapePool.removeIndex(0);
			return aRs;
		}else{
			return new RenderShape();	
		}
	}
	
	static RenderText createRenderText(){
		if (textPool.size > 0){
			RenderText aTxt = textPool.get(0);
			textPool.removeIndex(0);
			return aTxt;
		}else{
			return new RenderText();	
		}
	}
	
	public static void Draw(){
		Gdx.gl.glClearColor(0.274509f, 00.270588f, 00.270588f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//Gdx.graphics.setVSync(true);
		Gdx.gl.glViewport((int) winPos.x, (int) winPos.y,w1, h1);
		//Draw Sprites
		camera.update();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		//spriteBatch.begin();
		
		//System.out.println("RenderList size: " +renderList.size);
		/*for (RenderSprite aRs : renderList){
			renderSprite(aRs);
			renderPool.add(aRs);
		}
		renderList.clear();*/
		
		for(String layer : layerOrder)
		{
			drawLayer(layers.get(layerOrder.indexOf(layer, true)));
		}
		
		spriteBatch.begin();
		for (RenderText aText : textList){
			renderText(aText);
			textPool.add(aText);
		}
		
		textList.clear();
		
		for (RenderText aText : screenTextList){
			renderScreenText(aText);
			textPool.add(aText);
		}
		
		screenTextList.clear();
		
		spriteBatch.end();
		
		//System.out.println("Render Calls: " +spriteBatch.renderCalls);
		//System.out.println("max in batch: " +spriteBatch.maxSpritesInBatch);
		
		//Draw Rectangles
		theShapeRenderer.setProjectionMatrix(camera.combined);
		theShapeRenderer.begin(ShapeType.Filled);
		for (RenderShape aRect : rectangleList){
			renderRectangle(aRect);
			renderShapePool.add(aRect);
		}
		rectangleList.clear();
		theShapeRenderer.end();
		
		theShapeRenderer.begin(ShapeType.Line);
		for (RenderShape aLine : lineList){
			renderLine(aLine);
			renderShapePool.add(aLine);
		}
		lineList.clear();
		theShapeRenderer.end();
		
		//System.out.println("filledRect num: " +rectangleFilledList.size);
		theShapeRenderer.begin(ShapeType.Filled);
		for (RenderShape aRect: rectangleFilledList){
			renderFilledRectangle(aRect);
			renderShapePool.add(aRect);
		}
		rectangleFilledList.clear();
		theShapeRenderer.end();
		

		//System.out.println("Triangle num: " +triangleFilledList.size);
		theShapeRenderer.begin(ShapeType.Filled);
		for (RenderShape aTrig: triangleFilledList){
			renderFilledTriangle(aTrig);
			renderShapePool.add(aTrig);
		}
		triangleFilledList.clear();
		theShapeRenderer.end();
	}
	
	static private void drawLayer(Layer layer)
	{
		if(layer.isActive)
		{
			spriteBatch.begin();
			for(RenderSprite sprite : layer.objects)
			{
				renderSprite(sprite);
				renderPool.add(sprite);
			}
			spriteBatch.end();
			layer.objects.clear();
			
			theShapeRenderer.begin(ShapeType.Filled);
			for (RenderShape aTrig: layer.filledTriangles){
				renderFilledTriangle(aTrig);
				renderShapePool.add(aTrig);
			}
			layer.filledTriangles.clear();
			theShapeRenderer.end();
		}
	}
	
	static private void renderSprite(RenderSprite _aRs){
		spriteBatch.setColor(_aRs.getColor().r, _aRs.getColor().b, _aRs.getColor().g,_aRs.getAlpha());//new Color(1,1,1,_aRs.getAlpha()));
		spriteBatch.draw(_aRs.getTextureRegion(),
		    (cameraPos.x + _aRs.getPosition().x - _aRs.getOrigin().x),  								// x position
			(cameraPos.y + _aRs.getPosition().y +  _aRs.getSourceSize().y -_aRs.getOrigin().y),  		// y position
			_aRs.getOrigin().x,  														// origin x
			- _aRs.getOrigin().y , 														// origin y
			_aRs.getSourceSize().x, 													// width 
			-_aRs.getSourceSize().y, 													// height
			_aRs.getXScale(),															// scale x
			_aRs.getYScale(),															// scale y
			_aRs.getRotation() 															// rotation	
		);
	}
	
	static private void renderRectangle(RenderShape _aRect){
		theShapeRenderer.setColor(_aRect.getRed(),_aRect.getGreen(),_aRect.getBlue(),1);
		theShapeRenderer.rect(_aRect.getPosition().x , _aRect.getPosition().y ,
				_aRect.getSize().x , _aRect.getSize().y);
	}
	
	static private void renderFilledRectangle(RenderShape _aRect){
		theShapeRenderer.setColor(_aRect.getRed(),_aRect.getGreen(),_aRect.getBlue(),1);
		//PLUSING 32 ON Y IS A REAL BAD IDEA
		theShapeRenderer.rect(_aRect.getPosition().x, _aRect.getPosition().y+32,
				_aRect.getSize().x , -_aRect.getSize().y);

	}
	
	static private void renderFilledTriangle(RenderShape _aTrig){
		theShapeRenderer.setColor(_aTrig.getRed(),_aTrig.getGreen(),_aTrig.getBlue(),1);

		theShapeRenderer.triangle(_aTrig.getPoint0().x,_aTrig.getPoint0().y,
				_aTrig.getPoint1().x, _aTrig.getPoint1().y,
				_aTrig.getPoint2().x, _aTrig.getPoint2().y);

	}
	
	static private void renderLine(RenderShape _aLine){
		theShapeRenderer.setColor(1,0,0,1);
		theShapeRenderer.line(_aLine.getL1().x, _aLine.getL1().y , _aLine.getL2().x, _aLine.getL2().y);
	}
	
	static private void renderText(RenderText _aText){
		BitmapFont font = ContentManager.getFont(_aText.getFont());
		font.setColor(_aText.getRed(),_aText.getGreen(),_aText.getBlue(),_aText.getAlpha());
		font.draw(spriteBatch, _aText.getText(), cameraPos.x +_aText.getPosition().x,cameraPos.y + _aText.getPosition().y);
	}
	
	static private void renderScreenText(RenderText _aText){
		BitmapFont font = ContentManager.getFont(_aText.getFont());
		font.setColor(_aText.getRed(),_aText.getGreen(),_aText.getBlue(),_aText.getAlpha());
		font.draw(spriteBatch, _aText.getText(), _aText.getPosition().x, _aText.getPosition().y);
	}
	
	static public void resizeRenderer(int width, int height){
        camera = new OrthographicCamera(cameraSize.x , cameraSize.y);
		camera.setToOrtho(true, cameraSize.x , cameraSize.y );
		
		scaleAspectRation();
	}
	
	static private void scaleAspectRation(){
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		//aspect ratio scaling needs cleaning up
		aspect = cameraSize.x / cameraSize.y;
		invAspect = 1/aspect;
		
		if ((w/h) >= (cameraSize.x/ cameraSize.y)){
			w1 = (int) (h * aspect);
			h1 = (int) h;
		}else if ((h/w) > (cameraSize.y / cameraSize.x)){
			h1 = (int) (w * (cameraSize.y / cameraSize.x));
			w1 = (int) w;
		}
		
		winPos.x  = (w - w1) /2;
		winPos.y  = (h - h1) /2;
		
		screenScale.x  = w1 / cameraSize.x;
		screenScale.y = h1 / cameraSize.y;
		
		screenEdge.x = Renderer.getCameraSize().x - Renderer.getWindowOffset().x;
		screenEdge.y =Renderer.getCameraSize().y - Renderer.getWindowOffset().y;
	}
	
	/**
	 *Get the size of the camera 
	 * @return returns camera size in pixels
	 */
	static public Vector2 getCameraSize(){
		return cameraSize;
	}
	
	static public Vector2 getWindowOffset(){
		return winPos;
	}
	
	/**
	 * A % representation of how much the renderer size is different
	 * to the window its being drawn it
	 * @return
	 */
	static public Vector2 getScreenScale(){
		return screenScale;
	}
	
	/**
	 * returns the position of the edge of the screen (before letter boxing stops)
	 * after screen scaling is applied, useful for doing GUIS and input
	 */
	static public Vector2 getScreenEdge(){
		return screenEdge;
	}
	

	static public void drawSprite(TextureRegion _region, Vector2 _pos, Rectangle _sourceRec, Vector2 _size,
			float _rotation, Vector2 _origin, float _xScale, float _yScale, float _alpha,Color _color, String layerName){
		RenderSprite aRs = createRenderSprite();
		aRs.setTextureRegion(_region);
		aRs.setPosition(_pos);
		aRs.setSourceRectangle(_sourceRec);
		aRs.setSourceSize(_size);
		aRs.setRotation(_rotation);
		aRs.setOrigin(_origin);
		aRs.setScale(_xScale, _yScale);
		aRs.setAlpha(_alpha);
		aRs.setColor(_color);
		layers.get(layerOrder.indexOf(layerName, true)).objects.add(aRs);
	}
	
	static public void drawRectangle(float _x, float _y, float _width, float _height){
		//RenderShape aShape = new RenderShape();
		RenderShape aShape = createRenderShape();
		aShape.setPosition(new Vector2(_x,_y));
		aShape.setSize(new Vector2(_width,_height));
		aShape.setColor(1, 0, 0);
		rectangleList.add(aShape);
	}
	
	static public void drawRectangle(float _x, float _y, float _width, float _height, float _red,float _green, float _blue,boolean _filled){
		//RenderShape aShape = new RenderShape();
		RenderShape aShape = createRenderShape();
		aShape.setPosition(new Vector2(_x,_y));
		aShape.setSize(new Vector2(_width,_height));
		aShape.setColor(_red, _green, _blue);
		if (!_filled){
			rectangleList.add(aShape);
		}else{
			rectangleFilledList.add(aShape);
		}
	}
	
	static public void drawTriangle(Vector2 _point0, Vector2 _point1, Vector2 _point2,String _layer, float _red,float _green, float _blue,boolean _filled){
		//RenderShape aShape = new RenderShape();
		RenderShape aShape = createRenderShape();
		aShape.setColor(_red, _green, _blue);
		aShape.setTrianglePoints(_point0, _point1, _point2);
		if (!_filled){
			System.out.println("Error: I never made it so you could draw non filled triangles");
		}else{
			//triangleFilledList.add(aShape);
			layers.get(layerOrder.indexOf(_layer, true)).filledTriangles.add(aShape);
		}
	}
	
	static public void drawRectangle(Vector2 _pos, float _width, float _height, float _red,float _green, float _blue,boolean _filled){
		RenderShape aShape = new RenderShape();
		aShape.setPosition(_pos);
		aShape.setSize(new Vector2(_width,_height));
		aShape.setColor(_red, _green, _blue);
		if (!_filled){
			rectangleList.add(aShape);
		}else{
			rectangleFilledList.add(aShape);
		}
	}
	
	static public void drawLine(Vector2 _point1, Vector2 _point2){
		RenderShape aShape = new RenderShape();
		aShape.setLine(_point1, _point2);
		lineList.add(aShape);
	}
	
	static public void drawText(String _font, String _text,Vector2 _pos, float _r, float _g, float _b, float _a){
		RenderText aText = createRenderText();
		aText.setText(_text);
		aText.setColor(_r, _g, _b, _a);
		aText.setfont(_font);
		aText.setPosition(_pos);
		textList.add(aText);
	}
	
	static public void drawScreenText(String _font, String _text,Vector2 _pos, float _r, float _g, float _b, float _a){
		RenderText aText = createRenderText();
		aText.setText(_text);
		aText.setColor(_r, _g, _b, _a);
		aText.setfont(_font);
		aText.setPosition(_pos);
		screenTextList.add(aText);
	}
	
	
	public static void setCameraPos(Vector2 _pos){
		cameraPos = _pos;
	}
}
