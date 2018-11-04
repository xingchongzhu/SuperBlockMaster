package com.superblock;

import java.util.Random;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.instant.CCInstantAction;
import org.cocos2d.actions.instant.CCToggleVisibility;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCRepeat;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.os.Handler;

/*字符串-〉数字
String s;
int i;
i = Integer.getInteger(s);
数字-〉字符串
s = String.valueOf(i);
*/
public class GameLayer extends CCLayer {

    final float customWide = 720;
    public CGSize s = CCDirector.sharedDirector().winSize();//画布大小
    public CCSprite blueBackground = CCSprite.sprite("back.png");//整个游戏背景蓝色
    public CCSprite white = CCSprite.sprite("white.jpg");//游戏背景白色
    public CCSprite gameOvew = CCSprite.sprite("goodbye.png");//游戏结束
    public CCSprite good = null;//CCSprite.sprite("good.png");//过关庆祝
    public CCSprite right = CCSprite.sprite("right.png");//右移
    public CCSprite left = CCSprite.sprite("left.png");//左移
    public CCSprite bottom = CCSprite.sprite("bottom.png");//向下
    public CCSprite change = CCSprite.sprite("change.png");//改变图形状态
    public CCSprite playPause = CCSprite.sprite("stop1.png");//播放暂停

    CCSprite zhadan = null;
    //游戏背景边界坐标
    CGPoint leftUp = CGPoint.ccp(0, 0);
    CGPoint rightUp = CGPoint.ccp(0, 0);
    CGPoint leftBottom = CGPoint.ccp(0, 0);
    CGPoint rightBottom = CGPoint.ccp(0, 0);
    CGPoint diffxy = CGPoint.ccp(0, 0);

    int currentRow = 0;//当前行
    int currentLine = 0;//当前列
    int nextRow = 0;//下一次出现图块行
    int nextLine = 0;//下一次出现图块列
    int nextColor[] = {0, 0, 0, 0};//下一图块颜色

    final int timeout = 0;
    CCSprite nextBlock[] = new CCSprite[4];//下一个图块
    final int blockSize = (int) (s.width / 20);//块大小
    final int blockRowNumber = 11;//游戏总行
    final int BlockLineNumber = 20;//游戏总列
    final int wide = blockSize * blockRowNumber + blockRowNumber + 1;//游戏背景宽
    final int height = blockSize * BlockLineNumber + BlockLineNumber + 1;//游戏背景高
    final float stand = s.width / 360;//标准
    final int distance = 1;//块间距
    final String title[] = {"Best:", "Score:", "Gate"};
    final float scanl = stand * 0.6f;//字体图标放缩
    final float scanl1 = stand * 0.3f;//字体大小放缩
    final float scanl2 = stand * 0.4f;//方向放缩大小
    final float scanlSize = stand * 0.4f;//方向位置放缩大小
    final int influence = 1;//炸弹影响范围
    final int opacity = 200;//图片透明度
    final int move = blockSize + 1;//移动距离
    final int began = 5;//开始位置
    final float scanlpaly = stand * (float) 0.2;//播放暂停放缩

    boolean touch = false;//长按
    int increase[] = new int[BlockLineNumber];//分数增加
    int timeNumber = 0;//左右移动时间
    public MainActivity block1;
    boolean bomb = false;//炸弹
    int bombtype = 1;
    //游戏死亡
    boolean liveDie = true;
    //游戏暂停
    static boolean pause = true;
    CGPoint startPostion = CGPoint.ccp(began * blockSize + began - blockSize / 2, BlockLineNumber * blockSize - blockSize / 2 + BlockLineNumber);

    //起始图块坐标
    CGPoint checkPosition[][][] = {
            //1
            //000
            // 0
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move))}},
            //2
            //  0
            // 000
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -2 * move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -2 * move))}},
            //3
            //0
            //000
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -2 * move))}},
            //4
            //00
            //00
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))}},
            //5
            //0
            //0
            //0
            //0
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -3 * move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(-move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -3 * move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(-move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0))}},
            //6
            //
            // 00
            // 00
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -2 * move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -2 * move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0))}},
            //7
            //00
            //  00
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(2 * move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -move)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(move, -2 * move))}},

            //地雷
            {{CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0))},
                    {CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0)), CGPoint.ccpAdd(startPostion, CGPoint.ccp(0, 0))}}

    };

    final int colornum = 4;
    String imageBlock1[] = {"origal.jpg", "green.png", "yellow.png", "blue.png"};
    //游戏坐标标记
    int matrixFlag[][] = new int[BlockLineNumber][blockRowNumber];
    //地雷位置标记
    int bombFlag[][] = new int[BlockLineNumber][blockRowNumber];
    //定时炸弹位置
    int bombpostion[][] = new int[BlockLineNumber][blockRowNumber];
    //定时炸弹数量
    int bombnum = 0;
    int bombtime[][] = new int[BlockLineNumber][blockRowNumber];

    CCSprite check[] = {CCSprite.sprite("daodan.png"), CCSprite.sprite("daodan.png"), CCSprite.sprite("daodan.png"), CCSprite.sprite("daodan.png")};

    public CCBitmapFontAtlas frontScore = null;//分数
    public CCBitmapFontAtlas frontBest = null;//最高成绩
    public CCBitmapFontAtlas frontGate = null;//关卡数
    int Score = 0;//分数
    int Best = 0;//最好成绩
    int Gate = 1;//关卡
    int id = 0;//图块号

    boolean touchBottom = false;//下移动
    boolean touchLeft = false;//左移动
    boolean touchRight = false;//右移动
    boolean running = false;//可以运行
    SharedPreferences settings;
    //游戏关卡
    final int maxGate = 1000;
    int gateScoreStand[] = new int[maxGate];
    int gateTimeStand[] = new int[maxGate];
    int runnum = 0;
    private Handler handler = new Handler();//游戏线程
    private Handler handler1 = new Handler();//游戏线程
    //起始游戏坐标
    int n = 0;
    CGPoint start = CGPoint.ccp(startPostion.x - 4 * move, startPostion.y - 19 * move);

    //构造函数
    public GameLayer(SharedPreferences settings, MainActivity block2) {
        for (int i = 0; i < maxGate; i++) {
            gateScoreStand[i] = (2 * i + 1) * 200;
            gateTimeStand[i] = (maxGate / 10 - i) * 5;
        }
        this.settings = settings;
        block1 = block2;
        handler1.postDelayed(task1, 500);
        init();
    }

    //保存数据
    public void savedata() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("best", Best);
        if (Gate > settings.getInt("gate", 1)) {
            editor.putInt("gate", Gate);
        }
        editor.putInt("score", Score);
        editor.commit();
    }

    //获取数据
    void getdata() {
        Best = settings.getInt("best", 0);
        Gate = settings.getInt("gate", 1);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {

        timeNumber = 0;
        touch = true;
        CGPoint convertedLocation = CCDirector.sharedDirector()
                .convertToGL(CGPoint.make(event.getX(), event.getY()));
        //音量设置

        if (running) {
            //播放暂停
            if (convertedLocation.x >= playPause.getPosition().x - scanlpaly * playPause.getContentSize().width / 2 &&
                    convertedLocation.x <= playPause.getPosition().x + scanlpaly * playPause.getContentSize().width / 2 &&
                    convertedLocation.y >= playPause.getPosition().y - scanlpaly * playPause.getContentSize().height / 2 &&
                    convertedLocation.y <= playPause.getPosition().y + scanlpaly * playPause.getContentSize().height / 2) {
                playStatus();
            }
            if (pause) {
                int nt = 255;
                //变换
                if (convertedLocation.x >= change.getPosition().x - scanlSize * change.getContentSize().width / 2 &&
                        convertedLocation.x <= change.getPosition().x + scanlSize * change.getContentSize().width / 2 &&
                        convertedLocation.y >= change.getPosition().y - scanlSize * change.getContentSize().height / 2 &&
                        convertedLocation.y <= change.getPosition().y + scanlSize * change.getContentSize().height / 2) {
                    change.setOpacity(nt);
                    SoundEngine.sharedEngine().playEffect(block1, R.raw.move);
                    changeBlock();
                }

                //右移
                if (convertedLocation.x >= right.getPosition().x - scanlSize * right.getContentSize().width / 2 &&
                        convertedLocation.x <= right.getPosition().x + scanlSize * right.getContentSize().width / 2 &&
                        convertedLocation.y >= right.getPosition().y - scanlSize * right.getContentSize().height / 2 &&
                        convertedLocation.y <= right.getPosition().y + scanlSize * right.getContentSize().height / 2) {
                    right.setOpacity(nt);
                    touchRight = true;//右移动
                    if (checkBounder(2)) {
                        SoundEngine.sharedEngine().playEffect(block1, R.raw.move1);
                        for (int i = 0; i < 4; i++) {
                            check[i].setPosition(CGPoint.ccpAdd(check[i].getPosition(), CGPoint.ccp(move, 0)));
                        }
                    }

                }
                //左移
                if (convertedLocation.x >= left.getPosition().x - scanlSize * left.getContentSize().width / 2 &&
                        convertedLocation.x <= left.getPosition().x + scanlSize * left.getContentSize().width / 2 &&
                        convertedLocation.y >= left.getPosition().y - scanlSize * left.getContentSize().height / 2 &&
                        convertedLocation.y <= left.getPosition().y + scanlSize * left.getContentSize().height / 2) {
                    left.setOpacity(nt);
                    touchLeft = true;//左移动
                    if (checkBounder(1)) {
                        SoundEngine.sharedEngine().playEffect(block1, R.raw.move1);

                        for (int i = 0; i < 4; i++) {
                            check[i].setPosition(CGPoint.ccpAdd(check[i].getPosition(), CGPoint.ccp(-move, 0)));
                        }
                    }
                }
                //下移
                if (convertedLocation.x >= bottom.getPosition().x - scanlSize * bottom.getContentSize().width / 2 &&
                        convertedLocation.x <= bottom.getPosition().x + scanlSize * bottom.getContentSize().width / 2 &&
                        convertedLocation.y >= bottom.getPosition().y - scanlSize * bottom.getContentSize().height / 2 &&
                        convertedLocation.y <= bottom.getPosition().y + scanlSize * bottom.getContentSize().height / 2) {
                    bottom.setOpacity(nt);
                    touchBottom = true;
                    //下移
                    if (checkBounder(3)) {
                        SoundEngine.sharedEngine().playEffect(block1, R.raw.move1);

                        for (int i = 0; i < 4; i++) {
                            check[i].setPosition(CGPoint.ccpAdd(check[i].getPosition(), CGPoint.ccp(0, -move)));
                        }
                    }

                }
            }
        }
        return super.ccTouchesBegan(event);
    }

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        touchLeft = false;
        touchRight = false;
        touchBottom = false;
        int nt = 180;
        change.setOpacity(nt);
        bottom.setOpacity(nt);
        left.setOpacity(nt);
        right.setOpacity(nt);
        // TODO Auto-generated method stub
        return super.ccTouchesEnded(event);
    }

    //暂停播放键
    public void setPause(boolean tt) {
        pause = tt;
    }

    void playStatus() {
        //fireworks fire=new fireworks(check[0].getPosition(),white);
        //fireworks firework=new fireworks(CGPoint.ccp(wide/2,height/2),blueBackground);
        if (pause) {
            pause = false;
            CCTexture2D text2d = CCTextureCache.sharedTextureCache().addImage("play1.png");
            playPause.setTexture(text2d);
        } else {
            pause = true;
            CCTexture2D text2d = CCTextureCache.sharedTextureCache().addImage("stop1.png");
            playPause.setTexture(text2d);
        }
    }

    //初始化数据
    void initData() {
        id = 0;
        touchBottom = false;//下移动
        touchLeft = false;//左移动
        touchRight = false;//右移动
        running = true;//可以运行
        liveDie = true;//游戏死亡
        bombnum = 0;
        pause = true;
        playPause = CCSprite.sprite("stop1.png");
        bomb = false;//炸弹
        bombtype = 1;
        timeNumber = 0;//左右移动时间
        currentRow = 0;//当前行
        currentLine = 0;//当前列
        nextRow = 0;
        nextLine = 0;
        Score = 0;//分数
        Best = 0;//最好成绩
        Gate = 1;//关卡
        //myclass my=new myclass();
        //my.getdata();
    }

    //判断边界
    boolean checkBounder(int flag) {
        for (int i = 0; i < 4; i++) {
            int line = (int) ((check[i].getPosition().x - start.x) / move);
            int row = (int) ((check[i].getPosition().y - start.y) / move);
            switch (flag) {
                case 1:
                    if (line <= 0 || (line - 1 >= 0 && row >= 0 && matrixFlag[row][line - 1] != -1))//到左边
                        return false;
                    break;
                case 2:
                    if (line >= blockRowNumber - 1 || (line + 1 <= blockRowNumber - 1 && row >= 0 && matrixFlag[row][line + 1] != -1))//到右边
                        return false;
                    break;
                case 3:
                    if (row <= 0 || (row - 1 >= 0 && matrixFlag[row - 1][line] != -1))//到下边
                        return false;
                    break;
            }
        }
        return true;//没碰到边界
    }

    //产生新图块
    void newBlock() {
        //向下移动图块
        boolean flag = true;
        if (!bomb) {
            currentRow = nextRow;
            Random random = new Random();
            nextRow = random.nextInt(7);
            while (true) {
                if (nextRow >= 0 && nextRow < 7)
                    break;
                else
                    nextRow = random.nextInt(7);//下一图块颜色
            }
            currentLine = nextLine;
            Random random1 = new Random();
            nextLine = random1.nextInt(3);
            while (true) {
                if (nextLine >= 0 && nextLine < 3)
                    break;
                else
                    nextLine = random.nextInt(3);//下一图块颜色
            }
            for (int i = 0; i < 4; i++) {
                check[i].setOpacity(opacity);
                check[i] = CCSprite.sprite(imageBlock1[nextColor[i]]);
                check[i].setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
                white.addChild(check[i]);
                check[i].setTag(id++);
                check[i].setPosition(checkPosition[currentRow][currentLine][i]);
                int line = (int) ((check[i].getPosition().x - start.x) / move);
                int row = (int) ((check[i].getPosition().y - start.y) / move);
                if (flag && (matrixFlag[row][line] != -1))//游戏死亡
                {
                    //	保存数据
                    savedata();
                    liveDie = false;
                    flag = false;
                    running = false;
                    block1.addSelect();
                    diejump();
                }
                nextColor[i] = random.nextInt(colornum);
                while (true) {
                    if (nextColor[i] >= 0 && nextColor[i] < colornum)
                        break;
                    else
                        nextColor[i] = random.nextInt(colornum);//下一图块颜色
                }
                CCTexture2D text2d = CCTextureCache.sharedTextureCache().addImage(imageBlock1[nextColor[i]]);
                nextBlock[i].setTexture(text2d);
                //nextBlock[i]=CCSprite.sprite(imageBlock1[nextColor[i]]);

                nextBlock[i].setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
                white.addChild(nextBlock[i]);
                nextBlock[i].setPosition(CGPoint.ccpAdd(checkPosition[nextRow][nextLine][i], diffxy));
            }
        } else {
            currentRow = 7;
            currentLine = 0;
            String st = " ";
            if (bombtype == 1) {
                st = "shoulei.png";
            } else if (bombtype == 2) {
                st = "daodan.png";
            } else if (bombtype == 3) {
                st = "pomp.png";
            }
            CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(st);

            for (int i = 0; i < 4; i++) {
                check[i] = CCSprite.sprite(st);
                check[i].setScale((float) (s.width / customWide * 0.6));
                check[i].setTag(id);
                check[i].setPosition(checkPosition[currentRow][currentLine][i]);
                int line = (int) ((check[i].getPosition().x - start.x) / move);
                int row = (int) ((check[i].getPosition().y - start.y) / move);
                if (flag && (matrixFlag[row][line] != -1) && bombtype != 2)//到下边
                {
                    //保存数据
                    savedata();
                    flag = false;
                    running = false;
                    block1.addSelect();
                    diejump();
                }
            }
            white.addChild(check[0]);
            id++;
        }
        if (!flag) {
            SoundEngine.sharedEngine().playEffect(block1, R.raw.die);
        }
    }

    //消行
    void clearRow() {
        boolean disapper = false;
        int flag = 0;
        int num = 0;

        if (bomb && bombtype == 2) {
            SoundEngine.sharedEngine().playEffect(block1, R.raw.bomb);

            int row = (int) (check[0].getPosition().y / move);
            int line = (int) (check[0].getPosition().x / move);
            int min = line - influence >= 0 ? line - influence : line;
            int max = (line + influence < blockRowNumber ? (line + influence) : line);
            int minrow = row + 2 < BlockLineNumber ? row + 2 : row;
            for (int i = row - influence >= 0 ? row - influence : row; i <= (row + influence < BlockLineNumber ? (row + influence) : row); i++) {
                for (int j = line - influence >= 0 ? line - influence : line; j <= (line + influence < blockRowNumber ? (line + influence) : line); j++) {
                    if (matrixFlag[i][j] != -1) {
                        white.removeChildByTag(matrixFlag[i][j], true);//消除行
                        matrixFlag[i][j] = -1;
                        if (bombpostion[i][j] != -1) {
                            minrow = 0;
                            min = 0;
                            max = blockRowNumber - 1;
                            bombpostion[i][j] = -1;
                            bombtime[i][j] = -1;
                        }
                        if (bombFlag[i][j] != -1) {
                            bombFlag[i][j] = -1;
                        }

                    }
                }
            }
            int pre[] = new int[blockRowNumber];
            for (int l = 0; l < blockRowNumber; l++) {
                pre[l] = 0;
            }
            for (int i = minrow; i < BlockLineNumber; i++) {
                for (int k = min, tmp = 0; k <= max; k++, tmp++) {
                    int tt = 0;

                    if (matrixFlag[i][k] != -1) {
                        CCSprite temp = (CCSprite) white.getChildByTag(matrixFlag[i][k]);
                        for (int r = i - 1; r >= pre[tmp]; r--) {
                            if (matrixFlag[r][k] != -1) {
                                break;
                            } else {
                                tt++;
                            }
                        }
                        pre[tmp] = i - tt;

                        if (bombFlag[i][k] != -1) {
                            bombFlag[i - tt][k] = bombFlag[i][k];
                            bombFlag[i][k] = -1;
                        }
                        if (bombpostion[i][k] != -1) {
                            bombpostion[i - tt][k] = bombpostion[i][k];
                            bombpostion[i][k] = -1;
                            bombtime[i - tt][k] = bombtime[i][k];
                            bombtime[i][k] = -1;
                        }
                        //CCMoveTo actionTo = CCMoveTo.action((float) 0.5,CGPoint.ccpAdd(temp.getPosition(),CGPoint.make(0, -1*tt*move)));
                        temp.setPosition(CGPoint.ccpAdd(temp.getPosition(), CGPoint.ccp(0, -tt * move)));
                        //temp.runAction(actionTo);
                        matrixFlag[i][k] = -1;
                        matrixFlag[i - tt][k] = temp.getTag();
                    }

                }
            }

        }
        for (int j = 0; j < BlockLineNumber; ) {
            flag = 0;
            for (int t = 0; t < blockRowNumber; t++)//满行
            {
                if (matrixFlag[j][t] == -1 || bombpostion[j][t] != -1) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                if (!disapper) {
                    SoundEngine.sharedEngine().playEffect(block1, R.raw.xiaohang);
                    disapper = true;
                }
                num++;
                for (int m = 0; m < blockRowNumber; m++) {
                    white.removeChildByTag(matrixFlag[j][m], true);//消除行
                    matrixFlag[j][m] = -1;
                    if (bombFlag[j][m] != -1) {
                        SoundEngine.sharedEngine().playEffect(block1, R.raw.dilei);

                        int low = m - influence >= 0 ? m - influence : m;
                        int high = m + influence < blockRowNumber ? m + influence : m;
                        if (j + 1 < BlockLineNumber) {
                            int tt = 0, pre[] = new int[2 * influence + 1];
                            for (int l = 0; l < 2 * influence + 1; l++) {
                                pre[l] = 0;
                            }
                            for (int i = j + 1; i < BlockLineNumber; i++) {
                                for (int k = low, tmp = 0; k <= high; k++, tmp++) {
                                    tt = 0;
                                    if (matrixFlag[i][k] != -1) {
                                        CCSprite temp = (CCSprite) white.getChildByTag(matrixFlag[i][k]);
                                        for (int r = i - 1; r >= pre[tmp]; r--) {
                                            if (matrixFlag[r][k] != -1) {
                                                break;
                                            } else {
                                                tt++;
                                            }
                                        }
                                        pre[tmp] = i - tt;

                                        if (bombFlag[i][k] != -1) {
                                            bombFlag[i - tt][k] = bombFlag[i][k];
                                            bombFlag[i][k] = -1;
                                        }
                                        if (bombpostion[i][k] != -1) {
                                            bombpostion[i - tt][k] = bombpostion[i][k];
                                            bombpostion[i][k] = -1;
                                            bombtime[i - tt][k] = bombtime[i][k];
                                            bombtime[i][k] = -1;
                                        }
                                        temp.setPosition(CGPoint.ccpAdd(temp.getPosition(), CGPoint.ccp(0, -tt * move)));
                                        matrixFlag[i][k] = -1;
                                        matrixFlag[i - tt][k] = temp.getTag();
                                    }

                                }
                            }
                        }
                        bombFlag[j][m] = -1;
                    }
                }
                //如果有个炸弹
                if (j + 1 < BlockLineNumber) {
                    for (int i = j + 1; i < BlockLineNumber; i++) {
                        for (int k = 0; k < blockRowNumber; k++) {
                            if (matrixFlag[i][k] != -1 && matrixFlag[i - 1][k] == -1) {
                                CCSprite temp = (CCSprite) white.getChildByTag(matrixFlag[i][k]);
                                temp.setPosition(CGPoint.ccpAdd(temp.getPosition(), CGPoint.ccp(0, -move)));

                                matrixFlag[i][k] = -1;
                                matrixFlag[i - 1][k] = temp.getTag();

                                if (bombFlag[i][k] != -1) {
                                    bombFlag[i - 1][k] = bombFlag[i][k];
                                    bombFlag[i][k] = -1;
                                }
                                if (bombpostion[i][k] != -1) {
                                    bombpostion[i - 1][k] = bombpostion[i][k];
                                    bombpostion[i][k] = -1;
                                    bombtime[i - 1][k] = bombtime[i][k];
                                    bombtime[i][k] = -1;
                                }
                            }
                        }
                    }
                }
                j = 0;
            } else {
                j++;
            }
        }

        Score = Score + increase[num];
        frontScore.setString("Score: " + Score * 10);//加分
        if (Best < Score) {
            Best = Score;
            frontBest.setString("Best: " + Best * 10);//加分
            frontBest.runAction(CCBlink.action(2, 5));
            savedata();
        }
        bombtype = 1;
        bomb = false;
        //判断是否爆炸
        if (num == 1) {
            bomb = true;
            bombtype = 1;
        } else if (num == 2) {
            bomb = true;
            bombtype = 2;
        } else if (num >= 3) {
            bomb = true;
            bombtype = 3;
            frontScore.runAction(CCBlink.action(2, 5));
        }
        if (Score * 10 > gateScoreStand[Gate]) {
            Gate++;
            frontGate.setString(String.valueOf(Gate));//加分
            jump();
            frontGate.runAction(CCBlink.action(2, 5));
        }
    }

    //游戏结束效果
    void diejump() {
        gameOvew.setPosition(s.width / 3, gameOvew.getContentSize().height / 2);
        this.addChild(gameOvew);
        gameOvew.setScale((float) (scanl * 0.4));

        CCIntervalAction move1 = CCMoveBy.action(1, CGPoint.make(250 * (s.width / customWide), 0));
        CCIntervalAction move2 = CCMoveBy.action(1, CGPoint.make(0, 50 * s.width / customWide));
        CCInstantAction tog1 = CCToggleVisibility.action();
        CCInstantAction tog2 = CCToggleVisibility.action();
        CCIntervalAction seq = CCSequence.actions(move1, tog1, move2, tog2, move1.reverse());
        CCAction action = CCRepeatForever.action(CCSequence.actions(seq, seq.reverse()));
        gameOvew.runAction(action);
    }

    //游戏得分效果
    void jump() {
        good = CCSprite.sprite("good.png");
        good.setPosition(s.width / 2, s.height / 2);
        good.setScale(s.width / customWide);
        this.addChild(good);
        CCJumpTo jumpTo = CCJumpTo.action(2, CGPoint.ccp(s.width / 2, s.height / 2), 100 * scanl, 3);
        CCHide hide = CCHide.action();
        CCSequence seq = CCSequence.actions(jumpTo, hide);
        good.runAction(CCRepeatForever.action(seq));
    }

    //得到当前游戏最高行
    int getCurrentHighest() {
        int currentHighestRow = 0, k = 0;
        for (int i = 0; i < BlockLineNumber; i++) {
            for (k = 0; k < blockRowNumber; k++) {
                if (matrixFlag[i][k] != -1) {
                    currentHighestRow = i;
                } else {
                    break;
                }
            }
            if (k == blockRowNumber - 1 && matrixFlag[i][blockRowNumber - 1] == -1)
                break;
        }
        return currentHighestRow;
    }

    //判断当前是否有满行
    boolean judgeCurrentFullRow() {
        int number = 4;
        int flag = 0;
        int row[] = new int[4], n = 1, num = 0;
        if (bomb) {
            number = 1;
        }
        //记录行号
        for (int i = 1; i < number; i++) {
            flag = 0;
            int j = (int) ((check[i].getPosition().y - start.y) / move);
            for (int k = 0; k < n; k++) {
                if (row[k] == j) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
                row[n++] = j;
        }
        //排序
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (row[i] < row[j]) {
                    int temp = row[i];
                    row[i] = row[j];
                    row[j] = temp;
                }
            }
        }
        //判断满行
        for (int j = 0; j < n; j++) {
            for (int t = 0; t < blockRowNumber; t++)//满行
            {
                if (matrixFlag[row[j]][t] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    //清理所有空格
    void clearNullPosition() {
        int pre[] = new int[blockRowNumber];
        for (int l = 0; l < blockRowNumber; l++) {
            pre[l] = 0;
        }
        for (int i = 1; i < BlockLineNumber; i++) {
            for (int k = 0, tmp = 0; k < blockRowNumber; k++, tmp++) {
                int tt = 0;

                if (matrixFlag[i][k] != -1) {
                    CCSprite temp = (CCSprite) white.getChildByTag(matrixFlag[i][k]);
                    for (int r = i - 1; r >= pre[tmp]; r--) {
                        if (matrixFlag[r][k] != -1) {
                            break;
                        } else {
                            tt++;
                        }
                    }
                    pre[tmp] = i - tt;

                    if (bombFlag[i][k] != -1) {
                        bombFlag[i - tt][k] = bombFlag[i][k];
                        bombFlag[i][k] = -1;
                    }
                    if (bombpostion[i][k] != -1) {
                        bombpostion[i - tt][k] = bombpostion[i][k];
                        bombpostion[i][k] = -1;
                        bombtime[i - tt][k] = bombtime[i][k];
                        bombtime[i][k] = -1;
                    }
                    //CCMoveTo actionTo = CCMoveTo.action((float) 0.5,CGPoint.ccpAdd(temp.getPosition(),CGPoint.make(0, -1*tt*move)));
                    temp.setPosition(CGPoint.ccpAdd(temp.getPosition(), CGPoint.ccp(0, -tt * move)));
                    //temp.runAction(actionTo);
                    matrixFlag[i][k] = -1;
                    matrixFlag[i - tt][k] = temp.getTag();
                }

            }
        }
    }

    //游戏移动线程
    private Runnable task1 = new Runnable() {
        public void run() {
            handler.postDelayed(this, 40);
            if (running && pause && liveDie) {
                funny();
                if (bombnum > 0) {
                    for (int i = 0; i < BlockLineNumber; i++) {
                        for (int k = 0; k < blockRowNumber; k++) {
                            if (bombtime[i][k] > 0) {
                                bombtime[i][k]--;
                                if (bombtime[i][k] == 37) {
                                    SoundEngine.sharedEngine().playEffect(block1, R.raw.alarm);

                                }
                            }
                            if (bombtime[i][k] == 0) {
                                SoundEngine.sharedEngine().playEffect(block1, R.raw.dingshi);
                                //fireworks fireworks=new fireworks(CGPoint.ccp(i*move, k*move),this);
                                white.removeChildByTag(matrixFlag[i][k], true);
                                bombtime[i][k] = -1;
                                bombpostion[i][k] = -1;
                                matrixFlag[i][k] = -1;
                                for (int l = 0; l < 4; l++) {
                                    white.removeChild(check[l], true);
                                }
                                clearNullPosition();
                                clearRow();
                                newBlock();
                            }

                        }
                    }
                }
            }
        }
    };
    //游戏线程
    private Runnable task = new Runnable() {
        public void run() {

            if (running && pause && liveDie) {
                fun();
            }

            if (runnum == 1)
                handler.removeCallbacks(task);
            handler.postDelayed(this, gateTimeStand[Gate - 1]);
        }
    };

    //float delta
    public void fun() {
        if (running && pause && liveDie) {
            if (checkBounder(3)) {
                for (int i = 0; i < 4; i++) {
                    check[i].setPosition(CGPoint.ccpAdd(check[i].getPosition(), CGPoint.ccp(0, -move)));
                }
            } else {
                int line = 0, row = 0;

                for (int i = 0; i < 4; i++) {
                    line = (int) ((check[i].getPosition().x - start.x) / move);
                    row = (int) ((check[i].getPosition().y - start.y) / move);
                    matrixFlag[row][line] = check[i].getTag();
                }
//					if(check[0].getPosition()==check[1].getPosition())
//					{
//						for(int i=0;i<4;i++)
//						{
//							if(i==0)
//							{
//								line=(int) ((check[i].getPosition().x-start.x)/move);
//							    row=(int) ((check[i].getPosition().y-start.y)/move);
//								matrixFlag[row][line]=check[i].getTag();
//							}
//							else
//							{
//								white.removeChildByTag(check[i].getTag(), true);
//							}
//						}
//					}
//					else
//					{
//						for(int i=0;i<4;i++)
//						{
//							line=(int) ((check[i].getPosition().x-start.x)/move);
//						    row=(int) ((check[i].getPosition().y-start.y)/move);
//							matrixFlag[row][line]=check[i].getTag();
//						}
//					}

                //有地雷
                if (bomb && bombtype == 1) {
                    bombFlag[row][line] = check[0].getTag();
                }
                if (bomb && bombtype == 3) {
                    bombnum++;
                    bombtime[row][line] = 300;
                    bombpostion[row][line] = check[0].getTag();
                    bombtype = 1;
                    bomb = false;
                }
                clearRow();
                newBlock();
            }

        }
    }

    //设置背景变化
    void setBackGround(int n) {
        blueBackground.setOpacity(n);
    }

    //初始化
    public void init() {
        runnum++;
        this.setIsTouchEnabled(true);//设置可以触屏
        getdata();
        if (runnum != 1)
            handler.removeCallbacks(task);
        handler.postDelayed(task, gateTimeStand[Gate - 1]);
        //blueBackground.setTextureRect(CGRect.make(0, 0, s.width, s.height)); //设置背景大小
        this.addChild(blueBackground);
        //blueBackground.setOpacity(200);
        blueBackground.setPosition(s.width / 2, s.height / 2);
        blueBackground.setScale((float) (s.width / customWide));
        white.setOpacity(300);
        //游戏背景
        white.setTextureRect(CGRect.make(0, 0, wide, height)); //设置背景大小
        this.addChild(white);
        white.setPosition(s.width / 2 - 2 * blockSize, s.height / 2 + 2 * blockSize);
        white.setOpacity(127);
        //边界坐标
        leftUp.x = white.getPosition().x - white.getContentSize().width / 2 + blockSize / 2 + 1;
        leftUp.y = white.getPosition().y + white.getContentSize().height / 2 + blockSize / 2 + 1;

        rightUp.x = white.getPosition().x + white.getContentSize().width / 2 + blockSize / 2 + 1;
        rightUp.y = white.getPosition().y + white.getContentSize().height / 2 + blockSize / 2 + 1;

        leftBottom.x = white.getPosition().x - white.getContentSize().width / 2 + blockSize / 2 + 1;
        leftBottom.y = white.getPosition().y - white.getContentSize().height / 2 + blockSize / 2 + 1;

        rightBottom.x = white.getPosition().x + white.getContentSize().width / 2 + blockSize / 2 + 1;
        rightBottom.y = white.getPosition().y - white.getContentSize().height / 2 + blockSize / 2 + 1;

        String name = "white.jpg";
        //块大小block
        for (int i = 0; i <= blockRowNumber + 1; i++) {
            CCSprite block = CCSprite.sprite(name);//游戏背景白色
            block.setOpacity(opacity);
            block.setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
            this.addChild(block);
            block.setPosition((white.getPosition().x - white.getContentSize().width / 2 - block.getContentSize().width / 2) + i * block.getContentSize().width + i * distance, white.getPosition().y + white.getContentSize().height / 2 + block.getContentSize().height / 2);

            CCSprite block1 = CCSprite.sprite(name);//游戏背景白色
            block1.setOpacity(opacity);
            block1.setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
            this.addChild(block1);
            block1.setPosition((white.getPosition().x - white.getContentSize().width / 2 - block.getContentSize().width / 2) + i * block.getContentSize().width + i * distance, white.getPosition().y - white.getContentSize().height / 2 - block.getContentSize().height / 2);

        }

        //块大小block
        for (int i = 0; i <= BlockLineNumber; i++) {
            CCSprite block = CCSprite.sprite(name);//游戏背景白色
            block.setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
            this.addChild(block);
            block.setPosition(white.getPosition().x - white.getContentSize().width / 2 - block.getContentSize().width / 2, white.getPosition().y + white.getContentSize().height / 2 - block.getContentSize().height / 2 - i * block.getContentSize().height - (i + 1) * distance);
            block.setOpacity(opacity);

            CCSprite block1 = CCSprite.sprite(name);//游戏背景白色
            block1.setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
            this.addChild(block1);
            block1.setPosition(white.getPosition().x + white.getContentSize().width / 2 + block.getContentSize().width / 2, white.getPosition().y + white.getContentSize().height / 2 - block.getContentSize().height / 2 - i * block.getContentSize().height - (i + 1) * distance);
            block1.setOpacity(opacity);
        }
        showData();

        this.addChild(right);
        this.addChild(bottom);
        this.addChild(left);
        this.addChild(change);
        int nt = 180;
        change.setOpacity(nt);
        bottom.setOpacity(nt);
        left.setOpacity(nt);
        right.setOpacity(nt);

        change.setPosition(CGPoint.ccp(s.width / 2 - 2 * blockSize, white.getPosition().y - white.getContentSize().height / 2 - scanlSize * change.getContentSize().height / 2 - 2 * blockSize));
        bottom.setPosition(CGPoint.ccp(change.getPosition().x, change.getPosition().y - scanlSize * change.getContentSize().height / 2 - scanlSize * bottom.getContentSize().height / 2 - blockSize));
        left.setPosition(CGPoint.ccp(change.getPosition().x - blockSize - scanlSize * bottom.getContentSize().width / 2 - scanlSize * left.getContentSize().width / 2, bottom.getPosition().y + blockSize));
        right.setPosition(CGPoint.ccp(change.getPosition().x + blockSize + scanlSize * bottom.getContentSize().width / 2 + scanlSize * right.getContentSize().width / 2, bottom.getPosition().y + blockSize));

        change.setScale(scanl2);
        bottom.setScale(scanl2);
        left.setScale(scanl2);
        right.setScale(scanl2);
        //change.setPosition(CGPoint.ccp(bottom.getPosition().x,bottom.getPosition().y+bottom.getContentSize().height/2+change.getContentSize().height/2));
        //初始化数据

        //显示图片
        diffxy.x = 9 * move;
        diffxy.y = -5 * move;

        //第一块图形出现的位置
        Random random = new Random();
        nextRow = random.nextInt(7);
        nextLine = random.nextInt(3);
        currentRow = random.nextInt(7);
        currentLine = random.nextInt(3);
        for (int k = 0; k < 4; k++) {
            check[k].setOpacity(opacity);
            nextColor[k] = random.nextInt(colornum);//下一图块颜色
            while (true) {
                if (nextColor[k] >= 0 && nextColor[k] < colornum)
                    break;
                else
                    nextColor[k] = random.nextInt(colornum);//下一图块颜色
            }
            nextBlock[k] = CCSprite.sprite(imageBlock1[nextColor[k]]);
            nextBlock[k].setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
            white.addChild(nextBlock[k]);
            nextBlock[k].setPosition(CGPoint.ccpAdd(checkPosition[nextRow][nextLine][k], diffxy));

            check[k] = CCSprite.sprite(imageBlock1[nextColor[k]]);
            check[k].setTextureRect(CGRect.make(0, 0, blockSize, blockSize));
            white.addChild(check[k]);
            check[k].setTag(id++);
            check[k].setPosition(checkPosition[currentRow][0][k]);
        }

        //初始化游戏背景坐标标记
        for (int i = 0; i < BlockLineNumber; i++) {
            for (int j = 0; j < blockRowNumber; j++) {
                matrixFlag[i][j] = -1;
                bombFlag[i][j] = -1;
                bombpostion[i][j] = -1;
                bombtime[i][j] = -1;
            }
        }
        //初始化增长幅度
        increase[0] = 0;
        for (int i = 1; i < BlockLineNumber; i++) {
            increase[i] = 2 * i - 1;
        }
        playPause.setPosition(white.getPosition().x + 9 * move, white.getPosition().y - 2 * move);
        this.addChild(playPause);
        playPause.setScale(scanlpaly);

    }

    //设置check[]位置
    void setCheckPosotion(int i, int j, CGPoint diff) {
        for (int k = 0; k < 4; k++)//CGPoint.ccpAdd(checkPosition[i][j][k],diff)
        {
            check[k].setPosition(CGPoint.ccpAdd(checkPosition[i][j][k], diff));
        }
    }

    //判断变化
    boolean judgeChange() {
        int n = currentLine + 1;
        if (n > 3)
            n = 0;
        CGPoint diff = CGPoint.ccpSub(check[0].getPosition(), checkPosition[currentRow][currentLine][0]);
        for (int i = 0; i < 4; i++) {
            CGPoint temp = CGPoint.ccpAdd(checkPosition[currentRow][n][i], diff);
            int line = (int) ((temp.x - start.x) / move);
            int row = (int) ((temp.y - start.y) / move);
            if (line < 0 || line > blockRowNumber - 1 || row < 0 || (line <= blockRowNumber - 1 && line >= 0 && row >= 0 && (matrixFlag[row][line] != -1)))//到左边
                return false;
        }
        return true;
    }

    //调用函数
    void changeBlock() {
        if (judgeChange()) {
            CGPoint diff = CGPoint.ccpSub(check[0].getPosition(), checkPosition[currentRow][currentLine][0]);
            if (++currentLine > 3) {
                currentLine = 0;
            }
            setCheckPosotion(currentRow, currentLine, diff);
        }
    }

    //设置是否可以运行
    public void setRun(boolean run) {
        if (run && runnum == 1)
            handler.postDelayed(task, gateTimeStand[Gate - 1]);
        running = run;
    }

    //显示数据
    void showData() {
        //设置显示字体
        frontBest = CCBitmapFontAtlas.bitmapFontAtlas(title[0] + " 0", "bitmapFontTest5.fnt");
        frontBest.setColor(ccColor3B.ccRED);
        frontBest.setScale(scanl);
        addChild(frontBest);
        //frontBest.setPosition(white.getPosition().x-white.getContentSize().width/2+frontBest.getContentSize().width/2, white.getPosition().y+white.getContentSize().height/2+frontBest.getContentSize().height/2+2*blockSize+frontBest.getContentSize().height/2);
        frontBest.setPosition(frontBest.getContentSize().width * scanl, s.height - frontBest.getContentSize().height / 2 * scanl);

        //设置显示字体
        frontScore = CCBitmapFontAtlas.bitmapFontAtlas(title[1] + " 0", "bitmapFontTest5.fnt");
        frontScore.setColor(ccColor3B.ccORANGE);
        frontScore.setScale(scanl);
        addChild(frontScore);
        frontScore.setPosition(frontBest.getPosition().x + frontScore.getContentSize().width * scanl, (float) (frontBest.getPosition().y - frontBest.getContentSize().height * scanl));
        //设置关卡
        CCBitmapFontAtlas label = CCBitmapFontAtlas.bitmapFontAtlas("Gate", "bitmapFontTest5.fnt");
        label.setColor(ccColor3B.ccBLUE);
        label.setScale(scanl);
        addChild(label);
        label.setPosition(CGPoint.ccp(white.getPosition().x + white.getContentSize().width / 2 + 3 * blockSize - blockSize / 2, white.getPosition().y + white.getContentSize().height / 2 + blockSize));

        //关卡数
        frontGate = CCBitmapFontAtlas.bitmapFontAtlas("0", "bitmapFontTest5.fnt");
        frontGate.setColor(ccColor3B.ccBLUE);
        frontGate.setScale(scanl);
        addChild(frontGate);
        frontGate.setPosition(CGPoint.ccp(white.getPosition().x + white.getContentSize().width / 2 + 3 * blockSize - blockSize / 2, white.getPosition().y + white.getContentSize().height / 2 - blockSize));

        frontScore.setString("Score: " + Score * 10);//加分
        frontGate.setString(String.valueOf(Gate));//加分
        frontBest.setString("Best: " + Best * 10);//加分
    }

    public void funny() {
        if (touch) {
            touch = false;
            timeNumber = -5;
        }
        timeNumber++;
        if (timeNumber > timeout && touchBottom) {
            //下移
            if (checkBounder(3)) {
                for (int i = 0; i < 4; i++) {
                    check[i].setPosition(CGPoint.ccpAdd(check[i].getPosition(), CGPoint.ccp(0, -move)));
                }
                SoundEngine.sharedEngine().playEffect(block1, R.raw.move1);
            } else {
                int line = 0, row = 0;

                for (int i = 0; i < 4; i++) {
                    line = (int) ((check[i].getPosition().x - start.x) / move);
                    row = (int) ((check[i].getPosition().y - start.y) / move);
                    matrixFlag[row][line] = check[i].getTag();
                }
//				if(check[0].getPosition()==check[1].getPosition())
//				{
//					for(int i=0;i<4;i++)
//					{
//						if(i==0)
//						{
//							line=(int) ((check[i].getPosition().x-start.x)/move);
//						    row=(int) ((check[i].getPosition().y-start.y)/move);
//							matrixFlag[row][line]=check[i].getTag();
//						}
//						else
//						{
//							white.removeChildByTag(check[i].getTag(), true);
//						}
//					}
//				}
//				else
//				{
//					for(int i=0;i<4;i++)
//					{
//						line=(int) ((check[i].getPosition().x-start.x)/move);
//					    row=(int) ((check[i].getPosition().y-start.y)/move);
//						matrixFlag[row][line]=check[i].getTag();
//					}
//				}
                //有地雷
                if (bomb && bombtype == 1) {
                    bombFlag[row][line] = check[0].getTag();
                }
                if (bomb && bombtype == 3) {
                    bombnum++;
                    bombtime[row][line] = 300;
                    bombpostion[row][line] = check[0].getTag();
                    bombtype = 1;
                    bomb = false;
                }
                clearRow();
                newBlock();
            }
        }
        if (touchLeft) {
            if (timeNumber > timeout && checkBounder(1)) {

                for (int i = 0; i < 4; i++) {
                    check[i].setPosition(CGPoint.ccpAdd(check[i].getPosition(), CGPoint.ccp(-move, 0)));
                }
                SoundEngine.sharedEngine().playEffect(block1, R.raw.move1);
            }

        }
        if (touchRight) {

            if (timeNumber > timeout && checkBounder(2)) {
                for (int i = 0; i < 4; i++) {
                    check[i].setPosition(CGPoint.ccpAdd(check[i].getPosition(), CGPoint.ccp(move, 0)));
                }
                SoundEngine.sharedEngine().playEffect(block1, R.raw.move1);
            }

        }
    }

    //设置游戏关卡
    public void setGate(int n) {
        Gate = n;
        if (Gate >= 2)
            Score = gateScoreStand[Gate - 1] / 10;
        if (Score > Best) {
            Best = Score;
        }
        frontBest.setString("Best:" + String.valueOf(Best * 10));
        frontScore.setString("Score:" + String.valueOf(Score * 10));
        frontGate.setString(String.valueOf(Gate));
    }

    //获得游戏是否运行
    public boolean getRun() {
        return running;
    }

    //清理游戏
    public void gameClear() {
        white.removeAllChildren(true);
        blueBackground.removeAllChildren(true);
        this.removeAllChildren(true);
        initData();
        init();

    }

}