import type { PatientDetail } from '../../services/patientService'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCheck, faHeart } from '@fortawesome/free-solid-svg-icons'
import styles from './PatientInfoCard.module.css'

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
}

interface Field {
  label: string
  value: string
}

function Field({ label, value }: Field) {
  return (
    <div className={styles.field}>
      <span className={styles.label}>{label}</span>
      <span className={styles.value}>{value}</span>
    </div>
  )
}

export default function PatientInfoCard({ patient }: Props) {
  return (
    <section className={styles.card} aria-label="Personal information">
      <p className={styles.sectionTitle}>PERSONAL INFORMATION</p>

      <div className={styles.rowGroup}>
        <Field label="LEGAL NAME (FULL)" value={patient.legalName} />
        <Field label="NAME TO USE" value={patient.nameToUse} />
        <Field label="SURNAME" value={patient.surname} />
      </div>

      <div className={styles.rowGroup}>
        <Field label="SECOND SURNAME" value={patient.secondSurname} />
        <Field label="DNI / IDENTIFICATION" value={patient.dni} />
        <Field label="DATE OF BIRTH" value={formatDateOfBirth(patient.dateOfBirth)} />
      </div>

      <div className={styles.rowGroup}>
        <Field label="EMAIL ADDRESS" value={patient.email} />
        <Field label="PHONE NUMBER" value={String(patient.phoneNumber)} />
        <Field label="GENDER IDENTITY" value={patient.genderIdentity} />
      </div>

      <div className={styles.rowGroupLast}>
        <Field label="CLINICAL USE SEX" value={patient.clinicalUseSex} />
        <Field label="ADMINISTRATIVE SEX" value={patient.administrativeSex} />
        <Field label="PRONOUNS" value={patient.pronouns} />
      </div>

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
