package com.quickreads.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quickreads.user.repository.model.QuickReadsUser;

@Repository
public interface QuickReadsUserRepository extends JpaRepository<QuickReadsUser, Integer>{

}
