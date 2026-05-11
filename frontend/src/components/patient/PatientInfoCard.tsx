import { useState } from 'react'
import type { PatientDetail, CreatePatientRequest } from '../../services/patientService'
import { updatePatient, updateFunctionalScore, dischargePatient, deletePatient } from '../../services/patientService'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCheck, faHeart, faPenToSquare, faTrash, faXmark } from '@fortawesome/free-solid-svg-icons'
import { useLanguage } from '../../contexts/LanguageContext'
import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form'
import ProgressBar from 'react-bootstrap/ProgressBar'
import Alert from 'react-bootstrap/Alert'
import Badge from 'react-bootstrap/Badge'
import 'bootstrap/dist/css/bootstrap.min.css'
import styles from './PatientInfoCard.module.css'

const GENDER_OPTIONS = ['MALE', 'FEMALE', 'OTHER', 'NONBINARY', 'UNKNOWN']
const SEX_OPTIONS = ['FEMALE', 'MALE', 'COMPLEX', 'UNKNOWN']

interface EditForm {
  legalName: string
  nameToUse: string
  pronouns: string
  surname: string
  secondSurname: string
  dni: string
  dateOfBirth: string
  email: string
  phoneNumber: string
  genderIdentity: string
  clinicalUseSex: string
  administrativeSex: string
  pathology: string
}

function toEditForm(patient: PatientDetail): EditForm {
  return {
    legalName: patient.legalName,
    nameToUse: patient.nameToUse,
    pronouns: patient.pronouns,
    surname: patient.surname,
    secondSurname: patient.secondSurname,
    dni: patient.dni,
    dateOfBirth: patient.dateOfBirth,
    email: patient.email,
    phoneNumber: String(patient.phoneNumber),
    genderIdentity: patient.genderIdentity,
    clinicalUseSex: patient.clinicalUseSex,
    administrativeSex: patient.administrativeSex,
    pathology: patient.pathology ?? '',
  }
}

function formatDateOfBirth(dateStr: string): string {
  const date = new Date(dateStr)
  const today = new Date()
  let age = today.getFullYear() - date.getFullYear()
  const hasBirthdayPassed =
    today.getMonth() > date.getMonth() ||
    (today.getMonth() === date.getMonth() && today.getDate() >= date.getDate())
  if (!hasBirthdayPassed) age -= 1
  return `${date.toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' })} (${age} years)`
}

interface Props {
  patient: PatientDetail
  onPatientUpdated: (updated: PatientDetail) => void
  isAdmin?: boolean
  onPatientDeleted?: () => void
}

interface FieldProps {
  label: string
  value: string
  isEditing: boolean
  editNode?: React.ReactNode
}

function Field({ label, value, isEditing, editNode }: FieldProps) {
  return (
    <div className={styles.field}>
      <span className={styles.label}>{label}</span>
      {isEditing && editNode ? editNode : <span className={styles.value}>{value}</span>}
    </div>
  )
}

export default function PatientInfoCard({ patient, onPatientUpdated, isAdmin = false, onPatientDeleted }: Props) {
  const { t } = useLanguage()
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState<EditForm>(toEditForm(patient))
  const [saving, setSaving] = useState(false)
  const [saveError, setSaveError] = useState<string | null>(null)
  const [scoreInput, setScoreInput] = useState(String(patient.functionalScore ?? ''))
  const [scoreError, setScoreError] = useState<string | null>(null)
  const [scoreUpdating, setScoreUpdating] = useState(false)
  const [discharging, setDischarging] = useState(false)
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteError, setDeleteError] = useState<string | null>(null)

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  function handleEdit() {
    setFormData(toEditForm(patient))
    setIsEditing(true)
  }

  function handleCancel() {
    setIsEditing(false)
    setSaveError(null)
  }

  async function handleSave() {
    setSaving(true)
    setSaveError(null)
    try {
      const payload: CreatePatientRequest = { ...formData, phoneNumber: parseInt(formData.phoneNumber, 10) }
      await updatePatient(patient.id, payload)
      onPatientUpdated({ ...patient, ...formData, phoneNumber: parseInt(formData.phoneNumber, 10) })
      setIsEditing(false)
    } catch {
      setSaveError(t('patient_info_save_error'))
    } finally {
      setSaving(false)
    }
  }

  function input(name: keyof EditForm, type = 'text') {
    return (
      <input
        className={styles.fieldInput}
        name={name}
        type={type}
        value={formData[name]}
        onChange={handleChange}
        required
      />
    )
  }

  async function handleUpdateScore() {
    const score = parseInt(scoreInput, 10)
    if (isNaN(score) || score < 0 || score > 100) {
      setScoreError('Score must be 0-100')
      return
    }
    setScoreUpdating(true)
    setScoreError(null)
    try {
      await updateFunctionalScore(patient.id, score)
      onPatientUpdated({ ...patient, functionalScore: score })
    } catch {
      setScoreError(t('patient_info_save_error'))
    } finally {
      setScoreUpdating(false)
    }
  }

  async function handleConfirmDischarge() {
    setDischarging(true)
    try {
      await dischargePatient(patient.id)
      onPatientUpdated({ ...patient, dischargeDate: new Date().toISOString().slice(0, 10) })
    } finally {
      setDischarging(false)
    }
  }

  async function handleDeleteConfirm() {
    try {
      await deletePatient(patient.id)
      onPatientDeleted?.()
    } catch {
      setDeleteError(t('patient_delete_error'))
    }
  }

  function select(name: keyof EditForm, options: string[]) {
    return (
      <select className={styles.fieldInput} name={name} value={formData[name]} onChange={handleChange} required>
        {options.map(o => <option key={o} value={o}>{o}</option>)}
      </select>
    )
  }

  return (
    <section className={styles.card} aria-label="Personal information">
      <div className={styles.cardHeader}>
        <p className={styles.sectionTitle}>{t('patient_info_section')}</p>
        {!isEditing ? (
          <div className="d-flex align-items-center gap-2 flex-wrap">
            <button type="button" className={styles.editBtn} onClick={handleEdit}>
              <FontAwesomeIcon icon={faPenToSquare} /> {t('common_edit')}
            </button>
            {isAdmin && (
              showDeleteConfirm ? (
                <div className="d-flex align-items-center gap-2 flex-wrap">
                  <span className="text-muted small">{t('patient_delete_confirm_text')}</span>
                  <Button variant="danger" size="sm" onClick={handleDeleteConfirm}>
                    <FontAwesomeIcon icon={faTrash} className="me-1" />
                    {t('patient_confirm_delete')}
                  </Button>
                  <Button variant="outline-secondary" size="sm" onClick={() => setShowDeleteConfirm(false)}>
                    <FontAwesomeIcon icon={faXmark} className="me-1" />
                    {t('common_cancel')}
                  </Button>
                </div>
              ) : (
                <Button variant="outline-danger" size="sm" onClick={() => setShowDeleteConfirm(true)}>
                  <FontAwesomeIcon icon={faTrash} className="me-1" />
                  {t('patient_delete_btn')}
                </Button>
              )
            )}
          </div>
        ) : (
          <div className={styles.actionRow}>
            <button type="button" className={styles.cancelBtn} onClick={handleCancel} disabled={saving}>
              <FontAwesomeIcon icon={faXmark} /> {t('common_cancel')}
            </button>
            <button type="button" className={styles.saveBtn} onClick={handleSave} disabled={saving}>
              <FontAwesomeIcon icon={faCheck} /> {saving ? t('common_saving') : t('common_save_changes')}
            </button>
          </div>
        )}
      </div>
      {deleteError && (
        <Alert variant="danger" className="mt-2 mb-0 py-1 px-2 small">
          {deleteError}
        </Alert>
      )}

      <div className={styles.rowGroup}>
        <Field label={t('patient_info_legal_name')} value={patient.legalName} isEditing={isEditing} editNode={input('legalName')} />
        <Field label={t('patient_info_name_to_use')} value={patient.nameToUse} isEditing={isEditing} editNode={input('nameToUse')} />
        <Field label={t('patient_info_surname')} value={patient.surname} isEditing={isEditing} editNode={input('surname')} />
      </div>

      <div className={styles.rowGroup}>
        <Field label={t('patient_info_second_surname')} value={patient.secondSurname} isEditing={isEditing} editNode={input('secondSurname')} />
        <Field label={t('patient_info_dni')} value={patient.dni} isEditing={isEditing} editNode={input('dni')} />
        <Field
          label={t('patient_info_dob')}
          value={formatDateOfBirth(patient.dateOfBirth)}
          isEditing={isEditing}
          editNode={input('dateOfBirth', 'date')}
        />
      </div>

      <div className={styles.rowGroup}>
        <Field label={t('patient_info_email')} value={patient.email} isEditing={isEditing} editNode={input('email', 'email')} />
        <Field label={t('patient_info_phone')} value={String(patient.phoneNumber)} isEditing={isEditing} editNode={input('phoneNumber', 'tel')} />
        <Field label={t('patient_info_gender')} value={patient.genderIdentity} isEditing={isEditing} editNode={select('genderIdentity', GENDER_OPTIONS)} />
      </div>

      <div className={styles.rowGroup}>
        <Field label={t('patient_info_clinical_sex')} value={patient.clinicalUseSex} isEditing={isEditing} editNode={select('clinicalUseSex', SEX_OPTIONS)} />
        <Field label={t('patient_info_admin_sex')} value={patient.administrativeSex} isEditing={isEditing} editNode={select('administrativeSex', SEX_OPTIONS)} />
        <Field label={t('patient_info_pronouns')} value={patient.pronouns} isEditing={isEditing} editNode={input('pronouns')} />
      </div>

      {isEditing && saveError && (
        <p className={styles.saveError} role="alert">{saveError}</p>
      )}

      {/* Pathology & Registration */}
      {(patient.pathology || patient.registrationDate) && (
        <div className={styles.rowGroupLast}>
          {patient.pathology && (
            <div className={styles.field}>
              <span className={styles.label}>{t('patient_pathology')}</span>
              <span className={styles.value}>{t('pathology_' + patient.pathology.toLowerCase())}</span>
            </div>
          )}
          {patient.registrationDate && (
            <div className={styles.field}>
              <span className={styles.label}>{t('patient_registration_date')}</span>
              <span className={styles.value}>{patient.registrationDate}</span>
            </div>
          )}
        </div>
      )}

      {/* Functional Score */}
      <div className={styles.polarSection}>
        <p className={styles.sectionTitle}>{t('patient_functional_score')}</p>
        {(() => {
          const score = patient.functionalScore ?? 0
          const variant = score >= 80 ? 'success' : score >= 50 ? 'warning' : 'danger'
          return (
            <>
              <ProgressBar
                now={score}
                label={`${score}/100`}
                variant={variant}
                style={{ height: 18, marginBottom: 8 }}
              />
              {score >= 80 && !patient.dischargeDate && (
                <Alert variant="success" className="d-flex align-items-center gap-2 py-2 px-3 mb-2">
                  <span className="fw-semibold">{t('patient_functional_advisory')}</span>
                  <Button
                    variant="success"
                    size="sm"
                    onClick={handleConfirmDischarge}
                    disabled={discharging}
                  >
                    {t('patient_confirm_discharge')}
                  </Button>
                </Alert>
              )}
              {patient.dischargeDate && (
                <div className="mb-2">
                  <Badge bg="success" className="fs-6">
                    {t('patient_discharged_badge')} · {patient.dischargeDate}
                  </Badge>
                </div>
              )}
              {!patient.dischargeDate && (
                <div style={{ maxWidth: 180 }}>
                  <Form.Control
                    type="number"
                    min={0}
                    max={100}
                    value={scoreInput}
                    onChange={e => setScoreInput(e.target.value)}
                    isInvalid={!!scoreError}
                    size="sm"
                  />
                  <Form.Control.Feedback type="invalid">{scoreError}</Form.Control.Feedback>
                  <Button variant="primary" size="sm" className="mt-2 w-100 rounded-pill" onClick={handleUpdateScore} disabled={scoreUpdating}>
                    {t('patient_score_update_btn')}
                  </Button>
                </div>
              )}
            </>
          )
        })()}
      </div>

      <div className={styles.polarSection}>
        <p className={styles.sectionTitle}>{t('polar_section')}</p>
        {patient.hasPolarConnection ? (
          <div className={styles.polarConnected}>
            <FontAwesomeIcon icon={faCheck} className={styles.polarConnectedIcon} />
            <span className={styles.polarConnectedText}>{t('polar_connected')}</span>
          </div>
        ) : (
          <div className={styles.polarDisconnected}>
            <p className={styles.polarDisconnectedMsg}>
              {t('polar_disconnected_msg')}
            </p>
            <button
              type="button"
              className={styles.polarBtn}
              onClick={() => window.open(`${import.meta.env.VITE_API_BASE_URL}/api/auth/polar/authorize?patientId=${patient.id}`, '_blank')}
            >
              <FontAwesomeIcon icon={faHeart} /> {t('polar_connect_btn')}
            </button>
          </div>
        )}
      </div>
    </section>
  )
}
