package com.superblock;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCBezierBy;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRepeat;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CCBezierConfig;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.view.MotionEvent;

public class MainView extends CCLayer
{
	CGSize s = CCDirector.sharedDirector().winSize();//画布大小
	CCSprite menuBackground=CCSprite.sprite("back.png");
	CCSprite shadow=CCSprite.sprite("white.jpg");
	CCSprite yun1=CCSprite.sprite("yun1.png");
	CCSprite yun=CCSprite.sprite("yun.png");
	float wide=s.width;
	float height=s.height;
	float stand=s.width/240;
	
	CCBitmapFontAtlas Fontplay=null;//新游戏
	CCBitmapFontAtlas Fontexitplay=null;//退出游戏
	CCBitmapFontAtlas Fonthelp=null;//游戏帮助
	
	CCSprite newGame=null;
	CCSprite exitGame=null;
	CCSprite help=null;
	
	boolean lableflag1=false;
	boolean lableflag2=false;
	
	int flag=1;
	boolean touch=false;
	boolean touchFlag=true;
	final float scanl=wide/720;
	String title[]={"New","Exit","Help"};
	MainActivity main;

	//CCSprite set=CCSprite.sprite("set.png");//声音设置
	//音量控制板
	boolean volFlag=true;
	
	CCSprite back=CCSprite.sprite("white.jpg");//游戏效果音背景
	CCSprite good=CCSprite.sprite("good.png");//游戏效果音背景
	CCLabel label1 = CCLabel.makeLabel("022", "DroidSans", 32);//游戏背景音
	CCLabel label2 = CCLabel.makeLabel("022", "DroidSans", 32);//游戏效果音
	
	final float scnal=s.width/720;
	int row=(int) (130*scnal);
	final int min=(int) (s.width/2-row);
	final int max=(int) (s.width/2+row);
	SharedPreferences settings;
	
	public MainView(MainActivity main,SharedPreferences settings)
	{
		this.settings=settings;
		this.main=main;
	//	mAudioManager=main.mAudioManager;
		this.addChild(menuBackground);
		menuBackground.setScale(scanl);		
		menuBackground.setPosition(wide/2,height/2);
		this.setIsTouchEnabled(true);
		
		SoundEngine.sharedEngine().playSound(main, R.raw.backsound, true);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.move);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.move1);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.bomb);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.die);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.dingshi);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.xiaohang);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.dilei);
		SoundEngine.sharedEngine().preloadEffect(main, R.raw.alarm);
		
    	this.addChild(yun);
    	this.addChild(yun1);
    	yun.setScale(scanl);
    	yun1.setScale(scanl);
    	yun.setPosition(wide/8,height/8*3);
    	yun1.setPosition(wide/8*3,height/8*2);
    	CCMoveBy move = CCMoveBy.action(15, CGPoint.make(wide/8*6,height/8*5));
    	CCMoveBy move1 = CCMoveBy.action(15, CGPoint.make(wide/8*5, height/8*5));
    	
    	CCIntervalAction seq = CCSequence.actions(move,  move.reverse());
    	CCIntervalAction seq1 = CCSequence.actions(move1,  move1.reverse());
        
    	yun.runAction(CCRepeatForever.action(seq));
    	yun1.runAction(CCRepeatForever.action(seq1));
    	
    	addItem();
    	showData();
	}
	void addItem()
    {		
		//菜单子项  
    	newGame=CCSprite.sprite("bar4.png");
		exitGame=CCSprite.sprite("bar4.png");		
		help=CCSprite.sprite("bar4.png");
        newGame.setScale(scanl);
        exitGame.setScale(scanl);       
        help.setScale(scanl);
        //一个菜单子项和行间距高度的和  
        float heightItem = newGame.getContentSize().height*scanl;  
        
        //菜单的起始纵坐标
        this.addChild(newGame);
        this.addChild(exitGame);      
        this.addChild(help);
        int n=150;

        newGame.setOpacity(n);
        exitGame.setOpacity(n);
        help.setOpacity(n);

        exitGame.setPosition(wide/2,height/2+heightItem);
        newGame.setPosition(exitGame.getPosition().x,(float) (exitGame.getPosition().y+1.5*heightItem));      
        help.setPosition(exitGame.getPosition().x,(float) (exitGame.getPosition().y-1.5*heightItem));
    }
	public boolean ccTouchesBegan(MotionEvent event) 
	{
		// TODO Auto-generated method stub
		CGPoint convertedLocation = CCDirector.sharedDirector()
		.convertToGL(CGPoint.make(event.getX(), event.getY())); 	

    	float x=newGame.getContentSize().width*scanl/2;
    	float y=newGame.getContentSize().height*scanl/2;
        int n=255;
    		
        if(touchFlag&&volFlag)
        {
        	if(convertedLocation.x>=newGame.getPosition().x-x&&convertedLocation.x<=newGame.getPosition().x+x&&convertedLocation.y>=newGame.getPosition().y-y&&convertedLocation.y<=newGame.getPosition().y+y)
        	{
        		newGame.setOpacity(n);
        		touch=true;
        		flag=1;
        	}
        	else if(convertedLocation.x>=exitGame.getPosition().x-x&&convertedLocation.x<=exitGame.getPosition().x+x&&convertedLocation.y>=exitGame.getPosition().y-y&&convertedLocation.y<=exitGame.getPosition().y+y)
        	{
        		exitGame.setOpacity(n);
        		touch=true;
        		flag=3;
        	}
        	else if(convertedLocation.x>=help.getPosition().x-x&&convertedLocation.x<=help.getPosition().x+x&&convertedLocation.y>=help.getPosition().y-y&&convertedLocation.y<=help.getPosition().y+y)
        	{
        		help.setOpacity(n);
        		touch=true;
        		flag=4;
        	}
        }
		return super.ccTouchesBegan(event);
	}
	public boolean ccTouchesMoved(MotionEvent event) 
	{
		CGPoint convertedLocation = CCDirector.sharedDirector()
		.convertToGL(CGPoint.make(event.getX(), event.getY())); 
		int n=1;
		// TODO Auto-generated method stub
		return super.ccTouchesMoved(event);
	}
	@Override
	public boolean ccTouchesEnded(MotionEvent event) 
	{
		// TODO Auto-generated method stub
		if(touch)
		{
			main.cancleMenu(flag,1);
			flag=1;
			touch=false;
			int n=150;

	        newGame.setOpacity(n);
	        exitGame.setOpacity(n);
	        help.setOpacity(n); 
	     }
		 return super.ccTouchesEnded(event);
	}
	//设置可以触摸
	public void setTouch(boolean t)
	{
		touchFlag=t;
	}
	//显示数据
  	void showData()
  	{
  		ccColor3B color=ccColor3B.ccORANGE;
  		//新游戏
  		Fontplay = CCBitmapFontAtlas.bitmapFontAtlas(title[0], "bitmapFontTest5.fnt");
  		Fontplay.setColor(color);
  		Fontplay.setScale(scanl);
  		addChild(Fontplay);
        Fontplay.setPosition(newGame.getPosition().x,newGame.getPosition().y);
  			       
        //退出游戏
        Fontexitplay = CCBitmapFontAtlas.bitmapFontAtlas(title[1], "bitmapFontTest5.fnt");
        Fontexitplay.setColor(color);
        Fontexitplay.setScale(scanl);
        addChild(Fontexitplay);  
        Fontexitplay.setPosition(CGPoint.ccp(exitGame.getPosition().x,exitGame.getPosition().y));

        //游戏帮助
        Fonthelp = CCBitmapFontAtlas.bitmapFontAtlas(title[2], "bitmapFontTest5.fnt");
        Fonthelp.setColor(color);
        Fonthelp.setScale(scanl);
        addChild(Fonthelp);  
        Fonthelp.setPosition(CGPoint.ccp(help.getPosition().x,help.getPosition().y));

  	}
}
