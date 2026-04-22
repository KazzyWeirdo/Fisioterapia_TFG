import apiClient from '../api/client'

export interface PhysiotherapistSummary {
  id: number
  name: string
  surname: string
}

export async function getPhysiotherapist(id: number): Promise<PhysiotherapistSummary> {
  const response = await apiClient.get<PhysiotherapistSummary>(`/physiotherapist/${id}`)
  return response.data
}
