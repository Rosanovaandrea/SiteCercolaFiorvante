package com.example.SiteCercolaFioravante.user.repository;

import com.example.SiteCercolaFioravante.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT usr.surname, usr.name, usr.email FROM User usr WHERE UPPER( usr.name ) LIKE CONCAT('%', UPPER( :nameSurname ), '%' ) OR  UPPER( usr.surname ) LIKE CONCAT('%', UPPER( :nameSurname ), '%' ) ")
    List<UserProjectionList> getUserByNameOrSurname(@Param("nameSurname") String nameSurname);

    @Query("SELECT usr.surname, usr.name, usr.email FROM User usr WHERE  usr.phoneNumber = :phoneNumber  ")
    List<UserProjectionList> getUserByPhoneNumber(@Param("phoneNumber") long phoneNumber);

    @Query("SELECT usr.surname, usr.name, usr.email FROM User usr WHERE UPPER( usr.email ) LIKE CONCAT('%', UPPER( :email ), '%' )  ")
    List<UserProjectionList> getUsersByEmail(@Param("email") String email);

}
