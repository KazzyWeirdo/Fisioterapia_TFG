import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { getPatient, type PatientDetail } from '../services/patientService'
import PatientInfoCard from '../components/patient/PatientInfoCard'
import IndibaSessionTab from '../components/patient/IndibaSessionTab'
import styles from './PatientDetailPage.module.css'

type Tab = 'overview' | 'training' | 'indiba' | 'pni' | 'statistics'

const TABS: { id: Tab; label: string }[] = [
  { id: 'overview', label: 'Overview' },
  { id: 'training', label: 'Training Sessions' },
  { id: 'indiba', label: 'INDIBA Sessions' },
  { id: 'pni', label: 'PNI Reports' },
  { id: 'statistics', label: 'Statistics' },
]

export default function PatientDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [patient, setPatient] = useState<PatientDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [activeTab, setActiveTab] = useState<Tab>('overview')

  useEffect(() => {
    if (!id) return
    setLoading(true)
    setError(null)
    getPatient(Number(id))
      .then(setPatient)
      .catch(() => setError('Failed to load patient'))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) return <p className={styles.status}>Loading…</p>
  if (error) return <p className={styles.error}>{error}</p>
  if (!patient) return null

  return (
    <div className={styles.page}>
      <h1 className={styles.heading}>
        {[patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')}
      </h1>

      <nav role="tablist" className={styles.tabBar} aria-label="Patient sections">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            role="tab"
            aria-selected={activeTab === tab.id}
            className={`${styles.tab} ${activeTab === tab.id ? styles.tabActive : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </nav>
      <div className={styles.tabSeparator} />

      <div className={styles.content}>
        {activeTab === 'overview' && <PatientInfoCard patient={patient} />}
        {activeTab === 'indiba' && (
          <IndibaSessionTab
            patientId={Number(id)}
            patientName={[patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')}
          />
        )}
        {activeTab !== 'overview' && activeTab !== 'indiba' && (
          <p className={styles.comingSoon}>Coming soon.</p>
        )}
      </div>
    </div>
  )
}
