package ua.foodtracker.service.mapper;

public interface Mapper<D, E> {
    D mapToDomain(E entity);

    E mapToEntity(D domain);
}
