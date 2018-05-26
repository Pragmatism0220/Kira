package com.moemoe.lalala.model.entity;

/**
 *
 * Created by yi on 2017/9/23.
 */

public class ShareStreamSendEntity {
    public String folderId;
    public String fileId;
    public String shareText;

    public ShareStreamSendEntity(String folderId, String fileId, String shareText) {
        this.folderId = folderId;
        this.fileId = fileId;
        this.shareText = shareText;
    }
}
