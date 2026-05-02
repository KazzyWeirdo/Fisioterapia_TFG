import { useEffect } from 'react'
import { NavLink, useLocation } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUsers, faClipboardList, faUser, faPlus, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons'
import { useAuth } from '../../contexts/AuthContext'
import { useLanguage } from '../../contexts/LanguageContext'
import { decodeJwtPayload } from '../../utils/jwt'
import styles from './Sidebar.module.css'

interface SidebarProps {
  isOpen: boolean
  onClose: () => void
}

function decodeScope(token: string | null): string | null {
  if (!token) return null
  try {
    const payload = decodeJwtPayload(token)
    return (payload.scope as string) ?? null
  } catch {
    return null
  }
}

export default function Sidebar({ isOpen, onClose }: SidebarProps) {
  const { token, logout } = useAuth()
  const { t } = useLanguage()
  const scope = decodeScope(token)
  const location = useLocation()

  // Auto-close sidebar on route change (mobile UX)
  useEffect(() => {
    onClose()
  }, [location.pathname, onClose])

  const isAdmin = scope?.split(' ').includes('ADMIN') ?? false
  const showAudit = isAdmin
  const showRegisterPhysio = isAdmin

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    isActive ? `${styles.link} ${styles.linkActive}` : styles.link

  return (
    <>
      {isOpen && (
        <div
          className={styles.backdrop}
          onClick={onClose}
          aria-hidden="true"
        />
      )}
      <aside
        className={`${styles.sidebar} ${isOpen ? styles.sidebarOpen : ''}`}
      >
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
          <div className={styles.section}>{t('sidebar_main')}</div>
          <NavLink to="/patients" className={linkClass} end>
            <FontAwesomeIcon icon={faUsers} className={styles.icon} /> {t('nav_patients')}
          </NavLink>
          {showAudit && (
            <NavLink to="/audit" className={linkClass}>
              <FontAwesomeIcon icon={faClipboardList} className={styles.icon} /> {t('nav_audit')}
            </NavLink>
          )}

          <div className={styles.section}>{t('sidebar_actions')}</div>
          <NavLink to="/patients/register" className={linkClass}>
            <FontAwesomeIcon icon={faUser} className={styles.icon} /> {t('nav_register_patient')}
          </NavLink>
          <NavLink to="/indiba/register" className={linkClass}>
            <FontAwesomeIcon icon={faPlus} className={styles.icon} /> {t('nav_register_indiba')}
          </NavLink>
          {showRegisterPhysio && (
            <NavLink to="/physiotherapist/register" className={linkClass}>
              <FontAwesomeIcon icon={faPlus} className={styles.icon} /> {t('nav_register_physio')}
            </NavLink>
          )}
        </nav>

        <div className={styles.bottom}>
          <button
            type="button"
            onClick={logout}
            className={`${styles.link} ${styles.logout}`}
          >
            <FontAwesomeIcon icon={faArrowRightFromBracket} className={styles.icon} /> {t('logout')}
          </button>
        </div>
      </aside>
    </>
  )
}
