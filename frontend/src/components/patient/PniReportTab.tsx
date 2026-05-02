import { useEffect, useMemo, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faClipboardList } from '@fortawesome/free-solid-svg-icons'
import { getPniReportsFromPatient, type PniReportSummary } from '../../services/pniReportService'
import { useLanguage } from '../../contexts/LanguageContext'
import styles from './PniReportTab.module.css'

interface PniReportTabProps {
  patientId: number
  patientName: string
}

function formatDate(raw: string): string {
  return new Date(raw + 'T00:00:00').toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: '2-digit',
  })
}

export default function PniReportTab({ patientId, patientName }: PniReportTabProps) {
  const { t } = useLanguage()
  const [reports, setReports] = useState<PniReportSummary[]>([])
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
    getPniReportsFromPatient(patientId, currentPage, 10)
      .then(data => {
        if (cancelled) return
        setReports(data.content)
        setTotalElements(data.totalElements)
        setTotalPages(data.totalPages)
      })
      .catch(() => { if (!cancelled) setError(t('common_error')) })
      .finally(() => { if (!cancelled) setLoading(false) })
    return () => { cancelled = true }
  }, [patientId, currentPage])

  const filtered = useMemo(() => reports.filter(r => {
    if (dateFrom && r.reportDate !== dateFrom) return false
    return true
  }), [reports, dateFrom])

  const pageNumbers = useMemo(() => Array.from({ length: totalPages }, (_, i) => i), [totalPages])

  function goToPage(page: number) {
    if (page < 0 || page >= totalPages) return
    setCurrentPage(page)
  }

  return (
    <div className={styles.tab}>
      <h2 className={styles.title}>{t('patient_tab_pni')}</h2>
      <p className={styles.subtitle}>
        Diagnostic history for patient <strong>{patientName}</strong>.
      </p>

      <div className={styles.statCard}>
        <div className={styles.statIcon}><FontAwesomeIcon icon={faClipboardList} /></div>
        <div>
          <div className={styles.statLabel}>TOTAL REPORTS</div>
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
              <th>ANALYSIS DATE</th>
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
              <tr><td colSpan={2} className={styles.stateCell}>No reports found</td></tr>
            )}
            {!loading && !error && filtered.map(r => (
              <tr key={r.id}>
                <td>{formatDate(r.reportDate)}</td>
                <td className={styles.actionsCell}>
                  <a href={`/pni/${r.id}`} className={styles.viewLink}>View Details ›</a>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className={styles.footer}>
          <span className={styles.footerText}>
            Showing {filtered.length} of {totalElements} reports
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
