import { useEffect, useMemo, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faClipboardList } from '@fortawesome/free-solid-svg-icons'
import { getAuditLogs, type AuditLogSummary } from '../services/auditLogService'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './AuditLogPage.module.css'

const PAGE_SIZE = 10

const ACTION_BADGE_STYLES: Record<string, { bg: string; color: string }> = {
  CREATE: { bg: '#dcfce7', color: '#166534' },
  UPDATE: { bg: '#fef9c3', color: '#854d0e' },
}

function ActionBadge({ action }: { action: string }) {
  const key = action.toUpperCase()
  const badge = ACTION_BADGE_STYLES[key] ?? { bg: '#f3f4f6', color: '#374151' }
  return (
    <span
      className={styles.badge}
      style={{ backgroundColor: badge.bg, color: badge.color }}
    >
      {action}
    </span>
  )
}


export default function AuditLogPage() {
  const { t } = useLanguage()
  const [logs, setLogs] = useState<AuditLogSummary[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [filtersOpen, setFiltersOpen] = useState(false)
  const [filterEntity, setFilterEntity] = useState('')
  const [filterAction, setFilterAction] = useState('')
  const [filterUser, setFilterUser]     = useState('')
  const [filterFrom, setFilterFrom]     = useState('')
  const [filterTo, setFilterTo]         = useState('')

  const activeFilterCount = [filterEntity, filterAction, filterUser, filterFrom, filterTo]
    .filter(Boolean).length

  useEffect(() => {
    let cancelled = false
    setLoading(true)
    setError(null)
    getAuditLogs(currentPage, PAGE_SIZE)
      .then((data) => {
        if (cancelled) return
        setLogs(data.content)
        setTotalElements(data.totalElements)
        setTotalPages(data.totalPages)
      })
      .catch(() => {
        if (cancelled) return
        setError(t('audit_load_error'))
      })
      .finally(() => {
        if (!cancelled) setLoading(false)
      })
    return () => {
      cancelled = true
    }
  }, [currentPage])

  const filteredLogs = useMemo(() => {
    return logs.filter((l) => {
      if (filterEntity && l.entityName !== filterEntity) return false
      if (filterAction && l.action.toUpperCase() !== filterAction) return false
      if (filterUser && !l.user.toLowerCase().includes(filterUser.toLowerCase())) return false
      if (filterFrom && l.timestamp.slice(0, 10) < filterFrom) return false
      if (filterTo   && l.timestamp.slice(0, 10) > filterTo)   return false
      return true
    })
  }, [logs, filterEntity, filterAction, filterUser, filterFrom, filterTo])

  const pageNumbers = useMemo(
    () => Array.from({ length: totalPages }, (_, i) => i),
    [totalPages],
  )

  function goToPage(page: number) {
    if (page < 0 || page >= totalPages) return
    setCurrentPage(page)
  }

  function clearFilters() {
    setFilterEntity('')
    setFilterAction('')
    setFilterUser('')
    setFilterFrom('')
    setFilterTo('')
  }

  return (
    <div className={styles.page}>
      <div className={styles.titleArea}>
        <h1 className={styles.title}>{t('audit_title')}</h1>
        <p className={styles.subtitle}>{t('audit_subtitle')}</p>
      </div>

      <div className={styles.statCardWrap}>
        <div className={styles.statCard}>
          <div className={styles.statIcon}><FontAwesomeIcon icon={faClipboardList} /></div>
          <div className={styles.statLabel}>{t('audit_stat_total')}</div>
          <div className={styles.statValue}>{totalElements.toLocaleString()}</div>
        </div>
      </div>

      <div className={styles.controls}>
        <button
          type="button"
          className={`${styles.filterBtn} ${filtersOpen ? styles.filterBtnActive : ''}`}
          onClick={() => setFiltersOpen(o => !o)}
          aria-expanded={filtersOpen}
        >
          {t('audit_filters_btn')}
          {activeFilterCount > 0 && (
            <span className={styles.filterBadge}>{activeFilterCount}</span>
          )}
        </button>
      </div>

      {filtersOpen && (
        <div className={styles.filterPanel}>
          <div className={styles.filterRow}>
            <label className={styles.filterLabel}>
              {t('audit_filter_entity')}
              <select
                className={styles.filterSelect}
                value={filterEntity}
                onChange={(e) => setFilterEntity(e.target.value)}
              >
                <option value="">{t('audit_filter_all')}</option>
                <option value="Patient">Patient</option>
                <option value="IndibaSession">IndibaSession</option>
                <option value="PniReport">PniReport</option>
                <option value="TrainingSession">TrainingSession</option>
                <option value="Physiotherapist">Physiotherapist</option>
              </select>
            </label>

            <label className={styles.filterLabel}>
              {t('audit_filter_action')}
              <select
                className={styles.filterSelect}
                value={filterAction}
                onChange={(e) => setFilterAction(e.target.value)}
              >
                <option value="">{t('audit_filter_all')}</option>
                <option value="CREATE">CREATE</option>
                <option value="UPDATE">UPDATE</option>
              </select>
            </label>

            <label className={styles.filterLabel}>
              {t('audit_filter_user')}
              <input
                type="text"
                className={styles.filterInput}
                placeholder={t('audit_filter_any_user')}
                value={filterUser}
                onChange={(e) => setFilterUser(e.target.value)}
              />
            </label>
          </div>

          <div className={styles.filterRow}>
            <label className={styles.filterLabel}>
              {t('audit_filter_from')}
              <input
                type="date"
                className={styles.filterInput}
                value={filterFrom}
                onChange={(e) => setFilterFrom(e.target.value)}
              />
            </label>

            <label className={styles.filterLabel}>
              {t('audit_filter_to')}
              <input
                type="date"
                className={styles.filterInput}
                value={filterTo}
                onChange={(e) => setFilterTo(e.target.value)}
              />
            </label>

            <div className={styles.filterClearWrap}>
              <button type="button" className={styles.clearBtn} onClick={clearFilters}>
                {t('audit_filter_clear')}
              </button>
            </div>
          </div>
        </div>
      )}

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th scope="col">{t('audit_col_entity')}</th>
              <th scope="col">{t('audit_col_action')}</th>
              <th scope="col">{t('audit_col_timestamp')}</th>
              <th scope="col">{t('audit_col_details')}</th>
              <th scope="col">{t('audit_col_user')}</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr>
                <td colSpan={5} className={styles.stateCell}>
                  {t('common_loading')}
                </td>
              </tr>
            )}
            {!loading && error && (
              <tr>
                <td colSpan={5} className={styles.errorCell}>
                  {error}
                </td>
              </tr>
            )}
            {!loading && !error && filteredLogs.length === 0 && (
              <tr>
                <td colSpan={5} className={styles.stateCell}>
                  {t('audit_empty')}
                </td>
              </tr>
            )}
            {!loading &&
              !error &&
              filteredLogs.map((log) => (
                <tr key={log.id}>
                  <td className={styles.entityCell}>{log.entityName}</td>
                  <td><ActionBadge action={log.action} /></td>
                  <td className={styles.timestampCell}>{log.timestamp}</td>
                  <td className={styles.detailsCell}>{log.details}</td>
                  <td className={styles.userCell}>{log.user}</td>
                </tr>
              ))}
          </tbody>
        </table>

        <div className={styles.footer}>
          <span className={styles.footerText}>
            {t('audit_footer', { n: filteredLogs.length, total: totalElements.toLocaleString() })}
          </span>
          <div className={styles.pagination}>
            <button
              type="button"
              className={styles.pageBtn}
              disabled={currentPage === 0}
              onClick={() => goToPage(currentPage - 1)}
              aria-label="Previous page"
            >
              ‹
            </button>
            {pageNumbers.map((n) => (
              <button
                key={n}
                type="button"
                className={
                  n === currentPage
                    ? `${styles.pageBtn} ${styles.pageBtnActive}`
                    : styles.pageBtn
                }
                onClick={() => goToPage(n)}
                aria-label={`Page ${n + 1}`}
                aria-current={n === currentPage ? 'true' : undefined}
              >
                {n + 1}
              </button>
            ))}
            <button
              type="button"
              className={styles.pageBtn}
              disabled={currentPage >= totalPages - 1}
              onClick={() => goToPage(currentPage + 1)}
              aria-label="Next page"
            >
              ›
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
