package com.rollingstone.dao.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.rollingstone.domain.Customer;
import com.rollingstone.domain.Liability;



public interface RsMortgageCustomerLiabilityRepository extends PagingAndSortingRepository<Liability, Long> {
    List<Liability> findCustomerLiabilityByCustomer(Customer customer);

    Page findAll(Pageable pageable);
}
