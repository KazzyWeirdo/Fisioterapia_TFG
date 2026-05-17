import { useEffect, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMoon, faBed } from '@fortawesome/free-solid-svg-icons'
import { faHeart } from '@fortawesome/free-regular-svg-icons'
import { useParams, useNavigate } from 'react-router-dom'
import { getPniReport, type PniReport } from '../services/pniReportService'
import { getPatient, type PatientDetail } from '../services/patientService'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './PniReportDetailPage.module.css'
import TabBar from '../components/TabBar'
import Breadcrumb from '../components/Breadcrumb'

function formatDate(raw: string, locale: string): string {
  return new Date(raw + 'T00:00:00').toLocaleDateString(locale, {
    year: 'numeric', month: 'long', day: 'numeric',
  })
}

function formatHoursAsleep(hours: number): string {
  const h = Math.floor(hours)
  const m = Math.round((hours - h) * 60)
  return m > 0 ? `${h}h ${m}min` : `${h}h`
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
          <Breadcrumb items={[
            { label: t('nav_patients'), onClick: () => navigate('/patients') },
            { label: patientName, onClick: () => navigate(`/patients/${report.patientId}`) },
            { label: t('pni_breadcrumb_current') },
          ]} />
          <h1 className={styles.title}>{formatDate(report.reportDate, localeTag)}</h1>
          <p className={styles.subtitle}>
            {t('pni_assessment_subtitle')} {patientName}
          </p>
        </div>
      </div>

      {/* Tab bar */}
      <TabBar
        tabs={TABS.map(({ label, tab }) => ({ id: tab, label }))}
        activeTab="pni"
        onTabChange={tab => navigate(`/patients/${report.patientId}`, { state: { tab } })}
        ariaLabel="Patient sections"
      />

      {/* Top row */}
      <div className={styles.topRow}>

        {/* Restoration Profile card */}
        <div className={styles.restorationCard}>
          <span className={styles.restorationBadge}>{t('pni_restoration_profile')}</span>
          <div className={styles.arcWrap}>
            <div className={styles.arcScore}>
              <span className={styles.arcMain}>{report.continuity != null ? Number(report.continuity).toFixed(1) : '—'}</span>
              <span className={styles.arcMax}>/5</span>
            </div>
            <div className={styles.arcLabel}>{t('pni_continuity')}</div>
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
                {formatHoursAsleep(report.hours_asleep)}
              </div>
            </div>
          </div>

          <div className={styles.metricCard}>
            <div className={`${styles.metricIcon} ${styles.metricIconRose}`}><FontAwesomeIcon icon={faHeart} /></div>
            <div className={styles.metricBody}>
              <div className={styles.metricLabel}>{t('pni_avg_hr')}</div>
              <div className={styles.metricValue}>
                {report.avg_hr != null ? report.avg_hr.toFixed(1) : '—'}<span className={styles.metricUnit}>bpm</span>
              </div>
            </div>
          </div>

          <div className={styles.metricCard}>
            <div className={`${styles.metricIcon} ${styles.metricIconRose}`}><FontAwesomeIcon icon={faHeart} /></div>
            <div className={styles.metricBody}>
              <div className={styles.metricLabel}>{t('pni_min_hr')}</div>
              <div className={styles.metricValue}>
                {report.min_hr}<span className={styles.metricUnit}>bpm</span>
              </div>
            </div>
          </div>

        </div>
      </div>

      {/* Bottom row */}
      <div className={styles.bottomRow}>
        <div className={styles.metricCard}>
          <div className={`${styles.metricIcon} ${styles.metricIconBlue}`}><FontAwesomeIcon icon={faBed} /></div>
          <div className={styles.metricBody}>
            <div className={styles.metricLabel}>{t('pni_deep_sleep')}</div>
            <div className={styles.metricValue}>
              {report.deep_sleep}<span className={styles.metricUnit}>{t('pni_deep_sleep_unit')}</span>
            </div>
          </div>
        </div>
      </div>

    </div>
  )
}
