package com.xu.pet.service;

import java.util.List;

public interface IMappingService {
    public <T> T map(Object source, Class<T> destinationType);

    public <T> List<T>  map(List source, Class<T> destinationType);
}
