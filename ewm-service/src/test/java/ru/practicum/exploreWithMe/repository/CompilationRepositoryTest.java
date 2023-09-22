package ru.practicum.exploreWithMe.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.exploreWithMe.commonFiles.compilation.repository.CompilationRepository;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class CompilationRepositoryTest {
    @Autowired
    private CompilationRepository compilationRepository;

    @Test
    void should() {

    }

}
