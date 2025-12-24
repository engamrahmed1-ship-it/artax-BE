-- ------------------------------ B2C ------------------------------
CREATE TABLE t_customer (
    customer_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    cust_type VARCHAR(5) DEFAULT NULL,
    status VARCHAR(15) DEFAULT NULL,
    date_joined DATE DEFAULT NULL,
    PRIMARY KEY (customer_id)
);

CREATE TABLE t_customer_individual (
    individual_id BIGINT(20) NOT NULL,
    title VARCHAR(4) DEFAULT NULL,
    first_name VARCHAR(255) DEFAULT NULL,
    second_name VARCHAR(255) DEFAULT NULL,
    last_name VARCHAR(255) DEFAULT NULL,
    cust_category VARCHAR(10) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    phone BIGINT(20) DEFAULT NULL,
    id_type VARCHAR(10) DEFAULT NULL,
    id_number VARCHAR(10) DEFAULT NULL,
    birthdate DATE DEFAULT NULL,
    gender VARCHAR(7) DEFAULT NULL,
    PRIMARY KEY (individual_id)
);

CREATE TABLE t_customer_individual_address (
    address_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    street VARCHAR(255) DEFAULT NULL,
    city VARCHAR(20) DEFAULT NULL,
    state VARCHAR(20) DEFAULT NULL,
    zip_code BIGINT(5) DEFAULT NULL,
    country VARCHAR(20) DEFAULT NULL,
    customer_id BIGINT(20),
    PRIMARY KEY (address_id)
);

-- ------------------------------ B2B ------------------------------
CREATE TABLE t_customer_business (
    company_id BIGINT(20) NOT NULL,
    company_name VARCHAR(255) DEFAULT NULL,
    street VARCHAR(255) DEFAULT NULL,
    city VARCHAR(20) DEFAULT NULL,
    state VARCHAR(20) DEFAULT NULL,
    zip_code BIGINT(5) DEFAULT NULL,
    country VARCHAR(20) DEFAULT NULL,
    commercial_register VARCHAR(20) DEFAULT NULL,
    website VARCHAR(50) DEFAULT NULL,
    company_class VARCHAR(10) DEFAULT NULL,
    industry VARCHAR(20) DEFAULT NULL,
    company_size VARCHAR(20) DEFAULT NULL,
    primary_contact_id BIGINT(20) DEFAULT NULL,
    PRIMARY KEY (company_id)
);

CREATE TABLE t_company_contacts (
    contact_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    title VARCHAR(4) DEFAULT NULL,
    first_name VARCHAR(255) DEFAULT NULL,
    second_name VARCHAR(255) DEFAULT NULL,
    last_name VARCHAR(255) DEFAULT NULL,
    job_title VARCHAR(255) DEFAULT NULL,
    priority VARCHAR(20) DEFAULT NULL,
    relation VARCHAR(255) DEFAULT NULL,
    id_type VARCHAR(10) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    phone BIGINT(20) DEFAULT NULL,
    id_number VARCHAR(10) DEFAULT NULL,
    birthdate DATE DEFAULT NULL,
    company_id BIGINT(20) DEFAULT NULL,
    PRIMARY KEY (contact_id)
);

-- ------------------------------ Common ------------------------------
CREATE TABLE t_crm_interactions (
    interaction_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    customer_id BIGINT(20) DEFAULT NULL,
    interaction_type VARCHAR(10) DEFAULT NULL,
    notes VARCHAR(1024) DEFAULT NULL,
    channel VARCHAR(10) DEFAULT NULL,
    agent_id VARCHAR(15) DEFAULT NULL,
    interaction_date DATE DEFAULT NULL,
    PRIMARY KEY (interaction_id)
);

CREATE TABLE t_tickets (
    ticket_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    ticket_code VARCHAR(255) DEFAULT NULL,
    customer_id BIGINT(20) DEFAULT NULL,
    subject VARCHAR(255) DEFAULT NULL,
    description VARCHAR(1024) DEFAULT NULL,
    priority VARCHAR(20) DEFAULT NULL,
    status VARCHAR(15) DEFAULT NULL,
    assigned_to VARCHAR(15) DEFAULT NULL,
    created_at DATE DEFAULT NULL,
    closed_at DATE DEFAULT NULL,
    resolution VARCHAR(2048),
    PRIMARY KEY (ticket_id)
);

CREATE TABLE t_lead (
    lead_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    source VARCHAR(15) DEFAULT NULL,
    status VARCHAR(15) DEFAULT NULL,
    assigned_to VARCHAR(15) DEFAULT NULL,
    created_at DATE DEFAULT NULL,
    converted_customer_id BIGINT(20) DEFAULT NULL,
    PRIMARY KEY (lead_id)
);

CREATE TABLE t_opportunity (
    opportunity_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    customer_id BIGINT(20) DEFAULT NULL,
    name VARCHAR(55) DEFAULT NULL,
    amount DECIMAL(12,2) NOT NULL,
    stage VARCHAR(15) DEFAULT NULL,
    probability DECIMAL(5,2) DEFAULT NULL,
    created_at DATE DEFAULT NULL,
    close_date DATE DEFAULT NULL,
    PRIMARY KEY (opportunity_id)
);

CREATE TABLE t_customer_documents (
    document_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    customer_id BIGINT(20) DEFAULT NULL,
    document_name VARCHAR(20) DEFAULT NULL,
    document_type VARCHAR(5) DEFAULT NULL,
    created_at DATE DEFAULT NULL,
    project_id BIGINT(20) DEFAULT NULL,
    document_path VARCHAR(2048),
    PRIMARY KEY (document_id)
);

CREATE TABLE t_projects (
    project_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    customer_id BIGINT(20) DEFAULT NULL,
    project_name VARCHAR(20) DEFAULT NULL,
    project_type VARCHAR(5) DEFAULT NULL,
    status VARCHAR(15) DEFAULT NULL,
    created_at DATE DEFAULT NULL,
    closed_at DATE DEFAULT NULL,
    budget DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (project_id)
);

CREATE TABLE t_project_details (
    project_sub_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    project_id BIGINT(20) NOT NULL,
    sub_name VARCHAR(20) DEFAULT NULL,
    status VARCHAR(15) DEFAULT NULL,
    created_at DATE DEFAULT NULL,
    closed_at DATE DEFAULT NULL,
    priority VARCHAR(20) DEFAULT NULL,
    budget DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (project_sub_id)
);

-- ------------------------------ Add FKs at the end ------------------------------

ALTER TABLE t_customer_individual
    ADD CONSTRAINT fk_individual_customer FOREIGN KEY (individual_id)
        REFERENCES t_customer(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_customer_individual_address
    ADD CONSTRAINT fk_customer_address FOREIGN KEY (customer_id)
        REFERENCES t_customer(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_customer_business
    ADD CONSTRAINT fk_business_customer FOREIGN KEY (company_id)
        REFERENCES t_customer(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_company_contacts
    ADD CONSTRAINT fk_contact_company FOREIGN KEY (company_id)
        REFERENCES t_customer_business(company_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_crm_interactions
    ADD CONSTRAINT fk_interaction_customer FOREIGN KEY (customer_id)
        REFERENCES t_customer(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_tickets
    ADD CONSTRAINT fk_ticket_customer FOREIGN KEY (customer_id)
        REFERENCES t_customer(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_lead
    ADD CONSTRAINT fk_lead_customer FOREIGN KEY (converted_customer_id)
        REFERENCES t_customer(customer_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE;

ALTER TABLE t_opportunity
    ADD CONSTRAINT fk_opportunity_customer FOREIGN KEY (customer_id)
        REFERENCES t_customer(customer_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE;

ALTER TABLE t_customer_documents
    ADD CONSTRAINT fk_document_customer FOREIGN KEY (customer_id)
        REFERENCES t_customer(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_projects
    ADD CONSTRAINT fk_project_customer FOREIGN KEY (customer_id)
        REFERENCES t_customer(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE t_project_details
    ADD CONSTRAINT fk_project_detail FOREIGN KEY (project_id)
        REFERENCES t_projects(project_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;
