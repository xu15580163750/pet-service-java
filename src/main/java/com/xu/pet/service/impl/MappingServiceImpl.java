package com.xu.pet.service.impl;

import com.xu.pet.service.IMappingService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MappingServiceImpl implements IMappingService {
    private ModelMapper modelMapper;

    public MappingServiceImpl() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public <T> T map(Object source, Class<T> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    @Override
    public <T> List<T> map(List source, Class<T> destinationType) {
        return (List<T>) source.stream().map(x -> modelMapper.map(x, destinationType)).collect(Collectors.toList());
    }
}
