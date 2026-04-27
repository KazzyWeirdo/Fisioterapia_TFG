import { useEffect, useMemo, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faDumbbell } from '@fortawesome/free-solid-svg-icons'
import { useParams, useNavigate } from 'react-router-dom'
import { getTrainingSession, type TrainingSessionDetail } from '../services/trainingSessionService'
import { getPatient, type PatientDetail } from '../services/patientService'
import styles from './TrainingSessionDetailPage.module.css'

const TABS = [
  { label: 'Overview',          tab: 'overview'   },
  { label: 'Training Sessions', tab: 'training'   },
  { label: 'INDIBA Sessions',   tab: 'indiba'     },
  { label: 'PNI Reports',       tab: 'pni'        },
  { label: 'Statistics',        tab: 'statistics' },
]

function formatDate(raw: string): string {
  return new Date(raw + 'T00:00:00').toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: 'numeric',
  })
}

function rpeBadgeStyle(rpe: number): { bg: string; color: string } {
  if (rpe <= 6) return { bg: '#dcfce7', color: '#166534' }
  if (rpe <= 8) return { bg: '#fef9c3', color: '#854d0e' }
  if (rpe === 9) return { bg: '#ffedd5', color: '#9a3412' }
  return { bg: '#fee2e2', color: '#b91c1c' }
}

export default function TrainingSessionDetailPage() {
  const { sessionId } = useParams<{ sessionId: string }>()
  const navigate = useNavigate()
  const [session, setSession] = useState<TrainingSessionDetail | null>(null)
  const [patient, setPatient] = useState<PatientDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!sessionId) return
    getTrainingSession(Number(sessionId))
      .then(s => {
        setSession(s)
        return getPatient(s.patientId)
      })
      .then(setPatient)
      .catch(() => setError('Failed to load session'))
      .finally(() => setLoading(false))
  }, [sessionId])

  const { totalVolume, avgRpe } = useMemo(() => {
    if (!session) return { totalVolume: 0, avgRpe: null }
    const allSets = session.exercises.flatMap(e => e.sets)
    const vol = allSets.reduce((sum, s) => sum + s.reps * s.weightKg, 0)
    const rpeValues = allSets.map(s => s.rpe)
    const avg = rpeValues.length > 0
      ? rpeValues.reduce((a, b) => a + b, 0) / rpeValues.length
      : null
    return { totalVolume: vol, avgRpe: avg }
  }, [session])

  const patientName = patient
    ? [patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')
    : `Patient #${session?.patientId}`

  if (loading) return <p className={styles.status}>Loading…</p>
  if (error || !session) return <p className={styles.error}>{error ?? 'Session not found'}</p>

  return (
    <div className={styles.page}>

      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <nav className={styles.breadcrumb}>
            <button type="button" className={styles.breadcrumbLink} onClick={() => navigate('/patients')}>
              Patients
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <button
              type="button"
              className={styles.breadcrumbLink}
              onClick={() => navigate(`/patients/${session.patientId}`, { state: { tab: 'training' } })}
            >
              {patientName}
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <span className={styles.breadcrumbCurrent}>Training Sessions</span>
          </nav>
          <h1 className={styles.title}>{formatDate(session.date)}</h1>
          <p className={styles.subtitle}>{patientName}</p>
        </div>
      </div>

      <nav className={styles.tabBar} aria-label="Patient sections">
        {TABS.map(({ label, tab }) => (
          <button
            key={tab}
            type="button"
            className={`${styles.tab} ${label === 'Training Sessions' ? styles.tabActive : ''}`}
            onClick={() => navigate(`/patients/${session.patientId}`, { state: { tab } })}
          >
            {label}
          </button>
        ))}
      </nav>
      <div className={styles.tabSeparator} />

      <div className={styles.statsCard}>
        <div className={styles.stat}>
          <span className={styles.statLabel}>VOLUME</span>
          <span className={styles.statValue}>
            {totalVolume.toLocaleString()} <span className={styles.statUnit}>kg</span>
          </span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>AVG RPE</span>
          <span className={styles.statValue}>{avgRpe !== null ? avgRpe.toFixed(1) : '—'}</span>
        </div>
      </div>

      {session.exercises.map(exercise => (
        <div key={exercise.id} className={styles.exerciseCard}>
          <div className={styles.exerciseHeader}>
            <div className={styles.exerciseName}>
              <FontAwesomeIcon icon={faDumbbell} className={styles.exerciseIcon} />
              <span>{exercise.name}</span>
            </div>
          </div>

          <table className={styles.setsTable}>
            <thead>
              <tr>
                <th>SET</th>
                <th>WEIGHT (KG)</th>
                <th>REPS</th>
                <th>REST (S)</th>
                <th>RPE</th>
                <th>PROGRESS</th>
              </tr>
            </thead>
            <tbody>
              {exercise.sets.map(set => {
                const badge = rpeBadgeStyle(set.rpe)
                return (
                  <tr key={set.setNumber}>
                    <td className={styles.setNum}>{set.setNumber}</td>
                    <td>{set.weightKg.toFixed(1)}</td>
                    <td>{set.reps}</td>
                    <td>{set.restTimeSeconds}</td>
                    <td>
                      <span
                        className={styles.rpeBadge}
                        style={{ backgroundColor: badge.bg, color: badge.color }}
                      >
                        {set.rpe}
                      </span>
                    </td>
                    <td className={styles.progressCell}>—</td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      ))}

    </div>
  )
}
