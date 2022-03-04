package de.nmadev.nmapi.ejb.ausbildung.nachweise;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class AusbildungsNachweisFactoryBean {

    @EJB
    private AusbildungsNachweisDao nachweisDao;


}
