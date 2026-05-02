import { useEffect, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMoon, faBolt } from '@fortawesome/free-solid-svg-icons'
import { faHeart } from '@fortawesome/free-regular-svg-icons'
import { useParams, useNavigate } from 'react-router-dom'
import { getPniReport, type PniReport } from '../services/pniReportService'
import { getPatient, type PatientDetail } from '../services/patientService'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './PniReportDetailPage.module.css'

function formatDate(raw: string, locale: string): string {
  return new Date(raw + 'T00:00:00').toLocaleDateString(locale, {
    year: 'numeric', month: 'long', day: 'numeric',
  })
}

function ansKey(value: number): 'pni_ans_optimal' | 'pni_ans_moderate' | 'pni_ans_low' {
  if (value >= 70) return 'pni_ans_optimal'
  if (value >= 40) return 'pni_ans_moderate'
  return 'pni_ans_low'
}

export default function PniReportDetailPage() {
  const { reportId } = useParams<{ reportId: string }>()
  const navigate = useNavigate()
  const { t, locale } = useLanguage()
  const localeTag = locale === 'es' ? 'es-ES' : 'en-US'
  const [report, setReport] = useState<PniReport | null>(null)
  const [patient, setPatient] = useState<PatientDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const TABS: { label: string; tab: string }[] = [
    { label: t('tab_overview'),         tab: 'overview'  },
    { label: t('patient_tab_training'), tab: 'training'  },
    { label: t('patient_tab_indiba'),   tab: 'indiba'    },
    { label: t('patient_tab_pni'),      tab: 'pni'       },
    { label: t('patient_tab_stats'),    tab: 'statistics' },
  ]

  useEffect(() => {
    if (!reportId) return
    getPniReport(Number(reportId))
      .then(r => {
        setReport(r)
        return getPatient(r.patientId)
      })
      .then(setPatient)
      .catch(() => setError(t('pni_load_error')))
      .finally(() => setLoading(false))
  }, [reportId])

  if (loading) return <p className={styles.status}>{t('common_loading')}</p>
  if (error || !report) return <p className={styles.error}>{error ?? t('pni_not_found')}</p>

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
              {t('nav_patients')}
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <button type="button" className={styles.breadcrumbLink} onClick={() => navigate(`/patients/${report.patientId}`)}>
              {patientName}
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <span className={styles.breadcrumbCurrent}>{t('pni_breadcrumb_current')}</span>
          </nav>
          <h1 className={styles.title}>{formatDate(report.reportDate, localeTag)}</h1>
          <p className={styles.subtitle}>
            {t('pni_assessment_subtitle')} {patientName}
          </p>
        </div>
      </div>

      {/* Tab bar */}
      <nav className={styles.tabBar} aria-label="Patient sections">
        {TABS.map(({ label, tab }) => (
          <button
            key={tab}
            type="button"
            className={`${styles.tab} ${tab === 'pni' ? styles.tabActive : ''}`}
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
          <span className={styles.restorationBadge}>{t('pni_restoration_profile')}</span>
          <div className={styles.arcWrap}>
            <div className={styles.arcScore}>
              <span className={styles.arcMain}>{report.ntrs}</span>
              <span className={styles.arcMax}>/100</span>
            </div>
            <div className={styles.arcLabel}>{t('pni_sleep_score')}</div>
          </div>
          <div className={styles.arcDecor} />
        </div>

        {/* Right metric cards */}
        <div className={styles.metricCol}>

          <div className={styles.metricCard}>
            <div className={`${styles.metricIcon} ${styles.metricIconBlue}`}><FontAwesomeIcon icon={faMoon} /></div>
            <div className={styles.metricBody}>
              <div className={styles.metricLabel}>{t('pni_hours_asleep')}</div>
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
            <div className={`${styles.metricIcon} ${styles.metricIconRose}`}><FontAwesomeIcon icon={faHeart} /></div>
            <div className={styles.metricBody}>
              <div className={styles.metricLabel}>{t('pni_hrv')}</div>
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
            <div className={styles.ansIcon}><FontAwesomeIcon icon={faBolt} /></div>
            <div>
              <div className={styles.ansTitle}>{t('pni_ans_charge')}</div>
            </div>
          </div>
          <div className={styles.gaugeWrap}>
            <div
              className={styles.gauge}
              style={{ '--gauge-pct': `${report.stress}%` } as React.CSSProperties}
            >
              <span className={styles.gaugeValue}>{report.stress}</span>
              <span className={styles.gaugeLabel}>{t(ansKey(report.stress))}</span>
            </div>
          </div>
        </div>
      </div>

    </div>
  )
}
