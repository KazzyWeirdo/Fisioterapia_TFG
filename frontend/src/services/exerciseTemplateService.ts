import apiClient from '../api/client'

export interface ExerciseSetInput {
  setNumber: number
  weightKg: number
  reps: number
  restTimeSeconds: number
  rpe: number
}

export interface ExerciseInput {
  name: string
  sets: ExerciseSetInput[]
}

export interface ExerciseTemplateDetail {
  id: number
  name: string
  exercises: Exercise[]
}

export interface Exercise {
  id: number
  name: string
  sets: ExerciseSet[]
}

export interface ExerciseSet {
  setNumber: number
  weightKg: number
  reps: number
  restTimeSeconds: number
  rpe: number
}

export interface CreateExerciseTemplateRequest {
  name: string
  exercises: ExerciseInput[]
}

export async function createExerciseTemplate(
  body: CreateExerciseTemplateRequest,
): Promise<void> {
  await apiClient.post(`/exercise-template`, body)
}

export async function getAllExerciseTemplates(): Promise<ExerciseTemplateDetail[]> {
  const response = await apiClient.get<ExerciseTemplateDetail[]>('/exercise-template')
  return response.data ?? []
}

export async function getExerciseTemplate(id: number): Promise<ExerciseTemplateDetail> {
  const response = await apiClient.get<ExerciseTemplateDetail>(`/exercise-template/${id}`)
  return response.data
}
