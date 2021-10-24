package com.quickreads.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickreads.user.api.model.Item;
import com.quickreads.user.api.model.ItemResponse;
import com.quickreads.user.repository.QuickreadsItemRepository;
import com.quickreads.user.util.AWSS3Util;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItemService {

	@Autowired
	private QuickreadsItemRepository itemRepository;

	@Autowired
	private AWSS3Util s3util;

	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	private static final String EXISTS = "Item with same name exists";
	private static final String DELIMITTER = "-";
	private static final String SPACE = " ";
	private static final String EMPTY = "";

	public List<Item> getItems() throws Exception {
		List<com.quickreads.user.repository.model.Item> findAllItem = null;
		try {
			findAllItem = itemRepository.findAll();
			if (findAllItem.isEmpty()) {
				return new ArrayList<Item>();
			}
		} catch (Exception e) {
			log.error("Failed while retrieving all items ", e.getLocalizedMessage());
			throw new Exception();
		}
		List<Item> allItems = new ArrayList<>();
		findAllItem.stream().forEach(item -> allItems
				.add(Item.builder().itemName(item.getItemName()).type(item.getType()).email(item.getEmail()).build()));
		return allItems;
	}

	public String getItem(String id) throws Exception {
		return s3util.getData(id);
	}

	public ItemResponse addItem(Item item) throws Exception {
		final String itemIdentifier = item.getType().concat(DELIMITTER)
				.concat(item.getItemName().replace(SPACE, EMPTY).toLowerCase());
		try {
			List<com.quickreads.user.repository.model.Item> findAll = itemRepository.findAll();
			if (!findAll.isEmpty() && findAll.stream()
					.anyMatch(existingItem -> existingItem.getItemIdentifier().equals(itemIdentifier))) {
				return ItemResponse.builder().status(FAILURE).details(EXISTS).build();
			}
			s3util.uploadData(itemIdentifier, item.getItemContent());
			com.quickreads.user.repository.model.Item saveItem = com.quickreads.user.repository.model.Item.builder()
					.itemName(item.getItemName()).email(item.getEmail()).type(item.getType())
					.itemIdentifier(itemIdentifier).build();
			com.quickreads.user.repository.model.Item savedItem = itemRepository.save(saveItem);
			log.info("Item saved : ", savedItem.getItemName());
			return ItemResponse.builder().status(SUCCESS).details(itemIdentifier).build();
		} catch (Exception e) {
			log.error("Item addition failed ", e.getLocalizedMessage());
			return ItemResponse.builder().status(FAILURE).details(e.getMessage()).build();
		}
	}

}
