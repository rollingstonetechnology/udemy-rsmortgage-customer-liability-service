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

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rollingstone.dao.jpa.RsMortgageCustomerLiabilityRepository;
import com.rollingstone.domain.Liability;
import com.rollingstone.domain.Customer;
import com.rollingstone.domain.Investment;


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

    @Autowired
   	private CustomerClient customerClient;
    
    public RsMortgageCustomerLiabilityService() {
    }

    @HystrixCommand(fallbackMethod = "createLiabilityWithoutValidation")
    public Liability createLiability(Liability liability) throws Exception {
    	Liability createdLiability = null;
    	if (liability != null && liability.getCustomer() != null){
    		
    		log.info("In service liability create"+ liability.getCustomer().getId());
    		if (customerClient == null){
        		log.info("In customerClient null got customer");
    		}
    		else {
    			log.info("In customerClient not null got customer");
    		}
    		
    		Customer customer = customerClient.getCustomer((new Long(liability.getCustomer().getId())));
    		
    		if (customer != null){
    			createdLiability  = customerLiabilityRepository.save(liability);
    		}else {
    			log.info("Invalid Customer");
    			throw new Exception("Invalid Customer");
    		}
    	}
    	else {
    			throw new Exception("Invalid Customer");
    	}
        return createdLiability;
    }
    
    public Liability createLiabilityWithoutValidation(Liability liability) throws Exception {
    	Liability createdLiability = null;
		log.info("Customer Validation Failed. Creating Liability without customer validation");
    	createdLiability  = customerLiabilityRepository.save(liability);
        return createdLiability;
    }

    public Liability getLiability(long id) {
        return customerLiabilityRepository.findOne(id);
    }

    public void updateLiability(Liability liability) throws Exception {
    	Liability createdLiability = null;
    	if (liability != null && liability.getCustomer() != null){
    		
    		log.info("In service liability create"+ liability.getCustomer().getId());
    		if (customerClient == null){
        		log.info("In customerClient null got customer");
    		}
    		else {
    			log.info("In customerClient not null got customer");
    		}
    		
    		Customer customer = customerClient.getCustomer((new Long(liability.getCustomer().getId())));
    		
    		if (customer != null){
    			createdLiability  = customerLiabilityRepository.save(liability);
    		}else {
    			log.info("Invalid Customer");
    			throw new Exception("Invalid Customer");
    		}
    	}
    	else {
    			throw new Exception("Invalid Customer");
    	}
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
