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

export interface IndibaSessionStats {
  totalSessions: number
  avgDurationMinutes: number
  mostTreatedArea: string | null
  avgCapacitiveIntensity: number | null
  avgResistiveIntensity: number | null
  modeDistribution: Record<string, number>
}

export interface PathologyRehabStats {
  pathology: string
  averageDaysToDischarge: number
  sampleSize: number
}

export async function getIndibaSessionStats(patientId: number): Promise<IndibaSessionStats> {
  const res = await apiClient.get<IndibaSessionStats>(`/patients/${patientId}/indiba-stats`)
  return res.data
}

export async function getPathologyRehabStats(): Promise<PathologyRehabStats[]> {
  const res = await apiClient.get<PathologyRehabStats[]>('/statistics/rehabilitation-by-pathology')
  return res.data ?? []
}
