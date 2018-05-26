package com.moemoe.lalala.view.base;

import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2018/5/16.
 */

public class BranchItemBean {


    private String branchPhoto;
    private String branchBg;
    private String branchName = "莲";

    public String getBranchPhoto() {
        return branchPhoto;
    }

    public void setBranchPhoto(String branchPhoto) {
        this.branchPhoto = branchPhoto;
    }

    public String getBranchBg() {
        return branchBg;
    }

    public void setBranchBg(String branchBg) {
        this.branchBg = branchBg;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BranchItemBean() {
    }

    public BranchItemBean(String branchPhoto, String branchBg, String branchName) {
        this.branchPhoto = branchPhoto;
        this.branchBg = branchBg;
        this.branchName = branchName;
    }


}
