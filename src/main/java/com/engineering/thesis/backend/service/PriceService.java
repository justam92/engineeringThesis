package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Price;
import com.engineering.thesis.backend.model.authentication.PriceRequest;

import java.util.List;
import java.util.Optional;

public interface PriceService {
    Price create(PriceRequest price) throws ResourceNotFoundException;
    Price updateById(Long id, Price price) throws ResourceNotFoundException;
    Long deleteById(Long id) throws ResourceNotFoundException;
    List<Price> getAll();
    Optional<Price> getById(Long id) throws ResourceNotFoundException;
}