package com.treasure.basic.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class AdvancedSearchBean  implements Serializable {

    private ArrayList<String> regionCode = new ArrayList<>(); //地区集合
    private ArrayList<String> parkCode = new ArrayList<>(); //园区集合
    private ArrayList<String> tzxxList = new ArrayList<>(); //投资信心
    private ArrayList<String> tzppdList = new ArrayList<>(); //投资匹配度
    private ArrayList<String> qyjzList = new ArrayList<>(); //企业价值
    private ArrayList<String> tzfxList = new ArrayList<>(); //投资风险
    private ArrayList<String> zsclList = new ArrayList<>(); //招商策略
    private ArrayList<String> lxdhList = new ArrayList<>(); //联系电话
    private ArrayList<String> wtzyList = new ArrayList<>(); //委托资源
    private ArrayList<String> cbrsList = new ArrayList<>(); //委托资源
    private ArrayList<String> pcfxList = new ArrayList<>(); //排除风险
    private ArrayList<String> regTimeSectionList = new ArrayList<>();//注册时间
    private ArrayList<String> regCapitalSectionList = new ArrayList<>();//注册资本
    private ArrayList<String> entScaleCodeList = new ArrayList<>();//企业规模
    private ArrayList<String> entTypeCodeList = new ArrayList<>();//企业类型
    private ArrayList<String> listedTypeList = new ArrayList<>() ;//上市状态
    private ArrayList<String> financingStageList = new ArrayList<>();//融资信息
    private ArrayList<String> leadCodesList = new ArrayList<>();//龙头企业
    private ArrayList<String> qualityCodesList = new ArrayList<>();//优质企业
    private ArrayList<String> qualificationList = new ArrayList<>();//重点资质
    private ArrayList<String> keyAwardCodesList = new ArrayList<>();//重点奖励
    private String contactPhone="";
    private String entrustResource="";
    private ArrayList<String>  insuredNum=new ArrayList<>();
    private ArrayList<String>  excludeRisk=new ArrayList<>();
    private String  industryChainNodeId="";
    private String  industryChainName="";
    private String preciseKeyWord="";
    private String keyWord="";

    public ArrayList<String> getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(ArrayList<String> regionCode) {
        this.regionCode = regionCode;
    }

    public ArrayList<String> getRegTimeSectionList() {
        return regTimeSectionList;
    }

    public void setRegTimeSectionList(ArrayList<String> regTimeSectionList) {
        this.regTimeSectionList = regTimeSectionList;
    }

    public ArrayList<String> getRegCapitalSectionList() {
        return regCapitalSectionList;
    }

    public void setRegCapitalSectionList(ArrayList<String> regCapitalSectionList) {
        this.regCapitalSectionList = regCapitalSectionList;
    }

    public ArrayList<String> getEntScaleCodeList() {
        return entScaleCodeList;
    }

    public void setEntScaleCodeList(ArrayList<String> entScaleCodeList) {
        this.entScaleCodeList = entScaleCodeList;
    }

    public ArrayList<String> getEntTypeCodeList() {
        return entTypeCodeList;
    }

    public void setEntTypeCodeList(ArrayList<String> entTypeCodeList) {
        this.entTypeCodeList = entTypeCodeList;
    }

    public ArrayList<String> getListedTypeList() {
        return listedTypeList;
    }

    public void setListedTypeList(ArrayList<String> listedTypeList) {
        this.listedTypeList = listedTypeList;
    }

    public ArrayList<String> getFinancingStageList() {
        return financingStageList;
    }

    public void setFinancingStageList(ArrayList<String> financingStageList) {
        this.financingStageList = financingStageList;
    }

    public ArrayList<String> getLeadCodesList() {
        return leadCodesList;
    }

    public void setLeadCodesList(ArrayList<String> leadCodesList) {
        this.leadCodesList = leadCodesList;
    }

    public ArrayList<String> getQualityCodesList() {
        return qualityCodesList;
    }

    public void setQualityCodesList(ArrayList<String> qualityCodesList) {
        this.qualityCodesList = qualityCodesList;
    }

    public ArrayList<String> getQualificationList() {
        return qualificationList;
    }

    public void setQualificationList(ArrayList<String> qualificationList) {
        this.qualificationList = qualificationList;
    }

    public ArrayList<String> getKeyAwardCodesList() {
        return keyAwardCodesList;
    }

    public void setKeyAwardCodesList(ArrayList<String> keyAwardCodesList) {
        this.keyAwardCodesList = keyAwardCodesList;
    }

    public ArrayList<String> getParkCode() {
        return parkCode;
    }

    public void setParkCode(ArrayList<String> parkCode) {
        this.parkCode = parkCode;
    }

    public ArrayList<String> getTzxxList() {
        return tzxxList;
    }

    public void setTzxxList(ArrayList<String> tzxxList) {
        this.tzxxList = tzxxList;
    }

    public ArrayList<String> getTzppdList() {
        return tzppdList;
    }

    public void setTzppdList(ArrayList<String> tzppdList) {
        this.tzppdList = tzppdList;
    }

    public ArrayList<String> getQyjzList() {
        return qyjzList;
    }

    public void setQyjzList(ArrayList<String> qyjzList) {
        this.qyjzList = qyjzList;
    }

    public ArrayList<String> getTzfxList() {
        return tzfxList;
    }

    public void setTzfxList(ArrayList<String> tzfxList) {
        this.tzfxList = tzfxList;
    }

    public ArrayList<String> getZsclList() {
        return zsclList;
    }

    public void setZsclList(ArrayList<String> zsclList) {
        this.zsclList = zsclList;
    }

    public ArrayList<String> getLxdhList() {
        return lxdhList;
    }

    public void setLxdhList(ArrayList<String> lxdhList) {
        this.lxdhList = lxdhList;
    }

    public ArrayList<String> getWtzyList() {
        return wtzyList;
    }

    public void setWtzyList(ArrayList<String> wtzyList) {
        this.wtzyList = wtzyList;
    }

    public ArrayList<String> getCbrsList() {
        return cbrsList;
    }

    public void setCbrsList(ArrayList<String> cbrsList) {
        this.cbrsList = cbrsList;
    }

    public ArrayList<String> getPcfxList() {
        return pcfxList;
    }

    public void setPcfxList(ArrayList<String> pcfxList) {
        this.pcfxList = pcfxList;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getEntrustResource() {
        return entrustResource;
    }

    public void setEntrustResource(String entrustResource) {
        this.entrustResource = entrustResource;
    }

    public ArrayList<String> getInsuredNum() {
        return insuredNum;
    }

    public void setInsuredNum(ArrayList<String> insuredNum) {
        this.insuredNum = insuredNum;
    }

    public ArrayList<String> getExcludeRisk() {
        return excludeRisk;
    }

    public void setExcludeRisk(ArrayList<String> excludeRisk) {
        this.excludeRisk = excludeRisk;
    }

    public String getIndustryChainNodeId() {
        return industryChainNodeId;
    }

    public void setIndustryChainNodeId(String industryChainNodeId) {
        this.industryChainNodeId = industryChainNodeId;
    }

    public String getIndustryChainName() {
        return industryChainName;
    }

    public void setIndustryChainName(String industryChainName) {
        this.industryChainName = industryChainName;
    }

    public String getPreciseKeyWord() {
        return preciseKeyWord;
    }

    public void setPreciseKeyWord(String preciseKeyWord) {
        this.preciseKeyWord = preciseKeyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public String toString() {
        return "AdvancedSearchBean{" +
                "regionCode=" + regionCode +
                ", parkCode=" + parkCode +
                ", tzxxList=" + tzxxList +
                ", tzppdList=" + tzppdList +
                ", qyjzList=" + qyjzList +
                ", tzfxList=" + tzfxList +
                ", regTimeSectionList=" + regTimeSectionList +
                ", regCapitalSectionList=" + regCapitalSectionList +
                ", entScaleCodeList=" + entScaleCodeList +
                ", entTypeCodeList=" + entTypeCodeList +
                ", listedTypeList=" + listedTypeList +
                ", financingStageList=" + financingStageList +
                ", leadCodesList=" + leadCodesList +
                ", qualityCodesList=" + qualityCodesList +
                ", qualificationList=" + qualificationList +
                ", keyAwardCodesList=" + keyAwardCodesList +
                '}';
    }
}
