package com.xu.pet.core;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 自定义业务分页
 *
 * @author xuqingf
 * @date 2023/2/20
 */
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = 3007258205642349200L;
    private List<T> list;
    private long total;
    private long pages;

    public PageInfo() {
        this.list = Collections.emptyList();
    }

    public PageInfo(List<T> list) {
        if (null == list) {
            this.list = Collections.emptyList();
        } else {
            this.list = list;
            this.pages = 1L;
            this.total = (long) list.size();
        }

    }

    public PageInfo(List<T> list, int total, int sizes) {
        this.list = null == list ? Collections.emptyList() : list;
        this.pages = ((long) (total + sizes) - 1L) / (long) sizes;
        this.total = (long) total;
    }

    public PageInfo(List<T> list, long total, int sizes) {
        this.list = null == list ? Collections.emptyList() : list;
        this.pages = (total + (long) sizes - 1L) / (long) sizes;
        this.total = total;
    }

    public PageInfo(List<T> list, long total, long pages) {
        this.list = null == list ? Collections.emptyList() : list;
        this.pages = pages;
        this.total = total;
    }

    public PageInfo(long total, long pages) {
        this.list = Collections.emptyList();
        this.pages = pages;
        this.total = total;
    }

    public static <T> PageInfoBuilder<T> builder() {
        return new PageInfoBuilder();
    }

    public List<T> getList() {
        return this.list;
    }

    public long getTotal() {
        return this.total;
    }

    public long getPages() {
        return this.pages;
    }

    public void setList(final List<T> list) {
        this.list = list;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public void setPages(final long pages) {
        this.pages = pages;
    }


    protected boolean canEqual(final Object other) {
        return other instanceof PageInfo;
    }

    public static class PageInfoBuilder<T> {
        private List<T> list;
        private long total;
        private long pages;

        PageInfoBuilder() {
        }

        public PageInfoBuilder<T> list(final List<T> list) {
            this.list = list;
            return this;
        }

        public PageInfoBuilder<T> total(final long total) {
            this.total = total;
            return this;
        }

        public PageInfoBuilder<T> pages(final long pages) {
            this.pages = pages;
            return this;
        }

        public PageInfo<T> build() {
            return new PageInfo(this.list, this.total, this.pages);
        }

        public String toString() {
            return "PageInfo.PageInfoBuilder(list=" + this.list + ", total=" + this.total + ", pages=" + this.pages + ")";
        }
    }
}
