package com.isep.trippy.Services;

import com.isep.trippy.Models.Traveller;
import com.isep.trippy.Repositories.TripsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class TripsService {
    @Autowired
    TripsRepository _tripsRepository ;
    public void addTraveller(Traveller _traveller){
        if(_traveller!=null){
            _tripsRepository.addTraveller(_traveller);
        }

    }

}
