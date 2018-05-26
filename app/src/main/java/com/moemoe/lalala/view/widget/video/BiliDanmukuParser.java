package com.moemoe.lalala.view.widget.video;

import android.graphics.Color;

import com.moemoe.lalala.utils.StringUtils;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.Locale;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuFactory;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.android.AndroidFileSource;
import master.flame.danmaku.danmaku.util.DanmakuUtils;

/**
 * Created by yi on 2018/2/6.
 */

public class BiliDanmukuParser extends BaseDanmakuParser {

    static {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
    }

    protected float mDispScaleX;
    protected float mDispScaleY;

    @Override
    public Danmakus parse() {

        if (mDataSource != null) {
            AndroidFileSource source = (AndroidFileSource) mDataSource;
            try {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                XmlContentHandler contentHandler = new XmlContentHandler();
                xmlReader.setContentHandler(contentHandler);
                xmlReader.parse(new InputSource(source.data()));
                return contentHandler.getResult();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public class XmlContentHandler extends DefaultHandler {

        private static final String TRUE_STRING = "true";

        public Danmakus result = new Danmakus();

        public BaseDanmaku item = null;

        public boolean completed = false;

        public int index = 0;

        public Danmakus getResult() {
            return result;
        }

        @Override
        public void startDocument() throws SAXException {

        }

        @Override
        public void endDocument() throws SAXException {
            completed = true;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            String tagName = localName.length() != 0 ? localName : qName;
            tagName = tagName.toLowerCase(Locale.getDefault()).trim();
            if (tagName.equals("d")) {
                // <d p="6.457,1,25.0,-1,0,0,8369d678-7c10-4642-b67d-00c523fbf1c4,44197cda-5e48-4e69-aee8-55610949a1d9">啊啊啊吧</d>
                // <d p="23.826000213623,1,25,16777215,1422201084,0,057075e9,757076900">我从未见过如此厚颜无耻之猴</d>
                // <d p="11.201999664307,1,25,16777215,1422201071,0,Da0c4a4c,757077041">汪汪汪汪汪</d>
                // 0:时间(弹幕出现时间)
                // 1:类型(1从右至左滚动弹幕|6从左至右滚动弹幕|5顶端固定弹幕|4底端固定弹幕|7高级弹幕|8脚本弹幕)
                // 2:字号
                // 3:颜色
                // 4:时间戳 ?
                // 5:弹幕池id
                // 6:用户hash
                // 7:弹幕id
                String pValue = attributes.getValue("p");
                // parse p value to danmaku
                String[] values = pValue.split(",");
                if (values.length > 0) {
                    long time = (long) (Float.parseFloat(values[0]) * 1000); // 出现时间
                    int type = Integer.parseInt(values[1]); // 弹幕类型
                    float textSize = Float.parseFloat(values[2]); // 字体大小
                  //  int color = (int) ((0x00000000ff000000 | Long.parseLong(values[3])) & 0x00000000ffffffff); // 颜色
                    // int poolType = Integer.parseInt(values[5]); // 弹幕池类型（忽略
                    item = mContext.mDanmakuFactory.createDanmaku(type, mContext);
                    if (item != null) {
                        item.setTime(time);
                        item.priority = 8;
                        item.textSize = 1.0f;
                        item.textColor = Color.WHITE;
                        item.textShadowColor = Color.WHITE;// <= Color.BLACK ? Color.WHITE : Color.BLACK;
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (item != null && item.text != null) {
                if (item.duration != null) {
                    String tagName = localName.length() != 0 ? localName : qName;
                    if (tagName.equalsIgnoreCase("d")) {
                        item.setTimer(mTimer);
                        item.flags = mContext.mGlobalFlagValues;
                        Object lock = result.obtainSynchronizer();
                        synchronized (lock) {
                            result.addItem(item);
                        }
                    }
                }
                item = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (item != null) {
                DanmakuUtils.fillText(item, decodeXmlString(new String(ch, start, length)));
                item.index = index++;
            }
        }

        private String decodeXmlString(String title) {
            if (title.contains("&amp;")) {
                title = title.replace("&amp;", "&");
            }
            if (title.contains("&quot;")) {
                title = title.replace("&quot;", "\"");
            }
            if (title.contains("&gt;")) {
                title = title.replace("&gt;", ">");
            }
            if (title.contains("&lt;")) {
                title = title.replace("&lt;", "<");
            }
            return title;
        }

    }

    private boolean isPercentageNumber(float number) {
        return number >= 0f && number <= 1f;
    }

    @Override
    public BaseDanmakuParser setDisplayer(IDisplayer disp) {
        super.setDisplayer(disp);
        mDispScaleX = mDispWidth / DanmakuFactory.BILI_PLAYER_WIDTH;
        mDispScaleY = mDispHeight / DanmakuFactory.BILI_PLAYER_HEIGHT;
        return this;
    }
}
