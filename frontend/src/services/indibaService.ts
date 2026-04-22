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
