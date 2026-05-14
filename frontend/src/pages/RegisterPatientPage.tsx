import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faIdCard, faAddressCard, faUsers, faHeart, faUser } from '@fortawesome/free-solid-svg-icons'
import { createPatient } from '../services/patientService'
import { useLanguage } from '../contexts/LanguageContext'
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
  const { t } = useLanguage()
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
      setError(t('reg_patient_error'))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className={styles.page}>
      <h1 className={styles.title}>{t('register_patient_title')}</h1>
      <p className={styles.subtitle}>{t('reg_patient_subtitle')}</p>

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
              <FontAwesomeIcon icon={faIdCard} /> {t('reg_patient_section_personal')}
            </h2>

            <div className={styles.field}>
              <label htmlFor="legalName">{t('reg_patient_legal_name')}</label>
              <input
                id="legalName"
                name="legalName"
                type="text"
                value={form.legalName}
                onChange={handleChange}
                placeholder={t('reg_patient_legal_name_placeholder')}
                required
              />
            </div>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="nameToUse">{t('reg_patient_name_to_use')}</label>
                <input
                  id="nameToUse"
                  name="nameToUse"
                  type="text"
                  value={form.nameToUse}
                  onChange={handleChange}
                  placeholder={t('reg_patient_name_to_use_placeholder')}
                  required
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="pronouns">{t('reg_patient_pronouns')}</label>
                <input
                  id="pronouns"
                  name="pronouns"
                  type="text"
                  value={form.pronouns}
                  onChange={handleChange}
                  placeholder={t('reg_patient_pronouns_placeholder')}
                  required
                />
              </div>
            </div>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="surname">{t('reg_patient_surname')}</label>
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
                <label htmlFor="secondSurname">{t('reg_patient_second_surname')}</label>
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
                <label htmlFor="dni">{t('reg_patient_dni')}</label>
                <input
                  id="dni"
                  name="dni"
                  type="text"
                  value={form.dni}
                  onChange={handleChange}
                  placeholder={t('reg_patient_dni_placeholder')}
                  required
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="dateOfBirth">{t('reg_patient_dob')}</label>
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
              <FontAwesomeIcon icon={faAddressCard} /> {t('reg_patient_section_contact')}
            </h2>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="email">{t('reg_patient_email')}</label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  value={form.email}
                  onChange={handleChange}
                  placeholder={t('reg_patient_email_placeholder')}
                  required
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="phoneNumber">{t('reg_patient_phone')}</label>
                <input
                  id="phoneNumber"
                  name="phoneNumber"
                  type="tel"
                  value={form.phoneNumber}
                  onChange={handleChange}
                  placeholder={t('reg_patient_phone_placeholder')}
                  required
                />
              </div>
            </div>
          </section>

          {/* Clinical & Administrative Identity */}
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>
              <FontAwesomeIcon icon={faUsers} /> {t('reg_patient_section_clinical')}
            </h2>

            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="genderIdentity">{t('reg_patient_gender')}</label>
                <select
                  id="genderIdentity"
                  name="genderIdentity"
                  value={form.genderIdentity}
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled>{t('reg_patient_select_option')}</option>
                  {GENDER_OPTIONS.map(opt => (
                    <option key={opt} value={opt}>{opt}</option>
                  ))}
                </select>
              </div>
              <div className={styles.field}>
                <label htmlFor="clinicalUseSex">{t('reg_patient_clinical_sex')}</label>
                <select
                  id="clinicalUseSex"
                  name="clinicalUseSex"
                  value={form.clinicalUseSex}
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled>{t('reg_patient_select_option')}</option>
                  {SEX_OPTIONS.map(opt => (
                    <option key={opt} value={opt}>{opt}</option>
                  ))}
                </select>
              </div>
              <div className={styles.field}>
                <label htmlFor="administrativeSex">{t('reg_patient_admin_sex')}</label>
                <select
                  id="administrativeSex"
                  name="administrativeSex"
                  value={form.administrativeSex}
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled>{t('reg_patient_select_option')}</option>
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
          <h3 className={styles.polarTitle}>{t('reg_patient_polar_title')}</h3>
          <p className={styles.polarDesc}>{t('reg_patient_polar_desc')}</p>
          <p className={styles.polarNote}>{t('reg_patient_polar_note')}</p>
          <p className={styles.polarCaption}>{t('reg_patient_polar_caption')}</p>
        </aside>
      </div>

      <div className={styles.footer}>
        <button type="button" onClick={handleDiscard} className={styles.discardBtn}>
          {t('reg_patient_discard')}
        </button>
        <button
          type="submit"
          form="register-patient-form"
          disabled={submitting}
          className={styles.submitBtn}
        >
          {submitting ? t('common_loading') : <><FontAwesomeIcon icon={faUser} /> {t('register_patient_submit')}</>}
        </button>
      </div>
    </div>
  )
}
