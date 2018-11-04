package com.superblock;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.*;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import android.view.MotionEvent;

public class MyMenuLayer extends CCLayer {
    CGSize s = CCDirector.sharedDirector().winSize();//画布大小
    CCSprite menuBackground = CCSprite.sprite("menuback.png");
    CCBitmapFontAtlas Fontplay = null;//新游戏
    CCBitmapFontAtlas Fontcontinuepleay = null;//继续游戏
    CCBitmapFontAtlas Fontexitplay = null;//退出游戏
    CCBitmapFontAtlas Fonthelp = null;//游戏帮助
    CCSprite newGame = null;
    CCSprite exitGame = null;
    CCSprite continueGame = null;
    CCSprite help = null;
    float heightItem;//距离
    float wide;
    float height;
    int flag = 1;
    int stand = 720;
    MainActivity main;
    boolean touch = false;
    final float scanl = s.width / 240 * 0.6f;//字体图标放缩

    public MyMenuLayer(MainActivity main) {
        this.setIsTouchEnabled(true);//设置可以触屏
        this.addChild(menuBackground);
        menuBackground.setScale((float) (s.width / stand));
        menuBackground.setPosition(CGPoint.ccp(s.width / 2, s.height / 2));
        //menuBackground.setTextureRect(CGRect.make(0, 0, s.width/3*2, s.height/2));
        wide = menuBackground.getContentSize().width;
        height = menuBackground.getContentSize().height;
        this.main = main;
        addItem();
        showData();
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        // TODO Auto-generated method stub
        CGPoint convertedLocation = CCDirector.sharedDirector()
                .convertToGL(CGPoint.make(event.getX(), event.getY()));
        float x = newGame.getContentSize().width * s.width / stand / 2;
        float y = newGame.getContentSize().height * s.width / stand / 2;

        int n = 255;

        if (convertedLocation.x >= newGame.getPosition().x - x && convertedLocation.x <= newGame.getPosition().x + x && convertedLocation.y >= newGame.getPosition().y - y && convertedLocation.y <= newGame.getPosition().y + y) {
            newGame.setOpacity(n);
            touch = true;
            flag = 2;


        } else if (convertedLocation.x >= continueGame.getPosition().x - x && convertedLocation.x <= continueGame.getPosition().x + x && convertedLocation.y >= continueGame.getPosition().y - y && convertedLocation.y <= continueGame.getPosition().y + y) {
            continueGame.setOpacity(n);
            touch = true;
            flag = 1;

        } else if (convertedLocation.x >= exitGame.getPosition().x - x && convertedLocation.x <= exitGame.getPosition().x + x && convertedLocation.y >= exitGame.getPosition().y - y && convertedLocation.y <= exitGame.getPosition().y + y) {
            exitGame.setOpacity(n);
            touch = true;
            flag = 3;

        } else if (convertedLocation.x >= help.getPosition().x - x && convertedLocation.x <= help.getPosition().x + x && convertedLocation.y >= help.getPosition().y - y && convertedLocation.y <= help.getPosition().y + y) {
            help.setOpacity(n);
            touch = true;
            flag = 4;

        }
        return super.ccTouchesBegan(event);
    }

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        // TODO Auto-generated method stub
        if (touch) {
            main.cancleMenu(flag, 2);
            flag = 1;
            touch = false;
            int n = 150;

            continueGame.setOpacity(n);
            newGame.setOpacity(n);
            exitGame.setOpacity(n);
            help.setOpacity(n);
        }


        return super.ccTouchesEnded(event);
    }

    void addItem() {
        //菜单子项
        newGame = CCSprite.sprite("bar4.png");
        exitGame = CCSprite.sprite("bar4.png");
        continueGame = CCSprite.sprite("bar4.png");
        help = CCSprite.sprite("bar4.png");

        int n = 150;

        continueGame.setOpacity(n);
        newGame.setOpacity(n);
        exitGame.setOpacity(n);
        help.setOpacity(n);

        newGame.setScale((float) (s.width / stand));
        exitGame.setScale((float) (s.width / stand));
        continueGame.setScale((float) (s.width / stand));
        help.setScale((float) (s.width / stand));
        //一个菜单子项和行间距高度的和  
        heightItem = newGame.getContentSize().height * s.width / stand;

        //菜单的起始纵坐标
        this.addChild(newGame);
        this.addChild(exitGame);
        this.addChild(continueGame);
        this.addChild(help);

        continueGame.setPosition(s.width / 2, (float) (menuBackground.getPosition().y + menuBackground.getContentSize().height * s.width / stand / 2 - heightItem));
        newGame.setPosition(continueGame.getPosition().x, (float) (continueGame.getPosition().y - 1.5 * heightItem));
        exitGame.setPosition(continueGame.getPosition().x, continueGame.getPosition().y - 3 * heightItem);
        help.setPosition(continueGame.getPosition().x, (float) (continueGame.getPosition().y - 4.5 * heightItem));
    }

    String title[] = {"Continue", "New", "Exit", "Help"};

    //显示数据
    void showData() {
        ccColor3B color = ccColor3B.ccORANGE;
        //新游戏
        Fontplay = CCBitmapFontAtlas.bitmapFontAtlas(title[0], "bitmapFontTest5.fnt");
        Fontplay.setColor(color);
        Fontplay.setScale(scanl);
        addChild(Fontplay);
        Fontplay.setPosition(newGame.getPosition().x, newGame.getPosition().y);

        //继续游戏
        Fontcontinuepleay = CCBitmapFontAtlas.bitmapFontAtlas(title[1], "bitmapFontTest5.fnt");
        Fontcontinuepleay.setColor(color);
        Fontcontinuepleay.setScale(scanl);
        addChild(Fontcontinuepleay);
        Fontcontinuepleay.setPosition(continueGame.getPosition().x, continueGame.getPosition().y);

        //退出游戏
        Fontexitplay = CCBitmapFontAtlas.bitmapFontAtlas(title[2], "bitmapFontTest5.fnt");
        Fontexitplay.setColor(color);
        Fontexitplay.setScale(scanl);
        addChild(Fontexitplay);
        Fontexitplay.setPosition(CGPoint.ccp(exitGame.getPosition().x, exitGame.getPosition().y));

        //游戏帮助
        Fonthelp = CCBitmapFontAtlas.bitmapFontAtlas(title[3], "bitmapFontTest5.fnt");
        Fonthelp.setColor(color);
        Fonthelp.setScale(scanl);
        addChild(Fonthelp);
        Fonthelp.setPosition(CGPoint.ccp(help.getPosition().x, help.getPosition().y));

    }

    int getFlag() {
        return flag;
    }
}


