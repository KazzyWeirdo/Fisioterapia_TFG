import { useAuth } from '../../contexts/AuthContext'
import { decodeJwtPayload } from '../../utils/jwt'
import styles from './Header.module.css'

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

export default function Header() {
  const { token } = useAuth()
  const displayName = decodeDisplayName(token)

  return (
    <header className={styles.header}>
      <div className={styles.user}>
        <span className={styles.name}>{displayName}</span>
      </div>
    </header>
  )
}
