package com.junha.service;

import org.springframework.stereotype.Service;

import com.junha.repository.RpsChallengeRepository;
import com.junha.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RpsService {
	private final RandomGeneratorService randomGeneratorService;
	private final RpsChallengeRepository rpsChallengeRepository;
	private final UserRepository userRepository;
}
