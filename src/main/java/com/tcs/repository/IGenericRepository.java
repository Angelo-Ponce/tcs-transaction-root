package com.tcs.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@NoRepositoryBean
public interface IGenericRepository <T, I> extends ReactiveCrudRepository<T, I> {
}
