import apiClient from '../api/client'

export interface PatientSummary {
  id: number
  name: string
  surname: string
  secondSurname: string
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
  sortDir: 'asc' | 'desc' = 'asc',
): Promise<PatientsPage> {
  const response = await apiClient.get<PatientsPage>('/patients/list', {
    params: { page, size, sort: `nameToUse,${sortDir}` },
  })
  return response.data
}

export interface PatientDetail {
  id: number
  email: string
  dni: string
  genderIdentity: string
  clinicalUseSex: string
  administrativeSex: string
  legalName: string
  nameToUse: string
  surname: string
  secondSurname: string
  pronouns: string
  phoneNumber: number
  dateOfBirth: string
  hasPolarConnection: boolean
}

export async function getPatient(id: number): Promise<PatientDetail> {
  const response = await apiClient.get<PatientDetail>(`/patients/${id}`)
  return response.data
}

export interface CreatePatientRequest {
  legalName: string
  nameToUse: string
  pronouns: string
  surname: string
  secondSurname: string
  dni: string
  dateOfBirth: string
  email: string
  phoneNumber: number
  genderIdentity: string
  clinicalUseSex: string
  administrativeSex: string
}

export async function createPatient(data: CreatePatientRequest): Promise<void> {
  await apiClient.post('/patients/create', data)
}

export async function updatePatient(id: number, data: CreatePatientRequest): Promise<void> {
  await apiClient.put(`/patients/${id}`, data)
}

export interface PatientExport {
  id: number
  dateOfBirth: string
  clinicalUseSex: string
}

export async function getAllPatientsForExport(): Promise<PatientExport[]> {
  const response = await apiClient.get<PatientExport[]>('/patients/export')
  return response.data ?? []
}
