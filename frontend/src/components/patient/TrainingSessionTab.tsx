import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faDumbbell } from '@fortawesome/free-solid-svg-icons'
import { getTrainingSessionsFromPatient } from '../../services/trainingSessionService'
import { useLanguage } from '../../contexts/LanguageContext'
import styles from './TrainingSessionTab.module.css'

interface TrainingSessionTabProps {
  patientId: number
  patientName: string
}

function formatDate(raw: string): string {
  return new Date(raw).toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: '2-digit',
  })
}

export default function TrainingSessionTab({ patientId, patientName }: TrainingSessionTabProps) {
  const navigate = useNavigate()
  const { t } = useLanguage()
  const [sessions, setSessions] = useState<{ id: number; date: string }[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [dateFrom, setDateFrom] = useState('')

  useEffect(() => {
    let cancelled = false
    setLoading(true)
    setError(null)
    getTrainingSessionsFromPatient(patientId, currentPage, 10)
      .then(data => {
        if (cancelled) return
        setSessions(data.content)
        setTotalElements(data.totalElements)
        setTotalPages(data.totalPages)
      })
      .catch(() => { if (!cancelled) setError(t('common_error')) })
      .finally(() => { if (!cancelled) setLoading(false) })
    return () => { cancelled = true }
  }, [patientId, currentPage])

  const filtered = useMemo(() => sessions.filter(s => {
    if (dateFrom && s.date.slice(0, 10) !== dateFrom) return false
    return true
  }), [sessions, dateFrom])

  const pageNumbers = useMemo(() => Array.from({ length: totalPages }, (_, i) => i), [totalPages])

  function goToPage(page: number) {
    if (page < 0 || page >= totalPages) return
    setCurrentPage(page)
  }

  return (
    <div className={styles.tab}>
      <h2 className={styles.title}>{t('patient_tab_training')}</h2>
      <p className={styles.subtitle}>
        Training history for patient <strong>{patientName}</strong>.
      </p>

      <div className={styles.statCard}>
        <div className={styles.statIcon}><FontAwesomeIcon icon={faDumbbell} /></div>
        <div>
          <div className={styles.statLabel}>TOTAL SESSIONS</div>
          <div className={styles.statValue}>{totalElements}</div>
        </div>
      </div>

      <div className={styles.controls}>
        <label className={styles.dateLabel}>
          Filter by date
          <input
            type="date"
            className={styles.dateInput}
            value={dateFrom}
            onChange={e => setDateFrom(e.target.value)}
          />
        </label>
      </div>

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>SESSION DATE</th>
              <th>ACTIONS</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr><td colSpan={2} className={styles.stateCell}>Loading…</td></tr>
            )}
            {!loading && error && (
              <tr><td colSpan={2} className={styles.errorCell}>{error}</td></tr>
            )}
            {!loading && !error && filtered.length === 0 && (
              <tr><td colSpan={2} className={styles.stateCell}>No sessions found</td></tr>
            )}
            {!loading && !error && filtered.map(s => (
              <tr key={s.id}>
                <td>{formatDate(s.date)}</td>
                <td className={styles.actionsCell}>
                  <button
                    type="button"
                    className={styles.viewLink}
                    onClick={() => navigate(`/training-session/${s.id}`)}
                  >
                    View Details ›
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className={styles.footer}>
          <span className={styles.footerText}>
            Showing {filtered.length} of {totalElements} sessions
          </span>
          <div className={styles.pagination}>
            <button
              type="button"
              className={styles.pageBtn}
              disabled={currentPage === 0}
              onClick={() => goToPage(currentPage - 1)}
              aria-label="Previous page"
            >‹</button>
            {pageNumbers.map(n => (
              <button
                key={n}
                type="button"
                className={`${styles.pageBtn} ${n === currentPage ? styles.pageBtnActive : ''}`}
                onClick={() => goToPage(n)}
                aria-label={`Page ${n + 1}`}
                aria-current={n === currentPage ? 'true' : undefined}
              >{n + 1}</button>
            ))}
            <button
              type="button"
              className={styles.pageBtn}
              disabled={currentPage >= totalPages - 1}
              onClick={() => goToPage(currentPage + 1)}
              aria-label="Next page"
            >›</button>
          </div>
        </div>
      </div>
    </div>
  )
}
