package me.matamor.custominventories.utils.viewer;

import java.util.*;

public class Pagination<T> extends ArrayList<T> {

    private int pageSize;

    public Pagination(int pageSize) {
        this.pageSize = pageSize;
    }

    @SafeVarargs
    public Pagination(int pageSize, T... objects) {
        this(pageSize, Arrays.asList(objects));
    }

    public Pagination(int pageSize, Collection<T> objects) {
        this.pageSize = pageSize;

        addAll(objects);
    }

    public int pageSize() {
        return pageSize;
    }

    public int totalPages() {
        return (int) Math.ceil((double) size() / pageSize);
    }

    public boolean exists(int page) {
        return !(page < 0) && page < totalPages();
    }

    public List<T> getPage(int page) {
        if (page < 0 || page >= totalPages()) throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + totalPages());

        int min = page * this.pageSize;
        int max = ((page * this.pageSize) + this.pageSize);

        if (max > size()) {
            max = size();
        }

        return subList(min, max);
    }
}
