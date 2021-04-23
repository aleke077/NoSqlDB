package kz.bitlab.group29.group29security.services.impl;

import kz.bitlab.group29.group29security.entities.Cities;
import kz.bitlab.group29.group29security.entities.Clubs;
import kz.bitlab.group29.group29security.repository.CityRepository;
import kz.bitlab.group29.group29security.repository.ClubRepository;
import kz.bitlab.group29.group29security.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<Clubs> getAllClubs() {
        return clubRepository.findAllByDeletedAtNull();
    }

    @Override
    public List<Clubs> getAllClubsByCityId(Long cityId) {
        return clubRepository.findAllByDeletedAtNullAndCity_Id(cityId);
    }

    @Override
    public List<Clubs> getAllClubsByCity(Cities city) {
        return clubRepository.findAllByDeletedAtNullAndCity(city);
    }

    @Override
    public List<Cities> getAllCities() {
        return cityRepository.findAllByDeletedAtNull();
    }

    @Override
    public Clubs addClub(Clubs club) {
        if(club.getCity()!=null){
            Cities city = cityRepository.findByDeletedAtNullAndId(club.getCity().getId());
            if(city!=null){
                clubRepository.save(club);
            }
        }
        return null;
    }

    @Override
    public Clubs getClub(Long id) {
        return clubRepository.findByDeletedAtNullAndId(id);
    }

    @Override
    public Cities getCity(Long id) {
        return cityRepository.findByDeletedAtNullAndId(id);
    }

    @Override
    public Clubs saveClub(Clubs club) {
        return clubRepository.save(club);
    }

    @Override
    public void deleteClub(Clubs club) {
        club.setDeletedAt(new Date());
        clubRepository.save(club);
    }

    @Override
    public List<Clubs> searchClubs(String name, Long cityId, Integer uclFrom, Integer uclTo, Integer leagueFrom, Integer leagueTo) {

        Specification specification = (Specification<Clubs>)(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%"+name.toUpperCase()+"%");

        if(cityId!=0){
            specification = specification.and((Specification<Clubs>)(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("city").get("id"), cityId));
        }

        if(uclFrom!=null){
            specification = specification.and((Specification<Clubs>)(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("championsLeagueTitles"), uclFrom));
        }

        if(uclTo!=null){
            specification = specification.and((Specification<Clubs>)(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("championsLeagueTitles"), uclTo));
        }

        if(leagueFrom!=null){
            specification = specification.and((Specification<Clubs>)(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("leagueTitles"), leagueFrom));
        }

        if(leagueTo!=null){
            specification = specification.and((Specification<Clubs>)(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("leagueTitles"), leagueTo));
        }

        return clubRepository.findAll(specification);
    }
}