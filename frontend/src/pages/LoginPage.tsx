import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
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
      setError('Email is required')
      return
    }
    if (!password) {
      setPasswordError('Password is required')
      return
    }

    setLoading(true)
    try {
      const token = await login(email, password)
      auth.login(token)
      navigate('/patients', { replace: true })
    } catch {
      setError('Email non-existent or incorrect password')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.page}>
      <AuthCard>
        <form className={styles.form} onSubmit={handleSubmit}>
          <h1 className={styles.title}>Welcome back</h1>

          <AuthInput
            id="email"
            label="Practitioner Email"
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
            label="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={passwordError}
            required
            autoComplete="current-password"
          />

          <AuthButton type="submit" loading={loading}>
            Login
          </AuthButton>

          <AuthLink to="/forgot-password">Change password &rsaquo;</AuthLink>
        </form>
      </AuthCard>

      <footer className={styles.footer}>Secure Access Point</footer>
    </div>
  )
}
