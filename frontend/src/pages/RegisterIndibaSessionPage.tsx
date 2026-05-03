import { useEffect, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faClock, faGear, faShield, faEye, faTrash, faCheck } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { useLanguage } from '../contexts/LanguageContext'
import { createIndibaSession } from '../services/indibaService'
import type { PhysiotherapistSummary } from '../services/physiotherapistService'
import { getPatients, type PatientSummary } from '../services/patientService'
import styles from './RegisterIndibaSessionPage.module.css'

function parseTokenPayload(token: string): { sub: string; name: string; surname: string } {
  const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
  const json = decodeURIComponent(atob(base64).split('').map(c => '%' + c.charCodeAt(0).toString(16).padStart(2, '0')).join(''))
  return JSON.parse(json)
}

const MODES = ['CAPACITIVE', 'RESISTIVE', 'DUAL']

const EMPTY_FORM = {
  patientId: '' as number | '',
  beginSession: '',
  endSession: '',
  treatedArea: '',
  mode: 'CAPACITIVE',
  capacitiveIntensity: '40',
  resistiveIntensity: '40',
  objective: '',
  observations: '',
}

export default function RegisterIndibaSessionPage() {
  const navigate = useNavigate()
  const { token } = useAuth()
  const { t } = useLanguage()
  const [form, setForm] = useState(EMPTY_FORM)
  const [physio, setPhysio] = useState<PhysiotherapistSummary | null>(null)
  const [patients, setPatients] = useState<PatientSummary[]>([])
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!token) return
    const { sub, name, surname } = parseTokenPayload(token)
    setPhysio({ id: Number(sub), name, surname })
    getPatients(0, 100).then(p => setPatients(p.content)).catch(() => {})
  }, [token])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!physio) return
    setSubmitting(true)
    setError(null)
    try {
      const isDual = form.mode === 'DUAL'
      const isCapacitive = form.mode === 'CAPACITIVE'
      await createIndibaSession({
        patientId: Number(form.patientId),
        beginSession: new Date(form.beginSession).toISOString(),
        endSession: new Date(form.endSession).toISOString(),
        treatedArea: form.treatedArea,
        mode: form.mode,
        capacitiveIntensity: (isDual || isCapacitive) ? parseFloat(form.capacitiveIntensity) : null,
        resistiveIntensity: (isDual || !isCapacitive) ? parseFloat(form.resistiveIntensity) : null,
        objective: form.objective,
        physiotherapistId: physio.id,
        observations: form.observations,
      })
      navigate(-1)
    } catch {
      setError(t('indiba_error'))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form className={styles.page} onSubmit={handleSubmit}>

      <div>
        <h1 className={styles.heading}>{t('indiba_register_title')}</h1>
        <p className={styles.subtitle}>{t('indiba_register_subtitle')}</p>
      </div>

      {/* SESSION TIMING */}
      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faClock} /> {t('indiba_section_timing')}
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="beginSession">{t('indiba_begin')}</label>
            <input id="beginSession" name="beginSession" type="datetime-local"
              className={styles.input} value={form.beginSession} onChange={handleChange} required />
          </div>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="endSession">{t('indiba_end')}</label>
            <input id="endSession" name="endSession" type="datetime-local"
              className={styles.input} value={form.endSession} onChange={handleChange} required />
          </div>
        </div>
      </div>

      {/* TREATMENT PARAMETERS */}
      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faGear} /> {t('indiba_section_params')}
        </div>

        <div className={styles.field}>
          <label className={styles.label} htmlFor="patientId">{t('indiba_patient')}</label>
          <select id="patientId" name="patientId" className={styles.select}
            value={form.patientId} onChange={handleChange} required>
            <option value="">{t('indiba_patient_placeholder')}</option>
            {patients.map(p => (
              <option key={p.id} value={p.id}>
                {p.name} {p.surname}{p.secondSurname ? ` ${p.secondSurname}` : ''}
              </option>
            ))}
          </select>
        </div>

        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="treatedArea">{t('indiba_area')}</label>
            <input id="treatedArea" name="treatedArea" type="text"
              className={styles.input} placeholder={t('indiba_area_placeholder')}
              value={form.treatedArea} onChange={handleChange} required />
          </div>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="mode">{t('indiba_mode')}</label>
            <select id="mode" name="mode" className={styles.select}
              value={form.mode} onChange={handleChange} required>
              {MODES.map(m => (
                <option key={m} value={m}>
                  {m.charAt(0) + m.slice(1).toLowerCase()}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className={styles.row}>
          {form.mode === 'DUAL' ? (
            <>
              <div className={styles.fieldNarrow}>
                <label className={styles.label} htmlFor="capacitiveIntensity">{t('indiba_capacitive_intensity')}</label>
                <div className={styles.intensityWrapper}>
                  <input id="capacitiveIntensity" name="capacitiveIntensity" type="number" min="0" max="100"
                    className={styles.intensityInput} value={form.capacitiveIntensity} onChange={handleChange} required />
                  <span className={styles.intensityUnit}>%</span>
                </div>
              </div>
              <div className={styles.fieldNarrow}>
                <label className={styles.label} htmlFor="resistiveIntensity">{t('indiba_resistive_intensity')}</label>
                <div className={styles.intensityWrapper}>
                  <input id="resistiveIntensity" name="resistiveIntensity" type="number" min="0" max="100"
                    className={styles.intensityInput} value={form.resistiveIntensity} onChange={handleChange} required />
                  <span className={styles.intensityUnit}>%</span>
                </div>
              </div>
            </>
          ) : (
            <div className={styles.fieldNarrow}>
              <label className={styles.label} htmlFor={form.mode === 'CAPACITIVE' ? 'capacitiveIntensity' : 'resistiveIntensity'}>
                {form.mode === 'CAPACITIVE' ? t('indiba_capacitive_intensity') : t('indiba_resistive_intensity')}
              </label>
              <div className={styles.intensityWrapper}>
                <input
                  id={form.mode === 'CAPACITIVE' ? 'capacitiveIntensity' : 'resistiveIntensity'}
                  name={form.mode === 'CAPACITIVE' ? 'capacitiveIntensity' : 'resistiveIntensity'}
                  type="number" min="0" max="100"
                  className={styles.intensityInput}
                  value={form.mode === 'CAPACITIVE' ? form.capacitiveIntensity : form.resistiveIntensity}
                  onChange={handleChange} required />
                <span className={styles.intensityUnit}>%</span>
              </div>
            </div>
          )}
          <div className={styles.field}>
            <label className={styles.label}>{t('indiba_physio')}</label>
            <div className={styles.autoFillBox}>
              <span className={styles.autoFillName}>
                <FontAwesomeIcon icon={faShield} /> {physio ? `${physio.name} ${physio.surname}` : '…'}
              </span>
              <span className={styles.autoFillBadge}>{t('indiba_autofilled')}</span>
            </div>
          </div>
        </div>

        <div className={styles.field}>
          <label className={styles.label} htmlFor="objective">{t('indiba_objective')}</label>
          <input id="objective" name="objective" type="text"
            className={styles.input} placeholder={t('indiba_objective_placeholder')}
            value={form.objective} onChange={handleChange} />
        </div>
      </div>

      {/* CLINICAL OBSERVATIONS */}
      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faEye} /> {t('indiba_section_observations')}
        </div>
        <div className={styles.field}>
          <label className={styles.label} htmlFor="observations">{t('indiba_observations_label')}</label>
          <textarea id="observations" name="observations" className={styles.textarea}
            placeholder={t('indiba_observations_placeholder')}
            value={form.observations} onChange={handleChange} />
        </div>
      </div>

      {error && <p className={styles.error}>{error}</p>}

      <div className={styles.footer}>
        <button type="button" className={styles.discardBtn} onClick={() => navigate(-1)}>
          <FontAwesomeIcon icon={faTrash} /> {t('indiba_discard')}
        </button>
        <button type="submit" className={styles.submitBtn} disabled={submitting || !physio}>
          <FontAwesomeIcon icon={faCheck} /> {t('indiba_submit')}
        </button>
      </div>

    </form>
  )
}
