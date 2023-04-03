package com.commutebot.member.domain;

import java.util.List;

public interface TeamRepository {
    String teamCodeConverterToLeader(int teamCode);
    boolean existCheck(int code);
    List<Team> getAllTeam();
    Team findTeam(String username);
}
