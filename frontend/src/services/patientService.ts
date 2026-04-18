import apiClient from '../api/client'

export interface PatientSummary {
  id: number
  name: string
  surname: string
}

export interface PatientsPage {
  content: PatientSummary[]
  totalElements: number
  totalPages: number
  pageNumber: number
  isLast: boolean
}

export async function getPatients(
  page: number,
  size = 10,
): Promise<PatientsPage> {
  const response = await apiClient.get<PatientsPage>('/patients/list', {
    params: { page, size },
  })
  return response.data
}
