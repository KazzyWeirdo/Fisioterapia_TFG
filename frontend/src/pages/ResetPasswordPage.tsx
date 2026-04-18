import { useState, type FormEvent } from 'react'
import { useSearchParams } from 'react-router-dom'
import { resetPassword } from '../services/authService'
import AuthCard from '../components/auth/AuthCard'
import PasswordInput from '../components/auth/PasswordInput'
import AuthButton from '../components/auth/AuthButton'
import AuthLink from '../components/auth/AuthLink'
import styles from './ResetPasswordPage.module.css'

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token') ?? ''

  const [newPassword, setNewPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [submitted, setSubmitted] = useState(false)
  const [error, setError] = useState<string | null>(null)

  function validatePassword(pw: string): string | null {
    if (!pw) return 'Password is required'
    if (pw.length < 12) return 'Password must be at least 12 characters'
    if (!/[A-Z]/.test(pw)) return 'Password must contain at least one uppercase letter'
    if (!/[a-z]/.test(pw)) return 'Password must contain at least one lowercase letter'
    if (!/\d/.test(pw)) return 'Password must contain at least one digit'
    if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(pw)) return 'Password must contain at least one special character'
    return null
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    const pwError = validatePassword(newPassword)
    if (pwError) {
      setError(pwError)
      return
    }
    if (!confirmPassword) {
      setError('Please confirm your password')
      return
    }
    if (newPassword !== confirmPassword) {
      setError('Passwords do not match')
      return
    }
    setError(null)
    setLoading(true)
    try {
      await resetPassword(token, newPassword)
      setSubmitted(true)
    } catch {
      setError('Invalid or expired reset link. Please request a new one.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.page}>
      <AuthCard>
        {!token ? (
          <div className={styles.success}>
            <p className={styles.successTitle}>Invalid reset link</p>
            <p className={styles.successText}>
              This password reset link is invalid or has already been used.
            </p>
            <AuthLink to="/forgot-password">Request a new link</AuthLink>
          </div>
        ) : submitted ? (
          <div className={styles.success}>
            <div className={styles.successIcon}>&#10003;</div>
            <p className={styles.successTitle}>Password updated</p>
            <p className={styles.successText}>Your password has been changed successfully.</p>
            <AuthLink to="/login">Back to login</AuthLink>
          </div>
        ) : (
          <form className={styles.form} onSubmit={handleSubmit} noValidate>
            <h1 className={styles.title}>Set new password</h1>
            <p className={styles.subtitle}>Choose a strong password for your account.</p>

            <PasswordInput
              id="new-password"
              label="New password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
              autoComplete="new-password"
            />

            <PasswordInput
              id="confirm-password"
              label="Confirm password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              error={error}
              required
              autoComplete="new-password"
            />

            <AuthButton type="submit" loading={loading}>
              Reset password
            </AuthButton>
          </form>
        )}
      </AuthCard>

      <div className={styles.stepDots} aria-hidden="true">
        <span className={styles.dot} />
        <span className={styles.dot} />
        <span className={`${styles.dot} ${styles.active}`} />
      </div>
    </div>
  )
}
