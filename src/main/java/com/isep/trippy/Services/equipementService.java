package com.isep.trippy.Services;

import com.isep.trippy.Models.car;
import com.isep.trippy.Models.homeAppliance;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class equipementService {
    @Autowired
    com.isep.trippy.Repositories.equipementRepository equipementRepository;

    public void registerCar(car car){
        if(car !=null){
            equipementRepository.registerCar(car);
        }
    }

    public void registerHomeAppliance(homeAppliance homeAppliance){
        if(homeAppliance !=null){
            equipementRepository.registerHomeAppliance(homeAppliance);
        }
    }
}
