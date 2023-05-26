package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidateMap = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Стажер", "Очень хочу научится", LocalDateTime.now()));
        save(new Candidate(0, "Джуниор", "Миска риса и я ваш", LocalDateTime.now()));
        save(new Candidate(0, "Junior+", "Java core, алгоритмы", LocalDateTime.now()));
        save(new Candidate(0, "Middle Java Developer", "Поднимаю таски как бог", LocalDateTime.now()));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidateMap.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidateMap.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidateMap.computeIfPresent(candidate.getId(), (id, oldCandidate) ->
                new Candidate(oldCandidate.getId(), candidate.getName(),
                        candidate.getDescription(), candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidateMap.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateMap.values();
    }
}