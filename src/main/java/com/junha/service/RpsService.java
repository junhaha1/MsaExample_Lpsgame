package com.junha.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junha.common.GameResult;
import com.junha.common.RockPaperScissors;
import com.junha.repository.RpsChallengeRepository;
import com.junha.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RpsService {
	private final RandomGeneratorService randomGeneratorService;
	private final RpsChallengeRepository rpsChallengeRepository;
	private final UserRepository userRepository;
	
	private RockPaperScissors createRandomRps() {
		return randomGeneratorService.getRockPaperScissors();
	}
	
	private GameResult checkScore(RockPaperScissors userRps, RockPaperScissors computerRps) {
		/*리팩토링 코드 HashMap 사용*/
		GameResult result = GameResult.LOST;
		Map<RockPaperScissors, RockPaperScissors> rpsMap = new HashMap<>();
		
		rpsMap.put(RockPaperScissors.SCISSORS, RockPaperScissors.PAPER);
		rpsMap.put(RockPaperScissors.ROCK, RockPaperScissors.SCISSORS);
		rpsMap.put(RockPaperScissors.PAPER, RockPaperScissors.ROCK);
		
		
		if (userRps == computerRps) {
			result = GameResult.TIE;
		} else {
			if (rpsMap.get(userRps) == computerRps) {
				result = GameResult.WON;
			} else {
				result = GameResult.LOST;
			}
		}
		/*리팩토링 되지 않은 이전 코드*/
//		GameResult result = GameResult.LOST;
//		if (userRps == computerRps) {
//			result = GameResult.TIE;
//		} else if (userRps == RockPaperScissors.SCISSORS) {
//			if (computerRps == RockPaperScissors.ROCK){
//				result = GameResult.LOST;
//			} else {
//				result = GameResult.WON;
//			}
//		} else if (userRps == RockPaperScissors.ROCK) {
//			if (computerRps == RockPaperScissors.SCISSORS){
//				result = GameResult.WON;
//			} else {
//				result = GameResult.LOST;
//			}
//		} else if (userRps == RockPaperScissors.PAPER) {
//			if (computerRps == RockPaperScissors.SCISSORS){
//				result = GameResult.LOST;
//			} else {
//				result = GameResult.WON;
//			}
//		}
	}
}
