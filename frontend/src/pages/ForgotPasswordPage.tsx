import { useState, type FormEvent } from 'react'
import { forgotPassword } from '../services/authService'
import { useLanguage } from '../contexts/LanguageContext'
import AuthCard from '../components/auth/AuthCard'
import AuthInput from '../components/auth/AuthInput'
import AuthButton from '../components/auth/AuthButton'
import AuthLink from '../components/auth/AuthLink'
import styles from './ForgotPasswordPage.module.css'

export default function ForgotPasswordPage() {
  const { t } = useLanguage()
  const [email, setEmail] = useState('')
  const [loading, setLoading] = useState(false)
  const [submitted, setSubmitted] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)

    if (!email.trim()) {
      setError(t('login_error_email_required'))
      return
    }

    setLoading(true)
    try {
      await forgotPassword(email)
      setSubmitted(true)
    } catch {
      setError(t('forgot_error'))
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
            <p className={styles.successTitle}>{t('forgot_success')}</p>
          </div>
        ) : (
          <form className={styles.form} onSubmit={handleSubmit} noValidate>
            <h1 className={styles.title}>{t('forgot_title')}</h1>
            <p className={styles.subtitle}>{t('forgot_subtitle')}</p>

            <AuthInput
              id="reset-email"
              label={t('forgot_email_label')}
              type="email"
              placeholder="name@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              error={error}
              required
              autoComplete="email"
            />

            <AuthButton type="submit" loading={loading}>
              {t('forgot_submit')}
            </AuthButton>
          </form>
        )}

        <AuthLink to="/login">{t('forgot_back')}</AuthLink>
      </AuthCard>

      <div className={styles.stepDots} aria-hidden="true">
        <span className={styles.dot} />
        <span className={`${styles.dot} ${styles.active}`} />
        <span className={styles.dot} />
      </div>
    </div>
  )
}
