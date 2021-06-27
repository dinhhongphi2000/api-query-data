package org.apiquery.services;

import java.text.ParseException;
import java.util.List;

import org.apiquery.shared.data.PageData;
import org.apiquery.shared.data.QueryAdvanceFilter;
import org.apiquery.shared.data.QueryFilter;
import org.apiquery.shared.exceptions.ServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BaseService<DTO, ENTITY, ID> {
    PageData<DTO> findAll(Pageable pageable, QueryFilter filter, String[] includes);
    PageData<DTO> findAll(Pageable pageable, QueryAdvanceFilter filter, String[] includes);
    long count(QueryFilter filter);
}