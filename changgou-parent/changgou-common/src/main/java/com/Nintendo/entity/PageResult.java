package com.Nintendo.entity;

import java.util.List;

/**
 * Package: com.Nintendo.entity
 * Author: ZZM
 * Date: Created in 2019/8/14 10:57
 **/
public class PageResult<T> {
    private Long total;//总记录数
    private List<T> rows;//记录
    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
