package boyuan.fortune;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "Fortune")
public class FortuneEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(columnDefinition = "text", nullable = false)
    public String message;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    public String category;

    @Column(columnDefinition = "text")
    public String author;
}
