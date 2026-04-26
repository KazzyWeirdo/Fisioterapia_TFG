import { NavLink } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'
import { decodeJwtPayload } from '../../utils/jwt'
import styles from './Sidebar.module.css'

function decodeScope(token: string | null): string | null {
  if (!token) return null
  try {
    const payload = decodeJwtPayload(token)
    return (payload.scope as string) ?? null
  } catch {
    return null
  }
}

export default function Sidebar() {
  const { token, logout } = useAuth()
  const scope = decodeScope(token)

  const isAdmin = scope?.split(' ').includes('ADMIN') ?? false
  const showAudit = isAdmin
  const showRegisterPhysio = isAdmin

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    isActive ? `${styles.link} ${styles.linkActive}` : styles.link

  return (
    <aside className={styles.sidebar}>
      <div className={styles.logo} aria-label="Fisioterapia">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <rect
            x="3"
            y="6"
            width="18"
            height="14"
            rx="2"
            stroke="white"
            strokeWidth="2"
          />
          <path d="M9 6V4h6v2" stroke="white" strokeWidth="2" />
          <path
            d="M12 10v6M9 13h6"
            stroke="white"
            strokeWidth="2"
            strokeLinecap="round"
          />
        </svg>
      </div>

      <nav className={styles.nav}>
        <div className={styles.section}>MAIN</div>
        <NavLink to="/patients" className={linkClass} end>
          <span className={styles.icon}>👥</span> Patients
        </NavLink>
        {showAudit && (
          <NavLink to="/audit" className={linkClass}>
            <span className={styles.icon}>📋</span> Audit Logs
          </NavLink>
        )}

        <div className={styles.section}>ACTIONS</div>
        <NavLink to="/patients/register" className={linkClass}>
          <span className={styles.icon}>👤</span> Register Patient
        </NavLink>
        <NavLink to="/indiba/register" className={linkClass}>
          <span className={styles.icon}>➕</span> Register Indiba Session
        </NavLink>
        {showRegisterPhysio && (
          <NavLink to="/physiotherapist/register" className={linkClass}>
            <span className={styles.icon}>➕</span> Register Physiotherapist
          </NavLink>
        )}
      </nav>

      <div className={styles.bottom}>
        <button
          type="button"
          onClick={logout}
          className={`${styles.link} ${styles.logout}`}
        >
          <span className={styles.icon}>↪</span> Logout
        </button>
      </div>
    </aside>
  )
}
