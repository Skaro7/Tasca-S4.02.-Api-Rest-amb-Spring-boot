package cat.itacademy.s04.t02.n02.exception;

public class FruitNotFoundException extends RuntimeException {

    public FruitNotFoundException(Long id) {
        super("No s'ha trobat cap fruita amb l'ID: " + id);
    }
}