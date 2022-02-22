package dnd.project.dnd6th7worryrecordservice.domain.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "CATEGORY")
@Entity
public class Category {

    @Id
    @Column(name = "categoryId")
    private Long categoryId;

    @Column(name = "categoryName")
    private String CategoryName;
}
