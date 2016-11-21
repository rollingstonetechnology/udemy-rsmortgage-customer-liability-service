package com.rollingstone.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rollingstone.dao.jpa.RsMortgageCustomerLiabilityRepository;
import com.rollingstone.domain.Liability;
import com.rollingstone.domain.Customer;


/*
 * Service class to do CRUD for Customer Liability through JPS Repository
 */
@Service
public class RsMortgageCustomerLiabilityService {

    private static final Logger log = LoggerFactory.getLogger(RsMortgageCustomerLiabilityService.class);

    @Autowired
    private RsMortgageCustomerLiabilityRepository customerLiabilityRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    GaugeService gaugeService;

    public RsMortgageCustomerLiabilityService() {
    }

    public Liability createLiability(Liability liability) {
        return customerLiabilityRepository.save(liability);
    }

    public Liability getLiability(long id) {
        return customerLiabilityRepository.findOne(id);
    }

    public void updateLiability(Liability liability) {
    	customerLiabilityRepository.save(liability);
    }

    public void deleteLiability(Long id) {
    	customerLiabilityRepository.delete(id);
    }

    public Page<Liability> getAllLiabilitysByPage(Integer page, Integer size) {
        Page pageOfLiabilitys = customerLiabilityRepository.findAll(new PageRequest(page, size));
        
        // example of adding to the /metrics
        if (size > 50) {
            counterService.increment("com.rollingstone.getAll.largePayload");
        }
        return pageOfLiabilitys;
    }
    
    public List<Liability> getAllLiabilitys() {
        Iterable<Liability> pageOfLiabilitys = customerLiabilityRepository.findAll();
        
        List<Liability> customerLiabilitys = new ArrayList<Liability>();
        
        for (Liability liability : pageOfLiabilitys){
        	customerLiabilitys.add(liability);
        }
    	log.info("In Real Service getAllLiabilitys  size :"+customerLiabilitys.size());

    	
        return customerLiabilitys;
    }
    
    public List<Liability> getAllLiabilitysForCustomer(Customer customer) {
        Iterable<Liability> pageOfLiabilitys = customerLiabilityRepository.findCustomerLiabilityByCustomer(customer);
        
        List<Liability> customerLiabilitys = new ArrayList<Liability>();
        
        for (Liability liability : pageOfLiabilitys){
        	customerLiabilitys.add(liability);
        }
    	log.info("In Real Service getAllLiabilitys  size :"+customerLiabilitys.size());

    	
        return customerLiabilitys;
    }
}
