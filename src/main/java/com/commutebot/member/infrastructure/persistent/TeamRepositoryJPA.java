package com.commutebot.member.infrastructure.persistent;

import com.commutebot.member.domain.Team;
import com.commutebot.member.domain.TeamRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepositoryJPA extends JpaRepository<Team, Integer>, TeamRepository {

    // Get Leader Name From TeamCode
    default String teamCodeConverterToLeader(int teamCode){
        return findByCode(teamCode).getLeader();
    }
    Team findByCode(int code);

    // TeamCode Exist Check
    default boolean existCheck(int code){
        return existsByCode(code);
    }
    Boolean existsByCode(int code);

    // Get All Team
    default List<Team> getAllTeam(){
        return findAll();
    }
    List<Team> findAll();

    default Team findTeam(String leader){
        return findByLeader(leader);
    }
    Team findByLeader(String leader);
}
