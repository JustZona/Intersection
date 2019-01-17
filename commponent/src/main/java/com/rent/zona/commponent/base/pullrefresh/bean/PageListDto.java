package com.rent.zona.commponent.base.pullrefresh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class PageListDto<T> {
    public static final String PAGE_KEY = "NextString";
    public static final String DATA_LIST_KEY = "DataList";
    public static final String DATA_USER_LIST = "UserList";

//    @SerializedName(PAGE_KEY)
//    public String pageKey;

    @SerializedName(DATA_LIST_KEY)
    public ArrayList<T> dataList;

//    @SerializedName(DATA_USER_LIST)
//    public ArrayList<T> userlist;

    @SerializedName("Total")
    public int total;

    public int pageIndex;
    public int pageSize;

//    public String getPageKey() {
//        return pageKey;
//    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    /**
     * pageindex 从0开始
     * @return
     */
    public boolean hasNoMore() {
//        return pageIndex>=getPageCount()-1;
        return (dataList!=null?dataList.size():0)<pageSize;
    }

    public int getPageCount(){
        if(total>0 && pageSize>0){
            return total/pageSize+(total%pageSize>0?1:0);
        }
        return 0;
    }
//    public PageListDto(String pageKey, ArrayList<T> dataList, int total) {
//        this.pageKey = pageKey;
//        this.dataList = dataList;
//        this.total = total;
//    }


    //    public PageListDto(String pageKey, ArrayList<T> dataList, ArrayList<T> userlist, int total) {
//        this.pageKey = pageKey;
//        this.dataList = dataList;
//        this.userlist = userlist;
//        this.total = total;
//    }

    public PageListDto() {
    }

    public PageListDto(ArrayList<T> dataList, int total, int pageIndex, int pageSize) {
        this.dataList = dataList;
        this.total = total;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
