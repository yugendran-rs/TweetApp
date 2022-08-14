package com.tweetapp.dao;

import java.util.List;
import java.util.Optional;

import com.tweetapp.exception.UserExistsException;

public interface Crud<T> {

	public Optional<T> findById(Long id);

	public T save(T obj) throws UserExistsException;

	public int update(T obj);

	public List<T> findAll();

}
