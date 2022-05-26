package com.nttdata.bank.movement.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nttdata.bank.movement.client.model.Account;

import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "service-account", url = "localhost:9959")
public interface AccountRest {
	
	@PutMapping("/{id}")
	public Mono<Account> updateBalance(@PathVariable String id, 
										@RequestParam Double balance,
										@RequestParam Byte type);

}
