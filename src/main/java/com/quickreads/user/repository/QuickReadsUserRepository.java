package com.quickreads.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.quickreads.user.repository.model.QuickReadsUser;

@Repository
public interface QuickReadsUserRepository extends JpaRepository<QuickReadsUser, Integer>{

	@Query("FROM QuickReadsUser WHERE email = ?1")
	QuickReadsUser getQuickreadsUser(String username);

}
