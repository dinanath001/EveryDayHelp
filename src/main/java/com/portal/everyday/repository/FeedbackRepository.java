package com.portal.everyday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portal.everyday.entity.FeedBack;

public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {

}
