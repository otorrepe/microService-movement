package com.nttdata.bank.movement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bank.movement.model.Movement;
import com.nttdata.bank.movement.service.MovementService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MovementController {
	
	@Autowired
	private MovementService movementService;
	
	@GetMapping
	private Flux<Movement> findByAccountId(@RequestParam String accountId){	
		return movementService.findByAccountId(accountId);
	}
	
	@GetMapping("/{id}")
	private Mono<Movement> findById(@PathVariable String id){
		return movementService.findById(id);
	}
	
	@PostMapping
	private Mono<Movement> save(@RequestBody Movement transaction, @RequestParam String accountId){	
		transaction.setAccountId(accountId);
		return movementService.save(transaction);	
	}

}
