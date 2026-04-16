import { useState } from 'react'
import AuthInput from './AuthInput'
import type { InputHTMLAttributes } from 'react'
import styles from './PasswordInput.module.css'

type PasswordInputProps = Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> & {
  label: string
  error?: string | null
  id: string
}

export default function PasswordInput({ label, error, id, ...inputProps }: PasswordInputProps) {
  const [visible, setVisible] = useState(false)

  return (
    <AuthInput
      label={label}
      error={error}
      id={id}
      type={visible ? 'text' : 'password'}
      suffix={
        <button
          type="button"
          className={styles.toggle}
          onClick={() => setVisible((v) => !v)}
          aria-label="Toggle password visibility"
        >
          {visible ? <EyeIcon /> : <EyeOffIcon />}
        </button>
      }
      {...inputProps}
    />
  )
}

function EyeIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path
        d="M10 4C5.5 4 2 10 2 10s3.5 6 8 6 8-6 8-6-3.5-6-8-6z"
        stroke="#6b7280"
        strokeWidth="1.5"
        strokeLinejoin="round"
      />
      <circle cx="10" cy="10" r="2.5" stroke="#6b7280" strokeWidth="1.5" />
    </svg>
  )
}

function EyeOffIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path
        d="M3 3l14 14M8.7 8.7A2.5 2.5 0 0 0 12.3 12.3M6.1 6.1C4.3 7.3 2 10 2 10s3.5 6 8 6c1.7 0 3.2-.6 4.5-1.5M10 4c4.5 0 8 6 8 6a13.5 13.5 0 0 1-2.1 2.9"
        stroke="#6b7280"
        strokeWidth="1.5"
        strokeLinecap="round"
      />
    </svg>
  )
}
