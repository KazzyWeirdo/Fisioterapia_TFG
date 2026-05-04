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

export interface ExerciseSet {
  setNumber: number
  weightKg: number
  reps: number
  restTimeSeconds: number
  rpe: number
}

export interface Exercise {
  id: number
  name: string
  sets: ExerciseSet[]
}

export interface TrainingSessionDetail {
  id: number
  patientId: number
  date: string
  exercises: Exercise[]
}

export interface ExerciseSetInput {
  setNumber: number
  weightKg: number
  reps: number
  restTimeSeconds: number
  rpe: number
}

export interface ExerciseInput {
  name: string
  exercises: ExerciseSetInput[]
}

export interface CreateTrainingSessionRequest {
  patientId: number
  date: string
  exercises: ExerciseInput[]
}

export async function createTrainingSession(
  body: CreateTrainingSessionRequest,
): Promise<void> {
  await apiClient.post(`/training-session/create`, body)
}

export async function getTrainingSession(id: number): Promise<TrainingSessionDetail> {
  const response = await apiClient.get<TrainingSessionDetail>(`/training-session/session/${id}`)
  return response.data
}

export interface TrainingSetExport {
  patientId: number
  sessionId: number
  sessionDate: string
  exerciseName: string
  setNumber: number
  weightKg: number
  reps: number
  restTimeSeconds: number
  rpe: number
}

export async function getAllTrainingForExport(): Promise<TrainingSetExport[]> {
  const response = await apiClient.get<TrainingSetExport[]>('/training-session/export')
  return response.data ?? []
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
