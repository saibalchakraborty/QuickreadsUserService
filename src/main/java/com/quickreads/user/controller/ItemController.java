package com.quickreads.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickreads.user.api.model.Item;
import com.quickreads.user.api.model.ItemResponse;
import com.quickreads.user.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1")
@Slf4j
public class ItemController {

	@Autowired
	private ItemService service;

	private static final String SUCCESS = "SUCCESS";

	@GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Item>> getAllItems() {
		try {
			List<Item> items = service.getItems();
			log.info("All items are ", items);
			if (!items.isEmpty()) {
				return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Item>>(new ArrayList<Item>(), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Failed in getting items ", e.getLocalizedMessage());
			return new ResponseEntity<List<Item>>(new ArrayList<Item>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Item> getItem(@PathVariable String id) {
		try {
			String item = service.getItem(id);
			log.info("Item found ", item);
			if (Objects.nonNull(item)) {
				return new ResponseEntity<Item>(Item.builder().itemContent(item).build(), HttpStatus.OK);
			} else {
				return new ResponseEntity<Item>(Item.builder().build(), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Failed in getting items ", e.getLocalizedMessage());
			return new ResponseEntity<Item>(Item.builder().build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/add/item")
	public ResponseEntity<ItemResponse> createWelcome(@RequestBody Item item) {
		try {
			ItemResponse response = service.addItem(item);
			log.info("Added item {}", item);
			if (response.getStatus().equals(SUCCESS)) {
				return new ResponseEntity<ItemResponse>(response, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<ItemResponse>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Failed adding item ", e.getLocalizedMessage());
			return new ResponseEntity<ItemResponse>(ItemResponse.builder().build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
