package com.javacars.javacars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class CarController
{
    private final CarRepository carrepos;
    private final RabbitTemplate rt;

    public CarController(CarRepository carrepos, RabbitTemplate rt)
    {
        this.carrepos = carrepos;
        this.rt = rt;
    }

    @GetMapping("/cars/id/{id}")
    public Optional<Car> findOne(@PathVariable Long id)
    {
        return carrepos.findById(id);
    }

    @GetMapping("/cars/year/{year}")
    public List<Car> findYear(@PathVariable int year)
    {
        List<Car> carList = carrepos.findAll();
        List<Car> filteredList = new ArrayList<>();
        for(Car car : carList)
        {
            if (car.getYear() == year)
                filteredList.add(car);
        }
        return filteredList;
    }

    @GetMapping("/cars/brand/{brand}")
    public List<Car> findBrand(@PathVariable String brand)
    {
        CarLog message = new CarLog("Search for " + brand);
        rt.convertAndSend(JavaCarsApplication.QUEUE_NAME, message.toString());
        log.info("Sent search message to queue.");

        List<Car> carList = carrepos.findAll();
        List<Car> filteredList = new ArrayList<>();
        for(Car car : carList)
        {
            if (car.getBrand().equalsIgnoreCase(brand))
                filteredList.add(car);
        }
        return filteredList;
    }

    @PostMapping("/cars/upload")
    public List<Car> newCars(@RequestBody List<Car> newCar)
    {
        CarLog message = new CarLog("Data loaded");
        rt.convertAndSend(JavaCarsApplication.QUEUE_NAME, message.toString());
        log.info("Sent upload message to queue.");

        return carrepos.saveAll(newCar);
    }

    @DeleteMapping("/cars/delete/{id}")
    public void deleteCar(@PathVariable Long id)
    {
        carrepos.deleteById(id);

        CarLog message = new CarLog(id + "Data deleted");
        rt.convertAndSend(JavaCarsApplication.QUEUE_NAME, message.toString());
        log.info("Sent delete message to queue.");
    }
}
