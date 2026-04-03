package cat.itacademy.s04.t02.n02.exception;

public class ProviderNotFoundException extends RuntimeException {

    public ProviderNotFoundException(Long id) {
        super("No s'ha trobat cap proveidor amb l'ID: " + id);
    }
}