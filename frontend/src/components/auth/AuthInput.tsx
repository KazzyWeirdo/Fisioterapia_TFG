import type { InputHTMLAttributes, ReactNode } from 'react'
import styles from './AuthInput.module.css'

interface AuthInputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string
  error?: string | null
  id: string
  suffix?: ReactNode
}

export default function AuthInput({ label, error, id, suffix, ...inputProps }: AuthInputProps) {
  return (
    <div className={styles.field}>
      <label className={styles.label} htmlFor={id}>
        {label}
      </label>
      <div className={styles.inputWrapper}>
        <input
          id={id}
          className={styles.input}
          aria-invalid={!!error}
          aria-describedby={error ? `${id}-error` : undefined}
          {...inputProps}
        />
        {suffix}
      </div>
      {error && (
        <span className={styles.error} id={`${id}-error`} role="alert">
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none" aria-hidden="true">
            <circle cx="7" cy="7" r="6.5" stroke="#ef4444" />
            <path d="M7 4v3.5" stroke="#ef4444" strokeWidth="1.5" strokeLinecap="round" />
            <circle cx="7" cy="10" r="0.75" fill="#ef4444" />
          </svg>
          {error}
        </span>
      )}
    </div>
  )
}
