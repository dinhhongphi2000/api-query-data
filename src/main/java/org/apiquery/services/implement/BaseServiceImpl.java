package org.apiquery.services.implement;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apiquery.dtos.*;
import org.apiquery.repositories.*;
import org.apiquery.services.*;
import org.apiquery.shared.data.PageData;
import org.apiquery.shared.data.QueryAdvanceFilter;
import org.apiquery.shared.data.QueryFilter;
import org.apiquery.shared.exceptions.ServiceException;
import org.apiquery.shared.utils.EntityToDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class BaseServiceImpl<DTO, ENTITY, ID> implements BaseService<DTO, ENTITY, ID> {
	BaseRepository<ENTITY, ID> baseRepository;

	private ApplicationContext context;

	@SuppressWarnings("rawtypes")
	private Class clazz;

	public BaseServiceImpl(BaseRepository<ENTITY, ID> baseRepo) {
		baseRepository = baseRepo;
	}

	public BaseServiceImpl(BaseRepository<ENTITY, ID> baseRepo, ApplicationContext context) {
		baseRepository = baseRepo;
		this.context = context;
	}

	protected List<DTO> convertEntityToDtoList(List<ENTITY> entities) throws Exception {
		if (entities == null)
			return null;

		List<DTO> dtos = new ArrayList<>();
		for (ENTITY entity : entities) {
			DTO dto = EntityToDTO.convertTo(getDTOClazz(), entity, null);
			dtos.add(dto);
		}
		return dtos;
	}

	@SuppressWarnings({ "unchecked" })
	private <S extends DTO> Class<S> getDTOClazz() {
		if (this.clazz == null) {
			Type genType = this.getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return null;
			}
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			if (!(params[0] instanceof Class)) {
				return null;
			}
			return (Class<S>) params[0];
		}
		return this.clazz;
	}

	@SuppressWarnings({ "unchecked" })
	private <S extends ENTITY> Class<S> getENTITYClazz() {
		if (this.clazz == null) {
			Type genType = this.getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return null;
			}
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			if (!(params[0] instanceof Class)) {
				return null;
			}
			return (Class<S>) params[1];
		}
		return this.clazz;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageData<DTO> findAll(Pageable pageable, QueryFilter filter, String[] includes) {
		if (pageable == null)
			pageable = PageRequest.of(0, 10);

		if (filter == null) {
			filter = new QueryFilter();
		}

		Specification specification = filter.createSpecification(getENTITYClazz());

		Page<ENTITY> entitiesPage = baseRepository.findAll(specification, pageable);
		PageData<DTO> pageData = new PageData<DTO>(entitiesPage);
		List<DTO> dtos = new ArrayList<>();
		try {
			for (ENTITY entity : entitiesPage.getContent()) {
				DTO dto = EntityToDTO.convertTo(getDTOClazz(), entity, includes);
				dtos.add(dto);
			}
			pageData.setData(dtos);
			return pageData;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageData<DTO> findAll(Pageable pageable, QueryAdvanceFilter filter, String[] includes) {
		if (pageable == null)
			pageable = PageRequest.of(0, 10);

		if (filter == null) {
			filter = new QueryAdvanceFilter();
		}

		Specification specification = filter.createSpecification(getENTITYClazz());

		Page<ENTITY> entitiesPage = baseRepository.findAll(specification, pageable);
		PageData<DTO> pageData = new PageData<DTO>(entitiesPage);
		List<DTO> dtos = new ArrayList<>();
		try {
			for (ENTITY entity : entitiesPage.getContent()) {
				DTO dto = EntityToDTO.convertTo(getDTOClazz(), entity, includes);
				dtos.add(dto);
			}
			pageData.setData(dtos);
			return pageData;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public long count(QueryFilter filter) {
		if (filter == null) {
			filter = new QueryFilter();
		}

		Specification specification = filter.createSpecification(getENTITYClazz());
		return baseRepository.count(specification);
	}
}