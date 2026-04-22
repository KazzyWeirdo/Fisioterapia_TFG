import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getIndibaSession, type IndibaSession } from '../services/indibaService'
import { getPhysiotherapist, type PhysiotherapistSummary } from '../services/physiotherapistService'
import styles from './IndibaDetailPage.module.css'

function formatHeaderDate(raw: string): string {
  return new Date(raw).toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: 'numeric',
  })
}

function formatTime(raw: string): string {
  return new Date(raw).toLocaleTimeString('en-US', {
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

function formatSyncDate(raw: string): string {
  return new Date(raw).toLocaleDateString('en-US', {
    day: '2-digit', month: 'short', year: 'numeric',
  }) + ' at ' + new Date(raw).toLocaleTimeString('en-US', {
    hour: '2-digit', minute: '2-digit', hour12: true,
  }) + ' CET'
}

function formatMode(mode: string): string {
  return mode.charAt(0).toUpperCase() + mode.slice(1).toLowerCase()
}

const TABS = ['Overview', 'Training Sessions', 'INDIBA Sessions', 'PNI Reports', 'Statistics', 'Audit Logs']

export default function IndibaDetailPage() {
  const { sessionId } = useParams<{ sessionId: string }>()
  const navigate = useNavigate()
  const [session, setSession] = useState<IndibaSession | null>(null)
  const [physio, setPhysio] = useState<PhysiotherapistSummary | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!sessionId) return
    getIndibaSession(Number(sessionId))
      .then(s => {
        setSession(s)
        return getPhysiotherapist(s.physiotherapistId)
      })
      .then(setPhysio)
      .catch(() => setError('Failed to load session'))
      .finally(() => setLoading(false))
  }, [sessionId])

  if (loading) return <p className={styles.status}>Loading…</p>
  if (error || !session) return <p className={styles.error}>{error ?? 'Session not found'}</p>

  return (
    <div className={styles.page}>

      {/* Header */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <nav className={styles.breadcrumb}>
            <button type="button" onClick={() => navigate(-1)} className={styles.breadcrumbLink}>
              Patients
            </button>
            <span className={styles.breadcrumbSep}>›</span>
            <span className={styles.breadcrumbCurrent}>INDIBA Session</span>
          </nav>
          <h1 className={styles.title}>{formatHeaderDate(session.beginSession)}</h1>
          <p className={styles.sessionId}>Session ID: {formatSessionId(session.id, session.beginSession)}</p>
        </div>
      </div>

      {/* Tab bar — visual only */}
      <nav className={styles.tabBar} aria-label="Patient sections">
        {TABS.map(tab => (
          <span
            key={tab}
            className={`${styles.tab} ${tab === 'INDIBA Sessions' ? styles.tabActive : ''}`}
          >
            {tab}
          </span>
        ))}
      </nav>
      <div className={styles.tabSeparator} />

      {/* Body */}
      <div className={styles.body}>

        {/* Left — Treatment card */}
        <div className={styles.mainCard}>
          <h2 className={styles.cardTitle}>
            <span className={styles.cardIcon}>📊</span> Treatment Protocol
          </h2>

          <div className={styles.fieldRow}>
            <div className={styles.field}>
              <span className={styles.fieldLabel}>TREATED AREA</span>
              <span className={styles.fieldValue}>{session.treatedArea}</span>
            </div>
            <div className={styles.field}>
              <span className={styles.fieldLabel}>OBJECTIVE</span>
              <span className={styles.fieldValue}>{session.objective}</span>
            </div>
          </div>

          <div className={styles.miniCards}>
            <div className={styles.miniCard}>
              <span className={styles.miniLabel}>MODE</span>
              <span className={styles.miniValue}>⚡ {formatMode(session.mode)}</span>
            </div>
            <div className={styles.miniCard}>
              <span className={styles.miniLabel}>INTENSITY</span>
              <span className={styles.miniValueLarge}>
                {session.intensity}<span className={styles.pct}>%</span>
              </span>
            </div>
          </div>

          <div className={styles.observations}>
            <span className={styles.fieldLabel}>OBSERVATIONS</span>
            <blockquote className={styles.observationsText}>
              "{session.observations}"
            </blockquote>
          </div>
        </div>

        {/* Right — Sidebar */}
        <div className={styles.sidebar}>

          <div className={styles.sideCard}>
            <h3 className={styles.sideCardTitle}>🕐 SESSION TIMELINE</h3>
            <div className={styles.timeline}>
              <div className={styles.timelineItem}>
                <div className={styles.timelineDotActive} />
                <div>
                  <div className={styles.timelineLabel}>Session Start</div>
                  <div className={styles.timelineTime}>{formatTime(session.beginSession)}</div>
                </div>
              </div>
              <div className={styles.timelineItem}>
                <div className={styles.timelineDot} />
                <div>
                  <div className={styles.timelineLabel}>Session End</div>
                  <div className={styles.timelineTime}>{formatTime(session.endSession)}</div>
                </div>
              </div>
            </div>
            <div className={styles.durationRow}>
              <span className={styles.durationLabel}>Duration</span>
              <span className={styles.durationBadge}>
                {computeDuration(session.beginSession, session.endSession)} Minutes
              </span>
            </div>
          </div>

          <div className={styles.sideCard}>
            <h3 className={styles.sideCardTitle}>ATTENDING PHYSIOTHERAPIST</h3>
            <div className={styles.physioRow}>
              <div className={styles.physioAvatar}>👤</div>
              <div>
                <div className={styles.physioName}>
                  {physio ? `${physio.name} ${physio.surname}` : `Physiotherapist #${session.physiotherapistId}`}
                </div>
                <div className={styles.physioRole}>Clinical Specialist</div>
              </div>
            </div>
          </div>

        </div>
      </div>

      {/* Footer */}
      <footer className={styles.footer}>
        <div className={styles.footerLeft}>
        </div>
        <span className={styles.footerSync}>Last synced: {formatSyncDate(session.beginSession)}</span>
      </footer>

    </div>
  )
}
