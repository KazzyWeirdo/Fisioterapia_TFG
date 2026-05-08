import { useLanguage } from '../../contexts/LanguageContext'
import styles from './LanguageSwitcher.module.css'

export default function LanguageSwitcher() {
  const { locale, toggleLanguage } = useLanguage()

  return (
    <button
      type="button"
      className={styles.pill}
      onClick={toggleLanguage}
      aria-label={`Switch to ${locale === 'en' ? 'Spanish' : 'English'}`}
      title={`Switch to ${locale === 'en' ? 'Español' : 'English'}`}
    >
      <span className={locale === 'en' ? styles.active : styles.inactive}>EN</span>
      <span className={styles.divider}>/</span>
      <span className={locale === 'es' ? styles.active : styles.inactive}>ES</span>
    </button>
  )
}
