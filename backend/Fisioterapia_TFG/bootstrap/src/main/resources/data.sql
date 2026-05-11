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
    polar_access_token, polar_user_id,
    pathology, registration_date, functional_score, discharge_date
) VALUES
  (1,  'Ana',    'Ana',    'García',    'Ruiz',   'FEMALE',    'FEMALE',  'FEMALE',  '12345678A', 'ella/sus',  'ana.garcia@email.com',     612345678, '1990-03-15', NULL, NULL, 'LUMBAR_PAIN',              '2025-11-01', 85,   '2026-02-15'),
  (2,  'Miquel', 'Miquel', 'Puig',      'Torres', 'MALE',      'MALE',    'MALE',    '23456789B', 'ell/seu',   'miquel.puig@email.com',    623456789, '1985-07-22', NULL, NULL, 'KNEE_OSTEOARTHRITIS',      '2025-12-10', 60,   NULL),
  (3,  'Alex',   'Alex',   'Fernández',  NULL,    'NONBINARY', 'COMPLEX', 'COMPLEX', '34567890C', 'ell/ells',  'alex.fernandez@email.com', 634567890, '1995-11-08', NULL, NULL, 'ROTATOR_CUFF_INJURY',      '2026-01-05', 45,   NULL),
  (4,  'Sara',   'Sara',   'Gómez',     'Molina', 'FEMALE',    'FEMALE',  'FEMALE',  '45678901D', 'ella/sus',  'sara.gomez@email.com',     645678901, '2000-01-30', NULL, NULL, 'CERVICAL_PAIN',            '2026-02-20', 72,   NULL),
  (5,  'David',  'David',  'Navarro',   'Blanco', 'MALE',      'MALE',    'MALE',    '56789012E', 'ell/seu',   'david.navarro@email.com',  656789012, '1978-05-12', NULL, NULL, 'PLANTAR_FASCIITIS',        '2025-10-15', 91,   '2026-03-20')
ON CONFLICT (id) DO NOTHING;

-- INDIBA sessions
INSERT INTO indiba_sessions (
    id, patient_id, begin_session, end_session,
    treated_area, mode, capacitive_intensity, resistive_intensity, physiotherapist_id, observations
) VALUES
  (1, 1, '2026-01-10 09:00:00', '2026-01-10 09:45:00', 'Lumbar',      'CAPACITIVE', 3.5,  NULL, 1, 'El pacient respon bé'),
  (2, 1, '2026-01-17 09:00:00', '2026-01-17 09:45:00', 'Lumbar',      'RESISTIVE',  NULL, 4.0,  1, NULL),
  (3, 2, '2026-01-12 10:00:00', '2026-01-12 10:45:00', 'Genoll dret', 'DUAL',       3.0,  3.0,  2, 'Seguiment setmanal'),
  (4, 3, '2026-02-05 11:00:00', '2026-02-05 11:45:00', 'Espatlla',    'CAPACITIVE', 2.5,  NULL, 1, NULL),
  (5, 4, '2026-02-18 16:00:00', '2026-02-18 16:45:00', 'Cervical',    'RESISTIVE',  NULL, 3.0,  2, 'Millora notable')
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
INSERT INTO training_sessions (id, patient_id, physiotherapist_id, start_date_time, end_date_time) VALUES
  (1, 2, 1, '2026-01-14 10:00:00', '2026-01-14 11:00:00'),
  (2, 2, 1, '2026-01-21 10:00:00', '2026-01-21 11:00:00'),
  (3, 5, 2, '2026-03-03 11:00:00', '2026-03-03 12:00:00'),
  (4, 5, 2, '2026-03-10 11:00:00', '2026-03-10 12:00:00')
ON CONFLICT (id) DO NOTHING;

-- Standalone exercise templates (not linked to any session — reusable)
INSERT INTO exercise_templates (id, name, training_session_id) VALUES
  (10, 'Protocol de Genoll',  NULL),
  (11, 'Protocol Lumbar',     NULL),
  (12, 'Protocol d''Espatlla', NULL)
ON CONFLICT (id) DO NOTHING;

-- Exercise templates linked to training sessions
INSERT INTO exercise_templates (id, name, training_session_id) VALUES
  (1, 'Entrenament Cames',   1),
  (2, 'Entrenament Pit',     2),
  (3, 'Entrenament Esquena', 3),
  (4, 'Recuperació',         4)
ON CONFLICT (id) DO NOTHING;

-- Exercises (now linked to exercise_template_id)
INSERT INTO exercises_jpa_entity (id, exercise_template_id, name) VALUES
  -- Entrenament Cames (template 1)
  (1, 1, 'Sentadilla'),
  (2, 1, 'Press banca'),
  -- Entrenament Pit (template 2)
  (3, 2, 'Peso muerto'),
  -- Entrenament Esquena (template 3)
  (4, 3, 'Remo con barra'),
  -- Recuperació (template 4)
  (5, 4, 'Extensió de quàdriceps'),
  -- Protocol de Genoll (template 10)
  (20, 10, 'Extensió de genoll'),
  (21, 10, 'Flexió de genoll'),
  (22, 10, 'Premsa de cames'),
  -- Protocol Lumbar (template 11)
  (30, 11, 'Pont gluti'),
  (31, 11, 'Bird-dog'),
  (32, 11, 'Planxa abdominal'),
  -- Protocol d''Espatlla (template 12)
  (40, 12, 'Elevació lateral'),
  (41, 12, 'Press militar'),
  (42, 12, 'Rotació externa')
ON CONFLICT (id) DO NOTHING;

-- Exercise sets (embeddable, stored in exercise_sets table)
INSERT INTO exercise_sets (exercise_id, set_number, weight_kg, reps, rest_time_seconds, rpe) VALUES
  -- Sentadilla
  (1, 1, 60.0,  10, 90,  7),
  (1, 2, 60.0,  10, 90,  7),
  (1, 3, 65.0,   8, 90,  8),
  -- Press banca
  (2, 1, 50.0,  12, 60,  6),
  (2, 2, 50.0,  12, 60,  7),
  -- Peso muerto
  (3, 1, 80.0,   6, 120, 8),
  (3, 2, 80.0,   6, 120, 9),
  -- Remo con barra
  (4, 1, 40.0,  12, 60,  6),
  (4, 2, 42.5,  10, 60,  7),
  -- Extensió de quàdriceps
  (5, 1, 30.0,  15, 60,  5),
  (5, 2, 32.5,  12, 60,  6),
  -- Extensió de genoll
  (20, 1, 20.0, 15, 60,  5),
  (20, 2, 22.5, 12, 60,  6),
  (20, 3, 25.0, 10, 60,  7),
  -- Flexió de genoll
  (21, 1, 15.0, 15, 60,  5),
  (21, 2, 17.5, 12, 60,  6),
  (21, 3, 20.0, 10, 60,  7),
  -- Premsa de cames
  (22, 1, 60.0, 12, 90,  6),
  (22, 2, 70.0, 10, 90,  7),
  (22, 3, 80.0,  8, 90,  8),
  -- Pont gluti
  (30, 1,  0.0, 15, 45,  5),
  (30, 2,  0.0, 15, 45,  6),
  (30, 3, 10.0, 12, 45,  7),
  -- Bird-dog
  (31, 1,  0.0, 10, 30,  4),
  (31, 2,  0.0, 10, 30,  5),
  -- Planxa abdominal
  (32, 1,  0.0, 30, 60,  5),
  (32, 2,  0.0, 40, 60,  6),
  (32, 3,  0.0, 45, 60,  7),
  -- Elevació lateral
  (40, 1,  5.0, 15, 60,  5),
  (40, 2,  6.0, 12, 60,  6),
  (40, 3,  7.0, 10, 60,  7),
  -- Press militar
  (41, 1, 30.0, 10, 90,  6),
  (41, 2, 35.0,  8, 90,  7),
  (41, 3, 37.5,  6, 90,  8),
  -- Rotació externa
  (42, 1,  4.0, 15, 45,  5),
  (42, 2,  5.0, 12, 45,  6),
  (42, 3,  6.0, 10, 45,  7)
ON CONFLICT DO NOTHING;
