package com.moemoe.lalala.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.moemoe.lalala.R;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Danmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuFactory;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.android.AndroidFileSource;
import master.flame.danmaku.danmaku.util.DanmakuUtils;

/**
 *
 * Created by yi on 2018/2/2.
 */

public class KiraDanmakuParser extends BaseDanmakuParser {

    static {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
    }

    private float dispScaleX = 0f;
    private float dispScaleY = 0f;

    @Override
    protected IDanmakus parse() {
        if(mDataSource != null){
            AndroidFileSource source = (AndroidFileSource) mDataSource;
            try {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                XmlContentHandler contentHandler = new XmlContentHandler();
                xmlReader.setContentHandler(contentHandler);
                xmlReader.parse(new InputSource(source.data()));
                return contentHandler.result;
            }catch (SAXException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    class XmlContentHandler extends DefaultHandler{
        Danmakus result = new Danmakus();
        BaseDanmaku item;
        boolean completed = false;
        int index = 0;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            completed = true;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            String tagName = !TextUtils.isEmpty(localName)?localName : qName;
            if("d".equals(tagName)){
                // <d p="23.826000213623,1,25,16777215,1422201084,0,057075e9,757076900">我从未见过如此厚颜无耻之猴</d>
                // 0:时间(弹幕出现时间)
                // 1:类型(1从右至左滚动弹幕|6从左至右滚动弹幕|5顶端固定弹幕|4底端固定弹幕|7高级弹幕|8脚本弹幕)
                // 2:字号
                // 3:颜色
                // 4:时间戳 ?
                // 5:弹幕池id
                // 6:用户id
                // 7:弹幕id
                String pValue = attributes.getValue("p");
                String[] values = pValue.split(",");
                if(values.length > 0){
                    long time = (long) (Float.valueOf(values[0]) * 1000);
                    int type = Integer.valueOf(values[1]);
//                    float textSize;
//                    if(1 == Integer.valueOf(values[2])){
//                        textSize = context.getResources().getDimensionPixelSize(R.dimen.x40);
//                    }else if (2 == Integer.valueOf(values[2])){
//                        textSize = context.getResources().getDimensionPixelSize(R.dimen.x30);
//                    }else {
//                        textSize = context.getResources().getDimensionPixelSize(R.dimen.x20);
//                    }
//                    int textColor = StringUtils.readColorStr(values[3], Color.WHITE);
                    String userId = values[6];

                    item = mContext.mDanmakuFactory.createDanmaku(type,mContext);
                    if(item != null){
                        item.setTime(time);
                        item.textSize = Float.valueOf(values[2]);
                        item.textColor = Color.WHITE;
                        item.textShadowColor = Color.WHITE;
                        item.userHash = userId;
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(item != null && item.text != null){
                if(item.duration != null){
                    String tagName = !TextUtils.isEmpty(localName)?localName : qName;
                    if("d".equals(tagName)){
                        item.setTimer(mTimer);
                        item.flags = mContext.mGlobalFlagValues;
                        synchronized (result.obtainSynchronizer()){
                            result.addItem(item);
                        }
                    }
                }
            }
            item = null;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(item != null){

                String decodeString = new String(ch,start,length);
                if (decodeString.contains("&amp;")) {
                    decodeString = decodeString.replace("&amp;", "&");
                }
                if (decodeString.contains("&quot;")) {
                    decodeString = decodeString.replace("&quot;", "\"");
                }
                if (decodeString.contains("&gt;")) {
                    decodeString = decodeString.replace("&gt;", ">");
                }
                if (decodeString.contains("&lt;")) {
                    decodeString = decodeString.replace("&lt;", "<");
                }

                DanmakuUtils.fillText(item,decodeString);
                item.index = index++;
            }
        }
    }

    @Override
    public BaseDanmakuParser setDisplayer(IDisplayer disp) {
        super.setDisplayer(disp);
        dispScaleX = mDispWidth / DanmakuFactory.BILI_PLAYER_WIDTH;
        dispScaleY = mDispHeight / DanmakuFactory.BILI_PLAYER_HEIGHT;
        return this;
    }
}
