package com.commutebot.member.domain;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Team {
//    101("스윗트래커 서비스플랫폼")
//    102("스윗트래커 메세징플랫폼")
//    103("스윗트래커 RnD")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;

    @Column(nullable = false)
    private int code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String leader;

    public Team(int code, String name, String leader) {
        this.code = code;
        this.name = name;
        this.leader = leader;
    }

    public void changeLeader(String username){
        this.leader = username;
    }
}
