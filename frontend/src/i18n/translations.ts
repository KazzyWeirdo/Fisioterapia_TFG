export type Locale = 'en' | 'es'

export const translations = {
  en: {
    // Layout
    logout: 'Logout',
    sidebar_main: 'MAIN',
    sidebar_actions: 'ACTIONS',
    nav_patients: 'Patients',
    nav_audit: 'Audit Logs',
    nav_register_patient: 'Register Patient',
    nav_register_indiba: 'Register Indiba Session',
    nav_register_physio: 'Register Physiotherapist',

    // Auth
    login_title: 'Welcome back',
    login_email_label: 'Practitioner Email',
    login_password_label: 'Password',
    login_submit: 'Login',
    login_change_password: 'Change password ›',
    login_footer: 'Secure Access Point',
    login_error_email_required: 'Email is required',
    login_error_password_required: 'Password is required',
    login_error_invalid: 'Email non-existent or incorrect password',

    forgot_title: 'Reset your password',
    forgot_subtitle: "Enter your email and we'll send you a reset link.",
    forgot_email_label: 'Practitioner Email',
    forgot_submit: 'Send reset link',
    forgot_back: '‹ Back to login',
    forgot_success: 'Check your inbox for the reset link.',
    forgot_error: 'Could not send reset email. Please try again.',

    reset_title: 'Set new password',
    reset_subtitle: 'Choose a strong password for your account.',
    reset_new_label: 'New password',
    reset_confirm_label: 'Confirm password',
    reset_submit: 'Reset password',
    reset_error_mismatch: 'Passwords do not match.',
    reset_error_invalid_token: 'Reset link is invalid or expired.',

    // Patients page
    patients_title: 'Patients',
    patients_search_placeholder: 'Search patients…',
    patients_export_csv: 'Export CSV',
    patients_empty: 'No patients found.',
    patients_load_error: 'Failed to load patients.',

    // Patient detail
    patient_tab_indiba: 'INDIBA Sessions',
    patient_tab_training: 'Training Sessions',
    patient_tab_pni: 'PNI Reports',
    patient_tab_stats: 'Statistics',
    patient_not_found: 'Patient not found.',

    // Register patient
    register_patient_title: 'Register Patient',
    register_patient_submit: 'Register Patient',
    register_patient_discard: 'DISCARD ENTRY',

    // Register physiotherapist
    register_physio_title: 'Register Physiotherapist',
    register_physio_submit: 'Register Physiotherapist',
    register_physio_discard: 'DISCARD ENTRY',

    // INDIBA session
    indiba_register_title: 'Register INDIBA Session',
    indiba_register_subtitle: 'Document a new INDIBA treatment session with clinical parameters and observations.',
    indiba_section_timing: 'SESSION TIMING',
    indiba_section_params: 'TREATMENT PARAMETERS',
    indiba_section_observations: 'CLINICAL OBSERVATIONS',
    indiba_begin: 'BEGIN SESSION',
    indiba_end: 'END SESSION',
    indiba_patient: 'PATIENT',
    indiba_patient_placeholder: 'Search or select patient...',
    indiba_area: 'TREATED AREA',
    indiba_area_placeholder: 'e.g. Lumbar spine, Right shoulder',
    indiba_mode: 'MODE',
    indiba_intensity: 'INTENSITY',
    indiba_objective: 'OBJECTIVE',
    indiba_objective_placeholder: 'State the primary goal for this specific session',
    indiba_physio: 'ATTENDING PHYSIOTHERAPIST',
    indiba_autofilled: 'AUTO-FILLED',
    indiba_observations_label: 'OBSERVATIONS',
    indiba_observations_placeholder: 'Document patient feedback, tissue response, and post-session functional changes...',
    indiba_submit: 'REGISTER SESSION',
    indiba_discard: 'DISCARD ENTRY',
    indiba_error: 'Failed to register session. Please check the fields and try again.',

    // Training session
    training_register_title: 'Register Training Session',
    training_submit: 'REGISTER SESSION',
    training_discard: 'DISCARD ENTRY',
    training_error: 'Failed to register session. Please check the fields and try again.',

    // PNI Report
    pni_title: 'PNI Report',
    pni_empty: 'No PNI reports available.',

    // Statistics tab
    stats_transition_title: 'Monthly Transition Ratio',
    stats_workload_title: 'Workload Progression',

    // Audit log
    audit_title: 'Audit Logs',
    audit_empty: 'No audit logs found.',
    audit_load_error: 'Failed to load audit logs.',

    // Common
    common_loading: 'Loading…',
    common_error: 'Something went wrong.',
    common_save: 'Save',
    common_cancel: 'Cancel',
    common_back: 'Back',
    common_name: 'Name',
    common_email: 'Email',
    common_date: 'Date',
    common_actions: 'Actions',
  },

  es: {
    // Layout
    logout: 'Cerrar sesión',
    sidebar_main: 'PRINCIPAL',
    sidebar_actions: 'ACCIONES',
    nav_patients: 'Pacientes',
    nav_audit: 'Registros de auditoría',
    nav_register_patient: 'Registrar paciente',
    nav_register_indiba: 'Registrar sesión INDIBA',
    nav_register_physio: 'Registrar fisioterapeuta',

    // Auth
    login_title: 'Bienvenido',
    login_email_label: 'Correo del profesional',
    login_password_label: 'Contraseña',
    login_submit: 'Iniciar sesión',
    login_change_password: 'Cambiar contraseña ›',
    login_footer: 'Acceso seguro',
    login_error_email_required: 'El correo es obligatorio',
    login_error_password_required: 'La contraseña es obligatoria',
    login_error_invalid: 'Correo inexistente o contraseña incorrecta',

    forgot_title: 'Restablecer contraseña',
    forgot_subtitle: 'Introduce tu correo y te enviaremos un enlace de restablecimiento.',
    forgot_email_label: 'Correo del profesional',
    forgot_submit: 'Enviar enlace',
    forgot_back: '‹ Volver al inicio de sesión',
    forgot_success: 'Revisa tu bandeja de entrada.',
    forgot_error: 'No se pudo enviar el correo. Inténtalo de nuevo.',

    reset_title: 'Nueva contraseña',
    reset_subtitle: 'Elige una contraseña segura para tu cuenta.',
    reset_new_label: 'Nueva contraseña',
    reset_confirm_label: 'Confirmar contraseña',
    reset_submit: 'Restablecer contraseña',
    reset_error_mismatch: 'Las contraseñas no coinciden.',
    reset_error_invalid_token: 'El enlace de restablecimiento no es válido o ha expirado.',

    // Patients page
    patients_title: 'Pacientes',
    patients_search_placeholder: 'Buscar pacientes…',
    patients_export_csv: 'Exportar CSV',
    patients_empty: 'No se encontraron pacientes.',
    patients_load_error: 'Error al cargar los pacientes.',

    // Patient detail
    patient_tab_indiba: 'Sesiones INDIBA',
    patient_tab_training: 'Sesiones de entrenamiento',
    patient_tab_pni: 'Informes PNI',
    patient_tab_stats: 'Estadísticas',
    patient_not_found: 'Paciente no encontrado.',

    // Register patient
    register_patient_title: 'Registrar paciente',
    register_patient_submit: 'Registrar paciente',
    register_patient_discard: 'DESCARTAR',

    // Register physiotherapist
    register_physio_title: 'Registrar fisioterapeuta',
    register_physio_submit: 'Registrar fisioterapeuta',
    register_physio_discard: 'DESCARTAR',

    // INDIBA session
    indiba_register_title: 'Registrar sesión INDIBA',
    indiba_register_subtitle: 'Documenta una nueva sesión de tratamiento INDIBA con parámetros clínicos y observaciones.',
    indiba_section_timing: 'HORARIO DE SESIÓN',
    indiba_section_params: 'PARÁMETROS DE TRATAMIENTO',
    indiba_section_observations: 'OBSERVACIONES CLÍNICAS',
    indiba_begin: 'INICIO DE SESIÓN',
    indiba_end: 'FIN DE SESIÓN',
    indiba_patient: 'PACIENTE',
    indiba_patient_placeholder: 'Buscar o seleccionar paciente...',
    indiba_area: 'ZONA TRATADA',
    indiba_area_placeholder: 'p. ej. Columna lumbar, Hombro derecho',
    indiba_mode: 'MODO',
    indiba_intensity: 'INTENSIDAD',
    indiba_objective: 'OBJETIVO',
    indiba_objective_placeholder: 'Indica el objetivo principal de esta sesión',
    indiba_physio: 'FISIOTERAPEUTA ASIGNADO',
    indiba_autofilled: 'AUTO-RELLENO',
    indiba_observations_label: 'OBSERVACIONES',
    indiba_observations_placeholder: 'Documenta la respuesta del paciente, la respuesta tisular y los cambios funcionales post-sesión...',
    indiba_submit: 'REGISTRAR SESIÓN',
    indiba_discard: 'DESCARTAR',
    indiba_error: 'Error al registrar la sesión. Revisa los campos e inténtalo de nuevo.',

    // Training session
    training_register_title: 'Registrar sesión de entrenamiento',
    training_submit: 'REGISTRAR SESIÓN',
    training_discard: 'DESCARTAR',
    training_error: 'Error al registrar la sesión. Revisa los campos e inténtalo de nuevo.',

    // PNI Report
    pni_title: 'Informe PNI',
    pni_empty: 'No hay informes PNI disponibles.',

    // Statistics tab
    stats_transition_title: 'Ratio de transición mensual',
    stats_workload_title: 'Progresión de carga de trabajo',

    // Audit log
    audit_title: 'Registros de auditoría',
    audit_empty: 'No se encontraron registros de auditoría.',
    audit_load_error: 'Error al cargar los registros de auditoría.',

    // Common
    common_loading: 'Cargando…',
    common_error: 'Algo salió mal.',
    common_save: 'Guardar',
    common_cancel: 'Cancelar',
    common_back: 'Volver',
    common_name: 'Nombre',
    common_email: 'Correo',
    common_date: 'Fecha',
    common_actions: 'Acciones',
  },
} satisfies Record<Locale, Record<string, string>>

export type TranslationKey = keyof typeof translations.en
