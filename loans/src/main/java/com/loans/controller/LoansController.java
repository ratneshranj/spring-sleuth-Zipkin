package com.loans.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.loans.model.Loans;
import com.loans.repository.LoansRepository;

@Controller
public class LoansController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoansController.class);
	
	@Autowired
	private LoansRepository loansRepository;
	
	@GetMapping("/myLoans/{customerId}")
	public @ResponseBody List<Loans> getLoansDetails(@PathVariable("customerId") int customerId) {
		logger.debug("Start getLoansDetails for costomer id : {}",customerId);
		List<Loans> loans=loansRepository.findByCustomerIdOrderByStartDtDesc(customerId);
		logger.debug("End getLoansDetails for costomer id {} is : {}",customerId,loans);
		return loans;
	}

}
