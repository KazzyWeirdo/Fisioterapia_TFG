import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  getPatients,
  type PatientSummary,
} from '../services/patientService'
import styles from './PatientsPage.module.css'

const PAGE_SIZE = 10

export default function PatientsPage() {
  const navigate = useNavigate()

  const [patients, setPatients] = useState<PatientSummary[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [search, setSearch] = useState('')
  const [sortDir, setSortDir] = useState<'asc' | 'desc'>('asc')

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
        setError('Failed to load patients')
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
      <h1 className={styles.title}>Patient Records</h1>
      <p className={styles.subtitle}>
        Manage and review clinical histories, recovery progress,
        <br />
        and personalized care plans for your active clientele.
      </p>

      <div className={styles.statCard}>
        <div className={styles.statLabel}>ACTIVE</div>
        <div className={styles.statValue}>{totalElements}</div>
      </div>

      <div className={styles.controls}>
        <div className={styles.searchWrap}>
          <span className={styles.searchIcon}>🔍</span>
          <input
            type="text"
            className={styles.searchInput}
            placeholder="Search patients..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <button type="button" className={styles.downloadBtn}>
          ⬇ Download .csv
        </button>
      </div>

      <div className={styles.tableWrap}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th onClick={toggleSort} className={styles.sortableHeader}>
                PATIENT NAME {sortDir === 'asc' ? '↑' : '↓'}
              </th>
              <th className={styles.actionCol}>ACTION</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr>
                <td colSpan={2} className={styles.stateCell}>
                  Loading…
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
                  No patients found
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
                      View Details
                    </button>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>

        <div className={styles.footer}>
          <span className={styles.footerText}>
            Showing {filteredPatients.length} of {totalElements} patients
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
