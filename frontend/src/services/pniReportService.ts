import apiClient from '../api/client'

export interface PniReportSummary {
  id: number
  reportDate: string
}

export interface PniReportsPage {
  content: PniReportSummary[]
  totalElements: number
  totalPages: number
  pageNumber: number
  isLast: boolean
}

const EMPTY_PAGE: PniReportsPage = {
  content: [], totalElements: 0, totalPages: 0, pageNumber: 0, isLast: true,
}

export interface PniReport {
  id: number
  patientId: number
  reportDate: string
  hours_asleep: number
  hrv: number
  stress: number
  ntrs: number
}

export async function getPniReport(reportId: number): Promise<PniReport> {
  const response = await apiClient.get<PniReport>(`/pni/report/${reportId}`)
  return response.data
}

export interface PniExport {
  patientId: number
  reportId: number
  reportDate: string
  hoursAsleep: number
  hrv: number
  ansCharge: number
  sleepScore: number
}

export async function getAllPniForExport(): Promise<PniExport[]> {
  const response = await apiClient.get<PniExport[]>('/pni/export')
  return response.data ?? []
}

export async function getPniReportsFromPatient(
  patientId: number,
  page = 0,
  size = 10,
): Promise<PniReportsPage> {
  const response = await apiClient.get<PniReportsPage>(`/pni/${patientId}`, {
    params: { page, size },
  })
  if (response.status === 204 || !response.data) return EMPTY_PAGE
  return response.data
}
