import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { useLanguage } from '../contexts/LanguageContext'
import { login } from '../services/authService'
import AuthCard from '../components/auth/AuthCard'
import AuthInput from '../components/auth/AuthInput'
import PasswordInput from '../components/auth/PasswordInput'
import AuthButton from '../components/auth/AuthButton'
import AuthLink from '../components/auth/AuthLink'
import styles from './LoginPage.module.css'

export default function LoginPage() {
  const navigate = useNavigate()
  const auth = useAuth()
  const { t } = useLanguage()

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [passwordError, setPasswordError] = useState<string | null>(null)

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)
    setPasswordError(null)

    if (!email.trim()) {
      setError(t('login_error_email_required'))
      return
    }
    if (!password) {
      setPasswordError(t('login_error_password_required'))
      return
    }

    setLoading(true)
    try {
      const token = await login(email, password)
      auth.login(token)
      navigate('/patients', { replace: true })
    } catch {
      setError(t('login_error_invalid'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.page}>
      <AuthCard>
        <form className={styles.form} onSubmit={handleSubmit}>
          <h1 className={styles.title}>{t('login_title')}</h1>

          <AuthInput
            id="email"
            label={t('login_email_label')}
            type="email"
            placeholder="fisio@example.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            error={error}
            required
            autoComplete="email"
          />

          <PasswordInput
            id="password"
            label={t('login_password_label')}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={passwordError}
            required
            autoComplete="current-password"
          />

          <AuthButton type="submit" loading={loading}>
            {t('login_submit')}
          </AuthButton>

          <AuthLink to="/forgot-password">{t('login_change_password')}</AuthLink>
        </form>
      </AuthCard>

      <footer className={styles.footer}>{t('login_footer')}</footer>
    </div>
  )
}
