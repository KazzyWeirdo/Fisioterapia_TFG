import { Link } from 'react-router-dom'
import type { ReactNode } from 'react'
import styles from './AuthLink.module.css'

interface AuthLinkProps {
  to: string
  children: ReactNode
}

export default function AuthLink({ to, children }: AuthLinkProps) {
  return (
    <Link to={to} className={styles.link}>
      {children}
    </Link>
  )
}
