-- ============================================================
-- SEED DATA — for local development only
-- Password for all accounts: Admin1234!
-- BCrypt hash generated with strength 10
-- ============================================================

-- Roles
INSERT INTO roles (id, name) VALUES
  (1, 'ADMIN'),
  (2, 'USER'),
  (3, 'AUDITOR')
ON CONFLICT (id) DO NOTHING;

-- Physiotherapists
-- Password: Admin1234!
INSERT INTO psychiatrists (id, name, surname, email, password) VALUES
  (1, 'Laura',  'Martínez',  'laura.martinez@fisio.com',  '$2a$10$0LIhcn5MrhO.XEaLpUeLDeEh0jsVdi.1CsQWxv0NbLL.Sx3bUvz1.'),
  (2, 'Carlos', 'López',     'carlos.lopez@fisio.com',    '$2a$10$0LIhcn5MrhO.XEaLpUeLDeEh0jsVdi.1CsQWxv0NbLL.Sx3bUvz1.')
ON CONFLICT (id) DO NOTHING;

-- Physiotherapist ↔ Role assignments
INSERT INTO psychiatrist_roles (user_id, role_id) VALUES
  (1, 1),  -- Laura → ADMIN
  (1, 2),  -- Laura → USER
  (2, 2)   -- Carlos → USER
ON CONFLICT DO NOTHING;

-- Patients
INSERT INTO patients (
    id, legal_name, name_to_use, surname, second_surname,
    gender_identity, administrative_sex, clinical_use_sex,
    dni, pronouns, email, phone_number, date_of_birth,
    polar_access_token, polar_user_id
) VALUES
  (1,  'Ana',    'Ana',    'García',    'Ruiz',   'FEMALE',    'FEMALE',  'FEMALE',  '12345678A', 'ella/sus',  'ana.garcia@email.com',     612345678, '1990-03-15', NULL, NULL),
  (2,  'Miquel', 'Miquel', 'Puig',      'Torres', 'MALE',      'MALE',    'MALE',    '23456789B', 'ell/seu',   'miquel.puig@email.com',    623456789, '1985-07-22', NULL, NULL),
  (3,  'Alex',   'Alex',   'Fernández',  NULL,    'NONBINARY', 'COMPLEX', 'COMPLEX', '34567890C', 'ell/ells',  'alex.fernandez@email.com', 634567890, '1995-11-08', NULL, NULL),
  (4,  'Sara',   'Sara',   'Gómez',     'Molina', 'FEMALE',    'FEMALE',  'FEMALE',  '45678901D', 'ella/sus',  'sara.gomez@email.com',     645678901, '2000-01-30', NULL, NULL),
  (5,  'David',  'David',  'Navarro',   'Blanco', 'MALE',      'MALE',    'MALE',    '56789012E', 'ell/seu',   'david.navarro@email.com',  656789012, '1978-05-12', NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- INDIBA sessions
INSERT INTO indiba_sessions (
    id, patient_id, begin_session, end_session,
    treated_area, mode, intensity, objective, physiotherapist_id, observations
) VALUES
  (1, 1, '2026-01-10 09:00:00', '2026-01-10 09:45:00', 'Lumbar',      'CAPACITIVE', 3.5, 'Reducció del dolor',        1, 'El pacient respon bé'),
  (2, 1, '2026-01-17 09:00:00', '2026-01-17 09:45:00', 'Lumbar',      'RESISTIVE',  4.0, 'Millora mobilitat',         1, NULL),
  (3, 2, '2026-01-12 10:00:00', '2026-01-12 10:45:00', 'Genoll dret', 'DUAL',       3.0, 'Recuperació post-cirurgia', 2, 'Seguiment setmanal'),
  (4, 3, '2026-02-05 11:00:00', '2026-02-05 11:45:00', 'Espatlla',    'CAPACITIVE', 2.5, 'Tendinitis rotadors',       1, NULL),
  (5, 4, '2026-02-18 16:00:00', '2026-02-18 16:45:00', 'Cervical',    'RESISTIVE',  3.0, 'Contractura cervical',      2, 'Millora notable')
ON CONFLICT (id) DO NOTHING;

-- PNI reports
INSERT INTO pni_reports (
    id, patient_id, report_date, hours_asleep, hrv, ans_charge, sleep_score
) VALUES
  (1, 1, '2026-01-09',  7.5,  58.0, 72, 80),
  (2, 1, '2026-01-16',  8.0,  62.0, 65, 85),
  (3, 2, '2026-01-11',  6.0,  45.0, 80, 65),
  (4, 3, '2026-02-04',  7.0,  55.0, 70, 75),
  (5, 4, '2026-02-17',  5.5,  40.0, 85, 60),
  (6, 5, '2026-03-01',  8.5,  70.0, 55, 90),
  (7, 5, '2026-03-08',  8.0,  68.0, 58, 88)
ON CONFLICT (id) DO NOTHING;

-- Training sessions
INSERT INTO training_sessions (id, patient_id, date) VALUES
  (1, 2, '2026-01-14'),
  (2, 2, '2026-01-21'),
  (3, 5, '2026-03-03'),
  (4, 5, '2026-03-10')
ON CONFLICT (id) DO NOTHING;

-- Exercises (linked to training sessions)
INSERT INTO exercises_jpa_entity (id, training_session_id, name) VALUES
  (1, 1, 'Sentadilla'),
  (2, 1, 'Press banca'),
  (3, 2, 'Peso muerto'),
  (4, 3, 'Remo con barra'),
  (5, 4, 'Extensió de quàdriceps')
ON CONFLICT (id) DO NOTHING;

-- Exercise sets (embeddable, stored in exercise_sets table)
INSERT INTO exercise_sets (exercise_id, set_number, weight_kg, reps, rest_time_seconds, rpe) VALUES
  (1, 1, 60.0,  10, 90,  7),
  (1, 2, 60.0,  10, 90,  7),
  (1, 3, 65.0,   8, 90,  8),
  (2, 1, 50.0,  12, 60,  6),
  (2, 2, 50.0,  12, 60,  7),
  (3, 1, 80.0,   6, 120, 8),
  (3, 2, 80.0,   6, 120, 9),
  (4, 1, 40.0,  12, 60,  6),
  (4, 2, 42.5,  10, 60,  7),
  (5, 1, 30.0,  15, 60,  5),
  (5, 2, 32.5,  12, 60,  6)
ON CONFLICT DO NOTHING;
