package com.mindhub.homebanking2.Repositories;

import com.mindhub.homebanking2.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
