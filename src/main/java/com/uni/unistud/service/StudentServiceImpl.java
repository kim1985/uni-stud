package com.uni.unistud.service;

import com.uni.unistud.dto.StudentCourseDTO;
import com.uni.unistud.dto.StudentDTO;
import com.uni.unistud.entity.Course;
import com.uni.unistud.entity.Student;
import com.uni.unistud.exception.CourseFullException;
import com.uni.unistud.exception.DuplicateResourceException;
import com.uni.unistud.exception.ResourceNotFoundException;
import com.uni.unistud.exception.StudentCourseException;
import com.uni.unistud.mapper.StudentMapper;
import com.uni.unistud.repository.CourseRepository;
import com.uni.unistud.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementazione del service per la gestione degli studenti
 * Gestisce tutte le operazioni CRUD, iscrizioni/disinscrizioni ai corsi
 * e recupero con paginazione degli studenti
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;

    /**
     * Crea un nuovo studente verificando che l'email non sia già utilizzata
     * L'email è il campo univoco per identificare gli studenti
     *
     * @param studentDTO dati dello studente da creare
     * @return studente creato con ID generato
     * @throws DuplicateResourceException se l'email è già utilizzata
     */
    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Verifica unicità email prima della creazione
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new DuplicateResourceException("Studente con email " + studentDTO.getEmail() + " già esistente");
        }

        // Converte DTO in entità e salva nel database
        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);

        // Restituisce l'entità salvata convertita in DTO
        return studentMapper.toDTO(savedStudent);
    }

    /**
     * Aggiorna i dati di uno studente esistente
     * Verifica che la nuova email non sia già utilizzata da un altro studente
     *
     * @param studentId ID dello studente da aggiornare
     * @param studentDTO nuovi dati dello studente
     * @return studente aggiornato
     * @throws ResourceNotFoundException se lo studente non esiste
     * @throws DuplicateResourceException se la nuova email è già utilizzata
     */
    @Override
    public StudentDTO updateStudent(Long studentId, StudentDTO studentDTO) {
        // Recupera lo studente esistente o lancia eccezione se non trovato
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentId));

        // Verifica unicità email solo se è stata modificata
        if (!existingStudent.getEmail().equals(studentDTO.getEmail()) &&
                studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new DuplicateResourceException("Email già in uso da un altro studente");
        }

        // Aggiorna tutti i campi modificabili
        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setEmail(studentDTO.getEmail());

        // Salva le modifiche e restituisce lo studente aggiornato
        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    /**
     * Recupera un singolo studente con tutti i suoi corsi associati
     * Usa JOIN FETCH per evitare lazy loading e problema N+1
     *
     * @param studentId ID dello studente da recuperare
     * @return studente con lista completa dei corsi
     * @throws ResourceNotFoundException se lo studente non esiste
     */
    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long studentId) {
        // Query ottimizzata che carica studente e corsi in una sola chiamata
        Student student = studentRepository.findByIdWithCourses(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentId));

        // Converte l'entità completa (con corsi) in DTO
        return studentMapper.toDTO(student);
    }

    /**
     * Recupera tutti gli studenti senza i loro corsi (per performance)
     * Ideale per liste generali dove i dettagli dei corsi non servono
     *
     * @return lista di tutti gli studenti senza corsi
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        // Carica solo i dati base degli studenti (senza corsi)
        List<Student> students = studentRepository.findAll();

        // Converte ogni studente in DTO
        return studentMapper.toDTOList(students);
    }

    /**
     * Elimina uno studente dal sistema
     * Le associazioni con i corsi vengono eliminate automaticamente (cascade)
     *
     * @param studentId ID dello studente da eliminare
     * @throws ResourceNotFoundException se lo studente non esiste
     */
    @Override
    public void deleteStudent(Long studentId) {
        // Verifica esistenza prima di tentare l'eliminazione
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Studente non trovato con ID: " + studentId);
        }

        // Elimina lo studente (le associazioni student_courses vengono rimosse automaticamente)
        studentRepository.deleteById(studentId);
    }

    /**
     * Iscrive uno studente a un corso specifico
     * Gestisce la relazione many-to-many bidirezionale
     *
     * @param studentCourseDTO contiene gli ID di studente e corso
     * @return studente aggiornato con il nuovo corso
     * @throws ResourceNotFoundException se studente o corso non esistono
     * @throws StudentCourseException se lo studente è già iscritto al corso
     */
    @Override
    public StudentDTO enrollStudentInCourse(StudentCourseDTO studentCourseDTO) {
        // Recupera lo studente con i suoi corsi attuali per verificare duplicati
        Student student = studentRepository.findByIdWithCourses(studentCourseDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentCourseDTO.getStudentId()));

        // Recupera il corso da associare
        Course course = courseRepository.findById(studentCourseDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + studentCourseDTO.getCourseId()));

        // Verifica che lo studente non sia già iscritto al corso
        if (student.getCourses().contains(course)) {
            throw new StudentCourseException("Lo studente è già iscritto a questo corso");
        }

        // NUOVA VERIFICA: Controlla se il corso ha ancora posti disponibili
        if (!course.hasAvailableSpots()) {
            throw new CourseFullException(
                    String.format("Il corso '%s' ha raggiunto la capacità massima di %d studenti",
                            course.getTitle(), course.getMaxCapacity())
            );
        }

        // Aggiunge il corso usando il metodo helper che gestisce la bidirezionalità
        student.addCourse(course);

        // Salva lo studente (il corso viene aggiornato automaticamente)
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    /**
     * Disiscrive uno studente da un corso specifico
     * Rimuove la relazione many-to-many bidirezionale
     *
     * @param studentCourseDTO contiene gli ID di studente e corso
     * @return studente aggiornato senza il corso rimosso
     * @throws ResourceNotFoundException se studente o corso non esistono
     * @throws StudentCourseException se lo studente non è iscritto al corso
     */
    @Override
    public StudentDTO unenrollStudentFromCourse(StudentCourseDTO studentCourseDTO) {
        // Recupera lo studente con i suoi corsi per verificare l'iscrizione
        Student student = studentRepository.findByIdWithCourses(studentCourseDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato con ID: " + studentCourseDTO.getStudentId()));

        // Recupera il corso da rimuovere
        Course course = courseRepository.findById(studentCourseDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato con ID: " + studentCourseDTO.getCourseId()));

        // Verifica che lo studente sia effettivamente iscritto al corso
        if (!student.getCourses().contains(course)) {
            throw new StudentCourseException("Lo studente non è iscritto a questo corso");
        }

        // Rimuove il corso usando il metodo helper che gestisce la bidirezionalità
        student.removeCourse(course);

        // Salva lo studente (il corso viene aggiornato automaticamente)
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    /**
     * Recupera tutti gli studenti con i loro corsi associati
     * Usa JOIN FETCH per ottimizzare le performance ed evitare N+1
     *
     * @return lista completa di studenti con i loro corsi
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsWithCourses() {
        // Query ottimizzata che carica studenti e corsi in una sola chiamata
        List<Student> students = studentRepository.findAllWithCourses();

        // Converte ogni studente (con i suoi corsi) in DTO
        return studentMapper.toDTOList(students);
    }
}
