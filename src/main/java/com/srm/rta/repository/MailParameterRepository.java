package com.srm.rta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srm.rta.entity.MailParameterEntity;

@Repository
public interface MailParameterRepository extends JpaRepository<MailParameterEntity, Integer>{

}
