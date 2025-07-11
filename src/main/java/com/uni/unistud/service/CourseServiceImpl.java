package com.uni.unistud.service;

import com.uni.unistud.dto.CourseDTO;
import com.uni.unistud.entity.Course;
import com.uni.unistud.exception.DuplicateResourceException;
import com.uni.unistud.exception.ResourceNotFoundException;
import com.uni.unistud.mapper.CourseMapper;
import com.uni.unistud.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementazione del service per la gestione dei corsi
 * Gestisce tutte le operazioni CRUD e di ricerca con paginazione
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    /**
     * Crea un nuovo corso verificando che il titolo non sia già esistente
     *
     * @param courseDTO dati del corso da creare
     * @return corso creato con ID generato
     * @throws DuplicateResourceException se il titolo è già utilizzato
     */
    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        // Verifica che non esista già un corso con lo stesso titolo
        if (courseRepository.existsByTitle(courseDTO.getTitle())) {
            throw new DuplicateResourceException("Corso con titolo '" + courseDTO.getTitle() + "' già esistente");
        }

        // Converte DTO in entità e salva nel database
        Course course = courseMapper.toEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);

        // Restituisce l'entità salvata convertita in DTO
        return courseMapper.toDTO(savedCourse);
    }

    /**
     * Aggiorna un corso esistente verificando duplicati del titolo
     *
     * @param courseId ID del corso da aggiornare
     * @param courseDTO nuovi dati del corso
     * @return corso aggiornato
     * @throws ResourceNotFoundException se il corso non esiste
     * @throws DuplicateResourceException se il nuovo titolo è già utilizzato
     */
    @Override
    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
        // Recupera il corso esistente o lancia eccezione se non trovato
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + courseId));

        // Verifica che il nuovo titolo non sia già utilizzato da un altro corso
        if (!existingCourse.getTitle().equals(courseDTO.getTitle()) &&
                courseRepository.existsByTitle(courseDTO.getTitle())) {
            throw new DuplicateResourceException("Titolo già in uso da un altro corso");
        }

        // Aggiorna solo il titolo (altri campi possono essere aggiunti qui)
        existingCourse.setTitle(courseDTO.getTitle());

        // Salva le modifiche e restituisce il corso aggiornato
        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDTO(updatedCourse);
    }

    /**
     * Recupera un singolo corso con i suoi studenti associati
     *
     * @param courseId ID del corso da recuperare
     * @return corso con lista degli studenti
     * @throws ResourceNotFoundException se il corso non esiste
     */
    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long courseId) {
        // Usa query con JOIN FETCH per caricare anche gli studenti in una sola query
        Course course = courseRepository.findByIdWithStudents(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + courseId));

        // Converte l'entità completa (con studenti) in DTO
        return courseMapper.toDTO(course);
    }

    /**
     * Recupera tutti i corsi con paginazione e ordinamento
     *
     * @param pageable parametri di paginazione (page, size, sort)
     * @return pagina di corsi senza studenti (per performance)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseDTO> getAllCourses(Pageable pageable) {
        // Recupera corsi paginati dal database
        Page<Course> coursePage = courseRepository.findAll(pageable);

        // Converte ogni entità Course in DTO usando il mapper
        return coursePage.map(courseMapper::toDTO);
    }

    /**
     * Recupera tutti i corsi con i loro studenti in formato paginato
     * Usa JOIN FETCH per evitare il problema N+1
     *
     * @param pageable parametri di paginazione
     * @return pagina di corsi con studenti associati
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseDTO> getCoursesWithStudents(Pageable pageable) {
        // Query ottimizzata che carica corsi e studenti in una sola chiamata
        Page<Course> coursePage = courseRepository.findAllWithStudents(pageable);

        // Converte ogni corso (con studenti) in DTO
        return coursePage.map(courseMapper::toDTO);
    }

    /**
     * Cerca corsi per titolo con ricerca case-insensitive e paginazione
     *
     * @param title testo da cercare nel titolo (ricerca parziale)
     * @param pageable parametri di paginazione
     * @return pagina di corsi che contengono il testo nel titolo
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseDTO> searchCoursesByTitle(String title, Pageable pageable) {
        // Ricerca case-insensitive con LIKE (%title%) nel database
        Page<Course> coursePage = courseRepository.findByTitleContainingIgnoreCase(title, pageable);

        // Converte i risultati della ricerca in DTO
        return coursePage.map(courseMapper::toDTO);
    }

    /**
     * Elimina un corso dal sistema
     * Prima verifica che esista per dare un errore chiaro
     *
     * @param courseId ID del corso da eliminare
     * @throws ResourceNotFoundException se il corso non esiste
     */
    @Override
    public void deleteCourse(Long courseId) {
        // Verifica esistenza prima di tentare l'eliminazione
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Corso non trovato con ID: " + courseId);
        }

        // Elimina il corso (cascade eliminerà anche le associazioni student_courses)
        courseRepository.deleteById(courseId);
    }
}
