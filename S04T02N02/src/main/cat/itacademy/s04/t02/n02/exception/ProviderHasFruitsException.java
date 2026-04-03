package cat.itacademy.s04.t02.n02.exception;

public class ProviderHasFruitsException extends RuntimeException {

    public ProviderHasFruitsException(Long id) {
        super("El proveidor amb l'ID " + id + " te fruites associades i no es pot eliminar.");
    }
}