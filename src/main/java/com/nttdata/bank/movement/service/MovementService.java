package com.nttdata.bank.movement.service;

import com.nttdata.bank.movement.model.Movement;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementService {
	
	Flux<Movement> findByAccountId(String accountId);
	
	Mono<Movement> findById(String id);
	
	Mono<Movement> save(Movement movement);

}
