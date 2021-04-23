package kz.bitlab.group29.group29security.repository;

import kz.bitlab.group29.group29security.entities.Cities;
import kz.bitlab.group29.group29security.entities.Clubs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ClubRepository extends JpaRepository<Clubs, Long>, JpaSpecificationExecutor<Clubs> {

    List<Clubs> findAllByCity_Id(Long cityId);
    List<Clubs> findAllByCity(Cities city);

    List<Clubs> findAllByDeletedAtNullAndCity_Id(Long cityId);
    List<Clubs> findAllByDeletedAtNullAndCity(Cities city);
    List<Clubs> findAllByDeletedAtNull();

    Clubs findByDeletedAtNullAndId(Long id);

}