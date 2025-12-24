package com.artax.crm.mapper;


import com.artax.crm.dto.create.*;
import com.artax.crm.dto.get.*;
import com.artax.crm.entity.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    // ---------------- GET DTOs ----------------
    @Mapping(target = "dateJoined", source = "dateJoined")
    CustomerDto toCustomerDto(Customer source);
    CustomerIndividualDto toIndividualDto(CustomerIndividual individual);
    CustomerBusinessDto toBusinessDto(CustomerBusiness business);
    CustomerAddressDto toCustomerAddressDto(CustomerIndividualAddress address);
    CompanyContactDto toCompanyContactDto(CompanyContacts contact);

    // ---------------- CREATE/UPDATE ----------------
    Customer toCustomer(CustomerCreateRequest dto);

    // ---------------- B2C Individual ----------------
    @Mapping(target = "individualId", ignore = true) // DB-generated
    @Mapping(target = "customer", expression = "java(customer)") // link parent
    CustomerIndividual toIndividual(IndividualCreateRequest dto, @Context Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerIndividual", expression = "java(customerIndividual)")
    CustomerIndividualAddress toAddress(AddressCreateRequest dto, @Context CustomerIndividual customerIndividual);

    // ---------------- B2B Business ----------------
    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "customer", expression = "java(customer)")
    CustomerBusiness toBusiness(BusinessCreateRequest dto, @Context Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", expression = "java(company)")
    CompanyContacts toContact(CompanyContactRequest dto, @Context CustomerBusiness company);




}

