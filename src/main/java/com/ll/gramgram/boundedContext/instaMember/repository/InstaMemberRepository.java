package com.ll.gramgram.boundedContext.instaMember.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstaMemberRepository extends JpaRepository<InstaMember, Long> {
    Optional<InstaMember> findByUsername(String username);

    Optional<InstaMember> findByOauthId(String oauthId);
}
