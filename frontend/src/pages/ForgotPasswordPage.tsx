import { useState, type FormEvent } from 'react'
import { forgotPassword } from '../services/authService'
import AuthCard from '../components/auth/AuthCard'
import AuthInput from '../components/auth/AuthInput'
import AuthButton from '../components/auth/AuthButton'
import AuthLink from '../components/auth/AuthLink'
import styles from './ForgotPasswordPage.module.css'

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('')
  const [loading, setLoading] = useState(false)
  const [submitted, setSubmitted] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)

    if (!email.trim()) {
      setError('Email is required')
      return
    }

    setLoading(true)
    try {
      await forgotPassword(email)
      setSubmitted(true)
    } catch {
      setError('Something went wrong. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.page}>
      <AuthCard>
        {submitted ? (
          <div className={styles.success}>
            <div className={styles.successIcon}>&#10003;</div>
            <p className={styles.successTitle}>Check your inbox</p>
            <p className={styles.successText}>
              We've sent a password reset link to <strong>{email}</strong>. It may take a few
              minutes to arrive.
            </p>
          </div>
        ) : (
          <form className={styles.form} onSubmit={handleSubmit} noValidate>
            <h1 className={styles.title}>Forgot password?</h1>
            <p className={styles.subtitle}>
              Enter your email address and we'll send you a link to reset your password.
            </p>

            <AuthInput
              id="reset-email"
              label="Email address"
              type="email"
              placeholder="name@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              error={error}
              required
              autoComplete="email"
            />

            <AuthButton type="submit" loading={loading}>
              Send reset link
            </AuthButton>
          </form>
        )}

        <AuthLink to="/login">&#8592; Back to login</AuthLink>
      </AuthCard>

      <div className={styles.stepDots} aria-hidden="true">
        <span className={styles.dot} />
        <span className={`${styles.dot} ${styles.active}`} />
        <span className={styles.dot} />
      </div>
    </div>
  )
}
