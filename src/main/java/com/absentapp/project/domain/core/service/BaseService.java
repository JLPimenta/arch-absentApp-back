package com.absentapp.project.domain.core.service;

import com.absentapp.project.domain.core.entity.BaseEntity;
import com.absentapp.project.domain.core.exception.DomainException;
import com.absentapp.project.domain.core.repository.BaseRepository;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Transactional(readOnly = true)
public abstract class BaseService<T extends BaseEntity> implements IBaseService<T> {

    private final BaseRepository<T> repository;

    protected BaseService(final BaseRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public T create(T entity) throws DomainException {
        validate(entity);
        return this.repository.save(entity);
    }

    @Override
    public T findById(String id) throws DomainException {
        return this.repository.findById(id).orElseThrow(() -> new DomainException("Item não encontrado."));
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public List<T> findAll(@Nullable Specification<T> specification, Sort sort) throws DomainException {
        return this.repository.findAll(specification, sort);

        //TODO: Implementar Specification
    }

    @Override
    public Page<T> findAll(Pageable pageable) throws DomainException {
        return this.repository.findAll(pageable);
    }

    @Override
    public T update(T entity, String id) throws DomainException {
        T existent = repository.findById(id).orElseThrow(() -> new DomainException("Item não encontrado"));

        bind(existent, entity);

        return repository.save(existent);
    }

    @Override
    public void bind(T entity, T update) {
        BeanUtils.copyProperties(update, entity, "id");
    }

    @Override
    public void delete(String id) {
        this.repository.deleteById(id);
    }

    @Override
    public void validate(T entity) throws DomainException {
    }

}
