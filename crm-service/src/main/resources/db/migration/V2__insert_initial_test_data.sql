INSERT INTO t_customer (customer_id, cust_type, status, date_joined) VALUES
(1, 'B2C', 'ACTIVE', '2023-01-10'),
(2, 'B2C', 'ACTIVE', '2023-03-15'),
(3, 'B2B', 'ACTIVE', '2022-11-01');

INSERT INTO t_customer_individual (
    individual_id, title, first_name, second_name, last_name,
    cust_category, email, phone, id_type, id_number, birthdate, gender
) VALUES
(1, 'Mr', 'Ahmed', 'Ali', 'Hassan', 'VIP', 'ahmed.hassan@test.com', 966501111111, 'NID', 'A12345', '1990-05-10', 'MALE'),
(2, 'Ms', 'Sara', 'Mohamed', 'Kamal', 'REG', 'sara.kamal@test.com', 966502222222, 'NID', 'B67890', '1995-09-22', 'FEMALE');

INSERT INTO t_customer_individual_address (
    street, city, state, zip_code, country, customer_id
) VALUES
('King Fahd Rd', 'Riyadh', 'Riyadh', 11564, 'KSA', 1),
('Prince Sultan St', 'Jeddah', 'Makkah', 21442, 'KSA', 2);

INSERT INTO t_customer_business (
    company_id, company_name, street, city, state, zip_code, country,
    commercial_register, website, company_class, industry, company_size
) VALUES
(3, 'Future Tech Solutions', 'Tech Park', 'Riyadh', 'Riyadh', 11564, 'KSA',
 'CR-556677', 'www.futuretech.com', 'A', 'IT', 'MEDIUM');


INSERT INTO t_company_contacts (
    title, first_name, second_name, last_name, job_title,
    priority, relation, id_type, email, phone, id_number, birthdate, company_id
) VALUES
('Mr', 'Omar', 'Yousef', 'Saleh', 'IT Manager',
 1, 'PRIMARY', 'NID', 'omar.saleh@futuretech.com', 966503333333, 'C99887', '1988-02-18', 3),
('Ms', 'Lina', 'Fahad', 'Nasser', 'Procurement',
 2, 'SECONDARY', 'NID', 'lina.nasser@futuretech.com', 966504444444, 'D77665', '1992-06-12', 3);


INSERT INTO t_crm_interactions (
    customer_id, interaction_type, notes, channel, agent_id, interaction_date
) VALUES
(1, 'CALL', 'Asked about service upgrade', 'PHONE', 'AGT01', '2023-10-01'),
(3, 'EMAIL', 'Requested proposal', 'EMAIL', 'AGT02', '2023-10-03');


INSERT INTO t_tickets (
    ticket_code, customer_id, subject, description,
    priority, status, assigned_to, created_at, closed_at, resolution
) VALUES
('TKT-101', 1, 'Login Issue', 'Unable to login to portal',
 'HIGH', 'OPEN', 'SUP01', '2023-10-01', NULL, NULL),

('TKT-102', 3, 'Server Latency', 'High response time observed',
 'MEDIUM', 'IN_PROGRESS', 'SUP02', '2023-10-03', NULL, NULL),

('TKT-103', 2, 'UI Bug', 'Dashboard widgets not loading',
 'LOW', 'CLOSED', 'SUP01', '2023-09-25', '2023-09-28', 'Cache cleared and issue resolved');


INSERT INTO t_lead (
    source, status, assigned_to, created_at, converted_customer_id
) VALUES
('WEBSITE', 'NEW', 'AGT01', '2023-09-20', NULL),
('EMAIL', 'CONVERTED', 'AGT02', '2023-08-15', 3);


INSERT INTO t_opportunity (
    customer_id, name, amount, stage, probability, created_at, close_date
) VALUES
(3, 'Cloud Migration Phase 2', 12000.00, 'NEGOTIATION', 75.00, '2023-10-01', '2023-11-15'),
(1, 'Managed Services Annual', 25000.00, 'CLOSING', 90.00, '2023-09-10', '2023-10-30');


INSERT INTO t_projects (
    customer_id, project_name, project_type, status, created_at, closed_at, budget
) VALUES
(3, 'ERP Upgrade', 'IT', 'ACTIVE', '2023-08-01', NULL, 50000.00),
(1, 'Website Revamp', 'WEB', 'CLOSED', '2023-01-01', '2023-04-01', 15000.00);


INSERT INTO t_project_details (
    project_id, sub_name, status, created_at, closed_at, priority, budget
) VALUES
(1, 'Backend Migration', 'ACTIVE', '2023-08-01', NULL, 1, 20000.00),
(1, 'Frontend Redesign', 'PENDING', '2023-08-15', NULL, 2, 15000.00),
(2, 'UI Design', 'CLOSED', '2023-01-01', '2023-02-15', 1, 8000.00);


INSERT INTO t_customer_documents (
    customer_id, document_name, document_type, created_at, project_id, document_path
) VALUES
(3, 'Contract', 'PDF', '2023-08-01', 1, '/docs/contracts/futuretech.pdf'),
(1, 'ID Copy', 'IMG', '2023-01-10', NULL, '/docs/ids/ahmed_id.png');


