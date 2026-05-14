import { useEffect, useMemo, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBolt } from '@fortawesome/free-solid-svg-icons'
import { getIndibaSessionsFromPatient, type IndibaSessionSummary } from '../../services/indibaService'
import { useLanguage } from '../../contexts/LanguageContext'
import styles from './IndibaSessionTab.module.css'

interface IndibaSessionTabProps {
  patientId: number
  patientName: string
}

function formatDate(raw: string, locale: string): string {
  return new Date(raw).toLocaleDateString(locale, {
    year: 'numeric', month: 'long', day: '2-digit',
  })
}

function formatTime(raw: string, locale: string): string {
  return new Date(raw).toLocaleTimeString(locale, {
    hour: '2-digit', minute: '2-digit', hour12: false,
  })
}

export default function IndibaSessionTab({ patientId, patientName }: IndibaSessionTabProps) {
  const { t, locale } = useLanguage()
  const localeTag = locale === 'es' ? 'es-ES' : 'en-US'
  const [sessions, setSessions] = useState<IndibaSessionSummary[]>([])
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
    getIndibaSessionsFromPatient(patientId, currentPage, 10)
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
      <h2 className={styles.title}>{t('patient_tab_indiba')}</h2>
      <p className={styles.subtitle}>
        {t('indiba_tab_subtitle')} <strong>{patientName}</strong>.
      </p>

      <div className={styles.statCard}>
        <div className={styles.statIcon}><FontAwesomeIcon icon={faBolt} /></div>
        <div>
          <div className={styles.statLabel}>{t('indiba_stat_total')}</div>
          <div className={styles.statValue}>{totalElements}</div>
        </div>
      </div>

      <div className={styles.controls}>
        <label className={styles.dateLabel}>
          {t('common_filter_by_date')}
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
              <th>{t('indiba_col_date')}</th>
              <th>{t('indiba_col_time')}</th>
              <th>{t('indiba_col_actions')}</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr><td colSpan={3} className={styles.stateCell}>{t('common_loading')}</td></tr>
            )}
            {!loading && error && (
              <tr><td colSpan={3} className={styles.errorCell}>{error}</td></tr>
            )}
            {!loading && !error && filtered.length === 0 && (
              <tr><td colSpan={3} className={styles.stateCell}>{t('indiba_empty')}</td></tr>
            )}
            {!loading && !error && filtered.map(s => (
              <tr key={s.id}>
                <td>{formatDate(s.date, localeTag)}</td>
                <td>{formatTime(s.date, localeTag)}</td>
                <td className={styles.actionsCell}>
                  <a href={`/indiba/${s.id}`} className={styles.viewLink}>{t('common_view_details')}</a>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className={styles.footer}>
          <span className={styles.footerText}>
            {t('indiba_tab_footer', { n: filtered.length, total: totalElements })}
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
