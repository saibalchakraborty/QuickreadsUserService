package com.quickreads.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quickreads.user.repository.model.Welcome;

public interface QuickreadsWelcomeRepository extends JpaRepository<Welcome, Integer> {

}
