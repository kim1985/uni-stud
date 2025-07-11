package com.uni.unistud.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utility per la gestione della paginazione con validazione automatica dei parametri
 */
public class PaginationUtils {

    /** Dimensione predefinita della pagina */
    private static final int DEFAULT_SIZE = 10;

    /** Dimensione massima consentita per evitare sovraccarichi */
    private static final int MAX_SIZE = 100;

    /**
     * Crea un oggetto Pageable con validazione automatica dei parametri
     *
     * @param page numero della pagina (0-based, viene corretto se negativo)
     * @param size dimensione della pagina (viene limitata tra 1 e MAX_SIZE)
     * @param sortBy campo per l'ordinamento (può essere null)
     * @param direction direzione ordinamento ("asc" o "desc", default "asc")
     * @return Pageable configurato e validato
     */
    public static Pageable create(int page, int size, String sortBy, String direction) {
        return PageRequest.of(
                Math.max(0, page),              // Garantisce che page >= 0
                validateSize(size),             // Valida e corregge la dimensione
                createSort(sortBy, direction)   // Crea l'ordinamento
        );
    }

    /**
     * Valida la dimensione della pagina assicurandosi che sia nei limiti consentiti
     *
     * @param size dimensione richiesta
     * @return dimensione validata (DEFAULT_SIZE se fuori range)
     */
    private static int validateSize(int size) {
        // Se size è valido (tra 1 e MAX_SIZE) lo restituisce, altrimenti usa il default
        return (size > 0 && size <= MAX_SIZE) ? size : DEFAULT_SIZE;
    }

    /**
     * Crea l'oggetto Sort basato sui parametri di ordinamento
     *
     * @param sortBy nome del campo per l'ordinamento
     * @param direction direzione dell'ordinamento
     * @return Sort configurato (unsorted se sortBy è vuoto/null)
     */
    private static Sort createSort(String sortBy, String direction) {
        // Se non c'è un campo di ordinamento, restituisce Sort.unsorted()
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return Sort.unsorted();
        }

        // Determina la direzione: "desc" -> DESC, tutto il resto -> ASC
        Sort.Direction dir = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Crea e restituisce il Sort con direzione e campo specificati
        return Sort.by(dir, sortBy);
    }
}
