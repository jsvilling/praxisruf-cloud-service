package ch.fhnw.ip6.praxisruf.configuration.api;

import java.util.Collection;
import java.util.Set;

public interface ConfigurationCrudService<T, I> {

    T create(I dto);

    T findById(I id);

    Set<T> findAll();

    T update(T dto);

    void deleteById(I id);

    void deleteAllById(Collection<I> ids);
    
}
