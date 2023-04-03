package com.commutebot.member.infrastructure.persistent;

import com.commutebot.member.domain.Member;
import com.commutebot.member.domain.MemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepositoryJPA extends JpaRepository<Member, Integer>, MemberRepository {

    // Save Member In SignUp
    default void store(final Member member){
        save(member);
    }


    // Username Duplicate Check
    default boolean usernameDuplicateCheck(String username) {
        return existsByUsername(username);
    }
    boolean existsByUsername(String username);


    // Email Duplicate Check
    default boolean emailDuplicateCheck(String email) {
        return existsByEmail(email);
    }
    boolean existsByEmail(String email);


    // Get Member From Username
    default Member findMemberToUsername(String username){
        return findByUsername(username);
    }
    Member findByUsername(String username);


    // Get Member From Email
    default Member findMemberToEmail(String email){
        return findByEmail(email);
    }
    Member findByEmail(String username);


    // Get Optional Member To Authenticate Check
    default Optional<Member> findMemberForAuthenticateChecks(String username) {
        return Optional.ofNullable(findByUsername(username));
    }


    // Withdrawal Request
    default void exit(String username){deleteByUsername(username);}
    void deleteByUsername(String username);

}
