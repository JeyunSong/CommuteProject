package com.commutebot.member.domain;

import java.util.Optional;

public interface MemberRepository {

    void store(Member member);
    boolean usernameDuplicateCheck(String username);
    boolean emailDuplicateCheck(String email);
    Member findMemberToEmail(String email);
    Member findMemberToUsername(String username);
    Optional<Member> findMemberForAuthenticateChecks(String username);
    void exit(String username);

}
