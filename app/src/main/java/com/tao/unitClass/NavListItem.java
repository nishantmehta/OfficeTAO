package com.tao.unitClass;

/**
 * Created by nishantmehta.n on 3/29/14.
 */
public class NavListItem {
    String title;
    int icon;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    int flag;

    public long getFormID() {
        return formID;
    }

    public void setFormID(long formID) {
        this.formID = formID;
    }

    long formID;

    public NavListItem(int icon, String title,long formID,int flag) {
        this.icon = icon;
        this.title = title;
        this.formID=formID;
        this.flag=flag;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
