import apiClient from '../api/client'

export interface TransitionRatioPoint {
  month: number
  transitionRatio: number
}

export interface WorkloadPoint {
  sessionDate: string
  workload: number
}

export async function getTransitionRatio(
  patientId: number,
  year: number,
): Promise<TransitionRatioPoint[]> {
  const response = await apiClient.get<TransitionRatioPoint[]>(
    `/statistics/${patientId}/${year}/patient-transition-ratio`,
  )
  if (response.status === 204 || !response.data) return []
  return response.data
}

export async function getWorkloadProgression(
  patientId: number,
  exerciseName: string,
): Promise<WorkloadPoint[]> {
  const response = await apiClient.get<WorkloadPoint[]>(
    `/statistics/${patientId}/${encodeURIComponent(exerciseName)}/workload-progression`,
  )
  if (response.status === 204 || !response.data) return []
  return response.data
}
