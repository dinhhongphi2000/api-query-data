package org.apiquery.shared.data;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageData<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int maxItemPerPage;

    public PageData() {}

    public PageData(Page page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.maxItemPerPage = page.getSize();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getNumber() {
        if(data != null)
            return data.size();
        return 0;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxItemPerPage() {
        return maxItemPerPage;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return this.data;
    }
}
