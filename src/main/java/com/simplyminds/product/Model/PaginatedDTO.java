package com.simplyminds.product.Model;

import java.util.List;


public class PaginatedDTO<T> {
    private T Objects;
    private int totalObjects;
    private int totalPages;
    private int currentPage;
    private int currentSize;

    public PaginatedDTO(T objects, int totalObjects, int totalPages, int currentPage, int currentSize) {
        Objects = objects;
        this.totalObjects = totalObjects;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.currentSize = currentSize;
    }

    public T getObjects() {
        return Objects;
    }

    public void setObjects(T objects) {
        Objects = objects;
    }

    public int getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(int totalObjects) {
        this.totalObjects = totalObjects;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }
}
