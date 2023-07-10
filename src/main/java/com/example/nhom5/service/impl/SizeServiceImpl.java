package com.example.nhom5.service.impl;

import com.example.nhom5.domain.Size;
import com.example.nhom5.repository.SizeRepository;
import com.example.nhom5.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public Size addSize(Size size) {
        return sizeRepository.saveAndFlush(size);
    }
}
