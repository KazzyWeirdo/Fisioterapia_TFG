import { useEffect, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCalendarDays, faCheck, faShield } from '@fortawesome/free-solid-svg-icons'
import { createTrainingSession } from '../services/trainingSessionService'
import { getPatients, type PatientSummary } from '../services/patientService'
import { getAllExerciseTemplates } from '../services/exerciseTemplateService'
import { useAuth } from '../contexts/AuthContext'
import { decodeJwtPayload } from '../utils/jwt'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './RegisterTrainingSessionPage.module.css'

export default function RegisterTrainingSessionPage() {
  const { t } = useLanguage()
  const { token } = useAuth()
  const [patients, setPatients] = useState<PatientSummary[]>([])
  const [patientId, setPatientId] = useState<number | ''>('')
  const [templates, setTemplates] = useState<{ id: number; name: string }[]>([])
  const [selectedTemplateId, setSelectedTemplateId] = useState<number | ''>('')
  const [physio, setPhysio] = useState<{ id: number; name: string; surname: string } | null>(null)

  const today = new Date().toISOString().slice(0, 10)
  const [sessionDate, setSessionDate] = useState(today)
  const [startTime, setStartTime] = useState('09:00')
  const [endTime, setEndTime] = useState('10:00')
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [submitted, setSubmitted] = useState(false)

  useEffect(() => {
    getPatients(0, 200).then(p => setPatients(p.content)).catch(() => {})
    getAllExerciseTemplates().then(t => setTemplates(t)).catch(() => {})
    if (token) {
      const payload = decodeJwtPayload(token)
      setPhysio({
        id: Number(payload.sub),
        name: String(payload.name),
        surname: String(payload.surname),
      })
    }
  }, [token])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!patientId || !physio || !selectedTemplateId) return
    setSubmitting(true)
    setSubmitError(null)
    try {
      await createTrainingSession({
        patientId,
        physiotherapistId: physio.id,
        startDateTime: `${sessionDate}T${startTime}:00`,
        endDateTime: `${sessionDate}T${endTime}:00`,
        exerciseTemplateId: selectedTemplateId,
      })
      setSubmitted(true)
    } catch {
      setSubmitError(t('training_error'))
    } finally {
      setSubmitting(false)
    }
  }

  if (submitted) {
    return (
      <div className={styles.successPage}>
        <div className={styles.successIcon}><FontAwesomeIcon icon={faCheck} /></div>
        <h2 className={styles.successTitle}>{t('training_success_title')}</h2>
        <p className={styles.successMsg}>{t('training_success_msg')}</p>
      </div>
    )
  }

  return (
    <form className={styles.page} onSubmit={handleSubmit}>

      <div>
        <h1 className={styles.heading}>{t('training_register_title')}</h1>
        <p className={styles.subtitle}>{t('training_register_subtitle')}</p>
      </div>

      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faCalendarDays} /> {t('training_section_general')}
        </div>
        <div className={styles.rowThree}>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="sessionDate">{t('training_field_date')}</label>
            <input
              id="sessionDate"
              type="date"
              className={styles.input}
              value={sessionDate}
              onChange={e => setSessionDate(e.target.value)}
              required
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="startTime">{t('training_field_start_time')}</label>
            <input
              id="startTime"
              type="time"
              className={styles.input}
              value={startTime}
              onChange={e => setStartTime(e.target.value)}
              required
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="endTime">{t('training_field_end_time')}</label>
            <input
              id="endTime"
              type="time"
              className={styles.input}
              value={endTime}
              onChange={e => setEndTime(e.target.value)}
              required
            />
          </div>
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label}>{t('training_field_patient')}</label>
            <select
              className={styles.input}
              value={patientId}
              onChange={e => setPatientId(Number(e.target.value))}
              required
            >
              <option value="">{t('training_patient_placeholder')}</option>
              {patients.map(p => (
                <option key={p.id} value={p.id}>
                  {[p.name, p.surname, p.secondSurname].filter(Boolean).join(' ')}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.field}>
            <label className={styles.label}>{t('training_field_template')}</label>
            <select
              className={styles.input}
              value={selectedTemplateId}
              onChange={e => setSelectedTemplateId(Number(e.target.value))}
              required
            >
              <option value="">{t('training_template_placeholder')}</option>
              {templates.map(tmpl => (
                <option key={tmpl.id} value={tmpl.id}>
                  {tmpl.name}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label}>{t('training_field_physio')}</label>
            <div className={styles.autoFillBox}>
              <span className={styles.autoFillName}>
                <FontAwesomeIcon icon={faShield} /> {physio ? `${physio.name} ${physio.surname}` : '…'}
              </span>
              <span className={styles.autoFillBadge}>{t('indiba_autofilled')}</span>
            </div>
          </div>
        </div>
      </div>

      {submitError && <p className={styles.error}>{submitError}</p>}

      <div className={styles.footer}>
        <button type="submit" className={styles.submitBtn} disabled={submitting || !physio || !selectedTemplateId}>
          {t('training_complete_registration')}
        </button>
      </div>

    </form>
  )
}
