package com.portal.everyday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portal.everyday.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String>
{
    public Admin findByEmailAndPassword(String email, String password);
}
