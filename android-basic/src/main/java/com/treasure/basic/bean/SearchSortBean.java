package com.treasure.basic.bean;

/**
 * date:2023/2/8
 * author:李超(licha)
 * function:
 */
public class SearchSortBean {
    private String sortName;
    private String sortId;

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public SearchSortBean(String sortName, String sortId) {
        this.sortName = sortName;
        this.sortId = sortId;
    }
}
