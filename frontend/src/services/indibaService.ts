import apiClient from '../api/client'

export interface IndibaSessionSummary {
  id: number
  date: string
}

export interface IndibaSessionsPage {
  content: IndibaSessionSummary[]
  totalElements: number
  totalPages: number
  pageNumber: number
  isLast: boolean
}

const EMPTY_PAGE: IndibaSessionsPage = {
  content: [], totalElements: 0, totalPages: 0, pageNumber: 0, isLast: true,
}

export interface IndibaSession {
  id: number
  patiendId: number
  beginSession: string
  endSession: string
  treatedArea: string
  mode: string
  intensity: number
  objective: string
  physiotherapistId: number
  observations: string
}

export async function getIndibaSession(sessionId: number): Promise<IndibaSession> {
  const response = await apiClient.get<IndibaSession>(`/indiba/session/${sessionId}`)
  return response.data
}

export interface CreateIndibaSessionRequest {
  patientId: number
  beginSession: string
  endSession: string
  treatedArea: string
  mode: string
  intensity: number
  objective: string
  physiotherapistId: number
  observations: string
}

export async function createIndibaSession(data: CreateIndibaSessionRequest): Promise<void> {
  await apiClient.post('/indiba/create', data)
}

export interface IndibaExport {
  patientId: number
  sessionId: number
  beginSession: string
  endSession: string
  treatedArea: string
  mode: string
  intensity: number
  objective: string
  observations: string
}

export async function getAllIndibaForExport(): Promise<IndibaExport[]> {
  const response = await apiClient.get<IndibaExport[]>('/indiba/export')
  return response.data ?? []
}

export async function getIndibaSessionsFromPatient(
  patientId: number,
  page = 0,
  size = 10,
): Promise<IndibaSessionsPage> {
  const response = await apiClient.get<IndibaSessionsPage>(`/indiba/${patientId}`, {
    params: { page, size },
  })
  if (response.status === 204 || !response.data) return EMPTY_PAGE
  return response.data
}
