import { useEffect, useMemo, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMagnifyingGlass, faDownload } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'
import { useLanguage } from '../contexts/LanguageContext'
import {
  getPatients,
  getAllPatientsForExport,
  type PatientSummary,
  type PatientExport,
} from '../services/patientService'
import { getAllIndibaForExport, type IndibaExport } from '../services/indibaService'
import { getAllPniForExport, type PniExport } from '../services/pniReportService'
import { getAllTrainingForExport, type TrainingSetExport } from '../services/trainingSessionService'
import { downloadCsv } from '../utils/csvUtils'
import styles from './PatientsPage.module.css'

const PAGE_SIZE = 10

export default function PatientsPage() {
  const navigate = useNavigate()
  const { t } = useLanguage()

  const [patients, setPatients] = useState<PatientSummary[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [search, setSearch] = useState('')
  const [sortDir, setSortDir] = useState<'asc' | 'desc'>('asc')
  const [downloading, setDownloading] = useState<Record<string, boolean>>({})

  useEffect(() => {
    let cancelled = false
    setLoading(true)
    setError(null)
    getPatients(currentPage, PAGE_SIZE, sortDir)
      .then((data) => {
        if (cancelled) return
        setPatients(data.content)
        setTotalElements(data.totalElements)
        setTotalPages(data.totalPages)
      })
      .catch(() => {
        if (cancelled) return
        setError(t('patients_load_error'))
      })
      .finally(() => {
        if (!cancelled) setLoading(false)
      })
    return () => {
      cancelled = true
    }
  }, [currentPage, sortDir])

  const filteredPatients = useMemo(() => {
    const q = search.trim().toLowerCase()
    if (!q) return patients
    return patients.filter(
      (p) =>
        p.name.toLowerCase().includes(q) ||
        p.surname.toLowerCase().includes(q),
    )
  }, [patients, search])

  const pageNumbers = useMemo(
    () => Array.from({ length: totalPages }, (_, i) => i),
    [totalPages],
  )

  async function handleExport<T>(
    key: string,
    fetchFn: () => Promise<T[]>,
    filename: string,
    headers: string[],
    toRow: (item: T) => (string | number | null | undefined)[],
  ) {
    setDownloading((d) => ({ ...d, [key]: true }))
    try {
      const data = await fetchFn()
      downloadCsv(filename, headers, data.map(toRow))
    } finally {
      setDownloading((d) => ({ ...d, [key]: false }))
    }
  }

  function toggleSort() {
    setSortDir((d) => (d === 'asc' ? 'desc' : 'asc'))
    setCurrentPage(0)
  }

  function goToPage(page: number) {
    if (page < 0 || page >= totalPages) return
    setCurrentPage(page)
  }

  return (
    <div className={styles.page}>
      <h1 className={styles.title}>{t('patients_title')}</h1>
      <p className={styles.subtitle}>{t('patients_page_subtitle')}</p>

      <div className={styles.statCard}>
        <div className={styles.statLabel}>{t('patients_stat_active')}</div>
        <div className={styles.statValue}>{totalElements}</div>
      </div>

      <div className={styles.controls}>
        <div className={styles.searchWrap}>
          <FontAwesomeIcon icon={faMagnifyingGlass} className={styles.searchIcon} />
          <input
            type="text"
            className={styles.searchInput}
            placeholder={t('patients_search_placeholder')}
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <div className={styles.exportGroup}>
          <button
            type="button"
            className={styles.downloadBtn}
            disabled={downloading.patients}
            onClick={() => handleExport<PatientExport>(
              'patients',
              getAllPatientsForExport,
              'patients.csv',
              ['id', 'date_of_birth', 'clinical_sex'],
              (p) => [p.id, p.dateOfBirth, p.clinicalUseSex],
            )}
          >
            {downloading.patients ? t('patients_exporting') : <><FontAwesomeIcon icon={faDownload} /> {t('patients_export_patients')}</>}
          </button>
          <button
            type="button"
            className={styles.downloadBtn}
            disabled={downloading.indiba}
            onClick={() => handleExport<IndibaExport>(
              'indiba',
              getAllIndibaForExport,
              'indiba_sessions.csv',
              ['patient_id', 'session_id', 'begin_session', 'end_session', 'treated_area', 'mode', 'intensity', 'objective', 'observations'],
              (s) => [s.patientId, s.sessionId, s.beginSession, s.endSession, s.treatedArea, s.mode, s.intensity, s.objective, s.observations],
            )}
          >
            {downloading.indiba ? t('patients_exporting') : <><FontAwesomeIcon icon={faDownload} /> {t('patients_export_indiba')}</>}
          </button>
          <button
            type="button"
            className={styles.downloadBtn}
            disabled={downloading.pni}
            onClick={() => handleExport<PniExport>(
              'pni',
              getAllPniForExport,
              'pni_reports.csv',
              ['patient_id', 'report_id', 'report_date', 'hours_asleep', 'hrv', 'ans_charge', 'sleep_score'],
              (r) => [r.patientId, r.reportId, r.reportDate, r.hoursAsleep, r.hrv, r.ansCharge, r.sleepScore],
            )}
          >
            {downloading.pni ? t('patients_exporting') : <><FontAwesomeIcon icon={faDownload} /> {t('patients_export_pni')}</>}
          </button>
          <button
            type="button"
            className={styles.downloadBtn}
            disabled={downloading.training}
            onClick={() => handleExport<TrainingSetExport>(
              'training',
              getAllTrainingForExport,
              'training_sessions.csv',
              ['patient_id', 'session_id', 'session_date', 'exercise_name', 'set_number', 'weight_kg', 'reps', 'rest_time_seconds', 'rpe'],
              (s) => [s.patientId, s.sessionId, s.sessionDate, s.exerciseName, s.setNumber, s.weightKg, s.reps, s.restTimeSeconds, s.rpe],
            )}
          >
            {downloading.training ? t('patients_exporting') : <><FontAwesomeIcon icon={faDownload} /> {t('patients_export_training')}</>}
          </button>
        </div>
      </div>

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th onClick={toggleSort} className={styles.sortableHeader}>
                {t('patients_col_name')} {sortDir === 'asc' ? '↑' : '↓'}
              </th>
              <th className={styles.actionCol}>{t('patients_col_action')}</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr>
                <td colSpan={2} className={styles.stateCell}>
                  {t('common_loading')}
                </td>
              </tr>
            )}
            {!loading && error && (
              <tr>
                <td colSpan={2} className={styles.errorCell}>
                  {error}
                </td>
              </tr>
            )}
            {!loading && !error && filteredPatients.length === 0 && (
              <tr>
                <td colSpan={2} className={styles.stateCell}>
                  {t('patients_empty')}
                </td>
              </tr>
            )}
            {!loading &&
              !error &&
              filteredPatients.map((p) => (
                <tr key={p.id}>
                  <td className={styles.nameCell}>{[p.name, p.surname, p.secondSurname].filter(Boolean).join(' ')}</td>
                  <td className={styles.actionCol}>
                    <button
                      type="button"
                      className={styles.viewBtn}
                      onClick={() => navigate(`/patients/${p.id}`)}
                    >
                      {t('common_view_details')}
                    </button>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>

        <div className={styles.footer}>
          <span className={styles.footerText}>
            {t('patients_footer', { n: filteredPatients.length, total: totalElements })}
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
