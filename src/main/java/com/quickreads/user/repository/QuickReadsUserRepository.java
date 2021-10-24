package com.quickreads.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.quickreads.user.repository.model.QuickReadsUser;

@Repository
public interface QuickReadsUserRepository extends JpaRepository<QuickReadsUser, Integer>{

	@Query("FROM quickreadsuser user WHERE user.email = ?1")
	QuickReadsUser getQuickreadsUser(String username);
	@Query("DELETE quickreadsuser user WHERE user.email = ?1")
	Boolean deleteQuickreadsUser(String username);

}
