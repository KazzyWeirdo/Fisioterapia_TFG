import { useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons'
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
          <FontAwesomeIcon icon={visible ? faEye : faEyeSlash} />
        </button>
      }
      {...inputProps}
    />
  )
}
