package com.quickreads.user.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quickreads.user.repository.model.QuickReadsUser;

@Repository
public interface QuickReadsUserRepository extends JpaRepository<QuickReadsUser, Long>{

	@Query("FROM QuickReadsUser user WHERE user.email = ?1 AND user.status = ?2")
	QuickReadsUser getQuickreadsUser(String username, String status);

	@Query("UPDATE QuickReadsUser user set user.status = ?1 WHERE user.email = ?2")
	@Modifying
	@Transactional
	int updateQuickreadsUser(String status, String username);

}
