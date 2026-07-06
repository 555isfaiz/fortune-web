package boyuan.fortune;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class FortuneSeeder {

    private static final Logger LOG = Logger.getLogger(FortuneSeeder.class);
    private static final String INDEX = "/fortune-files.txt";
    private static final Pattern AUTHOR = Pattern.compile("(?s)(.*?)\\R\\s*--\\s*(.+)$");

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        long existing = FortuneEntity.count();
        if (existing > 0) {
            LOG.infof("Fortunes already present (%d), skipping seed", existing);
            return;
        }
        int seeded = seed();
        LOG.infof("Seeded %d fortunes", seeded);
    }

    private int seed() {
        List<FortuneEntity> entities = indexFiles().stream().flatMap(file -> readBlocks("/" + file).stream().map(block -> {
            FortuneEntity e = new FortuneEntity();
            e.category = file;
            Matcher m = AUTHOR.matcher(block);
            if (m.matches()) {
                e.message = m.group(1).strip();
                e.author = m.group(2).strip();
            } else {
                e.message = block;
            }
            return e;
        })).toList();
        FortuneEntity.persist(entities);
        return entities.size();
    }

    private List<String> indexFiles() {
        List<String> files = new ArrayList<>();
        try (InputStream in = classpath(INDEX)) {
            if (in == null) {
                LOG.warnf("Index %s not found on classpath", INDEX);
                return files;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            files.addAll(r.readAllLines());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return files;
    }

    private List<String> readBlocks(String file) {
        try (InputStream in = classpath(file)) {
            if (in == null) {
                LOG.warnf("Fortune file %s not found, skipping", file);
                return List.of();
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String content = r.readAllAsString();
            // fortune files delimit entries with a line containing only "%"
            return Arrays.stream(content.split("\\R%\\R")).map(String::strip).filter(s -> !s.isEmpty()).toList();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private InputStream classpath(String name) {
        return getClass().getResourceAsStream(name);
    }
}
