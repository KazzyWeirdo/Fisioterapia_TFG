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
  { value: 'AUDITOR', labelKey: 'reg_physio_role_auditor', captionKey: 'reg_physio_role_auditor_caption' },
  { value: 'USER',    labelKey: 'reg_physio_role_user',    captionKey: 'reg_physio_role_user_caption'    },
  { value: 'ADMIN',   labelKey: 'reg_physio_role_admin',   captionKey: 'reg_physio_role_admin_caption'   },
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
      setError(t('reg_physio_error_no_role'))
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
      setError(t('reg_physio_error'))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className={styles.page}>
      <p className={styles.adminLabel}>{t('reg_physio_admin_label')}</p>
      <h1 className={styles.title}>{t('register_physio_title')}</h1>
      <p className={styles.subtitle}>{t('reg_physio_subtitle')}</p>

      <form aria-label="Register physiotherapist" onSubmit={handleSubmit}>

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>{t('reg_physio_section_personal')}</h2>
          <p className={styles.sectionSub}>{t('reg_physio_section_personal_sub')}</p>
          <div className={styles.card}>
            <div className={styles.row}>
              <div className={styles.field}>
                <label htmlFor="firstName">{t('reg_physio_first_name')}</label>
                <input
                  id="firstName"
                  name="firstName"
                  value={form.firstName}
                  onChange={handleChange}
                  required
                  placeholder={t('reg_physio_first_name_placeholder')}
                />
              </div>
              <div className={styles.field}>
                <label htmlFor="surname">{t('reg_physio_surname')}</label>
                <input
                  id="surname"
                  name="surname"
                  value={form.surname}
                  onChange={handleChange}
                  required
                  placeholder={t('reg_physio_surname_placeholder')}
                />
              </div>
            </div>
            <div className={styles.field}>
              <label htmlFor="secondSurname">{t('reg_physio_second_surname')}</label>
              <input
                id="secondSurname"
                name="secondSurname"
                value={form.secondSurname}
                onChange={handleChange}
                placeholder={t('reg_physio_second_surname_placeholder')}
              />
            </div>
          </div>
        </section>

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>{t('reg_physio_section_access')}</h2>
          <p className={styles.sectionSub}>{t('reg_physio_section_access_sub')}</p>
          <div className={styles.card}>
            <div className={styles.field}>
              <label htmlFor="email">{t('reg_physio_email_label')}</label>
              <input
                id="email"
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                required
                placeholder={t('reg_physio_email_placeholder')}
              />
            </div>

            <div className={styles.rolesSection}>
              <p className={styles.rolesLabel}>{t('reg_physio_roles_label')}</p>
              <div className={styles.rolesGrid}>
                {ROLES.map(({ value, labelKey, captionKey }) => (
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
                      <span className={styles.roleLabel}>{t(labelKey as Parameters<typeof t>[0])}</span>
                      <span className={styles.roleCaption}>{t(captionKey as Parameters<typeof t>[0])}</span>
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
            <FontAwesomeIcon icon={faXmark} /> {t('reg_physio_discard')}
          </button>
          <button type="submit" disabled={submitting} className={styles.submitBtn}>
            {submitting ? t('common_loading') : <><FontAwesomeIcon icon={faUserPlus} /> {t('register_physio_submit')}</>}
          </button>
        </div>
      </form>
    </div>
  )
}
