import { useState } from 'react'
import type { PatientDetail, CreatePatientRequest } from '../../services/patientService'
import { updatePatient } from '../../services/patientService'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCheck, faHeart, faPenToSquare, faXmark } from '@fortawesome/free-solid-svg-icons'
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
      setSaveError('Failed to save changes. Please try again.')
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
        <p className={styles.sectionTitle}>PERSONAL INFORMATION</p>
        {!isEditing ? (
          <button type="button" className={styles.editBtn} onClick={handleEdit}>
            <FontAwesomeIcon icon={faPenToSquare} /> Edit
          </button>
        ) : (
          <div className={styles.actionRow}>
            <button type="button" className={styles.cancelBtn} onClick={handleCancel} disabled={saving}>
              <FontAwesomeIcon icon={faXmark} /> Cancel
            </button>
            <button type="button" className={styles.saveBtn} onClick={handleSave} disabled={saving}>
              <FontAwesomeIcon icon={faCheck} /> {saving ? 'Saving…' : 'Save Changes'}
            </button>
          </div>
        )}
      </div>

      <div className={styles.rowGroup}>
        <Field label="LEGAL NAME (FULL)" value={patient.legalName} isEditing={isEditing} editNode={input('legalName')} />
        <Field label="NAME TO USE" value={patient.nameToUse} isEditing={isEditing} editNode={input('nameToUse')} />
        <Field label="SURNAME" value={patient.surname} isEditing={isEditing} editNode={input('surname')} />
      </div>

      <div className={styles.rowGroup}>
        <Field label="SECOND SURNAME" value={patient.secondSurname} isEditing={isEditing} editNode={input('secondSurname')} />
        <Field label="DNI / IDENTIFICATION" value={patient.dni} isEditing={isEditing} editNode={input('dni')} />
        <Field
          label="DATE OF BIRTH"
          value={formatDateOfBirth(patient.dateOfBirth)}
          isEditing={isEditing}
          editNode={input('dateOfBirth', 'date')}
        />
      </div>

      <div className={styles.rowGroup}>
        <Field label="EMAIL ADDRESS" value={patient.email} isEditing={isEditing} editNode={input('email', 'email')} />
        <Field label="PHONE NUMBER" value={String(patient.phoneNumber)} isEditing={isEditing} editNode={input('phoneNumber', 'tel')} />
        <Field label="GENDER IDENTITY" value={patient.genderIdentity} isEditing={isEditing} editNode={select('genderIdentity', GENDER_OPTIONS)} />
      </div>

      <div className={styles.rowGroupLast}>
        <Field label="CLINICAL USE SEX" value={patient.clinicalUseSex} isEditing={isEditing} editNode={select('clinicalUseSex', SEX_OPTIONS)} />
        <Field label="ADMINISTRATIVE SEX" value={patient.administrativeSex} isEditing={isEditing} editNode={select('administrativeSex', SEX_OPTIONS)} />
        <Field label="PRONOUNS" value={patient.pronouns} isEditing={isEditing} editNode={input('pronouns')} />
      </div>

      {isEditing && saveError && (
        <p className={styles.saveError} role="alert">{saveError}</p>
      )}

      <div className={styles.polarSection}>
        <p className={styles.sectionTitle}>POLAR INTEGRATION</p>
        {patient.hasPolarConnection ? (
          <div className={styles.polarConnected}>
            <FontAwesomeIcon icon={faCheck} className={styles.polarConnectedIcon} />
            <span className={styles.polarConnectedText}>Polar Connected</span>
          </div>
        ) : (
          <div className={styles.polarDisconnected}>
            <p className={styles.polarDisconnectedMsg}>
              No Polar account linked. Connect to enable automatic sleep and recovery sync.
            </p>
            <button
              type="button"
              className={styles.polarBtn}
              onClick={() => window.open(`/api/auth/polar/authorize?patientId=${patient.id}`, '_blank')}
            >
              <FontAwesomeIcon icon={faHeart} /> Connect Polar Account
            </button>
          </div>
        )}
      </div>
    </section>
  )
}
