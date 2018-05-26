package com.moemoe.lalala.model.entity;

/**
 * Created by Sora on 2018/3/8.
 */

public class DocSearchEntity {
    //    comments (integer, optional): 评论数 ,
//    createTime (string, optional): 帖子创建时间,格式yyyy-MM-dd HH:ss ,
//    createUserId (string, optional): 创建用户ID ,
//    createUserName (string, optional): 发帖人 ,
//    desc (string, optional): 帖子内容 ,
//    docId (string, optional): 帖子ID ,
//    docType (string, optional): 发帖类型 ,
//    docTypeSchema (string, optional): 跳转区域 ,
//    image (string, optional): 封面 ,
//    likes (integer, optional): 标签点赞数 ,
//    schema (string, optional): schema ,
//    title (string, optional): 标题
    private int comments;
    private String createTime;
    private String createUserId;
    private String createUserName;
    private String desc;
    private String docId;
    private String docType;
    private String docTypeSchema;
    private String image;
    private String schema;
    private String title;
    private int likes;

    public int getComments() {
        return comments;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public String getDesc() {
        return desc;
    }

    public String getDocId() {
        return docId;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocTypeSchema() {
        return docTypeSchema;
    }

    public String getImage() {
        return image;
    }

    public String getSchema() {
        return schema;
    }

    public String getTitle() {
        return title;
    }

    public int getLikes() {
        return likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setDocTypeSchema(String docTypeSchema) {
        this.docTypeSchema = docTypeSchema;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
