import apiClient from '../api/client'

export interface TrainingSessionsPage {
  content: { id: number; date: string }[]
  totalElements: number
  totalPages: number
  pageNumber: number
  isLast: boolean
}

const EMPTY_PAGE: TrainingSessionsPage = {
  content: [], totalElements: 0, totalPages: 0, pageNumber: 0, isLast: true,
}

export async function getTrainingSessionsFromPatient(
  patientId: number,
  page = 0,
  size = 10,
): Promise<TrainingSessionsPage> {
  const response = await apiClient.get<TrainingSessionsPage>(`/training-session/${patientId}`, {
    params: { page, size },
  })
  if (response.status === 204 || !response.data) return EMPTY_PAGE
  return response.data
}
