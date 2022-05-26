package com.nttdata.bank.movement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bank.movement.client.AccountRest;
import com.nttdata.bank.movement.model.Movement;
import com.nttdata.bank.movement.repository.MovementRepository;
import com.nttdata.bank.movement.service.MovementService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovementServiceImpl implements MovementService{
	
	@Autowired
	private MovementRepository movementRepository;
	
	@Autowired
	private AccountRest accountRest;

	@Override
	public Flux<Movement> findByAccountId(String accountId) {
		return movementRepository.findMovementByAccountId(accountId);
	}

	@Override
	public Mono<Movement> findById(String id) {
		return movementRepository.findById(id)
				.switchIfEmpty(Mono.error(new Exception("No existe un movimiento con el id: " + id)));
	}

	@Override
	public Mono<Movement> save(Movement movement) {
		Mono<Movement> movementMono = Mono.just(movement);
		return movementMono.flatMap(m -> {
			Mono<Movement> oMovementMono = Mono.empty();
			if(m.getCategory() == 1) {
			
				if(m.getType() == 1) {
					oMovementMono = accountRest.updateBalance(m.getAccountId(), m.getAmount(), (byte) 1)
											.flatMap(a -> movementRepository.save(movement));
				}
				if(m.getType() == 2) {
					oMovementMono = accountRest.updateBalance(m.getAccountId(), m.getAmount(), (byte) 2)
											.switchIfEmpty(Mono.error(new Exception("No tiene saldo suficiente.")))
											.flatMap(a -> movementRepository.save(movement));
				}
			}
			if(m.getCategory() == 2) {
				if(m.getType() == 3) {
					oMovementMono = accountRest.updateBalance(m.getAccountId(), m.getAmount(), (byte) 2)
											.switchIfEmpty(Mono.error(new Exception("No tiene saldo suficiente para realizar la transferencia.")))
											.flatMap(a -> {
												return accountRest.updateBalance(m.getExternalAccount(), m.getAmount(), (byte) 1)
													.flatMap(ac -> {
														Movement oMovement = new Movement();
														oMovement.setAccountId(m.getExternalAccount());
														oMovement.setAmount(m.getAmount());
														oMovement.setCategory(m.getCategory());
														oMovement.setCreateAt(m.getCreateAt());
														oMovement.setDescription("Depósito");
														oMovement.setExternalAccount(m.getAccountId());
														oMovement.setType(m.getType());
														return movementRepository.save(oMovement);
													}).flatMap(aco -> movementRepository.save(m));
											});
				}
			}	
			return oMovementMono;
		});
	}

}
