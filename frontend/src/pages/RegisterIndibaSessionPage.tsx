import { useEffect, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faClock, faGear, faShield, faEye, faTrash, faCheck } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
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
  intensity: '40',
  objective: '',
  observations: '',
}

export default function RegisterIndibaSessionPage() {
  const navigate = useNavigate()
  const { token } = useAuth()
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
      await createIndibaSession({
        patientId: Number(form.patientId),
        beginSession: new Date(form.beginSession).toISOString(),
        endSession: new Date(form.endSession).toISOString(),
        treatedArea: form.treatedArea,
        mode: form.mode,
        intensity: parseFloat(form.intensity),
        objective: form.objective,
        physiotherapistId: physio.id,
        observations: form.observations,
      })
      navigate(-1)
    } catch {
      setError('Failed to register session. Please check the fields and try again.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form className={styles.page} onSubmit={handleSubmit}>

      <div>
        <h1 className={styles.heading}>Register INDIBA Session</h1>
        <p className={styles.subtitle}>Document a new INDIBA treatment session with clinical parameters and observations.</p>
      </div>

      {/* SESSION TIMING */}
      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faClock} /> SESSION TIMING
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="beginSession">BEGIN SESSION</label>
            <input id="beginSession" name="beginSession" type="datetime-local"
              className={styles.input} value={form.beginSession} onChange={handleChange} required />
          </div>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="endSession">END SESSION</label>
            <input id="endSession" name="endSession" type="datetime-local"
              className={styles.input} value={form.endSession} onChange={handleChange} required />
          </div>
        </div>
      </div>

      {/* TREATMENT PARAMETERS */}
      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faGear} /> TREATMENT PARAMETERS
        </div>

        <div className={styles.field}>
          <label className={styles.label} htmlFor="patientId">PATIENT</label>
          <select id="patientId" name="patientId" className={styles.select}
            value={form.patientId} onChange={handleChange} required>
            <option value="">Search or select patient...</option>
            {patients.map(p => (
              <option key={p.id} value={p.id}>
                {p.name} {p.surname}{p.secondSurname ? ` ${p.secondSurname}` : ''}
              </option>
            ))}
          </select>
        </div>

        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="treatedArea">TREATED AREA</label>
            <input id="treatedArea" name="treatedArea" type="text"
              className={styles.input} placeholder="e.g. Lumbar spine, Right shoulder"
              value={form.treatedArea} onChange={handleChange} required />
          </div>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="mode">MODE</label>
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
          <div className={styles.fieldNarrow}>
            <label className={styles.label} htmlFor="intensity">INTENSITY</label>
            <div className={styles.intensityWrapper}>
              <input id="intensity" name="intensity" type="number" min="0" max="100"
                className={styles.intensityInput} value={form.intensity} onChange={handleChange} required />
              <span className={styles.intensityUnit}>%</span>
            </div>
          </div>
          <div className={styles.field}>
            <label className={styles.label}>ATTENDING PHYSIOTHERAPIST</label>
            <div className={styles.autoFillBox}>
              <span className={styles.autoFillName}>
                <FontAwesomeIcon icon={faShield} /> {physio ? `${physio.name} ${physio.surname}` : '…'}
              </span>
              <span className={styles.autoFillBadge}>AUTO-FILLED</span>
            </div>
          </div>
        </div>

        <div className={styles.field}>
          <label className={styles.label} htmlFor="objective">OBJECTIVE</label>
          <input id="objective" name="objective" type="text"
            className={styles.input} placeholder="State the primary goal for this specific session"
            value={form.objective} onChange={handleChange} />
        </div>
      </div>

      {/* CLINICAL OBSERVATIONS */}
      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faEye} /> CLINICAL OBSERVATIONS
        </div>
        <div className={styles.field}>
          <label className={styles.label} htmlFor="observations">OBSERVATIONS</label>
          <textarea id="observations" name="observations" className={styles.textarea}
            placeholder="Document patient feedback, tissue response, and post-session functional changes..."
            value={form.observations} onChange={handleChange} />
        </div>
      </div>

      {error && <p className={styles.error}>{error}</p>}

      <div className={styles.footer}>
        <button type="button" className={styles.discardBtn} onClick={() => navigate(-1)}>
          <FontAwesomeIcon icon={faTrash} /> DISCARD ENTRY
        </button>
        <button type="submit" className={styles.submitBtn} disabled={submitting || !physio}>
          <FontAwesomeIcon icon={faCheck} /> REGISTER SESSION
        </button>
      </div>

    </form>
  )
}
