import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faIdCard, faAddressCard, faUsers, faHeart, faUser } from '@fortawesome/free-solid-svg-icons'
import { createPatient } from '../services/patientService'
import styles from './RegisterPatientPage.module.css'

interface RegisterPatientForm {
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

const EMPTY_FORM: RegisterPatientForm = {
  legalName: '',
  nameToUse: '',
  pronouns: '',
  surname: '',
  secondSurname: '',
  dni: '',
  dateOfBirth: '',
  email: '',
  phoneNumber: '',
  genderIdentity: '',
  clinicalUseSex: '',
  administrativeSex: '',
}

const GENDER_OPTIONS = ['MALE', 'FEMALE', 'OTHER', 'NONBINARY', 'UNKNOWN']
const SEX_OPTIONS = ['FEMALE', 'MALE', 'COMPLEX', 'UNKNOWN']

export default function RegisterPatientPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState<RegisterPatientForm>(EMPTY_FORM)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  const handleDiscard = () => {
    setForm(EMPTY_FORM)
    navigate('/patients')
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSubmitting(true)
    setError(null)
    try {
      await createPatient({ ...form, phoneNumber: parseInt(form.phoneNumber, 10) })
      navigate('/patients')
    } catch {
      setError('Failed to register patient. Please check the form and try again.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className={styles.page}>
      <h1 className={styles.title}>Register Patient</h1>
      <p className={styles.subtitle}>
        Onboard a new patient to the clinical system. Ensure all legal information matches official
        identification documents.
      </p>

      <div className={styles.layout}>
        <form
          id="register-patient-form"
          aria-label="Register patient"
          onSubmit={handleSubmit}
          className={styles.formCard}
        >
          {/* Personal Information */}
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>
              <FontAwesomeIcon icon={faIdCard} /> Personal Information
            </h2>

            <div className={styles.field}>
              <label htmlFor="legalName">Legal Name (Full)</label>
              <input
                id="legalName"
                name="legalName"
                type="text"
                value={form.legalName}
                onChange={handleChange}
                placeholder="As it appears on official documents"
                required
              />
            </div>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="nameToUse">Name to Use</label>
                <input
                  id="nameToUse"
                  name="nameToUse"
                  type="text"
                  value={form.nameToUse}
                  onChange={handleChange}
                  placeholder="Preferred first name"
                  required
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="pronouns">Pronouns</label>
                <input
                  id="pronouns"
                  name="pronouns"
                  type="text"
                  value={form.pronouns}
                  onChange={handleChange}
                  placeholder="e.g. He/Him, They/Them"
                  required
                />
              </div>
            </div>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="surname">Surname</label>
                <input
                  id="surname"
                  name="surname"
                  type="text"
                  value={form.surname}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="secondSurname">Second Surname</label>
                <input
                  id="secondSurname"
                  name="secondSurname"
                  type="text"
                  value={form.secondSurname}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="dni">DNI / Identification</label>
                <input
                  id="dni"
                  name="dni"
                  type="text"
                  value={form.dni}
                  onChange={handleChange}
                  placeholder="ID Number"
                  required
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="dateOfBirth">Date of Birth</label>
                <input
                  id="dateOfBirth"
                  name="dateOfBirth"
                  type="date"
                  value={form.dateOfBirth}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
          </section>

          {/* Contact Details */}
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>
              <FontAwesomeIcon icon={faAddressCard} /> Contact Details
            </h2>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="email">Email Address</label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  value={form.email}
                  onChange={handleChange}
                  placeholder="patient@example.com"
                  required
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="phoneNumber">Phone Number</label>
                <input
                  id="phoneNumber"
                  name="phoneNumber"
                  type="tel"
                  value={form.phoneNumber}
                  onChange={handleChange}
                  placeholder="000000000"
                  required
                />
              </div>
            </div>
          </section>

          {/* Clinical & Administrative Identity */}
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>
              <FontAwesomeIcon icon={faUsers} /> Clinical &amp; Administrative Identity
            </h2>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="genderIdentity">Gender Identity</label>
                <select
                  id="genderIdentity"
                  name="genderIdentity"
                  value={form.genderIdentity}
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled>Select Option</option>
                  {GENDER_OPTIONS.map(opt => (
                    <option key={opt} value={opt}>{opt}</option>
                  ))}
                </select>
              </div>
              <div className={styles.field}>
                <label htmlFor="clinicalUseSex">Clinical Use Sex</label>
                <select
                  id="clinicalUseSex"
                  name="clinicalUseSex"
                  value={form.clinicalUseSex}
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled>Select Option</option>
                  {SEX_OPTIONS.map(opt => (
                    <option key={opt} value={opt}>{opt}</option>
                  ))}
                </select>
              </div>
              <div className={styles.field}>
                <label htmlFor="administrativeSex">Administrative Sex</label>
                <select
                  id="administrativeSex"
                  name="administrativeSex"
                  value={form.administrativeSex}
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled>Select Option</option>
                  {SEX_OPTIONS.map(opt => (
                    <option key={opt} value={opt}>{opt}</option>
                  ))}
                </select>
              </div>
            </div>
          </section>

          {error && (
            <p role="alert" className={styles.error}>
              {error}
            </p>
          )}
        </form>

        <aside className={styles.polarCard}>
          <div className={styles.polarIconWrap}>
            <FontAwesomeIcon icon={faHeart} className={styles.polarIcon} />
          </div>
          <h3 className={styles.polarTitle}>Polar Ecosystem</h3>
          <p className={styles.polarDesc}>
            Connect patient's biometric sensors to sync heart rate, sleep, and recovery metrics
            automatically.
          </p>
          <p className={styles.polarNote}>
            After saving, open the patient's profile to connect their Polar account.
          </p>
          <p className={styles.polarCaption}>ENCRYPTED POLAR API INTEGRATION</p>
        </aside>
      </div>

      <div className={styles.footer}>
        <button type="button" onClick={handleDiscard} className={styles.discardBtn}>
          × Discard Entry
        </button>
        <button
          type="submit"
          form="register-patient-form"
          disabled={submitting}
          className={styles.submitBtn}
        >
          {submitting ? 'Registering…' : <><FontAwesomeIcon icon={faUser} /> Register Patient</>}
        </button>
      </div>
    </div>
  )
}
