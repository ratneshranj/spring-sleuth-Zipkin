package com.account.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.account.model.Accounts;
import com.account.model.Card;
import com.account.model.CustomerDetails;
import com.account.repository.AccountsRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Controller
public class AccountsController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Autowired
	RestTemplate restTemplate;	
	
	@GetMapping("/myAccount/{customerId}")
	public @ResponseBody Accounts getAccountDetails(@PathVariable("customerId") int customerId) {
		logger.debug("Start getAccountDetails for costomer id : {}",customerId);
		Accounts accounts=accountsRepository.findByCustomerId(customerId);
		logger.debug("End getAccountDetails for costomer id {} is : {}",customerId,accounts);
		return accounts;
	}
	
	@GetMapping("/myCustomerDetails/{customerId}")
	@CircuitBreaker(name = "myCustomerSupport" ,fallbackMethod = "myCustomerDetailsFallBack")
	public @ResponseBody CustomerDetails myCustomerDetails(@PathVariable("customerId") int customerId) {
		logger.debug("Start myCustomerDetails for costomer id : {}",customerId);
		Accounts accounts = accountsRepository.findByCustomerId(customerId);
		List<Card> card = this.restTemplate.getForObject("http://card/myCards/"+customerId, List.class);
		//List<Loans> loans = this.restTemplate.getForObject("http://loans/myCards/"+customerId, List.class);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccount(accounts);
		//customerDetails.setLoans(loans);
		customerDetails.setCard(card);
		
		logger.debug("End myCustomerDetails for costomer id : {}",customerId);
		
		return customerDetails;
	}
	
	private CustomerDetails myCustomerDetailsFallBack(int customerId, Throwable t) {
		logger.debug("Start myCustomerDetailsFallBack for costomer id : {}",customerId);
		Accounts accounts = accountsRepository.findByCustomerId(customerId);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccount(accounts);
		logger.debug("End myCustomerDetailsFallBack for costomer id : {}",customerId);
		return customerDetails;

	}

}
