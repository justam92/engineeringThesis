package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Price;
import com.engineering.thesis.backend.model.authentication.PriceRequest;
import com.engineering.thesis.backend.repository.PriceRepository;
import com.engineering.thesis.backend.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;

    @Override
    public Price create(PriceRequest price) {
        if (price == null) {
            throw new RuntimeException("Error: Price fields can't be empty.");
        }
        return priceRepository.save(Price.builder()
                .treatment(price.getTreatment())
                .price(price.getPrice())
                .build());
    }

    @Override
    public Price updateById(Long id, Price price) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (price == null) {
            throw new RuntimeException("Error: Price fields can't be empty.");
        }
        Price priceToUpdate = priceRepository.findById(price.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Price with id: " + price.getId() + " doesn't exists."));
        priceToUpdate.setPrice(price.getPrice());
        priceToUpdate.setTreatment(price.getTreatment());
        return priceRepository.save(priceToUpdate);
    }

    @Override
    public Long deleteById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (priceRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Price with id: " + id + " doesn't exists.");
        }
        priceRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Price> getAll() {
        return priceRepository.findAll();
    }

    @Override
    public Optional<Price> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (priceRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Price with id: " + id + " doesn't exists.");
        }
        return priceRepository.findById(id);
    }
}