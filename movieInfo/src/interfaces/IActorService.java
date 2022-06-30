package interfaces;

import java.util.Vector;

import dto.ActorInfoDto;

public interface IActorService {
	
	Vector<ActorInfoDto> selectActorInfor(String searchWord);
	
	Vector<ActorInfoDto> selectAllActorInfor();
	
	int insertActorInfo(ActorInfoDto dto);

	boolean selectActorDoubleCheck(String actorName);

	int deleteActorInfo(int personN);
	
	int updateActorInfo(int personNum, ActorInfoDto dto);
}