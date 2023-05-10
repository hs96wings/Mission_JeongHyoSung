package com.ll.gramgram.boundedContext.notification.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToInstaMemberOrderByIdDesc(InstaMember toInstaMember);

    List<Notification> findByToInstaMember_usernameOrderByIdDesc(String username);

    int countByToInstaMemberAndReadDateIsNull(InstaMember instaMember);
}
