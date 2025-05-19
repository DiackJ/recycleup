package com.hyperskill.customerFeedback.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.ArrayList;

public class PageDTO<MyEntity> {
    private int totalDocuments;
    private boolean isFirstPage;
    private boolean isLastPage;
    @JsonIgnore
    private int currentPage;
    @JsonIgnore
    private int totalPages;
    @JsonIgnore
    private List<MyEntity> documents;
    private Page<MyEntity> pages;

    public PageDTO(){
        this.documents = new ArrayList<>();
    }


    public int getTotalDocuments(){
        return this.totalDocuments;
    }
    public void setTotalDocuments(int totalDocuments){
        this.totalDocuments = totalDocuments;
    }

    public void setFirstPage(boolean isFirstPage){
        this.isFirstPage = isFirstPage;
    }

    public void setLastPage(boolean isLastPage){
        this.isLastPage = isLastPage;
    }

    public void setDocuments(List<MyEntity> documents){
        this.documents = documents;
    }

    public int getCurrentPage(){
        return this.currentPage;
    }
    public void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
    }

    public int getTotalPages(){
        return this.totalPages;
    }
    public void setTotalPages(int totalPages){
        this.totalPages = totalPages;
    }

    public void setPages(Page<MyEntity> pages){
        this.pages = pages;
    }
}
