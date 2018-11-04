package com.superblock;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity  {
	
	private CCGLSurfaceView view=null;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    GameLayer gameLayer;
    CCDirector director;
   //生成一个游戏场景对象
    static CCScene scene;
    //菜单场景
    MyMenuLayer menu;
	static boolean keyFlay=true;
	boolean backflag=false;
	public MainView mainview=null;
	boolean first=true;
	SelectGate select;
	SharedPreferences settings;
	boolean fflag=true;
	boolean onstop=false;
	Help help;
	//系统声音
	//public AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
	
    public void onCreate(Bundle savedInstanceState) 
    {    		 
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		view=new CCGLSurfaceView(this);
        setContentView(view);
        //得到ccdirector对象
        director=CCDirector.sharedDirector();
        //设置应用程序相关属性
        //设置当前游戏当中所使用的view对象
        director.attachInView(view);
        //设置游戏设法显示fps值  针值
        //director.setDisplayFPS(true);
        //设置游戏渲染一帧所需要的时间
        director.setAnimationInterval(1/60.0);
        //生成一个游戏场景对象
        scene=CCScene.node();
        //生成布景层对象
        settings = getSharedPreferences("superGame", Activity.MODE_PRIVATE);		
        menu=new MyMenuLayer(this);
        mainview=new MainView(this,settings);
        help=new Help(settings,this);
        gameLayer=new GameLayer(settings,this);
        //将对象添加到游戏场景当中
        scene.addChild(mainview);
       
        //运行游戏场景
        director.runWithScene(scene);
    }
	//补抓按键
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{				
		
		if(keyCode == KeyEvent.KEYCODE_BACK) //监控/拦截/屏蔽返回键
		{ 						
			hint();
			return false; 
		} 
		else if(keyCode == KeyEvent.KEYCODE_MENU&&!first) 
		{
			gameLayer.setRun(false);
			scene.removeChild(select, true);
			if(keyFlay)
			{
				keyFlay=false;				
				scene.addChild(menu);				
			}
			else
			{
				scene.removeChild(menu, true);
				gameLayer.setRun(true);
				keyFlay=true;								
			}								
			return false;
		} 	
		if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			if(!first)
			{
				gameLayer.setPause(true);
				gameLayer.setRun(false);
				gameLayer.playStatus();
			}
			SoundEngine.sharedEngine().pauseSound();
		}
		return super.onKeyDown(keyCode, event);
	}	 
	//菜单处理
	void delt(int n,int flag)
	{
		scene.removeChild(help, true);
		switch(n)
		{
		case 1://新游戏		
			gameLayer.setRun(false);
			select= new SelectGate(settings,this);
			scene.removeChild(menu, true);
	        scene.addChild(select);	
			if(flag==2)
			{				
				keyFlay=true;	
			}
			else
			{
				mainview.setTouch(false);
			}
			break;
		case 2://继续游戏				
			scene.removeChild(menu, true);	
			keyFlay=true;
			gameLayer.setRun(true);		
			break;
		case 3://退出游戏
			if(flag==2)
				gameLayer.savedata();
			SoundEngine.sharedEngine().pauseSound();
			keyFlay=true;
			this.finish();
			break;
		case 4://游戏帮助
			scene.removeChild(menu, true);
			scene.addChild(help);
			keyFlay=true;
			help.setblink();
			break;
		}		
	}
	public void removehelp()
	{
		scene.removeChild(help, true);
		if(!first)
		{
			gameLayer.setRun(true);
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		SoundEngine.sharedEngine().playSound(this, R.raw.backsound, true);
		gameLayer.setRun(onstop);
		super.onResume();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		SoundEngine.sharedEngine().pauseSound();
		onstop=gameLayer.getRun();
		gameLayer.setRun(false);
		
		super.onStop();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		SoundEngine.sharedEngine().pauseSound();
		super.onPause();
	}
	protected void hint() 
	{ 					   
		gameLayer.setRun(false);
        AlertDialog.Builder builder0 = new AlertDialog.Builder(MainActivity.this);
        builder0.setTitle("       Exit").setIcon(R.drawable.hint);
        builder0.setNeutralButton("Ensure", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) {	
				
				gameLayer.savedata();
				dialog.dismiss();			
				SoundEngine.sharedEngine().pauseSound();
				MainActivity.this.finish();
			}
		});
        builder0.setPositiveButton("Cancle", new DialogInterface.OnClickListener() 
        {
			public void onClick(DialogInterface dialog, int which) 
			{               
                 dialog.dismiss(); 
                 gameLayer.setRun(true);
			}
		});    
        builder0.setCancelable(false);//setCanceledOnTouchOutside(false);  
        builder0.create().show();
	} 
    //添加选择菜单
	public void addSelect()
	{
		select= new SelectGate(settings,this);
		scene.addChild(select);
	}
	//移除菜单
	public void removeSelect()
	{
		scene.removeChild( select, true);
	}
	//取消选关菜单
	public void cancleSelectGate()
	{	
		if(fflag)
		{
			first=false;
			fflag=false;
			scene.removeChild(mainview, true);		
			scene.addChild(gameLayer);
		}
		scene.removeChild( select, true);
		scene.removeChild(menu, true);
		gameLayer.savedata();
		gameLayer.gameClear();
		gameLayer.setGate(select.getGate());					
		gameLayer.setRun(true);
	}
	//取消菜单	
    public void cancleMenu(int n,int flag)
    {
    	delt(n,flag);
    }
	
}