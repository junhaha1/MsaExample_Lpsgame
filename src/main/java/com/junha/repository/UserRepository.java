package com.junha.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junha.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByAlias(final String alias);
}
