package com.junha.service;

import org.springframework.stereotype.Service;

import com.junha.common.RockPaperScissors;

@Service
public class RandomGeneratorService {
	public RockPaperScissors getRockPaperScissors() {
		return RockPaperScissors.randomRps();
	}
}
