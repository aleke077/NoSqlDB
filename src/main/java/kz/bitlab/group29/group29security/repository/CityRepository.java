package kz.bitlab.group29.group29security.repository;

import kz.bitlab.group29.group29security.entities.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<Cities, Long> {

    List<Cities> findAllByDeletedAtNull();
    Cities findByDeletedAtNullAndId(Long id);

}
