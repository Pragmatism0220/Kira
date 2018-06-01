package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.model.entity.VisitorsEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/6/1.
 */

public class ItemConverter {
    public static List<VisitorsEntity> getData() {
        List<VisitorsEntity> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Date date = new Date();
            date.setMonth(i + 1);

            int limit = random(1, 5);
//            for (int j = 0; j < limit; j++) {
//                VisitorsEntity itemBean = new VisitorsEntity();
//                itemBean.setTitleTime(String.valueOf(date.getTime()));
//                itemBean.s
//            }
        }
        return data;
    }

    private static int random(int start, int end) {
        return new Random().nextInt(end - start + 1) + start;
    }
}
