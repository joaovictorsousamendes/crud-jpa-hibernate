package service;

import java.util.List;

public interface ServiceInterface <T>{

    default void validateId(long id){
        if(id < 1) throw new IllegalArgumentException("ID " + id + " is not valid");
    }

    default void validateStringAttribute(String str, String attributeName){
        if(str == null || str.isBlank()){
            throw new IllegalArgumentException(attributeName + " is blank or null.");
        }
    }
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
