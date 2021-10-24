package com.quickreads.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quickreads.user.repository.model.Item;

public interface QuickreadsItemRepository extends JpaRepository<Item, Integer> {

	@Query("FROM item i WHERE i.email = ?1")
	List<Item> getItems(String userName);

}
