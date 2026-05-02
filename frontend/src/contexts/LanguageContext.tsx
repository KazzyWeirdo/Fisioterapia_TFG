import { createContext, useContext, useState, type ReactNode } from 'react'
import { translations, type Locale, type TranslationKey } from '../i18n/translations'

interface LanguageContextValue {
  locale: Locale
  t: (key: TranslationKey) => string
  toggleLanguage: () => void
}

const LanguageContext = createContext<LanguageContextValue | null>(null)

export function LanguageProvider({ children }: { children: ReactNode }) {
  const [locale, setLocale] = useState<Locale>(() => {
    const stored = localStorage.getItem('lang')
    return stored === 'es' ? 'es' : 'en'
  })

  const t = (key: TranslationKey): string => translations[locale][key]

  const toggleLanguage = () => {
    setLocale(prev => {
      const next = prev === 'en' ? 'es' : 'en'
      localStorage.setItem('lang', next)
      return next
    })
  }

  return (
    <LanguageContext.Provider value={{ locale, t, toggleLanguage }}>
      {children}
    </LanguageContext.Provider>
  )
}

export function useLanguage(): LanguageContextValue {
  const ctx = useContext(LanguageContext)
  if (!ctx) throw new Error('useLanguage must be used inside LanguageProvider')
  return ctx
}
