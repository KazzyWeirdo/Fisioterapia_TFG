import { useTranslation } from 'react-i18next'
import i18n from '../i18n/config'

export type Locale = 'en' | 'es'

export function LanguageProvider({ children }: { children: React.ReactNode }) {
  return <>{children}</>
}

export function useLanguage() {
  const { t } = useTranslation()
  const locale = (i18n.language ?? 'en') as Locale

  const toggleLanguage = () => {
    const next: Locale = locale === 'en' ? 'es' : 'en'
    i18n.changeLanguage(next)
    localStorage.setItem('lang', next)
  }

  return { t, locale, toggleLanguage }
}
