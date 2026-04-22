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
