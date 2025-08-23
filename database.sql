DROP DATABASE IF EXISTS bank_management;
CREATE DATABASE bank_management;
USE bank_management;

-- 1) Admins
DROP TABLE IF EXISTS admins;
CREATE TABLE admins (
  admin_id       INT AUTO_INCREMENT PRIMARY KEY,
  username       VARCHAR(50) NOT NULL UNIQUE,
  password       VARCHAR(100) NOT NULL,           -- plain text as requested
  email          VARCHAR(100) UNIQUE,
  is_super_admin BOOLEAN NOT NULL DEFAULT FALSE,
  created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2) Users (registration starts as PENDING)
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  user_id          INT AUTO_INCREMENT PRIMARY KEY,
  username         VARCHAR(50) NOT NULL UNIQUE,
  password         VARCHAR(100) NOT NULL,         -- plain text as requested
  email            VARCHAR(100) NOT NULL UNIQUE,
  phone            VARCHAR(20),
  account_type     ENUM('SAVINGS','CURRENT') NOT NULL,
  status           ENUM('PENDING','APPROVED','REJECTED','DEACTIVATED') NOT NULL DEFAULT 'PENDING',
  approved_by      INT NULL,
  approved_at      DATETIME NULL,
  rejection_reason VARCHAR(255) NULL,
  created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_users_approved_by_admin
    FOREIGN KEY (approved_by) REFERENCES admins(admin_id)
    ON UPDATE RESTRICT ON DELETE SET NULL
) ENGINE=InnoDB;

-- 3) Security Questions (fixed 4)
DROP TABLE IF EXISTS security_questions;
CREATE TABLE security_questions (
  question_id   INT AUTO_INCREMENT PRIMARY KEY,
  question_text VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

INSERT INTO security_questions (question_text) VALUES
('What is your first school name?'),
('What is the name of your favorite teacher?'),
('What is your pet''s name?'),
('What is your mother''s maiden name?');

-- 4) User Security Answers (one selected question per user)
DROP TABLE IF EXISTS user_security_answers;
CREATE TABLE user_security_answers (
  answer_id   INT AUTO_INCREMENT PRIMARY KEY,
  user_id     INT NOT NULL,
  question_id INT NOT NULL,
  answer      VARCHAR(100) NOT NULL,              -- plain text answer
  CONSTRAINT fk_usa_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON UPDATE RESTRICT ON DELETE CASCADE,
  CONSTRAINT fk_usa_question
    FOREIGN KEY (question_id) REFERENCES security_questions(question_id)
    ON UPDATE RESTRICT ON DELETE RESTRICT,
  UNIQUE KEY uk_user_question (user_id, question_id)
) ENGINE=InnoDB;

-- 5) Accounts (created only on approval)
DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts (
  account_id     INT AUTO_INCREMENT PRIMARY KEY,
  user_id        INT NOT NULL,
  account_number VARCHAR(20) NOT NULL UNIQUE,
  balance        DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_accounts_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6) Admin approvals audit
DROP TABLE IF EXISTS admin_approvals;
CREATE TABLE admin_approvals (
  approval_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id     INT NOT NULL,
  admin_id    INT NOT NULL,
  action      ENUM('APPROVED','REJECTED') NOT NULL,
  reason      VARCHAR(255) NULL,
  created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_approval_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON UPDATE RESTRICT ON DELETE CASCADE,
  CONSTRAINT fk_approval_admin
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id)
    ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 7) Transactions (only CASH, UPI, INTERNAL)
DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
  txn_id         INT AUTO_INCREMENT PRIMARY KEY,
  account_id     INT NOT NULL,
  user_id        INT NOT NULL,
  txn_type       ENUM('CREDIT','DEBIT') NOT NULL,
  amount         DECIMAL(15,2) NOT NULL,
  description    VARCHAR(255) NULL,
  txn_time       DATETIME NOT NULL,
  balance_after  DECIMAL(15,2) NULL,
  channel        ENUM('CASH','UPI','INTERNAL') NULL,
  created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_txn_account
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
    ON UPDATE RESTRICT ON DELETE CASCADE,
  CONSTRAINT fk_txn_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8) Bank configuration for passbook/reports (Jandhan Bank, Rajkot Main)
DROP TABLE IF EXISTS bank_config;
CREATE TABLE bank_config (
  config_id     INT AUTO_INCREMENT PRIMARY KEY,
  bank_name     VARCHAR(100) NOT NULL,
  branch_name   VARCHAR(100) NOT NULL,
  branch_code   VARCHAR(20)  NOT NULL,
  ifsc_code     VARCHAR(20)  NOT NULL,
  support_email VARCHAR(100) NOT NULL,
  updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

INSERT INTO bank_config (bank_name, branch_name, branch_code, ifsc_code, support_email)
VALUES ('Jandhan Bank', 'Rajkot Main', '1001', 'JDBK001001', 'support@jandhanbank.com');

-- 9) Seed super admin (change password/email later)
INSERT INTO admins (username, password, email, is_super_admin)
VALUES ('superadmin', 'admin123', 'superadmin@jandhanbank.com', TRUE);




USE bank_management;

-- 👨‍💼 Admins (you already have superadmin = id 1)
INSERT INTO admins (username, password, email, is_super_admin)
VALUES 
('admin1', 'pass123', 'admin1@jandhanbank.com', FALSE),
('admin2', 'pass456', 'admin2@jandhanbank.com', FALSE);

-- 👤 Users (no hardcoded IDs)
INSERT INTO users (username, password, email, phone, account_type, status, approved_by, approved_at, rejection_reason)
VALUES
('john_doe', 'john123', 'john@example.com', '9876543210', 'SAVINGS', 'APPROVED', 2, NOW(), NULL),
('jane_smith', 'jane123', 'jane@example.com', '9123456780', 'CURRENT', 'APPROVED', 2, NOW(), NULL),
('rohit_k', 'rohit123', 'rohit@example.com', '9988776655', 'SAVINGS', 'PENDING', NULL, NULL, NULL),
('sneha_p', 'sneha123', 'sneha@example.com', '9911223344', 'CURRENT', 'REJECTED', 3, NOW(), 'Incomplete documents'),
('amit_b', 'amit123', 'amit@example.com', '9090909090', 'SAVINGS', 'DEACTIVATED', 2, NOW(), NULL);

-- ❓ User Security Answers (use subqueries to pick correct user IDs dynamically)
INSERT INTO user_security_answers (user_id, question_id, answer)
VALUES
((SELECT user_id FROM users WHERE username='john_doe'), 1, 'Greenwood High'),
((SELECT user_id FROM users WHERE username='jane_smith'), 2, 'Mr. Mehta'),
((SELECT user_id FROM users WHERE username='rohit_k'), 3, 'Bruno'),
((SELECT user_id FROM users WHERE username='sneha_p'), 4, 'Patel'),
((SELECT user_id FROM users WHERE username='amit_b'), 1, 'Sunrise School');

-- 💳 Accounts (for APPROVED users only)
INSERT INTO accounts (user_id, account_number, balance)
VALUES
((SELECT user_id FROM users WHERE username='john_doe'), 'ACC10001', 5000.00),
((SELECT user_id FROM users WHERE username='jane_smith'), 'ACC10002', 12000.50);

-- ✅ Admin Approvals
INSERT INTO admin_approvals (user_id, admin_id, action, reason)
VALUES
((SELECT user_id FROM users WHERE username='john_doe'), 2, 'APPROVED', NULL),
((SELECT user_id FROM users WHERE username='jane_smith'), 2, 'APPROVED', NULL),
((SELECT user_id FROM users WHERE username='sneha_p'), 3, 'REJECTED', 'Incomplete documents'),
((SELECT user_id FROM users WHERE username='amit_b'), 2, 'APPROVED', NULL);

-- 💰 Transactions
INSERT INTO transactions (account_id, user_id, txn_type, amount, description, txn_time, balance_after, channel)
VALUES
((SELECT account_id FROM accounts WHERE account_number='ACC10001'),
 (SELECT user_id FROM users WHERE username='john_doe'),
 'CREDIT', 2000.00, 'Cash deposit', NOW(), 7000.00, 'CASH'),

((SELECT account_id FROM accounts WHERE account_number='ACC10001'),
 (SELECT user_id FROM users WHERE username='john_doe'),
 'DEBIT', 500.00, 'ATM withdrawal', NOW(), 6500.00, 'CASH'),

((SELECT account_id FROM accounts WHERE account_number='ACC10002'),
 (SELECT user_id FROM users WHERE username='jane_smith'),
 'CREDIT', 5000.00, 'Salary credit', NOW(), 17000.50, 'INTERNAL'),

((SELECT account_id FROM accounts WHERE account_number='ACC10002'),
 (SELECT user_id FROM users WHERE username='jane_smith'),
 'DEBIT', 2000.00, 'UPI Payment to Amazon', NOW(), 15000.50, 'UPI');

-- 🏦 Bank Config (you already have Rajkot Main, add another branch)
INSERT INTO bank_config (bank_name, branch_name, branch_code, ifsc_code, support_email)
VALUES 
('Jandhan Bank', 'Mumbai Central', '1002', 'JDBK001002', 'support-mumbai@jandhanbank.com');


use bank_management;
-- 1) Transfers Table
DROP TABLE IF EXISTS transfers;
CREATE TABLE transfers (
    transfer_id      INT AUTO_INCREMENT PRIMARY KEY,
    from_account_id  INT NOT NULL,
    to_account_id    INT NOT NULL,
    amount           DECIMAL(15,2) NOT NULL,
    description      VARCHAR(255) NULL,
    transfer_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transfer_from_account
        FOREIGN KEY (from_account_id) REFERENCES accounts(account_id)
        ON UPDATE RESTRICT ON DELETE CASCADE,
    CONSTRAINT fk_transfer_to_account
        FOREIGN KEY (to_account_id) REFERENCES accounts(account_id)
        ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=InnoDB;



USE bank_management;

-- 👤 Add more users with Indian names
INSERT INTO users (username, password, email, phone, account_type, status, approved_by, approved_at)
VALUES
('ram_sharma', 'ram123', 'ram.sharma@example.com', '9876500001', 'SAVINGS', 'APPROVED', 2, NOW()),
('sita_patel', 'sita123', 'sita.patel@example.com', '9876500002', 'CURRENT', 'APPROVED', 2, NOW()),
('arjun_verma', 'arjun123', 'arjun.verma@example.com', '9876500003', 'SAVINGS', 'APPROVED', 2, NOW()),
('priya_mehta', 'priya123', 'priya.mehta@example.com', '9876500004', 'CURRENT', 'APPROVED', 3, NOW()),
('vikas_gupta', 'vikas123', 'vikas.gupta@example.com', '9876500005', 'SAVINGS', 'APPROVED', 2, NOW());

-- 💳 Accounts for them
INSERT INTO accounts (user_id, account_number, balance)
VALUES
((SELECT user_id FROM users WHERE username='ram_sharma'), 'ACC10003', 8000.00),
((SELECT user_id FROM users WHERE username='sita_patel'), 'ACC10004', 15000.00),
((SELECT user_id FROM users WHERE username='arjun_verma'), 'ACC10005', 10000.00),
((SELECT user_id FROM users WHERE username='priya_mehta'), 'ACC10006', 12000.00),
((SELECT user_id FROM users WHERE username='vikas_gupta'), 'ACC10007', 6000.00);

-- 💰 Transactions spread across different months (2024 & 2025)
INSERT INTO transactions (account_id, user_id, txn_type, amount, description, txn_time, balance_after, channel)
VALUES
-- Ram Sharma (2024)
((SELECT account_id FROM accounts WHERE account_number='ACC10003'),
 (SELECT user_id FROM users WHERE username='ram_sharma'),
 'CREDIT', 5000.00, 'Cash deposit', '2024-01-15 10:20:00', 13000.00, 'CASH'),

((SELECT account_id FROM accounts WHERE account_number='ACC10003'),
 (SELECT user_id FROM users WHERE username='ram_sharma'),
 'DEBIT', 2000.00, 'UPI to Flipkart', '2024-02-12 14:45:00', 11000.00, 'UPI'),

-- Sita Patel (2024)
((SELECT account_id FROM accounts WHERE account_number='ACC10004'),
 (SELECT user_id FROM users WHERE username='sita_patel'),
 'CREDIT', 10000.00, 'Salary Credit', '2024-03-01 09:00:00', 25000.00, 'INTERNAL'),

((SELECT account_id FROM accounts WHERE account_number='ACC10004'),
 (SELECT user_id FROM users WHERE username='sita_patel'),
 'DEBIT', 3000.00, 'Shopping at Reliance', '2024-04-05 18:30:00', 22000.00, 'UPI'),

-- Arjun Verma (2024)
((SELECT account_id FROM accounts WHERE account_number='ACC10005'),
 (SELECT user_id FROM users WHERE username='arjun_verma'),
 'CREDIT', 8000.00, 'Cash deposit', '2024-05-10 11:15:00', 18000.00, 'CASH'),

((SELECT account_id FROM accounts WHERE account_number='ACC10005'),
 (SELECT user_id FROM users WHERE username='arjun_verma'),
 'DEBIT', 2500.00, 'Electricity Bill', '2024-06-20 08:50:00', 15500.00, 'UPI'),

-- Priya Mehta (2025)
((SELECT account_id FROM accounts WHERE account_number='ACC10006'),
 (SELECT user_id FROM users WHERE username='priya_mehta'),
 'CREDIT', 12000.00, 'Salary Credit', '2025-01-05 09:10:00', 24000.00, 'INTERNAL'),

((SELECT account_id FROM accounts WHERE account_number='ACC10006'),
 (SELECT user_id FROM users WHERE username='priya_mehta'),
 'DEBIT', 4000.00, 'Online Shopping', '2025-02-14 21:45:00', 20000.00, 'UPI'),

-- Vikas Gupta (2025)
((SELECT account_id FROM accounts WHERE account_number='ACC10007'),
 (SELECT user_id FROM users WHERE username='vikas_gupta'),
 'CREDIT', 3000.00, 'Cash deposit', '2025-03-18 12:20:00', 9000.00, 'CASH'),

((SELECT account_id FROM accounts WHERE account_number='ACC10007'),
 (SELECT user_id FROM users WHERE username='vikas_gupta'),
 'DEBIT', 1500.00, 'Mobile Recharge', '2025-04-09 16:40:00', 7500.00, 'UPI');



ALTER TABLE admin_approvals 
MODIFY COLUMN action ENUM('APPROVED','REJECTED','DEACTIVATED') NOT NULL;

use bank_management;
-- 1) FD Applications (user submits request)
DROP TABLE IF EXISTS fd_applications;
CREATE TABLE fd_applications (
    fd_app_id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT NOT NULL,
    account_id        INT NOT NULL,
    amount            DECIMAL(15,2) NOT NULL,
    tenure_months     INT NOT NULL CHECK (tenure_months >= 6 AND tenure_months <= 60), -- 6 months to 5 years
    interest_rate     DECIMAL(5,2) NOT NULL DEFAULT 6.50, -- % per annum
    application_date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status            ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    rejection_reason  VARCHAR(255) NULL,
    approved_by       INT NULL,
    approved_at       DATETIME NULL,
    CONSTRAINT fk_fd_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON UPDATE RESTRICT ON DELETE CASCADE,
    CONSTRAINT fk_fd_account
        FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON UPDATE RESTRICT ON DELETE CASCADE,
    CONSTRAINT fk_fd_admin
        FOREIGN KEY (approved_by) REFERENCES admins(admin_id)
        ON UPDATE RESTRICT ON DELETE SET NULL
) ENGINE=InnoDB;

-- Drop table if exists
DROP TABLE IF EXISTS fixed_deposits;

CREATE TABLE fixed_deposits (
    fd_id             INT AUTO_INCREMENT PRIMARY KEY,
    fd_app_id         INT NOT NULL UNIQUE,
    user_id           INT NOT NULL,
    account_id        INT NOT NULL,
    amount            DECIMAL(15,2) NOT NULL,
    tenure_months     INT NOT NULL,
    interest_rate     DECIMAL(5,2) NOT NULL,
    maturity_amount   DECIMAL(15,2) NOT NULL,
    start_date        DATE NOT NULL DEFAULT (CURRENT_DATE),
    maturity_date     DATE NOT NULL,
    status            ENUM('ACTIVE', 'MATURED', 'CLOSED') NOT NULL DEFAULT 'ACTIVE',
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Use unique constraint names to avoid "duplicate" error
    CONSTRAINT fk_fixed_deposit_app
        FOREIGN KEY (fd_app_id) REFERENCES fd_applications(fd_app_id)
        ON UPDATE RESTRICT ON DELETE CASCADE,

    CONSTRAINT fk_fixed_deposit_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON UPDATE RESTRICT ON DELETE CASCADE,

    CONSTRAINT fk_fixed_deposit_account
        FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=InnoDB;


-- fd-setup.sql
-- Insert FD Applications (Users apply)
INSERT INTO fd_applications (user_id, account_id, amount, tenure_months, interest_rate, status)
SELECT 
    u.user_id,
    a.account_id,
    5000.00,
    12,
    6.80,
    'PENDING'
FROM users u
JOIN accounts a ON u.user_id = a.user_id
WHERE u.username = 'john_doe'
LIMIT 1;

INSERT INTO fd_applications (user_id, account_id, amount, tenure_months, interest_rate, status)
SELECT 
    u.user_id,
    a.account_id,
    10000.00,
    24,
    7.00,
    'APPROVED'
FROM users u
JOIN accounts a ON u.user_id = a.user_id
WHERE u.username = 'jane_smith'
LIMIT 1;

-- Approve one application manually
UPDATE fd_applications 
SET 
    status = 'APPROVED',
    approved_by = 1,
    approved_at = NOW()
WHERE status = 'PENDING' AND user_id = (
    SELECT user_id FROM users WHERE username = 'john_doe'
);

-- Insert Fixed Deposits for approved applications
INSERT INTO fixed_deposits (fd_app_id, user_id, account_id, amount, tenure_months, interest_rate, maturity_amount, start_date, maturity_date, status)
SELECT 
    fa.fd_app_id,
    fa.user_id,
    fa.account_id,
    fa.amount,
    fa.tenure_months,
    fa.interest_rate,
    ROUND(fa.amount * POW(1 + fa.interest_rate / 100 / 12, fa.tenure_months), 2),
    CURRENT_DATE,
    DATE_ADD(CURRENT_DATE, INTERVAL fa.tenure_months MONTH),
    'ACTIVE'
FROM fd_applications fa
WHERE fa.status = 'APPROVED'
  AND NOT EXISTS (SELECT 1 FROM fixed_deposits fd WHERE fd.fd_app_id = fa.fd_app_id);