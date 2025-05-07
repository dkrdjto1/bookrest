package com.example.myapplication.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InterParkBookVo {
    @SerializedName("title")
    private String title;
    @SerializedName("query")
    private String query;
    @SerializedName("link")
    private String link;
    @SerializedName("language")
    private String language;
    @SerializedName("copyright")
    private String copyright;
    @SerializedName("pubDate")
    private String pubDate;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("totalResults")
    private String totalResults;
    @SerializedName("startIndex")
    private String startIndex;
    @SerializedName("itemsPerPage")
    private String itemsPerPage;
    @SerializedName("maxResults")
    private String maxResults;
    @SerializedName("queryType")
    private String queryType;
    @SerializedName("searchCategoryId")
    private String searchCategoryId;
    @SerializedName("searchCategoryName")
    private String searchCategoryName;
    @SerializedName("returnCode")
    private String returnCode;
    @SerializedName("returnMessage")
    private String returnMessage;
    @SerializedName("item")
    private ArrayList<InterParkItemVo> items;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(String itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getSearchCategoryId() {
        return searchCategoryId;
    }

    public void setSearchCategoryId(String searchCategoryId) {
        this.searchCategoryId = searchCategoryId;
    }

    public String getSearchCategoryName() {
        return searchCategoryName;
    }

    public void setSearchCategoryName(String searchCategoryName) {
        this.searchCategoryName = searchCategoryName;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public ArrayList<InterParkItemVo> getItems() {
        return items;
    }

    public void setItems(ArrayList<InterParkItemVo> items) {
        this.items = items;
    }
}
