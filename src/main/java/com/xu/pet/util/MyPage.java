package com.xu.pet.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MyPage<T> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = -3720998571176536865L;
    private List<T> content = new ArrayList<>();
    private long totalElements;
    private int current;
    private int size;
    private boolean hasPrevious;
    private boolean hasNext;
    private boolean first;
    private boolean last;
    private boolean empty;
    private int pages;
    private int numberOfElements;

    public MyPage() {
    }

    // 根据 MyBatis-Plus 的 Page 类构造 MyPage 对象
    public MyPage(Page<T> page) {
        this.content = page.getRecords();
        this.totalElements = page.getTotal();
        this.current = (int) page.getCurrent();
        this.size = (int) page.getSize();
        this.numberOfElements = page.getRecords().size();
        this.pages = (int) page.getPages();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.first = current == 1;
        this.last = current == pages;
    }

    // 是否有前一页
    public boolean hasPrevious() {
        return hasPrevious;
    }

    // 是否有下一页
    public boolean hasNext() {
        return hasNext;
    }

    // 是否第一页
    public boolean isFirst() {
        return first;
    }

    // 是否最后一页
    public boolean isLast() {
        return last;
    }

    // 获取内容
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    // 设置内容
    public void setContent(List<T> content) {
        this.content = content;
    }

    // 是否有内容
    public boolean hasContent() {
        return getNumberOfElements() > 0;
    }

    // 获取单页大小
    public int getSize() {
        return size;
    }

    // 设置单页大小
    public void setSize(int size) {
        this.size = size;
    }

    // 获取全部元素数目
    public long getTotalElements() {
        return totalElements;
    }

    // 设置全部元素数目
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    // 获取当前页号
    public int getCurrent() {
        return current;
    }

    // 设置当前页号
    public void setCurrent(int current) {
        this.current = current;
    }

    // 获取总页数
    public int getPages() {
        return pages;
    }

    // 设置总页数
    public void setPages(int pages) {
        this.pages = pages;
    }

    // 获取单页元素数目
    public int getNumberOfElements() {
        return numberOfElements;
    }

    // 设置单页元素数目
    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    // 判断是否为空
    public boolean isEmpty() {
        return !hasContent();
    }

    // 设置是否为空
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    // 迭代器
    @Override
    public Iterator<T> iterator() {
        return getContent().iterator();
    }
}
