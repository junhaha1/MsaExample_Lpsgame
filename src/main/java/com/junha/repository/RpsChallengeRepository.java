package com.junha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junha.domain.RpsChallenge;

public interface RpsChallengeRepository extends JpaRepository<RpsChallenge, Long>{
	List<RpsChallenge> findTop5ByUserAliasOrderByIdDesc(String userAlias);

}
