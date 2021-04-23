package kz.bitlab.group29.group29security.services;

import kz.bitlab.group29.group29security.entities.Cities;
import kz.bitlab.group29.group29security.entities.Clubs;

import java.util.List;

public interface ClubService {

    List<Clubs> getAllClubs();
    List<Clubs> getAllClubsByCityId(Long cityId);
    List<Clubs> getAllClubsByCity(Cities city);
    Clubs addClub(Clubs club);
    Clubs getClub(Long id);
    Clubs saveClub(Clubs club);
    void deleteClub(Clubs club);

    List<Cities> getAllCities();
    Cities getCity(Long id);

    List<Clubs> searchClubs(String name, Long cityId, Integer uclFrom, Integer uclTo, Integer leagueFrom, Integer leagueTo);

}
