package com.zengate.minesweeperbattle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Input;
import com.zengate.minesweeperbattle.Engine.RenderableEntity;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Engine.SoundManager;

public class MinesweeperBattle implements ApplicationListener {
	
	private float delta;

	@Override
	public void create() {		
		ContentManager.Init();
		SceneManager.Init();
		SoundManager.Init();
		Input.Init();
		Renderer.Init(960, 512);
		LocalValues.Init();
		
		LoadContent theContent = new LoadContent();

	}

	@Override
	public void dispose() {
		SoundManager.Dispose();
	}

	@Override
	public void render() {	
		delta = Gdx.graphics.getDeltaTime();
		delta = Math.min(delta, 0.1f);
		
		Input.Update(delta);
		SceneManager.Update(delta);
		Renderer.Draw();

	}

	@Override
	public void resize(int width, int height) {
		Renderer.resizeRenderer(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
