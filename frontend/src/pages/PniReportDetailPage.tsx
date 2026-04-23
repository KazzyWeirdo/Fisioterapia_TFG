import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getPniReport, type PniReport } from '../services/pniReportService'
import { getPatient, type PatientDetail } from '../services/patientService'
import styles from './PniReportDetailPage.module.css'

const TABS: { label: string; tab: string }[] = [
  { label: 'Overview',          tab: 'overview'  },
  { label: 'Training Sessions', tab: 'training'  },
  { label: 'INDIBA Sessions',   tab: 'indiba'    },
  { label: 'PNI Reports',       tab: 'pni'       },
  { label: 'Statistics',        tab: 'statistics' },
]

function formatDate(raw: string): string {
  return new Date(raw + 'T00:00:00').toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: 'numeric',
  })
}

function ansLabel(value: number): string {
  if (value >= 70) return 'OPTIMAL'
  if (value >= 40) return 'MODERATE'
  return 'LOW'
}

export default function PniReportDetailPage() {
  const { reportId } = useParams<{ reportId: string }>()
  const navigate = useNavigate()
  const [report, setReport] = useState<PniReport | null>(null)
  const [patient, setPatient] = useState<PatientDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!reportId) return
    getPniReport(Number(reportId))
      .then(r => {
        setReport(r)
        return getPatient(r.patientId)
      })
      .then(setPatient)
      .catch(() => setError('Failed to load report'))
      .finally(() => setLoading(false))
  }, [reportId])

  if (loading) return <p className={styles.status}>Loading…</p>
  if (error || !report) return <p className={styles.error}>{error ?? 'Report not found'}</p>

  const patientName = patient
    ? [patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')
    : `Patient #${report.patientId}`

  return (
    <div className={styles.page}>

      {/* Header */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <nav className={styles.breadcrumb}>
            <button type="button" className={styles.breadcrumbLink} onClick={() => navigate('/patients')}>
              Patients
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <button type="button" className={styles.breadcrumbLink} onClick={() => navigate(`/patients/${report.patientId}`)}>
              {patientName}
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <span className={styles.breadcrumbCurrent}>PNI Reports</span>
          </nav>
          <h1 className={styles.title}>{formatDate(report.reportDate)}</h1>
          <p className={styles.subtitle}>
            Psychoneuroimmunology Comprehensive Assessment for {patientName}
          </p>
        </div>
      </div>

      {/* Tab bar */}
      <nav className={styles.tabBar} aria-label="Patient sections">
        {TABS.map(({ label, tab }) => (
          <button
            key={tab}
            type="button"
            className={`${styles.tab} ${label === 'PNI Reports' ? styles.tabActive : ''}`}
            onClick={() => navigate(`/patients/${report.patientId}`, { state: { tab } })}
          >
            {label}
          </button>
        ))}
      </nav>
      <div className={styles.tabSeparator} />

      {/* Top row */}
      <div className={styles.topRow}>

        {/* Restoration Profile card */}
        <div className={styles.restorationCard}>
          <span className={styles.restorationBadge}>RESTORATION PROFILE</span>
          <div className={styles.arcWrap}>
            <div className={styles.arcScore}>
              <span className={styles.arcMain}>{report.ntrs}</span>
              <span className={styles.arcMax}>/100</span>
            </div>
            <div className={styles.arcLabel}>Sleep Score</div>
          </div>
          <div className={styles.arcDecor} />
        </div>

        {/* Right metric cards */}
        <div className={styles.metricCol}>

          <div className={styles.metricCard}>
            <div className={`${styles.metricIcon} ${styles.metricIconBlue}`}>🌙</div>
            <div className={styles.metricBody}>
              <div className={styles.metricLabel}>HOURS ASLEEP</div>
              <div className={styles.metricValue}>
                {report.hours_asleep}<span className={styles.metricUnit}>h</span>
              </div>
              <div className={styles.progressBar}>
                <div
                  className={styles.progressFill}
                  style={{ width: `${Math.min((report.hours_asleep / 10) * 100, 100)}%` }}
                />
              </div>
            </div>
          </div>

          <div className={styles.metricCard}>
            <div className={`${styles.metricIcon} ${styles.metricIconRose}`}>🤍</div>
            <div className={styles.metricBody}>
              <div className={styles.metricLabel}>HRV (AVERAGE)</div>
              <div className={styles.metricValue}>
                {report.hrv}<span className={styles.metricUnit}>ms</span>
              </div>
            </div>
          </div>

        </div>
      </div>

      {/* Bottom row */}
      <div className={styles.bottomRow}>
        <div className={styles.ansCard}>
          <div className={styles.ansHeader}>
            <div className={styles.ansIcon}>⚡</div>
            <div>
              <div className={styles.ansTitle}>ANS Charge</div>
            </div>
          </div>
          <div className={styles.gaugeWrap}>
            <div
              className={styles.gauge}
              style={{ '--gauge-pct': `${report.stress}%` } as React.CSSProperties}
            >
              <span className={styles.gaugeValue}>{report.stress}</span>
              <span className={styles.gaugeLabel}>{ansLabel(report.stress)}</span>
            </div>
          </div>
        </div>
      </div>

    </div>
  )
}
