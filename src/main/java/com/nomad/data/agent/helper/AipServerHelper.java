package com.nomad.data.agent.helper;


import com.nomad.data.agent.domain.dao.common.AipServer;
import com.nomad.data.agent.domain.dao.common.AipServerExample;
import com.nomad.data.agent.domain.mappers.common.AipServerMapper;
import com.nomad.data.agent.utils.enums.ServerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AipServerHelper {

	private final AipServerMapper dao;

	/**
	 * 특정 타입의 서버 목록을 조회
	 *
	 * @param type 서버 타입
	 * @return 서버 목록
	 */
	public List<AipServer> findServersBy(ServerType type) {
		AipServerExample example = new AipServerExample();
		{
			example.createCriteria().andServerTpEqualTo(type.getValue());
		}
		return dao.selectByExample(example);
	}

	/**
	 * 타입이 일치하는 첫번째 서버 조회
	 *
	 * @param type 서버 타입
	 * @return 타입이 일치하는 첫번째 서버, 기존코드 호환을 위해 존재하지 않는 경우 비어있는 객체를 반환
	 */
	public AipServer findFirstServerBy(ServerType type) {
		AipServerExample example = new AipServerExample();
		{
			example.createCriteria().andServerTpEqualTo(type.getValue());
			example.setOrderByClause("SEQ");
		}
		return dao.selectByExample(example)
				.stream()
				.findFirst()
				.orElseGet(AipServer::new);
	}

	/**
	 * 타입 및 호스트 명이 일치하는 첫번째 서버 조회
	 *
	 * @param type 서버 타입
	 * @param host 호스트 명
	 * @return 타입 및 호스트 명이 일치하는 첫번째 서버, 기존코드 호환을 위해 존재하지 않는 경우 비어있는 객체를 반환
	 */
	public AipServer findFirstServerBy(ServerType type, String host) {
		AipServerExample example = new AipServerExample();
		{
			example.createCriteria()
					.andServerTpEqualTo(type.getValue())
					.andHostEqualTo(host);
			example.setOrderByClause("SEQ");
		}
		return dao.selectByExample(example)
				.stream()
				.findFirst()
				.orElseGet(AipServer::new);
	}

	/**
	 * 호스트 명이 일치하는 첫번째 서버 조회
	 *
	 * @param host 호스트 명
	 * @return 호스트 명이 일치하는 첫번째 서버, 기존코드 호환을 위해 존재하지 않는 경우 비어있는 객체를 반환
	 */
	public AipServer findFirstServerBy(String host) {
		AipServerExample example = new AipServerExample();
		{
			example.createCriteria().andHostEqualTo(host);
			example.setOrderByClause("SEQ");
		}
		return dao.selectByExample(example)
				.stream()
				.findFirst()
				.orElseGet(AipServer::new);
	}
}