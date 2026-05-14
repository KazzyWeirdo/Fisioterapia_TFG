import { useState, useEffect } from 'react'
import { useParams, useLocation, useNavigate } from 'react-router-dom'
import { getPatient, type PatientDetail } from '../services/patientService'
import { useLanguage } from '../contexts/LanguageContext'
import { useAuth } from '../contexts/AuthContext'
import { decodeJwtPayload } from '../utils/jwt'
import PatientInfoCard from '../components/patient/PatientInfoCard'
import TrainingSessionTab from '../components/patient/TrainingSessionTab'
import IndibaSessionTab from '../components/patient/IndibaSessionTab'
import PniReportTab from '../components/patient/PniReportTab'
import StatisticsTab from '../components/patient/StatisticsTab'
import styles from './PatientDetailPage.module.css'
import TabBar from '../components/TabBar'
import Breadcrumb from '../components/Breadcrumb'

type Tab = 'overview' | 'training' | 'indiba' | 'pni' | 'statistics'

export default function PatientDetailPage() {
  const { id } = useParams<{ id: string }>()
  const location = useLocation()
  const navigate = useNavigate()
  const { t } = useLanguage()
  const { token } = useAuth()
  const isAdmin = (() => {
    try {
      const payload = decodeJwtPayload(token!)
      return (payload.scope as string)?.split(' ').includes('ADMIN') ?? false
    } catch { return false }
  })()
  const [patient, setPatient] = useState<PatientDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const initialTab = (location.state as { tab?: Tab } | null)?.tab ?? 'overview'
  const [activeTab, setActiveTab] = useState<Tab>(initialTab)

  const TABS: { id: Tab; label: string }[] = [
    { id: 'overview', label: t('tab_overview') },
    { id: 'training', label: t('patient_tab_training') },
    { id: 'indiba', label: t('patient_tab_indiba') },
    { id: 'pni', label: t('patient_tab_pni') },
    { id: 'statistics', label: t('patient_tab_stats') },
  ]

  useEffect(() => {
    if (!id) return
    setLoading(true)
    setError(null)
    getPatient(Number(id))
      .then(setPatient)
      .catch(() => setError(t('patient_load_error')))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) return <p className={styles.status}>{t('common_loading')}</p>
  if (error) return <p className={styles.error}>{error}</p>
  if (!patient) return null

  return (
    <div className={styles.page}>
      <Breadcrumb items={[
        { label: t('nav_patients'), onClick: () => navigate('/patients') },
        { label: [patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ') },
      ]} />

      <h1 className={styles.heading}>
        {[patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')}
      </h1>

      <TabBar
        tabs={TABS.map(tab => ({ id: tab.id, label: tab.label }))}
        activeTab={activeTab}
        onTabChange={id => setActiveTab(id as Tab)}
        ariaLabel="Patient sections"
      />

      <div className={styles.content}>
        {activeTab === 'overview' && <PatientInfoCard patient={patient} onPatientUpdated={p => setPatient(p)} isAdmin={isAdmin} onPatientDeleted={() => navigate('/patients')} />}
        {activeTab === 'training' && (
          <TrainingSessionTab
            patientId={Number(id)}
            patientName={[patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')}
          />
        )}
        {activeTab === 'indiba' && (
          <IndibaSessionTab
            patientId={Number(id)}
            patientName={[patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')}
          />
        )}
        {activeTab === 'pni' && (
          <PniReportTab
            patientId={Number(id)}
            patientName={[patient.nameToUse, patient.surname, patient.secondSurname].filter(Boolean).join(' ')}
          />
        )}
        {activeTab === 'statistics' && <StatisticsTab patientId={Number(id)} />}
        {activeTab !== 'overview' && activeTab !== 'training' && activeTab !== 'indiba' && activeTab !== 'pni' && activeTab !== 'statistics' && (
          <p className={styles.comingSoon}>Coming soon.</p>
        )}
      </div>
    </div>
  )
}
