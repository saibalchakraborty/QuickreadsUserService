package com.quickreads.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quickreads.user.repository.model.Item;

public interface QuickreadsItemRepository extends JpaRepository<Item, Integer> {

}
