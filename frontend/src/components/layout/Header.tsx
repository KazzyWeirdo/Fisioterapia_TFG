import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBars } from '@fortawesome/free-solid-svg-icons'
import { useAuth } from '../../contexts/AuthContext'
import { decodeJwtPayload } from '../../utils/jwt'
import LanguageSwitcher from './LanguageSwitcher'
import styles from './Header.module.css'

interface HeaderProps {
  onMenuToggle: () => void
}

function decodeDisplayName(token: string | null): string {
  if (!token) return 'Physiotherapist'
  try {
    const payload = decodeJwtPayload(token)
    const name = (payload.name as string) ?? ''
    const surname = (payload.surname as string) ?? ''
    return `${name} ${surname}`.trim() || 'Physiotherapist'
  } catch {
    return 'Physiotherapist'
  }
}

export default function Header({ onMenuToggle }: HeaderProps) {
  const { token } = useAuth()
  const displayName = decodeDisplayName(token)

  return (
    <header className={styles.header}>
      <button
        type="button"
        className={styles.menuBtn}
        onClick={onMenuToggle}
        aria-label="Toggle menu"
      >
        <FontAwesomeIcon icon={faBars} />
      </button>
      <div className={styles.user}>
        <LanguageSwitcher />
        <span className={styles.name}>{displayName}</span>
      </div>
    </header>
  )
}
