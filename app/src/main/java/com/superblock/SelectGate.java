package com.superblock;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCAtlasNode;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabelAtlas;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.content.SharedPreferences;
import android.view.MotionEvent;

public class SelectGate extends CCLayer
{
	CGSize s = CCDirector.sharedDirector().winSize();//画布大小
	CCSprite menuBackground=CCSprite.sprite("select.png");
	CCSprite left=CCSprite.sprite("zuo1.png");
	CCSprite right=CCSprite.sprite("you1.png");
	CCSprite back=CCSprite.sprite("fanhui.png");
	float stand=s.width/720;
	
	final float scale=(float) (stand*0.7);//关卡图片放大
	final int row=3;//关卡数行数
	final int line=4;//关卡数列数
	final int Maxcount=row*line;//最多能显示关卡数
	
	int id=0;
	int gateId=2*Maxcount,gateTag=2*Maxcount;
	
	int began=1;
	int end=1;
	final float scale1=(float) (stand*0.8);//背景图片放大
	SharedPreferences settings;
	int Best=0;//最高分数
	int Gate=0;//最高关卡数
	float w=0;//坐标比例
	float h=0;//纵坐标比例
	CGPoint start=null;
	int selectGate=1;
	boolean zuo=false;
	boolean you=false;
	MainActivity main;
	//构造函数
	public SelectGate(SharedPreferences settings,MainActivity main) 
    {
		this.setIsTouchEnabled(true);
		this.settings=settings;
		this.main=main;
		menuBackground.setPosition(s.width/2,s.height/2);
		menuBackground.setScale(scale1);
		this.addChild(menuBackground);
		addGate();
		this.schedule("fun",(float) 0.1);
    }
	public boolean ccTouchesBegan(MotionEvent event) {
		// TODO Auto-generated method stub
    	CGPoint convertedLocation = CCDirector.sharedDirector()
   		.convertToGL(CGPoint.make(event.getX(), event.getY()));
    	float x=left.getContentSize().width*stand/2;
    	float y=left.getContentSize().height*stand/2;   
    	if(convertedLocation.x>=left.getPosition().x-x&&convertedLocation.x<=left.getPosition().x+x&&convertedLocation.y>=left.getPosition().y-y&&convertedLocation.y<=left.getPosition().y+y)
    	{
    		zuo=true;
        }
    	else if(convertedLocation.x>=right.getPosition().x-x&&convertedLocation.x<=right.getPosition().x+x&&convertedLocation.y>=right.getPosition().y-y&&convertedLocation.y<=right.getPosition().y+y)
    	{
    		you=true;
    	}
    	else if(convertedLocation.x>=back.getPosition().x-back.getContentSize().width*scale1/2&&convertedLocation.x<=back.getPosition().x+back.getContentSize().width*scale1/2&&convertedLocation.y>=back.getPosition().y-back.getContentSize().height*scale1/2&&convertedLocation.y<=right.getPosition().y+back.getContentSize().height*scale1/2)
    	{
    		main.removeSelect();
    		main.mainview.setTouch(true);
    	}
    	for(int i=0;i<Maxcount;i++)
    	{
    		if(convertedLocation.x>=this.getChildByTag(i).getPosition().x-w/2&&convertedLocation.x<=this.getChildByTag(i).getPosition().x+w/2&&convertedLocation.y>=this.getChildByTag(i).getPosition().y-h/2&&convertedLocation.y<=this.getChildByTag(i).getPosition().y+h/2)
    		{
    			CCLabelAtlas label2=(CCLabelAtlas) this.getChildByTag(gateId+i);
    			label2.setScale(scale);
    			selectGate=began+i;
    			if(selectGate<=end)
    			{
    				main.cancleSelectGate();
    			}
    			break;
    		}
    	}
    	return super.ccTouchesBegan(event);
	}
	//长按
	public void fun(float delta)
	{
		if(zuo)
		{
			CCTexture2D  text2d = CCTextureCache.sharedTextureCache().addImage("zuo.png");
    	    left.setTexture(text2d);  	
    	    upPage();
		}
		if(you)
		{
			CCTexture2D  text2d = CCTextureCache.sharedTextureCache().addImage("you.png");
    	    right.setTexture(text2d);
    	    nextPage();
		}
	}
	//获得游戏关卡
	public int getGate()
	{
		return selectGate;
	}
	public boolean ccTouchesEnded(MotionEvent event) 
	{
		zuo=false;
		you=false;
		CCTexture2D  text2d = CCTextureCache.sharedTextureCache().addImage("zuo1.png");
	    left.setTexture(text2d); 
	    CCTexture2D  text2d1 = CCTextureCache.sharedTextureCache().addImage("you1.png");
	    right.setTexture(text2d1);
		// TODO Auto-generated method stub
		return super.ccTouchesEnded(event);
	}
	//获取数据
	void getdata()
	{		
		Best=settings.getInt("best",0); 
		Gate=settings.getInt("gate",1); 	
	}
	//关卡下一页
	void nextPage()
	{
		if(end+row<=Gate)
		{
			began=began+row;
			end=end+row;
		}
		else
		{
			began=began+Gate-end;	
			end=Gate;			
		}
		boolean flag=false;
		int n=began;
		String name="";
		for(int i=0;i<Maxcount;i++)
		{
			flag=false;
			if(n<=end)
			{
				flag=true;					
				name="open.png";
			}
			else
			{
				name="close.png";
			}
			CCTexture2D  text2d = CCTextureCache.sharedTextureCache().addImage(name);
			CCSprite temp=(CCSprite) this.getChildByTag(i);
			temp.setTexture(text2d);
			temp.setScale(scale);
			if(flag)
			{
				CCLabelAtlas label2=(CCLabelAtlas) this.getChildByTag(gateId+i);
				label2.setScale(scale);
    			label2.setString(String.valueOf(n));
			}
			else
			{
				CCLabelAtlas label2=(CCLabelAtlas) this.getChildByTag(gateId+i);
				label2.setScale(scale);
    			label2.setString(String.valueOf(""));
			}
			n++;
	    	   
		}
	}
	//关卡上一页
	void upPage()
	{
		if(began-row>0)
		{
			began=began-row;
			end=end-row;
		}
		else
		{
			end=end-began+1;
			began=1;		
		}
		boolean flag=false;
		int n=began;
		String name="";
		for(int i=0;i<Maxcount;i++)
		{
			flag=false;
			if(n<=end)
			{
				flag=true;					
				name="open.png";
			}
			else
			{
				name="close.png";
			}
			CCTexture2D  text2d = CCTextureCache.sharedTextureCache().addImage(name);
			CCSprite temp=(CCSprite) this.getChildByTag(i);
			temp.setTexture(text2d);
			temp.setScale(scale);
			if(flag)
			{
				CCLabelAtlas label2=(CCLabelAtlas) this.getChildByTag(gateId+i);
				label2.setScale(scale);
    			label2.setString(String.valueOf(n));
			}
			else
			{
				CCLabelAtlas label2=(CCLabelAtlas) this.getChildByTag(gateId+i);
				label2.setScale(scale);
    			label2.setString(String.valueOf(""));
			}
			n++;
	    	   
		}
	}
	//添加显示关卡
	void addGate()
	{	
		CCSprite temp=CCSprite.sprite("close.png");
		w=(temp.getContentSize().width+temp.getContentSize().width/4)*scale;
		h=(temp.getContentSize().height+temp.getContentSize().height/4)*scale;
		start=CGPoint.ccp((float) (s.width/2-1.5*w),s.height/2+h);//CGPoint.ccpAdd(menuBackground.getPosition(), CGPoint.ccp(-row/2*w, -line/2*h));
		getdata();
		String name="";
		if(Gate>Maxcount)
		{			
			began=Gate-Maxcount+1;		
		}
		int n=began;
		end=Gate;
		boolean flag=false;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<line;j++)
			{		
				flag=false;
				if(n++<=end)
				{
					flag=true;					
					name="open.png";
				}
				else
				{
					name="close.png";
				}
				CCSprite close=CCSprite.sprite(name);
														
				close.setTag(id++);
				this.addChild(close);		
				close.setPosition(CGPoint.ccpAdd(CGPoint.ccp(j*w,-i*h),start));
				close.setScale(scale);
				if(flag)
				{
					CCLabelAtlas label2 = CCLabelAtlas.label(String.valueOf(n-1),"tuffy_bold_italic-charmap.png", 48, 64, ' ');
					label2.setPosition(CGPoint.ccpAdd(CGPoint.ccp(j*w-scale*close.getContentSize().width/2,-i*h),start));
					label2.setScale(scale);
					label2.setTag(gateTag++);
					
					this.addChild(label2);
				}
				else
				{
					CCLabelAtlas label2 = CCLabelAtlas.label("","tuffy_bold_italic-charmap.png", 48, 64, ' ');
					label2.setTag(gateTag++);
					label2.setScale(scale);
					label2.setPosition(CGPoint.ccpAdd(CGPoint.ccp(j*w-scale*close.getContentSize().width/2,-i*h),start));
					this.addChild(label2);
				}	
			}
		}
		this.addChild(back);
		//back.setPosition(back.getContentSize().width*scale1,menuBackground.getChildByTag(id-2).getPosition().y-2*back.getContentSize().height*scale1);
		back.setScale(scale1);
		back.setPosition((float) (s.width/2-2.5*w),(float) (s.height/2-2*h));
		
		left.setPosition(start.x-w/3*2,s.height/2);
		left.setScale(stand);
		this.addChild(left);
	
		right.setPosition(start.x+line*w-w/3,s.height/2);
		right.setScale(stand);
		this.addChild(right);
		
	}
}
