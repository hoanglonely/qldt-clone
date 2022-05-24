package com.mb.lab.banks.user.business.dto.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

/**
 * @author trung
 */
public class ListDto<T> {

    @SuppressWarnings({ "rawtypes" })
    private static final ListDto EMPTY_LIST = new ListDto<>(Collections.emptyList(), 0l);

    @SuppressWarnings("unchecked")
    public static final <T> ListDto<T> emptyList() {
        return EMPTY_LIST;
    }

    public static <DOMAIN, T, E extends T> ListDto<T> get(ListCountQuery<DOMAIN, T> query,
            Pageable pageable) {
        List<? extends DOMAIN> domains = query.getList();

        Function<List<? extends DOMAIN>, List<? extends DOMAIN>> beforeConvertFunction = query.getBeforeConvertFunction();
        if (beforeConvertFunction != null) {
            domains = beforeConvertFunction.apply(domains);
        }

        List<T> dtos;
        if (CollectionUtils.isEmpty(domains)) {
            dtos = Collections.emptyList();
        } else {
            dtos = new ArrayList<>(domains.size());
            for (DOMAIN domain : domains) {
                dtos.add(query.convert(domain));
            }
        }

        if (pageable != null && (pageable.getPageNumber() > 0 || dtos.size() >= pageable.getPageSize())) {
            long count = query.count();
            return new ListDto<>(dtos, count);
        } else {
            return new ListDto<>(dtos);
        }
    }

    private List<T> list;
    private Long count;

    public ListDto(List<T> list) {
        this.list = list;
        this.count = (long) list.size();
    }

    public ListDto(List<T> list, Long count) {
        this.list = list;
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public static interface ListCountQuery<DOMAIN, T> {

        public List<? extends DOMAIN> getList();

        public default Function<List<? extends DOMAIN>, List<? extends DOMAIN>> getBeforeConvertFunction() {
            return null;
        }

        public long count();

        public T convert(DOMAIN domain);

    }
}
