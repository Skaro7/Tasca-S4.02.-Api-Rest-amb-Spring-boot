package cat.itacademy.s04.t02.n02.exception;

public class DuplicateProviderException extends RuntimeException {

    public DuplicateProviderException(String name) {
        super("Ja existeix un proveidor amb el nom: " + name);
    }
}