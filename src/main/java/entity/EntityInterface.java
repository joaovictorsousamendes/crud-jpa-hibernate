package entity;

public interface EntityInterface {

    default String standarizeString(String string){
        return string.trim().toUpperCase();
    }
}
