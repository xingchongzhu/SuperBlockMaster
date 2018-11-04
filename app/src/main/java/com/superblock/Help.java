package com.superblock;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.menus.CCMenuItemFont;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.ccColor3B;

import android.content.SharedPreferences;
import android.view.MotionEvent;

public class Help extends GameLayer {

    boolean playflag = true;
    float scalxy = (float) (this.s.width / customWide * 0.8);
    CCSprite back = CCSprite.sprite("fanhui.png");

    public Help(SharedPreferences settings, MainActivity block2) {
        super(settings, block2);
        this.setRun(false);
        this.right.setOpacity(255);
        this.left.setOpacity(255);
        this.bottom.setOpacity(255);
        this.change.setOpacity(255);

        CCSprite dilei = CCSprite.sprite("shoulei.png");
        CCSprite daodan = CCSprite.sprite("daodan.png");
        CCSprite pomp = CCSprite.sprite("pomp.png");

        this.addChild(dilei);
        this.addChild(daodan);
        this.addChild(pomp);
        dilei.setScale(scalxy);
        daodan.setScale(scalxy);
        pomp.setScale(scalxy);

        float x = s.width / 4;
        float y = s.height / 7 * 5;
        dilei.setPosition(x, y);
        daodan.setPosition((float) (x + dilei.getContentSize().width * scalxy * 2), y);
        pomp.setPosition((float) (x + dilei.getContentSize().width * scalxy * 4), y);

        CCSprite front = CCSprite.sprite("front.png");
        front.setPosition(s.width / 2 - dilei.getContentSize().width * scalxy, s.height / 2);
        front.setScale(this.scanl2);
        this.addChild(front);

        this.addChild(back);
        back.setScale(s.width / customWide);
        back.setPosition(back.getContentSize().width * s.width / customWide / 2, back.getContentSize().height * s.width / customWide / 2);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        // TODO Auto-generated method stub
        CGPoint convertedLocation = CCDirector.sharedDirector()
                .convertToGL(CGPoint.make(event.getX(), event.getY()));
        if (convertedLocation.x >= back.getPosition().x - back.getContentSize().width / 2 * s.width / customWide &&
                convertedLocation.x <= back.getPosition().x + back.getContentSize().width / 2 * s.width / customWide &&
                convertedLocation.y >= back.getPosition().y - back.getContentSize().height / 2 * s.width / customWide &&
                convertedLocation.y <= back.getPosition().y + back.getContentSize().height / 2 * s.width / customWide) {
            this.block1.removehelp();
        }
        return super.ccTouchesBegan(event);
    }

    public void setblink() {
        CCFadeOut fadeOut = CCFadeOut.action(3);
        CCFadeIn fadeIn = CCFadeIn.action(3);

        CCSequence seq = CCSequence.actions(fadeIn, fadeOut);
        CCRepeatForever forever = CCRepeatForever.action(seq);
        this.bottom.runAction(forever.copy());
        this.right.runAction(forever.copy());
        this.left.runAction(forever.copy());
        this.change.runAction(forever);
        this.schedule("fun", 2);

        CCIntervalAction blink = CCBlink.action(1, 2);
        this.frontBest.runAction(CCRepeatForever.action(blink));
        this.frontGate.runAction(CCRepeatForever.action(blink.copy()));
        this.frontScore.runAction(CCRepeatForever.action(blink.copy()));
    }

    public void fun(float delta) {
        CCTexture2D text2d = null;
        if (playflag) {
            playflag = false;
            text2d = CCTextureCache.sharedTextureCache().addImage("play1.png");
        } else {
            playflag = true;
            text2d = CCTextureCache.sharedTextureCache().addImage("stop1.png");
        }
        this.playPause.setTexture(text2d);
    }

}
