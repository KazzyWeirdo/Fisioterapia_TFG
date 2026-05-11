import apiClient from '../api/client'

export interface PatientSummary {
  id: number
  name: string
  surname: string
  secondSurname: string
  pathology: string | null
  functionalScore: number | null
  dischargeDate: string | null
}

export interface PatientsPage {
  content: PatientSummary[]
  totalElements: number
  totalPages: number
  pageNumber: number
  isLast: boolean
}

const EMPTY_PATIENTS_PAGE: PatientsPage = {
  content: [], totalElements: 0, totalPages: 0, pageNumber: 0, isLast: true,
}

export async function getPatients(
  page: number,
  size = 10,
  sortDir: 'asc' | 'desc' = 'asc',
): Promise<PatientsPage> {
  const response = await apiClient.get<PatientsPage>('/patients/list', {
    params: { page, size, sort: `nameToUse,${sortDir}` },
  })
  if (response.status === 204 || !response.data) return EMPTY_PATIENTS_PAGE
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
  pathology: string | null
  registrationDate: string | null
  functionalScore: number | null
  dischargeDate: string | null
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
  pathology?: string
}

export async function createPatient(data: CreatePatientRequest): Promise<void> {
  await apiClient.post('/patients/create', data)
}

export async function updatePatient(id: number, data: CreatePatientRequest): Promise<void> {
  await apiClient.put(`/patients/${id}`, data)
}

export async function updateFunctionalScore(patientId: number, score: number): Promise<void> {
  await apiClient.patch(`/patients/${patientId}/functional-score`, { score })
}

export async function dischargePatient(patientId: number): Promise<void> {
  await apiClient.patch(`/patients/${patientId}/discharge`)
}

export interface PatientExport {
  id: number
  dateOfBirth: string
  clinicalUseSex: string
  pathology: string | null
}

export async function getAllPatientsForExport(): Promise<PatientExport[]> {
  const response = await apiClient.get<PatientExport[]>('/patients/export')
  return response.data ?? []
}
