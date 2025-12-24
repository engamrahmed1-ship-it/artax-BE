package com.artax.crm.repository;

import com.artax.crm.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = {"b2b", "b2c"})
    Optional<Customer> findWithDetailsByCustomerId(Long customerId);

    // Only B2C names
    @Query("""
        SELECT c FROM Customer c
        JOIN c.b2c i
        WHERE LOWER(i.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(i.secondName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(i.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
        """)
    Page<Customer> searchB2CByName(@Param("name") String name, Pageable pageable);

    // Only B2B company names
    @Query("""
        SELECT c FROM Customer c
        JOIN c.b2b b
        WHERE LOWER(b.companyName) LIKE LOWER(CONCAT('%', :name, '%'))
        """)
    Page<Customer> searchB2BByName(@Param("name") String name, Pageable pageable);


    // B2C phone
            @Query("""
        SELECT c FROM Customer c
        JOIN c.b2c i
        WHERE CAST(i.phone AS string) LIKE CONCAT('%', :phone, '%')
        """)
    Page<Customer> searchB2CByPhone(@Param("phone") String phone, Pageable pageable);


    // B2B contacts phone
    @Query("""
        SELECT DISTINCT c FROM Customer c
        JOIN c.b2b b
        JOIN b.contacts bc
        WHERE CAST(bc.phone AS string) LIKE CONCAT('%', :phone, '%')
        """)
    Page<Customer> searchB2BByPhone(@Param("phone") String phone, Pageable pageable);
    // Search by Name + Type
    @Query("""
            SELECT DISTINCT c FROM Customer c
            LEFT JOIN c.b2b b
            LEFT JOIN c.b2c i
            LEFT JOIN b.contacts bc
            LEFT JOIN i.addresses ia
            WHERE (LOWER(b.companyName) LIKE LOWER(CONCAT('%', :name, '%'))
               OR LOWER(i.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
               OR LOWER(i.secondName) LIKE LOWER(CONCAT('%', :name, '%'))
               OR LOWER(i.lastName) LIKE LOWER(CONCAT('%', :name, '%')))
              AND c.custType = :custType
            """)
    Page<Customer> searchByNameAndType(@Param("name") String name,
                                       @Param("custType") String custType,
                                       Pageable pageable);


    @Query("""
            SELECT DISTINCT c FROM Customer c
            LEFT JOIN c.b2b b
            LEFT JOIN c.b2c i
            LEFT JOIN b.contacts bc
            WHERE (CAST(i.phone AS string) LIKE %:phone%
               OR CAST(bc.phone AS string) LIKE %:phone%)
              AND c.custType = :custType
            """)
    Page<Customer> searchByPhoneAndType(@Param("phone") String phone,
                                        @Param("custType") String custType,
                                        Pageable pageable);


    // Find all by customer type
    Page<Customer> findByCustType(String custType, Pageable pageable);


    // 4. Count all customers
    long count();
}