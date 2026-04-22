import apiClient from '../api/client'

export interface RegisterPhysiotherapistRequest {
  name: string
  surname: string
  secondSurname?: string
  email: string
  role: string[]
}

export async function registerPhysiotherapist(data: RegisterPhysiotherapistRequest): Promise<void> {
  await apiClient.post('/physiotherapist/register', data)
}

export interface PhysiotherapistSummary {
  id: number
  name: string
  surname: string
}

export async function getPhysiotherapist(id: number): Promise<PhysiotherapistSummary> {
  const response = await apiClient.get<PhysiotherapistSummary>(`/physiotherapist/${id}`)
  return response.data
}
