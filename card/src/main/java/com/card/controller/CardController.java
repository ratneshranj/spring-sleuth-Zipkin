package com.card.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.card.model.Card;
import com.card.repository.CardRepository;

@Controller
public class CardController {
	
	private static final Logger logger = LoggerFactory.getLogger(CardController.class);
	
	@Autowired
	private CardRepository cardsRepository;
	
	@GetMapping("/myCards/{customerId}")
	public @ResponseBody List<Card> getCardDetails(@PathVariable("customerId") int customerId) {
		logger.debug("Start getCardDetails for costomer id : {}",customerId);
		List<Card> cards=cardsRepository.findByCustomerId(customerId);
		logger.debug("End getCardDetails for costomer id {} is : {}",customerId,cards);
		return cards;
	}
}
