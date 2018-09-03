package com.fuyou.play.util.HORUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.fuyou.play.R;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25 0025.
 *
 * @author wwei
 */

public class BornAstrolabeView extends View {

    private static final String TAG = "BornAstrolabeView";

    //盘的类别，0为单人盘，1为合盘
    private int AST_TYPE;

    //自定义表盘颜色
    private int mBorderColor;
    //自定义表盘宽度
    private float mBorderWidth;
    //控件测量信息
    private RectF mBounds;
    //控件宽度
    private float width;
    //控件高度
    private float height;
    //画笔
    private Paint mPaint;
    //文字画笔
    private Paint textPaint;
    //圆环画笔
    private Paint cyclePaint;
    private float cycleBorderWidth;
    private Rect textRect;
    //表盘半径
    private float radius;
    //星盘绘制数据
    private HORChartResult result;
    //副星盘绘制数据
    private HORChartResult result2;
    //宫位数据
    private List<HORHouseData> houseDatas;
    //行星数据
    private List<HORPlanetData> planetDatas;
    //副行星数据
    private List<HORPlanetData> planetDatas2;
    //连线数据
    private List<HORAspect> aspects;
    //星座相对于第一宫的偏移
    private float offset;
    //第一宫对应的星座
    private int firstPos;
    //行星连线的长度
    private final int LINE_OUT_LENGTH = 50;

    //星盘常量数据
    private String[] numHouse = new String[]{ "1", "2", "3", "4", "5","6","7", "8", "9", "10", "11", "12"};
    private String[] symbol = new String[]{"♈","♉","♊","♋","♌","♍","♎","♏","♐","♑","♒","♓"};
    private String[] plantStr = new String[]{"☼","☽","☿","♀","♂","♆","♅","♄","♃","♇"};

    private Bitmap[] horo_bitmaps;
    private Bitmap[] p_bitmaps;

    //主星盘行星顺序字符
    private String[] posStr = null;
    //星座下标顺序集合
    private int[] horList = null;
    private SparseArray<Float> horArray = null;
    //宫位偏移角集合
    private List<Float> houseOffsetList;
    //宫位编号偏移角集合
    private List<Float> houseOffsetNum;
    //主星盘连线点集合
    private List<PointEntity> pointEntities;
    //副星盘连线点集合
    private List<PointEntity> pointEntities2;
    //主星盘行星偏移角
    private List<Float> planetOffsetList;
    //副星盘行星偏移角
    private List<Float> planetOffsetList_2;
    //主星盘行星修正角度
    private float[] planetRevise;
    //副星盘行星修正角度
    private float[] planetRevise2;
    private List<PlanetOffsetEntity> planetOffsetEntities;

    public BornAstrolabeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BornAstrolabeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Typeface TF_BOOK;
    private Paint linePaint;

    private void init(Context context){
        horo_bitmaps = new Bitmap[12];
        p_bitmaps = new Bitmap[10];
        int[] temps_int = {
                R.drawable.h_sign_choosed_0,
                R.drawable.h_sign_choosed_1,
                R.drawable.h_sign_choosed_2,
                R.drawable.h_sign_choosed_3,
                R.drawable.h_sign_choosed_4,
                R.drawable.h_sign_choosed_5,
                R.drawable.h_sign_choosed_6,
                R.drawable.h_sign_choosed_7,
                R.drawable.h_sign_choosed_8,
                R.drawable.h_sign_choosed_9,
                R.drawable.h_sign_choosed_10,
                R.drawable.h_sign_choosed_11
        };
        int[] temps_p_int = {
                R.drawable.a_planet_0,
                R.drawable.a_planet_1,
                R.drawable.a_planet_2,
                R.drawable.a_planet_3,
                R.drawable.a_planet_4,
                R.drawable.a_planet_5,
                R.drawable.a_planet_6,
                R.drawable.a_planet_7,
                R.drawable.a_planet_8,
                R.drawable.a_planet_9
        };
        for (int i=0;i<12;i++){
            horo_bitmaps[i] = BitmapFactory.decodeResource(getResources(), temps_int[i]);
        }
        for (int i=0;i<10;i++){
            p_bitmaps[i] = BitmapFactory.decodeResource(getResources(), temps_p_int[i]);
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x66336699);
        mPaint.setStrokeWidth(2);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xffffffff);
        textRect = new Rect();
        cyclePaint = new Paint();
        cyclePaint.setAntiAlias(true);
//        cyclePaint.setStyle(Paint.Style.STROKE);
        cyclePaint.setStrokeWidth(cycleBorderWidth);


        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);

        try {
            /*TF_BOOK = Typeface.createFromAsset(getContext().getAssets(),"font/AvenirLTStd-Book.otf");
            textPaint.setTypeface(TF_BOOK);*/
        }catch (Exception e){
            Log.i("TAG","IF_BOOK");
        }

    }
    /*<color name="draw_hor_color_red">#ff0000</color>
    <color name="draw_hor_color_blue">#0000ff</color>s
    <color name="draw_hor_color_green">#00ff00</color>
    <color name="draw_hor_color_yellow">#ffff00</color>*/

    private int[] colorCircles = {0xffff0000, 0xff0000ff,0xff00ff00, 0xffffff00};
    private int[] colorLines = {0xffffffff, 0xffffff00, 0xff00ff00, 0xffff0000,0xff3366ff};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (result!=null){
            canvas.drawColor(0x00000000);
            mPaint.setColor(0xFFFFFFFF);
            canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius,mPaint);
            mPaint.setColor(getResources().getColor(R.color.base_all_half));
            mPaint.setStrokeWidth(radius/5 - 2 - radius/20);
            canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius*9/10 - radius/40 -2,mPaint);
            mPaint.setColor(getResources().getColor(R.color.base_all_half_half));
            mPaint.setStrokeWidth(radius/20 - 2);
            canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius*39/40-2,mPaint);
            mPaint.setStrokeWidth(2);
            mPaint.setColor(0xFFFFFFFF);
            canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius*19/20,mPaint);
            canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius*4/5,mPaint);
            canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius*29/40,mPaint);

            linePaint.setColor(0xffffffff);
            canvas.drawLine(width/2-radius-LINE_OUT_LENGTH/2,mBounds.centerY(),width/2+radius+LINE_OUT_LENGTH/2,mBounds.centerY(),linePaint);
            float line_offset1 =  houseOffsetList.get(3);
            float line_offset2 =  houseOffsetList.get(9);
            float start_x,start_y,end_x,end_y;
            start_x = (radius+LINE_OUT_LENGTH/2) *(float) Math.cos(Math.PI/180 * (line_offset1+180));
            start_y = (radius+LINE_OUT_LENGTH/2) *(float) Math.sin(Math.PI/180 * (line_offset1+180));
            end_x = (radius+LINE_OUT_LENGTH/2) *(float) Math.cos(Math.PI/180 * (line_offset2+180));
            end_y = (radius+LINE_OUT_LENGTH/2) *(float) Math.sin(Math.PI/180 * (line_offset2+180));
            end_x+=mBounds.centerX();
            end_y+=mBounds.centerY();
            start_x+=mBounds.centerX();
            start_y+=mBounds.centerY();
            canvas.drawLine(start_x,start_y,end_x,end_y,linePaint);
            //绘制外盘和宫位
            drawHouse(canvas);
            //绘制行星
            drawPlanet(canvas);
        }
    }

    private void drawPlanet(Canvas canvas){
        float start_x,start_y,end_x,end_y;
        float text_x,text_y;
        float point_x,point_y;
        String itemText = "";

        textPaint.setTextSize(36);
        textPaint.setStrokeWidth(1);
        pointEntities = new ArrayList<>();
        pointEntities2 = new ArrayList<>();



        if (AST_TYPE==0){//单人盘
            for (int i=0;i<planetDatas.size();i++){
                int colorIdx = i%4;
                textPaint.setColor(colorCircles[colorIdx]);
                float planetOffset = planetOffsetList.get(i);
                float reviseOffset = planetRevise[i];
                //画行星文字
//                itemText = plantStr[i];
//                textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
                text_x = (radius*3/5 - 30) *(float) Math.cos(Math.PI/180 * (reviseOffset+180));
                text_y = (radius*3/5 - 32) *(float) Math.sin(Math.PI/180 * (reviseOffset+180));
                text_x+=mBounds.centerX();
                text_y+=mBounds.centerY();

                try {
                    canvas.drawBitmap(p_bitmaps[i],text_x-9,text_y-9,textPaint);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e);
                }
//                drawText(canvas,itemText,text_x-textRect.width()/2,text_y+textRect.height()/4,textPaint,0);
                mPaint.setColor(colorCircles[colorIdx]);

                point_x = (radius*3/5 - 30 - radius/10) *(float) Math.cos(Math.PI/180 * (planetOffset+180));
                point_y = (radius*3/5 - 32 - radius/10) *(float) Math.sin(Math.PI/180 * (planetOffset+180));
                point_x+=mBounds.centerX();
                point_y+=mBounds.centerY();
                PointEntity pointEntity = new PointEntity();
                pointEntity.setX(point_x);
                pointEntity.setY(point_y);
                pointEntities.add(pointEntity);
                canvas.drawCircle(point_x,point_y,2,mPaint);

                start_x = (radius*3/5 - 26 - radius/10) *(float) Math.cos(Math.PI/180 * (planetOffset+180));
                start_y = (radius*3/5 - 28 - radius/10) *(float) Math.sin(Math.PI/180 * (planetOffset+180));
                start_x+=mBounds.centerX();
                start_y+=mBounds.centerY();
                end_x = (radius*3/5 -  radius/10) *(float) Math.cos(Math.PI/180 * (reviseOffset+180));
                end_y = (radius*3/5 -  radius/10) *(float) Math.sin(Math.PI/180 * (reviseOffset+180));
                end_x+=mBounds.centerX();
                end_y+=mBounds.centerY();
                linePaint.setColor(0xffffffff);
                canvas.drawLine(start_x,start_y,end_x,end_y,linePaint);
            }
        } else {
            for (int i=0;i<planetDatas.size();i++){
                int colorIdx = i%4;
                textPaint.setColor(colorCircles[colorIdx]);
                float planetOffset = planetOffsetList.get(i);
                float reviseOffset = planetRevise[i];
                //画行星文字
//                itemText = plantStr[i];
//                textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
                //主星盘
                text_x = (getPlanet2Seat() - 30 - 22) *(float) Math.cos(Math.PI/180 * (reviseOffset+180));
                text_y = (getPlanet2Seat() - 32 - 22) *(float) Math.sin(Math.PI/180 * (reviseOffset+180));
                text_x+=mBounds.centerX();
                text_y+=mBounds.centerY();
                try {
                    canvas.drawBitmap(p_bitmaps[i],text_x-9,text_y-9,textPaint);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e);
                }
//                canvas.drawBitmap(p_bitmaps[i],text_x-9,text_y-9,textPaint);
//                drawText(canvas,itemText,text_x-textRect.width()/2,text_y+textRect.height()/4,textPaint,0);
                mPaint.setColor(colorCircles[colorIdx]);

                point_x = (getPlanet2Seat() + 2) *(float) Math.cos(Math.PI/180 * (planetOffset+180));
                point_y = (getPlanet2Seat() + 2) *(float) Math.sin(Math.PI/180 * (planetOffset+180));
                point_x+=mBounds.centerX();
                point_y+=mBounds.centerY();
                PointEntity pointEntity = new PointEntity();
                pointEntity.setX(point_x);
                pointEntity.setY(point_y);
                pointEntities.add(pointEntity);
                canvas.drawCircle(point_x,point_y,2,mPaint);
                start_x = (getPlanet2Seat() - 26) *(float) Math.cos(Math.PI/180 * (reviseOffset+180));
                start_y = (getPlanet2Seat() - 28) *(float) Math.sin(Math.PI/180 * (reviseOffset+180));
                start_x+=mBounds.centerX();
                start_y+=mBounds.centerY();
                end_x = (getPlanet2Seat()) *(float) Math.cos(Math.PI/180 * (planetOffset+180));
                end_y = (getPlanet2Seat()) *(float) Math.sin(Math.PI/180 * (planetOffset+180));
                end_x+=mBounds.centerX();
                end_y+=mBounds.centerY();
                linePaint.setColor(0xffffffff);
                canvas.drawLine(start_x,start_y,end_x,end_y,linePaint);
            }

            if (planetDatas2!=null){
                for (int i=0;i<planetDatas2.size();i++){
                    int colorIdx = i%4;
                    textPaint.setColor(colorCircles[colorIdx]);
                    float planetOffset = planetOffsetList_2.get(i);
                    float reviseOffset = planetRevise2[i];
                    //画行星文字
//                    itemText = plantStr[i];
//                    textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
                    text_x = (radius*3/5 - 30) *(float) Math.cos(Math.PI/180 * (reviseOffset+180));
                    text_y = (radius*3/5 - 32) *(float) Math.sin(Math.PI/180 * (reviseOffset+180));
                    text_x+=mBounds.centerX();
                    text_y+=mBounds.centerY();
//                    drawText(canvas,itemText,text_x-textRect.width()/2,text_y+textRect.height()/4,textPaint,0);
                    try {
                        canvas.drawBitmap(p_bitmaps[i],text_x-9,text_y-9,textPaint);
                    }catch (Exception e){
                        ExceptionUtils.ExceptionSend(e);
                    }

                    //画连线点
                    mPaint.setColor(colorCircles[colorIdx]);
                    point_x = (radius*3/5 - 30 - radius/10) *(float) Math.cos(Math.PI/180 * (planetOffset+180));
                    point_y = (radius*3/5 - 32 - radius/10) *(float) Math.sin(Math.PI/180 * (planetOffset+180));
                    point_x+=mBounds.centerX();
                    point_y+=mBounds.centerY();
                    PointEntity pointEntity = new PointEntity();
                    pointEntity.setX(point_x);
                    pointEntity.setY(point_y);
                    pointEntities2.add(pointEntity);
                    canvas.drawCircle(point_x,point_y,2,mPaint);
                    //画指向线
                    start_x = (radius*3/5 - 26 - radius/10) *(float) Math.cos(Math.PI/180 * (planetOffset+180));
                    start_y = (radius*3/5 - 28 - radius/10) *(float) Math.sin(Math.PI/180 * (planetOffset+180));
                    start_x+=mBounds.centerX();
                    start_y+=mBounds.centerY();
                    end_x = (radius*3/5 -  radius/10) *(float) Math.cos(Math.PI/180 * (reviseOffset+180));
                    end_y = (radius*3/5 -  radius/10) *(float) Math.sin(Math.PI/180 * (reviseOffset+180));
                    end_x+=mBounds.centerX();
                    end_y+=mBounds.centerY();
                    linePaint.setColor(0xffffffff);
                    canvas.drawLine(start_x,start_y,end_x,end_y,linePaint);
                }
            }

        }
        //连线
        if (aspects!=null){
            for (int i=0;i<aspects.size();i++){
                HORAspect aspect = aspects.get(i);
                int p1 = aspect.get_planet1().getId();
                int p2 = aspect.get_planet2().getId();
                int colorIdx = aspect.getType().getId();
                linePaint.setColor(colorLines[colorIdx]);
                int line_type = getLineType(aspect);
//                Log.i("TAG","line_type:"+line_type);
                if (AST_TYPE==0){
                    if (line_type==LINE_TYPE_1){
                        canvas.drawLine(pointEntities.get(p1).getX(),pointEntities.get(p1).getY(),pointEntities.get(p2).getX(),pointEntities.get(p2).getY(),linePaint);
                    }else if(line_type==LINE_TYPE_2){
                        drawDashed(canvas,colorLines[colorIdx],pointEntities.get(p1).getX(),pointEntities.get(p1).getY(),pointEntities.get(p2).getX(),pointEntities.get(p2).getY());
                    }
                } else {
                    if (line_type==LINE_TYPE_1){
                        canvas.drawLine(pointEntities.get(p1).getX(),pointEntities.get(p1).getY(),pointEntities2.get(p2).getX(),pointEntities2.get(p2).getY(),linePaint);
                    }else if(line_type==LINE_TYPE_2){
                        drawDashed(canvas,colorLines[colorIdx],pointEntities.get(p1).getX(),pointEntities.get(p1).getY(),pointEntities2.get(p2).getX(),pointEntities2.get(p2).getY());
                    }
                }

            }
        }

    }

    private void drawHouse(Canvas canvas){
        float start_x,start_y,end_x,end_y;
        float text_x,text_y;
        String itemText = "";
        textPaint.setStrokeWidth(1);
        mPaint.setColor(0xFFFFFFFF);
        for (int i=360;i>0;i--){
            int rem = i%30;
            if (rem==0){
                mPaint.setStrokeWidth(2);
                start_x= radius*29/40 *(float) Math.cos(Math.PI/180 * (i +offset));
                start_y= radius*29/40 *(float) Math.sin(Math.PI/180 * (i +offset));
                end_x= radius *(float) Math.cos(Math.PI/180 * (i +offset));
                end_y= radius *(float) Math.sin(Math.PI/180 * (i +offset));
                start_x+=mBounds.centerX();
                end_x+=mBounds.centerX();
                start_y+=mBounds.centerY();
                end_y+=mBounds.centerY();
                canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);

                int planetIdx = getGuardPlanet(horList[12 - i/30]);
                textPaint.setTextSize(32);
                textPaint.setColor(colorCircles[planetIdx%4]);
//                itemText = posStr[12 - i/30];
                text_x = radius*7/8 *(float) Math.cos(Math.PI/180 * (i +offset - 15+180));
                text_y = radius*7/8 *(float) Math.sin(Math.PI/180 * (i +offset - 15+180));
                text_x+=mBounds.centerX();
                text_y+=mBounds.centerY();
//                textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
//                drawText(canvas,itemText,text_x-textRect.width()/2,text_y+textRect.height()/4,textPaint,0);
                try {
                    canvas.drawBitmap(horo_bitmaps[horList[12 - i/30]],text_x-16,text_y-16,textPaint);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e);
                }

//                canvas.drawBitmap(horo_bitmaps[12 - i/30],text_x-16,text_y-16,textPaint);
                textPaint.setTextSize(24);
                textPaint.setColor(0xffffffff);


                textPaint.setColor(colorCircles[planetIdx%4]);
                itemText = plantStr[planetIdx];
                text_x = radius*7/8 *(float) Math.cos(Math.PI/180 * (i +offset - 8+180));
                text_y = radius*7/8 *(float) Math.sin(Math.PI/180 * (i +offset - 8+180));
                text_x+=mBounds.centerX();
                text_y+=mBounds.centerY();
                try {
                    canvas.drawBitmap(p_bitmaps[planetIdx],text_x-9,text_y-9,textPaint);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e);
                }
                /*textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
                drawText(canvas,itemText,text_x-textRect.width()/2,text_y+textRect.height()/4,textPaint,0);*/

            }else {
                if (i%5==0){
                    mPaint.setStrokeWidth(2);
                }else {
                    mPaint.setStrokeWidth(1);
                }
                start_x= radius*29/40 *(float) Math.cos(Math.PI/180 *(i +offset));
                start_y= radius*29/40 *(float) Math.sin(Math.PI/180 *(i +offset));
                end_x= radius*4/5 *(float) Math.cos(Math.PI/180 * (i +offset));
                end_y= radius*4/5 *(float) Math.sin(Math.PI/180 * (i +offset));
                start_x+=mBounds.centerX();
                end_x+=mBounds.centerX();
                start_y+=mBounds.centerY();
                end_y+=mBounds.centerY();
                canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
            }
        }

        mPaint.setStrokeWidth(2);
        canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius*3/5,mPaint);


        textPaint.setTextSize(24);
        textPaint.setColor(0xffffffff);
        for (int i=0;i<houseDatas.size();i++){
            float houseOffset = houseOffsetList.get(i);
            start_x= radius*3/5 *(float) Math.cos(Math.PI/180 * (houseOffset+180));
            start_y= radius*3/5 *(float) Math.sin(Math.PI/180 * (houseOffset+180));
            end_x= radius*29/40 *(float) Math.cos(Math.PI/180 * (houseOffset+180));
            end_y= radius*29/40 *(float) Math.sin(Math.PI/180 * (houseOffset+180));
            start_x+=mBounds.centerX();
            end_x+=mBounds.centerX();
            start_y+=mBounds.centerY();
            end_y+=mBounds.centerY();
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);

            float numOffset = houseOffsetNum.get(i);
            numOffset += 180;
            numOffset = numOffset%360;
            itemText = numHouse[i];
            text_x = radius*33/50 *(float) Math.cos(Math.PI/180 * (numOffset));
            text_y = radius*33/50 *(float) Math.sin(Math.PI/180 * (numOffset));

            text_x+=mBounds.centerX();
            text_y+=mBounds.centerY();

            textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);

            drawText(canvas,itemText,text_x-textRect.width()/2,text_y+textRect.height()/2,textPaint,0);



        }
    }

    private void initDrawData(HORChartResult result){
//        LogCustom.show(result.getHouseDatas().toString());
        houseDatas = result.getHouseDatas();
        planetDatas = result.getPlanetDatas();
        aspects = result.getAspectDatas();
        offset = (float) houseDatas.get(0).getOffsetAngle();

        firstPos = houseDatas.get(0).getSign().getId();
        posStr = new String[12];

        horList = new int[12];
        horArray = new SparseArray<>();
        for (int i=0;i<12;i++){
            int num = firstPos + i;
            if (num>=12){
                num = num - 12;
            }
            posStr[i] = symbol[num];
            horList[i] = num;
            horArray.put(num,(12 - i)*30 + offset);
        }

        //宫位开始坐标集合
        houseOffsetList = new ArrayList<>();
        for (int i = 0;i<houseDatas.size();i++){
            int sign = houseDatas.get(i).getSign().getId();
            float temp = horArray.get(sign) - (float)houseDatas.get(i).getOffsetAngle();
            if (i!=0) temp = temp%360;
            houseOffsetList.add(temp);
        }

        //宫位数字坐标集合
        houseOffsetNum = new ArrayList<>();
        for (int i =0;i<12;i++){
            float startF = houseOffsetList.get(i);
            int nextNum = i+1;
            if (nextNum==12){
                startF -= 360;
                nextNum=0;
            }
            float endF = houseOffsetList.get(nextNum);
            float centerF = (startF+endF)/2;
//            Log.i("TAG","centerF:"+centerF);
            houseOffsetNum.add(centerF);
        }

        LogCustom.show(TAG, "houseOffsetList:"+houseOffsetList.toString());
        LogCustom.show(TAG, "houseOffsetNum:"+houseOffsetNum.toString());

        //主星盘行星数据
        planetOffsetList = new ArrayList<>();
        float[] fs = new float[planetDatas.size()];
        for (int i=0;i<planetDatas.size();i++){
            HORPlanetData pd = planetDatas.get(i);
            int horIdx = pd.getSign().getId();
            float planetOffset = horArray.get(horIdx) - (float)pd.getOffsetAngle();
            planetOffsetList.add(planetOffset);
            fs[i] = planetOffset;
        }
        planetRevise = getReviseForOffsets(planetOffsetList);
        LogCustom.i("TAG","planetOffsetList:"+planetOffsetList.toString());
        LogCustom.i("TAG","planetRevise:"+ Arrays.toString(planetRevise));
    }

    private void initDrawData(HORChartResult result, HORChartResult result2){

        houseDatas = result.getHouseDatas();
        planetDatas = result.getPlanetDatas();


        offset = (float) houseDatas.get(0).getOffsetAngle();
        firstPos = houseDatas.get(0).getSign().getId();
        posStr = new String[12];
        horList = new int[12];
        horArray = new SparseArray<>();
        for (int i=0;i<12;i++){
            int num = firstPos + i;
            if (num>=12){
                num = num - 12;
            }
            posStr[i] = symbol[num];
            horList[i] = num;
            horArray.put(num,(12 - i)*30 + offset);
        }

        //宫位开始坐标集合
        houseOffsetList = new ArrayList<>();
        for (int i = 0;i<houseDatas.size();i++){
            int sign = houseDatas.get(i).getSign().getId();
            houseOffsetList.add(horArray.get(sign) - (float)houseDatas.get(i).getOffsetAngle());
        }

        //宫位数字坐标集合
        houseOffsetNum = new ArrayList<>();
        for (int i =0;i<12;i++){
            float startF = houseOffsetList.get(i);
            int nextNum = i+1;
            if (nextNum==12){
                startF -= 360;
                nextNum=0;
            }
            float endF = houseOffsetList.get(nextNum);
            float centerF = (startF+endF)/2;
//            Log.i("TAG","centerF:"+centerF);
            houseOffsetNum.add(centerF);
        }

        //主星盘行星数据
        if (result2!=null){
            planetDatas2 = result2.getPlanetDatas();
            aspects = HORUtils.calculatorSynastryAspectDatasWithMainPlanetDatas(result.getPlanetDatas(), result2.getPlanetDatas());
            LogCustom.show("合盘连线："+aspects.toString());
            planetOffsetList_2 = new ArrayList<>();
            for (int i=0;i<planetDatas2.size();i++){
                HORPlanetData pd = planetDatas2.get(i);
                int horIdx = pd.getSign().getId();
                float planetOffset = horArray.get(horIdx) - (float)pd.getOffsetAngle();
                planetOffsetList_2.add(planetOffset);
            }
            planetRevise2 = getReviseForOffsets(planetOffsetList_2);
        } else {
            planetDatas2 = null;
            aspects = null;
        }

        planetOffsetList = new ArrayList<>();
        for (int i=0;i<planetDatas.size();i++){
            HORPlanetData pd = planetDatas.get(i);
            int horIdx = pd.getSign().getId();
            float planetOffset = horArray.get(horIdx) - (float)pd.getOffsetAngle();
            planetOffsetList.add(planetOffset);
        }
        planetRevise = getReviseForOffsets(planetOffsetList);
    }

    public void reDraw(HORChartResult result, int type){
        this.result = result;
        AST_TYPE = type;
        initDrawData(result);
        invalidate();
    }
    public void reDraw(HORChartResult result, HORChartResult result2, int type){
        this.result = result;
        this.result2 = result2;
        AST_TYPE = type;
        initDrawData(result, result2);
        invalidate();
    }

    private float getPlanet2Seat(){
       return radius*7/20;
    }

    private float getPlanetSeat(){
       return radius*3/5;
    }

    void drawDashed(Canvas canvas, int colorRes, float sx, float sy, float ex, float ey){
        Paint paint = new Paint();
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(colorRes);
        Path path = new Path();
        path.moveTo(sx, sy);
        path.lineTo(ex, ey);
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    void drawCycle(Canvas canvas){
        float startPercent = 0;
        float sweepPercent = 0;
//        canvas.rotate(0,mBounds.centerX(),mBounds.centerY());
        for (int i = 0; i < 12; i++) {
            int colorPos = i%4;
            cyclePaint.setColor(colorCircles[colorPos]);
            startPercent = i*30;
            //这里采用比例占100的百分比乘于360的来计算出占用的角度，使用先乘再除可以算出值
            sweepPercent = (i + 1)*30;
            canvas.drawArc(new RectF(30, 30, radius*2, radius*2), startPercent, sweepPercent, false, cyclePaint);
//            canvas.drawArc(new RectF(30, 30, radius*2, radius*2), 0, 360, false, cyclePaint);
        }

//        cyclePaint.setColor(colorCircles[0]);
//        canvas.drawArc(new RectF(30, 30, radius*2, radius*2), 0, 360, false, cyclePaint);
    }

    void drawText(Canvas canvas , String text , float x , float y, Paint paint , float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(getLeft(),getTop(),getRight(),getBottom());
        width = mBounds.right - mBounds.left;
        height = mBounds.bottom - mBounds.top;
        if (width<height){
            radius = width/2 - width/20;
        }else {
            radius = height/2 - height/20;
        }
        cycleBorderWidth = radius/5;
        /*smallLength = 10;
        largeLength = 20;*/
    }

    private int getGuardPlanet(int horIdx){
        int planetIdx = 0;
        switch (horIdx){
            case 0:
                planetIdx = 4;
                break;
            case 1:
                planetIdx = 3;
                break;
            case 2:
                planetIdx = 2;
                break;
            case 3:
                planetIdx = 1;
                break;
            case 4:
                planetIdx = 0;
                break;
            case 5:
                planetIdx = 2;
                break;
            case 6:
                planetIdx = 3;
                break;
            case 7:
                planetIdx = 9;
                break;
            case 8:
                planetIdx = 8;
                break;
            case 9:
                planetIdx = 7;
                break;
            case 10:
                planetIdx = 6;
                break;
            case 11:
                planetIdx = 5;
                break;
        }
        return planetIdx;
    }


    private final int LINE_TYPE_1 = 1;//实线
    private final int LINE_TYPE_2 = 2;//虚线
    private int getLineType(HORAspect aspect){
        int lineType = -1;
        int type = aspect.getType().getId();
        double orb = aspect.getOrb();
        switch (type){
            case 0:
                if (orb<=3){
                    lineType = LINE_TYPE_1;
                } else if (orb>3&&orb<=6){
                    lineType = LINE_TYPE_2;
                }
                break;
            case 1:
                if (orb<=3){
                    lineType = LINE_TYPE_1;
                } else if (orb>3&&orb<=5){
                    lineType = LINE_TYPE_2;
                }
                break;
            case 2:
                if (orb<=3){
                    lineType = LINE_TYPE_1;
                } else if (orb>3&&orb<=5){
                    lineType = LINE_TYPE_2;
                }
                break;
            case 3:
                if (orb<=3){
                    lineType = LINE_TYPE_1;
                } else if (orb>3&&orb<=5){
                    lineType = LINE_TYPE_2;
                }
                break;
            case 4:
                if (orb<=2){
                    lineType = LINE_TYPE_1;
                } else if (orb>2&&orb<=4){
                    lineType = LINE_TYPE_2;
                }
                break;
        }
        return lineType;
    }

    //修正角度
    private float[] getReviseForOffsets(List<Float> list){
        float[] result = new float[list.size()];
        ArrayList<ReviseEntity> reviseEntities = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            ReviseEntity entity = new ReviseEntity();
            entity.setPosition(i);
            entity.setOffset(list.get(i));
            reviseEntities.add(entity);
        }
        Collections.sort(reviseEntities,new Comparator<ReviseEntity>() {
            @Override
            public int compare(ReviseEntity o1, ReviseEntity o2) {
                int r;
                if (o1.getOffset() > o2.getOffset()){
                    r = 1;
                }else {
                    r = -1;
                }
                return r;
            }
        });
        LogCustom.show("TAG","reviseEntities:"+reviseEntities.toString());
        for (int i=0;i<reviseEntities.size();i++){
            if (i!=0){
                float e1 = reviseEntities.get(i-1).getOffset();
                float e2 = reviseEntities.get(i).getOffset();
                float f = Math.abs(e1-e2);
                if (f<10){
                    float r_offset = reviseEntities.get(i-1).getOffset()+10;
                    reviseEntities.get(i).setOffset(r_offset);
                }
            }
            result[reviseEntities.get(i).getPosition()] = reviseEntities.get(i).getOffset();
        }

        return result;
    }

    private class PointEntity{
        float x,y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

    }

    private class ReviseEntity{
        int position;
        float offset;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public float getOffset() {
            return offset;
        }

        public void setOffset(float offset) {
            this.offset = offset;
        }

        @Override
        public String toString() {
            return "{" +
                    "position:" + position +
                    ", offset:" + offset +
                    '}';
        }
    }

}