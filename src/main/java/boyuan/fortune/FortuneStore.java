package boyuan.fortune;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Random;

@ApplicationScoped
public class FortuneStore {

    private static final String CUSTOM_CATEGORY = "custom";
    private final Random random = new Random();

    @Transactional
    public void persist(Fortune fortune) {
        FortuneEntity entity = new FortuneEntity();
        entity.message = fortune.message();
        fortune.author().ifPresent(author -> entity.author = author);
        entity.category = CUSTOM_CATEGORY;
        entity.persist();
    }

    @Transactional
    public Optional<Fortune> getRandom() {
        long total = FortuneEntity.count();
        if (total <= 0) {
            return Optional.empty();
        }
        int offset = random.nextInt((int) total + 1);
        return FortuneEntity.findAll(Sort.by("id")).page(offset, 1).firstResultOptional()
                .map(e -> new Fortune(((FortuneEntity) e).message, Optional.ofNullable(((FortuneEntity) e).author)));
    }
}
