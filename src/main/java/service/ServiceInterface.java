package service;

import java.util.List;

public interface ServiceInterface <T>{
    default String standarizeString(String string){
        return string.trim().toUpperCase();
    }

    void validateEntity(T entity);

    void save(T entity);
    void update(T entity);
    T getById(long id);
    List<T> getAll();
    List<T> getAll(int startIndex, int maxResults);
    void delete(long id);

}
