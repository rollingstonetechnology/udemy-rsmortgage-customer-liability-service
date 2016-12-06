package com.rollingstone.api.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.rollingstone.domain.Customer;
import com.rollingstone.domain.Liability;
import com.rollingstone.exception.HTTP400Exception;
import com.rollingstone.service.RsMortgageCustomerLiabilityService;
/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/rsmortgage-customer-liability-service/v1/customer-liability")
public class CustomerLiabilityController extends AbstractRestController {

    @Autowired
    private RsMortgageCustomerLiabilityService customerLiabilityService;
  
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomerLiability(@RequestBody Liability liability,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Liability createdLiability = this.customerLiabilityService.createLiability(liability);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdLiability.getId()).toString());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<Liability> getAllCustomersLiabilityByPage(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerLiabilityService.getAllLiabilitysByPage(page, size);
    }
    
    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    List<Liability> getAllCustomerLiabilitys(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
    
        return this.customerLiabilityService.getAllLiabilitys();
    }
    
    @RequestMapping(value = "/all/{customerId}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    List<Liability> getAllCustomerLiabilitysForSingleCustomer(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      @PathVariable("id") Long id,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerLiabilityService.getAllLiabilitysForCustomer(new Customer());
    }

    
    @RequestMapping("/simple/{id}")
	public Liability getSimpleCustomerLiability(@PathVariable("id") Long id) {
    	Liability liability = this.customerLiabilityService.getLiability(id);
         checkResourceFound(liability);
         return liability;
	}

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Liability getLiability(@PathVariable("id") Long id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Liability liability = this.customerLiabilityService.getLiability(id);
        checkResourceFound(liability);
        return liability;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomerLiability(@PathVariable("id") Long id, @RequestBody Liability liability,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        checkResourceFound(this.customerLiabilityService.getLiability(id));
        if (id != liability.getId()) throw new HTTP400Exception("ID doesn't match!");
        this.customerLiabilityService.updateLiability(liability);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerLiability(@PathVariable("id") Long id, HttpServletRequest request,
                                 HttpServletResponse response) {
        checkResourceFound(this.customerLiabilityService.getLiability(id));
        this.customerLiabilityService.deleteLiability(id);
    }
}
