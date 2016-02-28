package com.hacktech.projects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.TimeUtils;

public class NoteGame extends ApplicationAdapter {
	boolean touchHeld = false;
	long id = 0;
	private boolean startScreen = true;
	
	
	ShaderProgram colorShader;
	SpriteBatch batch;
	Texture staffBg;
	Texture quarterNote;
	Texture eighth_note;
	Texture line_blue;
	Texture line_black;
	Texture bar;
	Texture ball;
	Texture stick;
	Texture eighth_tail;
	Texture quarter_rest;
	Texture half;
	Texture whole;
	Texture half_rest;
	Texture eighth_rest;
	Texture whole_rest;
	Texture dot;
	private final double BPM = 200;
	MusicFileProcessor mfp;
	
	boolean finished = false;
	long startTime;
	Queue<BeatTouch> beatInput = new Queue<BeatTouch>();
	IntMap<Array<Beat>> songHash = new IntMap<Array<Beat>>(); 
	Array<Beat> beatSheet = new Array<Beat>();
	@Override
	public void create () {
		
		colorShader = new ShaderProgram(Gdx.files.internal("color.vert"),Gdx.files.internal("color.frag"));
		
		if (!colorShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + colorShader.getLog());
		
		mfp = new MusicFileProcessor();
		batch = new SpriteBatch();
		staffBg = new Texture("staffbg.png");
		quarterNote = new Texture("quarterNote.png");
		line_blue = new Texture("line_blue.png");
		line_black = new Texture("line_black.png");
		half = new Texture ("half.png");
		eighth_note = new Texture ("eighth_note.png");
		stick = new Texture ("stick.png");
		ball = new Texture ("ball.png");
		bar = new Texture ("bar.png");
		eighth_tail = new Texture ("eighth_tail.png");
		whole = new Texture ("whole.png");
		quarter_rest = new Texture("quarter_rest.png");
		half_rest = new Texture ("half_rest.png");
		eighth_rest = new Texture ("eighth_rest.png");
		whole_rest = new Texture ("whole_rest.png");
		dot = new Texture ("dot.png");
		
		beatSheet = new Array<Beat>();
		beatSheet.add(new Beat(1,1, true));
		beatSheet.add(new Beat(2,1));
		beatSheet.add(new Beat(3, -2));
		beatSheet.add(new Beat(3.5, -2));
		beatSheet.add(new Beat(4,-2));
		beatSheet.add(new Beat(4.5,-2));
		beatSheet.add(new Beat(5, 2));
		beatSheet.add(new Beat(5.5, 2));
		beatSheet.add(new Beat(6, 2));
		beatSheet.add(new Beat(6.5,2));
		beatSheet.add(new Beat(7,2));
		beatSheet.add(new Beat(7.5,2));
		beatSheet.add(new Beat(8, 1));
		beatSheet.add(new Beat(9, 4));
		
		mfp.addMeasureLines(beatSheet);
		
		songHash = mfp.makeListIntoHashTable(beatSheet);
	}
	
	private void begin(){
		startScreen = false;
		startTime = TimeUtils.millis();
	}
	private void drawNoteFinal(Beat b, double x, SpriteBatch batch)
	{
		if (b.type == 1){
			batch.draw(stick, (float)x + 100, 318, 3, 20);
			batch.draw(ball, (float)x + 94, 312, 8, 8);
			if (b.dotted)
			{
				batch.draw(dot, (float)x + 127, 307, 12, 12);
			}
		}
		if (b.type == 2)
		{
			batch.draw(stick, (float)x + 100, 318, 3, 20);
			batch.draw(ball, (float)x + 94, 312, 8, 8);
			if (b.barredByPrev)
			{
				batch.draw(line_black, (float)x + 113, 337, -50, 10);
			}
			else if (!b.barsToNext)
			{
			batch.draw(eighth_tail, (float)x + 112, 290, 25, 1.5f*40.0f);
			}
		}
		if (b.type == 3)
		{
			batch.draw(stick, (float)x + 110, 250, 3, 52);
			batch.draw(half, (float)x  + 90, 200, 25, 15);	
			if (b.dotted == true){
				batch.draw(dot, (float)x + 120, 200,20, 20);
			}
		}
		if (b.type == 4)
		{
			batch.draw(whole, (float)x + 92, 312, 14, 10);
		}
		if (b.type == 0){
			batch.draw(line_black, (float)x+100, 311,3,35);
		}
		if (b.type == -1){
			batch.draw(quarter_rest, (float)x + 82, 315, 27, 27);
		}
		if (b.type == -2){
			batch.draw(eighth_rest, (float)x + 102, 285, 25, 48);
		}
		if (b.type == -3){
			batch.draw(half_rest, (float)x+ 70, 304, 50, 35);
		}
		if (b.type == -4){
			batch.draw(whole_rest, (float)x+70, 294, 50, 35);
		}
		if (b.type == -5)
		{
			batch.draw(line_black, (float)x+70, 311,10,34);
			batch.draw(line_black, (float)x+63, 311,2,34);
		}
		
	}
	private void drawNote(Beat b, double x, SpriteBatch batch)
	{
		if (b.type == 1){
			batch.draw(stick, (float)x + 22, 100, 3, 47);
			batch.draw(ball, (float)x, 84, 25, 15);
			if (b.dotted)
			{
				batch.draw(dot, (float)x + 30, 77, 20, 20);
			}
		}
		if (b.type == 2)
		{
			batch.draw(stick, (float)x + 22, 100, 3, 47);
			batch.draw(ball, (float)x, 84, 25, 15);
			if (b.barredByPrev)
			{
				batch.draw(line_black, (float)x + 23, 137, -50, 10);
			}
			else if (!b.barsToNext)
			{
			batch.draw(eighth_tail, (float)x + 22, 90, 25, 1.5f*40.0f);
			}
		}
		if (b.type == 3)
		{
			batch.draw(stick, (float)x + 20, 95, 3, 52);
			batch.draw(half, (float)x, 84, 25, 15);	
			if (b.dotted == true){
				batch.draw(dot, (float)x + 30, 75,20, 20);
			}
		}
		if (b.type == 4)
		{
			batch.draw(whole, (float)x, 84, 25, 15);
		}
		if (b.type == 0){
			batch.draw(line_black, (float)x + 20, 82,3,70);
		}
		if (b.type == -1){
			batch.draw(quarter_rest, (float)x, 85, 35, 1.7f*35.0f);
		}
		if (b.type == -2){
			batch.draw(eighth_rest, (float)x + 12, 85, 25, 48);
		}
		if (b.type == -3){
			batch.draw(half_rest, (float)x-20, 104, 50, 35);
		}
		if (b.type == -4){
			batch.draw(whole_rest, (float)x-20, 94, 50, 35);
		}
		if (b.type == -5)
		{
			batch.draw(line_black, (float)x-20, 82,10,70);
			batch.draw(line_black, (float)x-27, 82,2,70);
		}
		
	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(startScreen){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			batch.begin();
			BitmapFont font =  new BitmapFont();
			
			CharSequence str = "Click Anywhere To Begin";
			
			font.draw(batch,str,Gdx.graphics.getWidth()/2.0f,Gdx.graphics.getHeight()/2.0f);
			batch.end();
			
			if(Gdx.input.justTouched()){
				begin();
			}
		}
		else {
			long timeElapsed = TimeUtils.millis() - startTime;
			
			double min = (double)timeElapsed / (60.0*1000.0);
			double beatsElapsed = BPM*min;
			if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
			{
				if (!touchHeld)
				{
					beatInput.addFirst(new BeatTouch(beatsElapsed, id++));
					
					System.out.println(beatsElapsed + " " + id);
					touchHeld = true;
				}
			}
			else{
				touchHeld = false;
			}
			
			batch.begin();
			
			if(Math.ceil(beatsElapsed - 1) >= 1 && Math.ceil(beatsElapsed - 1) <= 4){
				BitmapFont font =  new BitmapFont();
				font.getData().scale(5.0f);
				CharSequence str = ""+(int)Math.ceil(beatsElapsed - 1);
				font.setColor(0.0f,0.0f,0.0f,1.0f);
				font.draw(batch,str,Gdx.graphics.getWidth()/4.0f,3.0f*Gdx.graphics.getHeight()/4.0f);
			}
		
			/*
			 * DRAW THE STAFF / ANY BACKGROUND
			 */
			batch.draw(staffBg, 0, 60, 500, 100);
		
			
			if(!finished){
			
			/*
			 * RENDER THE BEATS
			 */
			Array<Beat> beatWindow = songHash.get((int)Math.floor(beatsElapsed/5.0));
			if(beatWindow != null){
				if((int)Math.ceil(beatsElapsed/5.0) != (int)Math.floor(beatsElapsed/5.0)){
					Array<Beat> b2 = songHash.get((int)Math.ceil(beatsElapsed/5.0));
					if(b2 != null){
						beatWindow.addAll(b2);
					}
				}
				
				double bWindow = 4;
				double bEnd = beatsElapsed+bWindow;
				for(Beat b : beatWindow){
					if(b.beatTime > beatsElapsed - 0.25 && b.beatTime < bEnd){
						if(Math.abs(beatsElapsed - b.beatTime) < 0.1){
							b.status = 1;
						}
						
						double x = 50.0 + (400.0*(b.beatTime - beatsElapsed) / bWindow);
						drawNote(b, x, batch);
						
						if(b.type == -5 && b.status == 1){
							finished = true;
						}
					}
				}
			}
		
			}
			
			/*
			 * Render beat line
			 */
			batch.draw(line_blue, 86,82,3,70);
			batch.draw(line_blue, 65,82,3,70);
			if (finished == true){
				int bWindow = 4;
				batch.draw(staffBg, 0, 300, 800, 50);
				
				IntMap<Array<Beat>> songHashCopy = new IntMap<Array<Beat>>(songHash);
				
				
				double x = 0;
			
					for(Beat b : beatSheet){
						x = (100.0*(b.beatTime)/bWindow) - 20;
						drawNoteFinal(b,x,batch);
					}
					
					
				
					
			
				
				
			}

		
			
			batch.end();
		}
		}
	}


class BeatTouch{
	double timeStamp;
	long identity;
	public BeatTouch(double ts, long id){
		this.timeStamp = ts;
		this.identity = id;
	}
}
