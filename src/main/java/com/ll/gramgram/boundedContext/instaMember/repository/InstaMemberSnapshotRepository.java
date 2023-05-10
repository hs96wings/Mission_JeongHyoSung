package com.ll.gramgram.boundedContext.instaMember.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMemberSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstaMemberSnapshotRepository extends JpaRepository<InstaMemberSnapshot, Long> {

}
