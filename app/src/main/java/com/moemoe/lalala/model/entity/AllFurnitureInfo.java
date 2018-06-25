package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/5/30.
 */

public class AllFurnitureInfo {

    private String describe;//家具描述
    private String detailIcon;//图标
    private String furnitureType;//家具类型
    private String id;
    private String image;//家具图片

    private boolean isPutInHouse;// 家具是否（使用）放入宅屋，true：已经放入宅屋 ,
    private boolean isUserFurnitureHad;//家具是否拥有，true：拥有当前家具

    private String name;//名称
    private String schema;//跳转地址
    private String schemaShop;//家具跳转商店
    private String furnitureProductId;

    private boolean isSuitPutInHouse;//套装是否（使用）放入宅屋，true：已经放入宅屋 ,
    private boolean isUserSuitFurnitureHad;// 套装是否拥有，true：拥有当前家具 ,


    private String suitTypeDescribe;//套装风格描述
    private String suitTypeImage;//套装风格封面图片
    private String suitTypeName;//套装风格名称
    private String suitTypeSchemaShop;//套装家具跳转
    private String type;//区分套装
    private String furnitureSuitProductId;
    private int sortId;//排序值
    private String suitTypeId;// 套装风格ID ,
    private int position;

    public void setFurnitureProductId(String furnitureProductId) {
        this.furnitureProductId = furnitureProductId;
    }

    public String getFurnitureProductId() {
        return furnitureProductId;
    }

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String suitTypeDetailIcon;//套装图标

    public String getSuitTypeDetailIcon() {
        return suitTypeDetailIcon;
    }

    public void setSuitTypeDetailIcon(String suitTypeDetailIcon) {
        this.suitTypeDetailIcon = suitTypeDetailIcon;
    }

    public String getSuitTypeSchemaShop() {
        return suitTypeSchemaShop;
    }

    public void setSuitTypeSchemaShop(String suitTypeSchemaShop) {
        this.suitTypeSchemaShop = suitTypeSchemaShop;
    }


    public String getDetailIcon() {
        return detailIcon;
    }

    public void setDetailIcon(String detailIcon) {
        this.detailIcon = detailIcon;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchemaShop() {
        return schemaShop;
    }

    public void setSchemaShop(String schemaShop) {
        this.schemaShop = schemaShop;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSortId() {
        return sortId;
    }

    public String getSuitTypeId() {
        return suitTypeId;
    }

    public void setSuitTypeId(String suitTypeId) {
        this.suitTypeId = suitTypeId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPutInHouse() {
        return isPutInHouse;
    }

    public void setPutInHouse(boolean putInHouse) {
        isPutInHouse = putInHouse;
    }

    public boolean isUserFurnitureHad() {
        return isUserFurnitureHad;
    }

    public void setUserFurnitureHad(boolean userFurnitureHad) {
        isUserFurnitureHad = userFurnitureHad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuitPutInHouse() {
        return isSuitPutInHouse;
    }

    public boolean isUserSuitFurnitureHad() {
        return isUserSuitFurnitureHad;
    }

    public String getSuitTypeDescribe() {
        return suitTypeDescribe;
    }

    public String getSuitTypeImage() {
        return suitTypeImage;
    }

    public String getSuitTypeName() {
        return suitTypeName;
    }

    public void setSuitPutInHouse(boolean suitPutInHouse) {
        isSuitPutInHouse = suitPutInHouse;
    }

    public void setUserSuitFurnitureHad(boolean userSuitFurnitureHad) {
        isUserSuitFurnitureHad = userSuitFurnitureHad;
    }

    public void setSuitTypeDescribe(String suitTypeDescribe) {
        this.suitTypeDescribe = suitTypeDescribe;
    }

    public void setSuitTypeImage(String suitTypeImage) {
        this.suitTypeImage = suitTypeImage;
    }

    public void setSuitTypeName(String suitTypeName) {
        this.suitTypeName = suitTypeName;
    }

    public String getFurnitureSuitProductId() {
        return furnitureSuitProductId;
    }

    public void setFurnitureSuitProductId(String furnitureSuitProductId) {
        this.furnitureSuitProductId = furnitureSuitProductId;
    }
}
