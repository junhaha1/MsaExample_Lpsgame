package com.junha.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.junha.common.GameResult;
import com.junha.common.RockPaperScissors;
import com.junha.common.RpsRule;
import com.junha.domain.RpsChallenge;
import com.junha.domain.User;
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
		/*static 메소드 사용하기*/
		return RpsRule.checkMap.get(userRps).get(computerRps);
		
		/*강사 리팩토링*/
		/*조건문을 사용하진 않지만 리소스 낭비가 심함.*/
//		Map<RockPaperScissors, GameResult> userRock = new HashMap<>();
//		userRock.put(RockPaperScissors.PAPER, GameResult.LOST);
//		userRock.put(RockPaperScissors.ROCK, GameResult.TIE);
//		userRock.put(RockPaperScissors.SCISSORS, GameResult.WON);
//		
//		Map<RockPaperScissors, GameResult> userPaper = new HashMap<>();
//		userPaper.put(RockPaperScissors.PAPER, GameResult.TIE);
//		userPaper.put(RockPaperScissors.ROCK, GameResult.WON);
//		userPaper.put(RockPaperScissors.SCISSORS, GameResult.LOST);
//		
//		Map<RockPaperScissors, GameResult> userScissors = new HashMap<>();
//		userScissors.put(RockPaperScissors.PAPER, GameResult.WON);
//		userScissors.put(RockPaperScissors.ROCK, GameResult.LOST);
//		userScissors.put(RockPaperScissors.SCISSORS, GameResult.TIE);
//		
//		Map<RockPaperScissors, Map<RockPaperScissors, GameResult>> checkMap = new HashMap<>();
//		checkMap.put(RockPaperScissors.ROCK, userRock);
//		checkMap.put(RockPaperScissors.PAPER, userPaper);
//		checkMap.put(RockPaperScissors.SCISSORS, userScissors);
//		return checkMap.get(userRps).get(computerRps);
		
		
		/*직접 리팩토링 코드 HashMap 사용*/
//		GameResult result = GameResult.LOST;
//		Map<RockPaperScissors, RockPaperScissors> rpsMap = new HashMap<>();
//		
//		rpsMap.put(RockPaperScissors.SCISSORS, RockPaperScissors.PAPER);
//		rpsMap.put(RockPaperScissors.ROCK, RockPaperScissors.SCISSORS);
//		rpsMap.put(RockPaperScissors.PAPER, RockPaperScissors.ROCK);
//		
//		if (userRps == computerRps) {
//			result = GameResult.TIE;
//		} else {
//			if (rpsMap.get(userRps) == computerRps) {
//				result = GameResult.WON;
//			} else {
//				result = GameResult.LOST;
//			}
//		}
		
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
	
	@Transactional //save를 여러번 할때 중간에 에러나면 롤백하여 전체 취소
	public Map<String, String> checkChallenge(RpsChallenge rpsChallenge){
		Map<String, String> map = new HashMap<String, String>();
		Optional<User> user = userRepository.findByAlias(rpsChallenge.getUser().getAlias());
		
		Assert.isNull(rpsChallenge.getGameResult(), "완료된 상태를 보낼 수 없습니다.");
		RockPaperScissors computerChoice = createRandomRps();
		GameResult gameResult = checkScore(rpsChallenge.getRps().getChallenge(), computerChoice);
		
		RpsChallenge checkedChallenge = new RpsChallenge(
				user.orElse(rpsChallenge.getUser()), 
				rpsChallenge.getRps(),
				computerChoice, 
				gameResult
			);
		rpsChallengeRepository.save(checkedChallenge);
		
		map.put("opponent", computerChoice.getCommentary()); //컴퓨터 가위바위보
		map.put("outcome", checkedChallenge.getGameResult().getCommentary()); //결과
		map.put("userId", "" + checkedChallenge.getUser().getId());
		
		return map;
	}
	public List<RpsChallenge> getStatsForUser(String userAlias){
		return rpsChallengeRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}
}
