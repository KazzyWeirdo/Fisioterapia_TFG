import { useState } from 'react'
import type { PatientDetail, CreatePatientRequest } from '../../services/patientService'
import { updatePatient } from '../../services/patientService'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCheck, faHeart, faPenToSquare, faXmark } from '@fortawesome/free-solid-svg-icons'
import { useLanguage } from '../../contexts/LanguageContext'
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

export default function PatientInfoCard({ patient, onPatientUpdated }: Props) {
  const { t } = useLanguage()
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState<EditForm>(toEditForm(patient))
  const [saving, setSaving] = useState(false)
  const [saveError, setSaveError] = useState<string | null>(null)

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
          <button type="button" className={styles.editBtn} onClick={handleEdit}>
            <FontAwesomeIcon icon={faPenToSquare} /> {t('common_edit')}
          </button>
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

      <div className={styles.rowGroupLast}>
        <Field label={t('patient_info_clinical_sex')} value={patient.clinicalUseSex} isEditing={isEditing} editNode={select('clinicalUseSex', SEX_OPTIONS)} />
        <Field label={t('patient_info_admin_sex')} value={patient.administrativeSex} isEditing={isEditing} editNode={select('administrativeSex', SEX_OPTIONS)} />
        <Field label={t('patient_info_pronouns')} value={patient.pronouns} isEditing={isEditing} editNode={input('pronouns')} />
      </div>

      {isEditing && saveError && (
        <p className={styles.saveError} role="alert">{saveError}</p>
      )}

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
              onClick={() => window.open(`/api/auth/polar/authorize?patientId=${patient.id}`, '_blank')}
            >
              <FontAwesomeIcon icon={faHeart} /> {t('polar_connect_btn')}
            </button>
          </div>
        )}
      </div>
    </section>
  )
}
