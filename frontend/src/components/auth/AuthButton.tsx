import type { ButtonHTMLAttributes, ReactNode } from 'react'
import styles from './AuthButton.module.css'

interface AuthButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  loading?: boolean
  children: ReactNode
}

export default function AuthButton({ loading, children, disabled, ...props }: AuthButtonProps) {
  return (
    <button className={styles.button} disabled={disabled || loading} {...props}>
      {loading && <span className={styles.spinner} aria-hidden="true" />}
      {children}
    </button>
  )
}
