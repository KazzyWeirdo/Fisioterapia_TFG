import { useEffect } from 'react'
import styles from './Toast.module.css'

interface ToastProps {
  message: string
  type?: 'error' | 'success'
  onClose: () => void
  duration?: number
}

export default function Toast({ message, type = 'error', onClose, duration = 4000 }: ToastProps) {
  useEffect(() => {
    const timer = setTimeout(onClose, duration)
    return () => clearTimeout(timer)
  }, [onClose, duration])

  return (
    <div className={`${styles.toast} ${styles[type]}`} role="alert">
      <span className={styles.message}>{message}</span>
      <button className={styles.close} onClick={onClose} aria-label="Close">×</button>
    </div>
  )
}
