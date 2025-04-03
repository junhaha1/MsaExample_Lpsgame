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
import com.junha.event.EventDispatcher;
import com.junha.event.RpsSolvedEvent;
import com.junha.repository.RpsChallengeRepository;
import com.junha.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RpsService {
	private final RandomGeneratorService randomGeneratorService;
	private final RpsChallengeRepository rpsChallengeRepository;
	private final UserRepository userRepository;
	private final EventDispatcher eventDispatcher;
	
	private RockPaperScissors createRandomRps() {
		return randomGeneratorService.getRockPaperScissors();
	}
	
	private GameResult checkScore(RockPaperScissors userRps, RockPaperScissors computerRps) {
		/*static 메소드 사용하기*/
		return RpsRule.checkMap.get(userRps).get(computerRps);
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
		
		//이벤트 달성 코드 추가
		eventDispatcher.send(new RpsSolvedEvent(
				checkedChallenge.getId(), 
				checkedChallenge.getUser().getId(),
				checkedChallenge.getUser().getAlias(), 
				checkedChallenge.getGameResult().getCommentary()
				));
		
		map.put("opponent", computerChoice.getCommentary()); //컴퓨터 가위바위보
		map.put("outcome", checkedChallenge.getGameResult().getCommentary()); //결과
		map.put("userId", "" + checkedChallenge.getUser().getId());
		
		return map;
	}
	public List<RpsChallenge> getStatsForUser(String userAlias){
		return rpsChallengeRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}
}
