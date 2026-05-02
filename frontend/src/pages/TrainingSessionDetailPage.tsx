import { useEffect, useMemo, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faDumbbell } from '@fortawesome/free-solid-svg-icons'
import { useParams, useNavigate } from 'react-router-dom'
import { getTrainingSession, type TrainingSessionDetail } from '../services/trainingSessionService'
import { getPatient, type PatientDetail } from '../services/patientService'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './TrainingSessionDetailPage.module.css'

function formatDate(raw: string, locale: string): string {
  return new Date(raw + 'T00:00:00').toLocaleDateString(locale, {
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
  const { t, locale } = useLanguage()
  const localeTag = locale === 'es' ? 'es-ES' : 'en-US'
  const [session, setSession] = useState<TrainingSessionDetail | null>(null)
  const [patient, setPatient] = useState<PatientDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const TABS = [
    { label: t('tab_overview'),         tab: 'overview'   },
    { label: t('patient_tab_training'), tab: 'training'   },
    { label: t('patient_tab_indiba'),   tab: 'indiba'     },
    { label: t('patient_tab_pni'),      tab: 'pni'        },
    { label: t('patient_tab_stats'),    tab: 'statistics' },
  ]

  useEffect(() => {
    if (!sessionId) return
    getTrainingSession(Number(sessionId))
      .then(s => {
        setSession(s)
        return getPatient(s.patientId)
      })
      .then(setPatient)
      .catch(() => setError(t('training_load_error')))
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

  if (loading) return <p className={styles.status}>{t('common_loading')}</p>
  if (error || !session) return <p className={styles.error}>{error ?? t('training_not_found')}</p>

  return (
    <div className={styles.page}>

      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <nav className={styles.breadcrumb}>
            <button type="button" className={styles.breadcrumbLink} onClick={() => navigate('/patients')}>
              {t('nav_patients')}
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
            <span className={styles.breadcrumbCurrent}>{t('training_breadcrumb_current')}</span>
          </nav>
          <h1 className={styles.title}>{formatDate(session.date, localeTag)}</h1>
          <p className={styles.subtitle}>{patientName}</p>
        </div>
      </div>

      <nav className={styles.tabBar} aria-label="Patient sections">
        {TABS.map(({ label, tab }) => (
          <button
            key={tab}
            type="button"
            className={`${styles.tab} ${tab === 'training' ? styles.tabActive : ''}`}
            onClick={() => navigate(`/patients/${session.patientId}`, { state: { tab } })}
          >
            {label}
          </button>
        ))}
      </nav>
      <div className={styles.tabSeparator} />

      <div className={styles.statsCard}>
        <div className={styles.stat}>
          <span className={styles.statLabel}>{t('training_stat_volume')}</span>
          <span className={styles.statValue}>
            {totalVolume.toLocaleString()} <span className={styles.statUnit}>kg</span>
          </span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>{t('training_stat_avg_rpe')}</span>
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
                <th>{t('training_col_set')}</th>
                <th>{t('training_col_weight')}</th>
                <th>{t('training_col_reps')}</th>
                <th>{t('training_col_rest')}</th>
                <th>{t('training_col_rpe')}</th>
                <th>{t('training_col_progress')}</th>
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
