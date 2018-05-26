package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/23.
 */

public class DeskmateStageLineEntity {
    private String content;
    private String id;
    private String parentOptionId;
    private String roleName;
    private String schema;
    private ArrayList<String> options;
    private ArrayList<DeskmateStageLineEntity> children;
}
