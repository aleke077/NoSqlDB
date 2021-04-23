package kz.bitlab.group29.group29security.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "clubs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clubs extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ucl_titles")
    private int championsLeagueTitles;

    @Column(name = "league_titles")
    private int leagueTitles;

    @ManyToOne(fetch = FetchType.EAGER)
    private Cities city;

}
