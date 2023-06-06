package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);

    private final Map<Integer, Candidate> candidateMap = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Стажер", "Очень хочу научится", LocalDateTime.now(), 1));
        save(new Candidate(0, "Джуниор", "Миска риса и я ваш", LocalDateTime.now(), 2));
        save(new Candidate(0, "Junior+", "Java core, алгоритмы", LocalDateTime.now(), 3));
        save(new Candidate(0, "Middle Java Developer", "Поднимаю таски как бог", LocalDateTime.now(), 1));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
                        candidate.getDescription(), candidate.getCreationDate(), candidate.getCityId())) != null;
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