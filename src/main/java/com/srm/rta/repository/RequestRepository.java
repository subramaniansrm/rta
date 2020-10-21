package com.srm.rta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srm.rta.entity.RequestEntity;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {

}
