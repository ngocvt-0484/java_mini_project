CREATE TABLE employee (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  department_id BIGINT,

  CONSTRAINT fk_employee_department
      FOREIGN KEY (department_id)
          REFERENCES department(id)
);
