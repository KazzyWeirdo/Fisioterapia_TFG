import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faXmark, faUserPlus } from '@fortawesome/free-solid-svg-icons'
import { registerPhysiotherapist } from '../services/physiotherapistService'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './RegisterPhysiotherapistPage.module.css'

interface RegisterPhysiotherapistForm {
  firstName: string
  surname: string
  secondSurname: string
  email: string
  role: string
}

const ROLES = [
  { value: 'AUDITOR', label: 'Auditor', caption: 'AUDIT ACCESS' },
  { value: 'USER', label: 'Physiotherapist User', caption: 'CLINICAL RECORDS ONLY' },
  { value: 'ADMIN', label: 'Administrator', caption: 'PERFORMANCE REVIEW' },
]

export default function RegisterPhysiotherapistPage() {
  const navigate = useNavigate()
  const { t } = useLanguage()
  const [form, setForm] = useState<RegisterPhysiotherapistForm>({
    firstName: '', surname: '', secondSurname: '', email: '', role: '',
  })
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))

  const handleCancel = () => {
    setForm({ firstName: '', surname: '', secondSurname: '', email: '', role: '' })
    navigate('/patients')
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!form.role) {
      setError('Please select a role.')
      return
    }
    setSubmitting(true)
    setError(null)
    try {
      await registerPhysiotherapist({
        name: form.firstName,
        surname: form.surname,
        secondSurname: form.secondSurname || undefined,
        email: form.email,
        role: [form.role],
      })
      navigate('/patients')
    } catch {
      setError('Failed to register physiotherapist. Please check the form and try again.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className={styles.page}>
      <p className={styles.adminLabel}>ADMINISTRATION</p>
      <h1 className={styles.title}>{t('register_physio_title')}</h1>
      <p className={styles.subtitle}>
        Onboard a new medical practitioner to the Clinical Atelier network.
        Ensure all credentials and access levels are verified.
      </p>

      <form aria-label="Register physiotherapist" onSubmit={handleSubmit}>

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>Personal Details</h2>
          <p className={styles.sectionSub}>Basic identification information for the practitioner's professional profile.</p>
          <div className={styles.card}>
            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="firstName">First Name</label>
                <input
                  id="firstName"
                  name="firstName"
                  value={form.firstName}
                  onChange={handleChange}
                  required
                  placeholder="e.g. Julian"
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="surname">Surname</label>
                <input
                  id="surname"
                  name="surname"
                  value={form.surname}
                  onChange={handleChange}
                  required
                  placeholder="e.g. Ross"
                />
              </div>
            </div>
            <div className={styles.field}>
              <label htmlFor="secondSurname">Second Surname</label>
              <input
                id="secondSurname"
                name="secondSurname"
                value={form.secondSurname}
                onChange={handleChange}
                placeholder="e.g. García"
              />
            </div>
          </div>
        </section>

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>Access &amp; Credentials</h2>
          <p className={styles.sectionSub}>Secure credentials and system authorization levels for this user account.</p>
          <div className={styles.card}>
            <div className={styles.field}>
              <label htmlFor="email">PhysiotherapistEmail</label>
              <input
                id="email"
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                required
                placeholder="physio.name@clinicalatelier.com"
              />
            </div>

            <div className={styles.rolesSection}>
              <p className={styles.rolesLabel}>Roles</p>
              <div className={styles.rolesGrid}>
                {ROLES.map(({ value, label, caption }) => (
                  <label
                    key={value}
                    className={`${styles.roleCard} ${form.role === value ? styles.roleCardSelected : ''}`}
                  >
                    <input
                      type="radio"
                      name="role"
                      value={value}
                      checked={form.role === value}
                      onChange={() => setForm(prev => ({ ...prev, role: value }))}
                      className={styles.roleRadio}
                    />
                    <div>
                      <span className={styles.roleLabel}>{label}</span>
                      <span className={styles.roleCaption}>{caption}</span>
                    </div>
                  </label>
                ))}
              </div>
            </div>
          </div>
        </section>

        {error && <p role="alert" className={styles.error}>{error}</p>}

        <div className={styles.footer}>
          <button type="button" onClick={handleCancel} className={styles.discardBtn}>
            <FontAwesomeIcon icon={faXmark} /> Discard Entry
          </button>
          <button type="submit" disabled={submitting} className={styles.submitBtn}>
            {submitting ? t('common_loading') : <><FontAwesomeIcon icon={faUserPlus} /> {t('register_physio_submit')}</>}
          </button>
        </div>
      </form>
    </div>
  )
}
