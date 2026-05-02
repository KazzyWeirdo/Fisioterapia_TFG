import { useEffect, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faChartBar, faBolt, faClock, faUser } from '@fortawesome/free-solid-svg-icons'
import { useParams, useNavigate } from 'react-router-dom'
import { getIndibaSession, type IndibaSession } from '../services/indibaService'
import { getPhysiotherapist, type PhysiotherapistSummary } from '../services/physiotherapistService'
import { getPatient, type PatientDetail } from '../services/patientService'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './IndibaDetailPage.module.css'

function formatHeaderDate(raw: string, locale: string): string {
  return new Date(raw).toLocaleDateString(locale, {
    year: 'numeric', month: 'long', day: 'numeric',
  })
}

function formatTime(raw: string, locale: string): string {
  return new Date(raw).toLocaleTimeString(locale, {
    hour: '2-digit', minute: '2-digit', hour12: true,
  })
}

function formatSessionId(id: number, beginSession: string): string {
  const year = new Date(beginSession).getFullYear()
  return `IND-${year}-${id}`
}

function computeDuration(begin: string, end: string): number {
  return Math.round((new Date(end).getTime() - new Date(begin).getTime()) / 60000)
}


function formatMode(mode: string): string {
  return mode.charAt(0).toUpperCase() + mode.slice(1).toLowerCase()
}

export default function IndibaDetailPage() {
  const { sessionId } = useParams<{ sessionId: string }>()
  const navigate = useNavigate()
  const { t, locale } = useLanguage()
  const localeTag = locale === 'es' ? 'es-ES' : 'en-US'
  const [session, setSession] = useState<IndibaSession | null>(null)
  const [physio, setPhysio] = useState<PhysiotherapistSummary | null>(null)
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
    if (!sessionId) return
    getIndibaSession(Number(sessionId))
      .then(s => {
        setSession(s)
        return Promise.all([
          getPhysiotherapist(s.physiotherapistId),
          getPatient(s.patiendId),
        ])
      })
      .then(([p, pat]) => { setPhysio(p); setPatient(pat) })
      .catch(() => setError(t('indiba_load_error')))
      .finally(() => setLoading(false))
  }, [sessionId])

  if (loading) return <p className={styles.status}>{t('common_loading')}</p>
  if (error || !session) return <p className={styles.error}>{error ?? t('indiba_not_found')}</p>

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
            <button type="button" className={styles.breadcrumbLink} onClick={() => navigate(`/patients/${session.patiendId}`)}>
              {patient
                ? [patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')
                : `Patient #${session.patiendId}`}
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <span className={styles.breadcrumbCurrent}>{t('indiba_breadcrumb_current')}</span>
          </nav>
          <h1 className={styles.title}>{formatHeaderDate(session.beginSession, localeTag)}</h1>
          <p className={styles.sessionId}>{t('indiba_session_id_label')} {formatSessionId(session.id, session.beginSession)}</p>
        </div>
      </div>

      {/* Tab bar */}
      <nav className={styles.tabBar} aria-label="Patient sections">
        {TABS.map(({ label, tab }) => (
          <button
            key={tab}
            type="button"
            className={`${styles.tab} ${tab === 'indiba' ? styles.tabActive : ''}`}
            onClick={() => navigate(`/patients/${session.patiendId}`, { state: { tab } })}
          >
            {label}
          </button>
        ))}
      </nav>
      <div className={styles.tabSeparator} />

      {/* Body */}
      <div className={styles.body}>

        {/* Left — Treatment card */}
        <div className={styles.mainCard}>
          <h2 className={styles.cardTitle}>
            <FontAwesomeIcon icon={faChartBar} className={styles.cardIcon} /> {t('indiba_treatment_protocol')}
          </h2>

          <div className={styles.fieldRow}>
            <div className={styles.field}>
              <span className={styles.fieldLabel}>{t('indiba_field_treated_area')}</span>
              <span className={styles.fieldValue}>{session.treatedArea}</span>
            </div>
            <div className={styles.field}>
              <span className={styles.fieldLabel}>{t('indiba_field_objective')}</span>
              <span className={styles.fieldValue}>{session.objective}</span>
            </div>
          </div>

          <div className={styles.miniCards}>
            <div className={styles.miniCard}>
              <span className={styles.miniLabel}>{t('indiba_field_mode')}</span>
              <span className={styles.miniValue}><FontAwesomeIcon icon={faBolt} /> {formatMode(session.mode)}</span>
            </div>
            <div className={styles.miniCard}>
              <span className={styles.miniLabel}>{t('indiba_field_intensity')}</span>
              <span className={styles.miniValueLarge}>
                {session.intensity}<span className={styles.pct}>%</span>
              </span>
            </div>
          </div>

          <div className={styles.observations}>
            <span className={styles.fieldLabel}>{t('indiba_field_observations')}</span>
            <blockquote className={styles.observationsText}>
              {session.observations ? `"${session.observations}"` : t('indiba_no_observation')}
            </blockquote>
          </div>
        </div>

        {/* Right — Sidebar */}
        <div className={styles.sidebar}>

          <div className={styles.sideCard}>
            <h3 className={styles.sideCardTitle}><FontAwesomeIcon icon={faClock} /> {t('indiba_timeline_title')}</h3>
            <div className={styles.timeline}>
              <div className={styles.timelineItem}>
                <div className={styles.timelineDotActive} />
                <div>
                  <div className={styles.timelineLabel}>{t('indiba_timeline_start')}</div>
                  <div className={styles.timelineTime}>{formatTime(session.beginSession, localeTag)}</div>
                </div>
              </div>
              <div className={styles.timelineItem}>
                <div className={styles.timelineDot} />
                <div>
                  <div className={styles.timelineLabel}>{t('indiba_timeline_end')}</div>
                  <div className={styles.timelineTime}>{formatTime(session.endSession, localeTag)}</div>
                </div>
              </div>
            </div>
            <div className={styles.durationRow}>
              <span className={styles.durationLabel}>{t('indiba_duration_label')}</span>
              <span className={styles.durationBadge}>
                {computeDuration(session.beginSession, session.endSession)} {t('indiba_duration_unit')}
              </span>
            </div>
          </div>

          <div className={styles.sideCard}>
            <h3 className={styles.sideCardTitle}>{t('indiba_physio_title')}</h3>
            <div className={styles.physioRow}>
              <div className={styles.physioAvatar}><FontAwesomeIcon icon={faUser} /></div>
              <div>
                <div className={styles.physioName}>
                  {physio ? `${physio.name} ${physio.surname}` : `Physiotherapist #${session.physiotherapistId}`}
                </div>
                <div className={styles.physioRole}>{t('indiba_physio_role')}</div>
              </div>
            </div>
          </div>

        </div>
      </div>

      {/* Footer */}
      <footer className={styles.footer}>
        <div className={styles.footerLeft}>
        </div>
      </footer>

    </div>
  )
}
