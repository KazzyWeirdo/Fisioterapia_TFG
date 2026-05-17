import { useEffect, useMemo, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faClipboardList } from '@fortawesome/free-solid-svg-icons'
import { getPniReportsFromPatient, syncPolarDataForPatient, type PniReportSummary } from '../../services/pniReportService'
import { useLanguage } from '../../contexts/LanguageContext'
import styles from './PniReportTab.module.css'

interface PniReportTabProps {
  patientId: number
  patientName: string
}

function formatDate(raw: string, locale: string): string {
  return new Date(raw + 'T00:00:00').toLocaleDateString(locale, {
    year: 'numeric', month: 'long', day: '2-digit',
  })
}

export default function PniReportTab({ patientId, patientName }: PniReportTabProps) {
  const { t, locale } = useLanguage()
  const localeTag = locale === 'es' ? 'es-ES' : 'en-US'
  const [reports, setReports] = useState<PniReportSummary[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [dateFrom, setDateFrom] = useState('')
  const [syncing, setSyncing] = useState(false)
  const [syncError, setSyncError] = useState<string | null>(null)
  const [refreshKey, setRefreshKey] = useState(0)

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
  }, [patientId, currentPage, refreshKey])

  const filtered = useMemo(() => reports.filter(r => {
    if (dateFrom && r.reportDate !== dateFrom) return false
    return true
  }), [reports, dateFrom])

  const pageNumbers = useMemo(() => Array.from({ length: totalPages }, (_, i) => i), [totalPages])

  async function handlePolarSync() {
    setSyncing(true)
    setSyncError(null)
    try {
      await syncPolarDataForPatient(patientId)
      setCurrentPage(0)
      setRefreshKey(k => k + 1)
    } catch {
      setSyncError(t('pni_sync_error'))
    } finally {
      setSyncing(false)
    }
  }

  function goToPage(page: number) {
    if (page < 0 || page >= totalPages) return
    setCurrentPage(page)
  }

  return (
    <div className={styles.tab}>
      <h2 className={styles.title}>{t('patient_tab_pni')}</h2>
      <p className={styles.subtitle}>
        {t('pni_tab_subtitle')} <strong>{patientName}</strong>.
      </p>

      <div className={styles.statCard}>
        <div className={styles.statIcon}><FontAwesomeIcon icon={faClipboardList} /></div>
        <div>
          <div className={styles.statLabel}>{t('pni_stat_total')}</div>
          <div className={styles.statValue}>{totalElements}</div>
        </div>
      </div>

      {syncError && (
        <div className="alert alert-danger alert-dismissible d-flex align-items-center py-2 px-3 mb-3" role="alert">
          <span className="me-2">{syncError}</span>
          <button
            type="button"
            className="btn-close ms-auto p-1"
            style={{ fontSize: '0.65rem' }}
            onClick={() => setSyncError(null)}
            aria-label="Dismiss"
          />
        </div>
      )}

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
        <div className={styles.controlsRight}>
          <button
            type="button"
            className="btn btn-success btn-sm"
            onClick={handlePolarSync}
            disabled={syncing}
            data-testid="polar-sync-btn"
          >
            {syncing && (
              <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true" />
            )}
            {syncing ? t('pni_syncing') : t('pni_sync_polar')}
          </button>
        </div>
      </div>

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>{t('pni_col_date')}</th>
              <th>{t('pni_col_actions')}</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr><td colSpan={2} className={styles.stateCell}>{t('common_loading')}</td></tr>
            )}
            {!loading && error && (
              <tr><td colSpan={2} className={styles.errorCell}>{error}</td></tr>
            )}
            {!loading && !error && filtered.length === 0 && (
              <tr><td colSpan={2} className={styles.stateCell}>{t('pni_reports_empty')}</td></tr>
            )}
            {!loading && !error && filtered.map(r => (
              <tr key={r.id}>
                <td>{formatDate(r.reportDate, localeTag)}</td>
                <td className={styles.actionsCell}>
                  <a href={`/pni/${r.id}`} className={styles.viewLink}>{t('common_view_details')}</a>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className={styles.footer}>
          <span className={styles.footerText}>
            {t('pni_tab_footer', { n: filtered.length, total: totalElements })}
          </span>
          <div className={styles.pagination}>
            <button
              type="button"
              className="btn btn-outline-secondary btn-sm"
              disabled={currentPage === 0}
              onClick={() => goToPage(currentPage - 1)}
              aria-label="Previous page"
            >‹</button>
            {pageNumbers.map(n => (
              <button
                key={n}
                type="button"
                className={n === currentPage ? 'btn btn-secondary btn-sm' : 'btn btn-outline-secondary btn-sm'}
                onClick={() => goToPage(n)}
                aria-label={`Page ${n + 1}`}
                aria-current={n === currentPage ? 'true' : undefined}
              >{n + 1}</button>
            ))}
            <button
              type="button"
              className="btn btn-outline-secondary btn-sm"
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
