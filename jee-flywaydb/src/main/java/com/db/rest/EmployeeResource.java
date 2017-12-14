
package com.db.rest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("employee")
@Stateless
public class EmployeeResource {

    @PersistenceContext(unitName = "MyPU")
    EntityManager em;

    @GET
    @Produces("application/xml")
    public Employee[] get() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList().toArray(new Employee[0]);
    }
}
